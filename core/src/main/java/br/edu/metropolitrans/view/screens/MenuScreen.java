package br.edu.metropolitrans.view.screens;

import javax.swing.text.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.edu.metropolitrans.MetropoliTrans;

public class MenuScreen implements Screen {

    public final MetropoliTrans jogo;
    public Texture background;
    /** Gerencia o botão */
    public Stage stage;
    /** Define o estilo do botão */
    public Skin skin;
    public Label titulo;
    public TextButton botaoJogar, botaoNovoJogo, botaoConfig, botaoSair;
    public ImageButton botaoMute;
    public boolean isMuted = false;
    public Viewport viewport;

    public MenuScreen(final MetropoliTrans jogo) {
        this.jogo = jogo;
        // Carrega a textura de fundo
        background = new Texture(Gdx.files.internal("files/backgrounds/background-light.png"));

        // Cria o Viewport e o Stage
        viewport = new ScreenViewport(); // Usa ScreenViewport para ajustar automaticamente ao tamanho da janela
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        skin.add("default", jogo.fonte);

        Texture botaoTextura = new Texture(Gdx.files.internal("files/buttons/botao-dark2.png"));
        Drawable botaoDrawable = new TextureRegionDrawable(new TextureRegion(botaoTextura));

        // Cria o estilo do botão
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.up = botaoDrawable;

        // Adiciona o estilo ao skin
        skin.add("default", textButtonStyle);

        // Carrega as imagens para o botão de mute
        Texture somTexture = new Texture(Gdx.files.internal("files/buttons/som.png"));
        Texture muteTexture = new Texture(Gdx.files.internal("files/buttons/mute.png"));
        Drawable somDrawable = new TextureRegionDrawable(new TextureRegion(somTexture));
        Drawable muteDrawable = new TextureRegionDrawable(new TextureRegion(muteTexture));

        // Cria o botão de mute
        ImageButton.ImageButtonStyle muteButtonStyle = new ImageButton.ImageButtonStyle();
        muteButtonStyle.imageUp = somDrawable;
        muteButtonStyle.imageChecked = muteDrawable;
        botaoMute = new ImageButton(muteButtonStyle);
        botaoMute.setPosition(Gdx.graphics.getWidth() - botaoMute.getWidth() - 10,
                Gdx.graphics.getHeight() - botaoMute.getHeight() - 10); // Posição no canto superior direito

        // Adiciona um listener ao botão de mute
        botaoMute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMuted = !isMuted;
                if (isMuted) {
                    jogo.MusicaMenu.pause();
                    botaoMute.getStyle().imageUp = muteDrawable;
                } else {
                    jogo.MusicaMenu.play();
                    botaoMute.getStyle().imageUp = somDrawable;
                }
            }
        });

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("files/fonts/monogram.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80; // Define o tamanho da fonte para o título
        BitmapFont fonteTitulo = generator.generateFont(parameter);
        generator.dispose(); // Libera os recursos do gerador

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonteTitulo;
        labelStyle.fontColor = Color.valueOf("4c4869");
        titulo = new Label("MetropoliTrans", labelStyle);
        titulo.setPosition(Gdx.graphics.getWidth() / 2 - titulo.getWidth() / 2, Gdx.graphics.getHeight() - 190);

        // Cria o botão
        botaoJogar = new TextButton("Jogar", skin, "default");
        botaoJogar.setSize(160, 60);
        botaoJogar.setPosition(Gdx.graphics.getWidth() / 2 - botaoJogar.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - botaoJogar.getHeight() / 2);

        botaoNovoJogo = new TextButton("Novo Jogo", skin, "default");
        botaoNovoJogo.setSize(160, 60);
        botaoNovoJogo.setPosition(botaoJogar.getX(), botaoJogar.getY() - botaoJogar.getHeight());

        botaoConfig = new TextButton("Configurações", skin, "default");
        botaoConfig.setSize(160, 60);
        botaoConfig.setPosition(botaoJogar.getX(), botaoNovoJogo.getY() - botaoNovoJogo.getHeight());

        botaoSair = new TextButton("Sair", skin, "default");
        botaoSair.setSize(160, 60);
        botaoSair.setPosition(botaoJogar.getX(), botaoConfig.getY() - botaoConfig.getHeight());

        // Adiciona uma ação ao botão Configurações
        botaoConfig.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                jogo.setScreen(new ConfigScreen(jogo));
            }
        });

        // Adiciona uma ação ao botão Novo Jogo
        botaoNovoJogo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                jogo.setScreen(new GameScreen(jogo));
            }
        });

        // Adiciona uma ação ao botão Sair
        botaoSair.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Adiciona uma ação ao botão Jogar
        botaoJogar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.setScreen(new GameScreen(jogo));
            }
        });

        // Adiciona o botão ao Stage
        stage.addActor(titulo);
        stage.addActor(botaoJogar);
        stage.addActor(botaoNovoJogo);
        stage.addActor(botaoConfig);
        stage.addActor(botaoSair);
        stage.addActor(botaoMute);

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.BLACK);

        // Atualiza o viewport
        viewport.apply();

        // Inicia o batch de desenho
        jogo.batch.begin();

        // Desenha a textura de fundo redimensionada
        jogo.batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        // x e y são as coordenadas da tela onde o texto será desenhado,
        // estes valores estão em metros

        // Finaliza o batch de desenho
        jogo.batch.end();

        // Desenha o Stage (e os atores, incluindo o botão)
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        // Reposiciona os elementos da interface do usuário
        botaoMute.setPosition(viewport.getWorldWidth() - botaoMute.getWidth() - 10,
                viewport.getWorldHeight() - botaoMute.getHeight() - 10);
        botaoJogar.setPosition(viewport.getWorldWidth() / 2 - botaoJogar.getWidth() / 2,
                viewport.getWorldHeight() / 2 - botaoJogar.getHeight() / 2);
        botaoNovoJogo.setPosition(botaoJogar.getX(), botaoJogar.getY() - botaoJogar.getHeight());
        botaoConfig.setPosition(botaoJogar.getX(), botaoNovoJogo.getY() - botaoNovoJogo.getHeight());
        botaoSair.setPosition(botaoJogar.getX(), botaoConfig.getY() - botaoConfig.getHeight());
        titulo.setPosition(viewport.getWorldWidth() / 2 - titulo.getWidth() / 2, viewport.getWorldHeight() - 190);
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
        stage.dispose();
        skin.dispose();
    }

}