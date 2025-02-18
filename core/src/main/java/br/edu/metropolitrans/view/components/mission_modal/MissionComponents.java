package br.edu.metropolitrans.view.components.mission_modal;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.font.FontBase;

public class MissionComponents {

    public MetropoliTrans jogo;
    public int missaoId;
    public Stage stage;
    public Label titulo;
    public Map<String, CheckBox> botoesImagens;
    public Image imagemCena;

    private ButtonGroup<CheckBox> grupoBotoes;
    private Texture defaultButtonTexture;
    private Texture defaultButtonDownTexture;
    private Texture checkOnTexture;
    private Texture checkOffTexture;
    private BitmapFont defaultFont;

    public MissionComponents(int missaoId, MetropoliTrans jogo) {
        this.jogo = jogo;
        this.missaoId = missaoId;
        this.stage = new Stage();

        // Inicializa as imagens e os botões
        botoesImagens = new HashMap<>();

        // Inicializa o grupo de botões
        grupoBotoes = new ButtonGroup<>();
        // grupoBotoes.setChecked();

        // Carrega as texturas e a fonte padrão
        defaultButtonTexture = new Texture(Gdx.files.internal("files/buttons/botao-dark2.png"));
        defaultButtonDownTexture = new Texture(Gdx.files.internal("files/buttons/botao-dark2_pressed.png"));
        checkOnTexture = new Texture(Gdx.files.internal("files/buttons/radio_button/check-on.png"));
        checkOffTexture = new Texture(Gdx.files.internal("files/buttons/radio_button/default-round.png"));
        defaultFont = FontBase.getInstancia().getFonte(25, FontBase.Fontes.PADRAO);
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
        // Carregar a textura da imagem da resposta
        Texture texture = new Texture(Gdx.files.internal("files/missionComponents/" + caminhoImagem));
        Image imagemResposta = new Image(texture);
        imagemResposta.setPosition(x + 35, y);

        // Criar o estilo do checkbox
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.checkboxOn = new TextureRegionDrawable(new TextureRegion(checkOnTexture));
        style.checkboxOff = new TextureRegionDrawable(new TextureRegion(checkOffTexture));
        style.font = defaultFont;

        // Criar o checkbox
        CheckBox botaoImagem = new CheckBox("", style);
        botaoImagem.setUserObject(correta);
        botaoImagem.setPosition(x, y);
        botaoImagem.setChecked(false);

        // Adicionar o checkbox e a imagem ao mapa e ao grupo de botões
        botoesImagens.put(nome, botaoImagem);
        grupoBotoes.add(botaoImagem);

        // Adicionar o checkbox e a imagem ao stage
        stage.addActor(botaoImagem);
        stage.addActor(imagemResposta);

        // Desmarcar todos os botões no grupo
        grupoBotoes.uncheckAll();
    }

    public void adicionarBotaoConfirmar(float x, float y, boolean geraXp) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(defaultButtonTexture));
        style.down = new TextureRegionDrawable(new TextureRegion(defaultButtonDownTexture));
        style.font = defaultFont;

        TextButton botaoConfirmar = new TextButton("Confirmar", style);
        botaoConfirmar.setSize(100, 30);
        botaoConfirmar.setPosition(x, y);

        botaoConfirmar.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                CheckBox selecionado = grupoBotoes.getChecked();
                if (selecionado != null) {
                    boolean correta = (boolean) selecionado.getUserObject();
                    if (correta) {
                        jogo.efeitoAcerto.play();

                        // Lógica para resposta correta
                        jogo.controller.resultadoRespostaMissao = 1;
                        jogo.personagem.moedas += jogo.controleMissao.getRecompensaMoedasMissao();

                        if (geraXp)
                            jogo.personagem.xp += 10;

                            jogo.objetosInterativos.get("objetoMissao").setVisible(false);
                    } else {
                        jogo.efeitoErro.play();

                        // Lógica para resposta incorreta
                        jogo.controller.resultadoRespostaMissao = 2;
                        jogo.personagem.moedas -= jogo.controleMissao.getValorErroMissao();
                    }
                }
                return true;
            }
        });

        stage.addActor(botaoConfirmar);
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
        imagemCena.setSize(200, 200);
        stage.addActor(imagemCena);
    }
}
