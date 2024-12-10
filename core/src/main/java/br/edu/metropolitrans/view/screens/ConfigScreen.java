package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.components.buttons.ButtonBase;
import br.edu.metropolitrans.view.components.buttons.TextButtonBase;
import br.edu.metropolitrans.view.font.FontBase;


public class ConfigScreen implements Screen {

    public final MetropoliTrans jogo;
    public Stage stage;
    public Skin skin;

    public Label titulo, volumeLabel, botaoAlabel, botaoDlabel, botaoWlabel, botaoSlabel, botaoUpLabel, botaoDownLabel,
            botaoLeftLabel, botaoRightLabel, botaoSpaceLabel, botaoEscLabel;
    public Slider sliderVolume;
    public Screen telaAnterior;
    public ButtonBase botaoA, botaoD, botaoW, botaoS, botaoUp, botaoDown, botaoLeft, botaoRight, botaoSpace, botaoEsc;

    public ConfigScreen(final MetropoliTrans jogo, Screen telaAnterior) {
        this.jogo = jogo;
        this.telaAnterior = telaAnterior;

        botaoA = new ButtonBase("files/buttons/botao-a.png", botaoAlabel);
        botaoD = new ButtonBase("files/buttons/botao-d.png", botaoDlabel);
        botaoS = new ButtonBase("files/buttons/botao-s.png", botaoSlabel);
        botaoW = new ButtonBase("files/buttons/botao-w.png", botaoWlabel);
        botaoUp = new ButtonBase("files/buttons/botao-up.png", botaoUpLabel);
        botaoDown = new ButtonBase("files/buttons/botao-down.png", botaoDownLabel);
        botaoLeft = new ButtonBase("files/buttons/botao-left.png", botaoLeftLabel);
        botaoRight = new ButtonBase("files/buttons/botao-right.png", botaoRightLabel);
        botaoSpace = new ButtonBase("files/buttons/botao-space.png", botaoSpaceLabel);
        botaoEsc = new ButtonBase("files/buttons/botao-esc.png", botaoEscLabel);

        // Carrega a textura de fundo e outras
        Texture background = new Texture(Gdx.files.internal("files/backgrounds/background-light.png"));

        // Cria o Stage e o Skin
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        skin.add("default", jogo.fonte);

        // Carrega a imagem do background
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(background));

        // Carrega as fontes
        BitmapFont fonteTitulo = FontBase.getInstancia().getFonte(80, FontBase.Fontes.PADRAO);
        BitmapFont fonteCorpo = FontBase.getInstancia().getFonte(29, FontBase.Fontes.PADRAO);

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonteTitulo;
        labelStyle.fontColor = Color.valueOf("4c4869");
        titulo = new Label("Configurações", labelStyle);
        titulo.setPosition(Gdx.graphics.getWidth() / 2 - titulo.getWidth() / 2, Gdx.graphics.getHeight() - 100);

        // Adiciona um Drawable para o Slider
        Texture sliderBackgroundTexture = new Texture(Gdx.files.internal("files/backgrounds/background-slider.png"));
        Texture sliderKnobTexture = new Texture(Gdx.files.internal("files/backgrounds/background-slider-knob.png"));
        Drawable sliderBackgroundDrawable = new TextureRegionDrawable(new TextureRegion(sliderBackgroundTexture));
        Drawable sliderKnobDrawable = new TextureRegionDrawable(new TextureRegion(sliderKnobTexture));
        skin.add("sliderBackground", sliderBackgroundDrawable);
        skin.add("sliderKnob", sliderKnobDrawable);

