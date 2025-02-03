package br.edu.metropolitrans.controller;

import java.util.List;

import com.badlogic.gdx.Gdx;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.Mission;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.Vehicle;
import br.edu.metropolitrans.model.dao.MissionDataDAO;
import br.edu.metropolitrans.model.maps.Mapas;
import br.edu.metropolitrans.model.utils.DebugMode;
import br.edu.metropolitrans.view.components.mission_modal.MissionComponents;

public class MissionController {

    private MetropoliTrans jogo;
    private Mission missao;
    private boolean controlaTrocaMissao;
    // private Npc npcAtualMissao;
    public boolean missaoConcluida;

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
        if (missaoId == 0) {
            DebugMode.mostrarLog("Missão", "Início do jogo");
            atualizarMissao(1, "heberto");
            controlaTrocaMissao = true;
        } else if (missaoId == 1) {
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
                                        + "Ajuda para Maria atravessar a rua [Artigo/Regra: 70]");
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
        } else if (missaoId == 2) {
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
                            Gdx.app.log("Missão", "Veículos sobrepostos");
                            jogo.explosao.setVisible(true);
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
                    // atualizarMissao(3, "juliana");
                    // controlaTrocaMissao = true; // TODO: Verificar se a missão 3 está funcionando
                    // para habilitar
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
        return jogo.npcs.stream()
                .filter(npc -> npc.nome.equals(nomeNpc))
                .findFirst()
                .orElse(null);
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
        jogo.npcs.stream().forEach(npc -> {
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
