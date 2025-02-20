package br.edu.metropolitrans.controller;

import java.util.List;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.GameDataNpc;
import br.edu.metropolitrans.model.Mission;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.Vehicle;
import br.edu.metropolitrans.model.dao.GameDataDAO;
import br.edu.metropolitrans.model.dao.MissionDataDAO;
import br.edu.metropolitrans.model.utils.DebugMode;
import br.edu.metropolitrans.view.components.mission_modal.MissionComponents;

public class MissionController {

    private MetropoliTrans jogo;
    private Mission missao;
    private boolean controlaTrocaMissao;
    public boolean missao1GuardaAparece;
    private Npc npcAtualMissao;
    public boolean missaoConcluida;
    public boolean ativaCamadaMissao4;
    public MissionComponents missionComponents;

    public MissionController(MetropoliTrans jogo) {
        this.jogo = jogo;
        missao = null;
        controlaTrocaMissao = false;
        missao1GuardaAparece = false;
        missaoConcluida = false;
        ativaCamadaMissao4 = false;
    }

    public void setValoresDafault() {
        missao = null;
        npcAtualMissao = null;
        controlaTrocaMissao = false;
        missao1GuardaAparece = false;
        missaoConcluida = false;
        ativaCamadaMissao4 = false;
        missionComponents = null;
    }

    /**
     * Controle de missões
     * 
     * @param missaoId ID da missão
     * @param jogo     Instância do jogo
     */
    public void controle(int missaoId) {
        // Carrega a missão referente ao id
        missao = MissionDataDAO.buscaMissaoPorId(missaoId);
        npcAtualMissao = jogo.controller.npcDialogoAtual;

        // Verifica se saiu da sala de aula e exibe o diálogo do guarda
        verificarExibicaoDialogoGuardaMissao1();

        /**
         * Primeira missão, verifica se o dialógo
         * com o prefeito já foi exibido, se sim,
         * deve setar missao atual para 1
         */
        switch (missaoId) {
            case 0:
                logicaInicial(npcAtualMissao);
                break;
            case 1:
                logicaMissao1(npcAtualMissao);
                break;
            case 2:
                logicaMissao2(npcAtualMissao);
                break;
            case 3:
                logicaMissao3(npcAtualMissao);
                break;
            case 4:
                logicaMissao4(npcAtualMissao);
                break;
            case 5:
                logicaMissao5(npcAtualMissao);
                break;
            case 6:
                logicaMissao6(npcAtualMissao);
                break;
            case 7:
                logicaMissao7(npcAtualMissao);
                break;
            case 8:
                logicaFinal(npcAtualMissao);
                break;
            default:
                break;
        }
    }

    private void salvarJogo() {
        jogo.salvarJogo("jogo");
    }

    private void logicaInicial(Npc npc) {
        DebugMode.mostrarLog("Missão", "Início do jogo");
        // Verifica se o diálogo com o prefeito foi exibido, se sim, ativa a animação
        // dele para que entre na prefeitura e suma depois

        if (npc != null && npc.nome.equals("heberto")) {
            if (npc.finalizouAnimacao) {
                npc.setPosition(npc.getX(), 800);
                npc.setVisible(false);
            }

            if (npc.statusAlertaMissao == 2) {
                npc.setRoteiro(List.of("C-2*32", "D-2*32", "C-2*32"));
                npc.repeteAnimacao = false;
                npc.animacaoAtivada = true;
                npc.statusAlertaMissao = 0;
            }
        }

        // Antes de trocar a missão, assim que falar com o Heberto, precisa falar
        // primeiro com a betania
        // para que a missão 1 seja ativada
        if (npc != null && npc.nome.equals("betania") && npc.statusAlertaMissao == 2) {
            atualizarMissao(1, "heberto");
            controlaTrocaMissao = true;
            jogo.efeitoNotificacao.play();
            jogo.controller.notificarLiberacaoModulo = true;
        }

    }

