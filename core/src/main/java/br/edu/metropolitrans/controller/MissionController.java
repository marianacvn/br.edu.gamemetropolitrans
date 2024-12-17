package br.edu.metropolitrans.controller;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.Mission;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.Vehicle;
import br.edu.metropolitrans.model.dao.MissionDataDAO;
import br.edu.metropolitrans.view.components.mission_modal.MissionComponents;

public class MissionController {

    private static Mission missao;
    private static boolean controlaTrocaMissao;

    /**
     * Controle de missões
     * 
     * @param missaoId ID da missão
     * @param jogo     Instância do jogo
     */
    public static void controle(int missaoId, MetropoliTrans jogo) {
        // Executa o controle da lógica do jogo
        jogo.controller.controleLogicaJogo();

        // Carrega a missão referente ao id
        missao = MissionDataDAO.buscaMissaoPorId(missaoId);

        /**
         * Primeira missão, verifica se o dialógo
         * com o prefeito já foi exibido, se sim,
         * deve setar missao atual para 1
         */
        if (missaoId == 0) {
            atualizarMissao(jogo, 1);
            controlaTrocaMissao = true;
        } else if (missaoId == 1) {
            // Atualiza o status de alerta da missão dos NPCs que fazem parte da missão
            if (controlaTrocaMissao) {
                atualizarAlertas(jogo);
                controlaTrocaMissao = false;
            }

            // Verifica se o diálogo com juliana está sendo exibido,
            // se sim exibe o veiculo realizando a ação
            Vehicle taxi = jogo.vehicles.get("taxi");
            Npc npc = jogo.controller.gameScreen.caixaDialogo.npc;
            if (jogo.controller.mostrarDialogo
                    && npc.nome.equals("juliana")) {
                taxi.setVisible(true);
                taxi.animacaoAtivada = true;
            } else {
                taxi.animacaoAtivada = false;
                taxi.setVisible(false);
                taxi.reiniciarAnimacao();
            }

            if (!taxi.isVisible()) {
                if (npc.nome.equals("juliana") && npc.statusAlertaMissao == 2) {
                    taxi.remove();
                    MissionComponents componentesMissao = jogo.missionComponents.get("missao1");
                    componentesMissao.titulo
                            .setText("Missão " + (jogo.controller.MISSAO) + ": " + missao.getDescricao());
                    jogo.controller.gameScreen.missaoModalBox.missionComponents = componentesMissao;
                    jogo.objetoMissao.setVisible(true);
                }
            }
        }
    }

    public static void reiniciarJogo() {
        missao = null;
        controlaTrocaMissao = false;
    }

    /**
     * Verifica se o NPC está na missão
     * 
     * @param npc NPC
     * @return true se o NPC está na missão
     */
    public static boolean npcEstaNaMisao(Npc npc) {
        return missao.getPersonagens().contains(npc.nome);
    }

    /**
     * Atualiza a missão
     * 
     * @param jogo     Instância do jogo
     * @param missaoId ID da missão
     */
    private static void atualizarMissao(MetropoliTrans jogo, int missaoId) {
        jogo.npcs.stream().forEach(npc -> {
            if (npcEstaNaMisao(npc) && npc.statusAlertaMissao == 2) {
                jogo.controller.MISSAO = missaoId;
            }
        });
    }

    /**
     * Atualiza os alertas da missão
     * 
     * @param jogo Instância do jogo
     */
    private static void atualizarAlertas(MetropoliTrans jogo) {
        jogo.npcs.stream().forEach(npc -> {
            if (npcEstaNaMisao(npc)) {
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
    public static Mission getMissao() {
        return missao;
    }

    /**
     * Retorna o valor de erro da missão
     * 
     * @return Valor de erro
     */
    public static int getValorErroMissao() {
        return missao != null ? missao.getValorErro() : 0;
    }

    /**
     * Retorna a recompensa de moedas da missão
     * 
     * @return Recompensa de moedas
     */
    public static int getRecompensaMoedasMissao() {
        return missao != null ? missao.getRecompensaMoedas() : 0;
    }
}
