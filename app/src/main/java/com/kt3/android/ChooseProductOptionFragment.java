package com.kt3.android;

import com.kt3.android.R;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kt3.android.enums.ICE_LEVEL;
import com.kt3.android.enums.SUGAR_LEVEL;

import mehdi.sakout.fancybuttons.FancyButton;

public class ChooseProductOptionFragment extends DialogFragment implements Button.OnClickListener {

    private RadioGroup rgSugar;
    private RadioGroup rgIce;
    private RadioButton rdSugarOne_Hundred;
    private RadioButton rdSugarFifty;
    private RadioButton rdSugarSeventy;
    private RadioButton rdSugarTwenty;
    private RadioButton rdSugarTen;

    private RadioButton rdIceOne_Hundred;
    private RadioButton rdIceFifty;
    private RadioButton rdIceSeventy;
    private RadioButton rdIceTwenty;
    private RadioButton rdIceTen;

    private FancyButton btnOk;
    private SUGAR_LEVEL sugar_level;
    private ICE_LEVEL ice_level;

    // 1. Defines the listener interface with a method passing back data result.
    public interface ChooseProductOptionDialogListener {
        void onFinishChooseProductOptionDialog(SUGAR_LEVEL sugar_level, ICE_LEVEL ice_level);
    }

    public ChooseProductOptionFragment() {
    }

    public static ChooseProductOptionFragment newInstance(String title) {
        ChooseProductOptionFragment frag = new ChooseProductOptionFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set to adjust screen height automatically, when soft keyboard appears on screen
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return inflater.inflate(R.layout.fragment_choose_product_option, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rgSugar = (RadioGroup) view.findViewById(R.id.rgSugar);
        rgIce = (RadioGroup) view.findViewById(R.id.rgIce);
        btnOk = view.findViewById(R.id.btnOk);

        rdSugarOne_Hundred = view.findViewById(R.id.rdSugarOne_Hundred);
        rdSugarFifty = view.findViewById(R.id.rdSugarFifty);
        rdSugarSeventy = view.findViewById(R.id.rdSugarSeventy);
        rdSugarTwenty = view.findViewById(R.id.rdSugarTwenty);
        rdSugarTen = view.findViewById(R.id.rdSugarTen);

        rdIceOne_Hundred = view.findViewById(R.id.rdIceOne_Hundred);
        rdIceFifty = view.findViewById(R.id.rdIceFifty);
        rdIceSeventy = view.findViewById(R.id.rdIceSeventy);
        rdIceTwenty = view.findViewById(R.id.rdIceTwenty);
        rdIceTen = view.findViewById(R.id.rdIceTen);

        rgSugar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rdSugarSeventy:
                        sugar_level = SUGAR_LEVEL.SEVENTY_PERCENT;
                        break;
                    case R.id.rdSugarFifty:
                        sugar_level = SUGAR_LEVEL.FIFTY_PERCENT;
                        break;
                    case R.id.rdSugarTwenty:
                        sugar_level = SUGAR_LEVEL.TWENTY_PERCENT;
                        break;
                    case R.id.rdSugarTen:
                        sugar_level = SUGAR_LEVEL.TEN_PERCENT;
                        break;
                    default:
                        sugar_level = SUGAR_LEVEL.ONE_HUNDRED_PERCENT;
                }
            }
        });

        rgIce.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rdIceSeventy:
                        ice_level = ICE_LEVEL.SEVENTY_PERCENT;
                        break;
                    case R.id.rdIceFifty:
                        ice_level = ICE_LEVEL.FIFTY_PERCENT;
                        break;
                    case R.id.rdIceTwenty:
                        ice_level = ICE_LEVEL.TWENTY_PERCENT;
                        break;
                    case R.id.rdIceTen:
                        ice_level = ICE_LEVEL.TEN_PERCENT;
                        break;
                    default:
                        ice_level = ICE_LEVEL.ONE_HUNDRED_PERCENT;
                }

            }
        });

        btnOk.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (EditorInfo.IME_ACTION_DONE == actionId) {
//            // Return input text back to activity through the implemented listener
//            //EditNameDialogListener listener = (EditNameDialogListener) getActivity();
//            //listener.onFinishEditDialog(mEditText.getText().toString());
//            // Close the dialog and return back to the parent activity
//            dismiss();
//            return true;
//        }
//        return false;
//    }

    @Override
    public void onClick(View view) {
        ChooseProductOptionDialogListener listener = (ChooseProductOptionDialogListener) getActivity();
        listener.onFinishChooseProductOptionDialog(sugar_level, ice_level);
        dismiss();
    }
}
