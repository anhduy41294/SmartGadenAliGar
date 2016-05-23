package com.aliens.smartgarden.View.AllProfileView;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliens.smartgarden.Global.GlobalVariable;
import com.aliens.smartgarden.R;
import com.aliens.smartgarden.Service.ProfileService;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    View view = null;
    RecyclerView rv;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        GlobalVariable globalVariable = new GlobalVariable();
        rv = (RecyclerView) view.findViewById(R.id.rvAllProfile);
        rv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(linearLayoutManager);

        AllProfileAdapter allProfileAdapter = new AllProfileAdapter(GlobalVariable.allProfile, view);
        rv.setAdapter(allProfileAdapter);

        return view;
    }

}
