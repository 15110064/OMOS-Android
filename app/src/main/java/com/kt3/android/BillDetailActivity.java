package com.kt3.android;

import com.kt3.android.domain.Bill;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BillDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_DT);
        setSupportActionBar(toolbar);

        Bill bill = (Bill) getIntent().getSerializableExtra("bill");
        ImageView img2 = findViewById(R.id.img2);
        Picasso.with(getApplicationContext())
                .load(bill.getImg())
                .error(R.drawable.placeholder_milktea)
                .placeholder(R.drawable.placeholder_milktea)
                .resize(400, 400)
                .into(img2);
        TextView txtOrderTime = findViewById(R.id.txtDT_DateTime);
        txtOrderTime.setText(bill.getDateTime());

        TextView txtAddress = findViewById(R.id.txtDT_Address);
        txtAddress.setText(bill.getAddress());

        TextView txtTotal = findViewById(R.id.txtDT_Total);
        txtTotal.setText(bill.getTotal() + "đ");

        TextView txtStatus = findViewById(R.id.txtDT_Status);
        switch (bill.getStatus()) {
            case DA_GIAO:
                txtStatus.setText("Đã giao");
//                txtStatus.setTextColor(Color.parseColor("#00C853"));
                break;
            case DANG_VC:
                txtStatus.setText("Đang vận chuyển");
//                txtStatus.setTextColor(Color.parseColor("#00C853"));
                break;
            case DANG_XL:
                txtStatus.setText("Đang xử lý");
//                txtStatus.setTextColor(Color.parseColor("#FFC107"));
                break;
            case DA_HUY:
                txtStatus.setText("Đã hủy");
//                txtStatus.setTextColor(Color.parseColor("#FFC107"));
                break;
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
