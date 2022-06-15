package com.adag.entropi.modules.StolenInfo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {StolenInfo.class}, version = 1, exportSchema = false)
public abstract class StolenInfoDB extends RoomDatabase {

    public abstract StolenInfoDAO getStolenInfoDAO();

    public static final String DB_NAME = "stolen_info_db";
    private static StolenInfoDB INSTANCE;
    private static final int THREADS = 4;

    public static final ExecutorService dbExecutor = Executors.newFixedThreadPool(THREADS);

    public static StolenInfoDB getInstance(final Context context){
        if(INSTANCE == null){
            synchronized (StolenInfoDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StolenInfoDB.class, DB_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
