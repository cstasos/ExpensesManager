package gr.sylesakis.expensesmanager.HashMapAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import gr.sylesakis.expensesmanager.Domain.CostCategory;
import gr.sylesakis.expensesmanager.Domain.Expense;
import gr.sylesakis.expensesmanager.R;

/**
 * Created by SakaroS on 25/12/2016.
 */

public class CostCategoryAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<CostCategory, List<Expense>> ccDetails;
    private List<CostCategory> cclist;

    public CostCategoryAdapter(Context context, HashMap<CostCategory, List<Expense>> ccDetails, List<CostCategory> cclist) {
        this.context = context;
        this.ccDetails = ccDetails;
        this.cclist = cclist;
    }

    @Override
    public Expense getChild(int parent, int child) {
        return ccDetails.get(cclist.get(parent)).get(child);
    }

    @Override
    public int getGroupCount() {
        return cclist.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return ccDetails.get(cclist.get(i)).size();
    }

    @Override
    public CostCategory getGroup(int parent) {
        return cclist.get(parent);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int parent, int child) {
        return child;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int parent, boolean isExpanded, View convertview, ViewGroup parentview) {
        String group_title = "Category: " + getGroup(parent).getTitle() + " ("+ getChildrenCount(parent) +")";
        if(convertview == null){
            LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertview = inflator.inflate(R.layout.parent_layout, parentview,false);
        }
        TextView parent_textview = (TextView) convertview.findViewById(R.id.parent_txt);
        parent_textview.setTypeface(null, Typeface.BOLD);
        parent_textview.setText(group_title);

        return convertview;
    }

    @Override
    public View getChildView(int parent, int child, boolean lastChild, View convertview, ViewGroup parentview){
        String child_title =  child+1 + ": "+getChild(parent, child).shortListDetails();
        if(convertview == null){
            LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertview = inflator.inflate(R.layout.child_layout, parentview,false);
        }
        TextView child_textview = (TextView) convertview.findViewById(R.id.child_txt);
        child_textview.setText(child_title);

        return convertview;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
