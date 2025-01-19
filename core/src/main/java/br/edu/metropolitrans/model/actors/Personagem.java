package br.edu.metropolitrans.model.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

import br.edu.metropolitrans.model.PersonagemDirecao;

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

    private PersonagemDirecao ultimaDirecao;

    /**
     * Ângulo de rotação do personagem
     */
    private float angulo;

    private Array<Rectangle> retangulosColisao, retangulosPista;

    public ArrayList<Npc> npcs;

    public Texture minimapaPonto;

    /**
     * Moedas do personagem
     */
    public int moedas;

    /**
     * XP do personagem
     */
    public int xp;

    /**
     * Indica se o personagem sofreu uma infração
     */
    public boolean sofreuInfracao;

    /**
     * Infrações cometidas pelo personagem
     */
    public int infracoes;

    private float margemInfracao = -30.0f;

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
        minimapaPonto = new Texture(Gdx.files.internal("files/characters/mainCharacter/minimap-male.png"));

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
        moedas = 200;
        xp = 10;
        sofreuInfracao = false;
        infracoes = 0;
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

    /**
     * Verifica e define a animação do personagem
     */
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

    /**
     * Atualiza a posição do personagem relacionado a infração
     */
    private void atualizaPosicaoInfracao() {
        switch (ultimaDirecao) {
            case NORTE:
                setPosition(getX(), getY() + margemInfracao);
                break;
            case SUL:
                setPosition(getX(), getY() - margemInfracao);
                break;
            case LESTE:
                setPosition(getX() + margemInfracao, getY());
                break;
            case OESTE:
                setPosition(getX() - margemInfracao, getY());
                break;
            case NORDESTE:
                setPosition(getX() + margemInfracao / ((float) Math.sqrt(2)),
                        getY() + margemInfracao / ((float) Math.sqrt(2)));
                break;
            case NOROESTE:
                setPosition(getX() - margemInfracao / ((float) Math.sqrt(2)),
                        getY() + margemInfracao / ((float) Math.sqrt(2)));
                break;
            case SUDESTE:
                setPosition(getX() + margemInfracao / ((float) Math.sqrt(2)),
                        getY() - margemInfracao / ((float) Math.sqrt(2)));
                break;
            case SUDOESTE:
                setPosition(getX() - margemInfracao / ((float) Math.sqrt(2)),
                        getY() - margemInfracao / ((float) Math.sqrt(2)));
                break;
            default:
                Gdx.app.log("Personagem", "Direção inválida durante o ajuste de infração!");
                break;
        }
    
        // Adiciona log para depuração
        Gdx.app.log("Personagem", String.format(
            "Infracao! Direção: %s | Posição final: X=%.2f, Y=%.2f",
            ultimaDirecao, getX(), getY()
        ));
    }

    /**
     * Verifica se o personagem colidiu com algum objeto
     * 
     * @param delta tempo entre um quadro e outro
     */
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

        // Verifica colisões com o NPC
        for (Npc npc : npcs) {
            if (sobrepoe(npc)) {
                // Ajusta a posição do personagem para evitar a colisão
                // Isso pode ser feito de várias maneiras, dependendo da lógica do seu jogo
                // Por exemplo, você pode mover o personagem de volta para a posição anterior
                setPosition(getX() - getVelocidadeVetor().x * delta, getY() - getVelocidadeVetor().y * delta);
                break;
            }
        }

        // Verifica colisões com a pista
        for (Rectangle retangulo : retangulosPista) {
            // Verifica se o personagem passou x do retangulo da pista,
            // caso isto ocorra deverá comunicar que o personagem saiu da pista
            // cometendo uma infração
            if (estaDentroDaDistancia(margemInfracao, retangulo)) {
                sofreuInfracao = true;
                atualizaPosicaoInfracao();
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

    public void setRetangulosPista(Array<Rectangle> retangulosPista) {
        this.retangulosPista = retangulosPista;
    }

    public void setUltimaDirecao(PersonagemDirecao direcao) {
        this.ultimaDirecao = direcao;
        Gdx.app.log("Personagem", "Última direção atualizada para: " + direcao);
    }

}
