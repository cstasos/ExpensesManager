package gr.sylesakis.expensesmanager.Form;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SakaroS on 22/12/2016.
 */

public class ExpenseForm {

    private int Category_ID;
    private int Location_ID;

    private String Description = null;
    private String datetime;
    private String price;

    public ExpenseForm() { }

    public int getCategory_ID() {
        return Category_ID;
    }

    public void setCategory_ID(int category_ID) {
        Category_ID = category_ID;
    }

    public int getLocation_ID() {
        return Location_ID;
    }

    public void setLocation_ID(int location_ID) {
        Location_ID = location_ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        this.datetime = sdf.format(new Date());
    }

    public String getPrice() { return price;}

    public void setPrice(String price) {
        if(price.equals("")){
            this.price = "0.00";
        }else{
            this.price = price;
        }
    }
}
