package com.aliens.smartgarden.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import com.aliens.smartgarden.Chart.ListViewBarChartActivity;
import com.aliens.smartgarden.Controller.LoaderHelper;
import com.aliens.smartgarden.Global.GlobalVariable;
import com.aliens.smartgarden.Model.Device;
import com.aliens.smartgarden.Model.RecordAction;
import com.aliens.smartgarden.Model.RecordSituation;
import com.aliens.smartgarden.R;
import com.aliens.smartgarden.Service.RecordActionService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    TextView nhietDoTxt, doAmTxt;
    Handler handlerSituation;
    ImageButton imgEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        globalVariable = new GlobalVariable();
        setUpSpinner();
        setUpButton();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        assert fab != null;
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        nhietDoTxt = (TextView) findViewById(R.id.txtTemperature);
        doAmTxt = (TextView) findViewById(R.id.txtHumidity);

        RecordSituationAsyncTask recordSituationAsyncTask = new RecordSituationAsyncTask();
        recordSituationAsyncTask.execute();
        RecordSituationDeviceAsyncTask recordSituationDeviceAsyncTask = new RecordSituationDeviceAsyncTask();
        recordSituationDeviceAsyncTask.execute();

//        recordAction = new RecordAction(3, "40");
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
        hub.subscribe(this);

        hub.on("notifyNewSituation", new SubscriptionHandler1<String>() {
            @Override
            public void run(String msg) {
                //Log.d("result := ", msg);
                String[] separated = msg.split("=");
                final String fStatusTem = separated[0];
                final String fStatusHum = separated[1];
                Log.i("=======Notify", msg);
                handlerSituation.post( new Runnable() {
                    @Override
                    public void run() {
                        nhietDoTxt.setText(String.valueOf(Integer.parseInt(fStatusTem)));
                        doAmTxt.setText(String.valueOf(Integer.parseInt(fStatusHum)));
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
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            i = new Intent(this, ListViewBarChartActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
        categories.add("Default Profile");
        categories.add("Custom Profile 1");
        categories.add("Custom Profile 2");
        categories.add("Custom Profile 3");
        categories.add("Custom Profile 4");
        categories.add("Custom Profile 5");

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
                    tuoinuocBtn.setText("Tưới nước : OFF");
                } else {
                    globalVariable.isTuoiNuoc = true;
                    tuoinuocBtn.setText("Tưới nước : ON");
                }
            }
        });

        maiCheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalVariable.isManChe) {
                    globalVariable.isManChe = false;
                    maiCheBtn.setText("Mái che : OFF");
                } else {
                    globalVariable.isManChe = true;
                    maiCheBtn.setText("Mái che : ON");
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

            tuoinuocBtn.setText(devices.get(0).isDeviceStatus()?"Tưới nước : ON" : "Tưới nước : OFF");
            maiCheBtn.setText(devices.get(1).isDeviceStatus()?"Mái che : ON" : "Mái che : OFF");
        }
    }

}
