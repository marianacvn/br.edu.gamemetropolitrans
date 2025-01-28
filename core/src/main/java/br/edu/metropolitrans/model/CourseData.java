package br.edu.metropolitrans.model;

import java.util.List;

public class CourseData {
    private List<Course> cursos;

    public CourseData() {
    }

    public List<Course> getCursos() {
        return cursos;
    }

    public void setCursos(List<Course> cursos) {
        this.cursos = cursos;
    }
}
