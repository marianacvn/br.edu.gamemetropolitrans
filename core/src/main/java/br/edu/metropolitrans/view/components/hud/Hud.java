package br.edu.metropolitrans.view.components.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.font.FontBase;

public class Hud {

    private MetropoliTrans jogo;
    private BitmapFont font;

    private float x;
    private float y;
    private Texture xpIcon;
    private Texture moedasIcon;
    private ShapeRenderer shapeRenderer;

    public Hud(MetropoliTrans jogo) {
        this.jogo = jogo;

        // Carregar a fonte
        font = FontBase.getInstancia().getFonte(24, new Color(Color.BLACK), FontBase.Fontes.PADRAO);

        this.shapeRenderer = new ShapeRenderer();
        // Carregar as texturas dos ícones
        xpIcon = new Texture(Gdx.files.internal("files/itens/xp2.png"));
        moedasIcon = new Texture(Gdx.files.internal("files/itens/moeda.png"));
    }

    public void render() {
        jogo.batch.begin();
        // Desenhar o ícone e o valor de XP
        jogo.batch.draw(xpIcon, x + 1170, y + 650, 80, 20);
        jogo.batch.end();
        // Desenha a linha de XP, com XP 0 é apenas uma bolinha, com XP 100 é uma linha
        // cheia
        // TODO: Implementar a lógica de desenho da linha de XP - Campo personagem.xp

        // Salvar o estado da matriz de projeção da câmera
        shapeRenderer.setProjectionMatrix(jogo.batch.getProjectionMatrix());

        // Desenhar a linha de XP
        float xpPercent = jogo.personagem.xp / 100.0f; // Supondo que o XP máximo é 100
        float xpBarWidth = 100 * xpPercent; // Largura da barra de XP

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);

        if (xpPercent == 0) {
            // Desenha um quadrado quando o XP é 0
            shapeRenderer.rect(x + 1170 + 4, y + 655, 10, 10);
        } else {
            // Desenha a barra de XP quando o XP é maior que 0
            shapeRenderer.rect(x + 1170 + 4, y + 655, xpBarWidth, 10);
        }

        shapeRenderer.end();

        jogo.batch.begin();
        // Desenhar o ícone e o valor de Moedas
        jogo.batch.draw(moedasIcon, x + 1170, y + 650 - 30, 20, 20);
        // Desenhar o valor de Moedas em cima do ícone
        font.draw(jogo.batch, String.valueOf(jogo.personagem.moedas), x + 1170 + 25, y + 650 - 15);
        jogo.batch.end();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void dispose() {
        font.dispose();
        xpIcon.dispose();
        moedasIcon.dispose();
        shapeRenderer.dispose();
    }
}
