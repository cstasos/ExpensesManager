package gr.sylesakis.expensesmanager.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import gr.sylesakis.expensesmanager.Domain.CostCategory;
import gr.sylesakis.expensesmanager.Domain.Expense;
import gr.sylesakis.expensesmanager.Domain.MyLocation;
import gr.sylesakis.expensesmanager.Form.AnalysisForm;
import gr.sylesakis.expensesmanager.Form.CostCategoryForm;
import gr.sylesakis.expensesmanager.Form.ExpenseForm;
import gr.sylesakis.expensesmanager.Form.LocationForm;

/**
 * Created by SakaroS on 20/12/2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String COST_CATEGORY_DB = "CREATE TABLE COST_CATEGORY ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "Category_Title TEXT NOT NULL UNIQUE, "
            + "Category_Description TEXT"
            + ");";

    private static final String LOCATION_DB = "CREATE TABLE LOCATION ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "Place_Name TEXT, "
            + "Latitude TEXT NOT NULL, "
            + "Longitude TEXT NOT NULL"
            + ");";

    private static final String EXPENSE_DB = "CREATE TABLE EXPENSE ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "Category_id INTEGER NOT NULL, "
            + "Price REAL NOT NULL,"
            + "ExpDescription TEXT,"
            + "DateTime DATE NOT NULL,"
            + "Location_id INTEGER NOT NULL,"
            + "FOREIGN KEY(Category_id) REFERENCES COST_CATEGORY(_id) ON DELETE CASCADE,"
            + "FOREIGN KEY(Location_id) REFERENCES LOCATION(_id)"
            + ");";

    private static final String DATABASE_NAME = "ExpenseManagerDB";
    private static final int DATABASE_VERSION = 1;


    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COST_CATEGORY_DB);
        db.execSQL(LOCATION_DB);
        db.execSQL(EXPENSE_DB);

        //Default location
        db.execSQL("INSERT INTO LOCATION ("
                + "Place_Name,"
                + "Latitude, "
                + "Longitude) Values ("
                + "'Default Location Australia', '-34.0000', '151.0000')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldverion, int newversion) {
        if(oldverion < 1){
            this.onCreate(db);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Enable foreign key
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }


    /**************************** COST CATEGORY ****************************/

    public static boolean addNewCostCategory(Context context, CostCategoryForm ccF){
        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getWritableDatabase();
        ContentValues ccFValues = new ContentValues();
        ccFValues.put("Category_Title", ccF.getTitle());
        ccFValues.put("Category_Description", ccF.getDescription());
        try{
            db.insertOrThrow("COST_CATEGORY", null, ccFValues);
            return true;
        } catch (SQLiteConstraintException ex) {
            Log.e("Unique Field",ex.getMessage());
            return false;
        } catch (SQLiteException e){
            return false;
        } finally {
            db.close();
        }
    }

    public static List<CostCategory> getAllCostCategories(Context context){
        List<CostCategory> list = new ArrayList<CostCategory>();

        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getReadableDatabase();
        Cursor cursor = db.query("COST_CATEGORY",
                new String[] {"_id", "Category_Title", "Category_Description"},
                null, null,
                null, null, null);
        try{
            while(cursor.moveToNext()){
                CostCategory tmpCC = new CostCategory();
                tmpCC.setID(cursor.getInt(0));
                tmpCC.setTitle(cursor.getString(1));
                tmpCC.setDescription(cursor.getString(2));
                tmpCC.setExpenseList(DataBaseHelper.getAllCostCategoryByIDExpensies(context, tmpCC.getID()));
                list.add(tmpCC);
            }
            return list;
        } catch (SQLiteException e){
            return null;
        } finally {
            db.close();
            cursor.close();
        }
    }

    public static CostCategory getCategoryByID(Context context, int id){

        CostCategory tmpCC = new CostCategory();

        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getReadableDatabase();
        Cursor cursor = db.query("COST_CATEGORY",
                new String[] {"_id", "Category_Title", "Category_Description"},
                "_id = ?", new String[] {Integer.toString(id)},
                null, null, null);
        try{
            if(cursor.moveToFirst()){
                tmpCC.setID(cursor.getInt(0));
                tmpCC.setTitle(cursor.getString(1));
                tmpCC.setDescription(cursor.getString(2));
            }
            return tmpCC;
        } catch (SQLiteException e){
            return null;
        } finally {
            db.close();
            cursor.close();
        }
    }

    public static CostCategory getCategoryByTitle(Context context, String title){
        CostCategory tmpCC = new CostCategory();

        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getReadableDatabase();
        Cursor cursor = db.query("COST_CATEGORY",
                new String[] {"_id", "Category_Title", "Category_Description"},
                "Category_Title = ?", new String[] {title},
                null, null, null);
        try{
            if(cursor.moveToFirst()){
                tmpCC.setID(cursor.getInt(0));
                tmpCC.setTitle(cursor.getString(1));
                tmpCC.setDescription(cursor.getString(2));
            }
            return tmpCC;
        } catch (SQLiteException e){
            return null;
        } finally {
            db.close();
            cursor.close();
        }
    }

    public static boolean deleteCostCategorybyID(Context context, int id){
        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getWritableDatabase();

        int value = db.delete("COST_CATEGORY",
                "_id = ?",
                new String[]{Integer.toString(id)});

        if(value > 0){
            return true;
        }else{
            return false;
        }
    }

    /***************************** LOCATION ***************************/

    public static boolean addNewLocation(Context context, LocationForm lcF){
        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getWritableDatabase();
        ContentValues lcFValues = new ContentValues();
        lcFValues.put("Place_Name", lcF.getLocationName());
        lcFValues.put("Latitude", lcF.getLocLatitude());
        lcFValues.put("Longitude", lcF.getLocLongtitude());
        try{
            db.insertOrThrow("LOCATION", null, lcFValues);
            return true;
        } catch (SQLiteException e){
            return false;
        } finally {
            db.close();
        }
    }

    public static int getLocationIDByLatLong(Context context, String lat, String lng){
        int loc_id = 1;

        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getReadableDatabase();
        Cursor cursor = db.query("LOCATION",
                new String[] {"_id"},
                "Latitude = ? AND Longitude = ?", new String[] {lat,lng},
                null, null, null);
        try{
            if(cursor.moveToFirst()){
                loc_id = cursor.getInt(0);
                Log.d("TABLE -> LOCATION ","Func {getLocationIDByLatLong} "+loc_id);
            }
        } catch (SQLiteException e){
            Log.e("TABLE -> LOCATION ","Func {getLocationIDByLatLong}");
        } finally {
            db.close();
            cursor.close();
        }

        return loc_id;
    }

    public static MyLocation getLocationByLatLong(Context context, String lat, String lng){
        MyLocation loc = null;

        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getReadableDatabase();
        Cursor cursor = db.query("LOCATION",
                new String[] {"_id", "Place_Name", "Latitude", "Longitude"},
                "Latitude = ? AND Longitude = ?", new String[] {lat,lng},
                null, null, null);
        try{
            if(cursor.moveToFirst()){
                loc = new MyLocation();
                loc.setID(cursor.getInt(0));
                loc.setLocName(cursor.getString(1));
                loc.setLocLat(cursor.getString(2));
                loc.setLocLng(cursor.getString(3));
            }
            return loc;
        } catch (SQLiteException e){
            Log.e("TABLE -> LOCATION ","Func {getLocationByLatLong}");
            return null;
        } finally {
            db.close();
            cursor.close();
        }
    }

    public static MyLocation getLocationByID(Context context, int ID){
        MyLocation loc = new MyLocation();

        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getReadableDatabase();
        Cursor cursor = db.query("LOCATION",
                new String[] {"_id", "Place_Name", "Latitude", "Longitude"},
                "_id = ?", new String[] {Integer.toString(ID)},
                null, null, null);
        try{
            if(cursor.moveToFirst()){
                loc.setID(cursor.getInt(0));
                loc.setLocName(cursor.getString(1));
                loc.setLocLat(cursor.getString(2));
                loc.setLocLng(cursor.getString(3));
            }
            return loc;
        } catch (SQLiteException e){
            return null;
        } finally {
            db.close();
            cursor.close();
        }
    }

    public static void updateLocationName(Context context, int id, String name){
        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getReadableDatabase();

        ContentValues locnameValue = new ContentValues();
        locnameValue.put("Place_Name",name);
        db.update("LOCATION", locnameValue,
                  "_id = ?", new String[]{Integer.toString(id)});

        db.close();
    }

    /******************************* EXPENSE **********************************/

    public static boolean addNewExpense(Context context, ExpenseForm expF){
        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getWritableDatabase();
        ContentValues expFValues = new ContentValues();
        expFValues.put("Category_id", expF.getCategory_ID());
        expFValues.put("Price",expF.getPrice());
        expFValues.put("ExpDescription", expF.getDescription());
        expFValues.put("DateTime", expF.getDatetime());
        expFValues.put("Location_id", expF.getLocation_ID());
        try{
            db.insertOrThrow("EXPENSE", null, expFValues);
            return true;
        } catch (SQLiteException e){
            return false;
        } finally {
            db.close();
        }
    }

    public static List<Expense> getAllCostCategoryByIDExpensies(Context context, int ccID){
        List<Expense> explist = new ArrayList<>();
        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getReadableDatabase();

        Cursor cursor = db.query("EXPENSE",
                new String[] {"_id", "Price", "expDescription", "DateTime", "Location_id"},
                "Category_id = ?", new String[] {Integer.toString(ccID)},
                null, null, null);

        try{
            while(cursor.moveToNext()){
                Expense tmpExp = new Expense();
                tmpExp.setCostcategory(DataBaseHelper.getCategoryByID(context,ccID));
                tmpExp.setID(cursor.getInt(0));
                String value = String.format("%.2f",cursor.getDouble(1)).replace(",", ".");
                tmpExp.setAmount(value);
                tmpExp.setDescription(cursor.getString(2));
                tmpExp.setDatetime(cursor.getString(3));
                tmpExp.setLocation(DataBaseHelper.getLocationByID(context, cursor.getInt(4)));
                explist.add(tmpExp);
            }
            return explist;
        } catch (SQLiteException e){
            return null;
        } finally {
            db.close();
            cursor.close();
        }
    }

    public static List<AnalysisForm> getSumAndGroupExpenses(Context context, String startDate, String endDate){
        List<AnalysisForm> analist = new ArrayList<>();
        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getReadableDatabase();

        Cursor cursor = db.query("EXPENSE",
                new String[] {"Category_id", "count(_id)", "sum(Price)"},
                "DateTime BETWEEN ? AND ?", new String[] {startDate, endDate},
                "Category_id", null, "sum(Price)" + " DESC");

        try{
            while(cursor.moveToNext()){
                AnalysisForm tmpAnaF = new AnalysisForm();
                CostCategory cc = DataBaseHelper.getCategoryByID(context, cursor.getInt(0));
                tmpAnaF.setCcName(cc.getTitle());
                tmpAnaF.setCcdDescription(cc.getDescription());
                tmpAnaF.setTotalExpenses(cursor.getInt(1));
                String value = String.format("%.2f",cursor.getDouble(2)).replace(",", ".");
                tmpAnaF.setTotalSum(value);

                analist.add(tmpAnaF);
            }

        } catch (SQLiteException e){
            Log.e("Func {getSumAndGroupExpenses}", e.getMessage());
            return null;
        } finally {
            db.close();
            cursor.close();
        }
        if(analist.isEmpty()){
            return null;
        }
        return analist;
    }

    public static boolean updateExpense(Context context, int expID, ExpenseForm expF){
        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getWritableDatabase();

        ContentValues expValues = new ContentValues();
        expValues.put("Category_id", expF.getCategory_ID());
        expValues.put("Price", expF.getPrice());
        expValues.put("ExpDescription", expF.getDescription());

        int value = db.update("EXPENSE", expValues,
                "_id = ?", new String[]{Integer.toString(expID)});
        if(value > 0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean deleteExpenseByID(Context context, int ID){
        SQLiteOpenHelper expmanHelper = new DataBaseHelper(context);
        SQLiteDatabase db = expmanHelper.getWritableDatabase();

        int value = db.delete("EXPENSE",
                "_id = ?",
                new String[]{Integer.toString(ID)});

        if(value > 0){
            return true;
        }else{
            return false;
        }
    }
}
