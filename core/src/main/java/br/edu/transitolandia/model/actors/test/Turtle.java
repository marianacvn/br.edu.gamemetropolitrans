package br.edu.transitolandia.model.actors.test;

import com.badlogic.gdx.scenes.scene2d.Stage;

import br.edu.transitolandia.model.actors.BaseActor;

public class Turtle extends BaseActor {
    public Turtle(float x, float y, Stage s) {
        super(x, y, s);
        String[] filenames = { "assets/test/turtle-1.png",
                "assets/test/turtle-2.png",
                "assets/test/turtle-3.png",
                "assets/test/turtle-4.png",
                "assets/test/turtle-5.png",
                "assets/test/turtle-6.png" };
        carregaAnimacaoDeArquivos(filenames, 0.1f,
                true);
    }
}