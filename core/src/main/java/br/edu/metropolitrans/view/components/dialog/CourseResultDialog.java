package br.edu.metropolitrans.view.components.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.Course;
import br.edu.metropolitrans.model.utils.DebugMode;
import br.edu.metropolitrans.view.components.buttons.TextButtonBase;
import br.edu.metropolitrans.view.font.FontBase;
import br.edu.metropolitrans.view.font.FontBase.Fontes;
import br.edu.metropolitrans.view.screens.CoursePageScreen;
import br.edu.metropolitrans.view.screens.CoursesScreen;

public class CourseResultDialog {

    private BitmapFont fonte;
    private Texture backgroundTexture;
    private Texture moedasIconTexture;
    private Image background;
    private Image moedasIcon;
    public Stage stage;
    public Label titulo;
    public TextButtonBase botaoConfirmar;
    public boolean exibeDialogo;
    public String textoTitulo;

    private CoursesScreen coursesScreen;
    private Course course;

    public CourseResultDialog(float x, float y, float largura, float altura, MetropoliTrans jogo) {
        this.textoTitulo = "";
        this.stage = new Stage();

        // Carrega a fonte a ser utilizada
        fonte = FontBase.getInstancia().getFonte(30, FontBase.Fontes.PADRAO);
        backgroundTexture = new Texture(Gdx.files.internal("files/backgrounds/background-light-dialog.png"));
        this.background = new Image(backgroundTexture);

        moedasIconTexture = new Texture(Gdx.files.internal("files/itens/moeda.png"));
        this.moedasIcon = new Image(moedasIconTexture);

        Skin skin = new Skin();
        skin.add("default", jogo.fonte);
        skin.add("default_small", FontBase.getInstancia().getFonte(16, Fontes.PADRAO));

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonte;
        labelStyle.fontColor = Color.BLACK;
        titulo = new Label(textoTitulo, labelStyle);
        titulo.setAlignment(Align.center);
        // Centraliza o título de acordo com o tamanho do dialogo
        titulo.setPosition(x + largura - titulo.getWidth(), y + 400);

        // Cria o botão de confirmação
        botaoConfirmar = new TextButtonBase("OK", "files/buttons/botao-dark2.png", skin);
        botaoConfirmar.setSize(100, 30);
        botaoConfirmar.setPosition(x + largura - 50, y + 270);

        // Informa a posicao e tamanho do background
        this.background.setSize(350, 250);
        this.background.setPosition(x + largura - 180, y + 270 - 50);
        stage.addActor(this.background);

        this.moedasIcon.setSize(20, 20);
        this.moedasIcon.setPosition(x + largura - 10, y + 320);
        stage.addActor(this.moedasIcon);

        // Adiciona uma ação ao botão Configurações
        botaoConfirmar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.efeitoConfirmar.play();
                desativarAcao();

                // Lança a tela do módulo desbloqueado (compra com moedas)
                if (coursesScreen != null && course != null) {
                    DebugMode.mostrarLog("CoursesScreen", "Curso liberado, carregando CoursePageScreen");
                    DebugMode.mostrarLog("CoursesScreen",
                            "Dados do Módulo " + course.getId() + " carregados: " + course.getNome());
                    jogo.setScreen(new CoursePageScreen(jogo, coursesScreen, course));
                }
            }
        });

        // Adiciona os atores ao palco
        stage.addActor(botaoConfirmar);
        stage.addActor(titulo);
    }

    public void ativarAcao(CoursesScreen coursesScreen, Course course, String textoTitulo) {
        this.coursesScreen = coursesScreen;
        this.course = course;

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
            stage.act(delta);
            stage.draw();
        }
    }
}
