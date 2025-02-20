package br.edu.metropolitrans.controller;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.Course;
import br.edu.metropolitrans.model.Dialog;
import br.edu.metropolitrans.model.Status;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.ObjetoInterativo;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.model.dao.CourseDAO;
import br.edu.metropolitrans.model.dao.DialogDAO;
import br.edu.metropolitrans.model.dao.MissionDataDAO;
import br.edu.metropolitrans.model.utils.DebugMode;
import br.edu.metropolitrans.view.screens.ConfigScreen;
import br.edu.metropolitrans.view.screens.CoursesScreen;

public class Controller {

    /**
     * Classe principal do Jogo
     */
    private MetropoliTrans jogo;

    /**
     * Informa ao renderizador quantos pixels correspondem a uma unidade do mundo
     */
    float unitScale = 1;

    /**
     * Missão atual
     */
    public int MISSAO;

    /**
     * Flag para mostrar a caixa de diálogo
     */
    public boolean mostrarDialogo;

    /**
     * Flag para mostrar a caixa de missão
     */
    public boolean mostrarCaixaMissao;

    /**
     * Flag para acertar a missão
     * - 0: Não respondeu
     * - 1: Acertou
     * - 2: Errou
     */
    public int resultadoRespostaMissao;

    /**
     * Flag para perdeu o jogo
     */
    public boolean perdeuJogo;

    /**
     * Flag para ganhou o jogo
     */
    public boolean ganhouJogo;

    public int qtdEntrouPrefeiura;

    public boolean interagiuComPc;

    public boolean phoneIsVisible;

    public boolean minimapaIsVisible;

    public boolean notificarLiberacaoModulo;

    /**
     * NPC do guarda de trânsito
     */
    public Npc guarda, betania, npcDialogoAtual;
    public String textoDialogoAtual;

    public Controller(MetropoliTrans jogo) {
        this.jogo = jogo;

        // Carrega os objetos de colisão
        montarColisao(jogo.mapas.mapa);
        montarCamadaPista(false);
    }

    public void inicializar() {
        mostrarDialogo = false;
        mostrarCaixaMissao = false;
        resultadoRespostaMissao = 0;
        perdeuJogo = false;
        ganhouJogo = false;
        qtdEntrouPrefeiura = 0;
        interagiuComPc = false;
        phoneIsVisible = false;
        minimapaIsVisible = true;
        notificarLiberacaoModulo = false;

        guarda = new Npc("guarda", jogo.estagioPrincipal);
        betania = new Npc("betania", jogo.estagioPrincipal);

        // Inicializa o diálogo do guarda no início do jogo
        atualizarDialogo(guarda, Npc.DIALOGO_INICIAL);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                jogo.controller.mostrarDialogo = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void atualizarDialogo(Npc npc, String texto) {
        npcDialogoAtual = npc;
        textoDialogoAtual = texto;
    }

    /**
     * Controle do personagem, movimenta de acordo com as teclas pressionadas (WSAD)
     *
     * @param delta
     */
    public void controlePersonagemWASD() {
        boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);

        controlePersonagem(up, down, left, right);
    }

    private void controlePersonagem(boolean up, boolean down, boolean left, boolean right) {
        int teclas = 0;
        if (up)
            teclas++;
        if (down)
            teclas++;
        if (left)
            teclas++;
        if (right)
            teclas++;

        // Verifica se mais de duas tecas estão pressionadas
        // Se sim, não faz nada
        if (teclas > 1 || (up && down) || (left && right)) {
            return;
        }

        if (up) {
            jogo.personagem.acelerarEmAngulo(90);
        } else if (down) {
            jogo.personagem.acelerarEmAngulo(270);
        } else if (left) {
            jogo.personagem.acelerarEmAngulo(180);
        } else if (right) {
            jogo.personagem.acelerarEmAngulo(0);
        }
    }

    public void controleTelefone() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            // Informa que o celular está visível
            phoneIsVisible = true;

            // Agenda a transição para a tela CoursesScreen após 1 segundo
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    phoneIsVisible = false;
                    if (jogo.telas.get("courses") == null)
                        jogo.telas.put("courses", new CoursesScreen(jogo, jogo.getScreen()));

