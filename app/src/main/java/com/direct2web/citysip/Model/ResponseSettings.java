package com.direct2web.citysip.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSettings {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("message")
@Expose
private String message;
@SerializedName("notification_setting")
@Expose
private List<NotificationSetting> notificationSetting = null;

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

public List<NotificationSetting> getNotificationSetting() {
return notificationSetting;
}

public void setNotificationSetting(List<NotificationSetting> notificationSetting) {
this.notificationSetting = notificationSetting;
}

}