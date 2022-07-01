package com.orpatservice.app.ui.leads.new_lead_fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.orpatservice.app.R;
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.TaskEnquiry;
import com.orpatservice.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class WarrantyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TaskEnquiry> list;
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public WarrantyListAdapter(Context context , List<TaskEnquiry> list) {
        this.context = context;
        this.list = list;
       // notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if(viewType == TYPE_HEADER)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complete_task, parent, false);
            return  new VHHeader(v);
        }
        else if(viewType == TYPE_ITEM)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parts_task_completed, parent, false);
            return new VHItem(v);
        }
        else if(viewType==TYPE_FOOTER)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_footer, parent, false);
            return new VHFooter(v);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }
    private TaskEnquiry getItem(int position)
    {
        return list.get(position);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VHHeader)
        {
            TaskEnquiry currentItem = getItem(position);
            VHHeader VHitem = (VHHeader)holder;

            VHitem.txtTitle.setText(list.get(position).getModel_no());
            VHitem.tv_warranty.setText(list.get(position).getIn_warranty());
        }
        else if(holder instanceof VHItem)
        {
            VHItem VHitem = (VHItem)holder;
            VHitem.tv_warranty_parts.setText(list.get(position).getModel_no());
            // VHitem.iv.setBackgroundResource(currentItem.getId());
        }
        else if (holder instanceof  VHFooter)
        {

        }
    }

    class VHHeader extends RecyclerView.ViewHolder{
        TextView txtTitle;
        TextView tv_warranty;
        public VHHeader(View itemView) {
            super(itemView);
            this.txtTitle = (TextView)itemView.findViewById(R.id.tv_task_completed_model_name_value);
            this.tv_warranty = (TextView)itemView.findViewById(R.id.tv_task_completed_warranty_date_year);
        }
    }

    class VHItem extends RecyclerView.ViewHolder{

        TextView tv_warranty_parts;
        public VHItem(View itemView) {
            super(itemView);
            this.tv_warranty_parts = (TextView)itemView.findViewById(R.id.tv_warranty_parts);
            //  this.iv = (ImageView)itemView.findViewById(R.id.ivListItem);
        }
    }

    class VHFooter extends RecyclerView.ViewHolder
    {

        public VHFooter(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return TYPE_HEADER;
        else if (position==list.size()+1)
            return  TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }
}