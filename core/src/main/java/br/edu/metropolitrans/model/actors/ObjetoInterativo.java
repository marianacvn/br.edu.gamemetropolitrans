package br.edu.metropolitrans.model.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Classe que representa um objeto interativo
 * 
 * @see BaseActor
 */
public class ObjetoInterativo extends BaseActor {

    public String nome;
    public float x, y;
    public String nomeArquivo;
    public Stage stage;

    /**
     * Cria um objeto interativo
     * 
     * @param nome        Nome do objeto
     * @param x           Posição x
     * @param y           Posição y
     * @param nomeArquivo Nome do arquivo de textura
     * @param stage       Estágio
     */
    public ObjetoInterativo(String nome, float x, float y, String nomeArquivo, Stage stage) {
        super(x, y, stage);
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.nomeArquivo = nomeArquivo;
        this.stage = stage;
        carregaTexturaEstatica("files/objects/" + nomeArquivo);
    }

    /**
     * Cria um objeto interativo
     * 
     * @param nome        Nome do objeto
     * @param x           Posição x
     * @param y           Posição y
     * @param nomeArquivo Nome do arquivo de textura
     * @param stage       Estágio
     * @param isVisible   Visibilidade
     */
    public ObjetoInterativo(String nome, float x, float y, String nomeArquivo, Stage stage, boolean isVisible) {
        this(nome, x, y, nomeArquivo, stage);
        setVisible(isVisible);
    }

}
