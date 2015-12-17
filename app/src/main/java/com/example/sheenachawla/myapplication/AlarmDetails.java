package com.example.sheenachawla.myapplication;

import java.io.Serializable;

/**
 * Created by sheenachawla on 13/12/15.
 */
public class AlarmDetails implements Serializable {

    private String name = "";
    private String label = "Alarm";
    private Boolean snooze = false;
    private Boolean onOff = false;
    private Long timeMilli;

    public void setName(String name) {
        this.name = name;
    }
    public void setSnooze(Boolean snooze){
        this.snooze = snooze;
    }
    public void setOnOff(Boolean onOff){
        this.onOff = onOff;
    }
    public void setTimeMilli(Long timeMilli){
        this.timeMilli = timeMilli;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public String getName() {
        return name;
    }
    public Long getTimeMilli() {
        return timeMilli;
    }
    public Boolean getSnooze() {
        return snooze;
    }
    public Boolean getOnOff() {
        return onOff;
    }
    public String getLabel() {

        return label;
    }


}