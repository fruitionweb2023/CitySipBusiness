package com.direct2web.citysip.Model.SpaAndSalon.Staff;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Staff {

@SerializedName("id")
@Expose
private String id;
@SerializedName("staff_name")
@Expose
private String staffName;
@SerializedName("about")
@Expose
private String about;
@SerializedName("time_from")
@Expose
private String timeFrom;
@SerializedName("time_to")
@Expose
private String timeTo;
@SerializedName("profile_image")
@Expose
private String profileImage;
@SerializedName("status")
@Expose
private String status;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getStaffName() {
return staffName;
}

public void setStaffName(String staffName) {
this.staffName = staffName;
}

public String getAbout() {
return about;
}

public void setAbout(String about) {
this.about = about;
}

public String getTimeFrom() {
return timeFrom;
}

public void setTimeFrom(String timeFrom) {
this.timeFrom = timeFrom;
}

public String getTimeTo() {
return timeTo;
}

public void setTimeTo(String timeTo) {
this.timeTo = timeTo;
}

public String getProfileImage() {
return profileImage;
}

public void setProfileImage(String profileImage) {
this.profileImage = profileImage;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

}