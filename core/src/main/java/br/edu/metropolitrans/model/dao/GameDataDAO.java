package br.edu.metropolitrans.model.dao;

import br.edu.metropolitrans.model.GameData;
import br.edu.metropolitrans.model.connection.DataSource;
import br.edu.metropolitrans.model.utils.DebugMode;

/**
 * Carrega o acesso aos dados dos jogo
 * 
 */

public class GameDataDAO {

    /**
     * Carrega os dados dos jogo
     * 
     * @return Dados dos jogo
     */

    public static GameData carregarDadosJogo(String tipo) {
        DataSource ds = DataSource.getInstancia();
        DebugMode.mostrarLog("GameDataDAO", "Tentando carregar dados do jogo do arquivo JSON.");
        GameData gameData = ds.conectar(GameData.class, tipo);
        if (gameData != null) {
            DebugMode.mostrarLog("GameDataDAO", "Dados do jogo carregados com sucesso.");
        } else {
            DebugMode.mostrarLog("GameDataDAO", "Falha ao carregar os dados do jogo.");
        }
        return gameData;
    }

    /**
     * Atualiza os dados do jogo
     * 
     * @param id     Id do jogo
     * @param status Novo status do jogo
     **/

    public static void atualizaDadosJogo(int id, GameData gameData) {
        if (gameData == null) {
            DebugMode.mostrarLog("GameDataDAO", "GameData está nulo.");
            return;
        }
        DataSource ds = DataSource.getInstancia();
        ds.desconectar(gameData, "jogo");
    }

    /**
     * Cria um novo save
     * 
     * @param saveId Id do novo save
     */
    public static void criarNovoSave(int saveId) {
        DataSource ds = DataSource.getInstancia();
        String novoArquivo = "save" + saveId + "-game-data.json";
        ds.criarCopia(novoArquivo, "jogo");
        ds.setArquivoAtualDadosJogo(novoArquivo);
    }

    /**
     * Define o save atual
     * 
     * @param saveId Id do save
     */
    public static void definirSaveAtual(int saveId) {
        DataSource ds = DataSource.getInstancia();
        String arquivoAtual = "save" + saveId + "-game-data.json";
        ds.setArquivoAtualDadosJogo(arquivoAtual);
    }
}