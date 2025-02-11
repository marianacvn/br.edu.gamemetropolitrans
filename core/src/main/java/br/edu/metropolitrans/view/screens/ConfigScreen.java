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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.ConfigData;
import br.edu.metropolitrans.model.dao.ConfigDAO;
import br.edu.metropolitrans.view.components.buttons.ButtonBase;
import br.edu.metropolitrans.view.components.buttons.TextButtonBase;
import br.edu.metropolitrans.view.font.FontBase;

public class ConfigScreen implements Screen {

    public final MetropoliTrans jogo;
    public Stage stage;
    public Skin skin;
    public Label titulo, titulo2, volumeLabel, botaoAlabel, botaoDlabel, botaoWlabel, botaoSlabel, botaoSpaceLabel,
            botaoEscLabel, botaoEnterLabel, botaoTLabel;
    public Slider sliderVolume;
    public Screen telaAnterior;
    public ButtonBase botaoA, botaoD, botaoW, botaoS, botaoSpace, botaoEsc, botaoEnter, botaoT;

    public ConfigScreen(final MetropoliTrans jogo, Screen telaAnterior) {
        this.jogo = jogo;
        this.telaAnterior = telaAnterior;

        botaoA = new ButtonBase("files/buttons/botao-a.png", botaoAlabel);
        botaoD = new ButtonBase("files/buttons/botao-d.png", botaoDlabel);
        botaoS = new ButtonBase("files/buttons/botao-s.png", botaoSlabel);
        botaoW = new ButtonBase("files/buttons/botao-w.png", botaoWlabel);
        botaoEnter = new ButtonBase("files/buttons/botao-enter.png", botaoEnterLabel);
        botaoT = new ButtonBase("files/buttons/botao-t.png", botaoTLabel);
        botaoSpace = new ButtonBase("files/buttons/botao-space.png", botaoSpaceLabel);
        botaoEsc = new ButtonBase("files/buttons/botao-esc.png", botaoEscLabel);

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
        titulo = new Label("Configurações", labelStyle);
        titulo.setPosition(Gdx.graphics.getWidth() / 2 - titulo.getWidth() / 2, Gdx.graphics.getHeight() - 100);

        titulo2 = new Label("Controles", labelStyle);
        titulo2.setPosition(Gdx.graphics.getWidth() / 2 - titulo2.getWidth() / 2, titulo.getY() - 200);

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
        sliderVolume.setValue(jogo.musicaMenu.getVolume());
        sliderVolume.setPosition(Gdx.graphics.getWidth() / 2 - 100, titulo.getY() - 60);

        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = fonteCorpo;
        labelStyle2.fontColor = Color.valueOf("4c4869");
        volumeLabel = new Label("Volume: " + (int) (sliderVolume.getValue() * 100) + "%", labelStyle2);
        volumeLabel.setPosition(Gdx.graphics.getWidth() / 2 - 100, titulo.getY() - 30);

        sliderVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                jogo.musicaMenu.setVolume(sliderVolume.getValue());

                ConfigData config = ConfigDAO.carregarConfig();
                config.setVolume(sliderVolume.getValue());
                ConfigDAO.salvarConfig(config);

                volumeLabel.setText("Volume: " + (int) (sliderVolume.getValue() * 100) + "%");
            }
        });

        // Cria os labels para os botões de controle
        botaoAlabel = new Label("Andar para a esquerda", labelStyle2);
        // botaoAlabel.setPosition(sliderVolume.getX() + 70, sliderVolume.getY() - 60);
        botaoDlabel = new Label("Andar para a direita", labelStyle2);
        // botaoDlabel.setPosition(sliderVolume.getX() + 70, botaoAlabel.getY() - 29);
        botaoWlabel = new Label("Andar para cima", labelStyle2);
        // botaoWlabel.setPosition(sliderVolume.getX() + 70, botaoDlabel.getY() - 28);
        botaoSlabel = new Label("Andar para baixo", labelStyle2);
        // botaoSlabel.setPosition(sliderVolume.getX() + 70, botaoWlabel.getY() - 27);
        botaoEnterLabel = new Label("Pular Diálogos", labelStyle2);
        // botaoEnterLabel.setPosition(sliderVolume.getX() + 70, botaoSlabel.getY() -
        // 24);
        botaoTLabel = new Label("Acessa o telefone", labelStyle2);
        // botaoTLabel.setPosition(sliderVolume.getX() + 70, botaoEnterLabel.getY() -
        // 25);
        botaoSpaceLabel = new Label("Interagir", labelStyle2);
        // botaoSpaceLabel.setPosition(sliderVolume.getX() + 70, botaoTLabel.getY() -
        // 30);
        botaoEscLabel = new Label("Configurações", labelStyle2);
        // botaoEscLabel.setPosition(sliderVolume.getX() + 70, botaoSpaceLabel.getY() -
        // 28);

        // Cria uma tabela para organizar os botões e labels
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(titulo2.getY() - 130);

        // Adiciona os botões e labels à tabela
        table.add(botaoA).padBottom(5).left();
        table.add(botaoAlabel).padBottom(5).left().row();
        table.add(botaoD).padBottom(5).left();
        table.add(botaoDlabel).padBottom(5).left().row();
        table.add(botaoW).padBottom(5).left();
        table.add(botaoWlabel).padBottom(5).left().row();
        table.add(botaoS).padBottom(5).left();
        table.add(botaoSlabel).padBottom(5).left().row();
        table.add(botaoT).padBottom(5).left();
        table.add(botaoTLabel).padBottom(5).left().row();
        table.add(botaoEnter).padBottom(5).left();
        table.add(botaoEnterLabel).padBottom(5).left().row();
        table.add(botaoSpace).padBottom(5).left();
        table.add(botaoSpaceLabel).padLeft(10).padBottom(10).left().row(); // Adiciona padding à esquerda para espaçar o
                                                                           // label do botão
        table.add(botaoEsc).padBottom(5).left();
        table.add(botaoEscLabel).padBottom(5).left().row();

        // Adiciona o título ao Stage
        stage.addActor(botaoVoltar);
        stage.addActor(titulo);
        stage.addActor(volumeLabel);
        stage.addActor(sliderVolume);
        stage.addActor(titulo2);
        stage.addActor(table);

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
