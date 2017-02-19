package gr.sylesakis.expensesmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import gr.sylesakis.expensesmanager.Form.CostCategoryForm;
import gr.sylesakis.expensesmanager.R;
import gr.sylesakis.expensesmanager.Services.DataBaseHelper;

public class AddCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
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
                recreate();
                return true;
            case R.id.menuAddExpense:
                finish();
                Intent addExpenseIntent = new Intent(this,AddExpenseActivity.class);
                startActivity(addExpenseIntent);
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

    public void saveCategorytoDB(View view) {
        EditText title_text = (EditText) findViewById(R.id.title_category_text);
        EditText description_text = (EditText) findViewById(R.id.description_category_text);

        CostCategoryForm ccF = new CostCategoryForm();
        ccF.setTitle(title_text.getText().toString());
        ccF.setDescription(description_text.getText().toString());

        if(DataBaseHelper.addNewCostCategory(this,ccF)){
            Toast.makeText(getApplicationContext(),"Insert a cost category successfully!",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.putExtra(getString(R.string.CostCategoryName), title_text.getText().toString());
            setResult(RESULT_OK, intent);
        }else{
            Toast.makeText(getApplicationContext(),"Failed to insert a cost category.",Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    public void CancelFunc(View view) {
        finish();
    }
}
