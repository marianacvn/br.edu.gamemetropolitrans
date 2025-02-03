package br.edu.metropolitrans.model.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

public class Vehicle extends ActorAnimation {
    private Rectangle boundingRectangle;

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
            List<String> roteiro, boolean temAnimacao) {
        super(nome, x, y, stage, velocidade, nomeArquivo, "files/vehicles/", roteiro, temAnimacao, 4, 6, true);
        margemAltura = -15;
        setVisible(false);
        boundingRectangle = new Rectangle(x, y, getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        boundingRectangle.setPosition(getX(), getY());
    }

    public Rectangle getBoundingRectangle() {
        return boundingRectangle;
    }
}