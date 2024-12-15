package br.edu.metropolitrans.model.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

public class Vehicle extends BaseActor {

    public String nome;
    public String nomeArquivo;
    public int velocidade;
    public int statusAlertaMissao;
    public Texture minimapaPonto;
    public boolean animacaoAtivada;
    private float xInicial;
    private float yInicial;

    private Animation<TextureRegion> animacaoCima;
    private Animation<TextureRegion> animacaoEsquerda;
    private Animation<TextureRegion> animacaoBaixo;
    private Animation<TextureRegion> animacaoDireita;

    private float tempoMissao;
    private int etapaMissao;
    private List<String> roteiro;
    private float distanciaPercorrida;

    /**
     * Construtor da classe Vehicle
     * 
     * @param nome        nome do veículo
     * @param x           posição inicial x do veículo
     * @param y           posição inicial y do veículo
     * @param velocidade  velocidade do veículo
     * @param nomeArquivo nome do arquivo de animação/gráfico do veículo
     * @param stage       palco do jogo
     * @param roteiro     roteiro do veículo, formato aceito
     *                    "DIREÇÃO-DISTÂNCIA*BLOCOS",
     *                    onde DIREÇÃO é C, E, B ou D (Cima, Esquerda, Baixo,
     *                    Direita) e DISTÂNCIA é a distância a ser percorrida
     *                    e BLOCOS é a quantidade de blocos a serem percorridos.
     *                    Exemplo: "C-1*2" (Cima, 1 bloco, 2 vezes)
     */
    public Vehicle(String nome, float x, float y, int velocidade, String nomeArquivo, Stage stage,
            List<String> roteiro) {
        super(x, y, stage);

        // Salva a posição inicial do veículo
        xInicial = x;
        yInicial = y;

        // Atribui os valores recebidos aos atributos da classe
        this.nome = nome;
        this.velocidade = velocidade;
        this.nomeArquivo = nomeArquivo;
        this.roteiro = roteiro;

        // Define algumas configurações iniciais
        margemAltura = -15;
        tempoMissao = 0;
        etapaMissao = 0;
        distanciaPercorrida = 0;
        animacaoAtivada = false;

        // Carrega as animações do veículo
        carregaAnimacoes("files/vehicles/" + nomeArquivo);

        try {
            minimapaPonto = new Texture(
                    Gdx.files.internal("files/characters/" + nomeArquivo.replace("sprite.png", "minimap.png")));
        } catch (Exception ignore) {
            Gdx.app.log("Vehicle", "Erro ao carregar minimapaPonto para " + nomeArquivo);
        }

        // Inicializa o veículo como invisível
        setVisible(false);
    }

    private void carregaAnimacoes(String nomeArquivo) {
        animacaoCima = carregaAnimacaoDeSpriteSheet(nomeArquivo, 4, 6, 0.1f, true, 0);
        animacaoEsquerda = carregaAnimacaoDeSpriteSheet(nomeArquivo, 4, 6, 0.1f, true, 1);
        animacaoBaixo = carregaAnimacaoDeSpriteSheet(nomeArquivo, 4, 6, 0.1f, true, 2);
        animacaoDireita = carregaAnimacaoDeSpriteSheet(nomeArquivo, 4, 6, 0.1f, true, 3);
    }

    public void animarParaCima() {
        setAnimacao(animacaoCima);
    }

    public void animarParaEsquerda() {
        setAnimacao(animacaoEsquerda);
    }

    public void animarParaBaixo() {
        setAnimacao(animacaoBaixo);
    }

    public void animarParaDireita() {
        setAnimacao(animacaoDireita);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (animacaoAtivada) {
            if (etapaMissao < roteiro.size()) {
                String[] instrucao = roteiro.get(etapaMissao).split("-");
                String direcao = instrucao[0];
                float distancia = Float.parseFloat(instrucao[1].split("\\*")[0])
                        * Float.parseFloat(instrucao[1].split("\\*")[1]);

                switch (direcao) {
                    case "C":
                        animarParaCima();
                        setVelocidade(velocidade);
                        moverParaCima(delta, distancia);
                        break;
                    case "E":
                        animarParaEsquerda();
                        setVelocidade(velocidade);
                        moverParaEsquerda(delta, distancia);
                        break;
                    case "B":
                        animarParaBaixo();
                        setVelocidade(velocidade);
                        moverParaBaixo(delta, distancia);
                        break;
                    case "D":
                        animarParaDireita();
                        setVelocidade(velocidade);
                        moverParaDireita(delta, distancia);
                        break;
                }
            } else {
                // Caso o veículo tenha terminado o roteiro, volta para a posição inicial
                // e começa novamente
                etapaMissao = 0;
                distanciaPercorrida = 0;
                setPosition(xInicial, yInicial);
            }
        }
    }

    private void moverParaCima(float delta, float distancia) {
        float movimento = getVelocidade() * delta;
        moveBy(0, movimento);
        distanciaPercorrida += movimento;
        if (distanciaPercorrida >= distancia) {
            etapaMissao++;
            distanciaPercorrida = 0;
        }
    }

    private void moverParaEsquerda(float delta, float distancia) {
        float movimento = getVelocidade() * delta;
        moveBy(-movimento, 0);
        distanciaPercorrida += movimento;
        if (distanciaPercorrida >= distancia) {
            etapaMissao++;
            distanciaPercorrida = 0;
        }
    }

    private void moverParaBaixo(float delta, float distancia) {
        float movimento = getVelocidade() * delta;
        moveBy(0, -movimento);
        distanciaPercorrida += movimento;
        if (distanciaPercorrida >= distancia) {
            etapaMissao++;
            distanciaPercorrida = 0;
        }
    }

    private void moverParaDireita(float delta, float distancia) {
        float movimento = getVelocidade() * delta;
        moveBy(movimento, 0);
        distanciaPercorrida += movimento;
        if (distanciaPercorrida >= distancia) {
            etapaMissao++;
            distanciaPercorrida = 0;
        }
    }
}