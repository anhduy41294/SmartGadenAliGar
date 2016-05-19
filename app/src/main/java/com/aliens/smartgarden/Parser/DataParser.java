package com.aliens.smartgarden.Parser;

import com.aliens.smartgarden.Model.RecordSituation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 12120 on 5/16/2016.
 */
public class DataParser {

    public static RecordSituation parseLastestSituation(String data) throws JSONException {
        RecordSituation rs = new RecordSituation();

        JSONObject jObj = new JSONObject(data);

        rs.setIdRecord(getInt("IdRecordSituation", jObj));
        rs.setTemperature(getFloat("Temperature", jObj));
        rs.setHumidity(getFloat("Humidity", jObj));
        rs.setLight(getFloat("Light", jObj));

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); //Thay đổi về sau
//        Date date;
//        try {
//            date = format.parse(getString("RecordTime", jObj));
//            rs.setRecordTime(date);
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        return rs;
    }


    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    private static double  getDouble(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getDouble(tagName);
    }

    private static JSONArray getArray(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getJSONArray(tagName);
    }
}
