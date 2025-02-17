package br.edu.metropolitrans.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.model.utils.DebugMode;
import br.edu.metropolitrans.view.components.dialog.DialogBox;
import br.edu.metropolitrans.view.components.hud.Hud;
import br.edu.metropolitrans.view.components.minimap.Minimap;
import br.edu.metropolitrans.view.components.mission_alert.MissionAlert;
import br.edu.metropolitrans.view.components.mission_modal.MissionModalBox;
import br.edu.metropolitrans.view.components.mission_modal.MissionResultDialog;
import br.edu.metropolitrans.view.components.phone.Phone;

/**
 * Tela principal do jogo
 */
public class GameScreen implements Screen {

    // Largura e altura da tela do jogo
    public static final float TELA_LARGURA = 1280;
    public static final float TELA_ALTURA = 720;
    /**
     * Referência para o jogo principal
     */
    final MetropoliTrans jogo;
    /**
     * Câmera do jogo
     */
    public final OrthographicCamera CAMERA;
    /**
     * Renderizador de formas/objetos
     */
    public final ShapeRenderer renderizadorForma;
    /**
     * Temporizador para acompanhar o tempo de jogo
     */
    float temporizador = 0f;
    /**
     * Caixa de diálogo
     */
    public DialogBox caixaDialogo;
    /**
     * Alerta de missão
     */
    public MissionAlert alertaMissao;
    /**
     * Caixa modal de missão
     */
    public MissionModalBox missaoModalBox;
    /**
     * Diálogo de resultado de missão
     */
    public MissionResultDialog missaoDialogoResultado;
    /**
     * Minimapa
     */
    public Minimap minimapa;
    /**
     * Batch de desenho
     */
    public SpriteBatch batch;

    /** Xp e Moedas */
    public Hud hud;

    /* Telefone dos cursos */
    public Phone phone;

    public GameScreen(final MetropoliTrans jogo) {
        this.jogo = jogo;

        // Inicializa o renderizador de formas
        renderizadorForma = new ShapeRenderer();

        batch = new SpriteBatch();

        // Define a camera do jogo
        CAMERA = new OrthographicCamera(TELA_LARGURA, TELA_ALTURA);
        CAMERA.setToOrtho(false, TELA_LARGURA, TELA_ALTURA);
        CAMERA.update();

        // Inicializar a HUD
        hud = new Hud(jogo);

        // Inicializar o minimapa
        minimapa = new Minimap(1170, 200, jogo);

        // Inicializar o telefone
        phone = new Phone(1170, 420, jogo);

        // Inicializa a caixa de diálogo
        caixaDialogo = new DialogBox(0, 64, 1280, 150, jogo);

        // Inicializa o alerta de missão
        alertaMissao = new MissionAlert(jogo.batch);

        // Inicializa a caixa modal de missão no centro da tela
        missaoModalBox = new MissionModalBox(TELA_LARGURA / 2 - 550, TELA_ALTURA / 2 - 400, 550, 400, jogo);

        // Inicializa o diálogo de resultado de missão
        missaoDialogoResultado = new MissionResultDialog(TELA_LARGURA / 2 - 250, TELA_ALTURA / 2 - 200, 250, 200, jogo);

    }

    @Override
    public void show() {
        jogo.musicaMenu.stop();
    }

    @Override
    public void render(float delta) {
        // Desenha os elementos do jogo
        desenhar();

        // Controlers
        controllers();

        // Renderiza a caixa dialogo, minimapa e alertas de missao
        desenharComponentes(delta);

        // Testes
        DebugMode.debugUI(this, jogo);
    }

    public void controllers() {
        // Controle da interação do personagem com os objetos do mapa
        jogo.controller.controleInteracao();

        // Controle da tela de configurações
        jogo.controller.controleTelaConfig();

        // Controle de diálogos
        jogo.controller.controleDialogos();

        // Controle de Infrações
        jogo.controller.controleInfracao();

        // Controle de missões
        jogo.controleMissao.controle(jogo.controller.MISSAO);

        // // Controle de cursos
        jogo.controller.controleCursos();

        // Verifica se a caixa de diálogo deve ser exibida
        // Se sim, exibe a caixa de diálogo, caso contrário permite
        // o controle do personagem continuando o jogo
        if (!jogo.controller.mostrarDialogo && !jogo.controller.mostrarCaixaMissao && !validaAnimacaoNpcAtiva()
                && !missaoDialogoResultado.exibeDialogo) {
            // Controle do personagem Setas ou WASD
            jogo.controller.controlePersonagemSetas();
            jogo.controller.controlePersonagemWASD();
            jogo.controller.controleTelefone();
        }
    }

