package br.edu.metropolitrans.view.components.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

import br.edu.metropolitrans.MetropoliTrans;

public class DialogBox {

    private float TEXTO_X;
    private float TEXTO_Y;
    private float LARGURA_MAX;

    private MetropoliTrans jogo;
    private ShapeRenderer renderizadorForma;
    private BitmapFont fonte;
    private String texto;
    private float x, y, largura, altura;
    private Texture backgroundTexture;
    private Texture npcTexture;
    private SpriteBatch batch;
    private boolean npcImagemComFundo;

    public DialogBox(float x, float y, float largura, float altura, MetropoliTrans jogo) {
        this.jogo = jogo;
        this.batch = jogo.batch;
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.renderizadorForma = new ShapeRenderer();
        this.texto = "";

        // Define a fonte a ser utilizada
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("files/fonts/Silver.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25; // Define o tamanho da fonte
        fonte = generator.generateFont(parameter);
        // Libera os recursos do gerador de fontes
        generator.dispose();

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
                                                                // conforme necessário
        }

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