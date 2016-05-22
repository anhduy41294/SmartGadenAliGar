package com.aliens.smartgarden.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aliens.smartgarden.Chart.ListViewBarChartActivity;
import com.aliens.smartgarden.Controller.LoaderHelper;
import com.aliens.smartgarden.Global.GlobalVariable;
import com.aliens.smartgarden.Model.Device;
import com.aliens.smartgarden.Model.Profile;
import com.aliens.smartgarden.Model.RecordAction;
import com.aliens.smartgarden.Model.RecordSituation;
import com.aliens.smartgarden.R;
import com.aliens.smartgarden.Service.RecordActionService;
import com.aliens.smartgarden.View.AllProfileView.AllProfileActivity;
import com.aliens.smartgarden.View.AllProfileView.AllProfileAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Spinner spinner;
    Button tuoinuocBtn, maiCheBtn;
    GlobalVariable globalVariable;
    RecordAction recordAction;
    TextView nhietDoTxt, doAmTxt, mayTuoiNuocStatus, manCheStatus;
    Handler handlerSituation;
    Handler handlerDevice;
    ImageButton imgEdit;
    MaterialNumberPicker numberPicker;
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        globalVariable = new GlobalVariable();
        setUpTuoiNuocDialog();
        setUpButton();

        nhietDoTxt = (TextView) findViewById(R.id.txtTemperature);
        doAmTxt = (TextView) findViewById(R.id.txtHumidity);
        mayTuoiNuocStatus = (TextView) findViewById(R.id.mayTuoiNuocStatus);
        manCheStatus = (TextView) findViewById(R.id.manCheStatus);

        RecordSituationAsyncTask recordSituationAsyncTask = new RecordSituationAsyncTask();
        recordSituationAsyncTask.execute();
        RecordSituationDeviceAsyncTask recordSituationDeviceAsyncTask = new RecordSituationDeviceAsyncTask();
        recordSituationDeviceAsyncTask.execute();
        getAllProfile getAllProfile = new getAllProfile();
        getAllProfile.execute();

