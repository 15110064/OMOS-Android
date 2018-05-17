package com.kt3.android.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.Gson;
import com.kt3.android.R;
import com.kt3.android.domain.CartItem;
import com.kt3.android.domain.CartSubject;
import com.kt3.android.other.AuthVolleyRequest;
import com.kt3.android.other.ConstantData;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by 97lynk on 22/02/2018.
 */

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemHolder>
        implements Observer {

    private Context context;
    private ArrayList<CartItem> cartItems;
    private CartSubject subject;

    public CartItemAdapter(Context context, ArrayList<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
        subject = CartSubject.getInstance();
        subject.addObserver(this);
    }

    @Override
    public CartItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);

        return new CartItemHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    @Override
    public void onBindViewHolder(final CartItemHolder holder, int position) {
        final CartItem cartItem = cartItems.get(position);
        Picasso.with(context)
                .load(cartItem.getProduct().getThumbnail())
                .placeholder(R.drawable.placeholder_milktea)
                .error(R.drawable.placeholder_milktea)
                .into(holder.imgProduct);

        final StringBuilder builder = new StringBuilder("");
        switch (cartItem.getIceLevel()) {
            case TEN_PERCENT:
                builder.append("10%");
                break;
            case TWENTY_PERCENT:
                builder.append("20%");
                break;
            case FIFTY_PERCENT:
                builder.append("50%");
                break;
            case SEVENTY_PERCENT:
                builder.append("70%");
                break;
            case ONE_HUNDRED_PERCENT:
                builder.append("100%");
                break;
            default:
                builder.append("Không");
        }
        builder.append(" đá, ");
        switch (cartItem.getSugarLevel()) {
            case TEN_PERCENT:
                builder.append("10%");
                break;
            case TWENTY_PERCENT:
                builder.append("20%");
                break;
            case FIFTY_PERCENT:
                builder.append("50%");
                break;
            case SEVENTY_PERCENT:
                builder.append("70%");
                break;
            case ONE_HUNDRED_PERCENT:
                builder.append("100%");
                break;
            default:
                builder.append("Không");
        }
        builder.append(" đường");
        holder.txtProperties.setText(builder.toString());
        holder.txtProductName.setText(cartItem.getProduct().getName());
        holder.txtProductPrice.setText(String.format("%s đ", cartItem.getSubTotal()));
        holder.btnQuantity.setNumber(String.format("%d", cartItem.getQuantity()));
        holder.btnQuantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                cartItem.setQuantity(newValue);
                cartItem.setSubTotal(new BigDecimal(cartItem.getProduct().getPrice().doubleValue() * newValue));
                editCartItem(cartItem, holder.access_token);
                subject.calculateTotalPrice();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(cartItem, holder.access_token);
            }


        });
    }


    private void showDialog(final CartItem cartItem, final String access_token) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).setMessage("Xóa item này?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCartItem(cartItem, access_token);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void deleteCartItem(final CartItem cartItem, final String access_token) {
        AuthVolleyRequest.getInstance(context)
                .requestObject(Request.Method.DELETE, ConstantData.ITEM_URL + cartItem.getId(), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                                cartItems.remove(cartItem);
                                subject.calculateTotalPrice();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
    }

    private void editCartItem(CartItem cartItem, final String access_token) {
        try {
            cartItem.setProductId(cartItem.getProduct().getId());
            JSONObject cartItemJson = new JSONObject(new Gson().toJson(cartItem));

            AuthVolleyRequest.getInstance(context)
                    .requestObject(
                            Request.Method.PUT, ConstantData.ITEM_URL + cartItem.getId(),
                            cartItemJson,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        notifyDataSetChanged();
        Toast.makeText(context, subject.getCartItems().size() + " : "
                + cartItems.size(), Toast.LENGTH_SHORT).show();
    }

    class CartItemHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView txtProductName;
        private TextView txtProductPrice;
        private TextView txtProperties;
        private ImageButton btnDelete;
        private ElegantNumberButton btnQuantity;
        private String access_token;

        public CartItemHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProperties = itemView.findViewById(R.id.txtProperties);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnQuantity = itemView.findViewById(R.id.btnQuantity);
            btnQuantity.setRange(1, 5);
            access_token = context.getSharedPreferences(ConstantData.OAUTH2_FILE_NAME, Context.MODE_PRIVATE)
                    .getString("access_token", null);
        }
    }
}