    private void logicaMissao1(Npc npc) {
        // Reposiona betania na posição inicial, pois ao entrar na prefeitura ela vai
        // para a sala de informática
        if (jogo.objetosInterativos.get("objeto") != null)
            jogo.npcs.get("betania").setPosition(274, 200);

        // Exibe o diálogo do guarda com instruções
        if (controlaTrocaMissao) {
            jogo.controller.atualizarDialogo(jogo.controller.guarda, Npc.DIALOGO_GUARDA_TECLA_ESPACO);
            jogo.controller.mostrarDialogo = true;
        }

        DebugMode.mostrarLog("Missão", "Início da missão 1");
        // Atualiza o status de alerta da missão dos NPCs que fazem parte da missão
        trocaMissao();

        DebugMode.mostrarLog("Missão", "Iniciando veículos da missão 1");
        Vehicle taxi = jogo.vehicles.get("taxi");
        Vehicle basicCar = jogo.vehicles.get("basic-car");
        Vehicle onibus = jogo.vehicles.get("onibus");

        // Verifica qual dialogo está sendo exibido
        // e exibe os veículos
        if (npc != null) {
            if (jogo.controller.mostrarDialogo) {
                if (npc.nome.equals("maria")) {
                    DebugMode.mostrarLog("Missão", "Missão 1: Exibindo desafio de Maria");
                    basicCar.setVisible(true);
                    onibus.setVisible(true);
                    basicCar.animacaoAtivada = true;
                    onibus.animacaoAtivada = true;

                    MissionComponents componentesMissao = jogo.missionComponents.get("missao0");
                    componentesMissao.titulo
                            .setText("Missão 1: "
                                    + "Selecione a opção que melhor representa uma \r\nsolução para ajudar a Maria atravessar a rua\r\n[Art.70]:");
                    missionComponents = componentesMissao;
                }

                if (npc.nome.equals("juliana")) {
                    taxi.setVisible(true);
                    taxi.animacaoAtivada = true;
                    missaoConcluida = false;
                }
            } else {
                basicCar.setVisible(false);
                basicCar.animacaoAtivada = false;

                onibus.setVisible(false);
                onibus.animacaoAtivada = false;

                taxi.setVisible(false);
                taxi.animacaoAtivada = false;

                if (npc.nome.equals("maria")) {
                    // Verifica se a animação de Maria já foi exibida e se o diálogo já foi lido,
                    // pois deverá exibir a caixa da missão
                    if (npc.statusAlertaMissao == 2 && !missaoConcluida) {
                        jogo.controller.mostrarCaixaMissao = true;
                    }

                    // Mostra a faixa de pedestroe, ativa uma animação de maria atravessando a pista
                    // e libera o personagem para atravessar
                    if (npc.statusAlertaMissao == 2 && missaoConcluida) {
                        // Para as animações dos veículos
                        onibus.pararAnimacao();
                        basicCar.pararAnimacao();

                        jogo.objetosInterativos.get("objetoChao").setVisible(false);
                        npc.setRoteiro(List.of("D-6*32", "B-3*32"));
                        npc.repeteAnimacao = false;
                        npc.animacaoAtivada = true;
                        npc.statusAlertaMissao = 0;

                        // Exibe o diálogo do guarda com instruções para as próximas missões
                        jogo.controller.atualizarDialogo(jogo.controller.guarda, Npc.DIALOGO_GUARDA_MINIMAPA);
                        jogo.controller.mostrarDialogo = true;
                    }
                }
            }

            // Verifica se o taxi está visível e se o diálogo de Juliana foi exibido
            // para exibir a caixa da missão ou se a missão foi concluída
            if (!taxi.isVisible() && npc != null && npc.nome.equals("juliana")) {
                if (npc.statusAlertaMissao == 2 && !missaoConcluida) {
                    DebugMode.mostrarLog("Missão", "Missão 1: Exibindo desafio de Juliana");
                    missaoConcluida = false;
                    taxi.pararAnimacao();

                    MissionComponents componentesMissao = jogo.missionComponents.get("missao1");
                    componentesMissao.titulo
                            .setText("Missão 2: " + missao.getDescricao());
                    missionComponents = componentesMissao;
                    jogo.objetosInterativos.get("objetoMissao").setVisible(true);
                }
                if (missaoConcluida) {
                    DebugMode.mostrarLog("Missão", "Missão 1 finalizada, exibindo placa");
                    jogo.objetosInterativos.get("objetoMissao").setVisible(false);
                    jogo.objetosInterativos.get("objetoPlaca1").setVisible(true);

                    // Conclui a missão
                    concluirMissao(missao);

                    // Atualiza a missão
                    atualizarMissao(2, "juliana");
                    controlaTrocaMissao = true;
                    jogo.objetosInterativos.get("objetoMissao").setPosition(1700, 1450);
                }
            }
        }
    }

