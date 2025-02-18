package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.ConfigData;
import br.edu.metropolitrans.model.dao.ConfigDAO;
import br.edu.metropolitrans.view.components.buttons.ImageButtonBase;
import br.edu.metropolitrans.view.components.buttons.TextButtonBase;
import br.edu.metropolitrans.view.font.FontBase;

public class MenuScreen implements Screen {

    public final MetropoliTrans jogo;
    /* Background da tela */
    public Texture background;
    /** Gerencia o botão */
    public Stage stage;
    /** Define o estilo do botão */
    public Skin skin;
    public Label titulo;
    public TextButtonBase botaoNovoJogo, botaoConfig, botaoCreditos, botaoSair;
    public ImageButtonBase botaoMute;
    /**
     * Define se o som está mutado ou não
     */
    public boolean isMuted;
    /**
     * Viewport da tela
     */
    public Viewport viewport;

    public MenuScreen(final MetropoliTrans jogo) {
        this.jogo = jogo;
        // Carrega a textura de fundo
        background = new Texture(Gdx.files.internal("files/backgrounds/background-principal-2.png"));

        isMuted = ConfigDAO.carregarConfig().isMute();

        // Cria o Viewport e o Stage
        viewport = new ScreenViewport(); // Usa ScreenViewport para ajustar automaticamente ao tamanho da janela
        stage = new Stage(viewport, jogo.batch);
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

        // Cria o botão de mute
        botaoMute = new ImageButtonBase("files/buttons/som.png", "files/buttons/mute.png");
        botaoMute.setPosition(Gdx.graphics.getWidth() - botaoMute.getWidth() - 10,
                Gdx.graphics.getHeight() - botaoMute.getHeight() - 10); // Posição no canto superior direito

        // Adiciona um listener ao botão de mute
        botaoMute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.efeitoConfirmar.play();

                isMuted = !isMuted;

                ConfigData config = ConfigDAO.carregarConfig();
                config.setMute(isMuted);
                ConfigDAO.salvarConfig(config);

                if (isMuted) {
                    jogo.musicaMenu.pause();

                } else {
                    jogo.musicaMenu.play();
                }
            }
        });

        // Carrega a fonte do título
        BitmapFont fonteTitulo = FontBase.getInstancia().getFonte(80, FontBase.Fontes.MONOGRAM);

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonteTitulo;
        // labelStyle.fontColor = Color.valueOf("36334c");
        labelStyle.fontColor = Color.WHITE;
        titulo = new Label("MetropoliTrans", labelStyle);
        titulo.setPosition(Gdx.graphics.getWidth() / 2 - titulo.getWidth() / 2, Gdx.graphics.getHeight() - 190);

        // Cria o botão
        botaoNovoJogo = new TextButtonBase("Novo Jogo", "files/buttons/botao-dark2.png", skin);
        botaoNovoJogo.setSize(160, 60);
        botaoNovoJogo.setPosition(Gdx.graphics.getWidth() / 2 - botaoNovoJogo.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - botaoNovoJogo.getHeight() / 2);

        botaoConfig = new TextButtonBase("Configurações", "files/buttons/botao-dark2.png", skin);
        botaoConfig.setSize(160, 60);
        botaoConfig.setPosition(botaoNovoJogo.getX(), botaoNovoJogo.getY() - botaoNovoJogo.getHeight() - 20);

        botaoCreditos = new TextButtonBase("Créditos", "files/buttons/botao-dark2.png", skin);
        botaoCreditos.setSize(160, 60);
        botaoCreditos.setPosition(botaoNovoJogo.getX(), botaoConfig.getY() - botaoConfig.getHeight() - 20);

        botaoSair = new TextButtonBase("Sair", "files/buttons/botao-dark2.png", skin);
        botaoSair.setSize(160, 60);
        botaoSair.setPosition(botaoNovoJogo.getX(), botaoCreditos.getY() - botaoCreditos.getHeight() - 20);

        // Adiciona uma ação ao botão Configurações
        botaoConfig.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.efeitoConfirmar.play();

                if (jogo.telas.get("config") == null)
                    jogo.telas.put("config", new ConfigScreen(jogo, MenuScreen.this));
                jogo.setScreen(jogo.telas.get("config"));
            }
        });

        // Adiciona uma ação ao botão Créditos
        botaoCreditos.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.efeitoConfirmar.play();

                if (jogo.telas.get("credit") == null)
                    jogo.telas.put("credit", new CreditScreen(jogo, MenuScreen.this));
                jogo.setScreen(jogo.telas.get("credit"));
            }
        });

        // Adiciona uma ação ao botão Novo Jogo
        botaoNovoJogo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.efeitoConfirmar.play();
                jogo.setScreen(new CharacterSelectionScreen(jogo));
            }
        });

        // Adiciona uma ação ao botão Sair
        botaoSair.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.efeitoConfirmar.play();
                Gdx.app.exit();
            }
        });

        // Adiciona o botão ao Stage
        stage.addActor(titulo);
        stage.addActor(botaoNovoJogo);
        stage.addActor(botaoConfig);
        stage.addActor(botaoCreditos);
        stage.addActor(botaoSair);
        stage.addActor(botaoMute);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(0, 0, 0, 1);

        // Atualiza o viewport
        viewport.apply();

        // Desenha o fundo
        jogo.batch.begin();
        jogo.batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        jogo.batch.end();

        // Atualiza e desenha o Stage
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

        // Reposiciona os elementos da interface do usuário
        // botaoMute.setPosition(viewport.getWorldWidth() - botaoMute.getWidth() - 10,
        //         viewport.getWorldHeight() - botaoMute.getHeight() - 10);
        // botaoNovoJogo.setPosition(botaoNovoJogo.getX(), botaoNovoJogo.getY() - botaoNovoJogo.getHeight());
        // botaoConfig.setPosition(botaoNovoJogo.getX(), botaoNovoJogo.getY() - botaoNovoJogo.getHeight());
        // botaoCreditos.setPosition(botaoNovoJogo.getX(), botaoConfig.getY() - botaoConfig.getHeight());
        // botaoSair.setPosition(botaoNovoJogo.getX(), botaoCreditos.getY() - botaoCreditos.getHeight());
        // titulo.setPosition(viewport.getWorldWidth() / 2 - titulo.getWidth() / 2, viewport.getWorldHeight() - 190);
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
        stage.dispose();
        skin.dispose();
        background.dispose();
    }

}
