package br.edu.metropolitrans.model;

import java.util.List;

public class ConfigSaveInfo {
    private String lastSave;
    private String lastSaveName;
    private List<ConfigSave> saves;

    public ConfigSaveInfo() {
    }

    public ConfigSaveInfo(String lastSave, String lastSaveName, List<ConfigSave> saves) {
        this.lastSave = lastSave;
        this.lastSaveName = lastSaveName;
        this.saves = saves;
    }

    public String getLastSave() {
        return lastSave;
    }

    public void setLastSave(String lastSave) {
        this.lastSave = lastSave;
    }

    public String getLastSaveName() {
        return lastSaveName;
    }

    public void setLastSaveName(String lastSaveName) {
        this.lastSaveName = lastSaveName;
    }

    public List<ConfigSave> getSaves() {
        return saves;
    }

    public void setSaves(List<ConfigSave> saves) {
        this.saves = saves;
    }

}
