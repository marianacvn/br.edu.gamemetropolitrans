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

    // public static void salvarDialogos(String personagem, Dialog dialog) {
    // DataSource ds = DataSource.getInstancia();
    // ds.desconectar(personagem + ".json", dialog);
    // }

}
