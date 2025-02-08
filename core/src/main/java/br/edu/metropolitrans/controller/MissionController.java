package br.edu.metropolitrans.controller;

import java.util.List;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.Mission;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.Vehicle;
import br.edu.metropolitrans.model.dao.MissionDataDAO;
import br.edu.metropolitrans.model.utils.DebugMode;
import br.edu.metropolitrans.view.components.mission_modal.MissionComponents;

public class MissionController {

    private MetropoliTrans jogo;
    private Mission missao;
    private boolean controlaTrocaMissao;
    // private Npc npcAtualMissao;
    public boolean missaoConcluida;
    public boolean ativaCamadaMissao4 = false;

    public MissionController(MetropoliTrans jogo) {
        this.jogo = jogo;
        missao = null;
        controlaTrocaMissao = false;
        missaoConcluida = false;
    }

    /**
     * Controle de missões
     * 
     * @param missaoId ID da missão
     * @param jogo     Instância do jogo
     */
    public void controle(int missaoId) {
        // Executa o controle da lógica do jogo
        jogo.controller.controleLogicaJogo();

        // Carrega a missão referente ao id
        missao = MissionDataDAO.buscaMissaoPorId(missaoId);
        Npc npc = jogo.controller.gameScreen.caixaDialogo.npc;

        /**
         * Primeira missão, verifica se o dialógo
         * com o prefeito já foi exibido, se sim,
         * deve setar missao atual para 1
         */
        switch (missaoId) {
            case 0:
                logicaInicial();
                break;
            case 1:
                logicaMissao1(npc);
                break;
            case 2:
                logicaMissao2(npc);
                break;
            case 3:
                logicaMissao3(npc);
                break;
            case 4:
                logicaMissao4(npc);
                break;
            default:
                break;
        }
    }

    private void logicaInicial() {
        DebugMode.mostrarLog("Missão", "Início do jogo");
        atualizarMissao(1, "heberto");
        controlaTrocaMissao = true;
    }

    private void logicaMissao1(Npc npc) {
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
                            .setText("Missão " + (jogo.controller.MISSAO) + ": "
                                    + "Ajuda para Maria atravessar a rua\r\n[Artigo/Regra: 70]");
                    jogo.controller.gameScreen.missaoModalBox.missionComponents = componentesMissao;
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

                        jogo.objetoChao.setVisible(false);
                        npc.setRoteiro(List.of("D-6*32", "B-3*32"));
                        npc.repeteAnimacao = false;
                        npc.animacaoAtivada = true;
                        npc.statusAlertaMissao = 0;
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
                            .setText("Missão " + (jogo.controller.MISSAO) + ": " + missao.getDescricao());
                    jogo.controller.gameScreen.missaoModalBox.missionComponents = componentesMissao;
                    jogo.objetoMissao.setVisible(true);
                }
                if (missaoConcluida) {
                    DebugMode.mostrarLog("Missão", "Missão 1 finalizada, exibindo placa");
                    jogo.objetoMissao.setVisible(false);
                    jogo.objetoPlaca1.setVisible(true);

                    // Atualiza a missão
                    atualizarMissao(2, "juliana");
                    controlaTrocaMissao = true;
                    jogo.objetoMissao.setPosition(1700, 1450);
                }
            }
        }
    }

    private void logicaMissao2(Npc npc) {
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
                        .setText("Missão " + (jogo.controller.MISSAO) + ": " + missao.getDescricao());
                jogo.controller.gameScreen.missaoModalBox.missionComponents = componentesMissao;
                jogo.objetoMissao.setVisible(true);
            }
            if (missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 2 finalizada, exibindo placa");
                jogo.objetoMissao.setVisible(false);
                jogo.objetoPlaca2.setVisible(true);

                // Atualiza a missão
                atualizarMissao(3, "antonio");
                controlaTrocaMissao = true;
                jogo.objetoMissao.setPosition(380, 1450);
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
                        .setText("Missão " + (jogo.controller.MISSAO) + ": " + missao.getDescricao());
                jogo.controller.gameScreen.missaoModalBox.missionComponents = componentesMissao;
                jogo.objetoMissao.setVisible(true);
            }
            if (missaoConcluida) {
                DebugMode.mostrarLog("Missão", "Missão 3 finalizada, exibindo placa");
                jogo.objetoMissao.setVisible(false);
                jogo.objetoPlaca3.setVisible(true);

                compactCar.setVisible(false);
                compactCar.animacaoAtivada = false;
                compactCar.pararAnimacao();

                sportBlueCar.setVisible(false);
                sportBlueCar.animacaoAtivada = false;
                sportBlueCar.pararAnimacao();

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
                            .setText("Missão " + (jogo.controller.MISSAO) + ": "
                                    + "Quais os componentes utilizados em\r\nciclofaixas? [Artigo/Regra: 58]");
                    jogo.controller.gameScreen.missaoModalBox.missionComponents = componentesMissao;
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
                        npc.statusAlertaMissao = 0;

                        // // Atualiza a missão
                        // atualizarMissao(5, "proximo");
                        // controlaTrocaMissao = true; // TODO: descomentar assim que a missão 5 for
                        // criada
                    }
                }
            }
        }
    }

    public void trocaMissao() {
        if (controlaTrocaMissao) {
            atualizarAlertas();
            controlaTrocaMissao = false;
            DebugMode.mostrarLog("Missão", "Troca de missão realizada");
        }
    }

    /**
     * Verifica se o NPC está na missão
     * 
     * @param nomeNpc Nome do NPC
     * @return true se o NPC está na missão
     */
    public Npc npcEstaNaMisao(String nomeNpc) {
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
