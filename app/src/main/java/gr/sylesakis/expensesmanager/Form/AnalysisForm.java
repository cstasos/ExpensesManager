package gr.sylesakis.expensesmanager.Form;

import gr.sylesakis.expensesmanager.Services.Amount;

/**
 * Created by SakaroS on 28/12/2016.
 */

public class AnalysisForm {

    private String ccName;
    private String ccdDescription;
    private String totalSum;
    private int totalExpenses;

    public AnalysisForm() {}

    public AnalysisForm(String ccName, String ccdDescription, String totalSum, int totalExpenses) {
        this.ccName = new String(ccName);
        this.ccdDescription = new String(ccdDescription);
        this.totalSum = new String(totalSum);
        this.totalExpenses = totalExpenses;
    }

    public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = new String(ccName);
    }

    public String getCcdDescription() {
        return ccdDescription;
    }

    public void setCcdDescription(String ccdDescription) {
        this.ccdDescription = new String(ccdDescription);
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = new String(totalSum) + Amount.getSymbol();
    }

    public int getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(int totalExpenses) {
        this.totalExpenses = totalExpenses;
    }


}
