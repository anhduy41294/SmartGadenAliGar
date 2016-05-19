package com.aliens.smartgarden.Model;

/**
 * Created by 12120 on 5/16/2016.
 */
public class Device {
    private int mIdDevice;
    private String mDeviceName;
    private boolean mDeviceStatus;

    public Device() {
    }

    public int getIdDevice() {
        return mIdDevice;
    }

    public void setIdDevice(int mIdDevice) {
        this.mIdDevice = mIdDevice;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String mDeviceName) {
        this.mDeviceName = mDeviceName;
    }

    public boolean isDeviceStatus() {
        return mDeviceStatus;
    }

    public void setDeviceStatus(boolean mDeviceStatus) {
        this.mDeviceStatus = mDeviceStatus;
    }
}

