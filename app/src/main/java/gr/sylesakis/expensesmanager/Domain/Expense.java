package gr.sylesakis.expensesmanager.Domain;


import java.text.SimpleDateFormat;

import gr.sylesakis.expensesmanager.Services.Amount;

/**
 * Created by SakaroS on 10/12/2016.
 */

public class Expense {
    private int ID;

    private String description = null;
    private String datetime;
    private Amount amount;
    private CostCategory costcategory;
    private MyLocation location;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public Expense() {
        this.amount = new Amount();
        this.location = new MyLocation();
    }

    public Expense(int ID, String description, String datetime, String price, CostCategory costcategory, MyLocation location) {
        this.ID = ID;
        this.description = description;
        this.datetime = datetime;
        this.amount = new Amount(price);
        this.costcategory = new CostCategory(costcategory);
        this.location = new MyLocation(location);
    }

    public Expense(Expense newExp) {
        this.ID = newExp.getID();
        this.description = new String(newExp.getDescription());
        this.datetime = new String(newExp.getDatetime());
        this.amount = new Amount(newExp.getAmount());
        this.costcategory = new CostCategory(newExp.getCostcategory());
        this.location = new MyLocation(newExp.getLocation());
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {this.description = new String(description);
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAmount() {
        return amount.getPrice();
    }

    public void setAmount(String price) {
        this.amount.setPrice(price);
    }

    public CostCategory getCostcategory() {
        return costcategory;
    }

    public void setCostcategory(CostCategory costcategory) {
        this.costcategory = new CostCategory(costcategory);
    }

    public MyLocation getLocation() {
        return location;
    }

    public void setLocation(MyLocation location) {
        this.location = location;
    }

    public String shortListDetails() {
        return this.datetime + " " + this.amount.toString()
                + " Desc: " + this.description;
    }

    public String showDetails(){
        return "Cost Category: " + this.costcategory.getTitle() + " Date & Hour: " + this.datetime
                + " Description: " + (this.description == null ? "No value" : this.description)
                + " Price: " + this.amount.toString();
    }

    @Override
    public String toString() {
        return "Cost Category: " + costcategory.getTitle() + "\n"
                + "Description: " + description + "\n"
                + "Price: " + amount.toString() + "\n"
                + "DateTime: " + datetime;
    }

}
