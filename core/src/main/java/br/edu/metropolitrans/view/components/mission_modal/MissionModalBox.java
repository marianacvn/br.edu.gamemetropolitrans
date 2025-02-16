package br.edu.metropolitrans.view.components.mission_modal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.screens.GameScreen;

public class MissionModalBox {

    private MetropoliTrans jogo;

    private float x, y, largura, altura;
    private Texture backgroundTexture;
    public MissionComponents missionComponents;

    public MissionModalBox(float x, float y, float largura, float altura, MetropoliTrans jogo) {
        this.jogo = jogo;
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;

        backgroundTexture = new Texture(Gdx.files.internal("files/backgrounds/background-light.png"));
    }

    public void render(float delta) {
        jogo.batch.begin();

        jogo.batch.draw(
                backgroundTexture,
                x + (GameScreen.TELA_LARGURA - largura) / 2,
                y + (GameScreen.TELA_ALTURA - altura) / 2,
                largura,
                altura);

        jogo.batch.end();

        if (missionComponents != null) {
            Gdx.input.setInputProcessor(missionComponents.stage);
            missionComponents.stage.act(delta);
            missionComponents.stage.draw();
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
