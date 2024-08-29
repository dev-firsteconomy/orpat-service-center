package com.orpatservice.app.ui.leads.technician.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.orpatservice.app.R;
import com.orpatservice.app.data.model.requests_leads.WarrantryPart;
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.LeadList;
import com.orpatservice.app.ui.leads.new_lead_fragment.new_lead_request.WarrantyList;
import com.orpatservice.app.ui.leads.technician.section.SectionedRecyclerViewAdapter;


import java.util.ArrayList;
import java.util.List;

public class AdapterSectionRecycler extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private List<LeadList> checkoutHistories;
    private ArrayList<WarrantryPart> list;

    private Context mContext;

    public AdapterSectionRecycler( Context mContext,List<LeadList> checkoutHistories,ArrayList<WarrantryPart> list) {
        super(mContext);
        this.checkoutHistories = checkoutHistories;
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getSectionCount() {
        return checkoutHistories.size();
    }

    @Override
    public int getItemCount(int section) {
        return checkoutHistories.size();
    }

    //Inflating views
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, boolean header) {
        if (header) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_complete_task, parent, false);
            return new SectionViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.under_warranty_parts_adapter, parent, false);
            return new ViewHolder(view);
        }
    }

    // Bind values on Header
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
     //   String sectionName = Util.getDateAndTime(checkoutHistories.get(section));
        String dd = checkoutHistories.get(section).getEnquiry().getModel_no();
        SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
        sectionViewHolder.txtHeader.setText(dd);
    }

    // Bind Value in Cells
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int section, int relativePosition, int absolutePosition) {
       // final WarrantyList order = checkoutHistories.get(section).getWarrantyParts().get(absolutePosition);
        String ss = checkoutHistories.get(section).getEnquiry().getWarranty_parts().get(absolutePosition).getName();


        for (int i=0 ; i<list.size();++i) {
            Log.d("sssssss",""+list.get(i).getName());
            final ViewHolder itemViewHolder = (ViewHolder) holder;
            itemViewHolder.textCheckedIn.setText(list.get(i).getName());
        }
       // String itemEventName = order.getTaskEnquiry();



        //If Checked out time is there,then only show Check out time else View won't be visible
       /* if (order.getCheckedOutAt() != null) {
            itemViewHolder.linCheckedOut.setVisibility(View.VISIBLE);
            String itemCheckedOut = order.getCheckedOutAt();
            itemViewHolder.textCheckedOut.setText(Util.getFormattedTime(itemCheckedOut));
        }*/

       // itemViewHolder.textEventName.setText(itemEventName);

        /*if (!TextUtils.isEmpty(itemLocation)) {
            itemViewHolder.textLocationName.setText(itemLocation);
        } else {
            itemViewHolder.linLocation.setVisibility(View.GONE);
        }*/

        //Cell tap
       /* itemViewHolder.linCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (order.getCanCheckOut()) {
                    toggleCheckInButtonVisibility(itemViewHolder.linDoCheckIn);
                }
            }
        });

        //Do check in click
        itemViewHolder.textDoCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCheckInAndDisplayCheckedOutTime(order, itemViewHolder);
            }
        });
*/
    }

    /**
     * Method to return event location
     *
     * @param order
     * @return event location
     */
  /*  private String getEventLocation(Order order) {
        Event event = order.getEvent();
        String name = null, roomName = null, address = null;
        if (event != null && event.getPlace() != null) {
            if (!TextUtils.isEmpty(event.getPlace().getName())) {
                name = event.getPlace().getName();
            }
            if (!TextUtils.isEmpty(event.getPlace().getRoomName())) {
                roomName = event.getPlace().getRoomName();
                name = roomName + "," + name;
            }
            if (!TextUtils.isEmpty(event.getPlace().getAddress())) {
                address = event.getPlace().getAddress();
                name = name + "\n" + address;

            }
            return name;

        }
        return null;
    }*/

    /**
     * Method to do check-in and display checked out time making Cell clickable false
     *
     * @param order
     * @param itemViewHolder
     */
  /*  private void doCheckInAndDisplayCheckedOutTime(Order order, ViewHolder itemViewHolder) {

        // Get current time stamp
        DateFormat.getDateTimeInstance().format(new Date());
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        order.setCheckedOutAt(currentDateTimeString);
        //Show checked out view to the user
        itemViewHolder.linCheckedOut.setVisibility(View.VISIBLE);
        itemViewHolder.textCheckedOut.setText(Util.getFormattedTime(currentDateTimeString));

        //Hide Checkgit commit -m "first commit" in button as we have already done check in
        itemViewHolder.linDoCheckIn.setVisibility(View.GONE);
        order.setCanCheckOut(false);

        //Reload
        notifyDataSetChanged();
    }
*/
    /**
     * Method to toggle visibility for Check in on cell click
     *
     * //@param linDoCheckIn -linearlayout for Checkin button
     */
    /*private void toggleCheckInButtonVisibility(LinearLayout linDoCheckIn) {
        linDoCheckIn.setVisibility(linDoCheckIn.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }*/

    // SectionViewHolder Class for Sections
    static class SectionViewHolder extends RecyclerView.ViewHolder {

       // @BindView(R.id.txt_header)
        TextView txtHeader;

        SectionViewHolder(View itemView) {
            super(itemView);
            txtHeader = itemView.findViewById(R.id.tv_task_completed_model_name_value);
           // ButterKnife.bind(this, itemView);
        }
    }

    // ItemViewHolder Class for Items in each Section
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCheckedIn;


        ViewHolder(View itemView) {
            super(itemView);
            textCheckedIn = itemView.findViewById(R.id.tv_warranty_parts);
        }
    }
}