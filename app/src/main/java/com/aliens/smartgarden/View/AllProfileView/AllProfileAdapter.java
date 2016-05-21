package com.aliens.smartgarden.View.AllProfileView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliens.smartgarden.Model.Profile;
import com.aliens.smartgarden.R;

import java.util.ArrayList;

/**
 * Created by Duy on 21-May-16.
 */
public class AllProfileAdapter extends RecyclerView.Adapter<AllProfileAdapter.ProfileViewHolder>{

    ArrayList<Profile> allProfile;

    AllProfileAdapter(ArrayList<Profile> allProfile) {
        this.allProfile = allProfile;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_cardview, parent, false);
        ProfileViewHolder pvh = new ProfileViewHolder(v);
        return pvh;

    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return allProfile.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        CardView cv;

        ProfileViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);

        }
    }

}