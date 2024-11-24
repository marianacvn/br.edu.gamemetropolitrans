package br.edu.transitolandia.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import br.edu.transitolandia.Transitolandia;

public class ConfigScreen implements Screen {

    final Transitolandia jogo;
    public Texture background;

    public ConfigScreen( final Transitolandia jogo) {
        this.jogo = jogo;
        // Carrega a textura de fundo
        background = new Texture(Gdx.files.internal("files/backgrounds/background.png"));
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
        
    }

    @Override
    public void resize(int width, int height) {
       
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
        
    }
}
