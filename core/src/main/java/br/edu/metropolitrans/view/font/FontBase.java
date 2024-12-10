package br.edu.metropolitrans.view.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Classe responsável por gerar fontes personalizadas
 * 
 * @version 1.0
 */
public class FontBase {

    private static FontBase intancia;
    private FreeTypeFontGenerator generator;

    public enum Fontes {
        PADRAO("files/fonts/Silver.ttf"),
        MONOGRAM("files/fonts/monogram.ttf");

        public String caminho;

        Fontes(String caminho) {
            this.caminho = caminho;
        }
    }

    private FontBase() {
    }

    /**
     * Retorna a instância da classe
     * 
     * @return Instância da classe
     */
    public static FontBase getInstancia() {
        if (intancia == null) {
            intancia = new FontBase();
        }
        return intancia;
    }

    /**
     * Retorna uma fonte personalizada
     * 
     * @param size
     *               Tamanho da fonte
     * @param fontes Tipo de fonte
     * @return Fonte personalizada
     */
    public BitmapFont getFonte(int size, FontBase.Fontes fontes) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        return getFonte(parameter, fontes);
    }

    /**
     * Retorna uma fonte personalizada
     * 
     * @param size
     *               Tamanho da fonte=
     * @param cor
     *               Cor da fonte
     * @param fontes Tipo de fonte
     * @return Fonte personalizada
     */
    public BitmapFont getFonte(int size, Color cor, FontBase.Fontes fontes) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = cor;
        return getFonte(parameter, fontes);
    }

    /**
     * Retorna uma fonte personalizada
     * 
     * @param parameter
     *                  Parâmetros da fonte
     * @param fontes    Tipo de fonte
     * @return Fonte personalizada
     */
    public BitmapFont getFonte(FreeTypeFontGenerator.FreeTypeFontParameter parameter, FontBase.Fontes fontes) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontes.caminho));
        BitmapFont fonte = generator.generateFont(parameter);
        generator.dispose(); // Libera os recursos do gerador de fontes
        return fonte;
    }

}
