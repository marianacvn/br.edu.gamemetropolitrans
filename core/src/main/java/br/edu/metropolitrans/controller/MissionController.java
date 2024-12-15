package br.edu.metropolitrans.controller;

import com.badlogic.gdx.Gdx;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.Mission;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.Vehicle;
import br.edu.metropolitrans.model.dao.MissionDataDAO;

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
            if (jogo.controller.mostrarDialogo
                    && jogo.controller.gameScreen.caixaDialogo.npc.nome.equals("juliana")) {
                jogo.vehicles.get("taxi").setVisible(true);
                jogo.vehicles.get("taxi").animacaoAtivada = true;
            }
        }
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

}
