package br.edu.metropolitrans.view.components.mission_modal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.font.FontBase;

public class MissionModalBox {

    private MetropoliTrans jogo;

    private BitmapFont fonte;
    private String texto;
    private float x, y, largura, altura;
    private Texture backgroundTexture;
    public MissionComponents missionComponents;

    public MissionModalBox(float x, float y, float largura, float altura, MetropoliTrans jogo) {
        this.jogo = jogo;
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.texto = "";

        // Carrega a fonte a ser utilizada
        fonte = FontBase.getInstancia().getFonte(30, FontBase.Fontes.PADRAO);

        backgroundTexture = new Texture(Gdx.files.internal("files/backgrounds/background-light.png"));
    }

    public void setTextoMissao(String texto) {
        this.texto = texto;
    }

    public void render(float delta) {
        jogo.batch.begin();

        jogo.batch.draw(
                backgroundTexture,
                x + 490,
                y + 200,
                largura,
                altura);

        jogo.batch.end();

        if (missionComponents != null) {
            missionComponents.stage.act(delta);
            missionComponents.stage.draw();
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        if (missionComponents != null) {
            missionComponents.setBasePosition(x, y);
        }
    }
}
