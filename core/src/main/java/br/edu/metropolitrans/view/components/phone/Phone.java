package br.edu.metropolitrans.view.components.phone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import br.edu.metropolitrans.MetropoliTrans;

public class Phone {
    private MetropoliTrans jogo;
    public Texture telefoneTexture;
    private float x, y;

    public Phone(float x, float y, MetropoliTrans jogo) {
        this.jogo = jogo;
        this.x = x;
        this.y = y;
        telefoneTexture = new Texture(Gdx.files.internal("files/animation/Phone_2.png"));
    }

    public void render() {
        if (jogo.controller.phoneIsVisible) {
            jogo.batch.begin();
            jogo.batch.draw(telefoneTexture, x, y);
            jogo.batch.end();
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
