package br.edu.metropolitrans.model;

public class DialogMission {
    private String missao;
    private String mensagem;

    public DialogMission() {
    }

    public DialogMission(String missao, String mensagem) {
        this.missao = missao;
        this.mensagem = mensagem;
    }

    public String getMissao() {
        return missao;
    }

    public void setMissao(String missao) {
        this.missao = missao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
