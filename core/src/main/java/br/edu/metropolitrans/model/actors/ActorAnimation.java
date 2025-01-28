package br.edu.metropolitrans.model.actors;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class ActorAnimation extends BaseActor {

    public String nome;
    public String nomeArquivo;
    public int velocidade;
    public int statusAlertaMissao;
    public boolean animacaoAtivada;
    public boolean repeteAnimacao;
    private float xInicial;
    private float yInicial;

    private Animation<TextureRegion> animacaoCima;
    private Animation<TextureRegion> animacaoEsquerda;
    private Animation<TextureRegion> animacaoBaixo;
    private Animation<TextureRegion> animacaoDireita;

    private int etapaMissao;
    private List<String> roteiro;
    private float distanciaPercorrida;
    private boolean temAnimacao;
    private int linhas, colunas;

    /**
     * Construtor da classe de Animacao
     * 
     * @param nome            nome
     * @param x               posição inicial x
     * @param y               posição inicial y
     * @param stage           palco do jogo
     * @param velocidade      velocidade
     * @param nomeArquivo     nome do arquivo de animação/gráfico
     * @param caminhoAnimacao caminho da animação
     * @param roteiro         roteiro da animação, formato aceito
     *                        "DIREÇÃO-DISTÂNCIA*BLOCOS",
     *                        onde DIREÇÃO é C, E, B ou D (Cima, Esquerda, Baixo,
     *                        Direita) e DISTÂNCIA é a distância a ser percorrida
     *                        e BLOCOS é a quantidade de blocos a serem percorridos.
     *                        Exemplo: "C-1*2" (Cima, 1 bloco, 2 vezes)
     * @param temAnimacao     se tem animação
     * @param linhas          linhas
     * @param colunas         colunas
     * @param repeteAnimacao  se repete a animação
     */
    public ActorAnimation(String nome, float x, float y, Stage stage, int velocidade, String nomeArquivo,
            String caminhoAnimacao, List<String> roteiro, boolean temAnimacao, int linhas, int colunas,
            boolean repeteAnimacao) {
        super(x, y, stage);

        // Salva a posição inicial do veículo
        xInicial = x;
        yInicial = y;

        this.nome = nome;
        this.roteiro = roteiro;
        this.velocidade = velocidade;
        this.nomeArquivo = nomeArquivo;
        this.roteiro = roteiro;
        this.temAnimacao = temAnimacao;
        this.linhas = linhas;
        this.colunas = colunas;
        this.repeteAnimacao = repeteAnimacao;

        etapaMissao = 0;
        distanciaPercorrida = 0;
        animacaoAtivada = false;

        // Carrega as animações do veículo
        carregaAnimacoes(caminhoAnimacao + nomeArquivo);
    }

    private void carregaAnimacoes(String nomeArquivo) {
        if (temAnimacao) {
            animacaoCima = carregaAnimacaoDeSpriteSheet(nomeArquivo, linhas, colunas, 0.1f, true, 0);
            animacaoEsquerda = carregaAnimacaoDeSpriteSheet(nomeArquivo, linhas, colunas, 0.1f, true, 1);
            animacaoBaixo = carregaAnimacaoDeSpriteSheet(nomeArquivo, linhas, colunas, 0.1f, true, 2);
            animacaoDireita = carregaAnimacaoDeSpriteSheet(nomeArquivo, linhas, colunas, 0.1f, true, 3);
        }
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

    public void reiniciarAnimacao() {
        if (repeteAnimacao) {
            etapaMissao = 0;
            distanciaPercorrida = 0;
            setPosition(xInicial, yInicial);
        } else {
            setVelocidade(0);
            roteiro = List.of();
            animacaoAtivada = false;
            setAnimacaoPausada(true);
        }
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
                reiniciarAnimacao();
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

    /**
     * Atualiza o roteiro do veículo
     * 
     * @param roteiro Lista de instruções do roteiro, formato aceito
     *                "DIREÇÃO-DISTÂNCIA*BLOCOS",
     *                onde DIREÇÃO é C, E, B ou D (Cima, Esquerda, Baixo,
     *                Direita) e DISTÂNCIA é a distância a ser percorrida
     *                e BLOCOS é a quantidade de blocos a serem percorridos.
     *                Exemplo: "C-1*2" (Cima, 1 bloco, 2 vezes)
     */
    public void setRoteiro(List<String> roteiro) {
        this.roteiro = roteiro;
    }

    /**
     * Atualiza a posição do veículo
     */
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        xInicial = x;
        yInicial = y;
    }
}
