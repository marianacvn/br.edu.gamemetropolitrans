package br.edu.metropolitrans.model.connection;

import br.edu.metropolitrans.model.dao.CourseDAO;
import br.edu.metropolitrans.model.dao.MissionDataDAO;

public class SaveManager {

    private static final int MAX_SAVES = 5;
    private static int saveAtual = 0;

    public static void criarNovoSave() {
        if (saveAtual < MAX_SAVES) {
            saveAtual++;
            CourseDAO.criarNovoSave(saveAtual);
            MissionDataDAO.criarNovoSave(saveAtual);
        } else {
            System.out.println("Número máximo de saves atingido.");
        }
    }

    public static void definirSaveAtual(int saveId) {
        if (saveId > 0 && saveId <= MAX_SAVES) {
            saveAtual = saveId;
            CourseDAO.definirSaveAtual(saveId);
            MissionDataDAO.definirSaveAtual(saveId);
        } else {
            System.out.println("Save inválido.");
        }
    }

    public static int getSaveAtual() {
        return saveAtual;
    }
}
