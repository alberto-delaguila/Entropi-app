package com.adag.entropi.modules;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;

public class DBConverters {
    @TypeConverter
    public static String toString(ArrayList<String> list){
        return String.join(",",list);
    }

    public static ArrayList<String> toArrayList(String list) {
        return new ArrayList<>(Arrays.asList(list.split(",")));
    }
}
