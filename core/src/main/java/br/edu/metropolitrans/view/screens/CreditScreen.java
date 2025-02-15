package br.edu.metropolitrans.view.screens;

import br.edu.metropolitrans.MetropoliTrans;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import br.edu.metropolitrans.view.font.FontBase;
import br.edu.metropolitrans.view.components.buttons.TextButtonBase;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;

public class CreditScreen implements Screen {

    public final MetropoliTrans jogo;
    public Texture background;
    public Stage stage;
    public Skin skin;
    public Label titulo,titulo2, text1, text2, text3, text4;
    public Screen telaAnterior;
    public TextButtonBase botaoVoltar;

    CreditScreen(final MetropoliTrans jogo, Screen telaAnterior) {
        this.jogo = jogo;
        this.telaAnterior = telaAnterior;

        // Cria o Stage e o Skin
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        skin.add("default", jogo.fonte);

        // Carrega as fontes
        BitmapFont fonteTitulo = FontBase.getInstancia().getFonte(80, FontBase.Fontes.PADRAO);
        BitmapFont fonteCorpo = FontBase.getInstancia().getFonte(29, FontBase.Fontes.PADRAO);

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonteTitulo;
        labelStyle.fontColor = Color.valueOf("4c4869");
        titulo = new Label("Créditos", labelStyle);
        titulo.setPosition(Gdx.graphics.getWidth() / 2 - titulo.getWidth() / 2, Gdx.graphics.getHeight() - 100);

        // cria o titulo 2 
        Label.LabelStyle labelStyle6 = new Label.LabelStyle();
        labelStyle6.font = fonteTitulo;
        labelStyle6.fontColor = Color.valueOf("4c4869");
        titulo2 = new Label("Autores:", labelStyle);
        // abaixo do título com um espaçamento de 5px
        titulo2.setPosition(Gdx.graphics.getWidth() / 2 - titulo2.getWidth() / 2, titulo.getY() - titulo.getHeight() - 5);

        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = fonteCorpo;
        labelStyle.fontColor = Color.valueOf("4c4869");
        text1 = new Label("Mariana de Carvalho Nunes (UFRPE-UAST)", labelStyle);
        // abaixo do título com um espaçamento de 5px
        text1.setPosition(Gdx.graphics.getWidth() / 2 - text1.getWidth() / 2, titulo2.getY() - titulo2.getHeight() - 5);

        Label.LabelStyle labelStyle3 = new Label.LabelStyle();
        labelStyle3.font = fonteCorpo;
        labelStyle.fontColor = Color.valueOf("4c4869");
        text2 = new Label("Prof. Dr. Richarlyson A. D'Emery (UFRPE-UAST)", labelStyle);
        // abaixo do text1
        text2.setPosition(Gdx.graphics.getWidth() / 2 - text2.getWidth() / 2, text1.getY() - text1.getHeight() - 5);

        Label.LabelStyle labelStyle4 = new Label.LabelStyle();
        labelStyle4.font = fonteCorpo;
        labelStyle.fontColor = Color.valueOf("4c4869");
        text3 = new Label("Universidade Federal Rural de Pernambuco", labelStyle);
        // abaixo do text2
        text3.setPosition(Gdx.graphics.getWidth() / 2 - text3.getWidth() / 2, text2.getY() - text2.getHeight() - 5);

        Label.LabelStyle labelStyle5 = new Label.LabelStyle();
        labelStyle5.font = fonteCorpo;
        labelStyle.fontColor = Color.valueOf("4c4869");
        text4 = new Label("Unidade Acadêmica de Serra Talhada - UAST", labelStyle);
        // abaixo do text3
        text4.setPosition(Gdx.graphics.getWidth() / 2 - text4.getWidth() / 2, text3.getY() - text3.getHeight() - 5);

        // Cria um botão para voltar ao menu
        TextButtonBase botaoVoltar = new TextButtonBase("Voltar", "files/buttons/botao-dark2.png", skin);
        botaoVoltar.setSize(100, 50); // Define o tamanho do botão
        botaoVoltar.setPosition(10, Gdx.graphics.getHeight() - botaoVoltar.getHeight() - 10); // Posição no canto //
                                                                                              // superior esquerdo
        botaoVoltar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.efeitoConfirmar.play();
                jogo.setScreen(telaAnterior);
            }
        });
        stage.addActor(botaoVoltar);
        stage.addActor(titulo);
        stage.addActor(titulo2);
        stage.addActor(text1);
        stage.addActor(text2);
        stage.addActor(text3);
        stage.addActor(text4);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.WHITE);

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
