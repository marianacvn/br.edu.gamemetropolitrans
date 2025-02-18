package br.edu.metropolitrans.model.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.edu.metropolitrans.model.utils.DebugMode;

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
    private String arquivoAtualCursos;
    private String arquivoAtualMissoes;
    private String arquivoAtualDadosJogo;

    private DataSource() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        arquivoAtualCursos = "courses.json"; // Arquivo padrão para cursos
        arquivoAtualMissoes = "missions.json"; // Arquivo padrão para missões
        arquivoAtualDadosJogo = "game-data.json"; // Arquivo padrão para dados do jogo
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
     * Define o arquivo de save atual para cursos
     */
    public void setArquivoAtualCursos(String nomeArquivo) {
        this.arquivoAtualCursos = nomeArquivo;
    }

    /**
     * Define o arquivo de save atual para missões
     */
    public void setArquivoAtualMissoes(String nomeArquivo) {
        this.arquivoAtualMissoes = nomeArquivo;
    }

    /**
     * Define o arquivo de save atual para dados do jogo
     */
    public void setArquivoAtualDadosJogo(String nomeArquivo) {
        this.arquivoAtualDadosJogo = nomeArquivo;
    }

    private String selecionarArquivos(String tipo) {
        if (tipo.equals("cursos")) {
            return arquivoAtualCursos;
        } else if (tipo.equals("missoes")) {
            return arquivoAtualMissoes;
        } else if (tipo.equals("jogo")) {
            return arquivoAtualDadosJogo;
        } else {
            // Se não for nenhum dos três, o tipo é o caminho do arquivo
            return tipo;
        }
    }

    /**
     * Busca o Json referente a entidade e converte para o objeto
     */
    public <T> T conectar(Class<T> clazz, String tipo) {
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(PASTA_DATASOURCE + selecionarArquivos(tipo)),
                StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, clazz);
        } catch (IOException e) {
            DebugMode.mostrarLog("DataSource", "Erro ao carregar o arquivo JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Salva o objeto em um Json
     */
    public void desconectar(Object dados, String tipo) {
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(PASTA_DATASOURCE + selecionarArquivos(tipo)),
                StandardCharsets.UTF_8)) {
            gson.toJson(dados, writer);
        } catch (IOException e) {
            DebugMode.mostrarLog("DataSource", "Erro ao salvar o arquivo JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cria uma cópia do arquivo de save
     */
    public void criarCopia(String novoArquivo, String tipo) {
        try (FileInputStream fis = new FileInputStream(PASTA_DATASOURCE + selecionarArquivos(tipo));
                FileOutputStream fos = new FileOutputStream(PASTA_DATASOURCE + novoArquivo)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            DebugMode.mostrarLog("DataSource", "Cópia do arquivo JSON criada: " + novoArquivo);
        } catch (IOException e) {
            DebugMode.mostrarLog("DataSource", "Erro ao criar a cópia do arquivo JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}