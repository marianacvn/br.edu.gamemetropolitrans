package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import br.edu.metropolitrans.model.Course;
import br.edu.metropolitrans.model.utils.DebugMode;
import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.view.components.buttons.TextButtonBase;
import br.edu.metropolitrans.view.font.FontBase;

public class CoursePageScreen implements Screen {
    public MetropoliTrans jogo;
    public Stage stage;
    public Skin skin;
    private Label titulo;
    private Label texto;
    private Image imagem;
    private TextButton linkVideo;

    public CoursePageScreen(MetropoliTrans jogo, CoursesScreen coursesScreen, Course course) {
        this.jogo = jogo;

        // Cria o Stage e o Skin
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        skin.add("default", jogo.fonte);

        // Adiciona um estilo para TextButton
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = jogo.fonte;
        textButtonStyle.fontColor = Color.BLACK;
        this.skin.add("default", textButtonStyle);

        BitmapFont fonteTexto = FontBase.getInstancia().getFonte(40, FontBase.Fontes.PADRAO);
        BitmapFont fonteTitulo = FontBase.getInstancia().getFonte(80, FontBase.Fontes.PADRAO);

        // Cria o título
        Label.LabelStyle labelStyleTitulo = new Label.LabelStyle();
        labelStyleTitulo.font = fonteTitulo;
        labelStyleTitulo.fontColor = Color.BLACK; // Ajuste a cor do texto para preto
        this.titulo = new Label(course.getNome(), labelStyleTitulo);
        this.titulo.setPosition(Gdx.graphics.getWidth() / 2 - this.titulo.getWidth() / 2,
                Gdx.graphics.getHeight() - this.titulo.getHeight() - 50);

        // Cria o texto
        Label.LabelStyle labelStyleTexto = new Label.LabelStyle();
        labelStyleTexto.font = fonteTexto;
        labelStyleTexto.fontColor = Color.BLACK; // Ajuste a cor do texto para preto
        this.texto = new Label(course.getDescricao(), labelStyleTexto);
        // seta a posição do texto com um gap de respiro abaixo do título
        this.texto.setPosition(Gdx.graphics.getWidth() / 2 - this.texto.getWidth() / 2,
                this.titulo.getY() - this.texto.getHeight() - 10);

        // Cria a imagem (opcional)
        if (course.getImagemPath() != null && !course.getImagemPath().isEmpty()) {
            this.imagem = new Image(new Texture(Gdx.files.internal(course.getImagemPath())));
            // SETA A POSIÇÃO IMAGEM PARA ABAIXO DO TEXTO
            this.imagem.setPosition(Gdx.graphics.getWidth() / 2 - this.imagem.getWidth() / 2,
                    this.texto.getY() - this.imagem.getHeight() - 5);
            // Adiciona a imagem ao stage
            stage.addActor(this.imagem);
        } else {
            this.imagem = null;
        }

        // Cria o link para o vídeo
        this.linkVideo = new TextButton("Assista ao vídeo", skin);
        // setar o link do video para abaixo da imagem
        this.linkVideo.setPosition(Gdx.graphics.getWidth() / 2 - this.linkVideo.getWidth() / 2,
                this.imagem.getY() - this.linkVideo.getHeight() - 10);

        this.linkVideo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(course.getVideoUrl());
            }
        });

        // Cria um botão para voltar à tela anterior
        TextButtonBase botaoVoltar = new TextButtonBase("Voltar", "files/buttons/botao-dark2.png", skin);
        botaoVoltar.setSize(100, 50);
        botaoVoltar.setPosition(10, Gdx.graphics.getHeight() - botaoVoltar.getHeight() - 10);
        botaoVoltar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                coursesScreen.atualizarBotoesStatus();
                jogo.setScreen(coursesScreen);
                DebugMode.mostrarLog("CoursePageScreen", "Voltando para a tela de cursos.");
            }
        });

        stage.addActor(this.titulo);
        stage.addActor(this.texto);
        stage.addActor(this.linkVideo);
        stage.addActor(botaoVoltar);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor preta
        ScreenUtils.clear(Color.WHITE);

        // Atualiza e desenha o Stage
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        skin.dispose();
    }

}
