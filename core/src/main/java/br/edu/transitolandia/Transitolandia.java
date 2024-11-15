package br.edu.transitolandia;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import br.edu.transitolandia.view.Menu;
import br.edu.transitolandia.view.actors.Personagem;

public class Transitolandia extends Game {

    public String DIRETORIO_BASE_ARQUIVOS = "files/";

    public SpriteBatch batch;
    public BitmapFont fonte;
    public FitViewport areaVisualizacao;
    public Stage mainStage;
    public Personagem personagem;

    @Override
    public void create() {
        mainStage = new Stage();
        batch = new SpriteBatch();
        // Fonte padrão do libgdx
        fonte = new BitmapFont();
        areaVisualizacao = new FitViewport(8, 5);

        // Por padrão a fonte tem 15pt, mas nós precisamos realizar uma escala
        // para o nosso viewport pela razão da altura do viewport para a altura da tela
        fonte.setUseIntegerPositions(false);
        fonte.getData().setScale(areaVisualizacao.getWorldHeight() / Gdx.graphics.getHeight());

        this.setScreen(new Menu(this));
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