                    jogo.setScreen(jogo.telas.get("courses"));
                }
            }, 1); // 1 segundo
        }
    }

    /**
     * Controle da tela de configurações
     */
    public void controleTelaConfig() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            jogo.telas.put("config", new ConfigScreen(jogo, jogo.telas.get("game")));
            jogo.setScreen(jogo.telas.get("config"));
        }
    }

    /**
     * Verifica a interação do personagem com os objetos do mapa
     */
    public void controleInteracao() {
        if (jogo.objetosInterativos.get("objeto") != null && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jogo.npcs.forEach((nome, npc) -> {
                interacaoComNpc(npc);
            });
        }

        if (jogo.objetosInterativos.get("objeto") != null && jogo.objetosInterativos.get("objetoMissao") != null
                && jogo.personagem.interagiu(jogo.objetosInterativos.get("objetoMissao"))
                && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            mostrarCaixaMissao = true;
        }

        if (jogo.objetosInterativos.get("objeto") != null
                && jogo.personagem.interagiu(jogo.objetosInterativos.get("objeto"))
                && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // Remove o objeto da missão da sala e minimapa
            jogo.objetosInterativos.get("objetoMissao").setVisible(false);

            // Ajusta para remover o objeto da missão 1 da sala
            if (MISSAO >= 1) {
                jogo.objetosInterativos.get("objetoPlaca1").setVisible(false);
            }

            minimapaIsVisible = false;
            jogo.npcs.forEach((nome, npc) -> {
                npc.remove();
            });

            // Muda o mapa para room.tmx
            jogo.mapaRenderizador.dispose();
            jogo.mapaRenderizador = new OrthogonalTiledMapRenderer(jogo.mapas.sala, unitScale, jogo.batch);

            // Salva a última posicao e seta posição do personagem para a entrada
            jogo.personagem.setPosition(1248, 1000);
            jogo.objetosInterativos.put("objetoSairSala",
                    new ObjetoInterativo("sairSala", 1216, 964, "background-transparent.png",
                            jogo.estagioPrincipal));
            jogo.objetosInterativos.put("objeto", null);

            // Carrega os objetos de colisão
            montarColisao(jogo.mapas.sala);
            montarCamadaPista(true);

            // Renova os retângulos de colisão
            jogo.personagem.npcs = new HashMap<>();
            jogo.personagem.setRetangulosColisao(jogo.retangulosColisao);
            jogo.personagem.setRetangulosPista(jogo.retangulosPista);
        } else if (jogo.objetosInterativos.get("objetoSairSala") != null
                && jogo.personagem.interagiu(jogo.objetosInterativos.get("objetoSairSala"))
                && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // coloca o objeto da missão ao sair da sala e minimapa
            jogo.objetosInterativos.get("objetoMissao").setVisible(true);
            qtdEntrouPrefeiura++;
            DebugMode.mostrarLog("Controller", "QtdEntrouPrefeiura: " + qtdEntrouPrefeiura);

            // Avisa para o mission controller que o guarda pode aparecer em frente a
            // prefeitura
            if (MISSAO == 1 && qtdEntrouPrefeiura == 1) {
                jogo.controleMissao.missao1GuardaAparece = true;
            }

            // Ajusta para remover o objeto da missão 1 da sala
            if (MISSAO > 1) {
                jogo.objetosInterativos.get("objetoPlaca1").setVisible(true);
            }

            minimapaIsVisible = true;
            // Adiciona os NPCs no array
            jogo.personagem.npcs = jogo.npcs;
            jogo.npcs.forEach((nome, npc) -> {
                npc.adicionarNoEstagio(jogo.estagioPrincipal);
            });

            // Muda o mapa para map.tmx
            jogo.mapaRenderizador.dispose();
            jogo.mapaRenderizador = new OrthogonalTiledMapRenderer(jogo.mapas.mapa, unitScale, jogo.batch);

            // Ajusta para o personagem sair da sala mas longe do objeto de entrada da
            // prefeitura

            // setar posição do personagem para a entrada
            jogo.personagem.setPosition(100, 690);
            jogo.objetosInterativos.put("objeto",
                    new ObjetoInterativo("entradaPrefeitura", 100, 760, "background-transparent.png",
                            jogo.estagioPrincipal));
            jogo.objetosInterativos.put("objetoSairSala", null);

            // Carrega os objetos de colisão
            montarColisao(jogo.mapas.mapa);
            montarCamadaPista(false);

            // Renova os retângulos de colisão e os npcs
            jogo.personagem.npcs = jogo.npcs;
            jogo.personagem.setRetangulosColisao(jogo.retangulosColisao);
            jogo.personagem.setRetangulosPista(jogo.retangulosPista);
        } else if (jogo.objetosInterativos.get("objetoPc") != null
                && jogo.personagem.interagiu(jogo.objetosInterativos.get("objetoPc"))
                && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            DebugMode.mostrarLog("Controller", "Interagindo com o PC...");
            interagiuComPc = true;

            // abre a tela de CoursesScreen
            if (jogo.telas.get("courses") == null)
                jogo.telas.put("courses", new CoursesScreen(jogo, jogo.getScreen()));

            jogo.setScreen(jogo.telas.get("courses"));
        }
    }

    /**
     * Controle de diálogos
     */
    public void controleDialogos() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && mostrarDialogo) {
            mostrarDialogo = false;
            if (jogo.personagem.tipoInfracao != null) {
                atualizaInfracao();
            } else {
                jogo.npcs.forEach((nome, npc) -> {
                    atualizaStatusAlertaMissaoNpc(npc);
                });
            }
        }
    }

    /**
     * Controle de compra e liberação de cursos
     * 
     * Regras: - O personagem só pode comprar um módulo se tiver moedas suficientes
     * (min 50 por módulo)
     * - O primeiro módulo é liberado automaticamente, quando personagem conclui,
     * desconta 50 moedas
     * - O segundo módulo é liberado assim que o primeiro for concluído, desconta 50
     * moedas
     * - A partir do terceiro módulo, só será liberado quando a missão anterior a
     * referente a ele for concluída
     * - O personagem só pode liberar um novo módulo se tiver assistido o módulo
     * anterior e a missão referente ao módulo anterior
     */
    public void controleCursos() {
        // Verifica se os dois
        // primieros módulos foram concluídos, pois Betânia precisa aparecer na sala
        if (interagiuComPc && MISSAO == 1
                && CourseDAO.listarCursosPorMissaoId(MISSAO).get(0).getStatus() == Status.CONCLUIDO
                && CourseDAO.listarCursosPorMissaoId(MISSAO).get(1).getStatus() == Status.CONCLUIDO) {
            // Adiciona Betânia na sala
            DebugMode.mostrarLog("Controller", "Módulos concluídos, Betânia aparece na sala...");
            jogo.npcs.get("betania").setPosition(1146, 1438);
            jogo.npcs.get("betania").adicionarNoEstagio(jogo.estagioPrincipal);
            jogo.personagem.npcs.put("betania", jogo.npcs.get("betania"));

            // Monta o diálogo de Betânia informando o personagem para colocar em prática o
            // que aprendeu
            atualizarDialogo(betania, Npc.DIALOGO_BETANIA_APLICAR_PRATICA);

            // Aguara 1 segundo para mostrar o diálogo
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    jogo.controller.mostrarDialogo = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            interagiuComPc = false;
        }

        // Liberar módulos dos cursos
        if (MISSAO > 1) {
            // Verifica se o módulo anterior foi concluído e libera 2 Módulos
            List<Course> cursos = CourseDAO.listarCursosPorMissaoId(MISSAO);
            List<Course> cursosProximaMissao = CourseDAO.listarCursosPorMissaoId(MISSAO + 1);
            if (MissionDataDAO.buscaMissaoPorId(MISSAO - 1).isFinalizouMissao()
                    && cursos.get(0).getStatus() == Status.BLOQUEADO) {
                notificarLiberacaoModulo = true;

                CourseDAO.atualizaStatusCurso(cursos.get(0).getId(), Status.LIBERADO);
                if (cursosProximaMissao != null) {
                    CourseDAO.atualizaStatusCurso(cursosProximaMissao.get(0).getId(), Status.LIBERADO);
                }

                ((CoursesScreen) jogo.telas.get("courses")).atualizarBotoesStatus();
            }

        }
    }

    /**
     * Controle de infrações do personagem
     */
    public void controleInfracao() {
        // Se as infrações forem maiores ou iguais a 4, perde o jogo
        if (jogo.personagem.infracoes > 4) {
            perdeuJogo = true;
        }

        // Verifica se o personagem saiu da pista e mostra a caixa de diálogo
        if (jogo.personagem.tipoInfracao != null) {
            if (!mostrarDialogo)
                jogo.efeitoErro.play();

            atualizarDialogo(guarda, carregaDialogos(guarda, 0));
            mostrarDialogo = true;
        }
    }

    private void atualizaInfracao() {
        // Atualiza a posição do personagem, precisa verificar para qual sentido ele
        // está indo, desta forma ajustar -x valor que é a margem para sofrer a
        // infração.
        // personagem.setPosition()
        if (jogo.personagem.tipoInfracao == Personagem.TipoInfracao.ALERTA) {
            jogo.personagem.tipoInfracao = null;
            guarda.DIALOGO_ATUAL++;
            jogo.personagem.dialogosGuarda++;
        } else if (jogo.personagem.tipoInfracao == Personagem.TipoInfracao.MULTA) {
            jogo.personagem.tipoInfracao = null;
            guarda.DIALOGO_ATUAL++;
            jogo.personagem.dialogosGuarda++;
            jogo.personagem.infracoes++;
        }
        // personagem.infracoes++;
        DebugMode.mostrarLog("Controller", "Sofreu infração: " + jogo.personagem.tipoInfracao);
        DebugMode.mostrarLog("Controller", "Atualizando diálogo do guarda... Atual: " + guarda.DIALOGO_ATUAL);
        DebugMode.mostrarLog("Controller", "Infracoes: " + jogo.personagem.infracoes);
    }

    /**
     * Verifica a interação do personagem com os NPCs
     *
     * @param npc Npc
     */
    private void interacaoComNpc(Npc npc) {
        if (jogo.telas.get("game") != null) {
            if (jogo.personagem.estaDentroDaDistancia(15, npc)) {
                // Verifica se o diálogo do NPC por ser exibido, pois é necessário assistir o
                // módulo referente a missão
                if (verificarExibicaoDialogoMissaoComCurso()) {
                    // No inicio do jogo, o guarda não tem diálogo
                    if (npc.nome.equals("guarda")) {
                        atualizarDialogo(npc, Npc.DIALOGO_GUARDA_GENERICO);
                        mostrarDialogo = true;
                    } else {
                        // Carrega os diálogos do NPC
                        atualizarDialogo(npc, carregaDialogos(npc));
                        mostrarDialogo = true;
                    }
                } else {
                    atualizarDialogo(betania, Npc.DIALOGO_BETANIA_MISSAO_BLOQUEADA);
                    mostrarDialogo = true;
                }
            }
        }
    }

    /**
     * Verifica se o diálogo do NPC por ser exibido, pois é necessário assistir o
     * módulo do curso antes de iniciar a missão
     * 
     * @return
     */
    public boolean verificarExibicaoDialogoMissaoComCurso() {
        return verificarExibicaoDialogoMissaoComCurso(MISSAO);
    }

    /**
     * Verifica se o diálogo do NPC por ser exibido, pois é necessário assistir o
     * módulo do curso antes de iniciar a missão
     * 
     * @param missao int
     * @return
     */
    public boolean verificarExibicaoDialogoMissaoComCurso(int missao) {
        List<Course> cursos = CourseDAO.listarCursosPorMissaoId(missao);
        for (Course curso : cursos) {
            if (curso.getStatus() != Status.CONCLUIDO) {
                return false;
            }
        }
        return true;
    }

    /**
     * Atualiza o status de alerta da missão do NPC
     *
     * @param npc Npc
     */
    private void atualizaStatusAlertaMissaoNpc(Npc npc) {
        if (jogo.telas.get("game") != null) {
            /**
             * Verifica se está dentro da distancia e se o npc faz parte da missão
             * Se sim, atualiza o status de alerta da missão
             */
            if (jogo.personagem.estaDentroDaDistancia(15, npc) &&
                    jogo.controleMissao.npcEstaNaMisao(npc.nome) != null &&
                    npc.statusAlertaMissao == 1 &&
                    verificarExibicaoDialogoMissaoComCurso()) {
                npc.statusAlertaMissao = 2;
            }
        }
    }

    /**
     * Carrega os diálogos do personagem
     * 
     * @param npc Npc
     */
    private String carregaDialogos(Npc npc) {
        return carregaDialogos(npc, MISSAO);
    }

    /**
     * Carrega os diálogos do personagem
     * 
     * @param npc    Npc
     * @param missao int
     */
    private String carregaDialogos(Npc npc, int missao) {
        // Carregar diálogos
        Dialog dialogo = DialogDAO.carregarDialogos(npc.nome);
        if (dialogo != null) {
            // Verifica se tem uma missão
            return dialogo.buscarProximoDialogoMissao(missao, npc.DIALOGO_ATUAL).getMensagem();
        }
        return "Olá, eu sou o " + npc.nome + "!";
    }

    /**
     * Monta os retângulos de colisão do mapa
     *
     * @param mapa TiledMap
     */
    private void montarColisao(TiledMap mapa) {
        DebugMode.mostrarLog("Controller", "Crianado a camada de colisao...");
        // Carrega os objetos de colisão
        jogo.objetosColisao = mapa.getLayers().get("colisao").getObjects();
        DebugMode.mostrarLog("Controller", "Objetos carregados: " + jogo.objetosColisao.getCount());
        jogo.retangulosColisao = new Array<Rectangle>();
        DebugMode.mostrarLog("Controller",
                "Array de retângulos de colisão criado... Tamanho: " + jogo.retangulosColisao.size);

        // Adiciona os retângulos de colisão do mapa ao array
        for (MapObject objeto : jogo.objetosColisao) {
            if (objeto instanceof RectangleMapObject) {
                Rectangle retangulo = ((RectangleMapObject) objeto).getRectangle();
                jogo.retangulosColisao.add(retangulo);
            }
        }

        // Adiciona os retângulos de colisão do mapa ao personagem
        DebugMode.mostrarLog("Controller",
                "Verificando cada retângulo da colisao, objetos: " + jogo.retangulosColisao.size);
        jogo.personagem.setRetangulosColisao(jogo.retangulosColisao);
    }

    /**
     * Monta os retângulos de colisão da pista
     *
     * @param mapa        TiledMap
     * @param removePista boolean - Remove a pista
     */
    public void montarCamadaPista(boolean removePista) {
        DebugMode.mostrarLog("Controller", "Iniciando chamada montagem da camada de objetos da pista...");
        if (removePista) {
            DebugMode.mostrarLog("Controller", "Removendo a camada de objetos da pista...");
            jogo.retangulosPista = new Array<Rectangle>();
            jogo.personagem.setRetangulosPista(jogo.retangulosPista);
        } else {
            DebugMode.mostrarLog("Controller", "Crianado a camada de objetos da pista...");
            // Carrega os objetos de pista
            jogo.objetosPista = jogo.mapas.mapa.getLayers().get("pista").getObjects();
            jogo.objetosCiclofaixa = jogo.mapas.mapa.getLayers().get("ciclofaixa").getObjects();
            DebugMode.mostrarLog("Controller", "Objetos carregados: " + jogo.objetosPista.getCount());
            jogo.retangulosPista = new Array<Rectangle>();
            DebugMode.mostrarLog("Controller",
                    "Array de retângulos de colisão criado... Tamanho: " + jogo.retangulosPista.size);

            // Adiciona os retângulos de pista do mapa ao array
            for (MapObject objeto : jogo.objetosPista) {
                if (objeto instanceof RectangleMapObject) {
                    Rectangle retangulo = ((RectangleMapObject) objeto).getRectangle();
                    jogo.retangulosPista.add(retangulo);
                }
            }

            if (MISSAO >= 4 && jogo.controleMissao.ativaCamadaMissao4) {
                for (MapObject objeto : jogo.objetosCiclofaixa) {
                    if (objeto instanceof RectangleMapObject) {
                        Rectangle retangulo = ((RectangleMapObject) objeto).getRectangle();
                        jogo.retangulosPista.add(retangulo);
                    }
                }
            }

            // Adiciona os retângulos de pista do mapa ao personagem
            DebugMode.mostrarLog("Controller",
                    "Verificando cada retângulo da pista, objetos: " + jogo.retangulosPista.size);
            jogo.personagem.setRetangulosPista(jogo.retangulosPista);
        }
    }

}
