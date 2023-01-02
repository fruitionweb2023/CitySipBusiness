package com.direct2web.citysip.Utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;

import com.direct2web.citysip.R;

public class CustomTextChangeListener implements TextWatcher {

        private View view;
        EditText etNext, etPrev;
        EditText currentView;
        AppCompatButton button;
        boolean flag= false;
        Context context;
        int next = 0 , add=1;

        public CustomTextChangeListener(Context context, EditText currentView, EditText etNext, EditText etPrev, boolean flag, AppCompatButton button) {
            this.context=context;
            this.etPrev = etPrev;
            this.etNext = etNext;
            this.currentView = currentView;
            this.flag=flag;
            this.button=button;



        }




        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = s.toString();
            if (etNext != null && etPrev != null){
                if (text.length() == 1) {
                    etNext.requestFocus();
                } else {
                    etPrev.requestFocus();
                }
            }/*else {

                if (currentView != null){

                    next += add;
                }

                if (next == 4){
                    button.setTextColor(context.getResources().getColor(R.color.clr_f8f8f8));
                    button.setBackground(context.getResources().getDrawable(R.drawable.button));
                }

            }*/

            /*if (text.length() > 0) {
                currentView.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_green));
            } else {
                currentView.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_two_dp_thik));

            }*/
            if (flag){
                closeKeyboard();
                button.setTextColor(context.getResources().getColor(R.color.clr_f8f8f8));
                button.setBackground(context.getResources().getDrawable(R.drawable.button));

            }else {

                button.setTextColor(context.getResources().getColor(R.color.cle_979592));
                button.setBackground(context.getResources().getDrawable(R.drawable.button_disable));


            }

        }

    public void closeKeyboard() {
        View view = currentView;
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    }