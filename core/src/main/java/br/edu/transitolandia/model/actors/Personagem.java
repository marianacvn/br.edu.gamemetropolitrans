package br.edu.transitolandia.model.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Personagem extends BaseActor {
    

    public Personagem(float x, float y, Stage s) {
        super(x, y, s);
        

        carregaAnimacaoDeSpriteSheet(
                "files/characters/mainCharacter/character-female-32.png",
                4,
                3,
                2.1f,
                false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        //if (isAnimacaoFinalizada())
            //remove();
    }

}
