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
    private boolean mStatus;

    public boolean isStatus() {
        return mStatus;
    }

    public void setStatus(boolean mStatus) {
        this.mStatus = mStatus;
    }

    public float getDuration() {
        return mDuration;
    }

    public void setDuration(float mDuration) {
        this.mDuration = mDuration;
    }

    private float mDuration;

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
