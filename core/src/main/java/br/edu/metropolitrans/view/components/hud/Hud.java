package br.edu.metropolitrans.view.components.hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import br.edu.metropolitrans.view.font.FontBase;

public class Hud {

    private BitmapFont font;
    private int xp;
    private int moedas;
    private float x;
    private float y;
    private Texture xpIcon;
    private Texture moedasIcon;
    

    public Hud() {
        this.xp = 0;
        this.moedas = 200;

        // Carregar a fonte
        font = FontBase.getInstancia().getFonte(18, new Color(1,1,1,1), FontBase.Fontes.PADRAO);

        // Carregar as texturas dos ícones
        xpIcon = new Texture(Gdx.files.internal("files/itens/xp.png"));
        moedasIcon = new Texture(Gdx.files.internal("files/itens/moeda.png"));

        // Inicializar a posição da HUD
        updatePosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void updatePosition(float screenWidth, float screenHeight) {
        this.x = screenWidth - 150; // Ajuste conforme necessário
        this.y = screenHeight - 10; // Ajuste conforme necessário
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        // Desenhar o ícone e o valor de XP
        batch.draw(xpIcon, x - 30, y - 40, 150, 20);
         // Desenhar o valor de XP em cima do ícone
        //  font.draw(batch, String.valueOf(xp), x - 20, y - 20);
        // Desenhar o ícone e o valor de Moedas
        batch.draw(moedasIcon, x - 30, y - 60, 120, 25);
         // Desenhar o valor de Moedas em cima do ícone
        font.draw(batch, String.valueOf(moedas), x - 5, y - 43);
        batch.end();
    }

    public void addXp(int amount) {
        xp += amount;
    }

    public void addMoedas(int amount) {
        moedas += amount;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setMoedas(int moedas) {
        this.moedas = moedas;
    }

    public void dispose() {
        font.dispose();
        xpIcon.dispose();
        moedasIcon.dispose();
    }
}
