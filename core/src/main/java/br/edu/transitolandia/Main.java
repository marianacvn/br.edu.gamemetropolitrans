package br.edu.transitolandia;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import br.edu.transitolandia.view.Menu;

public class Main extends Game {
    private SpriteBatch batch;
    

    @Override
    public void create() {
        batch = new SpriteBatch();
    

        this.setScreen(new Menu(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        
    }
}
