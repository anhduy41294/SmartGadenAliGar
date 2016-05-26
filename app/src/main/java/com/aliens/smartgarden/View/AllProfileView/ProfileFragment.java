package com.aliens.smartgarden.View.AllProfileView;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliens.smartgarden.Controller.LoaderHelper;
import com.aliens.smartgarden.Global.GlobalVariable;
import com.aliens.smartgarden.Model.Profile;
import com.aliens.smartgarden.R;
import com.aliens.smartgarden.Service.ProfileService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    View view = null;
    RecyclerView rv;
    public ProfileFragment() {
        // Required empty public constructor
    }

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rvAllProfile);
        rv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(linearLayoutManager);
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Đang tải...");

        getAllProfile getAllProfile = new getAllProfile();
        getAllProfile.execute();

        return view;
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
            progressDialog.show();
        }

        @Override
        protected ArrayList<Profile> doInBackground(String... params) {
            LoaderHelper loaderHelper = new LoaderHelper();
            return loaderHelper.getAllProfile();
        }

        @Override
        protected void onPostExecute(ArrayList<Profile> allProfile) {
            super.onPostExecute(allProfile);
            GlobalVariable.allProfile = allProfile;
            progressDialog.dismiss();
            AllProfileAdapter allProfileAdapter = new AllProfileAdapter(GlobalVariable.allProfile, view);
            rv.setAdapter(allProfileAdapter);
        }
    }

}
