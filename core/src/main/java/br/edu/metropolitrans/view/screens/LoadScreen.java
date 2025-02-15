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
import br.edu.metropolitrans.model.ConfigSave;
import br.edu.metropolitrans.model.connection.SaveManager;
import br.edu.metropolitrans.view.font.FontBase;

public class LoadScreen implements Screen {
    public final MetropoliTrans jogo;
    public final ConfigSave save;
    public Stage stage;
    public Skin skin;
    public Label titulo;
    public Texture background;

    public LoadScreen(MetropoliTrans jogo, ConfigSave save) {
        this.jogo = jogo;
        this.save = save;

        // Carrega a textura de fundo
        background = new Texture(Gdx.files.internal("files/backgrounds/background-principal-2.png"));

        // Cria o Stage e o Skin
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        skin.add("default", jogo.fonte);

        BitmapFont fonteTitulo = FontBase.getInstancia().getFonte(80, FontBase.Fontes.PADRAO);

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonteTitulo;
        // labelStyle.fontColor = Color.valueOf("36334c");
        labelStyle.fontColor = Color.WHITE;
        titulo = new Label("Carregando...", labelStyle);

        // Centraliza o título na tela
        float tituloWidth = titulo.getWidth();
        float tituloHeight = titulo.getHeight();
        float tituloX = (Gdx.graphics.getWidth() - tituloWidth) / 2;
        float tituloY = (Gdx.graphics.getHeight() - tituloHeight) / 2;
        titulo.setPosition(tituloX, tituloY);

        stage.addActor(titulo);

        // Verifica se existe um save
        // DESATIVADO - Não é mais necessário
        // if (save == null) {
        // SaveManager.criarNovoSave(SaveManager.verificaQualProximoSaveDis());
        // } else if (save != null && save.getName() != null) {
        // SaveManager.definirSaveAtual(save.getId());
        // } else {
        // SaveManager.criarNovoSave(save.getId());
        // }

        SaveManager.criarNovoSave(1);
    }

    @Override
    public void show() {
        // Inicia uma nova thread para carregar o mapa
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Carrega o mapa (simulação de carregamento)
                try {
                    Thread.sleep(1000); // Simula o tempo de carregamento do mapa
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Após o carregamento, muda para a tela do jogo
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (jogo.telas.get("game") == null) {
                            jogo.telas.put("game", new GameScreen(jogo));
                            Gdx.app.log("LoadScreen", "GameScreen criada");
                        }

                        jogo.controller.inicializar();

                        jogo.setScreen(jogo.telas.get("game"));
                    }
                });
            }
        }).start();
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.BLACK);

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
