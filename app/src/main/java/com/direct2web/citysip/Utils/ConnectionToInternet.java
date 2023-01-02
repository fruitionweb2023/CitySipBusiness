package com.direct2web.citysip.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Abidbeg 1 on 13-02-2016.
 */
public class ConnectionToInternet {

    private Context _context;

    public ConnectionToInternet(Context context){
        this._context = context;
    }

    public  boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public void ShowDilog(Activity activity){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("oops..!");
        alertDialog.setMessage("Your Internet Connection is Off!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               // finish();
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
