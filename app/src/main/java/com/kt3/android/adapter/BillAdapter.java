package com.kt3.android.adapter;

import android.app.Activity;
import android.app.ActivityOptions;

import com.kt3.android.BillDetailActivity;
import com.kt3.android.R;
import com.kt3.android.domain.Bill;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kt3.android.domain.OrderTable;
import com.kt3.android.enums.ORDER_STATUS;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 97lynk on 22/02/2018.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.Bill11Holder> {
    private Context context;
    private List<OrderTable> billList;

    public BillAdapter(Context context, List<OrderTable> billList) {
        this.context = context;
        this.billList = billList;
    }

    @Override
    public Bill11Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_in_bill, parent, false);

        return new Bill11Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Bill11Holder holder, int position) {
        final OrderTable bill = billList.get(position);
        holder.bill = bill;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        holder.txtDateTime1.setText(format.format(new Date(bill.getCreateIn())));
        holder.txtTotal1.setText("Tổng tiền: " + bill.getTotalPrice().doubleValue() + "đ");
        holder.txtAddress1.setText("Địa chỉ: " + bill.getAddress().toString());

        Drawable statusIcon = context.getResources().getDrawable(R.drawable.ic_help_outline_24dp);
        switch (bill.getOrder_status()) {
            case CHUA_XAC_NHAN:
                statusIcon = context.getResources().getDrawable(R.drawable.ic_help_outline_24dp);
                break;
            case DA_NHAN:
                statusIcon = context.getResources().getDrawable(R.drawable.ic_query_builder_24dp);
                break;
            case DANG_GIAO_HANG:
                statusIcon = context.getResources().getDrawable(R.drawable.ic_local_shipping_24dp);
                break;
            case DA_HUY:
                statusIcon = context.getResources().getDrawable(R.drawable.ic_cancel_24dp);
                break;
            case DA_GIAO:
                statusIcon = context.getResources().getDrawable(R.drawable.ic_check_circle_24dp);
                break;
        }
        holder.imgStatus1.setImageDrawable(statusIcon);

        Picasso.with(context)
                .load(R.drawable.placeholder_milktea)
                .error(R.drawable.placeholder_milktea)
                .placeholder(R.drawable.placeholder_milktea)
                .resize(300, 300)
                .into(holder.img1);
    }


    @Override
    public int getItemCount() {
        return billList.size();
    }

    class Bill11Holder extends RecyclerView.ViewHolder {
        public ImageView img1;
        public TextView txtDateTime1;
        public TextView txtTotal1;
        public TextView txtAddress1;
        public ImageView imgStatus1;
        public OrderTable bill;

        public Bill11Holder(final View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.img1);
            txtDateTime1 = itemView.findViewById(R.id.txtDateTime1);
            txtTotal1 = itemView.findViewById(R.id.txtTotal1);
            txtAddress1 = itemView.findViewById(R.id.txtAddress1);
            imgStatus1 = itemView.findViewById(R.id.imgStatus1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, BillDetailActivity.class);
                    i.putExtra("bill", bill);

                    View sharedView = img1;
                    String transitionName = "zoom";


                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context, sharedView, transitionName);
                    context.startActivity(i, transitionActivityOptions.toBundle());
                }
            });
        }

    }
}
