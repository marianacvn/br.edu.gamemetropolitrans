package br.edu.metropolitrans.model.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

import br.edu.metropolitrans.model.PersonagemDirecao;
import br.edu.metropolitrans.model.utils.DebugMode;

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
    public float angulo;

    private Array<Rectangle> retangulosColisao, retangulosPista;

    public HashMap<String, Npc> npcs;

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
    public TipoInfracao tipoInfracao;

    public enum TipoInfracao {
        ALERTA, MULTA
    }

    public int dialogosGuarda = 0;

    /**
     * Infrações cometidas pelo personagem
     */
    public int infracoes;

    private float margemInfracao = -30.0f;

    public String selectedCharacter;

    public Personagem(float x, float y, Stage s) {
        super(x, y, s);

        selectedCharacter = "male";

        // Ajusta as margens para centralizar o personagem na colisão
        margemAltura = -10;
        margemLargura = -15;
        margemX = 15;

        // // Carrega a textura
        atualizarSpritePersonagem("male");

        angulo = 270;

        setAceleracao(800);
        setVelocidadeMaxima(200);
        setDesaceleracao(800);
        // setZeraVelocidadeInstantaneamente(true);
    }

    public boolean interagiu(ObjetoInterativo objetoInterativo) {
        return this.sobrepoe(objetoInterativo);
    }

    public void setValoresDafault() {
        angulo = 270;

        // Inicia a animação com a direção SUL
        setAnimacao(sul);
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

    public void atualizarSpritePersonagem(String selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
        
        String nomeArquivo = "files/characters/mainCharacter/character-" + selectedCharacter + "_spritesheet.png";
        int linhas = 4;
        int colunas = 11;

        // Carrega a textura
        Texture textura = new Texture(Gdx.files.internal(nomeArquivo), true);
        minimapaPonto = new Texture(
                Gdx.files.internal("files/characters/mainCharacter/minimap-" + selectedCharacter + ".png"));

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
                ultimaDirecao = PersonagemDirecao.NORTE;
            } else if (angle > 135 && angle < 225) {
                angulo = 180;
                setAnimacao(oeste);
                ultimaDirecao = PersonagemDirecao.OESTE;
            } else if (angle >= 225 && angle <= 315) {
                angulo = 270;
                setAnimacao(sul);
                ultimaDirecao = PersonagemDirecao.SUL;
            } else {
                angulo = 0;
                setAnimacao(leste);
                ultimaDirecao = PersonagemDirecao.LESTE;
            }
        }
    }

    /**
     * Atualiza a posição do personagem relacionado a infração
     */
    private void atualizaPosicaoInfracao() {
        float novaPosX = getX();
        float novaPosY = getY();
    
        DebugMode.mostrarLog("Personagem", String.format(
                "Atualizando posição de infração. Direção: %s | Posição atual: X=%.2f, Y=%.2f | Margem: %.2f",
                ultimaDirecao, getX(), getY(), margemInfracao));
    
        switch (ultimaDirecao) {
            case NORTE:
                novaPosY += margemInfracao;
                break;
            case SUL:
                novaPosY -= margemInfracao;
                break;
            case LESTE:
                novaPosX += margemInfracao;
                break;
            case OESTE:
                novaPosX -= margemInfracao;
                break;
            default:
                DebugMode.mostrarLog("Personagem", "Direção inválida durante o ajuste de infração!");
                return;
        }
    
        setPosition(novaPosX, novaPosY);
    
        // Adiciona log para depuração
        DebugMode.mostrarLog("Personagem", String.format(
                "Infracao! Direção: %s | Posição final: X=%.2f, Y=%.2f",
                ultimaDirecao, getX(), getY()));
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
        npcs.forEach((nome, npc) -> {
            if (sobrepoe(npc)) {
                // Ajusta a posição do personagem para evitar a colisão
                // Isso pode ser feito de várias maneiras, dependendo da lógica do seu jogo
                // Por exemplo, você pode mover o personagem de volta para a posição anterior
                setPosition(getX() - getVelocidadeVetor().x * delta, getY() - getVelocidadeVetor().y * delta);
            }
        });

        // Verifica colisões com a pista
        if (DebugMode.INFRACOES_ATIVAS) {
            for (Rectangle retangulo : retangulosPista) {
                // Verifica se o personagem passou x do retangulo da pista,
                // caso isto ocorra deverá comunicar que o personagem saiu da pista
                // cometendo uma infração
                if (estaDentroDaDistancia(margemInfracao, retangulo)) {
                    if (dialogosGuarda > 0) {
                        tipoInfracao = Personagem.TipoInfracao.MULTA;
                    } else {
                        tipoInfracao = Personagem.TipoInfracao.ALERTA;
                    }
                    atualizaPosicaoInfracao();
                    return;
                }
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

    @Override
    public void dispose() {
        super.dispose();

        if (minimapaPonto != null) {
            minimapaPonto.dispose();
        }

        if (norte != null) {
            norte.getKeyFrame(0).getTexture().dispose();
        }

        if (sul != null) {
            sul.getKeyFrame(0).getTexture().dispose();
        }

        if (leste != null) {
            leste.getKeyFrame(0).getTexture().dispose();
        }

        if (oeste != null) {
            oeste.getKeyFrame(0).getTexture().dispose();
        }

        norte = null;
        sul = null;
        leste = null;
        oeste = null;
    }

}
