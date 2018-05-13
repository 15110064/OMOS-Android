package com.kt3.android.adapter;

import android.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kt3.android.AddressActivity;
import com.kt3.android.R;
import com.kt3.android.domain.Address;
import com.kt3.android.fragment.AddressFragment;
import com.kt3.android.other.ConstantData;
import com.kt3.android.other.MODE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 97lynk on 22/02/2018.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {
    private Context context;
    private List<Address> addressList;


    public AddressAdapter(Context context, List<Address> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_in_address, parent, false);

        return new AddressHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    @Override
    public void onBindViewHolder(final AddressHolder holder, int position) {
        final Address address = addressList.get(position);
        holder.txtAName.setText(address.getFullName());
        holder.txtAAddress.setText(String.format("%s, Phường/Xã %s, Quận/Huyện %s, TP/Tỉnh %s",
                address.getAddress(), address.getWardName(), address.getProvinceName(), address.getCityName()));
        holder.txtAPhone.setText(address.getPhone());

        char firstChar = address.getFullName().toUpperCase().charAt(0);
        holder.txtChar.setText(String.valueOf(firstChar));

        Drawable mDrawable = context.getResources().getDrawable(R.drawable.circle_textview);

        // defualt color
        int colorID = context.getResources().getIdentifier(
                "specific_character", "color", context.getPackageName());
        if (65 <= firstChar && firstChar <= 90) { // A - Z
            colorID = context.getResources().getIdentifier(String.valueOf(firstChar), "color", context.getPackageName());

        } else if (48 <= firstChar && firstChar <= 57) { // 0 - 9
            colorID = context.getResources().getIdentifier(String.format("d%s", firstChar), "color", context.getPackageName());

        }
        mDrawable.setColorFilter(new
                PorterDuffColorFilter(context.getResources().getColor(colorID), PorterDuff.Mode.SRC));
        holder.txtChar.setBackground(mDrawable);
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.more);
                //inflating menu from xml resource
                popup.inflate(R.menu.pop_address);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.editAddress:
                                Intent intent = new Intent(context, AddressActivity.class);
                                intent.putExtra("MODE", MODE.EDIT);
                                intent.putExtra("ADDRESS", address);
                                context.startActivity(intent);
                                break;
                            case R.id.delAddress:
                                showDialog(address);
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }

    public void showDialog(final Address address) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteAddress(address.getId());
                        addressList.remove(address);
                        notifyDataSetChanged();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Xóa địa chỉ này?")
                .setPositiveButton("Đồng ý", dialogClickListener)
                .setNegativeButton("Hủy", dialogClickListener)
                .show();
    }

    private void deleteAddress(int addressID) {
        final String access_token = context.getSharedPreferences(ConstantData.OAUTH2_FILE_NAME, Context.MODE_PRIVATE).getString("access_token", null);
        if (access_token == null) {
            Toast.makeText(context, "Lỗi xóa địa chỉ", Toast.LENGTH_LONG).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest deleteAddressReq = new JsonObjectRequest(Request.Method.DELETE,
                String.format("%s%d", ConstantData.ADDRESS_RESOURCE_URL, addressID), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.toString().contains("success"))
                            Toast.makeText(context, "Đã  xóa địa chỉ", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>(super.getHeaders());
                if (headers.containsKey("Authorization"))
                    headers.remove("Authorization");
                headers.put("Authorization", String.format("Bearer %s", access_token));
                return headers;
            }
        };
        queue.add(deleteAddressReq);
    }

    class AddressHolder extends RecyclerView.ViewHolder {

        TextView txtAName;
        TextView txtAAddress;
        TextView txtAPhone;
        TextView txtChar;
        ImageButton more;

        public AddressHolder(View itemView) {
            super(itemView);

            txtChar = itemView.findViewById(R.id.txtChar);

            txtAName = itemView.findViewById(R.id.txtAName);
            txtAAddress = itemView.findViewById(R.id.txtAAddess);
            txtAPhone = itemView.findViewById(R.id.txtAPhone);
            more = itemView.findViewById(R.id.more);
        }
    }
}
