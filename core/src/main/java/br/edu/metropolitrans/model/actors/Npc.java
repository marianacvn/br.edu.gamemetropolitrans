package br.edu.metropolitrans.model.actors;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Npc extends ActorAnimation {

    public static String DIALOGO_INICIAL = "Olá, senhor(a) estágiario(a) da secretária de trânsito, seja bem-vindo a cidade de Metropolitrans. O(a) senhor(a) precisa falar com o prefeito da cidade para ele lhe repassar os detalhes sobre o seu novo trabalho.";

    public String nomeArquivo;
    public int statusAlertaMissao;
    public Texture minimapaPonto;

    public Npc(String nome, Stage stage) {
        super(nome, 0, 0, stage, 100, "sprite-animation.png", "files/characters/" + nome + "/", List.of(), false, 4, 11,
                false);
    }

    /**
     * Diálogo atual
     */
    public int DIALOGO_ATUAL = 0;

    public Npc(String nome, float x, float y, String nomeArquivo, Stage stage, boolean temAnimacao) {
        super(nome, x, y, stage, 100, "sprite-animation.png", "files/characters/" + nome + "/", List.of(), temAnimacao,
                4, 11, false);
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

    public Npc(String nome, float x, float y, String nomeArquivo, Stage stage, int statusAlertaMissao,
            boolean temAnimacao) {
        this(nome, x, y, nomeArquivo, stage, temAnimacao);
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