    private void logicaMissao2(Npc npc) {
        // Exibe o diálogo do guarda com instruções
        if (controlaTrocaMissao) {
            jogo.controller.atualizarDialogo(jogo.controller.guarda, Npc.DIALOGO_GUARDA_TECLA_T);
            jogo.controller.mostrarDialogo = true;
        }

        DebugMode.mostrarLog("Missão", "Início da missão 2");
        trocaMissao();

        DebugMode.mostrarLog("Missão", "Iniciando veículos da missão 2");
        Vehicle basicVehicle2 = jogo.vehicles.get("basic-car-2");
        Vehicle basicVehicle3 = jogo.vehicles.get("basic-car-3");
        basicVehicle2.setLimiteRetangulo();
        basicVehicle3.setLimiteRetangulo();
        // Verifica qual dialogo está sendo exibido
        // e exibe os veículos
        if (npc != null) {
            if (jogo.controller.mostrarDialogo) {
                if (npc.nome.equals("antonio")) {
                    basicVehicle2.setVisible(true);
                    basicVehicle2.animacaoAtivada = true;

                    basicVehicle3.setVisible(true);
                    basicVehicle3.animacaoAtivada = true;

                    if (basicVehicle2.sobrepoe(basicVehicle3)) {
                        jogo.explosao.setVisible(true);
                    } else {
                        jogo.explosao.setVisible(false);
                    }

                    missaoConcluida = false;
                }
            } else {
                basicVehicle2.setVisible(false);
                basicVehicle2.animacaoAtivada = false;

                basicVehicle3.setVisible(false);
                basicVehicle3.animacaoAtivada = false;
            }
        }

        if (!basicVehicle2.isVisible() && npc != null && npc.nome.equals("antonio")) {
            if (npc.statusAlertaMissao == 2 && !missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 2: Exibindo desafio de Antônio");
                missaoConcluida = false;
                basicVehicle2.pararAnimacao();
                basicVehicle3.pararAnimacao();

                MissionComponents componentesMissao = jogo.missionComponents.get("missao2");
                componentesMissao.titulo
                        .setText("Missão 3: " + missao.getDescricao());
                missionComponents = componentesMissao;
                jogo.objetosInterativos.get("objetoMissao").setVisible(true);
                jogo.objetosInterativos.get("objetoMissaoHorizontal").setVisible(true);
            }
            if (missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 2 finalizada, exibindo placa");
                jogo.objetosInterativos.get("objetoMissao").setVisible(false);
                jogo.objetosInterativos.get("objetoMissaoHorizontal").setVisible(false);
                jogo.objetosInterativos.get("objetoHorizontal2").setVisible(true);

                // Conclui a missão
                concluirMissao(missao);

                // Atualiza a missão
                atualizarMissao(3, "antonio");
                controlaTrocaMissao = true;
                jogo.objetosInterativos.get("objetoMissao").setPosition(jogo.objetosInterativos.get("objetoPlaca3").x,
                        jogo.objetosInterativos.get("objetoPlaca3").y);
            }
        }
    }

