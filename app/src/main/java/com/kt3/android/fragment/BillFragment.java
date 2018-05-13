package com.kt3.android.fragment;

import com.kt3.android.R;
import com.kt3.android.adapter.BillAdapter;
import com.kt3.android.domain.Bill;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class BillFragment extends Fragment {


    public BillFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        ArrayList<Bill> bills = new ArrayList<>();
        bills.add(new Bill("10:30 - 23/02/2018", 179000.0, "1 Võ Văn Ngân, TĐ, TPHCM", R.drawable.history1, Bill.STATUS.DANG_VC));
        bills.add(new Bill("16:00 - 23/02/2018", 205000.0, "1 Đặng Văn Bi, TĐ, TPHCM", R.drawable.history3, Bill.STATUS.DANG_XL));
        bills.add(new Bill("16:00 - 23/02/2018", 205000.0, "1 Đặng Văn Bi, TĐ, TPHCM", R.drawable.history2, Bill.STATUS.DA_GIAO));
        bills.add(new Bill("16:00 - 23/02/2018", 205000.0, "1 Đặng Văn Bi, TĐ, TPHCM", R.drawable.history3, Bill.STATUS.DA_HUY));
        bills.add(new Bill("16:00 - 23/02/2018", 105000.0, "1 Đặng Văn Bi, TĐ, TPHCM", R.drawable.history1, Bill.STATUS.DANG_VC));
        bills.add(new Bill("16:00 - 23/02/2018", 354000.0, "1 Đặng Văn Bi, TĐ, TPHCM", R.drawable.history3, Bill.STATUS.DANG_XL));
        bills.add(new Bill("16:00 - 23/02/2018", 205000.0, "1 Đặng Văn Bi, TĐ, TPHCM", R.drawable.history3, Bill.STATUS.DANG_VC));
        bills.add(new Bill("16:00 - 23/02/2018", 205000.0, "1 Đặng Văn Bi, TĐ, TPHCM", R.drawable.history1, Bill.STATUS.DANG_XL));
        bills.add(new Bill("16:00 - 23/02/2018", 205000.0, "1 Đặng Văn Bi, TĐ, TPHCM", R.drawable.history1, Bill.STATUS.DA_HUY));
        bills.add(new Bill("16:00 - 23/02/2018", 205000.0, "1 Đặng Văn Bi, TĐ, TPHCM", R.drawable.history3, Bill.STATUS.DANG_XL));
        recyclerView.setAdapter(new BillAdapter(getContext(),bills));


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_Bill);
        final AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return view;
    }


}
