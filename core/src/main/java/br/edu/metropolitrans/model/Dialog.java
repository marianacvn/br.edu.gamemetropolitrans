package br.edu.metropolitrans.model;

import java.util.Random;
import java.util.List;

public class Dialog {
    private List<String> dialogosGenericos;
    private List<DialogMission> dialogosMissao;
    private transient Random random; // Campo não serializado do json

    public Dialog() {
        random = new Random();
    }

    public List<DialogMission> listaDialogosMissao(int missao) {
        return dialogosMissao.stream().filter(m -> m.getMissao() == missao).toList();
    }

    /**
     * Busca um diálogo genérico aleatório
     * 
     * @return Diálogo genérico aleatório
     */
    public String buscaUmDialogoGenericoAleatorio() {
        if (dialogosGenericos.isEmpty()) {
            return null;
        }
        if (dialogosGenericos.size() == 1) {
            return dialogosGenericos.get(0);
        }

        int index = random.nextInt(dialogosGenericos.size()-1);
        return dialogosGenericos.get(index);
    }

    /**
     * Busca o próximo diálogo da missão
     * 
     * @param missao       ID da missão
     * @param dialogoAtual ID do diálogo atual
     * @return Próximo diálogo da missão
     */
    public DialogMission buscarProximoDialogoMissao(int missao, int dialogoAtual) {
        // Lista todos os diálogos por missão
        List<DialogMission> dialogosMissaoAtual = listaDialogosMissao(missao);

        //Se não houver diálogos para a missão, retorna uma dialogo genérico aleatório
        if (dialogosMissaoAtual.isEmpty()) {
            return new DialogMission(missao, buscaUmDialogoGenericoAleatorio());
        }

        // Se o diálogo atual for o último, retorna o primeiro
        if (dialogoAtual >= dialogosMissaoAtual.size()) {
            return dialogosMissaoAtual.get(0);
        }

        // Retorna o próximo diálogo
        return dialogosMissaoAtual.get(dialogoAtual);
    }

    public boolean verificaSeDialogoMissaoExiste(int missao, int dialogoAtual) {
        return listaDialogosMissao(missao).size() > dialogoAtual;
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