    private void logicaMissao3(Npc npc) {
        DebugMode.mostrarLog("Missão", "Início da missão 3");
        trocaMissao();

        DebugMode.mostrarLog("Missão", "Iniciando veículos da missão 3");
        Vehicle compactCar = jogo.vehicles.get("compact-car");
        compactCar.setVisible(true);
        compactCar.animacaoAtivada = true;

        Vehicle sportBlueCar = jogo.vehicles.get("sport-blue-car");
        sportBlueCar.setVisible(true);
        sportBlueCar.animacaoAtivada = true;

        if (npc != null && jogo.controller.mostrarDialogo) {
            if (npc.nome.equals("jose")) {
                missaoConcluida = false;
            }
        }

        if (npc != null && npc.nome.equals("jose")) {
            if (npc.statusAlertaMissao == 2 && !missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 3: Exibindo desafio de José");
                missaoConcluida = false;
                compactCar.pararAnimacao();
                sportBlueCar.pararAnimacao();

                MissionComponents componentesMissao = jogo.missionComponents.get("missao3");
                componentesMissao.titulo
                        .setText("Missão 4: " + missao.getDescricao());
                missionComponents = componentesMissao;
                jogo.objetosInterativos.get("objetoMissao").setVisible(true);
            }
            if (missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 3 finalizada, exibindo placa");
                jogo.objetosInterativos.get("objetoMissao").setVisible(false);
                jogo.objetosInterativos.get("objetoPlaca3").setVisible(true);

                compactCar.setVisible(false);
                compactCar.animacaoAtivada = false;
                compactCar.pararAnimacao();

                sportBlueCar.setVisible(false);
                sportBlueCar.animacaoAtivada = false;
                sportBlueCar.pararAnimacao();

                // Conclui a missão
                concluirMissao(missao);

                // Atualiza a missão
                atualizarMissao(4, "jose");
                controlaTrocaMissao = true;
            }
        }
    }

    private void logicaMissao4(Npc npc) {
        DebugMode.mostrarLog("Missão", "Início da missão 4");
        trocaMissao();

        if (npc != null) {
            if (jogo.controller.mostrarDialogo) {
                if (npc.nome.equals("bruna")) {
                    missaoConcluida = false;

                    MissionComponents componentesMissao = jogo.missionComponents.get("missao4");
                    componentesMissao.titulo
                            .setText("Missão 5: " + missao.getDescricao());
                    missionComponents = componentesMissao;
                }
            } else {
                if (npc.nome.equals("bruna")) {
                    // Verifica se a animação de Bruna já foi exibida e se o diálogo já foi lido,
                    // pois deverá exibir a caixa da missão
                    if (npc.statusAlertaMissao == 2 && !missaoConcluida) {
                        DebugMode.mostrarLog("Missão",
                                "Missão 4: Exibindo desafio de Bruna, entrando na caixa de missão");
                        jogo.controller.mostrarCaixaMissao = true;
                    }

                    // Mostra a faixa de pedestroe, ativa uma animação de Bruna atravessando a pista
                    // e libera o personagem para atravessar
                    if (npc.statusAlertaMissao == 2 && missaoConcluida) {
                        DebugMode.mostrarLog("Missão", "Missão 4: Exibindo desafio de Bruna, exibindo ciclofaixa");
                        // Exibe a ciclofaixa no mapa prinxipal
                        ativaCamadaMissao4 = true;
                        jogo.controller.montarCamadaPista(false);

                        // Conclui a missão
                        concluirMissao(missao);

                        // Atualiza a missão
                        atualizarMissao(5, "bruna");
                        controlaTrocaMissao = true;
                        jogo.objetosInterativos.get("objetoMissao").setPosition(
                                jogo.objetosInterativos.get("objetoPlaca5").x,
                                jogo.objetosInterativos.get("objetoPlaca5").y);
                    }
                }
            }
        }
    }

