package br.edu.metropolitrans.view.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.Dialog;
import br.edu.metropolitrans.model.DialogMission;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.ObjetoInterativo;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.model.dao.DialogDAO;
import br.edu.metropolitrans.model.maps.Mapas;

/**
 * Tela principal do jogo
 */
public class GameScreen implements Screen {

    // Largura e altura da tela do jogo
    public static final int TELA_LARGURA = 1280;
    public static final int TELA_ALTURA = 720;

    /**
     * Referência para o jogo principal
     */
    final MetropoliTrans jogo;

    /**
     * Câmera do jogo
     */
    private final OrthographicCamera CAMERA;

    /**
     * Personagem do jogo
     */
    Personagem personagem;

    /**
     * Usado para renderizar o mapa
     */
    private OrthogonalTiledMapRenderer mapaRenderizador;

    /**
     * Mapas do jogo
     */
    public static Mapas mapas;

    /**
     * Informa ao renderizador quantos pixels correspondem a uma unidade do mundo
     */
    float unitScale = 1;

    /**
     * Renderizador de formas/objetos
     */
    private final ShapeRenderer renderizadorForma;

    /**
     * Temporizador para acompanhar o tempo de jogo
     */
    float temporizador = 0f;

    private MapObjects objetosColisao;
    private Array<Rectangle> retangulosColisao;
    public ArrayList<Npc> npcs;
    public ObjetoInterativo objeto, objetoSairSala;

    public GameScreen(final MetropoliTrans jogo) {
        this.jogo = jogo;

        // Carrega o mapa
        mapas = new Mapas();
        mapaRenderizador = new OrthogonalTiledMapRenderer(mapas.mapa, jogo.batch);

        // Inicializa o renderizador de formas
        renderizadorForma = new ShapeRenderer();

        // Define a camera do jogo
        CAMERA = new OrthographicCamera();
        CAMERA.setToOrtho(false, TELA_LARGURA, TELA_ALTURA);
        CAMERA.update();

        // Carrega as imagens
        personagem = new Personagem(640, 250, jogo.estagioPrincipal);
        Personagem.setLimitacaoMundo(Mapas.MAPA_LARGURA, Mapas.MAPA_ALTURA);

        // Carrega os objetos de colisão
        montarColisao(mapas.mapa);

        // Adiciona os npcs em um array
        npcs = new ArrayList<Npc>();

        // Carrega os Npcs
        npcs.add(new Npc("maria", 280, 1050, "npcFemale/maria/sprite.png", jogo.estagioPrincipal));
        npcs.add(new Npc("betania", 150, 400, "npcFemale/betania/sprite.png", jogo.estagioPrincipal));
        npcs.add(new Npc("bruna", 1190, 200, "npcFemale/bruna/sprite.png", jogo.estagioPrincipal));
        npcs.add(new Npc("antonio", 1500, 1000, "npcMale/antonio/sprite.png", jogo.estagioPrincipal));
        npcs.add(new Npc("heberto", 150, 200, "npcMale/heberto/sprite.png", jogo.estagioPrincipal));
        npcs.add(new Npc("jose", 90, 1450, "npcMale/jose/sprite.png", jogo.estagioPrincipal));
        npcs.add(new Npc("josinaldo", 2090, 150, "npcMale/josinaldo/sprite.png", jogo.estagioPrincipal));
        npcs.add(new Npc("paulo", 1500, 100, "npcMale/paulo/sprite.png", jogo.estagioPrincipal));
        npcs.add(new Npc("juliana", 1200, 1250, "npcFemale/juliana/sprite.png", jogo.estagioPrincipal));

        // Adiciona os npcs no array de colisão
        personagem.npcs = npcs;

        objeto = new ObjetoInterativo("entradaPrefeitura", 32, 220, "background-transparent.png",
                jogo.estagioPrincipal);

        // Define a tela anterior ao iniciar um novo jogo
        jogo.telas.put("config", new ConfigScreen(jogo, GameScreen.this));
    }

    @Override
    public void show() {
        // Para a música do menu e inicia a música do jogo
        jogo.MusicaMenu.stop();
    }

