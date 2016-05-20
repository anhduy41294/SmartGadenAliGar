package com.aliens.smartgarden.Service;

import android.util.Log;

import com.aliens.smartgarden.Model.Profile;
import com.aliens.smartgarden.Model.RecordAction;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 12120 on 5/19/2016.
 */
public class ProfileService {
    private static String BASE_URL = "http://aligarapi.apphb.com/api/profile";

    public String getAllProfile() {
        String url = BASE_URL + "/all";

        return getProfileData(url);
    }
    public String getProfileDetail(int id ) {
        String url = BASE_URL + "/detail?id=" + id;

        return getProfileData(url);
    }

    /**
     * GET METHOD
     */
    public String getProfileData(String url) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(false);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;
    }


    public String postProfile(Profile profile) {
        String stringUrl = BASE_URL + "/add";
        try {

            URL url = new URL(stringUrl); // here is your URL path

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("ProfileName", profile.getProfileName());
            postDataParams.put("TemperatureStandard", profile.getTemperatureStandard());
            postDataParams.put("LightStandard", 0);
            postDataParams.put("HumidityStandard", profile.getHumidityStandard());
            postDataParams.put("WaterDuration", profile.getDuaration());
            postDataParams.put("Status", "false");

            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(postDataParams.toString());

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            return Integer.toString(responseCode);

        }
        catch(Exception e){
            return null;
        }

    }

    public String postUpdateProfile(Profile profile) {
        String stringUrl = BASE_URL + "/update";
        try {

            URL url = new URL(stringUrl); // here is your URL path

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("IdProfile", profile.getIdProfile());
            postDataParams.put("ProfileName", profile.getProfileName());
            postDataParams.put("TemperatureStandard", profile.getTemperatureStandard());
            postDataParams.put("LightStandard", 0);
            postDataParams.put("HumidityStandard", profile.getHumidityStandard());
            postDataParams.put("WaterDuration", profile.getDuaration());
            postDataParams.put("Status", "false");

            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(postDataParams.toString());

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            return Integer.toString(responseCode);

        }
        catch(Exception e){
            return null;
        }

    }
}
