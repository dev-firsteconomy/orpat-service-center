package com.orpatservice.app.ui.leads.technician.section;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.orpatservice.app.R;

public class ChildViewHolder extends RecyclerView.ViewHolder {

    TextView name;

    public ChildViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.tv_warranty_parts);
    }
}