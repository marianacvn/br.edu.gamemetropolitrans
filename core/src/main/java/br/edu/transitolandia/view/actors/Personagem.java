package br.edu.transitolandia.view.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Personagem extends BaseActor {

    public Personagem(float x, float y, Stage s) {
        super(x, y, s);

        carregaAnimacaoDeSpriteSheet(
                "files/characters/mainCharacter/character-male_spritesheet.png",
                4,
                11,
                0.1f,
                true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isAnimacaoFinalizada())
            remove();
    }

}
