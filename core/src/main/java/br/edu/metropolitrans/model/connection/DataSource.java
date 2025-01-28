package br.edu.metropolitrans.model.connection;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Classe de conexão com a base de dados, neste caso é JSON
 */
public class DataSource {

    private static final String PASTA_DATASOURCE = "files/datasource/";
    private static DataSource instancia;
    private Gson gson;

    private DataSource() {
        gson = new Gson();
    }

    /**
     * Retorna a instância da conexão
     * 
     * @return instância da conexão
     */
    public static DataSource getInstancia() {
        if (instancia == null) {
            instancia = new DataSource();
        }
        return instancia;
    }

    /**
     * Busca o Json referente a entidade e converte para o objeto
     */
    public <T> T conectar(String nomeArquivo, Class<T> clazz) {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(PASTA_DATASOURCE + nomeArquivo), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            Gdx.app.log("DataSource", "Erro ao carregar o arquivo JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Salva o objeto em um Json
     */
    public void desconectar(String nomeArquivo, Object dados) {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(PASTA_DATASOURCE + nomeArquivo), StandardCharsets.UTF_8)) {
            gson.toJson(dados, writer);
        } catch (IOException e) {
            Gdx.app.log("DataSource", "Erro ao salvar o arquivo JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}