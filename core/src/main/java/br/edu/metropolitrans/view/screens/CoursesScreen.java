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
import br.edu.metropolitrans.view.components.dialog.CourseResultDialog;
import br.edu.metropolitrans.view.components.mission_modal.MissionResultDialog;
import br.edu.metropolitrans.view.font.FontBase;

public class CoursesScreen implements Screen {
    public final MetropoliTrans jogo;
    public Stage stage;
    public Skin skin;
    public Label titulo;
    public Screen telaAnterior;
    public TextButtonSecond botao1, botao2, botao3, botao4, botao5, botao6, botao7, botao8;

    /**
     * Diálogo de resultado da compra do curso
     */
    public CourseResultDialog dialogoResultado;

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
        titulo = new Label("Sistema de Cursos", labelStyle);

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
                jogo.efeitoConfirmar.play();
                jogo.setScreen(telaAnterior);
            }
        });

        // Inicializa o diálogo de resultado de missão
        dialogoResultado = new CourseResultDialog(GameScreen.TELA_LARGURA / 2 - 300, GameScreen.TELA_ALTURA / 2 - 300,
        300,
        300, jogo);

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
    }

    private TextButtonSecond criarBotaoModulo(int modulo, float x, float y, Status status) {
        String imagemBotao = "files/buttons/quadrado-block.png";
        String textoModulo = "";
        if (status == Status.LIBERADO) {
            imagemBotao = "files/buttons/quadrado.png";
            textoModulo = "Módulo " + modulo;
        } else if (status == Status.CONCLUIDO) {
            imagemBotao = "files/buttons/quadrado-concluido.png";
            textoModulo = "Módulo " + modulo;
        }

        TextButtonSecond botao = new TextButtonSecond(textoModulo, imagemBotao, skin);
        botao.setPosition(x, y);
        botao.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogo.efeitoConfirmar.play();

                Course course = CourseDAO.carregarDadosModulo(modulo);
                DebugMode.mostrarLog("CoursesScreen", "Botão " + modulo + " clicado, curso:" + course);

                // Atualiza o status do curso para concluido e desconta 50 moedas
                if (course.getStatus() == Status.LIBERADO) {
                    jogo.efeitoMoeda.play();
                    jogo.personagem.moedas -= 50;
                    CourseDAO.atualizaStatusCurso(course.getId(), Status.CONCLUIDO);
                }

                // Recarrega os dados do curso
                course = CourseDAO.carregarDadosModulo(modulo);
                DebugMode.mostrarLog("CoursesScreen", "Modulo " + modulo + " recarregado, curso:" + course);

                if (validaCursoLiberado(course)) {
                    // Antes de exibr a tela, exibe um modal de resultado informando a compra do
                    // módulo
                    dialogoResultado.ativarAcao(CoursesScreen.this, course,
                            "Módulo " + course.getId() + " comprado com sucesso!\r\nMoedas: -50.\r\nVocê tem "
                                    + jogo.personagem.moedas + " moedas restantes.");
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
            String imagemBotao = "files/buttons/quadrado-block.png";
            String textoModulo = "";
            if (course.getStatus() == Status.LIBERADO) {
                imagemBotao = "files/buttons/quadrado.png";
                textoModulo = "Módulo " + modulo;
            } else if (course.getStatus() == Status.CONCLUIDO) {
                imagemBotao = "files/buttons/quadrado-concluido.png";
                textoModulo = "Módulo " + modulo;
            }

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

        dialogoResultado.render(delta);
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
