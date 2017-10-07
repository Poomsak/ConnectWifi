package com.example.lenovo.connetcttest;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Switch switchwifi;
    private TextView tvstatus;
    private WifiManager wifimanager;
    private Button btn_connect;
    private RecyclerView lv;
    private GridLayoutManager gridLayoutManager;
    private AdapterWiFi adapterWiFi;
    private List<ScanResult> results;
    private String ITEM_KEY = "key";
    private ArrayList<Modelvalue> arraylist;
    //SimpleAdapter adapter;
    private WifiManager wifi;
    private int size = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvstatus = (TextView) findViewById(R.id.tv_status);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        lv = (RecyclerView) findViewById(R.id.re_scan);
        arraylist = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(MainActivity.this,1);
        adapterWiFi = new AdapterWiFi(MainActivity.this,arraylist);
        lv.setLayoutManager(gridLayoutManager);
        lv.setAdapter(adapterWiFi);
        lv.setHasFixedSize(true);

        /*final String networkSSID = "SoftCare";
        final String networkPass = "88888888";*/

        switchwifi = (Switch) findViewById(R.id.switchwifi);
        wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        /*if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }*/
        if (wifi.isWifiEnabled()==false){
            switchwifi.setChecked(false);
            tvstatus.setText("WiFi OFF");
        }else {
            switchwifi.setChecked(true);
            tvstatus.setText("WiFi ON");
        }
        /*this.adapter = new SimpleAdapter(MainActivity.this, arraylist,
                R.layout.row, new String[] { ITEM_KEY },
                new int[]
                { R.id.list_value });*/

        adapterWiFi.SetOnItemClickListener(new AdapterWiFi.OnClickWiFi() {
            @Override
            public void onClickConnect(View view, final String Name) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_wifi);
                dialog.setCancelable(true);

                TextView tv_Name = (TextView)dialog.findViewById(R.id.tv_Name);
                tv_Name.setText(Name);
                final EditText ed_pass = (EditText) dialog.findViewById(R.id.ed_pass);
                ed_pass.setInputType(InputType.TYPE_CLASS_NUMBER);
                 Button btn_connect = (Button) dialog.findViewById(R.id.btn_connect);
                btn_connect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WifiConfiguration wifiConfig = new WifiConfiguration();
                        wifiConfig.SSID = String.format("\"%s\"", Name);
                        wifiConfig.preSharedKey = String.format("\"%s\"", ed_pass.getText().toString().trim());

                        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);

                        int netId = wifiManager.addNetwork(wifiConfig);
                        wifiManager.disconnect();
                        wifiManager.enableNetwork(netId, true);
                        wifiManager.reconnect();
                        dialog.cancel();
                    }
                });
                dialog.show();

            }
        });
        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                results = wifi.getScanResults();
                size = results.size();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));



        switchwifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if (isChecked) {
                    tvstatus.setText("WiFi ON");

                    EnableWiFi();

                } else {
                    tvstatus.setText("WiFi OFF");

                    DisableWiFi();

                }
            }
        });

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Scan();
                /*WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = String.format("\"%s\"", networkSSID);
                wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

                WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);

                int netId = wifiManager.addNetwork(wifiConfig);
                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();*/

            }
        });

    }

    public void Scan(){
        arraylist.clear();
        wifi.startScan();

        Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
        try
        {
            size = size - 1;
            while (size >= 0)
            {
                /*HashMap<String, String> item = new HashMap<String, String>();
                item.put(ITEM_KEY, results.get(size).SSID);*/// + "  " + results.get(size).capabilities

                Modelvalue modelvalue = new Modelvalue();
                modelvalue.setName(results.get(size).SSID);
                modelvalue.setWPA(results.get(size).capabilities);
                arraylist.add(modelvalue);
                size--;
                adapterWiFi.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        { }
    }
    public void EnableWiFi(){
        wifimanager.setWifiEnabled(true);
    }

    public void DisableWiFi(){
        wifimanager.setWifiEnabled(false);
    }

}
