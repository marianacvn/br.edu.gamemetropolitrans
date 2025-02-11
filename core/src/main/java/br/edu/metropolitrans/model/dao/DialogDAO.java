package br.edu.metropolitrans.model.dao;

import br.edu.metropolitrans.model.Dialog;
import br.edu.metropolitrans.model.connection.DataSource;

public class DialogDAO {

    public static Dialog carregarDialogos(String personagem) {
        DataSource ds = DataSource.getInstancia();
        return ds.conectar(Dialog.class, "dialogs/" + personagem + ".json");
    }

    public static void salvarDialogos(String personagem, Dialog dialog) {
        DataSource ds = DataSource.getInstancia();
        ds.desconectar(dialog, "dialogs/" + personagem + ".json");
    }
}