//        recordAction = new RecordAction(1, "20");
//        SendRecordAction sendRecordAction = new SendRecordAction();
//        sendRecordAction.execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * Push Notification
         */
        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        String host = "http://aligarapi.apphb.com/";
        HubConnection connection = new HubConnection(host);

        HubProxy hub = connection.createHubProxy("MyHub");

        SignalRFuture<Void> awaitConnection = connection.start();
        try {
            awaitConnection.get();
        } catch (InterruptedException e) {
            // Handle ...
            e.printStackTrace();
        } catch (ExecutionException e) {
            // Handle ...
            e.printStackTrace();
        }
        handlerSituation = new Handler();
        handlerDevice = new Handler();
        hub.subscribe(this);

        hub.on("notifyNewSituation", new SubscriptionHandler1<String>() {
            @Override
            public void run(String msg) {
                //Log.d("result := ", msg);
                String[] separated = msg.split("=");
                final String fStatusTem = separated[0].substring(0, 2);
                final String fStatusHum = separated[1].substring(0, 2);
                Log.i("=======Notify", msg);
                handlerSituation.post( new Runnable() {
                    @Override
                    public void run() {
                        nhietDoTxt.setText(fStatusTem);
                        doAmTxt.setText(fStatusHum);
                    }
                } );
            }
        }, String.class);

        hub.on("notifyNew", new SubscriptionHandler1<String>() {
            @Override
            public void run(String msg) {
                //Log.d("result := ", msg);
                Log.i("=======Notify", msg);
            }
        }, String.class);

        hub.on("notifyNewDeviceStatus", new SubscriptionHandler1<String>() {
            @Override
            public void run(String msg) {
                //Log.d("result := ", msg);
                String[] separated = msg.split("=");
                final String fDevice = separated[0];
                final String fStatus = separated[1];
                Log.i("=======Notify", msg);
                handlerDevice.post( new Runnable() {
                    @Override
                    public void run() {
                        if (Integer.valueOf(fDevice) == 1)
                        {
                            if (Integer.valueOf(fStatus) == 1)
                            {
                                tuoinuocBtn.setText("Tắt Tưới nước");
                                mayTuoiNuocStatus.setText("Mở");
                            }else
                            {
                                tuoinuocBtn.setText("Mở Tưới nước");
                                mayTuoiNuocStatus.setText("Tắt");
                            }
                        }else
                        {
                            if (Integer.valueOf(fStatus) == 1)
                            {
                                maiCheBtn.setText("Tắt Mái che");
                                manCheStatus.setText("Mở");
                            }else
                            {
                                maiCheBtn.setText("Mở Mái che");
                                manCheStatus.setText("Tắt");
                            }
                        }
                    }
                } );
            }
        }, String.class);
        //Test
        imgEdit = (ImageButton) findViewById(R.id.imgEditProfile);
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(), EditProfileActivity.class);
                i.putExtra("IdProfile", 2);
                startActivity(i);
            }
        });
    }

    private void setUpTuoiNuocDialog() {
         numberPicker = new MaterialNumberPicker.Builder(getApplicationContext())
                .minValue(1)
                .maxValue(20)
                .defaultValue(10)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Thời gian tưới nước (phút):")
                .setView(numberPicker)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), String.valueOf(numberPicker.getValue()), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;
        if (id == R.id.nav_home) {
            i = new Intent(this, AllProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_chart) {
            i = new Intent(this, ListViewBarChartActivity.class);
            startActivity(i);
        }else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setUpSpinner() {
        spinner = (Spinner) findViewById(R.id.spnUserProfile);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (Profile p:globalVariable.allProfile) {
            categories.add(p.getProfileName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void setUpButton() {
        tuoinuocBtn = (Button) findViewById(R.id.btnWater);
        maiCheBtn = (Button) findViewById(R.id.btnCover);

        tuoinuocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalVariable.isTuoiNuoc) {
                    globalVariable.isTuoiNuoc = false;
                    tuoinuocBtn.setText("Mở tưới nước");
                    mayTuoiNuocStatus.setText("Mở");
                } else {
                    alertDialog.show();
                    globalVariable.isTuoiNuoc = true;
                    tuoinuocBtn.setText("Tắt Tưới nước");
                    mayTuoiNuocStatus.setText("Tắt");
                }
            }
        });

        maiCheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalVariable.isManChe) {
                    globalVariable.isManChe = false;
                    maiCheBtn.setText("Tắt mái che");
                    manCheStatus.setText("Mở");
                } else {
                    globalVariable.isManChe = true;
                    maiCheBtn.setText("Mở mái che");
                    manCheStatus.setText("Tắt");
                }
            }
        });
    }

    /**
     * AsyncTask
     */
    public class RecordSituationAsyncTask extends AsyncTask<String, Void, RecordSituation> {

        public RecordSituationAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected RecordSituation doInBackground(String... params) {

            LoaderHelper loaderHelper = new LoaderHelper();
            return loaderHelper.getLastestSituation();
        }

        @Override
        protected void onPostExecute(RecordSituation o) {
            super.onPostExecute(o);

            //Set value to UI
            nhietDoTxt.setText(String.valueOf((int)o.getTemperature()));
            doAmTxt.setText(String.valueOf((int)o.getHumidity()));
        }
    }

    /**
     * AsyncTask
     */
    public class SendRecordAction extends AsyncTask<String, Void, String> {

        public SendRecordAction() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            RecordActionService recordActionService = new RecordActionService();
            return recordActionService.postRecordAction(recordAction);
        }

        @Override
        protected void onPostExecute(String respond) {
            super.onPostExecute(respond);

        }
    }

    /**
     * AsyncTask
     */
    public class RecordSituationDeviceAsyncTask extends AsyncTask<String, Void, ArrayList<Device>> {

        public RecordSituationDeviceAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Device> doInBackground(String... params) {

            LoaderHelper loaderHelper = new LoaderHelper();
            return loaderHelper.getAllDeviceStatus();
        }

        @Override
        protected void onPostExecute(ArrayList<Device> devices) {
            super.onPostExecute(devices);

            //Set value to UI
            globalVariable.isTuoiNuoc = devices.get(0).isDeviceStatus();
            globalVariable.isManChe = devices.get(1).isDeviceStatus();
            tuoinuocBtn.setText(devices.get(0).isDeviceStatus()?"Tắt tưới nước" : "Mở tưới nước");
            maiCheBtn.setText(devices.get(1).isDeviceStatus()?"Tắt mái che" : "Mở mái che");
            mayTuoiNuocStatus.setText(devices.get(0).isDeviceStatus()?"Mở" : "Tắt");
            manCheStatus.setText(devices.get(1).isDeviceStatus()?"Mở" : "Tắt");
        }
    }

    /**
     * AsyncTask
     */
    public class getAllProfile extends AsyncTask<String, Void, ArrayList<Profile>> {

        public getAllProfile() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Profile> doInBackground(String... params) {

            LoaderHelper loaderHelper = new LoaderHelper();
            return loaderHelper.getAllProfile();
        }

        @Override
        protected void onPostExecute(ArrayList<Profile> allProfile) {
            super.onPostExecute(allProfile);
            globalVariable.allProfile = allProfile;
            setUpSpinner();
        }
    }
}
