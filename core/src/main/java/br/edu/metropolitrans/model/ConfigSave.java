package br.edu.metropolitrans.model;

public class ConfigSave {
    private int id;
    private String name;
    private String date;

    public ConfigSave() {
    }
    
    public ConfigSave(int id) {
        this.id = id;
    }

    public ConfigSave(int id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
