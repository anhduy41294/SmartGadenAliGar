package com.aliens.smartgarden.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aliens.smartgarden.Controller.LoaderHelper;
import com.aliens.smartgarden.Model.Profile;
import com.aliens.smartgarden.R;
import com.aliens.smartgarden.Service.ProfileService;

public class EditProfileActivity extends AppCompatActivity {

    private EditText edtProfileName;
    private EditText edtTemperature;
    private EditText edtHumidity;
    private EditText edtDuration;
    private FloatingActionButton fabSave;

    private Profile mProfile;
    int idProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getFormWiget();
        addEvent();
    }
    private void addEvent() {
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfile.setProfileName(edtProfileName.getText().toString());
                mProfile.setTemperatureStandard(Float.parseFloat(edtTemperature.getText().toString()));
                mProfile.setHumidityStandard(Float.parseFloat(edtHumidity.getText().toString()));
                mProfile.setDuration(Float.parseFloat(edtDuration.getText().toString()));
                mProfile.setLightStandard(0);
                mProfile.setIdProfile(idProfile);

                PostProfile postProfile = new PostProfile();
                postProfile.execute();

                startActivity(new Intent(getApplication(), MainActivity.class));
            }
        });
    }

    private void getFormWiget() {

        edtProfileName = (EditText) findViewById(R.id.edtProfileNameEdit);
        edtDuration = (EditText) findViewById(R.id.edtDurationEdit);
        edtHumidity = (EditText) findViewById(R.id.edtHumidityEdit);
        edtTemperature = (EditText) findViewById(R.id.edtTemperatureEdit);
        fabSave = (FloatingActionButton) findViewById(R.id.fabSaveEdit);

        mProfile = new Profile();
        Intent i = getIntent();
        idProfile = i.getIntExtra("IdProfile", 1);

        GetDetail getDetail = new GetDetail();
        getDetail.execute();

    }

    public class GetDetail extends AsyncTask<String, Void, Profile> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Profile doInBackground(String... params) {
            LoaderHelper loaderHelper = new LoaderHelper();

            return loaderHelper.getProfileDetail(idProfile);
        }

        @Override
        protected void onPostExecute(Profile profile) {
            super.onPostExecute(profile);

            edtProfileName.setText(profile.getProfileName());
            edtDuration.setText(String.valueOf(profile.getDuration()));
            edtTemperature.setText(String.valueOf(profile.getTemperatureStandard()));
            edtHumidity.setText(String.valueOf(profile.getHumidityStandard()));
        }
    }

    public class PostProfile extends AsyncTask<String, Void, String> {

        public PostProfile() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ProfileService profileService = new ProfileService();
            return profileService.postUpdateProfile(mProfile);
        }

        @Override
        protected void onPostExecute(String respond) {
            super.onPostExecute(respond);

        }
    }
}