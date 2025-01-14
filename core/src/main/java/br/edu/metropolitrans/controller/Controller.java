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
import br.edu.metropolitrans.model.PersonagemDirecao;
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

    public MissionController controleMissao;

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

    /**
     * Flag para acertar a missão
     * - 0: Não respondeu
     * - 1: Acertou
     * - 2: Errou
     */
    public int resultadoRespostaMissao;

    /**
     * Flag para perdeu o jogo
     */
    public boolean perdeuJogo;

    /**
     * NPC do guarda de trânsito
     */
    private Npc guarda;

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
        montarCamadaPista(false);

        if (jogo.telas.get("game") == null)
            jogo.telas.put("game", new GameScreen(jogo));

        gameScreen = (GameScreen) jogo.telas.get("game");
        mostrarDialogo = false;
        mostrarCaixaMissao = false;

        guarda = new Npc("guarda", jogo.estagioPrincipal);

        // Inicia o controle de missão
        controleMissao = new MissionController(jogo);
    }

    /**
     * Controle do personagem, movimenta de acordo com as teclas pressionadas
     * (Setas)
     */
    public void controlePersonagemSetas(float delta) {
        boolean up = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        controlePersonagem(up, down, left, right);
    }

    /**
     * Controle do personagem, movimenta de acordo com as teclas pressionadas (WSAD)
     *
     * @param delta
     */
    public void controlePersonagemWASD(float delta) {
        boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);

        controlePersonagem(up, down, left, right);
    }

    private void controlePersonagem(boolean up, boolean down, boolean left, boolean right) {
        if (up && right) {
            personagem.acelerarEmAngulo(45);
            personagem.setUltimaDirecao(PersonagemDirecao.NORDESTE);
        } else if (up && left) {
            personagem.acelerarEmAngulo(135);
            personagem.setUltimaDirecao(PersonagemDirecao.NOROESTE);
        } else if (down && right) {
            personagem.acelerarEmAngulo(315);
            personagem.setUltimaDirecao(PersonagemDirecao.SUDESTE);
        } else if (down && left) {
            personagem.acelerarEmAngulo(225);
            personagem.setUltimaDirecao(PersonagemDirecao.SUDOESTE);
        } else if (up) {
            personagem.acelerarEmAngulo(90);
            personagem.setUltimaDirecao(PersonagemDirecao.NORTE);
        } else if (down) {
            personagem.acelerarEmAngulo(270);
            personagem.setUltimaDirecao(PersonagemDirecao.SUL);
        } else if (left) {
            personagem.acelerarEmAngulo(180);
            personagem.setUltimaDirecao(PersonagemDirecao.OESTE);
        } else if (right) {
            personagem.acelerarEmAngulo(0);
            personagem.setUltimaDirecao(PersonagemDirecao.LESTE);
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

        if (objeto != null && objetoMissao != null && personagem.interagiu(objetoMissao)
                && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            mostrarCaixaMissao = true;
        }

        if (objeto != null && personagem.interagiu(objeto) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // Remove o objeto da missão da sala e minimapa
            objetoMissao.setVisible(false);
            gameScreen.minimapa.isVisible = false;
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
            montarCamadaPista(true);

            // Renova os retângulos de colisão
            personagem.npcs = new ArrayList<Npc>();
            personagem.setRetangulosColisao(jogo.retangulosColisao);
            personagem.setRetangulosPista(jogo.retangulosPista);
        } else if (objetoSairSala != null && personagem.interagiu(objetoSairSala)
                && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // coloca o objeto da missão ao sair da sala e minimapa
            objetoMissao.setVisible(true);
            gameScreen.minimapa.isVisible = true;
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
            personagem.setPosition(100, 690);
            objeto = new ObjetoInterativo("entradaPrefeitura", 100, 760, "background-transparent.png",
                    jogo.estagioPrincipal);
            objetoSairSala = null;

            // Carrega os objetos de colisão
            montarColisao(mapas.mapa);
            montarCamadaPista(false);

            // Renova os retângulos de colisão e os npcs
            personagem.npcs = npcs;
            personagem.setRetangulosColisao(jogo.retangulosColisao);
            personagem.setRetangulosPista(jogo.retangulosPista);
        }
    }

    /**
     * Controle de diálogos
     */
    public void controleDialogos() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            mostrarDialogo = false;
            if (personagem.sofreuInfracao) {
                personagem.sofreuInfracao = false;
                atualizaInfracao();
            } else {
                for (Npc npc : npcs) {
                    atualizaStatusAlertaMissaoNpc(npc);
                }
            }
        }
    }

    /**
     * Controle de lógica do jogo
     */
    public void controleLogicaJogo() {
        // Verifica se o personagem zerou as moedas
        // Se sim, volta para a tela de início pois perdeu o jogo
        if (perdeuJogo) {
            Gdx.app.log("Controller", "Perdeu o jogo!");
            // Volta todas as informações do jogo para o início
            Gdx.app.log("Controller", "Reiniciando o jogo...");
            jogo.reiniciarJogo();
        }
    }

    /**
     * Controle de infrações do personagem
     */
    public void controleInfracao() {
        // Se as infrações forem maiores ou iguais a 4, perde o jogo
        if (personagem.infracoes > 4) {
            perdeuJogo = true;
        }

        // Verifica se o personagem saiu da pista e mostra a caixa de diálogo
        if (personagem.sofreuInfracao) {
            gameScreen.caixaDialogo.npc = guarda;
            gameScreen.caixaDialogo.setTextoDialogo(carregaDialogos(guarda, 0));
            gameScreen.caixaDialogo.defineTexturaNpc();
            mostrarDialogo = true;
        }
    }

    private void atualizaInfracao() {
        // Atualiza a posição do personagem, precisa verificar para qual sentido ele
        // está indo, desta forma ajustar -x valor que é a margem para sofrer a
        // infração.
        // personagem.setPosition()
        guarda.DIALOGO_ATUAL++;
        personagem.infracoes++;
        Gdx.app.log("Controller", "Atualizando diálogo do guarda... Atual: " + guarda.DIALOGO_ATUAL);
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
                    controleMissao.npcEstaNaMisao(npc.nome) != null &&
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
        return carregaDialogos(npc, MISSAO);
    }

    /**
     * Carrega os diálogos do personagem
     * 
     * @param npc    Npc
     * @param missao int
     */
    private String carregaDialogos(Npc npc, int missao) {
        // Carregar diálogos
        Dialog dialogo = DialogDAO.carregarDialogos(npc.nome);
        if (dialogo != null) {
            // Verifica se tem uma missão
            return dialogo.buscarProximoDialogoMissao(missao, npc.DIALOGO_ATUAL).getMensagem();
        }
        return "Olá, eu sou o " + npc.nome + "!";
    }

    /**
     * Monta os retângulos de colisão do mapa
     *
     * @param mapa TiledMap
     */
    private void montarColisao(TiledMap mapa) {
        Gdx.app.log("Controller", "Crianado a camada de colisao...");
        // Carrega os objetos de colisão
        jogo.objetosColisao = mapa.getLayers().get("colisao").getObjects();
        Gdx.app.log("Controller", "Objetos carregados: " + jogo.objetosColisao.getCount());
        jogo.retangulosColisao = new Array<Rectangle>();
        Gdx.app.log("Controller", "Array de retângulos de colisão criado... Tamanho: " + jogo.retangulosColisao.size);

        // Adiciona os retângulos de colisão do mapa ao array
        for (MapObject objeto : jogo.objetosColisao) {
            if (objeto instanceof RectangleMapObject) {
                Rectangle retangulo = ((RectangleMapObject) objeto).getRectangle();
                jogo.retangulosColisao.add(retangulo);
            }
        }

        // Adiciona os retângulos de colisão do mapa ao personagem
        Gdx.app.log("Controller", "Verificando cada retângulo da colisao, objetos: " + jogo.retangulosColisao.size);
        personagem.setRetangulosColisao(jogo.retangulosColisao);
    }

    /**
     * Monta os retângulos de colisão da pista
     *
     * @param mapa        TiledMap
     * @param removePista boolean - Remove a pista
     */
    private void montarCamadaPista(boolean removePista) {
        Gdx.app.log("Controller", "Iniciando chamada montagem da camada de objetos da pista...");
        if (removePista) {
            Gdx.app.log("Controller", "Removendo a camada de objetos da pista...");
            jogo.retangulosPista = new Array<Rectangle>();
            personagem.setRetangulosPista(jogo.retangulosPista);
        } else {
            Gdx.app.log("Controller", "Crianado a camada de objetos da pista...");
            // Carrega os objetos de pista
            jogo.objetosPista = mapas.mapa.getLayers().get("pista").getObjects();
            Gdx.app.log("Controller", "Objetos carregados: " + jogo.objetosPista.getCount());
            jogo.retangulosPista = new Array<Rectangle>();
            Gdx.app.log("Controller", "Array de retângulos de colisão criado... Tamanho: " + jogo.retangulosPista.size);

            // Adiciona os retângulos de pista do mapa ao array
            for (MapObject objeto : jogo.objetosPista) {
                if (objeto instanceof RectangleMapObject) {
                    Rectangle retangulo = ((RectangleMapObject) objeto).getRectangle();
                    jogo.retangulosPista.add(retangulo);
                }
            }

            // Adiciona os retângulos de pista do mapa ao personagem
            Gdx.app.log("Controller", "Verificando cada retângulo da pista, objetos: " + jogo.retangulosPista.size);
            personagem.setRetangulosPista(jogo.retangulosPista);
        }
    }

}
