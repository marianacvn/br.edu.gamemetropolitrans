package br.edu.transitolandia.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import br.edu.transitolandia.Transitolandia;
import br.edu.transitolandia.model.actors.Personagem;
import br.edu.transitolandia.model.actors.maps.Mapas;

/**
 * Tela principal do jogo
 */
public class GameScreen implements Screen {

    // Largura e altura da tela do jogo
    public static final int TELA_LARGURA = 1200;
    public static final int TELA_ALTURA = 800;

    /**
     * Referência para o jogo principal
     */
    final Transitolandia jogo;

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

    public GameScreen(final Transitolandia jogo) {
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
        personagem = new Personagem(300, 5000, jogo.estagioPrincipal);
        personagem.setCamera(CAMERA);
        personagem.setLimitacaoMundo(Mapas.MAPA_LARGURA, Mapas.MAPA_ALTURA);

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

        // Realiza açoes do estagio principal
        jogo.estagioPrincipal.act(dt);

        // Limpa a tela com uma cor preta
        // ScreenUtils.clear(Color.BLACK);
        Gdx.gl.glClearColor(0, 0, 0, 0.1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Atualiza a câmera do jogo
        // jogo.areaVisualizacao.apply();
        // jogo.batch.setProjectionMatrix(jogo.areaVisualizacao.getCamera().combined);
        CAMERA.position.x = personagem.getX();
        CAMERA.position.y = personagem.getY();
        CAMERA.update();

        // Renderiza o mapa
        mapaRenderizador.setView(CAMERA);
        mapaRenderizador.render();

        renderizadorForma.setProjectionMatrix(CAMERA.combined);
        jogo.batch.setProjectionMatrix(CAMERA.combined);

        // Inicia o batch de desenho
        jogo.batch.begin();

        // Desenha o fundo da tela
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
