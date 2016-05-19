package com.aliens.smartgarden.Model;

/**
 * Created by 12120 on 5/16/2016.
 */
public class Profile {
    private int mIdProfile;
    private String mProfileName;
    private float mTemperatureStandard;
    private float mHumidityStandard;
    private float mLightStandard;

    public float getDuaration() {
        return mDuaration;
    }

    public void setDuaration(float mDuaration) {
        this.mDuaration = mDuaration;
    }

    private float mDuaration;

    public Profile() {
    }

    public int getIdProfile() {
        return mIdProfile;
    }

    public void setIdProfile(int mIdProfile) {
        this.mIdProfile = mIdProfile;
    }

    public String getProfileName() {
        return mProfileName;
    }

    public void setProfileName(String mProfileName) {
        this.mProfileName = mProfileName;
    }

    public float getTemperatureStandard() {
        return mTemperatureStandard;
    }

    public void setTemperatureStandard(float mTemperatureStandard) {
        this.mTemperatureStandard = mTemperatureStandard;
    }

    public float getHumidityStandard() {
        return mHumidityStandard;
    }

    public void setHumidityStandard(float mHumidityStandard) {
        this.mHumidityStandard = mHumidityStandard;
    }

    public float getLightStandard() {
        return mLightStandard;
    }

    public void setLightStandard(float mLightStandard) {
        this.mLightStandard = mLightStandard;
    }
}
