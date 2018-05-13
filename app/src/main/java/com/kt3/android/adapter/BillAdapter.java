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

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 97lynk on 22/02/2018.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.Bill11Holder> {
    private Context context;
    private List<Bill> billList;

    public BillAdapter(Context context, List<Bill> billList) {
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
        final Bill bill = billList.get(position);
        holder.bill = bill;
        holder.txtDateTime1.setText(bill.getDateTime());
        holder.txtTotal1.setText("Tổng tiền: " + bill.getTotal() + "đ");
        holder.txtAddress1.setText("Địa chỉ: " + bill.getAddress());

Drawable statusIcon = context.getResources().getDrawable(R.drawable.ic_query_builder_24dp);
        switch (bill.getStatus()) {
            case DA_GIAO:
//                holder.txtStatus1.setText("Đã giao");
//                holder.txtStatus1.setTextColor(Color.parseColor("#00C853"));
                statusIcon = context.getResources().getDrawable(R.drawable.ic_check_circle_24dp);
                break;
            case DANG_VC:
//                holder.txtStatus1.setText("Đang vận chuyển");
//                holder.txtStatus1.setTextColor(Color.parseColor("#00C853"));
                statusIcon = context.getResources().getDrawable(R.drawable.ic_local_shipping_24dp);
                break;
            case DANG_XL:
//                holder.txtStatus1.setText("Đang xử lý");
//                holder.txtStatus1.setTextColor(Color.parseColor("#FFC107"));
                break;
            case DA_HUY:
                statusIcon = context.getResources().getDrawable(R.drawable.ic_cancel_24dp);
                break;
        }
        holder.imgStatus1.setImageDrawable(statusIcon);

        Picasso.with(context)
                .load(bill.getImg())
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
        public Bill bill;

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
