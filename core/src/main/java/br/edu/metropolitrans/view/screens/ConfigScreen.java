package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import br.edu.metropolitrans.MetropoliTrans;

public class ConfigScreen implements Screen {

    public final MetropoliTrans jogo;
    public Stage stage;
    public Skin skin;
    public Label titulo, volumeLabel, botaoAlabel, botaoDlabel, botaoWlabel, botaoSlabel, botaoUpLabel, botaoDownLabel,
            botaoLeftLabel, botaoRightLabel, botaoSpaceLabel, botaoEscLabel;
    public Slider sliderVolume;
    public Screen telaAnterior;
    public ImageButton botaoA, botaoD, botaoW, botaoS, botaoUp, botaoDown, botaoLeft, botaoRight, botaoSpace, botaoEsc;

    public ConfigScreen(final MetropoliTrans jogo, Screen telaAnterior) {
        this.jogo = jogo;
        this.telaAnterior = telaAnterior;

        // Carrega a textura de fundo e outras
        Texture background = new Texture(Gdx.files.internal("files/backgrounds/background-light.png"));
        Texture ATexture = new Texture(Gdx.files.internal("files/buttons/botao-a.png"));
        Texture DTexture = new Texture(Gdx.files.internal("files/buttons/botao-d.png"));
        Texture STexture = new Texture(Gdx.files.internal("files/buttons/botao-s.png"));
        Texture WTexture = new Texture(Gdx.files.internal("files/buttons/botao-w.png"));
        Texture upTexture = new Texture(Gdx.files.internal("files/buttons/botao-up.png"));
        Texture downTexture = new Texture(Gdx.files.internal("files/buttons/botao-down.png"));
        Texture leftTexture = new Texture(Gdx.files.internal("files/buttons/botao-left.png"));
        Texture rightTexture = new Texture(Gdx.files.internal("files/buttons/botao-right.png"));
        Texture spaceTexture = new Texture(Gdx.files.internal("files/buttons/botao-space.png"));
        Texture escTexture = new Texture(Gdx.files.internal("files/buttons/botao-esc.png"));

        // Cria o Stage e o Skin
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        skin.add("default", jogo.fonte);

        // Carrega as imagens para os botões de controle
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(background));
        Drawable ADrawable = new TextureRegionDrawable(new TextureRegion(ATexture));
        Drawable DDrawable = new TextureRegionDrawable(new TextureRegion(DTexture));
        Drawable SDrawable = new TextureRegionDrawable(new TextureRegion(STexture));
        Drawable WDrawable = new TextureRegionDrawable(new TextureRegion(WTexture));
        Drawable upDrawable = new TextureRegionDrawable(new TextureRegion(upTexture));
        Drawable downDrawable = new TextureRegionDrawable(new TextureRegion(downTexture));
        Drawable leftDrawable = new TextureRegionDrawable(new TextureRegion(leftTexture));
        Drawable rightDrawable = new TextureRegionDrawable(new TextureRegion(rightTexture));
        Drawable spaceDrawable = new TextureRegionDrawable(new TextureRegion(spaceTexture));
        Drawable escDrawable = new TextureRegionDrawable(new TextureRegion(escTexture));

