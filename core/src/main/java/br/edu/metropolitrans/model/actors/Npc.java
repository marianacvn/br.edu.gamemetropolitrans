package br.edu.metropolitrans.model.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Npc extends BaseActor {

    public String nome;
    public String nomeArquivo;
    public int statusAlertaMissao;
    public Texture minimapaPonto;

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

        try {
            minimapaPonto = new Texture(
                    Gdx.files.internal("files/characters/" + nomeArquivo.replace("sprite.png", "minimap.png")));
        } catch (Exception ignore) {
            Gdx.app.log("Npc", "Erro ao carregar minimapaPonto para " + nomeArquivo);
        }
    }

    public Npc(String nome, float x, float y, String nomeArquivo, Stage stage, int statusAlertaMissao) {
        this(nome, x, y, nomeArquivo, stage);
        this.statusAlertaMissao = statusAlertaMissao;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Npc npc = (Npc) obj;
        return npc.nome.equals(nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}
