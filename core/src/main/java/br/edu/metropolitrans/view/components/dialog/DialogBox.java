package br.edu.metropolitrans.view.components.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.view.font.FontBase;

public class DialogBox {

    private MetropoliTrans jogo;
    private float TEXTO_X;
    private float TEXTO_Y;
    private float LARGURA_MAX;

    private ShapeRenderer renderizadorForma;
    private BitmapFont fonte;
    private String texto;
    private float x, y, largura, altura;
    private Texture backgroundTexture;
    private Texture npcTexture;
    private boolean npcImagemComFundo;
    public Npc npc;

    public DialogBox(float x, float y, float largura, float altura, MetropoliTrans jogo) {
        this.jogo = jogo;
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

    private void defineTexturaNpc() {
        try {
            if (npcImagemComFundo) {
                npcTexture = new Texture(Gdx.files
                        .internal("files/characters/" + npc.nome + "/portrait-" + npc.nome + "-256.png"));
            } else {
                npcTexture = new Texture(Gdx.files.internal(
                        "files/characters/" + npc.nome + "/portrait-" + npc.nome + "-256-semfundo.png"));
            }
        } catch (Exception e) {
            npcTexture = null;
        }
    }

    public void setNpcImagemComFundo(boolean npcImagemComFundo) {
        this.npcImagemComFundo = npcImagemComFundo;
    }

    public void render() {
        // Recupera o NPC e o texto a ser exibido do controlador
        npc = jogo.controller.npcDialogoAtual;
        texto = jogo.controller.textoDialogoAtual;
        defineTexturaNpc();

        if (npc == null) {
            return;
        }

        jogo.batch.begin();

        if (backgroundTexture != null) {
            jogo.batch.draw(backgroundTexture, x, y, largura, altura);
        } else {
            renderizadorForma.begin(ShapeRenderer.ShapeType.Filled);
            renderizadorForma.setColor(Color.BLACK);
            renderizadorForma.rect(x, y, largura, altura);
            renderizadorForma.end();
        }

        if (npcTexture != null) {
            jogo.batch.draw(npcTexture, x + 1170, y + 10, 100, 100); // Ajuste a posição e tamanho                                                   
        }

        // deixar esta mensagem centralizada
        fonte.draw(jogo.batch, "Pressione ENTER para continuar diálogo.", x + 20, y + 30, largura - 100, Align.center, true);

        fonte.setColor(Color.BLACK);
        TEXTO_X = x + 20;
        TEXTO_Y = y + altura - 30;
        LARGURA_MAX = largura - 100;
        fonte.draw(jogo.batch, texto, TEXTO_X, TEXTO_Y, LARGURA_MAX, Align.left, true);

        jogo.batch.end();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
