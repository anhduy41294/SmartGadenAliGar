package com.aliens.smartgarden.View.AllProfileView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aliens.smartgarden.Controller.LoaderHelper;
import com.aliens.smartgarden.Global.GlobalVariable;
import com.aliens.smartgarden.Model.Profile;
import com.aliens.smartgarden.Model.RecordSituation;
import com.aliens.smartgarden.R;

import java.util.ArrayList;

public class AllProfileActivity extends AppCompatActivity {

    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GlobalVariable globalVariable = new GlobalVariable();
        rv = (RecyclerView)findViewById(R.id.rvAllProfile);
        rv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);

        AllProfileAdapter allProfileAdapter = new AllProfileAdapter(GlobalVariable.allProfile);
        rv.setAdapter(allProfileAdapter);

    }

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

            //Set value to UI
            AllProfileAdapter allProfileAdapter = new AllProfileAdapter(allProfile);
            rv.setAdapter(allProfileAdapter);
        }
    }
}