    @Override
    public void render(float delta) {
        desenhar();
        controle(delta);
        controle2(delta);
        interagir();
        controleConfig(delta);
    }

    /**
     * Desenha a tela do jogo
     */
    private void desenhar() {

        // Atualiza o temporizador
        float dt = Gdx.graphics.getDeltaTime();
        temporizador += dt;

        // Limpa a tela com uma cor preta
        // ScreenUtils.clear(Color.BLACK);
        Gdx.gl.glClearColor(0, 0, 0, 0.1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Alinhamento da câmera do jogo
        alinhamentoCamera();

        // Renderiza o mapa, camadas de sobpiso, piso e colisão
        mapaRenderizador.setView(CAMERA);
        mapaRenderizador.render(new int[] { 0 }); // Sobpiso
        mapaRenderizador.render(new int[] { 1 }); // Piso
        mapaRenderizador.render(new int[] { 2 }); // Colisao
        mapaRenderizador.render(new int[] { 3 }); // Colisao

        // Inicia o batch de desenho
        jogo.batch.begin();

        // Rendereiza as formas
        renderizadorForma.setProjectionMatrix(CAMERA.combined);
        jogo.batch.setProjectionMatrix(CAMERA.combined);

        // Realiza açoes do estagio principal
        // Desenha o fundo da tela
        jogo.estagioPrincipal.act(dt);
        jogo.estagioPrincipal.draw();

        // Finaliza o batch de desenho
        jogo.batch.end();

        // Verifica se a camada de topo existe antes de renderizá-la
        if (mapas.mapa.getLayers().getCount() > 3) {
            // Renderiza a camada de Topo
            mapaRenderizador.render(new int[] { 3 }); // Topo
        }

        // Usado apenas para debug, comentar quando não for mais necessário
        debug();
    }

    /**
     * Controle do personagem, movimenta de acordo com as teclas pressionadas
     * (Setas)
     */
    public void controle(float delta) {
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
    public void controle2(float delta) {
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
     * Monta os retângulos de colisão do mapa
     * 
     * @param mapa TiledMap
     */
    public void montarColisao(TiledMap mapa) {
        // Carrega os objetos de colisão
        objetosColisao = mapa.getLayers().get("colisao").getObjects();
        retangulosColisao = new Array<Rectangle>();

        // Adiciona os retângulos de colisão do mapa ao array
        for (MapObject objeto : objetosColisao) {
            if (objeto instanceof RectangleMapObject) {
                Rectangle retangulo = ((RectangleMapObject) objeto).getRectangle();
                retangulosColisao.add(retangulo);
            }
        }

        // Adiciona os retângulos de colisão do mapa ao personagem
        personagem.setRetangulosColisao(retangulosColisao);
    }

    /**
     * Verifica a interação do personagem com os objetos do mapa
     */
    private void interagir() {
        if (objeto != null && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            for (Npc npc : npcs) {
                if (personagem.estaDentroDaDistancia(15,npc)) {
                    System.out.println("Interagindo com o NPC: " + npc.nome);
                    // Chama o método testeDialogos com o nome do NPC
                    testeDialogos(npc.nome);
                    return;
                }
            }

        }

        if (objeto != null && personagem.interagiu(objeto) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            // Remove os NPCs do mapa
            for (Npc npc : npcs) {
                npc.remove();
            }

            // Muda o mapa para room.tmx
            mapaRenderizador.dispose();
            mapaRenderizador = new OrthogonalTiledMapRenderer(mapas.sala, jogo.batch);
            ;

            // Salva a última posicao e setar posição do personagem para a entrada
            personagem.setPosition(1248, 1000);
            objetoSairSala = new ObjetoInterativo("sairSala", 1216, 964, "background-transparent.png",
                    jogo.estagioPrincipal);
            objeto = null;

            // Carrega os objetos de colisão
            montarColisao(mapas.sala);

            // Renova os retângulos de colisão
            personagem.npcs = new ArrayList<Npc>();
            personagem.setRetangulosColisao(retangulosColisao);
        } else if (objetoSairSala != null && personagem.interagiu(objetoSairSala)
                && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            // Adiciona os NPCs no array
            personagem.npcs = npcs;
            for (Npc npc : npcs) {
                npc.adicionarNoEstagio(jogo.estagioPrincipal);
            }

            // Muda o mapa para map.tmx
            mapaRenderizador.dispose();
            mapaRenderizador = new OrthogonalTiledMapRenderer(mapas.mapa, jogo.batch);

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
            personagem.setRetangulosColisao(retangulosColisao);
        }
    }

    public void testeDialogos(String nomePersonagem) {
        DialogDAO dialogDAO = new DialogDAO();

        // Carregar diálogos
        Dialog dialog = dialogDAO.carregarDialogos(nomePersonagem);
        if (dialog != null) {
            System.out.println("Diálogos Genéricos:");
            for (String dialogo : dialog.getDialogosGenericos()) {
                System.out.println(dialogo);
            }

            System.out.println("Diálogos de Missão:");
            for (DialogMission missao : dialog.getDialogosMissao()) {
                System.out.println("Missão: " + missao.getMissao());
                System.out.println("Mensagem: " + missao.getMensagem());
            }
        }

        // Salvar diálogos (se necessário)
        //dialogDAO.salvarDialogos("antonio", dialog);
    }

    public void controleConfig(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (jogo.telas.get("config") == null)
                jogo.telas.put("config", new ConfigScreen(jogo, GameScreen.this));
            jogo.setScreen(jogo.telas.get("config"));
        }
    }

    /**
     * Alinha a câmera do jogo
     */
    public void alinhamentoCamera() {
        // Atualiza a câmera do jogo
        CAMERA.position.set(personagem.getX() + personagem.getOriginX(), personagem.getY() + personagem.getOriginY(),
                0);

        // Calcular os limites da câmera
        float minimoX = CAMERA.viewportWidth / 2;
        float maximoX = Personagem.getLimitacaoMundo().width - minimoX;
        float minimoY = CAMERA.viewportHeight / 2;
        float maximoY = Personagem.getLimitacaoMundo().height - minimoY;

        // Clampar a posição da câmera para garantir que ela não ultrapasse os limites
        // do mundo
        CAMERA.position.x = MathUtils.clamp(CAMERA.position.x, minimoX, maximoX);
        CAMERA.position.y = MathUtils.clamp(CAMERA.position.y, minimoY, maximoY);

        // Atualiza a câmera
        CAMERA.update();
    }

    /**
     * Desenha formas para debug
     */
    public void debug() {
        // Desenha quadrados da colisão para debug
        renderizadorForma.begin(ShapeRenderer.ShapeType.Line);
        renderizadorForma.setColor(1, 0, 0, 1);

        // Desenha retângulos de colisão do mapa
        if (retangulosColisao != null) {
            for (Rectangle retangulo : retangulosColisao) {
                renderizadorForma.rect(retangulo.x, retangulo.y, retangulo.width, retangulo.height);
            }
        }

        // Desenha polígono de colisão do personagem
        if (personagem != null) {
            Polygon personagemPoligono = personagem.getLimitePoligono();
            renderizadorForma.polygon(personagemPoligono.getTransformedVertices());
        }

        // Desenha polígonos de colisão dos NPCs
        if (personagem.npcs != null) {
            for (Npc npc : personagem.npcs) {
                Polygon npcPoligono = npc.getLimitePoligono();
                renderizadorForma.polygon(npcPoligono.getTransformedVertices());
            }
        }

        // Desenha o polígono de colisão do objeto interativo
        if (objeto != null) {
            Polygon objetoPoligono = objeto.getLimitePoligono();
            renderizadorForma.polygon(objetoPoligono.getTransformedVertices());
        }

        // Desenha o polígono de colisão do objeto interativo
        if (objetoSairSala != null) {
            Polygon objetoPoligono = objetoSairSala.getLimitePoligono();
            renderizadorForma.polygon(objetoPoligono.getTransformedVertices());
        }
        renderizadorForma.end();
    }

    @Override
    public void resize(int width, int height) {
        // jogo.areaVisualizacao.update(width, height, true);
        CAMERA.viewportWidth = width;
        CAMERA.viewportHeight = height;
        CAMERA.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        mapaRenderizador.dispose();
    }

}
