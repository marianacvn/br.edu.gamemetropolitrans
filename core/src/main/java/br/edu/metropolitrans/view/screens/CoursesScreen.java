package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.badlogic.gdx.utils.ScreenUtils;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.components.buttons.TextButtonSecond;
import br.edu.metropolitrans.view.font.FontBase;

public class CoursesScreen implements Screen {
    public final MetropoliTrans jogo;
    public Stage stage;
    public Texture background;
    public Skin skin;
    public Label titulo;
    public TextButtonSecond botao1, botao2, botao3, botao4, botao5, botao6, botao7, botao8, botao9;

    public CoursesScreen(final MetropoliTrans jogo) {
        this.jogo = jogo;

        // Carrega a textura de fundo e outras
        background = new Texture(Gdx.files.internal("files/backgrounds/background-light.png"));

        // Cria o Stage e o Skin
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        skin.add("default", jogo.fonte);

        BitmapFont fonteTitulo = FontBase.getInstancia().getFonte(80, FontBase.Fontes.PADRAO);

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonteTitulo;
        labelStyle.fontColor = Color.valueOf("4c4869");
        titulo = new Label("Sistema de cursos", labelStyle);

        // Centraliza o título na tela
        float tituloWidth = titulo.getWidth();
        float tituloHeight = titulo.getHeight();
        // no meio do topo da tela
        float tituloX = Gdx.graphics.getWidth() / 2 - tituloWidth / 2;
        float tituloY = Gdx.graphics.getHeight() - tituloHeight - 50;
        titulo.setPosition(tituloX, tituloY);

        

        // Cria os botões 
        botao1 = new TextButtonSecond("Módulo 1", "files/buttons/quadrado.png", skin);
        botao1.setPosition(422, 426);

        botao2 = new TextButtonSecond("Módulo 2", "files/buttons/quadrado.png", skin);
        botao2.setPosition(botao1.getX() + botao1.getWidth() + 20, botao1.getY());

        botao3 = new TextButtonSecond("Módulo 3", "files/buttons/quadrado.png", skin);
        botao3.setPosition(botao2.getX() + botao2.getWidth() + 20, botao2.getY());

        botao4 = new TextButtonSecond("Módulo 4", "files/buttons/quadrado.png", skin);
        botao4.setPosition(botao1.getX(), botao1.getY() - botao1.getHeight() - 15);

        botao5 = new TextButtonSecond("Módulo 5", "files/buttons/quadrado.png", skin);
        botao5.setPosition(botao1.getX() + botao1.getWidth() + 20, botao4.getY());

        botao6 = new TextButtonSecond("Módulo 6", "files/buttons/quadrado.png", skin);
        botao6.setPosition(botao2.getX() + botao2.getWidth() + 20, botao5.getY());

        botao7 = new TextButtonSecond("Módulo 7", "files/buttons/quadrado.png", skin);
        botao7.setPosition(botao4.getX(), botao4.getY() - botao4.getHeight() - 15);

        botao8 = new TextButtonSecond("Módulo 8", "files/buttons/quadrado.png", skin);
        botao8.setPosition(botao1.getX() + botao1.getWidth() + 20, botao4.getY() - 150);

        botao9 = new TextButtonSecond("Módulo 9", "files/buttons/quadrado.png", skin);
        botao9.setPosition(botao2.getX() + botao2.getWidth() + 20, botao5.getY() - 150);

        stage.addActor(titulo);
        stage.addActor(botao1);
        stage.addActor(botao2);
        stage.addActor(botao3);
        stage.addActor(botao4);
        stage.addActor(botao5);
        stage.addActor(botao6);
        stage.addActor(botao7);
        stage.addActor(botao8);
        stage.addActor(botao9);

    }

    @Override
    public void show() {
        // Implement show logic here
    }

    @Override
    public void hide() {
        // Implement hide logic here
    }

    @Override
    public void pause() {
        // Implement pause logic here
    }

    @Override
    public void resume() {
        // Implement resume logic here
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.WHITE);

        // Desenha o fundo
        jogo.batch.begin();
        jogo.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        jogo.batch.end();

        // Atualiza e desenha o Stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        skin.dispose();
    }

}
