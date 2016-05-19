package com.aliens.smartgarden.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aliens.smartgarden.Model.Profile;
import com.aliens.smartgarden.R;
import com.aliens.smartgarden.Service.ProfileService;
import com.aliens.smartgarden.Service.RecordActionService;

public class AddProfileActivity extends AppCompatActivity {

    private EditText edtProfileName;
    private EditText edtTemperature;
    private EditText edtHumidity;
    private EditText edtDuration;
    private FloatingActionButton fabSave;
    private Profile mProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);

        mProfile = new Profile();
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
                mProfile.setDuaration(Float.parseFloat(edtDuration.getText().toString()));
                mProfile.setLightStandard(0);

                PostProfile postProfile = new PostProfile();
                postProfile.execute();

                startActivity(new Intent(getApplication(), MainActivity.class));
            }
        });
    }

    private void getFormWiget() {

        edtProfileName = (EditText) findViewById(R.id.edtProfileName);
        edtDuration = (EditText) findViewById(R.id.edtDuration);
        edtHumidity = (EditText) findViewById(R.id.edtHumidity);
        edtTemperature = (EditText) findViewById(R.id.edtTemperature);
        fabSave = (FloatingActionButton) findViewById(R.id.fabSave);

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
            return profileService.postProfile(mProfile);
        }

        @Override
        protected void onPostExecute(String respond) {
            super.onPostExecute(respond);

        }
    }
}
