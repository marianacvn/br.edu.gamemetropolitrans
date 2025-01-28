package br.edu.metropolitrans.model;

public class Course {
    private int id;
    private String nome;
    private String descricao;
    private Status status;
    private String imagemPath;
    private String videoUrl;

    public Course() {
    }

    public Course(int id, String nome, String descricao, Status status, String imagemPath, String videoUrl) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
        this.imagemPath = imagemPath;
        this.videoUrl = videoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getImagemPath() {
        return imagemPath;
    }

    public void setImagemPath(String imagemPath) {
        this.imagemPath = imagemPath;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    

}
