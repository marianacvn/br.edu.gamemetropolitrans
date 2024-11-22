package br.edu.transitolandia.model.actors.test;

import com.badlogic.gdx.scenes.scene2d.Stage;

import br.edu.transitolandia.model.actors.BaseActor;

public class Whirlpool extends BaseActor {
    public Whirlpool(float x, float y, Stage s) {
        super(x, y, s);
        carregaAnimacaoDeSpriteSheet(
                "assets/test/whirlpool.png",
                2,
                5,
                0.1f,
                false);
    }

    public void act(float dt) {
        super.act(dt);
        if (isAnimacaoFinalizada())
            remove();
    }
}
