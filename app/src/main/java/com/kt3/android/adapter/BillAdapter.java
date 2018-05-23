package com.kt3.android.adapter;

import android.app.Activity;
import android.app.ActivityOptions;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.goodiebag.pinview.Pinview;
import com.kt3.android.BillDetailActivity;
import com.kt3.android.CartActivity;
import com.kt3.android.R;
import com.kt3.android.UserActivity;
import com.kt3.android.domain.Address;
import com.kt3.android.domain.Bill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kt3.android.domain.OrderTable;
import com.kt3.android.enums.ORDER_STATUS;
import com.kt3.android.other.AuthVolleyRequest;
import com.kt3.android.other.ConstantData;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateTime = new Date(bill.getCreateIn());

        holder.txtDateTime1.setText(timeFormat.format(dateTime).concat(" ngày ").concat(dateFormat.format(dateTime)));
        holder.txtTotal1.setText("Tổng tiền: " + bill.getTotalPrice().doubleValue() + "đ");
        Address address = bill.getAddress();
        holder.txtAddress1.setText(String.format("Địa chỉ: %s, phường/xã %s, quận/huyện %s, TP HCM", address.getAddress(), address.getWardName(), address.getProvinceName()));

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
                public void onClick(final View v) {
//                    Intent i = new Intent(context, BillDetailActivity.class);
//                    i.putExtra("bill", bill);
//
//                    View sharedView = img1;
//                    String transitionName = "zoom";
//
//
//                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context, sharedView, transitionName);
//                    context.startActivity(i, transitionActivityOptions.toBundle());
                    if (bill.getOrder_status().equals(ORDER_STATUS.CHUA_XAC_NHAN)) {
                        UserActivity userActivity = (UserActivity) context;
                        View view = userActivity.getLayoutInflater().inflate(R.layout.layout_submit_code, null);
                        final AlertDialog submitCodeDialog = new AlertDialog.Builder(context)
                                .setView(view).setTitle("Nhập mã xác nhận")
                                .setNegativeButton(R.string.cancel, null)
                                .create();
                        Pinview pinvCode = view.findViewById(R.id.pivCode);
                        TextView txtDescription = view.findViewById(R.id.txtDescription);
                        pinvCode.setPinViewEventListener(new Pinview.PinViewEventListener() {
                            @Override
                            public void onDataEntered(Pinview pinview, boolean b) {
                                AuthVolleyRequest.getInstance(context)
                                        .requestObject(Request.Method.POST,
                                                String.format("%s/submit?code=%s&orderId=%d", ConstantData.CART_URL, pinview.getValue(), bill.getId()), null,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            Toast.makeText(context, response.getString("message").toString(), Toast.LENGTH_LONG).show();
                                                            String status = response.getString("status");
                                                            if (status.contains("success"))
                                                                submitCodeDialog.cancel();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                            }
                        });
                        submitCodeDialog.show();
                    }
                }
            });
        }

    }
}
