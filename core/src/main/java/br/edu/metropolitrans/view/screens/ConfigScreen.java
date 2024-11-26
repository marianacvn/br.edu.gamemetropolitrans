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
    public Label titulo;
    public Texture background;
    public Slider sliderVolume;

    public ConfigScreen(final MetropoliTrans jogo) {
        this.jogo = jogo;

        // Carrega a textura de fundo
        background = new Texture(Gdx.files.internal("files/backgrounds/background-light.png"));

        // Cria o Stage e o Skin
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        skin.add("default", jogo.fonte);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("files/fonts/Silver.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80; // Define o tamanho da fonte para o título
        BitmapFont fonteTitulo = generator.generateFont(parameter);
        generator.dispose(); // Libera os recursos do gerador

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
                jogo.setScreen(new MenuScreen(jogo));
            }
        });

        // Cria o Slider de Volume
        SliderStyle sliderStyle = new SliderStyle();
        sliderStyle.background = sliderBackgroundDrawable; // Use diretamente o Drawable
        sliderStyle.knob = sliderKnobDrawable; // Use diretamente o Drawable
        sliderVolume = new Slider(0, 1, 0.1f, false, sliderStyle);
        sliderVolume.setValue(jogo.MusicaMenu.getVolume());
        sliderVolume.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2);

        sliderVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                jogo.MusicaMenu.setVolume(sliderVolume.getValue());
            }
        });

        // Adiciona o título ao Stage
        stage.addActor(titulo);
        stage.addActor(sliderVolume);
        stage.addActor(botaoVoltar);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.BLACK);

        jogo.batch.begin();
        jogo.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        jogo.batch.end();

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
        background.dispose();
        stage.dispose();
        skin.dispose();
    }
}
