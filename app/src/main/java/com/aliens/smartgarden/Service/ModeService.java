package com.aliens.smartgarden.Service;

import android.util.Log;

import com.aliens.smartgarden.Model.RecordAction;

import org.json.JSONException;
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
public class ModeService {
    private static String BASE_URL = "http://aligarapi.apphb.com/api/usermode";

    public boolean getModeStatus() {
        String url = BASE_URL + "/get";

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
            String dataJSON = buffer.toString();
            boolean rs;
            try
            {
                JSONObject jOb = new JSONObject(dataJSON);

                rs = jOb.getBoolean("Mode");
                return rs;
            }catch (JSONException e)
            {

            }

        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return false;
    }

    public String postUsermode(boolean mode) {
        String stringUrl = BASE_URL + "/update";
        try {

            URL url = new URL(stringUrl); // here is your URL path

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("Mode", mode);
            Log.e("params", postDataParams.toString());

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
