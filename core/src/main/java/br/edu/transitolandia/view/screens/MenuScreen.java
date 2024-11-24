package br.edu.transitolandia.view.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import br.edu.transitolandia.Transitolandia;

public class MenuScreen implements Screen {

    public final Transitolandia jogo;
    public Texture background;
    /** Gerencia o botão */
    public Stage stage;
    /** Define o estilo do botão */
    public Skin skin;
    public TextButton botaoJogar, botaoNovoJogo, botaoConfig, botaoSair;

    public MenuScreen(final Transitolandia jogo) {
        this.jogo = jogo;
        // Carrega a textura de fundo
        background = new Texture(Gdx.files.internal("files/backgrounds/cityBackground.png"));

        // Cria o Stage e o Skin
        stage = new Stage();
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

        // Cria o botão
        botaoJogar = new TextButton("Jogar", skin, "default");
        botaoJogar.setSize(160, 60);
        botaoJogar.setPosition(Gdx.graphics.getWidth() / 2 - botaoJogar.getWidth() / 2, Gdx.graphics.getHeight() / 2 - botaoJogar.getHeight() / 2);
        

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
                // Ação a ser executada quando o botão for clicado
                jogo.setScreen(new ConfigScreen(jogo));
            }
        });

        // Adiciona uma ação ao botão Novo Jogo
        botaoNovoJogo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Ação a ser executada quando o botão for clicado
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
                // Ação a ser executada quando o botão for clicado
                jogo.setScreen(new GameScreen(jogo));
            }
        });

        // Adiciona o botão ao Stage
        stage.addActor(botaoJogar);
        stage.addActor(botaoNovoJogo);
        stage.addActor(botaoConfig);
        stage.addActor(botaoSair);


    }
    
    

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.BLACK);

        
        // Inicia o batch de desenho
        jogo.batch.begin();
        // Desenha a textura de fundo
        jogo.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // x e y são as coordenadas da tela onde o texto será desenhado,
        // estes valores estão em metros
       
        // Finaliza o batch de desenho
        jogo.batch.end();

        // Verifica se o jogador clicou na tela
        if (Gdx.input.isTouched()) {
            jogo.setScreen(new GameScreen(jogo));
            dispose();
        }

        // Desenha o Stage (e os atores, incluindo o botão)
        stage.act(delta);
        stage.draw();
       

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
