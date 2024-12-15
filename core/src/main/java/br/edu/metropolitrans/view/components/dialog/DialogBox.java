package br.edu.metropolitrans.view.components.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.font.FontBase;

public class DialogBox {

    private float TEXTO_X;
    private float TEXTO_Y;
    private float LARGURA_MAX;

    private ShapeRenderer renderizadorForma;
    private BitmapFont fonte;
    private String texto;
    private float x, y, largura, altura;
    private Texture backgroundTexture;
    private Texture npcTexture;
    private SpriteBatch batch;
    private boolean npcImagemComFundo;

    public DialogBox(float x, float y, float largura, float altura, MetropoliTrans jogo) {
        this.batch = jogo.batch;
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.renderizadorForma = new ShapeRenderer();
        this.texto = "";

        // Carrega a fonte a ser utilizada
        fonte = FontBase.getInstancia().getFonte(25, FontBase.Fontes.PADRAO);

        backgroundTexture = new Texture(Gdx.files.internal("files/backgrounds/dialog-background.png"));
    }

    public void setTextoDialogo(String texto) {
        this.texto = texto;
    }

    public void setNpcTexture(String nomePersonagem) {
        try {
            if (npcImagemComFundo) {
                npcTexture = new Texture(Gdx.files
                        .internal("files/characters/" + nomePersonagem + "/portrait-" + nomePersonagem + "-256.png"));
            } else {
                npcTexture = new Texture(Gdx.files.internal(
                        "files/characters/" + nomePersonagem + "/portrait-" + nomePersonagem + "-256-semfundo.png"));
            }
        } catch (Exception e) {
            npcTexture = null;
        }
    }

    public void setNpcImagemComFundo(boolean npcImagemComFundo) {
        this.npcImagemComFundo = npcImagemComFundo;
    }

    public void render() {
        batch.begin();

        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, x, y, largura, altura);
        } else {
            renderizadorForma.begin(ShapeRenderer.ShapeType.Filled);
            renderizadorForma.setColor(Color.BLACK);
            renderizadorForma.rect(x, y, largura, altura);
            renderizadorForma.end();
        }

        if (npcTexture != null) {
            batch.draw(npcTexture, x + 1170, y + 10, 100, 100); // Ajuste a posição e tamanho                                                   
        }

        // deixar esta mensagem centralizada
        fonte.draw(batch, "Pressione ENTER para pular diálogo.", x + 20, y + 30, largura - 100, Align.center, true);

        fonte.setColor(Color.BLACK);
        TEXTO_X = x + 20;
        TEXTO_Y = y + altura - 30;
        LARGURA_MAX = largura - 100;
        fonte.draw(batch, texto, TEXTO_X, TEXTO_Y, LARGURA_MAX, Align.left, true);

        batch.end();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
