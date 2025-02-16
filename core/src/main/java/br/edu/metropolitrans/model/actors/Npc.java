package br.edu.metropolitrans.model.actors;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

import br.edu.metropolitrans.model.utils.DebugMode;

public class Npc extends ActorAnimation {

    public static String DIALOGO_INICIAL = "Olá, senhor(a) estágiario(a) da Secretária de Trânsito, seja bem-vindo a cidade de Metropolitrans. O(a) senhor(a) precisa falar com o Prefeito da cidade na Prefeitura para ele lhe repassar os detalhes sobre o seu novo trabalho.";
    public static String DIALOGO_BETANIA_MISSAO_BLOQUEADA = "Para iniciar a missão, é necessário assistir o módulo referente a ela, dirija-se a Prefeitura e veja o módulo liberado no computador.";
    public static String DIALOGO_BETANIA_APLICAR_PRATICA = "Olá, estágiario(a), agora que você já assistiu os módulos referentes a essa missão, volte para a cidade e coloque em prática o conteúdo que aprendeu.";
    public static String DIALOGO_GUARDA_TECLA_T = "Ah, agora você pode acessar os módulos do curso a partir do seu celular, basta pressionar a tecla \"T\". Ah, você pode se orientar pelo minimapa. O personagem com quem você deve falar aparecerá nele.";
    public static String DIALOGO_GUARDA_TECLA_ESPACO = "Para acessar a Prefeitura, você deve pressionar a tecla \"Espaço\" próximo a entrada, da mesma forma para sair da sala (próximo a entrada) e interagir com o computador.";
    public static String DIALOGO_GUARDA_INDICA_CAMINHO = "Olá, estágiario(a), para iniciar a primeira missão vá até Maria e converse com ela, pela calçada acima da prefeitura. Boa sorte.";
    public static String DIALOGO_GUARDA_GENERICO = "Olá, estágiario(a), como está indo o novo trabalho?";

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
            DebugMode.mostrarLog("Npc", "Erro ao carregar minimapaPonto para " + nomeArquivo);
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

    @Override
    public void dispose() {
        super.dispose();

        if (minimapaPonto != null) {
            minimapaPonto.dispose();
        }
    }
}
