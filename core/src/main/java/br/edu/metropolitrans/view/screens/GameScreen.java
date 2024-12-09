package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.view.components.dialog.DialogBox;
import br.edu.metropolitrans.view.components.missionalert.MissionAlert;

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

    /**
     * Caixa de diálogo
     */
    public DialogBox caixaDialogo;

    /**
     * Flag para mostrar a caixa de diálogo
     */
    public boolean mostrarDialogo;

    /**
     * Missão atual
     */
    public int MISSAO = 0;

    public MissionAlert missionAlert;

    public GameScreen(final MetropoliTrans jogo) {
        this.jogo = jogo;

        // Inicializa o renderizador de formas
        renderizadorForma = new ShapeRenderer();

        // Define a camera do jogo
        CAMERA = new OrthographicCamera();
        CAMERA.setToOrtho(false, TELA_LARGURA, TELA_ALTURA);
        CAMERA.update();

        // Inicializa a caixa de diálogo
        caixaDialogo = new DialogBox(0, 64, 1280, 150, jogo);
        mostrarDialogo = false;

        missionAlert = new MissionAlert(jogo.batch);

    }

    @Override
    public void show() {
        // Para a música do menu e inicia a música do jogo
        jogo.MusicaMenu.stop();
    }

    @Override
    public void render(float delta) {

        // Desenha os elementos do jogo
        desenhar();

        // Controle da interação do personagem com os objetos do mapa
        jogo.controller.controleInteracao();

        // Controle da tela de configurações
        controleConfig();

        // Controle de diálogos
        jogo.controller.controleDialogos();

        // Verifica se a caixa de diálogo deve ser exibida
        // Se sim, exibe a caixa de diálogo, caso contrário permite
        // o controle do personagem continuando o jogo
        if (mostrarDialogo) {
            caixaDialogo.render();
        } else {
            // Controle do personagem Setas ou WASD
            jogo.controller.controlePersonagem(delta);
            jogo.controller.controlePersonagem2(delta);
        }
    }

    /**
     * Desenha a tela do jogo
     */
    private void desenhar() {

        // Atualiza o temporizador
        float dt = Gdx.graphics.getDeltaTime();
        temporizador += dt;

        // Limpa a tela com uma cor preta
        Gdx.gl.glClearColor(0, 0, 0, 1);
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Alinhamento da câmera do jogo
        alinhamentoCamera();

        // Renderiza o mapa, camadas de sobpiso, piso e colisão
        jogo.mapaRenderizador.setView(CAMERA);
        jogo.mapaRenderizador.render(new int[] { 0, 1, 2 }); // Sobpiso

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
        if (jogo.mapas.mapa.getLayers().getCount() > 3) {
            // Renderiza a camada de Topo
            jogo.mapaRenderizador.render(new int[] { 3 }); // Topo
        }

        // Atualiza a posição da caixa de diálogo para acompanhar a câmera
        caixaDialogo.setPosition(CAMERA.position.x - CAMERA.viewportWidth / 2,
                CAMERA.position.y - CAMERA.viewportHeight / 2);

        // Desenha o alerta de missão acima da posição do NPC
        if (jogo.personagem.npcs != null) {
            for (Npc npc : jogo.personagem.npcs) {
                missionAlert.x = npc.getX();
                missionAlert.y = npc.getY();
                missionAlert.status = npc.statusAlertaMissao;
                missionAlert.render();
            }
        }

        // Usado apenas para debug, comentar quando não for mais necessário
        // debug();
    }

    /**
     * Controle da tela de configurações
     */
    public void controleConfig() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            jogo.telas.put("config", new ConfigScreen(jogo, GameScreen.this));
            jogo.setScreen(jogo.telas.get("config"));
        }
    }

    /**
     * Alinha a câmera do jogo
     */
    public void alinhamentoCamera() {
        // Atualiza a câmera do jogo
        CAMERA.position.set(jogo.personagem.getX() + jogo.personagem.getOriginX(),
                jogo.personagem.getY() + jogo.personagem.getOriginY(),
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
        if (jogo.retangulosColisao != null) {
            for (Rectangle retangulo : jogo.retangulosColisao) {
                renderizadorForma.rect(retangulo.x, retangulo.y, retangulo.width, retangulo.height);
            }
        }

        // Desenha polígono de colisão do personagem
        if (jogo.personagem != null) {
            Polygon personagemPoligono = jogo.personagem.getLimitePoligono();
            renderizadorForma.polygon(personagemPoligono.getTransformedVertices());
        }

        // Desenha polígonos de colisão dos NPCs
        if (jogo.personagem.npcs != null) {
            for (Npc npc : jogo.personagem.npcs) {
                Polygon npcPoligono = npc.getLimitePoligono();
                renderizadorForma.polygon(npcPoligono.getTransformedVertices());
            }
        }

        // Desenha o polígono de colisão do objeto interativo
        if (jogo.objeto != null) {
            Polygon objetoPoligono = jogo.objeto.getLimitePoligono();
            renderizadorForma.polygon(objetoPoligono.getTransformedVertices());
        }

        // Desenha o polígono de colisão do objeto interativo
        if (jogo.objetoSairSala != null) {
            Polygon objetoPoligono = jogo.objetoSairSala.getLimitePoligono();
            renderizadorForma.polygon(objetoPoligono.getTransformedVertices());
        }
        renderizadorForma.end();
    }

    @Override
    public void resize(int width, int height) {
        jogo.estagioPrincipal.getViewport().update(width, height, true);

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
        jogo.mapaRenderizador.dispose();
    }

}
