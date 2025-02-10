package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.Course;
import br.edu.metropolitrans.model.Status;
import br.edu.metropolitrans.model.dao.CourseDAO;
import br.edu.metropolitrans.model.utils.DebugMode;
import br.edu.metropolitrans.view.components.buttons.TextButtonBase;
import br.edu.metropolitrans.view.components.buttons.TextButtonSecond;
import br.edu.metropolitrans.view.font.FontBase;

public class CoursesScreen implements Screen {
    public final MetropoliTrans jogo;
    public Stage stage;
    public Skin skin;
    public Label titulo;
    public Screen telaAnterior;
    public TextButtonSecond botao1, botao2, botao3, botao4, botao5, botao6, botao7, botao8, botao9;

    public CoursesScreen(final MetropoliTrans jogo, Screen telaAnterior) {
        this.jogo = jogo;
        this.telaAnterior = telaAnterior;

        // Cria o Stage e o Skin
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        skin.add("default", jogo.fonte);

        // Carrega a fonte para o título
        BitmapFont fonteTitulo = FontBase.getInstancia().getFonte(80, FontBase.Fontes.PADRAO);

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonteTitulo;
        labelStyle.fontColor = Color.valueOf("4c4869");
        titulo = new Label("Sistema de cursos", labelStyle);

        // Centraliza o título na tela
        float tituloWidth = titulo.getWidth();
        float tituloHeight = titulo.getHeight();
        // no meio do topo da tela
        float tituloX = Gdx.graphics.getWidth() / 2 - tituloWidth / 2;
        float tituloY = Gdx.graphics.getHeight() - tituloHeight - 50;
        titulo.setPosition(tituloX, tituloY);

        // Cria os botões
        botao1 = criarBotaoModulo(1, 422, 426, CourseDAO.carregarDadosModulo(1).getStatus());
        botao2 = criarBotaoModulo(2, botao1.getX() + botao1.getWidth() + 20, botao1.getY(),
                CourseDAO.carregarDadosModulo(2).getStatus());
        botao3 = criarBotaoModulo(3, botao2.getX() + botao2.getWidth() + 20, botao2.getY(),
                CourseDAO.carregarDadosModulo(3).getStatus());
        botao4 = criarBotaoModulo(4, botao1.getX(), botao1.getY() - botao1.getHeight() - 15,
                CourseDAO.carregarDadosModulo(4).getStatus());
        botao5 = criarBotaoModulo(5, botao1.getX() + botao1.getWidth() + 20, botao4.getY(),
                CourseDAO.carregarDadosModulo(5).getStatus());
        botao6 = criarBotaoModulo(6, botao2.getX() + botao2.getWidth() + 20, botao5.getY(),
                CourseDAO.carregarDadosModulo(6).getStatus());
        botao7 = criarBotaoModulo(7, botao4.getX(), botao4.getY() - botao4.getHeight() - 15,
                CourseDAO.carregarDadosModulo(7).getStatus());
        botao8 = criarBotaoModulo(8, botao1.getX() + botao1.getWidth() + 20, botao4.getY() - 150,
                CourseDAO.carregarDadosModulo(8).getStatus());
        botao9 = criarBotaoModulo(9, botao2.getX() + botao2.getWidth() + 20, botao5.getY() - 150,
                CourseDAO.carregarDadosModulo(9).getStatus());

        // Cria um botão para fechar a tela e voltar para a anterior
        TextButtonBase botaoFechar = new TextButtonBase("X", "files/buttons/botao-dark2.png", skin);
        botaoFechar.setSize(100, 50); // Define o tamanho do botão
        // posicao no canto superior direito
        botaoFechar.setPosition(Gdx.graphics.getWidth() - botaoFechar.getWidth() - 10,
                Gdx.graphics.getHeight() - botaoFechar.getHeight() - 10);

        // Adiciona um listener para o botão fechar
        botaoFechar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.setScreen(telaAnterior);
            }
        });

        stage.addActor(botaoFechar);
        stage.addActor(titulo);
        stage.addActor(botao1);
        stage.addActor(botao2);
        stage.addActor(botao3);
        stage.addActor(botao4);
        stage.addActor(botao5);
        stage.addActor(botao6);
        stage.addActor(botao7);
        stage.addActor(botao8);
        stage.addActor(botao9);
    }

    private TextButtonSecond criarBotaoModulo(int modulo, float x, float y, Status status) {
        String imagemBotao = "files/buttons/quadrado-block.png";
        String textoModulo = "";
        if (status == Status.LIBERADO || status == Status.CONCLUIDO) {
            imagemBotao = "files/buttons/quadrado.png";
            textoModulo = "Módulo " + modulo;
        }

        TextButtonSecond botao = new TextButtonSecond(textoModulo, imagemBotao, skin);
        botao.setPosition(x, y);
        botao.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Course course = CourseDAO.carregarDadosModulo(modulo);
                // Atualiza o status do curso para concluido e desconta 50 moedas
                if (course.getStatus() == Status.LIBERADO) {
                    jogo.personagem.moedas -= 50;
                    CourseDAO.atualizaStatusCurso(course.getId(), Status.CONCLUIDO);
                }

                if (validaCursoLiberado(course)) {
                    DebugMode.mostrarLog("CoursesScreen",
                            "Dados do Módulo " + modulo + " carregados: " + course.getNome());
                    jogo.setScreen(new CoursePageScreen(jogo, CoursesScreen.this, course));
                }
            }
        });
        return botao;
    }

    /**
     * Atualiza o status do botão
     * 
     * @param botao  Botão a ser atualizado
     * @param modulo Id do Módulo
     */
    private void atualizarBotaoStatus(TextButtonSecond botao, int modulo) {
        Course course = CourseDAO.buscaCursoPorId(modulo);
        if (course != null) {
            String imagemBotao = validaCursoLiberado(course)
                    ? "files/buttons/quadrado.png"
                    : "files/buttons/quadrado-block.png";
            String textoModulo = validaCursoLiberado(course)
                    ? "Módulo " + modulo
                    : "";
            botao.setText(textoModulo);
            botao.setNewImageButton(imagemBotao);
        }
    }

    /**
     * Atualiza o status dos botões
     */
    public void atualizarBotoesStatus() {
        atualizarBotaoStatus(botao1, 1);
        atualizarBotaoStatus(botao2, 2);
        atualizarBotaoStatus(botao3, 3);
        atualizarBotaoStatus(botao4, 4);
        atualizarBotaoStatus(botao5, 5);
        atualizarBotaoStatus(botao6, 6);
        atualizarBotaoStatus(botao7, 7);
        atualizarBotaoStatus(botao8, 8);
        atualizarBotaoStatus(botao9, 9);
    }

    /**
     * Valida se o curso está liberado
     * 
     * @param curso Curso
     * @return true se o curso estiver liberado, false caso contrário
     */
    private boolean validaCursoLiberado(Course curso) {
        if (curso != null
                && (curso.getStatus() == Status.LIBERADO || curso.getStatus() == Status.CONCLUIDO)) {
            DebugMode.mostrarLog("CoursesScreen", "Módulo " + curso.getId() + " bloqueado: " + curso.getNome());
            return true;
        }
        return false;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        // Implement hide logic here
    }

    @Override
    public void pause() {
        // Implement pause logic here
    }

    @Override
    public void resume() {
        // Implement resume logic here
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
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
