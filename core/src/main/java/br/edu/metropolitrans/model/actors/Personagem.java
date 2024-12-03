package br.edu.metropolitrans.model.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

/**
 * Personagem principal do jogo
 */
public class Personagem extends BaseActor {

    /**
     * Velocidade do personagem
     */
    public static final int VELOCIDADE = 100;

    /**
     * Animação do personagem
     */
    Animation<TextureRegion> norte, sul, leste, oeste;

    /**
     * Ângulo de rotação do personagem
     */
    private float angulo;

    private Array<Rectangle> retangulosColisao;

    public ArrayList<Npc> npcs;

    public Personagem(float x, float y, Stage s) {
        super(x, y, s);

        // Carraga a animação do personagem
        String nomeArquivo = "files/characters/mainCharacter/character-male_spritesheet.png";
        int linhas = 4;
        int colunas = 11;

        // Ajusta as margens para centralizar o personagem na colisão
        margemAltura = -10;
        margemLargura = -15;
        margemX = 15;

        // Carrega a textura
        Texture textura = new Texture(Gdx.files.internal(nomeArquivo), true);

        // Divide a textura em quadros
        int larguraQuadro = textura.getWidth() / colunas;
        int alturaQuadro = textura.getHeight() / linhas;
        float duracaoQuadro = 0.05f;

        // Cria uma matriz de texturas e uma lista de texturas
        TextureRegion[][] quadros = TextureRegion.split(textura, larguraQuadro, alturaQuadro);
        Array<TextureRegion> listaTexturas = new Array<TextureRegion>();

        // Percorre a matriz de texturas e adiciona na lista de texturas para criar a
        // animação de cada direção
        // A animação é criada com a lista de texturas, a duração de cada quadro e o
        // modo de repetição
        // NORTE
        for (int c = 0; c < colunas; c++)
            listaTexturas.add(quadros[0][c]);
        norte = new Animation<TextureRegion>(duracaoQuadro, listaTexturas, Animation.PlayMode.LOOP_PINGPONG);

        // OESTE
        listaTexturas.clear();
        for (int c = 0; c < colunas; c++)
            listaTexturas.add(quadros[1][c]);
        oeste = new Animation<TextureRegion>(duracaoQuadro, listaTexturas, Animation.PlayMode.LOOP_PINGPONG);

        // SUL
        listaTexturas.clear();
        for (int c = 0; c < colunas; c++)
            listaTexturas.add(quadros[2][c]);
        sul = new Animation<TextureRegion>(duracaoQuadro, listaTexturas, Animation.PlayMode.LOOP_PINGPONG);

        // LESTE
        listaTexturas.clear();
        for (int c = 0; c < colunas; c++)
            listaTexturas.add(quadros[3][c]);
        leste = new Animation<TextureRegion>(duracaoQuadro, listaTexturas, Animation.PlayMode.LOOP_PINGPONG);

        // Inicia a animação com a direção SUL
        setAnimacao(sul);
        angulo = 270;

        // Configuracao do personagem
        setAceleracao(800);
        setVelocidadeMaxima(200);
        setDesaceleracao(800);

    }

    public boolean interagiu(ObjetoInterativo objetoInterativo) {
        return this.sobrepoe(objetoInterativo);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Movimenta o personagem
        animacao();

        // Atualiza a posição do personagem
        aplicarFisica(delta);

        // Verifica colisões
        colisao(delta);

        // Limita o personagem ao mundo
        limitaMundo();

        // Alinha a câmera após atualizar a posição do personagem
        alinhamentoCamera();

    }

    private void animacao() {
        // Pausa a animação quando o personagem não está se movendo
        if (getVelocidade() == 0) {
            setAnimacaoPausada(true);
        } else {
            setAnimacaoPausada(false);

            // Define a direção da animação
            float angle = getAnguloMovimento();
            if (angle >= 45 && angle <= 135) {
                angulo = 90;
                setAnimacao(norte);
            } else if (angle > 135 && angle < 225) {
                angulo = 180;
                setAnimacao(oeste);
            } else if (angle >= 225 && angle <= 315) {
                angulo = 270;
                setAnimacao(sul);
            } else {
                angulo = 0;
                setAnimacao(leste);
            }
        }
    }

    private void colisao(float delta) {
        // Verifica colisões
        for (Rectangle retangulo : retangulosColisao) {
            if (sobrepoe(retangulo)) {
                // Ajusta a posição do personagem para evitar a colisão
                // Isso pode ser feito de várias maneiras, dependendo da lógica do seu jogo
                // Por exemplo, você pode mover o personagem de volta para a posição anterior
                setPosition(getX() - getVelocidadeVetor().x * delta, getY() - getVelocidadeVetor().y * delta);
                break;
            }
        }

        for (Npc npc : npcs) {
            if (sobrepoe(npc)) {
                // Ajusta a posição do personagem para evitar a colisão
                // Isso pode ser feito de várias maneiras, dependendo da lógica do seu jogo
                // Por exemplo, você pode mover o personagem de volta para a posição anterior
                setPosition(getX() - getVelocidadeVetor().x * delta, getY() - getVelocidadeVetor().y * delta);
                break;
            }
        }
    }

    public float getAngulo() {
        return angulo;
    }

    public void setRetangulosColisao(Array<Rectangle> retangulosColisao) {
        this.retangulosColisao = retangulosColisao;
    }

}