    private void logicaMissao5(Npc npc) {
        if (controlaTrocaMissao) {
            // Reposiciona o npc Maria
            atualizarNpc("maria");
            jogo.npcs.get("maria").setPosition(1732, 556);
        }

        DebugMode.mostrarLog("Missão", "Início da missão 5");
        trocaMissao();

        DebugMode.mostrarLog("Missão", "Iniciando veículos da missão 5");
        Vehicle blackViperCar = jogo.vehicles.get("black-viper-car");

        if (npc != null) {
            if (jogo.controller.mostrarDialogo) {
                if (npc.nome.equals("josinaldo")) {
                    blackViperCar.setVisible(true);
                    blackViperCar.animacaoAtivada = true;
                    missaoConcluida = false;
                }
            } else {
                blackViperCar.setVisible(false);
                blackViperCar.animacaoAtivada = false;
            }
        }

        if (!blackViperCar.isVisible() && npc != null && npc.nome.equals("josinaldo")) {
            if (npc.statusAlertaMissao == 2 && !missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 5: Exibindo desafio de Antônio");
                missaoConcluida = false;
                blackViperCar.pararAnimacao();

                MissionComponents componentesMissao = jogo.missionComponents.get("missao5");
                componentesMissao.titulo
                        .setText("Missão 6: " + missao.getDescricao());
                missionComponents = componentesMissao;
                jogo.objetosInterativos.get("objetoMissao").setVisible(true);
            }
            if (missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 5 finalizada, exibindo placa");
                jogo.objetosInterativos.get("objetoMissao").setVisible(false);
                jogo.objetosInterativos.get("objetoPlaca5").setVisible(true);

                Npc maria = jogo.npcs.get("maria");
                maria.valoresDefault(100);
                maria.setRoteiro(List.of("D-6*32", "C-3*32", "D-3*32"));
                maria.repeteAnimacao = false;
                maria.animacaoAtivada = true;
                maria.statusAlertaMissao = 0;

                // Conclui a missão
                concluirMissao(missao);

                // Atualiza a missão
                atualizarMissao(6, "josinaldo");
                controlaTrocaMissao = true;
                jogo.objetosInterativos.get("objetoMissao").setPosition(jogo.objetosInterativos.get("objetoPlaca6").x,
                        jogo.objetosInterativos.get("objetoPlaca6").y);
            }
        }
    }

    private void logicaMissao6(Npc npc) {
        DebugMode.mostrarLog("Missão", "Início da missão 6");
        trocaMissao();

        DebugMode.mostrarLog("Missão", "Iniciando veículos da missão 6");
        Vehicle compactCar = jogo.vehicles.get("compact-car");
        compactCar.setPosition(1382, 256);
        compactCar.setRoteiro(List.of("C-1*32"));
        compactCar.setVisible(true);
        compactCar.animacaoAtivada = true;

        if (npc != null && jogo.controller.mostrarDialogo) {
            if (npc.nome.equals("paulo")) {
                missaoConcluida = false;
            }
        }

        if (npc != null && npc.nome.equals("paulo")) {
            // Verifica se o personage está próximo do NPC
            // Se tiver aciona a buzina
            if (jogo.personagem.estaDentroDaDistancia(400, npc)) {
                jogo.efeitoBuzina.play();
            } else {
                jogo.efeitoBuzina.stop();
            }

            if (npc.statusAlertaMissao == 2 && !missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 6: Exibindo desafio de Paulo");
                missaoConcluida = false;
                compactCar.pararAnimacao();

                MissionComponents componentesMissao = jogo.missionComponents.get("missao6");
                componentesMissao.titulo
                        .setText("Missão 7: " + missao.getDescricao());
                missionComponents = componentesMissao;
                jogo.objetosInterativos.get("objetoMissao").setVisible(true);
            }
            if (missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 6 finalizada, exibindo placa");
                jogo.objetosInterativos.get("objetoMissao").setVisible(false);
                jogo.objetosInterativos.get("objetoPlaca6").setVisible(true);

                compactCar.setVisible(false);
                compactCar.animacaoAtivada = false;
                compactCar.pararAnimacao();

                jogo.efeitoBuzina.stop();
                jogo.efeitoBuzina.dispose();

                // Conclui a missão
                concluirMissao(missao);

                // Atualiza a missão
                atualizarMissao(7, "paulo");
                controlaTrocaMissao = true;
                jogo.objetosInterativos.get("objetoMissao").setPosition(jogo.objetosInterativos.get("objetoPlaca7").x,
                        jogo.objetosInterativos.get("objetoPlaca7").y);
            }
        }
    }

