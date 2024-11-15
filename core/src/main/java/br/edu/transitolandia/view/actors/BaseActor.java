package br.edu.transitolandia.view.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Classe base para os personagens do jogo
 */
public class BaseActor extends Actor {

    private Animation<TextureRegion> animacao;
    private float tempoAnimacao;
    private boolean animacaoPausada;

    public BaseActor(float x, float y, Stage s) {
        super();
        setPosition(x, y);

        animacao = null;
        tempoAnimacao = 0;
        animacaoPausada = false;

        s.addActor(this);
    }

    /**
     * Define a animação do ator
     * 
     * @param anim A animação a ser definida
     */
    public void setAnimacao(Animation<TextureRegion> anim) {
        if (anim != null) {
            animacao = anim;
            TextureRegion tr = animacao.getKeyFrame(0);
            float w = tr.getRegionWidth();
            float h = tr.getRegionHeight();
            setSize(w, h);
            setOrigin(w / 2, h / 2);
        }
    }

    /**
     * Define se a animação está pausada
     * 
     * @param pausa Se a animação está pausada
     */
    public void setAnimacaoPausada(boolean pausa) {
        animacaoPausada = pausa;
    }

    /**
     * Verifica se a animação foi finalizada
     */
    public boolean isAnimacaoFinalizada() {
        return animacao.isAnimationFinished(tempoAnimacao);
    }

    /**
     * Carrega uma animação a partir de um array de arquivos de imagem
     * 
     * @param nomesArquivos Array de strings com os nomes dos arquivos de imagem
     * @param duracaoQuadro Duração de cada quadro da animação
     * @param loop          Modo de execução da animação. se true LOOP se não NORMAL
     * @return A animação carregada
     */
    public Animation<TextureRegion> carregaAnimacaoDeArquivos(String[] nomesArquivos, float duracaoQuadro,
            boolean loop) {
        // Define quantos arquivos foram passados e cria um array de quadros
        int qtdArquivos = nomesArquivos.length;
        Array<TextureRegion> quadros = new Array<TextureRegion>();

        // Carrega cada arquivo de imagem e adiciona ao array de quadros
        for (int i = 0; i < qtdArquivos; i++) {
            String nomeArquivo = nomesArquivos[i];
            Texture textura = new Texture(Gdx.files.internal(nomeArquivo));
            // Aplica filtro de textura para suavizar a imagem
            textura.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            quadros.add(new TextureRegion(textura));
        }

        // Cria a animação com os quadros carregados e define o modo de execução
        Animation<TextureRegion> anim = new Animation<TextureRegion>(duracaoQuadro, quadros);
        anim.setPlayMode(loop ? PlayMode.LOOP : PlayMode.NORMAL);

        if (animacao == null)
            setAnimacao(anim);

        return anim;
    }

    /**
     * Carrega uma animação a partir de um arquivo de sprite sheet
     * 
     * @param nomeArquivo   Nome do arquivo de sprite sheet
     * @param linhas        Número de linhas da sprite sheet
     * @param colunas       Número de colunas da sprite sheet
     * @param duracaoQuadro Duração de cada quadro da animação
     * @param loop          Modo de execução da animação. se true LOOP se não NORMAL
     * @return A animação carregada
     */
    public Animation<TextureRegion> carregaAnimacaoDeSpriteSheet(String nomeArquivo, int linhas, int colunas,
            float duracaoQuadro, boolean loop) {
        // Carrega a textura da sprite sheet
        Texture textura = new Texture(Gdx.files.internal(nomeArquivo), true);
        textura.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Divide a textura em quadros
        int larguraQuadro = textura.getWidth() / colunas;
        int alturaQuadro = textura.getHeight() / linhas;

        // Cria uma matriz de quadros
        TextureRegion[][] quadros = TextureRegion.split(textura, larguraQuadro, alturaQuadro);

        // Cria um array de quadros
        int qtdQuadros = linhas * colunas;
        Array<TextureRegion> quadrosArray = new Array<TextureRegion>(qtdQuadros);

        // Adiciona cada quadro ao array de quadros, percorrendo a matriz de quadros
        // l = linha, c = coluna
        for (int l = 0; l < linhas; l++) {
            for (int c = 0; c < colunas; c++) {
                quadrosArray.add(quadros[l][c]);
            }
        }

        // Cria a animação com os quadros carregados e define o modo de execução
        Animation<TextureRegion> anim = new Animation<TextureRegion>(duracaoQuadro, quadrosArray);
        anim.setPlayMode(loop ? PlayMode.LOOP : PlayMode.NORMAL);

        if (animacao == null)
            setAnimacao(anim);

        return anim;
    }

    /**
     * Carrega uma textura estática
     * 
     * @param nomeArquivo Nome do arquivo de imagem
     * @return A animação carregada
     */
    public Animation<TextureRegion> carregaTexturaEstatica(String nomeArquivo) {
        String[] nomeArquivos = { nomeArquivo };
        return carregaAnimacaoDeArquivos(nomeArquivos, 1, false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!animacaoPausada)
            tempoAnimacao += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // Aplica a cor do efeito
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);

        // Desenha a animação
        if (animacao != null && isVisible()) {
            batch.draw(animacao.getKeyFrame(tempoAnimacao), getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                    getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }

}
