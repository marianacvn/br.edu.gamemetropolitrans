package br.edu.metropolitrans.view.components.phone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.edu.metropolitrans.MetropoliTrans;

public class Phone {
    public Texture telefoneTexture;
    private float x, y;
    private SpriteBatch batch;
    public boolean isVisible;

    public Phone(float x, float y, MetropoliTrans jogo) {
        this.batch = jogo.batch;
        this.x = x;
        this.y = y;
        this.isVisible = false;
        telefoneTexture = new Texture(Gdx.files.internal("files/animation/Phone_2.png"));
    }

    public void render() {
        if (isVisible) {
            batch.begin();
            batch.draw(telefoneTexture, x, y);
            batch.end();
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
