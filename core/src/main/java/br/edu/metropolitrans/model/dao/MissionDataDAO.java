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
        return ds.conectar(MissionData.class, "missoes");
    }

    /**
     * Busca uma missão pelo id
     * 
     * @param id Id da missão
     * @return Missão
     */
    public static Mission buscaMissaoPorId(int id) {
        MissionData missionData = carregarDadosMissoes();

        if (missionData != null) {
            for (Mission mission : missionData.getMissoes()) {
                if (mission.getId() == id) {
                    return mission;
                }
            }
        }
        
        return null;
    }

    /**
     * Atualiza a missão ou submissão
     * 
     * @param mission Missão
     */
    public static void atualizarMissao(Mission mission) {
        MissionData missionData = carregarDadosMissoes();
        for (Mission m : missionData.getMissoes()) {
            if (m.getId() == mission.getId()) {
                m.setFinalizouMissao(mission.isFinalizouMissao());
            }
        }
        DataSource ds = DataSource.getInstancia();
        ds.desconectar(missionData, "missoes");
    }

    /**
     * Cria um novo save
     * 
     * @param saveId Id do novo save
     */
    public static void criarNovoSave(int saveId) {
        DataSource ds = DataSource.getInstancia();
        String novoArquivo = "save" + saveId + "-missions.json";
        ds.criarCopia(novoArquivo, "missoes");
        ds.setArquivoAtualMissoes(novoArquivo);
    }

    /**
     * Define o save atual
     * 
     * @param saveId Id do save
     */
    public static void definirSaveAtual(int saveId) {
        DataSource ds = DataSource.getInstancia();
        String arquivoAtual = "save" + saveId + "-missions.json";
        ds.setArquivoAtualMissoes(arquivoAtual);
    }

    /**
     * Volta os estados dos arquivos de save para os estados iniciais
     * @param saveId ID do save
     */
    public static void voltarSaveParaEstadosIniciais(int saveId) {
        DataSource ds = DataSource.getInstancia();
        String novoArquivo = "save" + saveId + "-missions.json";	
        ds.criarCopia(novoArquivo, "missions.json");
        ds.setArquivoAtualMissoes(novoArquivo);
    }
}