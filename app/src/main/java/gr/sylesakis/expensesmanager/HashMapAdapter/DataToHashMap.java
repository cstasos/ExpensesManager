package gr.sylesakis.expensesmanager.HashMapAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gr.sylesakis.expensesmanager.Domain.CostCategory;
import gr.sylesakis.expensesmanager.Domain.Expense;

/**
 * Created by SakaroS on 25/12/2016.
 */

public class DataToHashMap {

    public static HashMap<CostCategory, List<Expense>> getInfo(List<CostCategory> cclist){
        HashMap<CostCategory, List<Expense>> ccDetails = new HashMap<CostCategory, List<Expense>>();

        for(CostCategory tmpCC : cclist){
            List<Expense> explist = new ArrayList<>();
            for(Expense tmpExp : tmpCC.getExpenseList()){
                explist.add(tmpExp);
            }
            ccDetails.put(tmpCC,explist);
        }
        return ccDetails;
    }
}
