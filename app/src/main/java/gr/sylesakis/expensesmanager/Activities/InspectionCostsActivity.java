package gr.sylesakis.expensesmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gr.sylesakis.expensesmanager.Domain.CostCategory;
import gr.sylesakis.expensesmanager.Domain.Expense;
import gr.sylesakis.expensesmanager.Form.ExpenseForm;
import gr.sylesakis.expensesmanager.HashMapAdapter.CostCategoryAdapter;
import gr.sylesakis.expensesmanager.HashMapAdapter.DataToHashMap;
import gr.sylesakis.expensesmanager.R;
import gr.sylesakis.expensesmanager.Services.Amount;
import gr.sylesakis.expensesmanager.Services.DataBaseHelper;

public class InspectionCostsActivity extends AppCompatActivity implements OnMapReadyCallback {

    /******* Expandable List ********/
    private HashMap<CostCategory, List<Expense>> ccDetails;
    private List<CostCategory> cclist;
    private ExpandableListView expandlist;
    private CostCategoryAdapter adapter;

    /***** Model ****/
    private Expense expense;

    /*************** MAP ***************/
    private GoogleMap mMap;
    private final LatLng mDefaultLocation = new LatLng(-34, 151);
    private static final int  DEFAULT_ZOOM= 15;
    private LatLng mExpenseLocation;

    /********* AlertDialog **********/
    private AlertDialog.Builder dialog;
    private AlertDialog pointerDLG;
    private View dialogView;
    private LayoutInflater inflater;

    private Spinner dlgSpinner;
    private TextView dlgtitleText;
    private EditText dlgdescText;
    private EditText dlgpriceText;
    private TextView dlgSymbolText;
    private Button dlgUpdateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_costs);
        myInit();
    }

    private void myInit(){
        inflater = InspectionCostsActivity.this.getLayoutInflater();
        ExpandableListCode();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void onCreateMyDialog(){
        List<String> categorynames = new ArrayList<>();
        for (CostCategory item : cclist) {
            categorynames.add(item.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorynames);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        dialog = new AlertDialog.Builder(InspectionCostsActivity.this);

        dialogView = inflater.inflate(R.layout.update_expense_dialog, null);
        dlgSpinner = (Spinner) dialogView.findViewById(R.id.dialogSpinner);
        dlgSpinner.setAdapter(adapter);
        dlgSpinner.setSelection(categorynames.indexOf(expense.getCostcategory().getTitle()));

        dlgtitleText = (TextView) dialogView.findViewById(R.id.dialogTitleText);
        dlgdescText = (EditText) dialogView.findViewById(R.id.dialogDescription);
        dlgpriceText = (EditText) dialogView.findViewById(R.id.dialogPrice);
        dlgUpdateButton = (Button) dialogView.findViewById(R.id.dialogUpdate);
        dlgSymbolText = (TextView) dialogView.findViewById(R.id.dialogPriceSymbol);
        dlgSymbolText.setText(Amount.getSymbol());

        dlgUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!dlgSpinner.getSelectedItem().toString().equals(expense.getCostcategory().getTitle())
                        || !dlgdescText.getText().toString().equals(expense.getDescription())
                        || !dlgpriceText.getText().toString().equals(expense.getAmount())){

                    ExpenseForm tmpExpF = new ExpenseForm();
                    tmpExpF.setCategory_ID(expense.getCostcategory().getID());
                    for (CostCategory item : cclist) {
                        if(item.getTitle().equals(dlgSpinner.getSelectedItem().toString())){
                            tmpExpF.setCategory_ID(item.getID());
                        }
                    }
                    tmpExpF.setDescription(dlgdescText.getText().toString());
                    tmpExpF.setPrice(dlgpriceText.getText().toString());
                    //update to database
                    if(DataBaseHelper.updateExpense(InspectionCostsActivity.this, expense.getID(), tmpExpF)){
                        Toast.makeText(getApplicationContext(),"Update!!",Toast.LENGTH_SHORT).show();
                        pointerDLG.cancel();
                        // reload the  activity
                        recreate();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"No change detected",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void ExpandableListCode(){
        expandlist = (ExpandableListView) findViewById(R.id.explist);
        ccDetails = DataToHashMap.getInfo(DataBaseHelper.getAllCostCategories(this));
        cclist = new ArrayList<CostCategory>(ccDetails.keySet());
        adapter = new CostCategoryAdapter(this, ccDetails, cclist);
        expandlist.setAdapter(adapter);
        expandlist.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);

        expandlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
                expense = new Expense(adapter.getChild(groupPosition,childPosition));
                showItInMap();
                return true;
            }
        });
    }

    private void showItInMap(){
        getLocationFromExpense();
        generateMap();
    }

    private void getLocationFromExpense(){
        if(expense != null){
            double lat = Double.parseDouble(expense.getLocation().getLocLat());
            double lng = Double.parseDouble(expense.getLocation().getLocLng());
            mExpenseLocation = new LatLng(lat, lng);
        }else{
            mExpenseLocation = mDefaultLocation;
        }
    }

    public void onClickEditExpense(View view){
        if(expense != null) {
            onCreateMyDialog();

            dlgdescText.setText(expense.getDescription());
            dlgpriceText.setText(expense.getAmount());

           // dialog.setIcon(R.drawable.ic_search);
            dialog.setView(dialogView);
            AlertDialog dlg = dialog.create();
            this.pointerDLG = dlg;
            dlg.show();
        }else{
            Toast.makeText(getApplicationContext(),"Please select a expense.",Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCancelInspection(View view){
        finish();
    }

    public void onClickDeleteExpense(View view) {
        if(expense != null) {
            //delete Expense
            if (DataBaseHelper.deleteExpenseByID(this, expense.getID())) {
                Toast.makeText(getApplicationContext(), "The expense deleted successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to delete the expense.", Toast.LENGTH_SHORT).show();
            }
            // reload the  activity
            recreate();
        }else{
            Toast.makeText(getApplicationContext(),"Please select a expense",Toast.LENGTH_SHORT).show();
        }
    }

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
                finish();
                Intent addExpenseIntent = new Intent(this,AddExpenseActivity.class);
                startActivity(addExpenseIntent);
                return true;
            case R.id.menuInspect:
                recreate();
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

    private void generateMap(){
        mMap.clear();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mExpenseLocation).zoom(DEFAULT_ZOOM).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions()
                .position(mExpenseLocation)
                .title(expense.getLocation().getLocName())
                .snippet(expense.toString()));
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //load the map
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0,0) , 0.0f));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // null ώστε να κληθεί μετά η getInfoContents().
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Άνοιξε την διάταξη για το
                // παράθυρο πληροφορίας
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int zoom = (int)mMap.getCameraPosition().zoom;
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition
                        .fromLatLngZoom(new LatLng(mExpenseLocation.latitude + (double)90/Math.pow(2, zoom), mExpenseLocation.longitude), zoom)));
                marker.showInfoWindow();
                return true;
            }
        });

    }
}
