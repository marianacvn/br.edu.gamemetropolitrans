package br.edu.metropolitrans.model;

import java.util.List;

public class GameData {
    private int missaoAtual;
    private GameDataPersonagem personagem;
    private List<GameDataNpc> npcs;

    public GameData() {
    }

    public GameData(int missaoAtual, GameDataPersonagem personagem, List<GameDataNpc> npcs) {
        this.missaoAtual = missaoAtual;
        this.personagem = personagem;
        this.npcs = npcs;
    }

    public int getMissaoAtual() {
        return missaoAtual;
    }

    public void setMissaoAtual(int missaoAtual) {
        this.missaoAtual = missaoAtual;
    }

    public GameDataPersonagem getPersonagem() {
        return personagem;
    }

    public void setPersonagem(GameDataPersonagem personagem) {
        this.personagem = personagem;
    }

    public List<GameDataNpc> getNpcs() {
        return npcs;
    }

    public void setNpcs(List<GameDataNpc> npcs) {
        this.npcs = npcs;
    }

}
