package br.edu.metropolitrans.model;

public class DialogMission {
    private int missao;
    private String mensagem;

    public DialogMission() {
    }

    public DialogMission(int missao, String mensagem) {
        this.missao = missao;
        this.mensagem = mensagem;
    }

    public int getMissao() {
        return missao;
    }

    public void setMissao(int missao) {
        this.missao = missao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
