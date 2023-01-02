package com.direct2web.citysip.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

public class facebookuttils {


    public static String getFacebookUrl(FragmentActivity activity, String facebook_url) {
        if (activity == null || activity.isFinishing()) return null;

        PackageManager packageManager = activity.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                Log.d("facebook Api", "new");
                return "fb://facewebmodal/f?href=" + facebook_url;
            } else { //older versions of fb app
                Log.d("facebook Api", "old");
                return "fb://page/" + splitUrl(activity, facebook_url);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("facebook Api", "exception");
            return facebook_url; //normal web url
        }
    }

    /***
     * this method used to get the facebook profile name only , this method split domain into two part index 0 contains https://www.facebook.com and index 1 contains after / part
     * @param context contain context
     * @param url contains facebook url like https://www.facebook.com/kfc
     * @return if it successfully split then return "kfc"
     *
     * if exception in splitting then return "https://www.facebook.com/kfc"
     *
     */
    public static String splitUrl(Context context, String url) {
        if (context == null) return null;
        Log.d("Split string: ", url + " ");
        try {
            String[] splittedUrl = url.split(".com/");
            Log.d("Split string: ", splittedUrl[1] + " ");
            return splittedUrl.length == 2 ? splittedUrl[1] : url;
        } catch (Exception ex) {
            return url;
        }
    }


}
