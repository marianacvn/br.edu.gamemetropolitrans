package br.edu.transitolandia;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;

import br.edu.transitolandia.view.screens.MenuScreen;

public class Transitolandia extends Game {
    public String DIRETORIO_BASE_ARQUIVOS = "files/";
    public Stage estagioPrincipal;
    public SpriteBatch batch;
    public BitmapFont fonte;


    @Override
    public void create() {
        estagioPrincipal = new Stage();
        batch = new SpriteBatch();

         // Carrega a fonte personalizada
         FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("files/fonts/Silver.ttf"));
         FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
         parameter.size = 30; // Define o tamanho da fonte
         fonte = generator.generateFont(parameter);
         // Libera os recursos do gerador de fontes
         generator.dispose();
         

        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        fonte.dispose();

    }
}
