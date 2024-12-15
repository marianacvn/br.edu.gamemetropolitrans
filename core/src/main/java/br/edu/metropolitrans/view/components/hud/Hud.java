package br.edu.metropolitrans.view.components.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.font.FontBase;

public class Hud {

    private SpriteBatch batch;
    private BitmapFont font;
    private int moedas;
    private float x;
    private float y;
    private Texture xpIcon;
    private Texture moedasIcon;

    public Hud(MetropoliTrans jogo) {
        this.batch = jogo.batch;
        this.moedas = 200;

        // Carregar a fonte
        font = FontBase.getInstancia().getFonte(18, new Color(1, 1, 1, 1), FontBase.Fontes.PADRAO);

        // Carregar as texturas dos ícones
        xpIcon = new Texture(Gdx.files.internal("files/itens/xp.png"));
        moedasIcon = new Texture(Gdx.files.internal("files/itens/moeda.png"));
    }

    public void render() {
        batch.begin();
        // Desenhar o ícone e o valor de XP
        batch.draw(xpIcon, x + 1170, y + 650, 80, 20);
        // Desenhar o ícone e o valor de Moedas
        batch.draw(moedasIcon, x + 1170, y  + 650 - 30, 20, 20);
        // Desenhar o valor de Moedas em cima do ícone
        font.draw(batch, String.valueOf(moedas), x + 1170 + 25, y + 650 - 15);
        batch.end();
    }

    public void addMoedas(int amount) {
        moedas += amount;
    }

    public void setMoedas(int moedas) {
        this.moedas = moedas;
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
