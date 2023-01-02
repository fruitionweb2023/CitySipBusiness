package com.direct2web.citysip.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FcmTokenRegistr {

    @SuppressLint("LongLogTag")
    public void tokenRegister(String token, Context context) {


        SessionManager sessionManager = new SessionManager(context);
        sessionManager.setToken(token);
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (!sessionManager.getUserId().equals("0") && !sessionManager.getUserId().equals("")) {
            Api api = RetrofitClient.getClient().create(Api.class);
            Log.e("sessionManager.getUserId()....", "...." + sessionManager.getUserId());
            Call<JsonObject> call = api.setToken("Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer,WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key, token, sessionManager.getUserId(), android_id);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("Fcm responce...", response.toString());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }

    }

}
