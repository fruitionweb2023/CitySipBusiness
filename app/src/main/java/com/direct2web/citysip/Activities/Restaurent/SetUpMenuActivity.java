package com.direct2web.citysip.Activities.Restaurent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.direct2web.citysip.Model.RestaurentModels.SetUpMenu.ResponseSetMenu;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetUpMenuActivity extends AppCompatActivity implements SelectCuisinesAdapter.RecyclerViewItemClickListener,
        SelectDishTypeAdapter.RecyclerViewItemClickListener, NewMenuListitemAdapter.onClickSwitch, NewMenuListitemAdapter.onClickSwitchOff {

    public static final int REQUEST_IMAGE = 100;
    ActivitySetUpMenuBinding binding;
    ActivityMenuListBinding binding2;
    SessionManager sessionManager;
    AlertDialog builder;
    EditText edtDishName, edtDishLimit, edtDishPrice, edtDescription;
    ImageView imgLogo;
    AppCompatButton btnSubmit, imgCancel;
    TextView txtCuisine, txtDishType, txtVeg, txtNonVeg;
    String img2 = "";
    int flag_image;
    Bitmap bitmap2;
    String category = "";

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

            onClickSwitch = new NewMenuListitemAdapter.onClickSwitch(){

                @Override
                public void onSwitchItemClick(int position) {
                    Menu timing = menuList.get(position);

//        Toast.makeText(SetUpTimingListActivity.this, timing.getDayId()+", "+timing.getDay(), Toast.LENGTH_SHORT).show();

                    sendStatus(sessionManager.getUserId(),"menu",timing.getId(),"1");
                }
            };

            onClickSwitchOff = new NewMenuListitemAdapter.onClickSwitchOff() {
                @Override
                public void onSwitchItemClickOff(int position) {
                    Menu timing = menuList.get(position);

//        Toast.makeText(SetUpTimingListActivity.this, timing.getDayId()+", "+timing.getDay()+" Off", Toast.LENGTH_SHORT).show();

                    sendStatus(sessionManager.getUserId(),"menu",timing.getId(),"0");
                }
            };

            getMenuList2(sessionManager.getUserId());

            getdata2();

            binding2.addCoupons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SetUpMenuActivity.this, SetUpAddMenuActivity.class);
                    startActivity(intent);
                }
            });

            binding2.llActiveMenuList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    first = false;
                    binding2.rlFirst.setVisibility(View.GONE);
                    binding2.rlSecond.setVisibility(View.VISIBLE);


                }
            });

            binding2.llInactiveMenuList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    binding2.rlFirst.setVisibility(View.GONE);
                    binding2.rlSecond.setVisibility(View.VISIBLE);

                }
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

            /*binding.txtSetUpLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(SetUpMenuActivity.this, SetUpImageActivity.class);
                    startActivity(intent);

                }
            });

            binding.btnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(SetUpMenuActivity.this, SetUpImageActivity.class);
                    startActivity(intent);

                }
            });*/


        /*    binding.imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    builder = new AlertDialog.Builder(SetUpMenuActivity.this).create();
                    LayoutInflater inflater = SetUpMenuActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.pop_up_add_menu, null);
                    builder.setCancelable(true);

                    edtDishName = dialogView.findViewById(R.id.edt_dish_name);
                    edtDishLimit = dialogView.findViewById(R.id.edt_dish_limit);
                    edtDishPrice = dialogView.findViewById(R.id.edt_price);
                    edtDescription = dialogView.findViewById(R.id.edt_description);
                    imgLogo = dialogView.findViewById(R.id.img_dish_logo);
                    imgCancel = dialogView.findViewById(R.id.img_close);
                    btnSubmit = dialogView.findViewById(R.id.btn_submit);
                    txtCuisine = dialogView.findViewById(R.id.txt_cuisines);
                    txtDishType = dialogView.findViewById(R.id.txt_dish_type);
                    txtVeg = dialogView.findViewById(R.id.txt_veg);
                    txtNonVeg = dialogView.findViewById(R.id.txt_non_veg);


                    imgCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.dismiss();
                        }
                    });

                    imgLogo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            flag_image = 2;
                            //openSelectpicDilog();
                            onProfileImageClick();
                        }
                    });

                    txtVeg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            txtVeg.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_green_fill));
                            txtNonVeg.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_grey));

                            category = "veg";
                        }
                    });

                    txtNonVeg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            txtNonVeg.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_green_fill));
                            txtVeg.setBackground(getResources().getDrawable(R.drawable.four_dp_corner_box_grey));

                            category = "nonveg";
                        }
                    });

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (edtDishName.getText().toString().equals("")) {

                                Toast.makeText(getApplicationContext(), "Enter Dish Name", Toast.LENGTH_SHORT).show();

                            } else if (edtDishPrice.getText().toString().equals("")) {

                                Toast.makeText(getApplicationContext(), "Enter Dish Price", Toast.LENGTH_SHORT).show();

                            } else if (edtDishLimit.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Enter Dish Limit", Toast.LENGTH_SHORT).show();


                            } else if (edtDescription.getText().toString().equals("")) {

                                Toast.makeText(getApplicationContext(), "Enter Dish Description", Toast.LENGTH_SHORT).show();

                            } else if (txtCuisine.getText().toString().equals("Cuisines")) {

                                Toast.makeText(getApplicationContext(), "Please Select Cuisines First", Toast.LENGTH_SHORT).show();

                            } else if (txtDishType.getText().toString().equals("Dish Type")) {

                                Toast.makeText(getApplicationContext(), "Please Select Dish Type First", Toast.LENGTH_SHORT).show();

                            } else if (category.equals("")) {
                                Toast.makeText(getApplicationContext(), "Please Select Categoty Veg or NonVeg", Toast.LENGTH_SHORT).show();

                            } else {

                                sendSetUpMenu();

                            }

                        }
                    });

                    txtCuisine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SelectCuisinesAdapter dataAdapter = new SelectCuisinesAdapter(cuisineList, SetUpMenuActivity.this);
                            customDialog = new CustomListViewDialog(SetUpMenuActivity.this, dataAdapter);

                            customDialog.show();
                            customDialog.setCanceledOnTouchOutside(false);

                        }
                    });

                    txtDishType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SelectDishTypeAdapter dataAdapter = new SelectDishTypeAdapter(dishTypeList, SetUpMenuActivity.this);
                            customDialog = new CustomListViewDialog(SetUpMenuActivity.this, dataAdapter);

                            customDialog.show();
                            customDialog.setCanceledOnTouchOutside(false);

                        }
                    });


                    builder.setView(dialogView);
                    builder.show();


                }
            });*/
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

    private void sendSetUpMenu() {

        pd = new ProgressDialog(SetUpMenuActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String authHeader = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesskey = WS_URL_PARAMS.access_key;
        String business_id = sessionManager.getUserId();
        String title = edtDishName.getText().toString();
        String cuisines = selectedCuisinesId;
        String dish_type = selectedDishTypeId;
        String amount = edtDishPrice.getText().toString();
        String max_dish = edtDishLimit.getText().toString();
        String description = edtDescription.getText().toString();

        RequestBody t1 = RequestBody.create(MediaType.parse("multipart/form-data"), authHeader);
        RequestBody t2 = RequestBody.create(MediaType.parse("multipart/form-data"), accesskey);
        RequestBody t3 = RequestBody.create(MediaType.parse("multipart/form-data"), business_id);
        RequestBody t4 = RequestBody.create(MediaType.parse("multipart/form-data"), title);
        RequestBody t5 = RequestBody.create(MediaType.parse("multipart/form-data"), cuisines);
        RequestBody t6 = RequestBody.create(MediaType.parse("multipart/form-data"), dish_type);
        RequestBody t7 = RequestBody.create(MediaType.parse("multipart/form-data"), amount);
        RequestBody t8 = RequestBody.create(MediaType.parse("multipart/form-data"), max_dish);
        RequestBody t9 = RequestBody.create(MediaType.parse("multipart/form-data"), description);
        RequestBody t10 = RequestBody.create(MediaType.parse("multipart/form-data"), category);
        RequestBody t11 = RequestBody.create(MediaType.parse("multipart/form-data"), category);

        File file = null;
        MultipartBody.Part body1 = null;
        if (!img2.equals("")) {
            file = new File(img2);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            String s = title + "_" + file.getName();
            String logo = s.replaceAll(" ", "_");
            body1 = MultipartBody.Part.createFormData("image", logo, requestFile);
        }

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseSetMenu> call = api.sendSetUpMenu(authHeader, t2, t3, t4, t5, t6, t7, t8, t9, t10,t11, body1);

        call.enqueue(new Callback<ResponseSetMenu>() {
            @Override
            public void onResponse(Call<ResponseSetMenu> call, Response<ResponseSetMenu> response) {

                Log.e("responceSetupMenu", new Gson().toJson(response.body()));

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                    } else {
                        getMenuList(sessionManager.getUserId());
                        Log.e("userId", sessionManager.getUserId());
                        builder.dismiss();
                    }

                } else {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSetMenu> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
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
                Log.e("responseMenuList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(SetUpMenuActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        menuList = response.body().getMenuList();

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


  /*  void onProfileImageClick() {
        Dexter.withActivity(SetUpMenuActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(SetUpMenuActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(SetUpMenuActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                Uri selectedImage = data.getData();
                if (uri != null) {
                    try {
                        // You can update this bitmap to your server
                        bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        selectedImage = getImageUri(getApplicationContext(), bitmap2);
                        Log.e("uripath", "" + selectedImage);

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        img2 = cursor.getString(columnIndex);
                        cursor.close();

                        Log.e("path", img2);
                        // loading profile image from local cache
                        loadProfile(uri.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    private void loadProfile(String url) {
        Log.e("TAG....", "Image cache path: " + url);

        //img2 = url;

        *//*Glide.with(this).load(url)
                .into(binding.imglogoPic);
        binding.imglogoPic.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));*//*

        Glide.with(this).load(url)
                .into(imgLogo);
        imgLogo.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetUpMenuActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
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

    }*/

    }

    @Override
    public void onSwitchItemClick(int position) {

        Menu timing = menuList.get(position);

//        Toast.makeText(SetUpTimingListActivity.this, timing.getDayId()+", "+timing.getDay(), Toast.LENGTH_SHORT).show();

        sendStatus(sessionManager.getUserId(),"menu",timing.getId(),"0");

    }

    @Override
    public void onSwitchItemClickOff(int position) {

        Menu timing = menuList.get(position);

//        Toast.makeText(SetUpTimingListActivity.this, timing.getDayId()+", "+timing.getDay()+" Off", Toast.LENGTH_SHORT).show();

        sendStatus(sessionManager.getUserId(),"menu",timing.getId(),"1");

    }

    public void sendStatus(String userId,String type,String id,String status){

        pd = new ProgressDialog(SetUpMenuActivity.this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseStatus> call = api.sendStatus("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId, type, id,status);
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