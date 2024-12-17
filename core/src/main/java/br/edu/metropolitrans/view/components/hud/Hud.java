package br.edu.metropolitrans.view.components.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.font.FontBase;

public class Hud {

    private MetropoliTrans jogo;
    private BitmapFont font;
    
    private float x;
    private float y;
    private Texture xpIcon;
    private Texture moedasIcon;

    public Hud(MetropoliTrans jogo) {
        this.jogo = jogo;

        // Carregar a fonte
        font = FontBase.getInstancia().getFonte(18, new Color(1, 1, 1, 1), FontBase.Fontes.PADRAO);

        // Carregar as texturas dos ícones
        xpIcon = new Texture(Gdx.files.internal("files/itens/xp.png"));
        moedasIcon = new Texture(Gdx.files.internal("files/itens/moeda.png"));
    }

    public void render() {
        jogo.batch.begin();
        // Desenhar o ícone e o valor de XP
        jogo.batch.draw(xpIcon, x + 1170, y + 650, 80, 20);
        // Desenha a linha de XP, com XP 0 é apenas uma bolinha, com XP 100 é uma linha cheia
        // TODO: Implementar a lógica de desenho da linha de XP - Campo personagem.xp
        // Desenhar o ícone e o valor de Moedas
        jogo.batch.draw(moedasIcon, x + 1170, y  + 650 - 30, 20, 20);
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
    }
}
