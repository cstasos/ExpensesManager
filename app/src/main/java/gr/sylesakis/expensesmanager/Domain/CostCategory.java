package gr.sylesakis.expensesmanager.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SakaroS on 8/12/2016.
 */

public class CostCategory {
    private int ID;
    private String title;
    private String description = null;

    private List<Expense> explist;

    public CostCategory() { }

    public CostCategory(String description, String title, int ID) {
        this.description = description;
        this.title = title;
        this.ID = ID;
    }

    public CostCategory(CostCategory newCC) {
        this.description = new String(newCC.getDescription());
        this.title = new String(newCC.getTitle());
        this.ID = newCC.getID();
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = new String(title);
    }

    public void setDescription(String description) {
        this.description = new String(description);
    }

    public void setExpenseList(List<Expense> explist){
        this.explist = new ArrayList<>();
        for(Expense ex: explist){
            this.explist.add(ex);
        }
    }

    public List<Expense> getExpenseList(){
        return this.explist;
    }

    @Override
    public String toString() {
        return "CostCategory{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", EXPLIST="+explist +
                '}';
    }
}
