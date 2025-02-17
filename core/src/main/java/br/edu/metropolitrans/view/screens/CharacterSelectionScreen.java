package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.components.buttons.TextButtonBase;
import br.edu.metropolitrans.view.font.FontBase;

public class CharacterSelectionScreen implements Screen {
    private Image characterImage;
    private Label titulo;
    private Texture maleTexture;
    private Texture femaleTexture;
    private Stage stage;
    private Skin skin;
    private String selectedCharacter;
    /**
     * Viewport da tela
     */
    public Viewport viewport;

    public CharacterSelectionScreen(MetropoliTrans jogo) {

        // Cria o Stage e o Skin
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        skin.add("default", jogo.fonte);

        BitmapFont fonteTitulo = FontBase.getInstancia().getFonte(80, FontBase.Fontes.PADRAO);

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonteTitulo;
        labelStyle.fontColor = Color.valueOf("4c4869");
        titulo = new Label("Selecione o personagem", labelStyle);

        // Posiciona o título no topo da tela no centro
        float tituloWidth = titulo.getWidth();
        float tituloHeight = titulo.getHeight();
        float tituloX = (Gdx.graphics.getWidth() - tituloWidth) / 2;
        float tituloY = Gdx.graphics.getHeight() - tituloHeight - 50;
        titulo.setPosition(tituloX, tituloY);

        // Carrega a textura do background
        Texture characterBackgroundTexture = new Texture(
                Gdx.files.internal("files/backgrounds/characterBackground.png"));

        // Cria uma Image para o background
        Image characterBackground = new Image(characterBackgroundTexture);

        // Adiciona a Image do background ao Stage
        stage.addActor(characterBackground);

        maleTexture = new Texture(Gdx.files.internal("files/characters/mainCharacter/male.png"));
        femaleTexture = new Texture(Gdx.files.internal("files/characters/mainCharacter/female.png"));
        characterImage = new Image(maleTexture);
        selectedCharacter = "male";

        characterImage.setPosition(Gdx.graphics.getWidth() / 2 - characterImage.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);
        stage.addActor(characterImage);

        // Posiciona a Image do background pra ficar mais pro meio da tela e subir mais um pouco 
        characterBackground.setPosition(Gdx.graphics.getWidth() / 2 - characterBackground.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - characterBackground.getHeight() / 2 + 50);

        // Botão para a seta esquerda
        ImageButton leftArrowButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("files/itens/setaesquerda.png")))));
        leftArrowButton.setPosition(Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2);
        leftArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.efeitoConfirmar.play();
                if (selectedCharacter.equals("female")) {
                    selectedCharacter = "male";
                    characterImage.setDrawable(new TextureRegionDrawable(new TextureRegion(maleTexture)));
                }
            }
        });

        stage.addActor(leftArrowButton);

        // Botão para a seta direita
        ImageButton rightArrowButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("files/itens/seta.png")))));
        rightArrowButton.setPosition(Gdx.graphics.getWidth() / 2 + 150, Gdx.graphics.getHeight() / 2);
        rightArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.efeitoConfirmar.play();
                if (selectedCharacter.equals("male")) {
                    selectedCharacter = "female";
                    characterImage.setDrawable(new TextureRegionDrawable(new TextureRegion(femaleTexture)));
                }
            }
        });
        stage.addActor(rightArrowButton);

        // Botão para continuar
        TextButtonBase continuarButton = new TextButtonBase("Continuar", "files/buttons/botao-dark2.png", skin);
        continuarButton.setSize(160, 60);
        continuarButton.setPosition(Gdx.graphics.getWidth() / 2 - continuarButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 4);

        continuarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedCharacter != null) {
                    jogo.efeitoConfirmar.play();
                    jogo.personagem.atualizarSpritePersonagem(selectedCharacter);
                    jogo.setScreen(new LoadScreen(jogo, null));
                }
            }
        });
        stage.addActor(continuarButton);

        stage.addActor(titulo);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.WHITE);

        // Atualiza o viewport
        viewport.apply();

        // Atualiza e desenha o Stage
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
        stage.dispose();
        skin.dispose();
        maleTexture.dispose();
        femaleTexture.dispose();

    }

}