    private void logicaMissao7(Npc npc) {
        DebugMode.mostrarLog("Missão", "Início da missão 7");
        trocaMissao();

        DebugMode.mostrarLog("Missão", "Iniciando veículos da missão 7");
        if (npc != null && jogo.controller.mostrarDialogo && npc.nome.equals("betania")) {
            missaoConcluida = false;
        }

        if (npc != null && npc.nome.equals("betania")) {
            if (npc.statusAlertaMissao == 2 && !missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 5: Exibindo desafio de Antônio");
                missaoConcluida = false;

                MissionComponents componentesMissao = jogo.missionComponents.get("missao7");
                componentesMissao.titulo
                        .setText("Missão 8: " + missao.getDescricao());
                missionComponents = componentesMissao;
                jogo.objetosInterativos.get("objetoMissao").setVisible(true);
            }
            if (missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 7 finalizada, exibindo placa");
                jogo.objetosInterativos.get("objetoMissao").setVisible(false);
                jogo.objetosInterativos.get("objetoPlaca7").setVisible(true);

                // Conclui a missão
                concluirMissao(missao);

                // Atualiza a missão
                atualizarMissao(8, "betania");
                controlaTrocaMissao = true;

                // Reposiciona os npcs em frente a prefeitura
                jogo.npcs.get("maria").setPosition(25, 482);
                jogo.npcs.get("bruna").setPosition(217, 650);
                jogo.npcs.get("antonio").setPosition(217, 482);
                jogo.npcs.get("jose").setPosition(25, 398);
                jogo.npcs.get("paulo").setPosition(217, 398);
            }
        }
    }

    private void logicaFinal(Npc npc) {
        // Exibe o diálogo de betania parabenizando o jogador
        if (controlaTrocaMissao) {
            atualizarNpc("heberto");
            jogo.npcs.get("heberto").setPosition(25, 650);

            jogo.controller.atualizarDialogo(jogo.controller.betania, Npc.DIALOGO_BETANIA_PARABENEZANDO);
            jogo.controller.mostrarDialogo = true;
            jogo.npcs.get("betania").setPosition(25, 566);
        }

        DebugMode.mostrarLog("Missão", "Final do jogo");
        trocaMissao();

        int count = 0;
        // Verifica se o dialogo final foi exibido e finaliza o jogo
        if (npc != null && npc.nome.equals("heberto") && npc.statusAlertaMissao == 2) {
            if (count <= 1)
                count++;
            DebugMode.mostrarLog("Missão", "Finalizando o jogo");
            if (count == 1) {
                jogo.efeitoAcerto.play();
                jogo.controller.ganhouJogo = true;
            }
        }
    }

    /**
     * Conclui a missão e atualiza o status da missão no banco de dados
     * 
     * @param missao Missão
     */
    private void concluirMissao(Mission missao) {
        // Atualiza o status da missão no banco de dados
        missao.setFinalizouMissao(true);
        MissionDataDAO.atualizarMissao(missao);
    }

    public void trocaMissao() {
        if (controlaTrocaMissao) {
            atualizarAlertas();
            controlaTrocaMissao = false;
            salvarJogo();
            DebugMode.mostrarLog("Missão", "Troca de missão realizada");
        }
    }