        // Cria um botão para voltar ao menu
        TextButtonBase botaoVoltar = new TextButtonBase("Voltar", "files/buttons/botao-dark2.png", skin);
        botaoVoltar.setSize(100, 50); // Define o tamanho do botão
        botaoVoltar.setPosition(10, Gdx.graphics.getHeight() - botaoVoltar.getHeight() - 10); // Posição no canto //
                                                                                              // superior esquerdo
        botaoVoltar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.setScreen(telaAnterior);
            }
        });

        // Cria o Slider de Volume
        SliderStyle sliderStyle = new SliderStyle();
        sliderStyle.background = sliderBackgroundDrawable; // Use diretamente o Drawable
        sliderStyle.knob = sliderKnobDrawable; // Use diretamente o Drawable
        sliderVolume = new Slider(0, 1, 0.1f, false, sliderStyle);
        sliderVolume.setValue(jogo.MusicaMenu.getVolume());
        sliderVolume.setPosition(Gdx.graphics.getWidth() / 2 - 100, titulo.getY() - 60);

        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = fonteCorpo;
        labelStyle2.fontColor = Color.valueOf("4c4869");
        volumeLabel = new Label("Volume: " + (int) (sliderVolume.getValue() * 100) + "%", labelStyle2);
        volumeLabel.setPosition(Gdx.graphics.getWidth() / 2 - 100, titulo.getY() - 30);

        sliderVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                jogo.MusicaMenu.setVolume(sliderVolume.getValue());
                volumeLabel.setText("Volume: " + (int) (sliderVolume.getValue() * 100) + "%");
            }
        });

        // Posiciona os botões de controle
        botaoA.setPosition(sliderVolume.getX(), sliderVolume.getY() - 50);
        botaoD.setPosition(sliderVolume.getX(), botaoA.getY() - botaoA.getHeight() - 10);
        botaoW.setPosition(sliderVolume.getX(), botaoD.getY() - botaoD.getHeight() - 10);
        botaoS.setPosition(sliderVolume.getX(), botaoW.getY() - botaoW.getHeight() - 10);
        botaoUp.setPosition(sliderVolume.getX(), botaoS.getY() - botaoS.getHeight() - 10);
        botaoDown.setPosition(sliderVolume.getX(), botaoUp.getY() - botaoUp.getHeight() - 10);
        botaoLeft.setPosition(sliderVolume.getX(), botaoDown.getY() - botaoDown.getHeight() - 10);
        botaoRight.setPosition(sliderVolume.getX(), botaoLeft.getY() - botaoLeft.getHeight() - 10);
        botaoSpace.setPosition(sliderVolume.getX(), botaoRight.getY() - botaoRight.getHeight() - 10);
        botaoEsc.setPosition(sliderVolume.getX(), botaoSpace.getY() - botaoSpace.getHeight() - 10);

        // Cria os labels para os botões de controle
        botaoAlabel = new Label("Andar para a esquerda", labelStyle2);
        botaoAlabel.setPosition(sliderVolume.getX() + 70, sliderVolume.getY() - 60);
        botaoDlabel = new Label("Andar para a direita", labelStyle2);
        botaoDlabel.setPosition(sliderVolume.getX() + 70, botaoAlabel.getY() - 29);
        botaoWlabel = new Label("Andar para cima", labelStyle2);
        botaoWlabel.setPosition(sliderVolume.getX() + 70, botaoDlabel.getY() - 28);
        botaoSlabel = new Label("Andar para baixo", labelStyle2);
        botaoSlabel.setPosition(sliderVolume.getX() + 70, botaoWlabel.getY() - 27);
        botaoUpLabel = new Label("Andar para cima", labelStyle2);
        botaoUpLabel.setPosition(sliderVolume.getX() + 70, botaoSlabel.getY() - 26);
        botaoDownLabel = new Label("Andar para baixo", labelStyle2);
        botaoDownLabel.setPosition(sliderVolume.getX() + 70, botaoUpLabel.getY() - 25);
        botaoLeftLabel = new Label("Andar para a esquerda", labelStyle2);
        botaoLeftLabel.setPosition(sliderVolume.getX() + 70, botaoDownLabel.getY() - 24);
        botaoRightLabel = new Label("Andar para a direita", labelStyle2);
        botaoRightLabel.setPosition(sliderVolume.getX() + 70, botaoLeftLabel.getY() - 23);
        botaoSpaceLabel = new Label("Interagir", labelStyle2);
        botaoSpaceLabel.setPosition(sliderVolume.getX() + 70, botaoRightLabel.getY() - 30);
        botaoEscLabel = new Label("Configurações", labelStyle2);
        botaoEscLabel.setPosition(sliderVolume.getX() + 70, botaoSpaceLabel.getY() - 28);

        // Cria o background e seta a posição
        ImageButton.ImageButtonStyle backgroundStyle = new ImageButton.ImageButtonStyle();
        backgroundStyle.imageUp = backgroundDrawable;
        ImageButton backgroundButton = new ImageButton(backgroundStyle);
        backgroundButton.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundButton.setPosition(0, 0);

        // Adiciona o título ao Stage
        stage.addActor(backgroundButton);
        stage.addActor(botaoVoltar);
        stage.addActor(titulo);
        stage.addActor(volumeLabel);
        stage.addActor(sliderVolume);
        stage.addActor(botaoA);
        stage.addActor(botaoD);
        stage.addActor(botaoW);
        stage.addActor(botaoS);
        stage.addActor(botaoUp);
        stage.addActor(botaoDown);
        stage.addActor(botaoLeft);
        stage.addActor(botaoRight);
        stage.addActor(botaoSpace);
        stage.addActor(botaoEsc);
        stage.addActor(botaoAlabel);
        stage.addActor(botaoDlabel);
        stage.addActor(botaoWlabel);
        stage.addActor(botaoSlabel);
        stage.addActor(botaoUpLabel);
        stage.addActor(botaoDownLabel);
        stage.addActor(botaoLeftLabel);
        stage.addActor(botaoRightLabel);
        stage.addActor(botaoSpaceLabel);
        stage.addActor(botaoEscLabel);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.BLACK);

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
