package com.direct2web.citysip.Activities.Restaurent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.direct2web.citysip.Adapter.RestaurentAdapters.DishTypeAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.MenuListAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.NewMenuListAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.NewMenuListitemAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.SelectCuisinesAdapter;
import com.direct2web.citysip.Adapter.RestaurentAdapters.SelectDishTypeAdapter;
import com.direct2web.citysip.Model.RestaurentModels.Cuisines.Cuisine;
import com.direct2web.citysip.Model.RestaurentModels.Cuisines.DishType;
import com.direct2web.citysip.Model.RestaurentModels.Cuisines.ResponseCuisinesList;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseStatus;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.Menu;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.NewMenu;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.ResponseMenuList;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.ResponseNewMenuList;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.BottomButtonClickListner;
import com.direct2web.citysip.Utils.ClickListener;
import com.direct2web.citysip.Utils.ConnectionToInternet;
import com.direct2web.citysip.Utils.CustomListViewDialog;
import com.direct2web.citysip.Utils.RecyclerTouchListener;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityMenuListBinding;
import com.direct2web.citysip.databinding.ActivitySetUpMenuBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetUpMenuActivity extends AppCompatActivity implements SelectCuisinesAdapter.RecyclerViewItemClickListener,
        SelectDishTypeAdapter.RecyclerViewItemClickListener, NewMenuListitemAdapter.onClickSwitch, NewMenuListitemAdapter.onClickSwitchOff {

    public static final int REQUEST_IMAGE = 100;
    ActivitySetUpMenuBinding binding;
    ActivityMenuListBinding binding2;
    SessionManager sessionManager;
    TextView txtCuisine, txtDishType;
    List<Cuisine> cuisineList = new ArrayList<>();
    List<DishType> dishTypeList = new ArrayList<>();
    CustomListViewDialog customDialog;
    String selectedCuisinesId, selectedDishTypeId;
    ProgressDialog pd;
    List<Menu> menuList;
    MenuListAdapter adapter;
    int flag = 1;
    public static String catpos = "0";
    BottomButtonClickListner bottomButtonClickListner;
    boolean first = true;
    DishTypeAdapter dishTypeAdapter;
    String id;
    List<NewMenu> newMenuList = new ArrayList<>();
    NewMenuListAdapter newMenuListAdapter;
    NewMenuListitemAdapter.onClickSwitch onClickSwitch;
    NewMenuListitemAdapter.onClickSwitchOff onClickSwitchOff;
    NewMenuListitemAdapter.OnItemClickListner onItemClickListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flag = getIntent().getIntExtra("flag", 1);

        if (flag == 1) {

            binding2 = DataBindingUtil.setContentView(this, R.layout.activity_menu_list);

            sessionManager = new SessionManager(this);

            bottomButtonClickListner = new BottomButtonClickListner(this, sessionManager);
            binding2.bottomnavigation.bbImgMyBusiness.setColorFilter(getResources().getColor(R.color.clr_f54748));
            binding2.bottomnavigation.bbHome.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
            binding2.bottomnavigation.bbMyBusiness.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
            binding2.bottomnavigation.bbOrder.setOnClickListener(new BottomButtonClickListner(this, sessionManager));
            binding2.bottomnavigation.bbMenu.setOnClickListener(new BottomButtonClickListner(this, sessionManager));

            pd = new ProgressDialog(SetUpMenuActivity.this);
            pd.setMessage("Please Wait....");
            pd.setCancelable(true);
            pd.show();

            onClickSwitch = position -> {
                Menu timing = menuList.get(position);
                sendStatus(sessionManager.getUserId(), "menu", timing.getId(), "1");
            };

            onClickSwitchOff = position -> {
                Menu timing = menuList.get(position);
                sendStatus(sessionManager.getUserId(), "menu", timing.getId(), "0");
            };

            getMenuList2(sessionManager.getUserId());
            getdata2();

            binding2.addCoupons.setOnClickListener(view -> {
                Intent intent = new Intent(SetUpMenuActivity.this, SetUpAddMenuActivity.class);
                intent.putExtra("flag", "0");
                startActivity(intent);
            });

            binding2.llActiveMenuList.setOnClickListener(v -> {
                first = false;
                binding2.rlFirst.setVisibility(View.GONE);
                binding2.rlSecond.setVisibility(View.VISIBLE);
            });

            binding2.llInactiveMenuList.setOnClickListener(v -> {
                binding2.rlFirst.setVisibility(View.GONE);
                binding2.rlSecond.setVisibility(View.VISIBLE);
            });

            binding2.rclrDishType.addOnItemTouchListener(new RecyclerTouchListener(SetUpMenuActivity.this, binding2.rclrDishType, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    DishType dishType = dishTypeList.get(position);
                    id = dishType.getId();
                    catpos = String.valueOf(position);
                    dishTypeAdapter.notifyDataSetChanged();

                    if (new ConnectionToInternet(getApplicationContext()).isConnectingToInternet()) {
                        setProductList(id);
                    } else {
                        new ConnectionToInternet(SetUpMenuActivity.this).ShowDilog(SetUpMenuActivity.this);
                    }
                }

                @Override
                public void onLongClick(View view, int position) {
                }
            }));

        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_set_up_menu);
            sessionManager = new SessionManager(this);
            pd = new ProgressDialog(SetUpMenuActivity.this);
            pd.setMessage("Please Wait....");
            pd.setCancelable(true);
            pd.show();
            getMenuList(sessionManager.getUserId());
            getdata();
        }
    }

    private void getMenuList(String userId) {
        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseMenuList> call = api.getMenuList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);
        call.enqueue(new Callback<ResponseMenuList>() {
            @Override
            public void onResponse(Call<ResponseMenuList> call, Response<ResponseMenuList> response) {
                Log.e("responseMenuList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {

                    menuList = response.body().getMenuList();

                    if (menuList != null) {

                        adapter = new MenuListAdapter(SetUpMenuActivity.this, menuList);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(SetUpMenuActivity.this);
                        binding.rclrMenu.setLayoutManager(layoutManager);
                        binding.rclrMenu.setAdapter(adapter);

                    } else {
                        Toast.makeText(SetUpMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMenuList> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("erro", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void getdata() {
        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseCuisinesList> call = api.getCuisine("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key);
        call.enqueue(new Callback<ResponseCuisinesList>() {
            @Override
            public void onResponse(Call<ResponseCuisinesList> call, Response<ResponseCuisinesList> response) {

                Log.e("responseCuisin", new Gson().toJson(response.body()));

                if (response.body() != null && response.isSuccessful()) {

                    cuisineList = response.body().getCuisines();
                    dishTypeList = response.body().getDishType();

                    SelectCuisinesAdapter dataAdapter = new SelectCuisinesAdapter(cuisineList, SetUpMenuActivity.this);
                    customDialog = new CustomListViewDialog(SetUpMenuActivity.this, dataAdapter);

                    SelectDishTypeAdapter dataAdapter2 = new SelectDishTypeAdapter(dishTypeList, SetUpMenuActivity.this);
                    customDialog = new CustomListViewDialog(SetUpMenuActivity.this, dataAdapter2);
                }
            }

            @Override
            public void onFailure(Call<ResponseCuisinesList> call, Throwable t) {

                t.printStackTrace();
                Log.e("error", t.getMessage());

            }
        });
    }


    private void getMenuList2(String userId) {

        Log.e("userId", userId);

        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseMenuList> call = api.getMenuList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);
        call.enqueue(new Callback<ResponseMenuList>() {
            @Override
            public void onResponse(Call<ResponseMenuList> call, Response<ResponseMenuList> response) {
                Log.e("responseMenuListNEEEEE", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        binding2.llError.setVisibility(View.VISIBLE);
                        binding2.llMain.setVisibility(View.GONE);
                        Glide.with(SetUpMenuActivity.this)
                                .load(Api.imageUrl + response.body().getNoDataImage())
                                .error(R.drawable.ic_group_1755)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(binding2.imgError);

                        Toast.makeText(SetUpMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        menuList = response.body().getMenuList();
                        binding2.llError.setVisibility(View.GONE);
                        binding2.llMain.setVisibility(View.VISIBLE);
                        if (menuList != null) {

                            List<Menu> tempActive = new ArrayList<>();
                            List<Menu> tempInActive = new ArrayList<>();


                            for (Menu menuItemList : menuList) {

                                if (menuItemList.getStatus().equals("1")) {
                                    tempActive.add(menuItemList);
                                } else {

                                    tempInActive.add(menuItemList);
                                }
                            }
                            binding2.txtActiveMenu.setText(String.valueOf(tempActive.size()));
                            binding2.txtInActiveMenu.setText(String.valueOf(tempInActive.size()));

                        } else {
                            binding2.txtActiveMenu.setText("0");
                            binding2.txtInActiveMenu.setText("0");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMenuList> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                Log.e("erro", t.getMessage());
                t.printStackTrace();
            }
        });

    }

    private void getdata2() {

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseCuisinesList> call = api.getCuisine("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key);
        call.enqueue(new Callback<ResponseCuisinesList>() {
            @Override
            public void onResponse(Call<ResponseCuisinesList> call, Response<ResponseCuisinesList> response) {

                Log.e("responseCuisin", new Gson().toJson(response.body()));

                if (response.body() != null && response.isSuccessful()) {

                    cuisineList = response.body().getCuisines();
                    dishTypeList = response.body().getDishType();

                    DishType dishType = new DishType("0", "All");

                    dishTypeList.add(0, dishType);

                    dishTypeAdapter = new DishTypeAdapter(SetUpMenuActivity.this, dishTypeList);
                    GridLayoutManager mLayoutManager = new GridLayoutManager(SetUpMenuActivity.this, 1, GridLayoutManager.HORIZONTAL, false);

                    binding2.rclrDishType.setLayoutManager(mLayoutManager);
                    binding2.rclrDishType.setAdapter(dishTypeAdapter);

                    catpos = "0";
                    id = dishTypeList.get(Integer.parseInt(catpos)).getId();
                    dishTypeAdapter.notifyDataSetChanged();
                    setProductList(id);
                }
            }

            @Override
            public void onFailure(Call<ResponseCuisinesList> call, Throwable t) {

                t.printStackTrace();
                Log.e("error", t.getMessage());

            }
        });
    }

    private void setProductList(String id) {

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseNewMenuList> call = api.getNewMenuList("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, sessionManager.getUserId(), id);
        call.enqueue(new Callback<ResponseNewMenuList>() {
            @Override
            public void onResponse(Call<ResponseNewMenuList> call, Response<ResponseNewMenuList> response) {

                Log.e("responseNewMenu", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {
                        Toast.makeText(SetUpMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        newMenuList = response.body().getNewMenuList();
                        newMenuListAdapter = new NewMenuListAdapter(newMenuList, SetUpMenuActivity.this);
                        LinearLayoutManager manager = new LinearLayoutManager(SetUpMenuActivity.this);
                        binding2.rclrMenuList.setLayoutManager(manager);
                        binding2.rclrMenuList.setAdapter(newMenuListAdapter);

                    }

                } else {
                    Toast.makeText(SetUpMenuActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseNewMenuList> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (first) {
            super.onBackPressed();
        } else {
            first = true;
            binding2.rlFirst.setVisibility(View.VISIBLE);
            binding2.rlSecond.setVisibility(View.GONE);
        }
    }
    @Override
    public void clickOnSelectCategoryItem(String data, String id) {

        txtCuisine.setText(data);
        selectedCuisinesId = id;
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    @Override
    public void clickOnSelectDishTypeItem(String data, String id) {

        txtDishType.setText(data);
        selectedDishTypeId = id;
        if (customDialog != null) {
            customDialog.dismiss();
        }
    }

    @Override
    public void onSwitchItemClick(int position) {
        Menu timing = menuList.get(position);
        sendStatus(sessionManager.getUserId(), "menu", timing.getId(), "0");
    }
    @Override
    public void onSwitchItemClickOff(int position) {
        Menu timing = menuList.get(position);
        sendStatus(sessionManager.getUserId(), "menu", timing.getId(), "1");
    }

    public void sendStatus(String userId, String type, String id, String status) {
        pd = new ProgressDialog(SetUpMenuActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseStatus> call = api.sendStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id, status);
        call.enqueue(new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {

                Log.e("responseDelete", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SetUpMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(SetUpMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(SetUpMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e("errorDelete", t.getMessage());
            }
        });
    }
}