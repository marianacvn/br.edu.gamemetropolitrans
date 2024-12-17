package br.edu.metropolitrans.model;

import java.util.List;

public class Mission {
    private int id;
    private String nome;
    private String descricao;
    private int recompensaMoedas;
    private int experiencia;
    private int valorErro;
    private List<String> personagens;

    public Mission() {
    }

    public Mission(int id, String nome, String descricao, int recompensaMoedas, int experiencia, int valorErro,
            List<String> personagens) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.recompensaMoedas = recompensaMoedas;
        this.experiencia = experiencia;
        this.valorErro = valorErro;
        this.personagens = personagens;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getRecompensaMoedas() {
        return recompensaMoedas;
    }

    public void setRecompensaMoedas(int recompensaMoedas) {
        this.recompensaMoedas = recompensaMoedas;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public int getValorErro() {
        return valorErro;
    }

    public void setValorErro(int valorErro) {
        this.valorErro = valorErro;
    }

    public List<String> getPersonagens() {
        return personagens;
    }

    public void setPersonagens(List<String> personagens) {
        this.personagens = personagens;
    }
}