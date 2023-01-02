package com.direct2web.citysip.Activities.Restaurent;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.direct2web.citysip.Adapter.RestaurentAdapters.OrderListAdapter;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseStatus;
import com.direct2web.citysip.Model.RestaurentModels.OrderList.Order;
import com.direct2web.citysip.Model.RestaurentModels.OrderList.ResponseOrderList;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityOrderListBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListActivity extends AppCompatActivity implements OrderListAdapter.onClickReject, OrderListAdapter.onClickAccept {

    ActivityOrderListBinding binding;
    SessionManager sessionManager;
    BottomButtonClickListner bottomButtonClickListner;
    ProgressDialog pd;

    List<Order> orderList = new ArrayList<>();
    List<Order> temp = new ArrayList<>();
    OrderListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_list);

        sessionManager = new SessionManager(this);
       /* bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);

        binding.bottomnavigation.bbImgOrder.setColorFilter(getResources().getColor(R.color.clay));
        binding.bottomnavigation.bbTxtOrder.setTextColor(getResources().getColor(R.color.clay));

        binding.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
        binding.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));*/

        binding.toolbar.toolbarBack.setVisibility(View.VISIBLE);

        binding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();


        getOrder(sessionManager.getUserId());


        binding.txtContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                filter("0");

                binding.txtContinue.setTextColor(getResources().getColor(R.color.clr_f54748));
                binding.txtAllOrder.setTextColor(getResources().getColor(R.color.clr_333333));
                binding.txtCanceled.setTextColor(getResources().getColor(R.color.clr_333333));
                binding.txtCompleted.setTextColor(getResources().getColor(R.color.clr_333333));

            }
        });

        binding.txtAllOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                filter("3");

                binding.txtAllOrder.setTextColor(getResources().getColor(R.color.clr_f54748));
                binding.txtContinue.setTextColor(getResources().getColor(R.color.clr_333333));
                binding.txtCanceled.setTextColor(getResources().getColor(R.color.clr_333333));
                binding.txtCompleted.setTextColor(getResources().getColor(R.color.clr_333333));

            }
        });

        binding.txtCanceled.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                filter("2");

                binding.txtCanceled.setTextColor(getResources().getColor(R.color.clr_f54748));
                binding.txtContinue.setTextColor(getResources().getColor(R.color.clr_333333));
                binding.txtAllOrder.setTextColor(getResources().getColor(R.color.clr_333333));
                binding.txtCompleted.setTextColor(getResources().getColor(R.color.clr_333333));

            }
        });

        binding.txtCompleted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                filter("1");

                binding.txtCompleted.setTextColor(getResources().getColor(R.color.clr_f54748));
                binding.txtContinue.setTextColor(getResources().getColor(R.color.clr_333333));
                binding.txtAllOrder.setTextColor(getResources().getColor(R.color.clr_333333));
                binding.txtCanceled.setTextColor(getResources().getColor(R.color.clr_333333));


            }
        });

        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter2(editable.toString());
            }
        });
    }

    private void filter2(String text) {

        ArrayList<Order> filterList = new ArrayList<>();
        for (Order item : temp) {
            if (item.getCustomerName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getId().toLowerCase().contains(text.toLowerCase()) ) {
                filterList.add(item);

            }
        }
        adapter.updatelist(filterList);
    }


    private void getOrder(String userId) {

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseOrderList> call = api.getOrderList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);

        call.enqueue(new Callback<ResponseOrderList>() {
            @Override
            public void onResponse(Call<ResponseOrderList> call, Response<ResponseOrderList> response) {

                Log.e("responseOrder", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(OrderListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        orderList = response.body().getOrderList();

                        adapter = new OrderListAdapter(orderList, OrderListActivity.this,OrderListActivity.this,OrderListActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderListActivity.this);
                        binding.rclrOrder.setLayoutManager(linearLayoutManager);
                        binding.rclrOrder.setAdapter(adapter);


                    }


                } else {

                    Toast.makeText(OrderListActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseOrderList> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("error", t.getMessage());
                t.printStackTrace();

            }
        });


    }

    void filter(String s) {

        temp.clear();

        if (orderList != null) {
            for (Order orderItemList : orderList) {

                if (s.equals("3")) {
                    if (orderItemList.getStatus().equals("0") || orderItemList.getStatus().equals("1")  || orderItemList.getStatus().equals("2")) {
                        temp.add(orderItemList);
                    }
                } else if (orderItemList.getStatus().equals(s)) {

                    temp.add(orderItemList);
                }
            }
        }

        try {
            adapter.updatelist(temp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickRejectButton(int position) {

        Order order = orderList.get(position);
        sendStatus("", "order_list", order.getId(), "2");

    }

    @Override
    public void onClickAcceptButton(int position) {

        Order order = orderList.get(position);
        sendStatus("", "order_list", order.getId(), "1");
    }

    public void sendStatus(String userId, String type, String id, String status) {

       /* pd = new ProgressDialog(SetUpCouponsActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();
*/
        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseStatus> call = api.sendStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id, status);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {

                Log.e("responseDelete", new Gson().toJson(response.body()));

               /* if (pd.isShowing()) {
                    pd.dismiss();
                }*/

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(OrderListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(OrderListActivity.this, "Successfully Update", Toast.LENGTH_SHORT).show();
                        getOrder(sessionManager.getUserId());
                    }

                } else {
                    Toast.makeText(OrderListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {

               /* if (pd.isShowing()) {
                    pd.dismiss();
                }*/
                t.printStackTrace();
                Log.e("errorDelete", t.getMessage());
            }
        });
    }

}