package com.adag.entropi.modules.StolenInfo;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface StolenInfoDAO {
    @Query("SELECT * FROM stolen_data")
    LiveData<List<StolenInfo>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(StolenInfo stolenInfo);
}
