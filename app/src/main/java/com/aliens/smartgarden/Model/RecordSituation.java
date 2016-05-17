package com.aliens.smartgarden.Model;

import java.util.Date;

/**
 * Created by 12120 on 5/16/2016.
 */
public class RecordSituation {
    private int mIdRecord;
    private Date mRecordTime;
    private float mTemperature;
    private float mHumidity;
    private float mLight;

    public RecordSituation() {
    }

    public int getIdRecord() {
        return mIdRecord;
    }

    public void setIdRecord(int mIdRecord) {
        this.mIdRecord = mIdRecord;
    }

    public Date getRecordTime() {
        return mRecordTime;
    }

    public void setRecordTime(Date mRecordTime) {
        this.mRecordTime = mRecordTime;
    }

    public float getTemperature() {
        return mTemperature;
    }

    public void setTemperature(float mTemperature) {
        this.mTemperature = mTemperature;
    }

    public float getHumidity() {
        return mHumidity;
    }

    public void setHumidity(float mHumidity) {
        this.mHumidity = mHumidity;
    }

    public float getLight() {
        return mLight;
    }

    public void setLight(float mLight) {
        this.mLight = mLight;
    }
}
