package br.edu.metropolitrans.model;

public class GameDataPersonagem {
    private int x;
    private int y;
    private int moedas;
    private int xp;
    private String tipoInfracao;
    private int infracoes;
    private String sprite;

    public GameDataPersonagem() {
    }

    public GameDataPersonagem(int x, int y, int moedas, int xp, String tipoInfracao, int infracoes, String sprite) {
        this.x = x;
        this.y = y;
        this.moedas = moedas;
        this.xp = xp;
        this.tipoInfracao = tipoInfracao;
        this.infracoes = infracoes;
        this.sprite = sprite;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getMoedas() {
        return moedas;
    }

    public void setMoedas(int moedas) {
        this.moedas = moedas;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getTipoInfracao() {
        return tipoInfracao;
    }

    public void setTipoInfracao(String tipoInfracao) {
        this.tipoInfracao = tipoInfracao;
    }

    public int getInfracoes() {
        return infracoes;
    }

    public void setInfracoes(int infracoes) {
        this.infracoes = infracoes;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

}