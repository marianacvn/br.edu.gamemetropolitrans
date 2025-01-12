package br.edu.metropolitrans.controller;

import com.badlogic.gdx.Gdx;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.Mission;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.Vehicle;
import br.edu.metropolitrans.model.dao.MissionDataDAO;
import br.edu.metropolitrans.view.components.mission_modal.MissionComponents;

public class MissionController {

    private MetropoliTrans jogo;
    private Mission missao;
    private boolean controlaTrocaMissao;
    private Npc npcAtualMissao;
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

        /**
         * Primeira missão, verifica se o dialógo
         * com o prefeito já foi exibido, se sim,
         * deve setar missao atual para 1
         */
        if (missaoId == 0) {
            atualizarMissao(1, "heberto");
            controlaTrocaMissao = true;
        } else if (missaoId == 1) {
            // Atualiza o status de alerta da missão dos NPCs que fazem parte da missão
            trocaMissao();

            Vehicle taxi = jogo.vehicles.get("taxi");
            Vehicle basicCar = jogo.vehicles.get("basic-car");
            Vehicle onibus = jogo.vehicles.get("onibus");
            // Verifica qual dialogo está sendo exibido
            // e exibe os veículos
            Npc npc = jogo.controller.gameScreen.caixaDialogo.npc;
            if (npc != null) {
                if (jogo.controller.mostrarDialogo) {
                    if (npc.nome.equals("maria")) {
                        basicCar.setVisible(true);
                        onibus.setVisible(true);
                        basicCar.animacaoAtivada = true;
                        onibus.animacaoAtivada = true;
                    }
                    if (npc.nome.equals("juliana")) {
                        taxi.setVisible(true);
                        taxi.animacaoAtivada = true;
                    }
                } else {
                    taxi.animacaoAtivada = false;
                    taxi.setVisible(false);
                    taxi.reiniciarAnimacao();
                    basicCar.animacaoAtivada = false;
                    basicCar.setVisible(false);
                    basicCar.reiniciarAnimacao();
                    onibus.animacaoAtivada = false;
                    onibus.setVisible(false);
                    onibus.reiniciarAnimacao();
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
                    if (missaoConcluida) {
                        jogo.objetoMissao.setVisible(false);
                        jogo.objetoPlaca1.setVisible(true);
                    }
                }
            }
        } else if (missaoId == 2) {
            // trocaMissao();
        }
    }

    public void trocaMissao() {
        if (controlaTrocaMissao) {
            atualizarAlertas();
            controlaTrocaMissao = false;
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
        }
    }

    /**
     * Atualiza os alertas da missão
     * 
     * @param jogo Instância do jogo
     */
    private void atualizarAlertas() {
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
