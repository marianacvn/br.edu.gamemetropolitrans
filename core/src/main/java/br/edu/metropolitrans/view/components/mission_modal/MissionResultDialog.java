package br.edu.metropolitrans.view.components.mission_modal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.components.buttons.TextButtonBase;
import br.edu.metropolitrans.view.font.FontBase;
import br.edu.metropolitrans.view.screens.GameScreen;

public class MissionResultDialog {

    private MetropoliTrans jogo;
    private BitmapFont fonte;
    private float x, y, largura, altura;
    private Texture backgroundTexture;
    public Stage stage;
    public Label titulo;
    public TextButtonBase botaoConfirmar;
    private boolean exibeDialogo;
    public String textoTitulo;

    public MissionResultDialog(float x, float y, float largura, float altura, MetropoliTrans jogo) {
        this.jogo = jogo;
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.textoTitulo = "";
        this.stage = new Stage();

        // Carrega a fonte a ser utilizada
        fonte = FontBase.getInstancia().getFonte(30, FontBase.Fontes.PADRAO);
        backgroundTexture = new Texture(Gdx.files.internal("files/backgrounds/background-light-dialog.png"));

        Skin skin = new Skin();
        skin.add("default", jogo.fonte);

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonte;
        labelStyle.fontColor = Color.BLACK;
        titulo = new Label(textoTitulo, labelStyle);
        titulo.setAlignment(Align.center);
        // Centraliza o título de acordo com o tamanho do dialogo
        titulo.setPosition(x + largura - titulo.getWidth(), y + 240);

        // Cria o botão de confirmação
        botaoConfirmar = new TextButtonBase("OK", "files/buttons/botao-dark2.png", skin);
        botaoConfirmar.setSize(100, 30);
        botaoConfirmar.setPosition(x + largura - 50, y + 120);

        // Adiciona uma ação ao botão Configurações
        botaoConfirmar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (jogo.personagem.moedas == 0) {
                    jogo.controller.perdeuJogo = true;
                }

                if (jogo.controller.resultadoRespostaMissao == 1) {
                    jogo.controller.resultadoRespostaMissao = 0;
                    jogo.controller.mostrarCaixaMissao = false;
                    jogo.controller.controleMissao.missaoConcluida = true;
                    Gdx.app.log("DialogoMissao", "Resposta correta!");
                } else {
                    jogo.controller.resultadoRespostaMissao = 0;
                    Gdx.app.log("DialogoMissao", "Resposta Incorreta!");
                }
            }
        });

        // Adiciona os atores ao palco
        stage.addActor(botaoConfirmar);
        stage.addActor(titulo);
    }

    public void ativarAcao(String textoTitulo) {
        this.textoTitulo = textoTitulo;
        titulo.setText(textoTitulo);
        exibeDialogo = true;
        Gdx.input.setInputProcessor(stage);
    }

    public void desativarAcao() {
        this.textoTitulo = "";
        exibeDialogo = false;
    }

    public void render(float delta) {
        if (exibeDialogo) {
            jogo.batch.begin();

            jogo.batch.draw(
                    backgroundTexture,
                    x + (GameScreen.TELA_LARGURA - largura) / 2,
                    y + (GameScreen.TELA_ALTURA - altura) / 2,
                    largura,
                    altura);

            jogo.batch.end();

            stage.act(delta);
            stage.draw();
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
