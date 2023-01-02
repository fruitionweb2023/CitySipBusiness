package com.direct2web.citysip.Model.RestaurentModels.ImageVideo;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("business_id")
    @Expose
    private String businessId;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("uri")
    @Expose
    private Uri uri;

    private boolean selected = false ;

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Video(Uri uri) {
        this.uri = uri;
    }

    public Video(String video) {
        this.video = video;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
return businessId;
}

public void setBusinessId(String businessId) {
this.businessId = businessId;
}

public String getVideo() {
return video;
}

public void setVideo(String video) {
this.video = video;
}

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}