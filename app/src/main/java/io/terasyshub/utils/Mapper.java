package io.terasyshub.utils;



import org.json.JSONException;
import org.json.JSONObject;

import io.terasyshub.models.Device;
import io.terasyshub.models.DeviceData;

public class Mapper {

    public static Device JSONtoDevice(JSONObject jsonObject){
        Device device = new Device();
        try {
            device.setId(jsonObject.getString("_id"));
            device.setName(jsonObject.getString("name"));
            device.setMac(jsonObject.getString("mac"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return device;
    }

    public static DeviceData JSONtoDeviceData(JSONObject jsonObject){
        DeviceData deviceData = new DeviceData();
        try {
            deviceData.setId(jsonObject.getString("_id"));
            deviceData.setUnit(jsonObject.getString("unit"));
            deviceData.setValue(jsonObject.getInt("value"));
            deviceData.setTimestamp(jsonObject.getLong("timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceData;
    }
}
