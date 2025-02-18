package br.edu.metropolitrans.model;

public class GameDataNpc {
    private String key;
    private int x;
    private int y;
    private int statusAlertaMissao;
    private int dialogoAtual;
    private boolean temAnimacao;

    public GameDataNpc() {
    }

    public GameDataNpc(String key, int x, int y, int statusAlertaMissao, int dialogoAtual, boolean temAnimacao) {
        this.key = key;
        this.x = x;
        this.y = y;
        this.statusAlertaMissao = statusAlertaMissao;
        this.dialogoAtual = dialogoAtual;
        this.temAnimacao = temAnimacao;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public int getStatusAlertaMissao() {
        return statusAlertaMissao;
    }

    public void setStatusAlertaMissao(int statusAlertaMissao) {
        this.statusAlertaMissao = statusAlertaMissao;
    }

    public int getDialogoAtual() {
        return dialogoAtual;
    }

    public void setDialogoAtual(int dialogoAtual) {
        this.dialogoAtual = dialogoAtual;
    }

    public boolean isTemAnimacao() {
        return temAnimacao;
    }

    public void setTemAnimacao(boolean temAnimacao) {
        this.temAnimacao = temAnimacao;
    }
}
