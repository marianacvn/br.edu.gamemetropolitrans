package br.edu.metropolitrans.model.dao;

import br.edu.metropolitrans.model.Mission;
import br.edu.metropolitrans.model.MissionData;
import br.edu.metropolitrans.model.connection.DataSource;

/**
 * Classe de acesso a dados das missões
 */
public class MissionDataDAO {

    /**
     * Carrega os dados das missões
     * 
     * @return Dados das missões
     */
    public static MissionData carregarDadosMissoes() {
        DataSource ds = DataSource.getInstancia();
        return ds.conectar("missions.json", MissionData.class);
    }

    /**
     * Busca uma missão pelo id
     * 
     * @param id Id da missão
     * @return Missão
     */
    public static Mission buscaMissaoPorId(int id) {
        MissionData missionData = carregarDadosMissoes();
        for (Mission mission : missionData.getMissoes()) {
            if (mission.getId() == id) {
                return mission;
            }
        }
        return null;
    }

    /**
     * Atualiza a missão ou submissão
     * 
     * @param mission     Missão
     * @param isSubmissao Se é submissão
     */
    public static void atualizarMissao(Mission mission) {
        MissionData missionData = carregarDadosMissoes();
        for (Mission m : missionData.getMissoes()) {
            if (m.getId() == mission.getId()) {
                m.setFinalizouMissao(mission.isFinalizouMissao());
            }
        }
        DataSource ds = DataSource.getInstancia();
        ds.desconectar("missions.json", missionData);
    }

}
