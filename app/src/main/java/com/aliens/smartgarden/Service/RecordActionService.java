package com.aliens.smartgarden.Service;

import android.util.Log;

import com.aliens.smartgarden.Model.RecordAction;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Duy on 18-May-16.
 */
public class RecordActionService {
    private static String BASE_URL = "http://aligarapi.apphb.com/api/recordaction";

    public String postRecordAction(RecordAction recordAction) {
        String stringUrl = BASE_URL + "/add";
        try {

            URL url = new URL(stringUrl); // here is your URL path

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("IdAction", Integer.toString(recordAction.getmIdAction()));
            postDataParams.put("Duration", recordAction.getmDuration());
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

//            if (responseCode == HttpsURLConnection.HTTP_OK) {
//
//                BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                StringBuffer sb = new StringBuffer("");
//                String line="";
//
//                while((line = in.readLine()) != null) {
//
//                    sb.append(line);
//                    break;
//                }
//
//                in.close();
//                return sb.toString();
//
//            }
//            else {
//                return new String("false : "+responseCode);
//            }
        }
        catch(Exception e){
            return null;
        }

    }

//    public String getPostDataString(JSONObject params) throws Exception {
//
//        StringBuilder result = new StringBuilder();
//        boolean first = true;
//
//        Iterator<String> itr = params.keys();
//
//        while(itr.hasNext()){
//
//            String key= itr.next();
//            Object value = params.get(key);
//
//            if (first)
//                first = false;
//            else
//                result.append("&");
//
//            result.append(URLEncoder.encode(key, "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
//
//        }
//        return result.toString();
//    }
}
