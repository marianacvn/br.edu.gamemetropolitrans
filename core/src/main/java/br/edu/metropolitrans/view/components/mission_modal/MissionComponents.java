package br.edu.metropolitrans.view.components.mission_modal;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.font.FontBase;

public class MissionComponents {

    public MetropoliTrans jogo;
    public int missaoId;
    public Stage stage;
    public Label titulo;
    public Map<String, ImageButton> botoesImagens;
    public Image imagemCena;

    public MissionComponents(int missaoId, MetropoliTrans jogo) {
        this.jogo = jogo;
        this.missaoId = missaoId;
        this.stage = new Stage();
        botoesImagens = new HashMap<>();
    }

    /**
     * Adiciona o título da missão
     * 
     * @param textoTitulo Texto do título
     * @param x           Posição X
     * @param y           Posição Y
     */
    public void adicionarTituloMissao(String textoTitulo, float x, float y) {
        // Carrega a fonte do título
        BitmapFont fonteTitulo = FontBase.getInstancia().getFonte(30, FontBase.Fontes.PADRAO);

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonteTitulo;
        labelStyle.fontColor = Color.BLACK;
        titulo = new Label(textoTitulo, labelStyle);
        titulo.setPosition(x, stage.getHeight() - titulo.getHeight() - y - 20);

        stage.addActor(titulo);
    }

    /**
     * Adiciona uma opção de botão de imagem
     * 
     * @param nome          Nome
     * @param caminhoImagem Caminho da imagem
     * @param correta       Se é correta resposta correta
     * @param x             Posição X
     * @param y             Posição Y
     * @param geraXp        Se gera XP
     */
    public void adicionarOpcaoImagem(String nome, String caminhoImagem, boolean correta, float x, float y,
            boolean geraXp) {
        // Skin skin = new Skin();
        Texture texture = new Texture(Gdx.files.internal("files/missionComponents/" + caminhoImagem));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(texture);
        ImageButton botaoImagem = new ImageButton(style);

        botaoImagem.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (correta) {
                    jogo.efeitoAcerto.play();

                    // Lógica para resposta correta
                    jogo.controller.resultadoRespostaMissao = 1;
                    jogo.personagem.moedas += jogo.controller.controleMissao.getRecompensaMoedasMissao();

                    if (geraXp)
                        jogo.personagem.xp += 10;

                    jogo.controller.objetoMissao.setVisible(false);
                } else {
                    jogo.efeitoErro.play();
                    
                    // Lógica para resposta incorreta
                    jogo.controller.resultadoRespostaMissao = 2;
                    jogo.personagem.moedas -= jogo.controller.controleMissao.getValorErroMissao();

                }
                return true;
            }
        });

        botaoImagem.setPosition(x, y);
        botoesImagens.put(nome, botaoImagem);
        stage.addActor(botaoImagem);
    }

    /**
     * Adiciona uma imagem de cena
     * 
     * @param caminhoImagem Caminho da imagem
     * @param x             Posição X
     * @param y             Posição Y
     */
    public void adicionarImagemCena(String caminhoImagem, float x, float y) {
        Texture texture = new Texture(Gdx.files.internal("files/missionComponents/" + caminhoImagem));
        imagemCena = new Image(texture);
        imagemCena.setPosition(x, y);
        stage.addActor(imagemCena);
    }
}