        // fonte título
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("files/fonts/Silver.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80; // Define o tamanho da fonte para o título
        BitmapFont fonteTitulo = generator.generateFont(parameter);
        generator.dispose(); // Libera os recursos do gerador

        // fonte texto corpo
        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("files/fonts/Silver.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 29; // Define o tamanho da fonte para o título
        BitmapFont fonteCorpo = generator2.generateFont(parameter2);
        generator2.dispose(); // Libera os recursos do gerador

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

        Texture botaoTextura = new Texture(Gdx.files.internal("files/buttons/botao-dark2.png"));
        Drawable botaoDrawable = new TextureRegionDrawable(new TextureRegion(botaoTextura));

        // Cria um botão para voltar ao menu
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.WHITE;
        TextButton botaoVoltar = new TextButton("Voltar", textButtonStyle);
        textButtonStyle.up = botaoDrawable;
        botaoVoltar.setPosition(10, Gdx.graphics.getHeight() - botaoVoltar.getHeight() - 10); // Posição no canto
        // superior esquerdo
        botaoVoltar.setSize(100, 50); // Define o tamanho do botão
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

        // Cria os botões de controle
        // botão A
        ImageButton.ImageButtonStyle AButtonStyle = new ImageButton.ImageButtonStyle();
        AButtonStyle.imageUp = ADrawable;
        botaoA = new ImageButton(AButtonStyle);
        botaoA.setPosition(sliderVolume.getX(), sliderVolume.getY() - 50); // Posição no canto superior direito

        // botão D
        ImageButton.ImageButtonStyle DButtonStyle = new ImageButton.ImageButtonStyle();
        DButtonStyle.imageUp = DDrawable;
        botaoD = new ImageButton(DButtonStyle);
        botaoD.setPosition(sliderVolume.getX(), botaoA.getY() - botaoA.getHeight() - 10); // Posição no canto superior
                                                                                          // direito

        // botão W
        ImageButton.ImageButtonStyle WButtonStyle = new ImageButton.ImageButtonStyle();
        WButtonStyle.imageUp = WDrawable;
        botaoW = new ImageButton(WButtonStyle);
        botaoW.setPosition(sliderVolume.getX(), botaoD.getY() - botaoD.getHeight() - 10); // Posição no canto superior
                                                                                          // direito

        // botão S
        ImageButton.ImageButtonStyle SButtonStyle = new ImageButton.ImageButtonStyle();
        SButtonStyle.imageUp = SDrawable;
        botaoS = new ImageButton(SButtonStyle);
        botaoS.setPosition(sliderVolume.getX(), botaoW.getY() - botaoW.getHeight() - 10); // Posição no canto superior
                                                                                          // direito

        // botão Up
        ImageButton.ImageButtonStyle upButtonStyle = new ImageButton.ImageButtonStyle();
        upButtonStyle.imageUp = upDrawable;
        botaoUp = new ImageButton(upButtonStyle);
        botaoUp.setPosition(sliderVolume.getX(), botaoS.getY() - botaoS.getHeight() - 10); // Posição no canto superior
                                                                                           // direito

        // botão Down
        ImageButton.ImageButtonStyle downButtonStyle = new ImageButton.ImageButtonStyle();
        downButtonStyle.imageUp = downDrawable;
        botaoDown = new ImageButton(downButtonStyle);
        botaoDown.setPosition(sliderVolume.getX(), botaoUp.getY() - botaoUp.getHeight() - 10); // Posição no canto
                                                                                               // superior direito

        // botão Left
        ImageButton.ImageButtonStyle leftButtonStyle = new ImageButton.ImageButtonStyle();
        leftButtonStyle.imageUp = leftDrawable;
        botaoLeft = new ImageButton(leftButtonStyle);
        botaoLeft.setPosition(sliderVolume.getX(), botaoDown.getY() - botaoDown.getHeight() - 10); // Posição no canto
                                                                                                   // superior direito

        // botão Right
        ImageButton.ImageButtonStyle rightButtonStyle = new ImageButton.ImageButtonStyle();
        rightButtonStyle.imageUp = rightDrawable;
        botaoRight = new ImageButton(rightButtonStyle);
        botaoRight.setPosition(sliderVolume.getX(), botaoLeft.getY() - botaoLeft.getHeight() - 10); // Posição no canto
                                                                                                    // superior direito

        // botão Space
        ImageButton.ImageButtonStyle spaceButtonStyle = new ImageButton.ImageButtonStyle();
        spaceButtonStyle.imageUp = spaceDrawable;
        botaoSpace = new ImageButton(spaceButtonStyle);
        botaoSpace.setPosition(sliderVolume.getX(), botaoRight.getY() - botaoRight.getHeight() - 10); // Posição no
                                                                                                      // canto superior
                                                                                                      // direito

        // botão Esc
        ImageButton.ImageButtonStyle escButtonStyle = new ImageButton.ImageButtonStyle();
        escButtonStyle.imageUp = escDrawable;
        botaoEsc = new ImageButton(escButtonStyle);
        botaoEsc.setPosition(sliderVolume.getX(), botaoSpace.getY() - botaoSpace.getHeight() - 10); // Posição no canto
                                                                                                    // superior direito

        // Adiciona o título ao Stage
        stage.addActor(backgroundButton);
        stage.addActor(titulo);
        stage.addActor(volumeLabel);
        stage.addActor(sliderVolume);
        stage.addActor(botaoVoltar);
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

        //jogo.batch.begin();
        //jogo.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //jogo.batch.end();

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
        //background.dispose();
    }
}
