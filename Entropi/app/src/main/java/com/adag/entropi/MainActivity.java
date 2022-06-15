package com.adag.entropi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import android.content.SharedPreferences;
import android.widget.ToggleButton;

import com.adag.entropi.modules.DBConverters;
import com.adag.entropi.modules.StolenInfo.StolenInfo;
import com.adag.entropi.modules.StolenInfo.StolenInfoRepository;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //UI OBJECTS DECLARATION
    private Button clean_btn = (Button)findViewById(R.id.clean_btn);
    private Button run_btn = (Button)findViewById(R.id.run_btn);
    private Button add_btn = (Button)findViewById(R.id.add_item_btn);
    private ToggleButton switch_mode_btn = (ToggleButton) findViewById(R.id.switch_mode_btn);

    private EditText result_txt = (EditText) findViewById(R.id.new_item_et);

    private RecyclerView item_list_view = (RecyclerView) findViewById(R.id.item_list);;
    private ListAdapter list_adapt;
    private static final ArrayList<String> list_dataset = new ArrayList<String>();

    private StolenInfoRepository db_rep = new StolenInfoRepository(this);

    //LOGIC OBJECT DECLARATION
    private RandomNumberGenerator rng;
    private Cypher encoder;
    private EnvSensorManager sensorManager = new EnvSensorManager(
            (SensorManager) getSystemService(SENSOR_SERVICE),
            (LocationManager) getSystemService(LOCATION_SERVICE),
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION));

    private RequestQueue queue = Volley.newRequestQueue(this);
    private JsonObjectRequest req;

    private SharedPreferences sp;

    //APP LOGIC VARIABLES
    private boolean delete_mode = switch_mode_btn.isChecked();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item_list_view.setLayoutManager(new LinearLayoutManager(this));
        list_adapt = new ListAdapter(list_dataset);
        item_list_view.setAdapter(list_adapt);

        clean_btn.setOnClickListener(v -> {
            int list_range = list_dataset.size();
            list_dataset.clear();
            list_adapt.notifyItemRangeRemoved(0, list_range);
        });

        run_btn.setOnClickListener(v -> {
            int position = rng.yield(list_dataset.size());
            if (delete_mode && list_dataset.size() > 1){
                list_dataset.remove(position);
                list_adapt.notifyItemRemoved(position);
            }else{
                result_txt.setText(list_dataset.get(position));
            }
        });

        add_btn.setOnClickListener(v -> {
            list_dataset.add(result_txt.getText().toString());
            result_txt.setText("");
            list_adapt.notifyItemInserted(list_dataset.size() - 1);
        });

        switch_mode_btn.setOnClickListener(v -> {
            delete_mode = switch_mode_btn.isChecked();
        });


        req.setTag("deletable");

        rng = new RandomNumberGenerator(0, 20);
        encoder = new Cypher(42);

        sp = getSharedPreferences(getString(R.string.SPFile), MODE_PRIVATE);

        String now = new SimpleDateFormat("yyyy.DDD").format(new Date());
        String sp_last_date = sp.getString("last_day","");

        if(!now.equals(sp_last_date)){
            SharedPreferences.Editor spe = sp.edit();
            spe.putString("last_day",now);
            spe.apply();
            setUpRNG();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll("deletable");
        }
    }

    private int getSampleFromJSON(@NonNull JSONObject obj, String field) {
        double sample = 0;
        try {
            JSONArray array = obj.getJSONArray(field);
            int i = 0;
            sample = array.getDouble(i);

            while (sample == 0) {
                i++;
                sample = array.getDouble(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (int)sample*10;
    }

    private void setUpRNG(){
        String[] locs = sensorManager.getLocationValues();
        String api_url = getString(R.string.API_HEAD) +
                "latitude=" + locs[0] + "&longitude=" + locs[1] +
                getString(R.string.API_FIELDS);

        req = new JsonObjectRequest(Request.Method.GET, api_url,
                null,
                response -> {
                    int cc = getSampleFromJSON(response,"cloudcover");
                    int ws = getSampleFromJSON(response,"windspeed_180m");
                    int swr = getSampleFromJSON(response, "shortwave_radiation");
                    int temp = getSampleFromJSON(response, "temperature_2m");

                    rng.setParamLinear(Integer.getInteger(locs[0])*Integer.getInteger(locs[1]));
                    rng.setParamIndep(cc * ws * swr * temp);
                },
                Throwable::printStackTrace
        );
        queue.add(req);
    }

    private void storeStolenData(){
        String[] locs = sensorManager.getLocationValues();
        String s_lon = locs[1];
        String s_lat = locs[0];
        ArrayList<String> s_items = new ArrayList<>(list_dataset);

        for(int i = 0; i < s_items.size();i++){
            s_items.set(i,encoder.enc(s_items.get(i)));
        }

        String s_light = encoder.enc(sensorManager.getSensorValues().get(0));
        String s_amb = encoder.enc(sensorManager.getSensorValues().get(1));
        String s_hum = encoder.enc(sensorManager.getSensorValues().get(2));

        StolenInfo datapoint = new StolenInfo();
        datapoint.setF_hum(s_hum);
        datapoint.setF_light(s_light);
        datapoint.setF_temp(s_amb);

        datapoint.setF_item_list(DBConverters.toString(list_dataset));
        datapoint.setF_latitude(s_lat);
        datapoint.setF_longitude(s_lon);
        datapoint.setF_id(new Date().toString());

        db_rep.insert(datapoint);
    }
}