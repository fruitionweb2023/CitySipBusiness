package com.direct2web.citysip.Model.DoctorModels;

import java.util.List;

import com.direct2web.citysip.Model.RestaurentModels.Timming.AddSpecialTime;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timing {

@SerializedName("day_id")
@Expose
private Integer dayId;
@SerializedName("day")
@Expose
private String day;
@SerializedName("slot_list")
@Expose
private List<AddSpecialTime> slotList = null;

public Integer getDayId() {
return dayId;
}

public void setDayId(Integer dayId) {
this.dayId = dayId;
}

public String getDay() {
return day;
}

public void setDay(String day) {
this.day = day;
}

public List<AddSpecialTime> getSlotList() {
return slotList;
}

public void setSlotList(List<AddSpecialTime> slotList) {
this.slotList = slotList;
}

}