    public void verificarExibicaoDialogoGuardaMissao1() {
        if (missao1GuardaAparece && jogo.controller.verificarExibicaoDialogoMissaoComCurso(1)
                && jogo.controller.MISSAO == 1) {
            // Coloca o guarda na porta da prefeitura
            jogo.npcs.get("guarda").setPosition(217, 650);
            jogo.controller.atualizarDialogo(jogo.controller.guarda, Npc.DIALOGO_GUARDA_INDICA_CAMINHO);

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    jogo.controller.mostrarDialogo = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            missao1GuardaAparece = false;
        }
    }

    /**
     * Verifica se o NPC está na missão
     * 
     * @param nomeNpc Nome do NPC
     * @return true se o NPC está na missão
     */
    public Npc npcEstaNaMisao(String nomeNpc) {
        if (missao == null) {
            missao = MissionDataDAO.buscaMissaoPorId(jogo.controller.MISSAO);
        }

        if (missao.getPersonagens().contains(nomeNpc)) {
            return buscaNpcPorNome(nomeNpc);
        }
        return null;
    }

    /**
     * Busca o NPC pelo nome
     * 
     * @param nomeNpc Nome do NPC
     * @return NPC
     */
    private Npc buscaNpcPorNome(String nomeNpc) {
        return jogo.npcs.get(nomeNpc);
    }

    /**
     * Atualiza a missão
     * 
     * @param jogo     Instância do jogo
     * @param missaoId ID da missão
     */
    private void atualizarMissao(int missaoId, String nomeNpc) {
        Npc npc = npcEstaNaMisao(nomeNpc);

        // No caso do primeiro diálogo com o Heberto, é necessário que mesmo com status
        // zero ele altere a missão
        if (npc != null && nomeNpc.equals("heberto") && npc.statusAlertaMissao == 0) {
            jogo.controller.MISSAO = missaoId;
            DebugMode.mostrarLog("Missão", "Ativanado missão " + missaoId);
        }

        if (npc != null && npc.statusAlertaMissao == 2) {
            jogo.controller.MISSAO = missaoId;
            DebugMode.mostrarLog("Missão", "Ativanado missão " + missaoId);
        }
    }

    /**
     * Atualiza os alertas da missão
     * 
     * @param jogo Instância do jogo
     */
    private void atualizarAlertas() {
        DebugMode.mostrarLog("Missão", "Atualizando alertas da missão");
        jogo.npcs.forEach((nome, npc) -> {
            if (npcEstaNaMisao(npc.nome) != null) {
                npc.statusAlertaMissao = 1;
            } else {
                npc.statusAlertaMissao = 0;
            }
        });
    }

    /**
     * Atualiza o NPC no estágio
     * OBS.: Este método só deve ser utiizado para executar apenas uma vez, se
     * entrar em algum loop, o jogo pode travar
     * 
     * @param nomeNpc Nome do NPC
     * @return NPC
     */
    private void atualizarNpc(String nomeNpc) {
        // Verifica se existe e remove o npc do estágio
        if (jogo.npcs.get(nomeNpc) != null) {
            jogo.npcs.get(nomeNpc).remove();

            // Atualiza o NPC do Heberto para um novo
            GameDataNpc npcData = GameDataDAO.buscarNpcPorNome(nomeNpc);

            // Adiciona o npc no estágio
            if (npcData != null) {
                jogo.npcs.put(npcData.getKey(),
                        new Npc(npcData.getKey(), npcData.getX(), npcData.getY(), npcData.getKey() + "/sprite.png",
                                jogo.estagioPrincipal, npcData.getStatusAlertaMissao(), npcData.isTemAnimacao()));
            }
        }
    }

    /**
     * Retorna a missão
     * 
     * @return Missão
     */
    public Mission getMissao() {
        return missao;
    }

    /**
     * Retorna o valor de erro da missão
     * 
     * @return Valor de erro
     */
    public int getValorErroMissao() {
        return missao != null ? missao.getValorErro() : 0;
    }

    /**
     * Retorna a recompensa de moedas da missão
     * 
     * @return Recompensa de moedas
     */
    public int getRecompensaMoedasMissao() {
        return missao != null ? missao.getRecompensaMoedas() : 0;
    }
}
