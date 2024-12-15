package br.edu.metropolitrans.view.components.mission_modal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.font.FontBase;

public class MissionModalBox {

    private float TEXTO_X;
    private float TEXTO_Y;
    private float LARGURA_MAX;

    private BitmapFont fonte;
    private String texto;
    private float x, y, largura, altura;
    private Texture backgroundTexture;
    private SpriteBatch batch;

    public MissionModalBox(float x, float y, float largura, float altura, MetropoliTrans jogo) {
        this.batch = jogo.batch;
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.texto = "";

        // Carrega a fonte a ser utilizada
        fonte = FontBase.getInstancia().getFonte(25, FontBase.Fontes.PADRAO);

        backgroundTexture = new Texture(Gdx.files.internal("files/backgrounds/background-light.png"));
    }

    public void setTextoMissao(String texto) {
        this.texto = texto;
    }

    public void render() {
        batch.begin();

        batch.draw(
                backgroundTexture,
                x + 490,
                y + 200,
                largura,
                altura);

        // deixar esta mensagem centralizada
        // fonte.draw(batch, "Pressione ENTER para pular diálogo.", x + 20, y + 30,
        // largura - 100, Align.center, true);

        // fonte.setColor(Color.BLACK);
        // TEXTO_X = x + 20;
        // TEXTO_Y = y + altura - 30;
        // LARGURA_MAX = largura - 100;
        // fonte.draw(batch, texto, TEXTO_X, TEXTO_Y, LARGURA_MAX, Align.left, true);

        batch.end();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
