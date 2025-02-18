package br.edu.metropolitrans.model.dao;

import br.edu.metropolitrans.model.ConfigData;
import br.edu.metropolitrans.model.connection.DataSource;

public class ConfigDAO {

    public static ConfigData carregarConfig() {
        DataSource ds = DataSource.getInstancia();
        return ds.conectar(ConfigData.class, "config.json");
    }

    public static void salvarConfig(ConfigData config) {
        DataSource ds = DataSource.getInstancia();
        ds.desconectar(config, "config.json");
    }
}