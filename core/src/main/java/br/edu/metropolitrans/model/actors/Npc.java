package br.edu.metropolitrans.model.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Npc extends BaseActor {

    public String nome;
    public String nomeArquivo;
    public int statusAlertaMissao;

    /**
     * Diálogo atual
     */
    public int DIALOGO_ATUAL = 0;

    public Npc(String nome, float x, float y, String nomeArquivo, Stage stage) {
        super(x, y, stage);
        this.nome = nome;
        this.nomeArquivo = nomeArquivo;

        margemAltura = -15;

        carregaTexturaEstatica("files/characters/" + nomeArquivo);
    }

    public Npc(String nome, float x, float y, String nomeArquivo, Stage stage, int statusAlertaMissao) {
        super(x, y, stage);
        this.nome = nome;
        this.nomeArquivo = nomeArquivo;
        margemAltura = -15;
        this.statusAlertaMissao = statusAlertaMissao;

        carregaTexturaEstatica("files/characters/" + nomeArquivo);
    }
}
