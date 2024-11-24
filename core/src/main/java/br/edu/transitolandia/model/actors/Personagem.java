package br.edu.transitolandia.model.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    /**
     * Câmera do jogo
     */
    private Camera camera;

    public Personagem(float x, float y, Stage s) {
        super(x, y, s);

        // Carraga a animação do personagem
        String nomeArquivo = "files/characters/mainCharacter/character-female-32.png";
        int linhas = 4;
        int colunas = 3;

        // Carrega a textura
        Texture textura = new Texture(Gdx.files.internal(nomeArquivo), true);

        // Divide a textura em quadros
        int larguraQuadro = textura.getWidth() / colunas;
        int alturaQuadro = textura.getHeight() / linhas;
        float duracaoQuadro = 0.2f;

        // Cria uma matriz de texturas e uma lista de texturas
        TextureRegion[][] quadros = TextureRegion.split(textura, larguraQuadro, alturaQuadro);
        Array<TextureRegion> listaTexturas = new Array<TextureRegion>();

        // Percorre a matriz de texturas e adiciona na lista de texturas para criar a
        // animação de cada direção
        // A animação é criada com a lista de texturas, a duração de cada quadro e o
        // modo de repetição
        // SUL
        for (int c = 0; c < colunas; c++)
            listaTexturas.add(quadros[0][c]);
        sul = new Animation<TextureRegion>(duracaoQuadro, listaTexturas, Animation.PlayMode.LOOP_PINGPONG);

        // OESTE
        listaTexturas.clear();
        for (int c = 0; c < colunas; c++)
            listaTexturas.add(quadros[1][c]);
        oeste = new Animation<TextureRegion>(duracaoQuadro, listaTexturas, Animation.PlayMode.LOOP_PINGPONG);

        // LESTE
        listaTexturas.clear();
        for (int c = 0; c < colunas; c++)
            listaTexturas.add(quadros[2][c]);
        leste = new Animation<TextureRegion>(duracaoQuadro, listaTexturas, Animation.PlayMode.LOOP_PINGPONG);

        // NORTE
        listaTexturas.clear();
        for (int c = 0; c < colunas; c++)
            listaTexturas.add(quadros[3][c]);
        norte = new Animation<TextureRegion>(duracaoQuadro, listaTexturas, Animation.PlayMode.LOOP_PINGPONG);

        // Inicia a animação com a direção SUL
        setAnimacao(sul);
        angulo = 270;

        // Configuracao do personagem
        setLimitePoligono(8);
        setAceleracao(400);
        setVelocidadeMaxima(100);
        setDesaceleracao(400);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Movimenta o personagem
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

        alinhamentoCamera(camera);
        limitaMundo();
        aplicarFisica(delta);
    }

    public float getAngulo() {
        return angulo;
    }

    public void setCamera(Camera c) {
        camera = c;
    }
}
