package com.direct2web.citysip.Activities.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.direct2web.citysip.Activities.Restaurent.ImagePickerActivity;
import com.direct2web.citysip.Adapter.DoctorAdapters.AddAndSetImageListDoctorAdapter;
import com.direct2web.citysip.Model.DoctorModels.DoctorAddImage.ImageAndVedio.Image;
import com.direct2web.citysip.Model.DoctorModels.DoctorAddImage.ImageAndVedio.ResponseDoctorAddImageAndVedio;
import com.direct2web.citysip.Model.DoctorModels.DoctorAddImage.ResponseDoctorAddImage;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityDoctorAddImageBinding;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorAddImageActivity extends AppCompatActivity implements AddAndSetImageListDoctorAdapter.OnItemClickListner {

    ActivityDoctorAddImageBinding binding;
    SessionManager sessionManager;

    String img2 = "";
    private static final int PICK_IMAGE_MULTIPLE = 200;
    public static final int REQUEST_IMAGE = 100;
    String[] projection = {MediaStore.MediaColumns.DATA};
    Bitmap bitmap2;
    ProgressDialog pd;
    List<Image> imagesEncodedList;
    ArrayList<Image> filePathsDoctor = new ArrayList<>();
    private static final String TAG = "SetUpImageActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_add_image);

        sessionManager = new SessionManager(this);


        binding.toolbar.toolbarBack.setVisibility(View.VISIBLE);


        binding.toolbar.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        binding.imgDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorAddImageActivity.this, DoctorDeleteImageActivity.class);
                startActivity(intent);
            }
        });


        if (imagesEncodedList != null) {
            int numberOfColumns = 3;
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
            binding.rclrImage.setLayoutManager(gridLayoutManager);
            AddAndSetImageListDoctorAdapter addAndSetImageListDoctorAdapter = new AddAndSetImageListDoctorAdapter(this, imagesEncodedList, this);
            binding.rclrImage.setAdapter(addAndSetImageListDoctorAdapter);
        }

        if (filePathsDoctor == null) {
            binding.btnVerify.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_disable));
            binding.btnVerify.setClickable(false);
        }

        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        getData(sessionManager.getUserId());

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filePathsDoctor != null) {

                    sendData();

                } else {

                    onBackPressed();
                }

            }
        });
    }

    void onProfileImageClick() {
        Dexter.withActivity(DoctorAddImageActivity.this)
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
        Intent intent = new Intent(DoctorAddImageActivity.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(DoctorAddImageActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorAddImageActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Uri uri12 = data.getParcelableExtra("path");
                if (data.getData() == null) {
                    binding.rclrImage.setVisibility(View.GONE);
                }

                if (uri12 != null) {         //on Single image selected

                    Uri uri = data.getData();

                    binding.rclrImage.setVisibility(View.VISIBLE);

                    bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri12);
                    uri = getImageUri(getApplicationContext(), bitmap2);
                    Log.e("uripath", "" + uri);
                    getImageFilePath(uri);


                } else {                              //on multiple image selected

                    if (data.getClipData() != null) {

                        ClipData mClipData = data.getClipData();

//                        if (mClipData.getItemCount() <= 4) {
                        binding.rclrImage.setVisibility(View.VISIBLE);
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();

                            bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            uri = getImageUri(getApplicationContext(), bitmap2);
                            Log.e("uripath", "" + uri);
                            getImageFilePath(uri);


                        }
                        Log.e("MainActivity", "Selected Images" + mClipData.getItemCount());

                        /*} else {
                            Toast.makeText(this, "Please Select Only 4 Picture", Toast.LENGTH_SHORT).show();
                        }*/
                    }

                }
            } else if (requestCode == PICK_IMAGE_MULTIPLE) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");
                    Uri selectedImage;
                    if (uri != null) {

                        binding.rclrImage.setVisibility(View.VISIBLE);

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
                            loadProfile(img2);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Something went wrong path", Toast.LENGTH_LONG).show();
                            Log.e("exception", e.getMessage());
                        }
                    } else {
                        Toast.makeText(this, "null data", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(this, "You haven't picked Image else", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("exception", e.getMessage());
            Toast.makeText(this, "Something went wrong : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    private void loadProfile(String url) {
        Log.e("TAG....", "Image cache path: " + url);

        checkImage(url);


    }


    // Get image file path
    public void getImageFilePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String absolutePathOfImage = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                if (absolutePathOfImage != null) {
                    Log.e("absolutePathOfImage", absolutePathOfImage);
                    checkImage(absolutePathOfImage);
                } else {
                    checkImage(String.valueOf(uri));
                }
            }
        }

    }


    public void checkImage(String filePath) {
        // Check before adding a new image to ArrayList to avoid duplicate images
        addImage(filePath);

    }

    public void addImage(String filePath) {


        Image image = new Image(filePath);

        filePathsDoctor.add(image);

        binding.btnVerify.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_doctor));
        binding.btnVerify.setClickable(true);

        if (imagesEncodedList == null) {
            imagesEncodedList = new ArrayList<>();
        }

        imagesEncodedList.add(image);

        Log.e("filepath...", filePath);

        int numberOfColumns = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        binding.rclrImage.setLayoutManager(gridLayoutManager);
        AddAndSetImageListDoctorAdapter addAndSetImageListDoctorAdapter = new AddAndSetImageListDoctorAdapter(this, imagesEncodedList, this);
        binding.rclrImage.setAdapter(addAndSetImageListDoctorAdapter);


    }

    public void sendData() {

        pd = new ProgressDialog(DoctorAddImageActivity.this);
        pd.setMessage("Please Wait.....");
        pd.setCancelable(false);
        pd.show();


        String business_id = sessionManager.getUserId();
        String header = "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject);
        String accesstoken = WS_URL_PARAMS.access_key;

        RequestBody t1 = RequestBody.create(MediaType.parse("multipart/form-data"), business_id);
        RequestBody t2 = RequestBody.create(MediaType.parse("multipart/form-data"), accesstoken);




            File file1 = null;
            MultipartBody.Part body1 = null;
        MultipartBody.Part[] body = new MultipartBody.Part[filePathsDoctor.size()];
            for (int i = 0; i < filePathsDoctor.size(); i++) {

                file1 = new File(filePathsDoctor.get(i).getPhoto());
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file1);

                String s = business_id + "_" + file1.getName();
                String logo = s.replaceAll(" ", "_");
                body1 = MultipartBody.Part.createFormData("image[]", logo, requestFile);
                body[i] = body1;
            }
            Log.e("files", "/--" + filePathsDoctor.size() + body.length);





        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseDoctorAddImage> call = api.sendImageFromDoctor(header, t2, t1, body);
        call.enqueue(new Callback<ResponseDoctorAddImage>() {
            @Override
            public void onResponse(Call<ResponseDoctorAddImage> call, Response<ResponseDoctorAddImage> response) {

                Log.e("responseAddImage", new Gson().toJson(response.body()));

                pd.dismiss();

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(DoctorAddImageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(DoctorAddImageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDoctorAddImage> call, Throwable t) {

                pd.dismiss();
                t.printStackTrace();
                Log.e("errorImage", t.getMessage());

            }
        });


    }

    public void getData(String userId) {

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseDoctorAddImageAndVedio> call = api.getImageAndVedioListDoctor("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);
        call.enqueue(new Callback<ResponseDoctorAddImageAndVedio>() {
            @Override
            public void onResponse(Call<ResponseDoctorAddImageAndVedio> call, Response<ResponseDoctorAddImageAndVedio> response) {

                Log.e("ResponceImgVidList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {
                        Toast.makeText(DoctorAddImageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        imagesEncodedList = response.body().getImage();

                        /*if (imagesEncodedList.size() > 0) {

                            binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button));
                            binding.btnVerify.setTextColor(getResources().getColor(R.color.white));


                        } else {
                            binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button_disable));
                            binding.btnVerify.setTextColor(getResources().getColor(R.color.clr_979592));

                        }*/

                        int numberOfColumns = 3;
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(DoctorAddImageActivity.this, numberOfColumns);
                        binding.rclrImage.setLayoutManager(gridLayoutManager);
                        AddAndSetImageListDoctorAdapter addAndSetImageListDoctorAdapter = new AddAndSetImageListDoctorAdapter(DoctorAddImageActivity.this, imagesEncodedList, DoctorAddImageActivity.this);
                        binding.rclrImage.setAdapter(addAndSetImageListDoctorAdapter);

                    }


                } else {

                    Toast.makeText(DoctorAddImageActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDoctorAddImageAndVedio> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(DoctorAddImageActivity.this, DoctorBusinessProfileActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onAddButtonClick(int postion) {

        onProfileImageClick();
    }

}