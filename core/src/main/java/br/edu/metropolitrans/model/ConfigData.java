package br.edu.metropolitrans.model;

public class ConfigData {
    private double volume;
    private boolean mute;
    private String typeDebugMode;
    private boolean trafficViolations;
    private ConfigSaveInfo saveInfo;

    public ConfigData() {
    }

    public ConfigData(double volume, boolean mute, String typeDebugMode, boolean trafficViolations,
            ConfigSaveInfo saveInfo) {
        this.volume = volume;
        this.mute = mute;
        this.typeDebugMode = typeDebugMode;
        this.trafficViolations = trafficViolations;
        this.saveInfo = saveInfo;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public String getTypeDebugMode() {
        return typeDebugMode;
    }

    public void setTypeDebugMode(String typeDebugMode) {
        this.typeDebugMode = typeDebugMode;
    }

    public boolean isTrafficViolations() {
        return trafficViolations;
    }

    public void setTrafficViolations(boolean trafficViolations) {
        this.trafficViolations = trafficViolations;
    }

    public ConfigSaveInfo getSaveInfo() {
        return saveInfo;
    }

    public void setSaveInfo(ConfigSaveInfo saveInfo) {
        this.saveInfo = saveInfo;
    }

}
