package br.edu.metropolitrans.model.connection;

import java.util.Date;

import com.badlogic.gdx.Gdx;

import br.edu.metropolitrans.model.ConfigData;
import br.edu.metropolitrans.model.ConfigSave;
import br.edu.metropolitrans.model.dao.ConfigDAO;
import br.edu.metropolitrans.model.dao.CourseDAO;
import br.edu.metropolitrans.model.dao.GameDataDAO;
import br.edu.metropolitrans.model.dao.MissionDataDAO;
import br.edu.metropolitrans.model.utils.DebugMode;

public class SaveManager {

    private static final int MAX_SAVES = 5;

    public static int verificaQualProximoSaveDis() {
        ConfigData configData = ConfigDAO.carregarConfig();
        for (int i = 1; i <= MAX_SAVES; i++) {
            try {
                configData.getSaveInfo().getSaves().get(i - 1);
            } catch (Exception e) {
                return i;
            }
        }
        return -1;
    }

    public static void criarNovoSave(int saveId) {
        ConfigData configData = ConfigDAO.carregarConfig();
        int quantidadeSaves = configData.getSaveInfo().getSaves() != null
                ? configData.getSaveInfo().getSaves().size()
                : 0;

        // Realiza a criação dos arquivos de save
        CourseDAO.criarNovoSave(saveId);
        MissionDataDAO.criarNovoSave(saveId);
        GameDataDAO.criarNovoSave(saveId);

        // Atualiza o arquivo de configuração, verificando se o save já existe
        // ou se é um novo save
        if (quantidadeSaves != 0) {
            ConfigSave configSave = configData.getSaveInfo().getSaves().get(0);
            configSave.setId(saveId);
            configSave.setName("save" + saveId);
            configSave.setDate((new Date()).toString());

            configData.getSaveInfo().getSaves().set(0, configSave);
        } else {
            ConfigSave configSave = new ConfigSave();
            configSave.setId(saveId);
            configSave.setName("save" + saveId);
            configSave.setDate((new Date()).toString());

            configData.getSaveInfo().getSaves().add(configSave);
        }


        ConfigDAO.salvarConfig(configData);
    }

    public static void definirSaveAtual(int saveId) {
        if (saveId > 0 && saveId <= MAX_SAVES) {
            CourseDAO.definirSaveAtual(saveId);
            MissionDataDAO.definirSaveAtual(saveId);
            GameDataDAO.definirSaveAtual(saveId);
        } else {
            DebugMode.mostrarLog("SaveManager", "Save inválido.");
        }
    }

    /**
     * Volta os estados dos arquivos de save para os estados iniciais
     * @param saveId ID do save
     */
    public static void voltarSaveParaEstadosIniciais(int saveId) {
        CourseDAO.voltarSaveParaEstadosIniciais(saveId);
        MissionDataDAO.voltarSaveParaEstadosIniciais(saveId);
        GameDataDAO.voltarSaveParaEstadosIniciais(saveId);
    }

    public static void removeArquivoSave(int saveId) {
        try {
            Gdx.files.local("files/datasource/save" + saveId + "-courses.json").delete();
            Gdx.files.local("files/datasource/save" + saveId + "-missions.json").delete();
            Gdx.files.local("files/datasource/save" + saveId + "-game-data.json").delete();
        } catch (Exception e) {
            DebugMode.mostrarLog("SaveManager", "Erro ao remover arquivo de save.");
        }
    }
}
