package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.font.FontBase;

public class LoadingScreen implements Screen {
    public final MetropoliTrans jogo;
    public OrthographicCamera camera;
    public SpriteBatch batch;
    public BitmapFont font;
    public Texture background;
    public String gameName = "MetropoliTrans";
    public String displayedText = "";
    public float timeElapsed = 0f;
    public float typingSpeed = 0.1f; // Velocidade de digitação
    public float displayTime = 0.5f; // Tempo para exibir o título após a digitaçã

    public LoadingScreen(final MetropoliTrans jogo) {
        this.jogo = jogo;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        // Carrega a textura de fundo
        background = new Texture(Gdx.files.internal("files/backgrounds/background-light.png"));

        // Carregar a fonte
        font = FontBase.getInstancia().getFonte(50, new Color(0x4c4869ff), FontBase.Fontes.MONOGRAM);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Desenhar o background
        batch.draw(background, 0, 0, 800, 480);
        // Desenhar o texto
        font.draw(batch, displayedText, 800 - font.getRegion().getRegionWidth() / 2, 240);
        batch.end();

        // Atualizar o texto exibido com o efeito de digitação
        timeElapsed += delta;
        if (timeElapsed >= typingSpeed && displayedText.length() < gameName.length()) {
            displayedText += gameName.charAt(displayedText.length());
            timeElapsed = 0f;
        }

        // Manter o título na tela por alguns segundos após a digitação completa
        if (displayedText.equals(gameName)) {
            displayTime -= delta;
            if (displayTime <= 0) {
                jogo.inicializarJogo();
                jogo.inicializarComponentesMissao();

                jogo.telas.put("menu", new MenuScreen(jogo));
                jogo.setScreen(jogo.telas.get("menu"));

                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {

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
        background.dispose();
        batch.dispose();
        font.dispose();
    }

}
