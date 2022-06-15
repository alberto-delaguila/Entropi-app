package com.adag.entropi.modules.StolenInfo;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StolenInfoRepository {
    private StolenInfoDAO stolenInfoDAO;
    private LiveData<List<StolenInfo>> stolenInfoList;

    public StolenInfoRepository(Context context) {
        StolenInfoDB db = StolenInfoDB.getInstance(context);
        stolenInfoDAO = db.getStolenInfoDAO();
        stolenInfoList = stolenInfoDAO.getAll();
    }

    public LiveData<List<StolenInfo>> getAllStolenInfoList() {
        return stolenInfoList;
    }

    public void insert(StolenInfo stolenInfo){
        StolenInfoDB.dbExecutor.execute(
                () -> stolenInfoDAO.insert(stolenInfo)
        );
    }
}
