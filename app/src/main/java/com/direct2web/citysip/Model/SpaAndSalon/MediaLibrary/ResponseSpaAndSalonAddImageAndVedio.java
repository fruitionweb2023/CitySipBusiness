package com.direct2web.citysip.Model.SpaAndSalon.MediaLibrary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSpaAndSalonAddImageAndVedio {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("message")
@Expose
private String message;
@SerializedName("image")
@Expose
private List<Image> image = null;
@SerializedName("video")
@Expose
private List<Video> video = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<Image> getImage() {
return image;
}

public void setImage(List<Image> image) {
this.image = image;
}

public List<Video> getVideo() {
return video;
}

public void setVideo(List<Video> video) {
this.video = video;
}

}