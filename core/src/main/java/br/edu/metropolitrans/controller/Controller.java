package br.edu.metropolitrans.controller;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.Dialog;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.ObjetoInterativo;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.model.dao.DialogDAO;
import br.edu.metropolitrans.model.maps.Mapas;
import br.edu.metropolitrans.view.screens.GameScreen;

public class Controller {

    /**
     * Classe principal do Jogo
     */
    private MetropoliTrans jogo;

    /**
     * Tela principal do jogo
     */
    public GameScreen gameScreen;

    /**
     * Mapas do jogo
     */
    public Mapas mapas;

    /**
     * Informa ao renderizador quantos pixels correspondem a uma unidade do mundo
     */
    float unitScale = 1;

    /**
     * Personagem do jogo
     */
    public Personagem personagem;

    /**
     * Lista de NPCs do jogo
     */
    public ArrayList<Npc> npcs;

    /**
     * Objetos interativos
     */
    public ObjetoInterativo objeto, objetoSairSala, objetoMissao;

    /**
     * Missão atual
     */
    public int MISSAO;

    /**
     * Flag para mostrar a caixa de diálogo
     */
    public boolean mostrarDialogo;

    /**
     * Flag para mostrar a caixa de missão
     */
    public boolean mostrarCaixaMissao;

    public Controller(MetropoliTrans jogo) {
        this.jogo = jogo;
        this.mapas = jogo.mapas;
        this.personagem = jogo.personagem;
        this.npcs = jogo.npcs;
        this.objeto = jogo.objeto;
        this.objetoSairSala = jogo.objetoSairSala;
        this.objetoMissao = jogo.objetoMissao;

        // Carrega os objetos de colisão
        montarColisao(mapas.mapa);

        if (jogo.telas.get("game") == null)
            jogo.telas.put("game", new GameScreen(jogo));

        gameScreen = (GameScreen) jogo.telas.get("game");
        mostrarDialogo = false;
        mostrarCaixaMissao = false;
    }
 
    /**
     * Controle do personagem, movimenta de acordo com as teclas pressionadas
     * (Setas)
     */
    public void controlePersonagem(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            personagem.acelerarEmAngulo(0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            personagem.acelerarEmAngulo(180);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            personagem.acelerarEmAngulo(90);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            personagem.acelerarEmAngulo(270);
        }
    }

    /**
     * Controle do personagem, movimenta de acordo com as teclas pressionadas (WSAD)
     *
     * @param delta
     */
    public void controlePersonagem2(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            personagem.acelerarEmAngulo(0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            personagem.acelerarEmAngulo(180);
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            personagem.acelerarEmAngulo(90);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            personagem.acelerarEmAngulo(270);
        }
    }

    /**
     * Verifica a interação do personagem com os objetos do mapa
     */
    public void controleInteracao() {
        if (objeto != null && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            for (Npc npc : npcs) {
                interacaoComNpc(npc);
            }
        }

        if (objeto != null && objetoMissao != null && personagem.interagiu(objetoMissao) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            mostrarCaixaMissao = true;
        }

        if (objeto != null && personagem.interagiu(objeto) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            // Remove os NPCs do mapa
            for (Npc npc : npcs) {
                npc.remove();
            }

            // Muda o mapa para room.tmx
            jogo.mapaRenderizador.dispose();
            jogo.mapaRenderizador = new OrthogonalTiledMapRenderer(mapas.sala, unitScale, jogo.batch);

            // Salva a última posicao e seta posição do personagem para a entrada
            personagem.setPosition(1248, 1000);
            objetoSairSala = new ObjetoInterativo("sairSala", 1216, 964, "background-transparent.png",
                    jogo.estagioPrincipal);
            objeto = null;

            // Carrega os objetos de colisão
            montarColisao(mapas.sala);

            // Renova os retângulos de colisão
            personagem.npcs = new ArrayList<Npc>();
            personagem.setRetangulosColisao(jogo.retangulosColisao);
        } else if (objetoSairSala != null && personagem.interagiu(objetoSairSala)
                && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            // Adiciona os NPCs no array
            personagem.npcs = npcs;
            for (Npc npc : npcs) {
                npc.adicionarNoEstagio(jogo.estagioPrincipal);
            }

            // Muda o mapa para map.tmx
            jogo.mapaRenderizador.dispose();
            jogo.mapaRenderizador = new OrthogonalTiledMapRenderer(mapas.mapa, unitScale, jogo.batch);

            // Ajusta para o personagem sair da sala mas longe do objeto de entrada da
            // prefeitura

            // setar posição do personagem para a entrada
            personagem.setPosition(77, 150);
            objeto = new ObjetoInterativo("entradaPrefeitura", 32, 220, "background-transparent.png",
                    jogo.estagioPrincipal);
            objetoSairSala = null;

            // Carrega os objetos de colisão
            montarColisao(mapas.mapa);

            // Renova os retângulos de colisão e os npcs
            personagem.npcs = npcs;
            personagem.setRetangulosColisao(jogo.retangulosColisao);
        }
    }

