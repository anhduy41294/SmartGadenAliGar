package com.aliens.smartgarden.Parser;

import com.aliens.smartgarden.Model.Device;
import com.aliens.smartgarden.Model.Profile;
import com.aliens.smartgarden.Model.RecordSituation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public static ArrayList<RecordSituation> parseSituationChart(String data) throws JSONException {
        JSONArray jArr = new JSONArray(data);
        ArrayList<RecordSituation> rs = new ArrayList<>();
        for (int i = 0; i < jArr.length(); i++) {
            RecordSituation recordSituation = new RecordSituation();
            recordSituation.setIdRecord(getInt("IdRecordSituation", jArr.getJSONObject(i)));
            recordSituation.setTemperature(getFloat("Temperature", jArr.getJSONObject(i)));
            recordSituation.setHumidity(getFloat("Humidity", jArr.getJSONObject(i)));
            recordSituation.setLight(getFloat("Light", jArr.getJSONObject(i)));

            //        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); //Thay đổi về sau
            //        Date date;
            //        try {
            //            date = format.parse(getString("RecordTime", jObj));
            //            rs.setRecordTime(date);
            //        } catch (ParseException e) {
            //            // TODO Auto-generated catch block
            //            e.printStackTrace();
            //        }
            rs.add(recordSituation);
        }
        return rs;
    }

    public static Profile parseProfileDetail(String data) throws JSONException {
        Profile rs = new Profile();

        JSONObject jObj = new JSONObject(data);

        rs.setIdProfile(getInt("IdProfile", jObj));
        rs.setProfileName(getString("ProfileName", jObj));
        rs.setTemperatureStandard(getFloat("TemperatureStandard", jObj));
        rs.setHumidityStandard(getFloat("HumidityStandard", jObj));
        rs.setLightStandard(getFloat("LightStandard", jObj));
        rs.setDuration(getFloat("WaterDuration", jObj));

        return rs;
    }
    public static ArrayList<Device> parseAllDeviceStatus(String data) throws JSONException {
        ArrayList<Device> rs = new ArrayList<Device>();

        //JSONObject jObj = new JSONObject(data);
        JSONArray jArr = new JSONArray(data);

        for (int i = 0; i < jArr.length(); i++)
        {
            Device temp = new Device();
            JSONObject jObj = jArr.getJSONObject(i);
            temp.setIdDevice(jObj.getInt("IdDevice"));
            temp.setDeviceStatus(jObj.getBoolean("DeviceStatus"));
            temp.setDeviceName(jObj.getString("DeviceName"));

            rs.add(temp);
        }
        return rs;
    }

    public static ArrayList<Profile> parseAllProfile(String data) throws JSONException {
        ArrayList<Profile> rs = new ArrayList<Profile>();

        JSONArray jArr = new JSONArray(data);

        for (int i = 0; i < jArr.length(); i++)
        {
            Profile temp = new Profile();
            JSONObject jObj = jArr.getJSONObject(i);
            temp.setIdProfile(jObj.getInt("IdProfile"));
            temp.setProfileName(jObj.getString("ProfileName"));
            temp.setTemperatureStandard((float)jObj.getDouble("TemperatureStandard"));
            temp.setHumidityStandard((float)jObj.getDouble("HumidityStandard"));
            temp.setDuration((float)jObj.getDouble("WaterDuration"));
            temp.setLightStandard((float)jObj.getDouble("LightStandard"));
            temp.setStatus(jObj.getBoolean("Status"));

            rs.add(temp);
        }
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
