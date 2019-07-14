package io.terasyshub.models;

public class DeviceData {
    String id,unit;
    int value;
    long timestamp;

    public DeviceData() {
    }

    public DeviceData(String id, String unit, int value, long timestamp) {
        this.id = id;
        this.unit = unit;
        this.value = value;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
