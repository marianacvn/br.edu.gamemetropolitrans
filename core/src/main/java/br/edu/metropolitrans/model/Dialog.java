package br.edu.metropolitrans.model;

import java.util.List;

public class Dialog {
    private List<String> dialogosGenericos;
    private List<DialogMission> dialogosMissao;

    public Dialog() {
    }

    public Dialog(List<String> dialogosGenericos, List<DialogMission> dialogosMissao) {
        this.dialogosGenericos = dialogosGenericos;
        this.dialogosMissao = dialogosMissao;
    }

    public List<String> getDialogosGenericos() {
        return dialogosGenericos;
    }

    public void setDialogosGenericos(List<String> dialogosGenericos) {
        this.dialogosGenericos = dialogosGenericos;
    }

    public List<DialogMission> getDialogosMissao() {
        return dialogosMissao;
    }

    public void setDialogosMissao(List<DialogMission> dialogosMissao) {
        this.dialogosMissao = dialogosMissao;
    }
}
