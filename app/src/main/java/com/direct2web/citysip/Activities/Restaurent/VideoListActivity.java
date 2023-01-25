package com.direct2web.citysip.Activities.Restaurent;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cocosw.bottomsheet.BottomSheet;
import com.direct2web.citysip.Activities.VideoViewActivity;
import com.direct2web.citysip.Adapter.RestaurentAdapters.SelectedVideoAdapter;
import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.ResponseAddVideo;
import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.ResponseImageVideoList;
import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.Video;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.RetrofitClient;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.Utils.WS_URL_PARAMS;
import com.direct2web.citysip.databinding.ActivityVideoListBinding;
import com.google.gson.Gson;
import com.gowtham.library.utils.LogMessage;
import com.gowtham.library.utils.TrimType;
import com.gowtham.library.utils.TrimVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListActivity extends AppCompatActivity implements SelectedVideoAdapter.OnItemClickListner, SelectedVideoAdapter.OnVideoItemClickListner {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 100;
    private static final int VIDEO_TRIM = 101;
    ActivityVideoListBinding binding;
    SessionManager sessionManager;
    boolean clicked = false;


    List<Video> imagesEncodedList;
    ArrayList<Video> filePaths = new ArrayList<>();

    ActivityResultLauncher<Intent> videoTrimResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));
                    Log.e("VideoTrimActivity", "Trimmed path:: " + uri);



                    /*binding.videoView.setMediaController(mediaController);
                    binding.videoView.setVideoURI(uri);
                    binding.videoView.requestFocus();
                    binding.videoView.start();

                    binding.videoView.setOnPreparedListener(mediaPlayer -> {
                        mediaController.setAnchorView(binding.videoView);
                    });*/

                    String filepath = String.valueOf(uri);
                    File file = new File(filepath);
                    long length = file.length();
                    addImage(filepath, uri);
                    Log.e("VideoTrimActivity", "Video size:: " + (length / 1024));
                } else
                    LogMessage.v("videoTrimResultLauncher data is null");
            });

    ActivityResultLauncher<Intent> takeOrSelectVideoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    Intent data = result.getData();
                    //check video duration if needed
        /*        if (TrimmerUtils.getDuration(this,data.getData())<=30){
                    Toast.makeText(this,"Video should be larger than 30 sec",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                    if (data.getData() != null) {
                        LogMessage.v("Video path:: " + data.getData());
                        openTrimActivity(String.valueOf(data.getData()));
                    } else {
                        Toast.makeText(this, "video uri is null", Toast.LENGTH_SHORT).show();
                    }
                } else
                    LogMessage.v("takeVideoResultLauncher data is null");
            });
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_list);

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
                Intent intent = new Intent(VideoListActivity.this,DeleteVideoActivity.class);
                startActivity(intent);
            }
        });
        if (imagesEncodedList != null) {
            int numberOfColumns = 3;
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,numberOfColumns);
            binding.rclrVideo.setLayoutManager(gridLayoutManager);
            SelectedVideoAdapter propertyImageAdapter = new SelectedVideoAdapter(this, imagesEncodedList,this,this);
            binding.rclrVideo.setAdapter(propertyImageAdapter);
        }

        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

        getData(sessionManager.getUserId());

        /*binding.txtSetUpLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VideoListActivity.this, SetUpTimingActivity.class);
                startActivity(intent);

            }
        });*/

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePaths != null) {

                    sendData();

                } else {

                    /*android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VideoListActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Are you sure want to go further without adding Video?");
                    builder.setTitle("Confirmation");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(VideoListActivity.this, SetUpTimingActivity.class);
                            startActivity(intent);

                        }
                    });

                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });

                    android.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();*/

                    onBackPressed();


                }

            }
        });

     /*   binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPictureDialog();

//                pickVideo();

                *//*Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);*//*

//                showPictureDialog();

                if (clicked) {

                    Toast.makeText(VideoListActivity.this, "Please Confirm How many Video Allowed to Upload", Toast.LENGTH_SHORT).show();

                } else {
                    clicked = true;
                    onMinToMaxTrimClicked();
                }


            }

        });*/


    }

    private void getData(String userId) {

        Api api = RetrofitClient.getClient().create(Api.class);
        Call<ResponseImageVideoList> call = api.getMedia("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key, userId);
        call.enqueue(new Callback<ResponseImageVideoList>() {
            @Override
            public void onResponse(Call<ResponseImageVideoList> call, Response<ResponseImageVideoList> response) {

                Log.e("ResponceImgVidList", new Gson().toJson(response.body()));

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {
                        Toast.makeText(VideoListActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        imagesEncodedList = response.body().getVideo();

                        if (imagesEncodedList == null){
                            clicked = true;
                        }else {
                            clicked = false;
                        }

                        if (imagesEncodedList.size() > 0 ){

                            binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button));
                            binding.btnVerify.setTextColor(getResources().getColor(R.color.white));


                        }else {
                            binding.btnVerify.setBackground(getResources().getDrawable(R.drawable.button_disable));
                            binding.btnVerify.setTextColor(getResources().getColor(R.color.clr_979592));

                        }

                        int numberOfColumns = 3;
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(VideoListActivity.this,numberOfColumns);
                        binding.rclrVideo.setLayoutManager(gridLayoutManager);
                        SelectedVideoAdapter propertyImageAdapter = new SelectedVideoAdapter(VideoListActivity.this, imagesEncodedList,VideoListActivity.this,VideoListActivity.this);
                        binding.rclrVideo.setAdapter(propertyImageAdapter);


                    }


                } else {

                    Toast.makeText(VideoListActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ResponseImageVideoList> call, Throwable t) {

                if (pd.isShowing()) {
                    pd.dismiss();
                }
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });


    }

    /*private void pickVideo() {
        new VideoPicker.Builder(SetUpVideoActivity.this)
                .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                .directory(VideoPicker.Directory.DEFAULT)
                .extension(VideoPicker.Extension.MP4)
                .build();
    }*/


    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select video from gallery",
                "Record video from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                chooseVideoFromGallary();
                                break;
                            case 1:
                                takeVideoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void chooseVideoFromGallary() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
    }

    private void takeVideoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();

                // MEDIA GALLERY
                String path = getPath2(selectedImageUri);
                if (path != null) {
                    File file = new File(path);
                    if (file.exists()) {
                        startActivityForResult(new Intent(VideoListActivity.this,
                                        VideoTrimmerActivity.class).putExtra("EXTRA_PATH", path),
                                VIDEO_TRIM);
                        overridePendingTransition(0, 0);
                    } else {
                        Toast.makeText(VideoListActivity.this, "Please select proper video", Toast.LENGTH_LONG);
                    }
                }
            }
        } else if (requestCode == VIDEO_TRIM) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

