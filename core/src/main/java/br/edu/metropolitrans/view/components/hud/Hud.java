package br.edu.metropolitrans.view.components.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.actors.BasicAnimation;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.view.font.FontBase;

public class Hud {

    private MetropoliTrans jogo;
    private BitmapFont font;

    private float x;
    private float y;
    private Texture xpIcon;
    private Texture moedasIcon;
    private Texture estrelaIcon, estrelaVaziaIcon;
    private ShapeRenderer shapeRenderer;
    private BasicAnimation notificacao;

    public Hud(MetropoliTrans jogo) {
        this.jogo = jogo;

        // Carregar a fonte
        font = FontBase.getInstancia().getFonte(28, new Color(Color.WHITE), FontBase.Fontes.MONOGRAM);

        this.shapeRenderer = new ShapeRenderer();
        // Carregar as texturas dos ícones
        xpIcon = new Texture(Gdx.files.internal("files/itens/xp2.png"));
        moedasIcon = new Texture(Gdx.files.internal("files/itens/moeda.png"));
        estrelaIcon = new Texture(Gdx.files.internal("files/itens/estrela-pintada.png"));
        estrelaVaziaIcon = new Texture(Gdx.files.internal("files/itens/estrela-vazia.png"));

        // Inicia a animação de notificação
        // Intancia animação da notificação
        notificacao = new BasicAnimation(0, 0, jogo.estagioPrincipal);
        String[] nomeArquivosNotificacao = {
                "files/animation/book-animation/frame_00.png",
                "files/animation/book-animation/frame_01.png",
                "files/animation/book-animation/frame_02.png",
                "files/animation/book-animation/frame_03.png",
                "files/animation/book-animation/frame_04.png",
                "files/animation/book-animation/frame_05.png",
                "files/animation/book-animation/frame_06.png",
                "files/animation/book-animation/frame_07.png"
        };
        notificacao.carregaAnimacaoDeArquivos(
                nomeArquivosNotificacao, 0.1f, true);
        notificacao.setVisible(false);
    }

    public void render() {
        if (jogo.controller.notificarLiberacaoModulo) {
            jogo.efeitoNotificacao.play();
            notificacao.setPosition(x + 1170, y + 550);
            notificacao.setVisible(true);

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    notificacao.setVisible(false);
                    jogo.controller.notificarLiberacaoModulo = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        jogo.batch.begin();
        // Desenhar o ícone e o valor de XP
        jogo.batch.draw(xpIcon, x + 1170, y + 650, 80, 20);
        jogo.batch.end();
        // Desenha a linha de XP, com XP 0 é apenas uma bolinha, com XP 100 é uma linha
        // cheia

        // Salvar o estado da matriz de projeção da câmera
        shapeRenderer.setProjectionMatrix(jogo.batch.getProjectionMatrix());

        // Desenhar a linha de XP
        float xpPercent = jogo.personagem.xp / 100.0f; // Supondo que o XP máximo é 100
        float xpBarWidth = 100 * xpPercent; // Largura da barra de XP

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);

        if (xpPercent == 0) {
            // Desenha um quadrado quando o XP é 0
            shapeRenderer.rect(x + 1170 + 4, y + 655, 10, 10);
        } else {
            // Desenha a barra de XP quando o XP é maior que 0
            shapeRenderer.rect(x + 1170 + 4, y + 655, xpBarWidth, 10);
        }

        shapeRenderer.end();

        jogo.batch.begin();
        // Desenhar o ícone e o valor de Moedas
        jogo.batch.draw(moedasIcon, x + 1170, y + 650 - 30, 20, 20);
        // Desenhar o valor de Moedas em cima do ícone
        font.draw(jogo.batch, String.valueOf(jogo.personagem.moedas), x + 1170 + 25, y + 650 - 15);
        // Desenha as quatro estrelas lado a lado
        // Verifica quantas infrações o personagem sofreu
        // Ex.: se sofreu 1, desenha uma estrela vazia e as outras cheias
        if (jogo.personagem.tipoInfracao != null && jogo.personagem.tipoInfracao == Personagem.TipoInfracao.ALERTA) {
            for (int i = 0; i < 4; i++) {
                jogo.batch.draw(estrelaIcon, x + 1170 + 25 * i, y + 650 - 60, 20, 20);
            }
        } else {
            for (int i = 0; i < jogo.personagem.infracoes; i++) {
                jogo.batch.draw(estrelaVaziaIcon, x + 1170 + 25 * i, y + 650 - 60, 20, 20);
            }

            for (int i = jogo.personagem.infracoes; i < 4; i++) {
                jogo.batch.draw(estrelaIcon, x + 1170 + 25 * i, y + 650 - 60, 20, 20);
            }
        }

        jogo.batch.end();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void dispose() {
        font.dispose();
        xpIcon.dispose();
        moedasIcon.dispose();
        shapeRenderer.dispose();
    }
}
