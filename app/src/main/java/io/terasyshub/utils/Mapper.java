package io.terasyshub.utils;



import org.json.JSONException;
import org.json.JSONObject;

import io.terasyshub.models.Device;

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
}