    /**
     * Desenha a tela do jogo
     */
    private void desenhar() {
        // Atualiza o temporizador
        float dt = Gdx.graphics.getDeltaTime();
        temporizador += dt;

        // Limpa a tela com uma cor preta
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Alinhamento da câmera do jogo
        alinhamentoCamera();
        jogo.mapaRenderizador.setView(CAMERA);

        // Renderiza o mapa e suas camadas Ex: piso e colisão
        // Verifica se o objeto de entrar existe, se sim, deve criar um vetor com a
        // quantidade de camadas
        // Obs.: Caso as camadas mudem no tiled, deve-se alterar aqui também
        int[] camadas = null;
        if (jogo.objetosInterativos.get("objeto") != null) {
            camadas = new int[] { 0, 1, 2, 3, 4, 5 };
        } else {
            camadas = new int[] { 0, 1, 2 };
        }
        jogo.mapaRenderizador.render(camadas);

        // A camada 1 é um sobrepiso da missão 4, por padrão ela é ouculta,
        // mas quando a missão é finalizada, ela é exibida
        if (jogo.controller.MISSAO >= 4 && jogo.controleMissao.ativaCamadaMissao4) {
            jogo.mapaRenderizador.getMap().getLayers().get(1).setVisible(true);
        }

        // Inicia o batch de desenho
        jogo.batch.begin();

        // Rendereiza as formas
        renderizadorForma.setProjectionMatrix(CAMERA.combined);
        jogo.batch.setProjectionMatrix(CAMERA.combined);

        // Realiza açoes do estagio principal
        // Desenha o fundo da tela
        jogo.estagioPrincipal.act(dt);
        jogo.estagioPrincipal.draw();

        // Finaliza o batch de desenho
        jogo.batch.end();

        // Renderiza a camada de Topo
        if (jogo.objetosInterativos.get("objeto") != null) {
            jogo.mapaRenderizador.render(new int[] { 6 }); // Topo
        } else {
            jogo.mapaRenderizador.render(new int[] { 3 }); // Topo
        }

    }

    /**
     * Desenha os componentes do jogo
     */
    private void desenharComponentes(float delta) {
        // Atualiza a posição da caixa de diálogo para acompanhar a câmera
        caixaDialogo.setPosition(CAMERA.position.x - CAMERA.viewportWidth / 2,
                CAMERA.position.y - CAMERA.viewportHeight / 2);
        if (jogo.controller.mostrarDialogo) {
            caixaDialogo.render();
        }

        hud.setPosition(CAMERA.position.x - CAMERA.viewportWidth / 2,
                CAMERA.position.y - CAMERA.viewportHeight / 2);
        hud.render();

        // Atualiza a posição do minimapa para acompanhar a câmera
        minimapa.setPosition(CAMERA.position.x - CAMERA.viewportWidth / 2 + 1070,
                CAMERA.position.y - CAMERA.viewportHeight / 2 + 10);
        if (!jogo.controller.mostrarDialogo)
            minimapa.render(jogo.personagem);

        // Desenha o alerta de missão acima da posição do NPC
        if (jogo.personagem.npcs != null) {
            jogo.personagem.npcs.forEach((nome, npc) -> {
                alertaMissao.x = npc.getX();
                alertaMissao.y = npc.getY();
                alertaMissao.status = npc.statusAlertaMissao;
                alertaMissao.render();
            });
        }

        // Atualiza a posição do telefone para acompanhar a câmera
        phone.setPosition(CAMERA.position.x - CAMERA.viewportWidth / 2 + 591,
                CAMERA.position.y - CAMERA.viewportHeight / 2 + 220);
        phone.render();

        // Atualiza a posição da caixa modal de missão para acompanhar a câmera
        missaoModalBox.setPosition(CAMERA.position.x - CAMERA.viewportWidth / 2,
                CAMERA.position.y - CAMERA.viewportHeight / 2);

        // Desenha a caixa modal de missão caso a flag esteja ativada
        if (jogo.controller.mostrarCaixaMissao) {
            missaoModalBox.render(delta);
        }

        // Atualiza a posição do diálogo de resultado de missão para acompanhar a câmera
        missaoDialogoResultado.setPosition(CAMERA.position.x - CAMERA.viewportWidth / 2,
                CAMERA.position.y - CAMERA.viewportHeight / 2);

        // Desenha o diálogo de resultado de missão caso a flag esteja ativada
        if (jogo.controller.ganhouJogo) {
            missaoDialogoResultado.ativarAcao(false,
                    "Parabéns, você\r\nganhou o jogo!\r\nDeseja jogar novamente?");
        } else if (jogo.controller.perdeuJogo) {
            missaoDialogoResultado.ativarAcao(false, "Desculpe, você\r\nperdeu o jogo!");
        } else if (jogo.controller.resultadoRespostaMissao == 1) {
            missaoDialogoResultado.ativarAcao(true, "Parabéns, você\r\nconcluiu a missão!");
        } else if (jogo.controller.resultadoRespostaMissao == 2) {
            missaoDialogoResultado.ativarAcao(true, "Desculpe, você\r\nnão acertou, tente\r\nnovamente!");
        } else {
            missaoDialogoResultado.desativarAcao();
        }

        missaoDialogoResultado.render(delta);
    }

    /**
     * Alinha a câmera do jogo
     */
    public void alinhamentoCamera() {
        // Atualiza a câmera do jogo
        CAMERA.position.set(jogo.personagem.getX() + jogo.personagem.getOriginX(),
                jogo.personagem.getY() + jogo.personagem.getOriginY(),
                0);

        // Calcular os limites da câmera
        float minimoX = CAMERA.viewportWidth / 2;
        float maximoX = Personagem.getLimitacaoMundo().width - minimoX;
        float minimoY = CAMERA.viewportHeight / 2;
        float maximoY = Personagem.getLimitacaoMundo().height - minimoY;

        // Clampar a posição da câmera para garantir que ela não ultrapasse os limites
        // do mundo
        CAMERA.position.x = MathUtils.clamp(CAMERA.position.x, minimoX, maximoX);
        CAMERA.position.y = MathUtils.clamp(CAMERA.position.y, minimoY, maximoY);

        // Atualiza a câmera
        CAMERA.update();
    }

    public boolean validaAnimacaoNpcAtiva() {
        if (caixaDialogo.npc != null && caixaDialogo.npc.animacaoAtivada) {
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        jogo.estagioPrincipal.getViewport().update(width, height, true);
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
        jogo.mapaRenderizador.dispose();
    }

}
