package com.adag.entropi.modules.StolenInfo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.adag.entropi.modules.DBConverters;

import java.util.ArrayList;

@Entity(tableName = "stolen_data")
public class StolenInfo {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "sd.id")
    private String f_id;

    @ColumnInfo(name = "user.long")
    private String f_longitude;

    @ColumnInfo(name = "user.lat")
    private String f_latitude;

    @ColumnInfo(name = "env.light_value")
    private String f_light;

    @ColumnInfo(name = "env.temp")
    private String f_temp;

    @ColumnInfo(name = "env.hum")
    private String f_hum;

    @ColumnInfo(name = "app.item_list")
    private String f_item_list;

    public StolenInfo(){
        f_id = "not-set";
    }

    @NonNull
    public String getF_item_list() {
        return f_item_list;
    }

    public void setF_item_list(@NonNull String list){
        f_item_list = list;
    }

    @NonNull
    public String getF_hum() {
        return f_hum;
    }

    public void setF_hum(@NonNull String hum){
        f_hum = hum;
    }

    @NonNull
    public String getF_temp() {
        return f_temp;
    }

    public void setF_temp(@NonNull String temp){
        f_temp = temp;
    }

    @NonNull
    public String getF_light() {
        return f_light;
    }

    public void setF_light(@NonNull String light){
        f_light = light;
    }

    @NonNull
    public String getF_latitude() {
        return f_latitude;
    }

    public void setF_latitude(@NonNull String latitude){
        f_latitude = latitude;
    }

    @NonNull
    public String getF_longitude() {
        return f_longitude;
    }

    public void setF_longitude(@NonNull String longitude){
        f_longitude = longitude;
    }

    @NonNull
    public String getF_id() {
        return f_id;
    }

    public void setF_id(@NonNull String id){
        f_id = id;
    }
}