//                    binding.vv.setVisibility(View.VISIBLE);
                    String videoPath = data.getExtras().getString("INTENT_VIDEO_FILE");

                    String path = getPath2(Uri.parse(videoPath));

                    Log.e("videopath", videoPath);
                    Log.e("path", path);

                    Uri returnedUri = null;
                    if (Uri.parse(data.getExtras().getString("INTENT_VIDEO_URI")).getScheme() == null) {
//                        returnedUri = Uri.fromFile(new File(path));
                        // or you can just do -->
                        returnedUri = Uri.parse("file://" + Uri.parse(data.getExtras().getString("INTENT_VIDEO_URI")).getPath());
                        Log.e("uri", returnedUri.toString());
                    } else {
                        returnedUri = Uri.parse(data.getExtras().getString("INTENT_VIDEO_URI"));
                        Log.e("uri1", returnedUri.toString());
                    }

                    Uri myUri = returnedUri;


//                    binding.vv.setVideoURI(myUri);
//                    binding.vv.requestFocus();
//                    binding.vv.start();

                    addImage(videoPath, myUri);


                    Toast.makeText(VideoListActivity.this, videoPath, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    public void addImage(String filePath, Uri fileUri) {


        Video image = new Video(fileUri);

        Video video = new Video(filePath);

        filePaths.add(video);

        if (imagesEncodedList == null) {
            imagesEncodedList = new ArrayList<>();
        }

        imagesEncodedList.add(image);

        Log.e("filepath...", filePath);

        int numberOfColumns = 3;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,numberOfColumns);
        binding.rclrVideo.setLayoutManager(gridLayoutManager);
        SelectedVideoAdapter propertyImageAdapter = new SelectedVideoAdapter(this, imagesEncodedList,VideoListActivity.this,VideoListActivity.this);
        binding.rclrVideo.setAdapter(propertyImageAdapter);
        //selectedImageAdapter.notifyDataSetChanged();
        //imageAdapter.notifyDataSetChanged();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPath2(Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(VideoListActivity.this, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(this, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return "";
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            int currentApiVersion = Build.VERSION.SDK_INT;
            //TODO changes to solve gallery video issue
            if (currentApiVersion > Build.VERSION_CODES.M && uri.toString().contains(getString(R.string.app_provider))) {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (cursor.getString(column_index) != null) {
                        String state = Environment.getExternalStorageState();
                        File file;
                        if (Environment.MEDIA_MOUNTED.equals(state)) {
                            file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", cursor.getString(column_index));
                        } else {
                            file = new File(context.getFilesDir(), cursor.getString(column_index));
                        }
                        return file.getAbsolutePath();
                    }
                    return "";
                }
            } else {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    if (cursor.getString(column_index) != null) {
                        return cursor.getString(column_index);
                    }
                    return "";
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return "";
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    private void openTrimActivity(String data) {

        TrimVideo.activity(data)
                .setTrimType(TrimType.MIN_MAX_DURATION)
                .setMinToMax(getEdtValueLong("10"), getEdtValueLong("30"))
                .start(this, videoTrimResultLauncher);
    }


    private void onMinToMaxTrimClicked() {
        if (isEdtTxtEmpty("10"))
            Toast.makeText(this, "Enter min gap duration", Toast.LENGTH_SHORT).show();
        else if (isEdtTxtEmpty("30"))
            Toast.makeText(this, "Enter max gap duration", Toast.LENGTH_SHORT).show();
        else if (checkCamStoragePer())
            showVideoOptions();
    }

    public void showVideoOptions() {
        try {
            BottomSheet.Builder builder = getBottomSheet();
            builder.sheet(R.menu.menu_video);
            builder.listener(item -> {
                if (R.id.action_take == item.getItemId())
                    captureVideo();
                else
                    openVideo();
                return false;
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BottomSheet.Builder getBottomSheet() {
        return new BottomSheet.Builder(this).title(R.string.txt_option);
    }

    public void captureVideo() {
        try {
            Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
            intent.putExtra("android.intent.extra.durationLimit", 30);
            takeOrSelectVideoResultLauncher.launch(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openVideo() {
        try {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            takeOrSelectVideoResultLauncher.launch(Intent.createChooser(intent, "Select Video"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (isPermissionOk(grantResults))
            showVideoOptions();
    }

    private boolean isEdtTxtEmpty(String editText) {
        return editText.trim().isEmpty();
    }

    private long getEdtValueLong(String editText) {
        return Long.parseLong(editText.trim());
    }

    private boolean checkCamStoragePer() {
        return checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    private boolean checkPermission(String... permissions) {
        boolean allPermitted = false;
        for (String permission : permissions) {
            allPermitted = (ContextCompat.checkSelfPermission(this, permission)
                    == PackageManager.PERMISSION_GRANTED);
            if (!allPermitted)
                break;
        }
        if (allPermitted)
            return true;
        ActivityCompat.requestPermissions(this, permissions,
                220);
        return false;
    }

    private boolean isPermissionOk(int... results) {
        boolean isAllGranted = true;
        for (int result : results) {
            if (PackageManager.PERMISSION_GRANTED != result) {
                isAllGranted = false;
                break;
            }
        }
        return isAllGranted;
    }


    public void sendData() {

        pd = new ProgressDialog(VideoListActivity.this);
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
        MultipartBody.Part[] body = new MultipartBody.Part[filePaths.size()];

        for (int i = 0; i < filePaths.size(); i++) {

            file1 = new File(filePaths.get(i).getVideo());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);

            String s = business_id + "_" + file1.getName();
            String logo = s.replaceAll(" ", "_");
            body1 = MultipartBody.Part.createFormData("image[]", logo, requestFile);
            body[i] = body1;
        }

        Log.e("files", "/--" + filePaths.size());


        Api api = RetrofitClient.getClient().create(Api.class);

        Call<ResponseAddVideo> call = api.sendVideo(header, t2, t1, body);
        call.enqueue(new Callback<ResponseAddVideo>() {
            @Override
            public void onResponse(Call<ResponseAddVideo> call, Response<ResponseAddVideo> response) {

                Log.e("responseAddImage", new Gson().toJson(response.body()));

                pd.dismiss();

                if (response.body() != null && response.isSuccessful()) {

                    if (response.body().getError()) {

                        Toast.makeText(VideoListActivity.this, getResources().getString(R.string.error_admin), Toast.LENGTH_SHORT).show();

                    } else {

                        /*Intent intent = new Intent(VideoListActivity.this, SetUpTimingActivity.class);
                        finish();
                        startActivity(intent);*/

                        Toast.makeText(VideoListActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();


                    }

                }


            }

            @Override
            public void onFailure(Call<ResponseAddVideo> call, Throwable t) {

                pd.dismiss();
                t.printStackTrace();
                Log.e("errorImage", t.getMessage());

            }
        });
    }


    @Override
    public void onAddButtonClick(int postion) {
        onMinToMaxTrimClicked();
       /* if (clicked) {

            Toast.makeText(VideoListActivity.this, "Please Confirm How many Video Allowed to Upload", Toast.LENGTH_SHORT).show();

        } else {
            clicked = true;
            onMinToMaxTrimClicked();
        }*/
    }

    @Override
    public void onPlayVideoButtonClick(int postion) {

        Intent intent = new Intent(VideoListActivity.this, VideoViewActivity.class);
        intent.putExtra("video", imagesEncodedList.get(postion).getVideo());
        Log.e("SendUrlVideo : ", "URL : " + imagesEncodedList.get(postion).getVideo());
        startActivity(intent);
    }
}