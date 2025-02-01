package br.edu.metropolitrans.model.dao;

import br.edu.metropolitrans.model.CourseData;
import br.edu.metropolitrans.model.connection.DataSource;

import com.badlogic.gdx.Gdx;

import br.edu.metropolitrans.model.Course;
import br.edu.metropolitrans.model.Status;

/**
 * Carrega o acesso aos dados dos cursos
 * 
 */

public class CourseDAO {

    /**
     * Carrega os dados dos cursos
     * 
     * @return Dados dos cursos
     */

    public static CourseData carregarDadosCursos() {
        DataSource ds = DataSource.getInstancia();
        Gdx.app.log("CourseDAO", "Tentando carregar dados dos cursos do arquivo JSON.");
        CourseData courseData = ds.conectar("courses.json", CourseData.class);
        if (courseData != null) {
            Gdx.app.log("CourseDAO", "Dados dos cursos carregados com sucesso.");
        } else {
            Gdx.app.log("CourseDAO", "Falha ao carregar os dados dos cursos.");
        }
        return courseData;
    }

    /**
     * Busca um curso pelo id
     * 
     * @param id Id do curso
     * @return Curso
     */

    public static Course buscaCursoPorId(int id) {
        CourseData courseData = carregarDadosCursos();
        if (courseData == null) {
            Gdx.app.log("CourseDAO", "CourseData está nulo.");
            return null;
        }
        for (Course course : courseData.getCursos()) {
            if (course.getId() == id) {
                Gdx.app.log("CourseDAO", "Curso encontrado: " + course.getNome());
                return course;
            }
        }
        Gdx.app.log("CourseDAO", "Curso não encontrado para o id: " + id);
        return null;
    }

    /**
     * Atualiza o status do curso [Bloqueado, Liberado ou Concluído]
     * 
     * @param id     Id do curso
     * @param status Novo status do curso
     **/

    public static void atualizaStatusCurso(int id, String status) {
        CourseData courseData = carregarDadosCursos();
        if (courseData == null) {
            Gdx.app.log("CourseDAO", "CourseData está nulo.");
            return;
        }
        for (Course course : courseData.getCursos()) {
            if (course.getId() == id) {
                course.setStatus(Status.valueOf(status.toUpperCase()));
            }
        }
        DataSource ds = DataSource.getInstancia();
        ds.desconectar("courses.json", courseData);

    }

    /**
     * Carrega os dados de um módulo específico
     * 
     * @param id Id do módulo
     * @return Dados do módulo
     */
    public static Course carregarDadosModulo(int id) {
        Course course = buscaCursoPorId(id);
        if (course != null) {
            Gdx.app.log("CourseDAO", "Dados do módulo carregados: " + course.getNome());
        } else {
            Gdx.app.log("CourseDAO", "Dados do módulo não encontrados para o id: " + id);
        }
        return course;
    }

}
