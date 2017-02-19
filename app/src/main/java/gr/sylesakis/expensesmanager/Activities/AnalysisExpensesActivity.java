package gr.sylesakis.expensesmanager.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gr.sylesakis.expensesmanager.Form.AnalysisForm;
import gr.sylesakis.expensesmanager.R;
import gr.sylesakis.expensesmanager.Services.DataBaseHelper;

public class AnalysisExpensesActivity extends AppCompatActivity {

    /******** Table Layout *****/
    private TextView ccNameText, ccDescText, ccNoExpText, ccTotalSumText;
    private TableLayout table;
    private TableRow tRow;

    /***************** Components ****************/
    private TextView startDateText, endDateText;

    /*************** Date Picker *****************/
    private int startYear = 0, endYear;
    private int startMonth, endMonth;
    private int startDay, endDay;
    private static final int START_DIALOG_ID = 0;
    private static final int END_DIALOG_ID = 1;

    private List<AnalysisForm> analist = new ArrayList<AnalysisForm>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_expenses);

        myInit();

        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startYear == 0) {
                    final Calendar calendar = Calendar.getInstance();
                    startYear = calendar.get(Calendar.YEAR);
                    startMonth = calendar.get(Calendar.MONTH);
                    startDay = 01;
                }
                showDialog(START_DIALOG_ID);
            }
        });

        endDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(END_DIALOG_ID);
            }
        });
    }

    private void myInit(){
        startDateText = (TextView) findViewById(R.id.analysisDateStartText);
        endDateText = (TextView) findViewById(R.id.analysisDateEndText);

        table = (TableLayout) findViewById(R.id.analysisTableLayout);
        table.setColumnStretchable(0,true); table.setColumnStretchable(1,true);
        table.setColumnStretchable(2,true); table.setColumnStretchable(3,true);

        //intialize end date text with current datetime
        final Calendar calendar = Calendar.getInstance();
        endYear = calendar.get(Calendar.YEAR);
        endMonth = calendar.get(Calendar.MONTH);
        endDay = calendar.get(Calendar.DAY_OF_MONTH);

        updateDatesText();
    }

    private void updateDatesText(){
        StringBuilder strdate = new StringBuilder();
        if(startYear != 0) {
            strdate.append(startYear).append("-");
            if(startMonth+1 <10){
                strdate.append("0").append(startMonth+1);
            }else{
                strdate.append(startMonth+1);
            }
            strdate.append("-");
            if(startDay < 10){
                strdate.append("0").append(startDay);
            }else{
                strdate.append(startDay);
            }
            startDateText.setText(strdate);
        }

        strdate = new StringBuilder();
        strdate.append(endYear).append("-");
        if(endMonth+1 <10){
            strdate.append("0").append(endMonth+1);
        }else{
            strdate.append(endMonth+1);
        }
        strdate.append("-");
        if(endDay < 10){
            strdate.append("0").append(endDay);
        }else{
            strdate.append(endDay);
        }
        endDateText.setText(strdate);
    }

    public void onClickSearchFunc(View view){
        clearRowsfromTable();
        if(checkValidations()){
            String startdate = startDateText.getText().toString();
            String enddate = endDateText.getText().toString();
            //call DataBase
            analist = DataBaseHelper.getSumAndGroupExpenses(this, startdate + " 00:00", enddate + " 23:59");
            if(analist != null) {
                FilltheTable();
            }else{
                Toast.makeText(getApplicationContext(),"NOTHING TO DISPLAY!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Invalid starting date!",Toast.LENGTH_SHORT).show();
        }
    }

    private void FilltheTable(){
        for(AnalysisForm item : analist){
            createtableText(item);
        }
    }

    private void createtableText(AnalysisForm item){
        tRow = new TableRow(this);
        ccNameText = new TextView(this);
        ccDescText = new TextView(this);
        ccNoExpText = new TextView(this);
        ccTotalSumText = new TextView(this);
        ccNameText.setText(item.getCcName());
        ccDescText.setText(item.getCcdDescription());
        ccNoExpText.setText(Integer.toString(item.getTotalExpenses()));
        ccTotalSumText.setText(item.getTotalSum());

        ccNameText.setGravity(Gravity.CENTER);
        ccDescText.setGravity(Gravity.CENTER);
        ccNoExpText.setGravity(Gravity.CENTER);
        ccTotalSumText.setGravity(Gravity.CENTER);

        tRow.addView(ccNameText);
        tRow.addView(ccDescText);
        tRow.addView(ccNoExpText);
        tRow.addView(ccTotalSumText);

        //style row
        tRow.setBackground(getResources().getDrawable(R.drawable.border_for_textview));
        table.addView(tRow);
    }

    private void clearRowsfromTable(){
        for(int i = 1; i<table.getChildCount();i++){
            View child = table.getChildAt(i);
            if(child instanceof TableRow){
                ((ViewGroup)child).removeAllViews();
            }
        }
    }

    private boolean checkValidations(){
        if(startYear == 0){
            return true;
        }else{
            Date sDate = new Date(startYear,startMonth-1,startDay);
            Date eDate = new Date(endYear,endMonth-1,endDay);
            if(sDate.compareTo(eDate) <= 0){
                return true;
            }
            return false;
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
                Intent analysisExpensesIntent = new Intent(this,AnalysisExpensesActivity.class);
                startActivity(analysisExpensesIntent);
                return true;
            case R.id.menuInspect:
                finish();
                Intent inspectionCostsIntent = new Intent(this,InspectionCostsActivity.class);
                startActivity(inspectionCostsIntent);
                return true;
            case R.id.menuAnalysis:
                recreate();
                return true;
            case R.id.menuExit:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private DatePickerDialog.OnDateSetListener startDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    startYear = year;
                    startMonth = monthOfYear;
                    startDay = dayOfMonth;
                    updateDatesText();
                }
            };

    private DatePickerDialog.OnDateSetListener endDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    endYear = year;
                    endMonth = monthOfYear;
                    endDay = dayOfMonth;
                    updateDatesText();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DIALOG_ID:
                return new DatePickerDialog(this,
                        startDateSetListener,
                        startYear, startMonth, startDay);
            case END_DIALOG_ID:
                return new DatePickerDialog(this,
                        endDateSetListener,
                        endYear, endMonth, endDay);
        }
        return null;
    }
}
