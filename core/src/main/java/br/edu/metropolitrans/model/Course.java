package br.edu.metropolitrans.model;

public class Course {
    private int id;
    private String nome;
    private String descricaoPagInicial;
    private String descricao;
    private Status status;
    private int missaoId;
    private String imagemPath;
    private String videoUrl;

    public Course() {
    }

    public Course(int id, String nome, String descricaoPagInicial, String descricao, Status status, int missaoId, String imagemPath,
            String videoUrl) {
        this.id = id;
        this.nome = nome;
        this.descricaoPagInicial = descricaoPagInicial;
        this.descricao = descricao;
        this.status = status;
        this.missaoId = missaoId;
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

    public String getDescricaoPagInicial() {
        return descricaoPagInicial;
    }

    public void setDescricaoPagInicial(String descricaoPagInicial) {
        this.descricaoPagInicial = descricaoPagInicial;
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

    public int getMissaoId() {
        return missaoId;
    }

    public void setMissaoId(int missaoId) {
        this.missaoId = missaoId;
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

    @Override
    public String toString() {
        return "Course{" + "id=" + id + ", status=" + status
                + ", missaoId=" + missaoId + "}";
    }

}
