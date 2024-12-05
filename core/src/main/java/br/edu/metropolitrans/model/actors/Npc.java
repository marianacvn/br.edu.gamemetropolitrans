package br.edu.metropolitrans.model.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Npc extends BaseActor {

    public String nome;
    public float x, y;
    public String nomeArquivo;
    public Stage stage;

    public Npc(String nome, float x, float y, String nomeArquivo, Stage stage) {
        super(x, y, stage);
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.nomeArquivo = nomeArquivo;
        this.stage = stage;

        margemAltura = -15;

        carregaTexturaEstatica("files/characters/" + nomeArquivo);
    }
}
