package br.edu.metropolitrans.model.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Herda a funcionalidade da classe Actor do LibGDX.
 * adicionando suporte para texturas/animação,
 * colisão de polígonos, movimento, limites do mundo e rolagem da câmera.
 * A maioria dos objetos do jogo deve estender esta classe; listas de extensões
 * podem ser recuperadas por palco e nome da classe.
 *
 * @see #Actor
 * @author Lee Stemkoski (Traduzido por Mariana)
 */
public class BaseActor extends Actor {

    /**
     * Margem para ajuste de retangulo de colisao
     */
    public int margemAltura, margemLargura, margemX, margemY = 0;

    /**
     * Animacao do ator
     */
    private Animation<TextureRegion> animacao;

    /**
     * Tempo de animacao
     */
    private float tempoAnimacao;

    /**
     * Animação pausada ou não
     */
    private boolean animacaoPausada;

    /**
     * Retangulo de limitacao do mundo
     */
    private static Rectangle limitacaoMundo;

    /**
     * Limite do poligono de colisao
     */
    private Polygon limitePoligono;

    /**
     * Vetor de velocidade
     */
    private Vector2 velocidadeVetor;

    /**
     * Vetor de aceleracao
     */
    private Vector2 aceleracaoVetor;

    /**
     * Aceleracao do ator
     */
    private float aceleracao;

    /**
     * Velocidade maxima do ator
     */
    private float velocidadeMaxima;

    /**
     * Desaceleracao do ator
     */
    private float desaceleracao;

    /**
     * Define a posição inicial do ator e adiciona ao palco
     */
    public BaseActor(float x, float y, Stage s) {
        super();

        // Inicializa o ator
        setPosition(x, y);
        s.addActor(this);

        // Inicializa dados de animacao
        animacao = null;
        tempoAnimacao = 0;
        animacaoPausada = false;

        // Inicializa dados de fisica do jogo
        velocidadeVetor = new Vector2(0, 0);
        aceleracaoVetor = new Vector2(0, 0);
        aceleracao = 0;
        velocidadeMaxima = 1000;
        desaceleracao = 0;

        limitePoligono = null;
    }

    /**
     * Adiciona um ator ao palco
     * 
     * @param stage palco
     */
    public void adicionarNoEstagio(Stage stage) {
        stage.addActor(this);
    }

    /**
     * Se este objeto se move completamente além dos limites do mundo,
     * ajuste sua posição para o lado oposto do mundo.
     */
    public void rolarAoRedorMundo() {
        if (getX() + getWidth() < 0)
            setX(limitacaoMundo.width);

        if (getX() > limitacaoMundo.width)
            setX(-getWidth());

        if (getY() + getHeight() < 0)
            setY(limitacaoMundo.height);

        if (getY() > limitacaoMundo.height)
            setY(-getHeight());
    }

    /**
     * Alinha o centro do ator nas coordenadas de posição fornecidas.
     *
     * @param x Coordenada x
     * @param y Coordenada y
     */
    public void centralizaNaPosicao(float x, float y) {
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }

    /**
     * Reposiciona este BaseActor para que seu centro seja alinhado
     * com o centro de outro BaseActor. Útil quando um BaseActor gera outro.
     *
     * @param outro BaseActor para centralizar neste BaseActor
     */
    public void centralizarNoAtor(BaseActor outro) {
        centralizaNaPosicao(outro.getX() + outro.getWidth() / 2, outro.getY() + outro.getHeight() / 2);
    }

    // region MÉTODOS DE ANIMAÇÃO

    /**
     * Define a animação usada ao renderizar este ator; também define o tamanho do
     * ator.
     *
     * @param anim animação que será desenhada quando o ator for renderizado
     */
    public void setAnimacao(Animation<TextureRegion> anim) {
        if (anim != null) {
            animacao = anim;
            TextureRegion tr = animacao.getKeyFrame(0);
            float l = tr.getRegionWidth();
            float a = tr.getRegionHeight();
            setSize(l, a);
            setOrigin(l / 2, a / 2);

            if (limitePoligono == null) {
                setLimiteRetangulo();
            }
        }
    }

