package com.aliens.smartgarden.View;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
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
import com.aliens.smartgarden.View.AllProfileView.ProfileFragment;

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


    NavigationView navigationView = null;
    Toolbar toolbar = null;
    ProgressDialog progressDialog;
    LoadFragment loadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        recordAction = new RecordAction(1, "20");
//        SendRecordAction sendRecordAction = new SendRecordAction();
//        sendRecordAction.execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        progressDialog = new ProgressDialog(getBaseContext());
        progressDialog.setTitle("Đang tải...");

        loadFragment = new LoadFragment();
        loadFragment.execute(1);
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

        if (id == R.id.nav_home) {
            LoadFragment loadFragmentMain = new LoadFragment();
            loadFragmentMain.execute(1);
        } else if (id == R.id.nav_chart) {
            LoadFragment loadFragmentChart = new LoadFragment();
            loadFragmentChart.execute(2);
        }else if (id == R.id.nav_profile) {
            LoadFragment loadFragmentProfile = new LoadFragment();
            loadFragmentProfile.execute(3);
        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class LoadFragment extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // progressDialog.show();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            switch (params[0])
            {
                case 1:
                {
                    MainFragment mainFragment = new MainFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, mainFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    return null;
                }
                case 2:
                {
                    ChartFragment chartFragment = new ChartFragment();
                    FragmentTransaction fragmentTransactionChart = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionChart.replace(R.id.fragment_container, chartFragment);
                    fragmentTransactionChart.commit();
                    return null;
                }
                case 3:
                {
                    ProfileFragment chartFragment = new ProfileFragment();
                    FragmentTransaction fragmentTransactionProfile = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionProfile.replace(R.id.fragment_container, chartFragment);
                    fragmentTransactionProfile.commit();
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }


}
