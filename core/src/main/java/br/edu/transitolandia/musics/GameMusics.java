package br.edu.transitolandia.musics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public enum GameMusics {

    GAMEMUSIC(loadMusic("files/songs/lofiSong.mp3")),;

    public Music music;

    GameMusics(Music music) {
        this.music = music;
    }

    private static Music loadMusic(String filePath) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        if (music == null) {
            System.out.println("Erro: Música não carregada corretamente.");
        }
        return music;
    }
}
