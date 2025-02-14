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
import br.edu.metropolitrans.model.utils.DebugMode;
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
    public TextButtonBase botaoConfirmar, botaoCancelar;
    private boolean exibeDialogo;
    public String textoTitulo;
    private String tipoModal;

    public MissionResultDialog(float x, float y, float largura, float altura, MetropoliTrans jogo) {
        this.jogo = jogo;
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.textoTitulo = "";
        this.stage = new Stage();
        this.tipoModal = "default";

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

        // Cria o botão de cancelamento
        botaoCancelar = new TextButtonBase("Cancelar", "files/buttons/botao-dark2.png", skin);
        botaoCancelar.setSize(100, 30);
        botaoCancelar.setPosition(botaoConfirmar.getX() + 20, botaoConfirmar.getY());

        // Adiciona uma ação ao botão Configurações
        botaoConfirmar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tipoModal.equals("default")) {
                    if (jogo.personagem.moedas == 0) {
                        jogo.controller.perdeuJogo = true;
                    }

                    if (jogo.controller.resultadoRespostaMissao == 1) {
                        jogo.controller.resultadoRespostaMissao = 0;
                        jogo.controller.mostrarCaixaMissao = false;
                        jogo.controller.controleMissao.missaoConcluida = true;
                        DebugMode.mostrarLog("DialogoMissao", "Resposta correta!");
                    } else {
                        jogo.controller.resultadoRespostaMissao = 0;
                        DebugMode.mostrarLog("DialogoMissao", "Resposta Incorreta!");
                    }
                } else {
                    // TODO: reposicionar o jogo, posicoes, moedas, etc
                }
            }
        });

        // Adiciona uma ação ao botão Configurações
        botaoCancelar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //jogo.controller.mostrarCaixaMissao = false;
            }
        });

        // Adiciona os atores ao palco
        stage.addActor(botaoConfirmar);
        stage.addActor(botaoCancelar);
        stage.addActor(titulo);
    }

    public void ativarAcao(String tipo, String textoTitulo) {
        // Atualiza os botões de acordo com o tipo do modal
        atualizarBotoes(tipo);

        this.textoTitulo = textoTitulo;
        titulo.setText(textoTitulo);
        exibeDialogo = true;
        Gdx.input.setInputProcessor(stage);
    }

    private void atualizarTextoBotao(String textoConfirmar, String textoCancelar) {
        botaoConfirmar.setText(textoConfirmar);
        botaoCancelar.setText(textoCancelar);
    }

    private void atualizarBotoes(String tipo) {
        this.tipoModal = tipo;
        
        if (tipo.equals("default")) {
            atualizarTextoBotao("OK", "");
            // botaoConfirmar.setPosition(x + largura - 50, y + 120);
            botaoConfirmar.setVisible(true);
            botaoCancelar.setVisible(false);
        } else {
            atualizarTextoBotao("Jogar Novamente", "Sair");
            // botaoConfirmar.setPosition(x + largura - 50, y + 120);
            // botaoCancelar.setPosition(botaoConfirmar.getX() + 20, botaoConfirmar.getY());
            botaoConfirmar.setVisible(true);
            botaoCancelar.setVisible(true);
        }
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