    /**
     * Cria uma animação a partir de imagens armazenadas em arquivos separados.
     *
     * @param nomesArquivos lista de nomes de arquivos contendo imagens de animação
     * @param duracaoQuadro quanto tempo cada quadro deve ser exibido
     * @param loop          a animação deve ser em loop
     * @return animação criada (útil para armazenar várias animações)
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
     * Cria uma animação a partir de uma spritesheet: uma grade retangular de
     * imagens armazenadas em um único arquivo.
     *
     * @param nomeArquivo   nome do arquivo contendo a spritesheet
     * @param linhas        número de linhas de imagens na spritesheet
     * @param colunas       número de colunas de imagens na spritesheet
     * @param duracaoQuadro quanto tempo cada quadro deve ser exibido
     * @param loop          a animação deve ser em loop
     * @return animação criada (útil para armazenar várias animações)
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
     * Cria uma animação a partir de uma spritesheet: uma grade retangular de
     * imagens armazenadas em um único arquivo. Porém apenas uma linha da
     * spritesheet
     *
     * @param nomeArquivo   nome do arquivo contendo a spritesheet
     * @param linhas        número de linhas de imagens na spritesheet
     * @param colunas       número de colunas de imagens na spritesheet
     * @param duracaoQuadro quanto tempo cada quadro deve ser exibido
     * @param loop          a animação deve ser em loop
     * @param linha         linha da spritesheet a ser utilizada
     * @return animação criada (útil para armazenar várias animações)
     */
    public Animation<TextureRegion> carregaAnimacaoDeSpriteSheet(String nomeArquivo, int linhas, int colunas,
            float duracaoQuadro, boolean loop, int linha) {
        Texture textura = new Texture(Gdx.files.internal(nomeArquivo), true);
        textura.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        int larguraQuadro = textura.getWidth() / colunas;
        int alturaQuadro = textura.getHeight() / linhas;

        TextureRegion[][] quadros = TextureRegion.split(textura, larguraQuadro, alturaQuadro);
        TextureRegion[] linhaQuadros = new TextureRegion[colunas];
        for (int c = 0; c < colunas; c++) {
            linhaQuadros[c] = quadros[linha][c];
        }

        Animation<TextureRegion> anim = new Animation<>(duracaoQuadro, linhaQuadros);
        anim.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);

