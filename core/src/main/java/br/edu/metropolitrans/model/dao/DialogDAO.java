package br.edu.metropolitrans.model.dao;

import br.edu.metropolitrans.model.Dialog;
import br.edu.metropolitrans.model.connection.DataSource;

public class DialogDAO {

    public Dialog carregarDialogos(String personagem) {
        DataSource ds = DataSource.getInstancia();
        return ds.conectar(personagem + ".json", Dialog.class);
    }

    public void salvarDialogos(String personagem, Dialog dialog) {
        DataSource ds = DataSource.getInstancia();
        ds.desconectar(personagem + ".json", dialog);
    }

}