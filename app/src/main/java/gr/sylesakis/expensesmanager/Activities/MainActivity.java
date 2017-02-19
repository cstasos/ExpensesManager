package gr.sylesakis.expensesmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import gr.sylesakis.expensesmanager.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                recreate();
                return true;
            case R.id.menuAddCostCategory:
                addCategoryFunc(null);
                return true;
            case R.id.menuAddExpense:
                addExpenseFunc(null);
                return true;
            case R.id.menuInspect:
                inspectionCostFunc(null);
                return true;
            case R.id.menuAnalysis:
                analysisExpenseFunc(null);
                return true;
            case R.id.menuExit:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addCategoryFunc(View view) {
        Intent addCategoryIntent = new Intent(this,AddCategoryActivity.class);
        startActivity(addCategoryIntent);
    }

    public void addExpenseFunc(View view) {
        Intent addExpenseIntent = new Intent(this,AddExpenseActivity.class);
        startActivity(addExpenseIntent);
    }

    public void inspectionCostFunc(View view) {
        Intent inspectionCostsIntent = new Intent(this,InspectionCostsActivity.class);
        startActivity(inspectionCostsIntent);
    }

    public void analysisExpenseFunc(View view) {
        Intent analysisExpensesIntent = new Intent(this,AnalysisExpensesActivity.class);
        startActivity(analysisExpensesIntent);
    }
}
