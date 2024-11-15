package br.edu.transitolandia.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import br.edu.transitolandia.Transitolandia;
import br.edu.transitolandia.view.actors.Personagem;

public class GameScreen implements Screen {
    final Transitolandia jogo;
    Texture fundoTela;
    Personagem personagem;

    public GameScreen(final Transitolandia jogo) {
        this.jogo = jogo;

        // Carrega as imagens
        fundoTela = new Texture(jogo.DIRETORIO_BASE_ARQUIVOS + "backgroundExemplo.png");
        personagem = new Personagem(20, 20, jogo.mainStage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        desenhar();
    }

    private void desenhar() {
        float dt = Gdx.graphics.getDeltaTime();
        jogo.mainStage.act(dt);

        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.BLACK);

        // Atualiza a câmera do jogo
        jogo.areaVisualizacao.apply();
        jogo.batch.setProjectionMatrix(jogo.areaVisualizacao.getCamera().combined);

        // Inicia o batch de desenho
        jogo.batch.begin();

        // Define a altura e largura com base no tamanho da tela do jogo
        float largura = jogo.areaVisualizacao.getWorldWidth();
        float altura = jogo.areaVisualizacao.getWorldHeight();

        // Desenha o fundo da tela
        jogo.mainStage.draw();
        jogo.batch.draw(fundoTela, 0, 0, largura, altura);
        
    
        // Finaliza o batch de desenho
		jogo.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        jogo.areaVisualizacao.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}
