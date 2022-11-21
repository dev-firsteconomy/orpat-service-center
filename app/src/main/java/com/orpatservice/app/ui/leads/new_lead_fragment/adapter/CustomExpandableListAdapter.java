package com.orpatservice.app.ui.leads.new_lead_fragment.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.orpatservice.app.R;
import com.orpatservice.app.data.model.requests_leads.WarrantryPart;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<WarrantryPart> expandableListTitle;
    private  TextView tv_not_covered_condition;
    private  ExpandableListView expandableListView;
   // private HashMap<String, List<String>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<WarrantryPart> expandableListTitle, ExpandableListView expandableListView
                                       ) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListView = expandableListView;
      //  this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {

       // return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
        return  expandableListTitle.get(listPosition).getWarranty_conditions().get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
      //  final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adapter_serviceable_warranty_child_parts, null);

            TextView expandedListTextView = (TextView) convertView
                    .findViewById(R.id.tv_serviceable_warranty_child_parts_name);

           /* TextView footer = new TextView(context);
            footer.setGravity(Gravity.CENTER);
            footer.setTextSize(15);
            footer.setText("Footer");
            expandableListView.addFooterView(footer);*/
            expandedListTextView.setText("-" + "   " + expandableListTitle.get(listPosition).getWarranty_conditions().get(expandedListPosition).getTitle());
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return  expandableListTitle.get(listPosition).getWarranty_conditions().size();
        //return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        //Log.d("getGroup(listPosition)", ""+ expandableListTitle.get(listPosition));
        //String listTitle = (String) getGroup(listPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.adapter_serviceable_warranty_parts, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.tv_serviceable_warranty_parts_name);
        tv_not_covered_condition = (TextView) convertView
                .findViewById(R.id.tv_not_covered_condition);
        ImageView  down_iv = (ImageView) convertView
                .findViewById(R.id.down_iv);

        listTitleTextView.setTypeface(null, Typeface.BOLD);

        if(expandableListTitle.get(listPosition).getWarranty_conditions().size() == 0){
            tv_not_covered_condition.setVisibility(GONE);
            down_iv.setVisibility(GONE);
        }else{
            tv_not_covered_condition.setVisibility(View.VISIBLE);

            down_iv.setVisibility(View.VISIBLE);
        }


        listTitleTextView.setText(expandableListTitle.get(listPosition).getName());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {

        return true;
    }
}