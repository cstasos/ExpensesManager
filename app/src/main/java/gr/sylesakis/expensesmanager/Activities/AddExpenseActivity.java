package gr.sylesakis.expensesmanager.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import gr.sylesakis.expensesmanager.Domain.CostCategory;
import gr.sylesakis.expensesmanager.Domain.MyLocation;
import gr.sylesakis.expensesmanager.Form.ExpenseForm;
import gr.sylesakis.expensesmanager.Form.LocationForm;
import gr.sylesakis.expensesmanager.R;
import gr.sylesakis.expensesmanager.Services.Amount;
import gr.sylesakis.expensesmanager.Services.DataBaseHelper;


public class AddExpenseActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int COST_CATEGORY_REQUEST = 1001;
    private static final long NEW_CATEGORY_ID = 1;

    private List<CostCategory> categories;
    private List<String> categorynames = new ArrayList<String>(Arrays.asList("Choose Category", "Add New"));

    /************** Components **********************/
    private Spinner spinnerView;
    //Location
    private EditText locnameText;
    private TextView loclatText;
    private TextView loclngText;
    //Expense
    private EditText expDescText;
    private EditText expPriceText;

    /*************** MODEL **************/
    private MyLocation myLoc = null;


    protected static final String TAG = "MainActivity";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    // Η τρέχουσα θέση της συσκευής
    private Location mCurrentLocation;
    // αν δεν δοθεί άδεια πρόσβασης στην τοποθεσία
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // Ένα αντικείμενο αίτησης θέσης με στόχο την αποθήκευση
    // παραμέτρων για τις αιτήσεις στο FusedLocationProviderApi.
    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // call GoogleApiClient()
        buildGoogleApiClient();

        myInit();
    }

    private void myInit() {
        /************** Components **********************/
        spinnerView = (Spinner) findViewById(R.id.expenseSpinner);
        //Location
        locnameText = (EditText) findViewById(R.id.locationName);
        loclatText = (TextView) findViewById(R.id.Lattext);
        loclngText = (TextView) findViewById(R.id.Lngtext);
        //Expense
        expDescText = (EditText) findViewById(R.id.expDescription);
        expPriceText = (EditText) findViewById(R.id.expPrice);
        // Symbol
        TextView symbol = (TextView) findViewById(R.id.currencySymbol);
        symbol.setText(Amount.getSymbol());

        fillSpinner();
    }

    private void checkLocationParam(String name, String lat, String lng){
        String tmp = new String();
        int dot;
        int sublen;

        if(lat.contains(".")){
            dot = lat.indexOf(".");
            tmp = lat.substring(dot+1, lat.length());
            sublen = lat.length() - (dot+1);
            if(sublen > 4){
                lat = lat.concat("*");
                tmp = lat.substring(dot+5, lat.length());
                lat = lat.replace(tmp,"");
            }
            else{
                for(int i = 4-sublen; i >= 1; i--){
                    lat=lat.concat("0");
                }
            }
        }else{
            lat = lat.concat(".0000");
        }

        tmp = new String();
        if(lng.contains(".")){
            dot = lng.indexOf(".");
            tmp = lng.substring(dot+1, lng.length());
            sublen = lng.length() - (dot+1);
            if(sublen > 4){
                lng = lng.concat("*");
                tmp = lng.substring(dot+5, lng.length());
                lng = lng.replace(tmp,"");
            }
            else{
                for(int i = 4-sublen; i >= 1; i--){
                    lng=lng.concat("0");
                }
            }
        }else{
            lng = lng.concat(".0000");
        }

        fillLocationName(name,lat,lng);

    }

    private void fillLocationName(String name, String lat, String lng){
        //call the get Location if Exits
        myLoc = DataBaseHelper.getLocationByLatLong(this, lat, lng);

        if(myLoc == null) {
            locnameText.setText(name);
            loclatText.setText("Latitude: " + lat);
            loclngText.setText("Longitude: " + lng);
        }else{
            locnameText.setText(myLoc.getLocName());
            loclatText.setText("Latitude: " + myLoc.getLocLat());
            loclngText.setText("Longitude: " + myLoc.getLocLng());
        }
    }

    private void fillSpinner() {
        //call getAllCostCategories()
        categories = DataBaseHelper.getAllCostCategories(this);
        for (CostCategory item : categories) {
            categorynames.add(item.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorynames);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerView.setAdapter(adapter);

        spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            Spinner sp = (Spinner) findViewById(R.id.expenseSpinner);
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == NEW_CATEGORY_ID) {
                    Intent addCategoryIntent = new Intent(AddExpenseActivity.this, AddCategoryActivity.class);
                    startActivityForResult(addCategoryIntent, COST_CATEGORY_REQUEST);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COST_CATEGORY_REQUEST) {
            if (resultCode == RESULT_OK) {
                String titleCategory = data.getStringExtra(getString(R.string.CostCategoryName));
                CostCategory tmpCC = DataBaseHelper.getCategoryByTitle(this, titleCategory);
                //add to list
                categorynames.add(tmpCC.getTitle());
                categories.add(tmpCC);
                // selected
                spinnerView.setSelection(categorynames.indexOf(tmpCC.getTitle()));
            }
        }
    }

    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private boolean CheckFields(){
        if(spinnerView.getSelectedItemPosition() < 2){
            //Error
            TextView spinStar = (TextView) findViewById(R.id.expSpinnerStar);
            spinStar.setTextColor(getResources().getColor(R.color.errorColor));
            showToast("Please select Cost Category!");
            return false;
        }

        if(expPriceText.getText().toString().equals("")){
            TextView euro = (TextView) findViewById(R.id.currencySymbol);
            euro.setTextColor(getResources().getColor(R.color.errorColor));
            showToast("Please input the price!");
            return false;
        }
        return true;
    }

    public void onClickRefreshLocFunc(View view){
        getDeviceLocation();
    }

    public void onClickSaveExpense(View view){
        if(CheckFields()) {
            int category_id = categories.get(spinnerView.getSelectedItemPosition() - 2).getID();
            LocationForm lcF = new LocationForm();
            int location_id = 1;
            boolean insertNewLocation = false;
            if (mLocationPermissionGranted) {
                if (myLoc == null) {
                    lcF.setLocationName(locnameText.getText().toString());
                    lcF.setLocLatitude(loclatText.getText().toString().replace("Latitude: ",""));
                    lcF.setLocLongtitude(loclngText.getText().toString().replace("Longitude: ",""));
                    insertNewLocation = DataBaseHelper.addNewLocation(this, lcF);
                } else {
                    location_id = myLoc.getID();
                    String locname = locnameText.getText().toString();
                    if(!myLoc.getLocName().equals(locname)){
                        DataBaseHelper.updateLocationName(this, location_id, locname);
                    }
                }
            }

            ExpenseForm expForm = new ExpenseForm();
            expForm.setCategory_ID(category_id);
            expForm.setDescription(expDescText.getText().toString());
            expForm.setPrice(expPriceText.getText().toString());
            expForm.setDatetime();

            if(insertNewLocation) {
                location_id = DataBaseHelper.getLocationIDByLatLong(this, lcF.getLocLatitude(), lcF.getLocLongtitude());
                expForm.setLocation_ID(location_id);
            } else {
                expForm.setLocation_ID(location_id);
                Log.e("Location", "Failed to insert location to DB");
            }

            if (DataBaseHelper.addNewExpense(this, expForm)) {
                showToast("The expense insert successfully!");
            } else {
                showToast("Expense failed to insert");
                Log.e("Expense", "Failed to insert Expense to DB");
            }
            finish();
        }
    }

    public void onClickExpenseCancel(View view) { finish(); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuMainMenu:
                finish();
                Intent mainIntent = new Intent(this,MainActivity.class);
                startActivity(mainIntent);
                return true;
            case R.id.menuAddCostCategory:
                finish();
                Intent addCategoryIntent = new Intent(this,AddCategoryActivity.class);
                startActivity(addCategoryIntent);
                return true;
            case R.id.menuAddExpense:
                recreate();
                return true;
            case R.id.menuInspect:
                finish();
                Intent inspectionCostsIntent = new Intent(this,InspectionCostsActivity.class);
                startActivity(inspectionCostsIntent);
                return true;
            case R.id.menuAnalysis:
                finish();
                Intent analysisExpensesIntent = new Intent(this,AnalysisExpensesActivity.class);
                startActivity(analysisExpensesIntent);
                return true;
            case R.id.menuExit:
                finish();
                System.exit(0);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /************* GoogleApiClient ***********/
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Η σύνδεση διακόπηκε");
        mGoogleApiClient.connect();
    }

    /**
     * Ανάκτησε την τρέχουσα θέση της συσκευής και
     * κάνε εγγραφή για το callback της μεθόδου onMapReady
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        getDeviceLocation();
    }

    /**
     * Χειρίζεται την αποτυχία σύνδεσης με τον Google Play services client
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    /**
     * Καλείται όταν αλλάζει η θέση της συσκευής.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    /**
     * Δημιουργεί τον GoogleApiClient.
     * Χρησιμοποιεί την μέθοδο addApi() για να αιτηθεί πρόσβαση
     * στο Google Places API και στον Fused Location Provider.
     */
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Θέτει το αίτημα τοποθεσίας.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Ανακτά την τρέχουσα τοποθεσία της συσκευής και ξεκινά
     * τις ειδοποιήσεις για ενημερώσεις τοποθεσίας
     */
    private void getDeviceLocation() {
        /*
         * Ζήτα την άδεια για ανάκτηση τοποθεσίας, έτσι ώστε να μπορέσουμε
         * να ανακτήσουμε την τοποθεσία της συσκευής. Το αποτέλεσμα της αίτησης
         * θα το χειριστεί σε μία κλήση προς τα πίσω (callback) η μέθοδος
         * onRequestPermissionsResult την οποία θα πρέπει να υλοποιήσουμε
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         *
         * Ανάκτησε την καλύτερη και πιο πρόσφατη
         * τοποθεσία της συσκευής, που μπορεί να είναι
         * null αν η τοποθεσία δεν είναι διαθέσιμη.
         * Επίσης ζήτησε τακτικές ενημερώσεις για την
         * τοποθεσία της συσκευής.
         */
        if (mLocationPermissionGranted) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
            double latitude = 0.0;
            double longitude = 0.0;
            String name = null;
            try{
                latitude = mCurrentLocation.getLatitude();
                longitude = mCurrentLocation.getLongitude();
                name = getAddress(latitude,longitude);
            } catch(NullPointerException e){
                latitude = -34.0000;
                longitude = 151.0000;
                name = "Default Location Australia";
            }finally{
                checkLocationParam(name,Double.toString(latitude),Double.toString(longitude));
            }

        } else {
            checkLocationParam("Default Location Australia","-34.0000","151.0000");
        }
    }

    /**
     * Μέθοδος χειρισμού του αποτελέσματος της αίτησης
     * για άδεια τοποθεσίας.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // Αν το αίτημα ακυρώθηκε, το array grantResults
                // είναι κενό.
                if (grantResults.length > 0
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    this.getDeviceLocation();
                }
            }
        }
    }

    private String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String add = null;
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses.size() > 0) {
                android.location.Address obj = addresses.get(0);
                add = obj.getLocality();
                add = add + ", " + obj.getThoroughfare();
                add = add + ", " + obj.getFeatureName();

                Log.v("IGA", "Address" + add);
            }else {
                add = "No location name detected";
            }
            return add;
        } catch (IOException e) {
            // Failed to locate the name
            add = "No location name detected";
            return add;

        }

    }
}
