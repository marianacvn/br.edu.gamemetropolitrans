package br.edu.transitolandia.test;

import br.edu.transitolandia.GameBeta;
import br.edu.transitolandia.view.actors.BaseActor;
import br.edu.transitolandia.view.actors.test.Starfish;
import br.edu.transitolandia.view.actors.test.Turtle;

public class StarfishCollector extends GameBeta {

    private Turtle turtle;
    private Starfish starfish;
    private BaseActor ocean;

    @Override
    public void initialize() {
        ocean = new BaseActor(0, 0, mainStage);
        ocean.carregaTexturaEstatica("assets/test/water.jpg");
        ocean.setSize(800, 600);

        starfish = new Starfish(380, 380, mainStage);
        turtle = new Turtle(20, 20, mainStage);
    }

    @Override
    public void update(float dt) {
    }

}
