package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import br.edu.metropolitrans.MetropoliTrans;

import br.edu.metropolitrans.view.components.buttons.TextButtonSecond;
import br.edu.metropolitrans.view.font.FontBase;

public class SaveGameScreen implements Screen {
    public final MetropoliTrans jogo;
    public Stage stage;
    public Skin skin;
    public Label titulo;
    public TextButtonSecond botao1, botao2, botao3, botao4, botao5;
    public TextButtonSecond lixeira1, lixeira2, lixeira3, lixeira4, lixeira5;

    public SaveGameScreen(MetropoliTrans jogo) {
        this.jogo = jogo;

        // Cria o Stage e o Skin
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        skin.add("default", jogo.fonte);

        // Carrega a fonte para o título
        BitmapFont fonteTitulo = FontBase.getInstancia().getFonte(80, FontBase.Fontes.PADRAO);

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonteTitulo;
        labelStyle.fontColor = Color.valueOf("4c4869");
        titulo = new Label("Selecione o Jogo:", labelStyle);

        // Centraliza o título na tela
        float tituloWidth = titulo.getWidth();
        float tituloHeight = titulo.getHeight();
        // no meio do topo da tela
        float tituloX = Gdx.graphics.getWidth() / 2 - tituloWidth / 2;
        float tituloY = Gdx.graphics.getHeight() - tituloHeight - 50;
        titulo.setPosition(tituloX, tituloY);

        // Cria uma tabela para organizar os botões
        Table table = new Table();
        table.setFillParent(true);
       

        // Cria os botões de save e lixeira
        botao1 = new TextButtonSecond("Save 1", "files/buttons/quadrado3.png", skin);
        lixeira1 = new TextButtonSecond("", "files/itens/lixeira3.png", skin);
        botao2 = new TextButtonSecond("Save 2", "files/buttons/quadrado3.png", skin);
        lixeira2 = new TextButtonSecond("", "files/itens/lixeira3.png", skin);
        botao3 = new TextButtonSecond("Save 3", "files/buttons/quadrado3.png", skin);
        lixeira3 = new TextButtonSecond("", "files/itens/lixeira3.png", skin);
        botao4 = new TextButtonSecond("Save 4", "files/buttons/quadrado3.png", skin);
        lixeira4 = new TextButtonSecond("", "files/itens/lixeira3.png", skin);
        botao5 = new TextButtonSecond("Save 5", "files/buttons/quadrado3.png", skin);
        lixeira5 = new TextButtonSecond("", "files/itens/lixeira3.png", skin);

        // Adiciona os botões à tabela
        table.add(botao1).padBottom(10);
        table.add(lixeira1).padLeft(10).padBottom(5).row();
        table.add(botao2).padBottom(10);
        table.add(lixeira2).padLeft(10).padBottom(5).row();
        table.add(botao3).padBottom(10);
        table.add(lixeira3).padLeft(10).padBottom(5).row();
        table.add(botao4).padBottom(10);
        table.add(lixeira4).padLeft(10).padBottom(5).row();
        table.add(botao5).padBottom(10);
        table.add(lixeira5).padLeft(10).padBottom(5).row();

        botao1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.setScreen(new LoadScreen(jogo));
            }
        });

        botao2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.setScreen(new LoadScreen(jogo));
            }
        });

        botao3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.setScreen(new LoadScreen(jogo));
            }
        });

        botao4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.setScreen(new LoadScreen(jogo));
            }
        });

        botao5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.setScreen(new LoadScreen(jogo));
            }
        });

        // Adiciona os listeners para os botões de lixeira
        lixeira1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Lógica para deletar o save 1
            }
        });

        lixeira2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Lógica para deletar o save 2
            }
        });

        lixeira3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Lógica para deletar o save 3
            }
        });

        lixeira4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Lógica para deletar o save 4
            }
        });

        lixeira5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Lógica para deletar o save 5
            }
        });

        stage.addActor(titulo);
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.WHITE);

        // Atualiza e desenha o Stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
