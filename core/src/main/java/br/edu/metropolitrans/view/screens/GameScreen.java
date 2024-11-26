package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.model.actors.maps.Mapas;

/**
 * Tela principal do jogo
 */
public class GameScreen implements Screen {

    // Largura e altura da tela do jogo
    public static final int TELA_LARGURA = 800;
    public static final int TELA_ALTURA = 500;

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
    private final OrthogonalTiledMapRenderer mapaRenderizador, salaRenderizador;

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

    public GameScreen(final MetropoliTrans jogo) {
        this.jogo = jogo;

        // Carrega o mapa
        mapas = new Mapas();
        mapaRenderizador = new OrthogonalTiledMapRenderer(mapas.mapa, unitScale);
        salaRenderizador = new OrthogonalTiledMapRenderer(mapas.sala, unitScale);

        // Inicializa o renderizador de formas
        renderizadorForma = new ShapeRenderer();

        // Define a camera do jogo
        CAMERA = new OrthographicCamera();
        CAMERA.setToOrtho(false, TELA_LARGURA, TELA_ALTURA);
        CAMERA.update();

        // Carrega as imagens
        personagem = new Personagem(610, 4600, jogo.estagioPrincipal);
        Personagem.setLimitacaoMundo(Mapas.MAPA_LARGURA, Mapas.MAPA_ALTURA);

    }

    @Override
    public void show() {
        // Para a música do menu e inicia a música do jogo
        jogo.MusicaMenu.stop();
        jogo.MusicaPrincipal.play();
    }

    @Override
    public void render(float delta) {
        desenhar();
        controle(delta);
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

        // Renderiza o mapa
        mapaRenderizador.setView(CAMERA);
        mapaRenderizador.render();

        renderizadorForma.setProjectionMatrix(CAMERA.combined);
        jogo.batch.setProjectionMatrix(CAMERA.combined);

        // Inicia o batch de desenho
        jogo.batch.begin();

        // Realiza açoes do estagio principal
        // Desenha o fundo da tela
        jogo.estagioPrincipal.act(dt);
        jogo.estagioPrincipal.draw();

        // Finaliza o batch de desenho
        jogo.batch.end();
    }

    /**
     * Controle do personagem, movimenta de acordo com as teclas pressionadas
     */
    public void controle(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // personagem.setX(personagem.getX() + Personagem.VELOCIDADE * delta);
            personagem.acelerarEmAngulo(0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // personagem.setX(personagem.getX() - Personagem.VELOCIDADE * delta);
            personagem.acelerarEmAngulo(180);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // personagem.setY(personagem.getY() + Personagem.VELOCIDADE * delta);
            personagem.acelerarEmAngulo(90);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // personagem.setY(personagem.getY() + Personagem.VELOCIDADE * delta);
            personagem.acelerarEmAngulo(270);
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

    @Override
    public void resize(int width, int height) {
        // jogo.areaVisualizacao.update(width, height, true);
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
        salaRenderizador.dispose();
    }

}
