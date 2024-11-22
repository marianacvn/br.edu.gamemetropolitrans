package br.edu.transitolandia.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import br.edu.transitolandia.Transitolandia;

public class Menu implements Screen {

    final Transitolandia jogo;
    public Texture background;

    public Menu(final Transitolandia game) {
        this.jogo = game;
        // Carrega a textura de fundo
        background = new Texture(Gdx.files.internal("files/backgrounds/city.png"));
    }
    

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.BLACK);

        // Atualiza a câmera do jogo
        //jogo.areaVisualizacao.apply();
        //jogo.batch.setProjectionMatrix(jogo.areaVisualizacao.getCamera().combined);

        // Inicia o batch de desenho
        jogo.batch.begin();
        // Desenha a textura de fundo
        jogo.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // x e y são as coordenadas da tela onde o texto será desenhado,
        // estes valores estão em metros
        // jogo.fonte.draw(jogo.batch, "Bem vindo a Transitolandia!!! ", 600, 700);
        // jogo.fonte.draw(jogo.batch, "Clique em qualquer lugar da tela para iniciar", 600, 720);

        // Finaliza o batch de desenho
        jogo.batch.end();

        // Verifica se o jogador clicou na tela
        if (Gdx.input.isTouched()) {
            jogo.setScreen(new GameScreen(jogo));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        //jogo.areaVisualizacao.update(width, height, true);
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
    }

}