        return anim;
    }

    /**
     * Método ideal para criar uma animação de 1 quadro a partir de uma única
     * textura.
     *
     * @param nomeArquivo nome do arquivo de imagem
     * @return animação criada (útil para armazenar várias animações)
     */
    public Animation<TextureRegion> carregaTexturaEstatica(String nomeArquivo) {
        String[] nomeArquivos = { nomeArquivo };
        return carregaAnimacaoDeArquivos(nomeArquivos, 1, false);
    }

    /**
     * Define o estado de pausa da animação.
     *
     * @param pausa verdadeiro para pausar a animação, falso para continuar
     */
    public void setAnimacaoPausada(boolean pausa) {
        animacaoPausada = pausa;
    }

    /**
     * Verifica se a animação está completa: se o modo de reprodução é normal (não
     * em loop) e o tempo decorrido é maior que o tempo correspondente ao último
     * quadro.
     */
    public boolean isAnimacaoFinalizada() {
        return animacao.isAnimationFinished(tempoAnimacao);
    }

    /**
     * Define a opacidade do ator
     * 
     * @param opacity valor de opacidade 0 (transparente) a 1 (opaco)
     */
    public void setOpacidade(float opacidade) {
        getColor().a = opacidade;
    }

    // endregion

    // region MÉTODOS DE FÍSICA

    /**
     * Define a aceleração do ator
     *
     * @param aceleracao Aceleração em (pixels/segundo) por segundo
     */
    public void setAceleracao(float aceleracao) {
        this.aceleracao = aceleracao;
    }

    /**
     * Define a desaceleração do objeto.
     * Desaceleração é aplicada apenas quando o objeto não está acelerando.
     *
     * @param desaceleracao Desaceleração em (pixels/segundo) por segundo
     */
    public void setDesaceleracao(float desaceleracao) {
        this.desaceleracao = desaceleracao;
    }

    /**
     * Define a velocidade máxima deste objeto.
     *
     * @param velocidadeMaxima Velocidade máxima deste objeto em (pixels/segundo).
     */
    public void setVelocidadeMaxima(float velocidadeMaxima) {
        this.velocidadeMaxima = velocidadeMaxima;
    }

    /**
     * Define a velocidade do movimento (em pixels/segundo) na direção atual.
     * Se a velocidade atual for zero (direção indefinida), a direção será definida
     * para 0 graus.
     *
     * @param velocidade do movimento (em pixels/segundo)
     */
    public void setVelocidade(float velocidade) {
        // Se o tamanho for zero, então assuma que o ângulo de movimento é zero graus
        if (velocidadeVetor.len() == 0)
            velocidadeVetor.set(velocidade, 0);
        else
            velocidadeVetor.setLength(velocidade);
    }

    /**
     * Calcula a velocidade do movimento (em pixels/segundo).
     *
     * @return velocidade do movimento (em pixels/segundo)
     */
    public float getVelocidade() {
        return velocidadeVetor.len();
    }

    /**
     * Retorna o vetor de velocidade deste objeto.
     * 
     * @return vetor de velocidade
     */
    public Vector2 getVelocidadeVetor() {
        return velocidadeVetor;
    }

    /**
     * Determina se este objeto está se movendo (se a velocidade é maior que zero).
     *
     * @return false quando a velocidade é zero, true caso contrário
     */
    public boolean estaMovendo() {
        return (getVelocidade() > 0);
    }

    /**
     * Define o ângulo do movimento (em graus).
     * Se a velocidade atual for zero, isso não terá efeito.
     *
     * @param angulo do movimento (em graus)
     */
    public void setAnguloMovimento(float angulo) {
        velocidadeVetor.setAngle(angulo);
    }

    /**
     * Retorna o ângulo do movimento (em graus), calculado a partir do vetor de
     * velocidade.
     * <br>
     * Para alinhar o ângulo da imagem do ator com o ângulo do movimento, use
     * <code>setRotation( getMotionAngle() )</code>.
     *
     * @return ângulo do movimento (em graus)
     */
    public float getAnguloMovimento() {
        return velocidadeVetor.angle();
    }

    /**
     * Atualiza o vetor de aceleração pelo ângulo e valor armazenado no campo de
     * aceleração.
     * A aceleração é aplicada pelo método <code>applyPhysics</code>.
     *
     * @param angulo Ângulo (em graus) no qual acelerar.
     * @see #aceleracao
     * @see #aplicarFisica
     */
    public void acelerarEmAngulo(float angulo) {
        aceleracaoVetor.add(new Vector2(aceleracao, 0).setAngle(angulo));
    }

    /**
     * Atualiza o vetor de aceleração pelo ângulo de rotação atual e valor
     * armazenado no campo de aceleração.
     * A aceleração é aplicada pelo método <code>applyPhysics</code>.
     *
     * @see #aceleracao
     * @see #aplicarFisica
     */
    public void acelerarFrente() {
        acelerarEmAngulo(getRotation());
    }

    /**
     * Ajusta o vetor de velocidade com base no vetor de aceleração,
     * então ajusta a posição com base no vetor de velocidade. <br>
     * Se não estiver acelerando, o valor de desaceleração é aplicado. <br>
     * A velocidade é limitada pelo valor de velocidade máxima. <br>
     * O vetor de aceleração é redefinido para (0,0) no final do método. <br>
     *
     * @param dt Tempo decorrido desde o quadro anterior (delta time); normalmente
     *           obtido do método <code>act</code>.
     * @see #aceleracao
     * @see #desaceleracao
     * @see #velocidadeMaxima
     */
    public void aplicarFisica(float dt) {
        // Aplique a aceleração ao vetor de velocidade
        velocidadeVetor.add(aceleracaoVetor.x * dt, aceleracaoVetor.y * dt);

        float velocidade = getVelocidade();

        // desacelerar quando não estiver acelerando
        if (aceleracaoVetor.len() == 0)
            velocidade -= desaceleracao * dt;

        // manter a velocidade dentro dos limites definidos
        velocidade = MathUtils.clamp(velocidade, 0, velocidadeMaxima);

        // atualiza a velocidade
        setVelocidade(velocidade);

        // atualiza a posição de acordo com o valor armazenado no vetor de velocidade
        moveBy(velocidadeVetor.x * dt, velocidadeVetor.y * dt);

        // reseta a aceleração
        aceleracaoVetor.set(0, 0);
    }

    // endregion

    // region MÉTODOS DE COLISÃO

    /**
     * Define o polígono de colisão do ator como um retângulo.
     * Este método é chamado automaticamente quando a animação é definida,
     * desde que o polígono de limite atual seja nulo.
     *
     * @see #setAnimacao
     */
    public void setLimiteRetangulo() {
        float l = getWidth() + margemLargura;
        float a = getHeight() + margemAltura;

        // float[] vertices = { 0, 0, l, 0, l, a, 0, a };
        // Soma com as margens de x e y
        float[] vertices = { 0 + margemX, 0 + margemY, l, 0 + margemY, l, a, 0 + margemX, a };
        limitePoligono = new Polygon(vertices);
    }

    /**
     * Substitui o padrão (retangular) polígono de colisão por um polígono de n
     * lados. <br>
     * Vertices de um polígono estão na elipse contida no retângulo delimitador.
     * Observação: um vértice estará localizado no ponto (0, largura);
     * um polígono de 4 lados aparecerá na orientação de um losango.
     *
     * @param numeroLados número de lados do poligono de colisão
     */
    public void setLimitePoligono(int numeroLados) {
        float l = getWidth();
        float a = getHeight();

        float[] vertices = new float[2 * numeroLados];
        for (int i = 0; i < numeroLados; i++) {
            float angle = i * 6.28f / numeroLados;
            // x-coordenada
            vertices[2 * i] = l / 2 * MathUtils.cos(angle) + l / 2;
            // y-coordenada
            vertices[2 * i + 1] = a / 2 * MathUtils.sin(angle) + a / 2;
        }
        limitePoligono = new Polygon(vertices);
    }

    /**
     * Retorna o polígono de colisão do ator, ajustado pela posição atual e rotação
     *
     * @return Polígono de colisão deste objeto
     */
    public Polygon getLimitePoligono() {
        limitePoligono.setPosition(getX(), getY());
        limitePoligono.setOrigin(getOriginX(), getOriginY());
        limitePoligono.setRotation(getRotation());
        limitePoligono.setScale(getScaleX(), getScaleY());
        return limitePoligono;
    }

    /**
     * Determina se este BaseActor se sobrepõe a outro BaseActor (de acordo com os
     * polígonos de colisão).
     *
     * @param outro BaseActor para verificar a sobreposição
     * @return verdadeiro se os polígonos de colisão deste e de outro BaseActor se
     *         sobrepõem
     * @see #setLimiteRetangulo
     * @see #setLimitePoligono
     */
    public boolean sobrepoe(BaseActor outro) {
        Polygon poligono1 = this.getLimitePoligono();
        Polygon poligono2 = outro.getLimitePoligono();

        // Teste inicial para melhorar o desempenho
        if (!poligono1.getBoundingRectangle().overlaps(poligono2.getBoundingRectangle()))
            return false;

        return Intersector.overlapConvexPolygons(poligono1, poligono2);
    }

    public boolean sobrepoe(Rectangle retangulo) {

        Polygon poligono1 = this.getLimitePoligono();

        // Cria um polígono a partir do retângulo
        Polygon poligono2 = new Polygon(new float[] {
                retangulo.x, retangulo.y,
                retangulo.x + retangulo.width, retangulo.y,
                retangulo.x + retangulo.width, retangulo.y + retangulo.height,
                retangulo.x, retangulo.y + retangulo.height
        });

        // Teste inicial para melhorar o desempenho
        if (!poligono1.getBoundingRectangle().overlaps(retangulo))
            return false;

        // Retorna se houve colisão
        return Intersector.overlapConvexPolygons(poligono1, poligono2);
    }

    /**
     * Implementa um comportamento "sólido":
     * quando houver sobreposição, mova este BaseActor para longe do outro BaseActor
     * ao longo do vetor de translação mínima até que não haja sobreposição.
     *
     * @param outro BaseActor para verificar a sobreposição
     * @return direção do vetor pelo qual o ator foi traduzido, nulo se não houver
     *         sobreposição
     */
    public Vector2 evitaSobreposicao(BaseActor outro) {
        Polygon poligono1 = this.getLimitePoligono();
        Polygon poligono2 = outro.getLimitePoligono();

        // Teste inicial para melhorar o desempenho
        if (!poligono1.getBoundingRectangle().overlaps(poligono2.getBoundingRectangle()))
            return null;

        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean sobreposicaoPoligono = Intersector.overlapConvexPolygons(poligono1, poligono2, mtv);

        if (!sobreposicaoPoligono)
            return null;

        this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
        return mtv.normal;
    }

    /**
     * Implementa um comportamento "sólido":
     * quando houver sobreposição, mova este BaseActor para longe do outro BaseActor
     * ao longo do vetor de translação mínima até que não haja sobreposição.
     *
     * @param retangulo retângulo para verificar a sobreposição
     * @return direção do vetor pelo qual o ator foi traduzido, nulo se não houver
     *         sobreposição
     */
    public Vector2 evitaSobreposicao(Rectangle retangulo) {
        Polygon poligono1 = this.getLimitePoligono();

        // Cria um polígono a partir do retângulo
        Polygon poligono2 = new Polygon(new float[] {
                retangulo.x, retangulo.y,
                retangulo.x + retangulo.width, retangulo.y,
                retangulo.x + retangulo.width, retangulo.y + retangulo.height,
                retangulo.x, retangulo.y + retangulo.height
        });

        // Teste inicial para melhorar o desempenho
        if (!poligono1.getBoundingRectangle().overlaps(retangulo))
            return null;

        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean sobreposicaoPoligono = Intersector.overlapConvexPolygons(poligono1, poligono2, mtv);

        if (!sobreposicaoPoligono)
            return null;

        this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
        return mtv.normal;
    }

    /**
     * Determina se este BaseActor está próximo de outro BaseActor (de acordo com os
     * polígonos de colisão).
     *
     * @param distancia quantia (pixels) pela qual aumentar a largura e a altura do
     *                  polígono de colisão
     * @param outro     BaseActor para verificar a proximidade
     * @return verdadeiro se os polígonos de colisão deste (aumentado) e de outro
     *         BaseActor se sobrepõem
     * @see #setLimiteRetangulo
     * @see #setLimitePoligono
     */
    public boolean estaDentroDaDistancia(float distancia, BaseActor outro) {
        Polygon poligono1 = this.getLimitePoligono();
        float escalaX = (this.getWidth() + 2 * distancia) / this.getWidth();
        float escalaY = (this.getHeight() + 2 * distancia) / this.getHeight();
        poligono1.setScale(escalaX, escalaY);

        Polygon poligono2 = outro.getLimitePoligono();

        // Teste inicial para melhorar o desempenho
        if (!poligono1.getBoundingRectangle().overlaps(poligono2.getBoundingRectangle()))
            return false;

        return Intersector.overlapConvexPolygons(poligono1, poligono2);
    }

    /**
     * Determina se este BaseActor está próximo de outro BaseActor (de acordo com os
     * polígonos de colisão).
     *
     * @param distancia quantia (pixels) pela qual aumentar a largura e a altura do
     *                  polígono de colisão
     * @param outro     Retangulo para verificar a proximidade
     * @return verdadeiro se os polígonos de colisão deste (aumentado) e de outro
     *         Retangulo se sobrepõem
     * @see #setLimiteRetangulo
     * @see #setLimitePoligono
     */
    public boolean estaDentroDaDistancia(float distancia, Rectangle outro) {
        Polygon poligono1 = this.getLimitePoligono();
        float escalaX = (this.getWidth() + 2 * distancia) / this.getWidth();
        float escalaY = (this.getHeight() + 2 * distancia) / this.getHeight();
        poligono1.setScale(escalaX, escalaY);

        // Cria um polígono a partir do retângulo
        Polygon poligono2 = new Polygon(new float[] {
            outro.x, outro.y,
            outro.x + outro.width, outro.y,
            outro.x + outro.width, outro.y + outro.height,
            outro.x, outro.y + outro.height
        });

        // Teste inicial para melhorar o desempenho
        if (!poligono1.getBoundingRectangle().overlaps(poligono2.getBoundingRectangle()))
            return false;

        return Intersector.overlapConvexPolygons(poligono1, poligono2);
    }

    /**
     * Define as dimensões do mundo para uso pelos métodos limitaMundo() e
     * scrollTo().
     *
     * @param largura Largura da limitação do mundo
     * @param altura  Altura da limitação do mundo
     */
    public static void setLimitacaoMundo(float largura, float altura) {
        limitacaoMundo = new Rectangle(0, 0, largura, altura);
    }

    /**
     * Define as dimensões do mundo para uso pelos métodos limitaMundo() e
     * scrollTo().
     *
     * @param ator no qual o tamanho determina os limites do mundo (normalmente uma
     *             imagem de fundo)
     */
    public static void setLimitacaoMundo(BaseActor ator) {
        setLimitacaoMundo(ator.getWidth(), ator.getHeight());
    }

    /**
     * Retorna o retângulo que representa os limites do mundo.
     */
    public static Rectangle getLimitacaoMundo() {
        return limitacaoMundo;
    }

    /**
     * Se uma borda de um objeto se move além dos limites do mundo,
     * ajuste sua posição para mantê-lo completamente na tela.
     */
    public void limitaMundo() {
        // Limita a posição do ator à limitação do mundo
        float x = getX();
        float y = getY();

        // Verifica a borda esquerda
        if (x < 0)
            setX(0);
        // Verifica a borda direita
        if (x + getWidth() > limitacaoMundo.width)
            setX(limitacaoMundo.width - getWidth());
        // Verifica a borda inferior
        if (y < 0)
            setY(0);
        // Verifica a borda superior
        if (y + getHeight() > limitacaoMundo.height)
            setY(limitacaoMundo.height - getHeight());
    }

    /**
     * Centraliza a câmera neste objeto, mantendo a área de visualização da câmera
     * (determinada pelo tamanho da tela) completamente dentro dos limites do mundo.
     */
    public void alinhamentoCamera() {
        Camera camera = this.getStage().getCamera();

        // Centraliza a câmera na posição do ator
        camera.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);

        // limita a câmera à área de visualização
        float minimoX = camera.viewportWidth / 2;
        float maximoX = limitacaoMundo.width - minimoX;
        float minimoY = camera.viewportHeight / 2;
        float maximoY = limitacaoMundo.height - minimoY;

        camera.position.x = MathUtils.clamp(camera.position.x, minimoX, maximoX);
        camera.position.y = MathUtils.clamp(camera.position.y, minimoY, maximoY);

        camera.update();
    }

    // endregion

    // region MÉTODOS DE LISTAGEM

    /**
     * Fornece uma lista de todas as instâncias do objeto do palco fornecido com o
     * nome da classe fornecido
     * ou cuja classe estende a classe com o nome fornecido.
     * Se nenhuma instância existir, retorna uma lista vazia.
     * É útil quando codificando interações entre diferentes tipos de objetos de
     * jogo no método de atualização.
     *
     * @param estagio    Estágio contendo instâncias de BaseActor
     * @param nomeClasse Nome da classe que estende a classe BaseActor
     * @return lista de instâncias do objeto no palco que estendem com o nome da
     *         classe
     */
    public static ArrayList<BaseActor> getList(Stage estagio, String nomeClasse) {
        ArrayList<BaseActor> lista = new ArrayList<BaseActor>();

        Class classe = null;
        try {
            classe = Class.forName(nomeClasse);
        } catch (Exception error) {
            error.printStackTrace();
        }

        for (Actor a : estagio.getActors()) {
            if (classe.isInstance(a))
                lista.add((BaseActor) a);
        }

        return lista;
    }

    /**
     * Retorna o número de instâncias de uma classe fornecida (que estende
     * BaseActor).
     * 
     * @param estagio    Estágio contendo instâncias de BaseActor
     * @param nomeClasse Nome de uma classe que estende a classe BaseActor
     * @return Número de instâncias da classe fornecida
     */
    public static int count(Stage estagio, String nomeClasse) {
        return getList(estagio, nomeClasse).size();
    }

    // endregion

    // region MÉTODOS DE AÇÃO E RENDERIZAÇÃO

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!animacaoPausada)
            tempoAnimacao += delta;
    }

    @Override
    /**
     * Desenha o quadro atual da animação; chamado automaticamente pelo método draw
     * na classe Stage. <br>
     * Se a cor foi definida, a imagem será tingida por essa cor. <br>
     * Se nenhuma animação foi definida ou o objeto é invisível, nada será
     * desenhado.
     *
     * @param batch       (supplied by Stage draw method)
     * @param parentAlpha (supplied by Stage draw method)
     * @see #setColor
     * @see #setVisible
     */
    public void draw(Batch batch, float parentAlpha) {

        // Aplica a cor do efeito
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);

        // Desenha a animação
        if (animacao != null && isVisible()) {
            batch.draw(animacao.getKeyFrame(tempoAnimacao), getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                    getHeight(), getScaleX(), getScaleY(), getRotation());
        }

        super.draw(batch, parentAlpha);
    }

    // endregion

}