    /**
     * Controle de diálogos
     */
    public void controleDialogos() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            mostrarDialogo = false;
            for (Npc npc : npcs) {
                atualizaStatusAlertaMissaoNpc(npc);
            }
        }
    }

    /**
     * Verifica a interação do personagem com os NPCs
     *
     * @param npc Npc
     */
    private void interacaoComNpc(Npc npc) {
        if (jogo.telas.get("game") != null) {
            // GameScreen gameScreen = (GameScreen) jogo.telas.get("game");
            if (personagem.estaDentroDaDistancia(15, npc)) {
                // Carrega os diálogos do NPC
                gameScreen.caixaDialogo.npc = npc;
                gameScreen.caixaDialogo.setTextoDialogo(carregaDialogos(npc));
                gameScreen.caixaDialogo.defineTexturaNpc();
                mostrarDialogo = true;
                return;
            }
        }
    }

    /**
     * Atualiza o status de alerta da missão do NPC
     *
     * @param npc Npc
     */
    private void atualizaStatusAlertaMissaoNpc(Npc npc) {
        if (jogo.telas.get("game") != null) {
            /**
             * Verifica se está dentro da distancia e se o npc faz parte da missão
             * Se sim, atualiza o status de alerta da missão
             */
            if (personagem.estaDentroDaDistancia(15, npc) &&
                    MissionController.npcEstaNaMisao(npc) &&
                    npc.statusAlertaMissao == 1) {
                npc.statusAlertaMissao = 2;
            }
        }
    }

    /**
     * Carrega os diálogos do personagem
     * 
     * @param npc Npc
     */
    private String carregaDialogos(Npc npc) {
        // Carregar diálogos
        Dialog dialogo = DialogDAO.carregarDialogos(npc.nome);
        if (dialogo != null) {
            // Verifica se tem uma missão
            return dialogo.buscarProximoDialogoMissao(MISSAO, npc.DIALOGO_ATUAL).getMensagem();
        }
        return "Olá, eu sou o " + npc.nome + "!";
    }

    /**
     * Monta os retângulos de colisão do mapa
     *
     * @param mapa TiledMap
     */
    private void montarColisao(TiledMap mapa) {
        // Carrega os objetos de colisão
        jogo.objetosColisao = mapa.getLayers().get("colisao").getObjects();
        jogo.retangulosColisao = new Array<Rectangle>();

        // Adiciona os retângulos de colisão do mapa ao array
        for (MapObject objeto : jogo.objetosColisao) {
            if (objeto instanceof RectangleMapObject) {
                Rectangle retangulo = ((RectangleMapObject) objeto).getRectangle();
                jogo.retangulosColisao.add(retangulo);
            }
        }

        // Adiciona os retângulos de colisão do mapa ao personagem
        personagem.setRetangulosColisao(jogo.retangulosColisao);
    }

}
