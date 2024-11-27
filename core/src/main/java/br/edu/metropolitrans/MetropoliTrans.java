package br.edu.metropolitrans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;

import br.edu.metropolitrans.view.screens.MenuScreen;

public class MetropoliTrans extends Game {
    public String DIRETORIO_BASE_ARQUIVOS = "files/";
    public Stage estagioPrincipal;
    public SpriteBatch batch;
    public BitmapFont fonte;
    // Musica do jogo
    public Music MusicaPrincipal;
    // Musica do menu
    public Music MusicaMenu;
    public HashMap<String, Screen> telas = new HashMap<>();

    @Override
    public void create() {
        estagioPrincipal = new Stage();
        batch = new SpriteBatch();

        // Carrega a fonte personalizada
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("files/fonts/Silver.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30; // Define o tamanho da fonte
        fonte = generator.generateFont(parameter);
        // Libera os recursos do gerador de fontes
        generator.dispose();

        // Carrega a música
        MusicaPrincipal = Gdx.audio.newMusic(Gdx.files.internal("files/songs/lofiSong.mp3"));
        MusicaPrincipal.setLooping(true);
        MusicaPrincipal.setVolume(0.5f);

        // Carrega a música do menu
        MusicaMenu = Gdx.audio.newMusic(Gdx.files.internal("files/songs/lofi-ambient.mp3"));
        MusicaMenu.setLooping(true);
        MusicaMenu.setVolume(0.5f);

        // Inicia a reprodução da música do menu
        MusicaMenu.play();

        telas.put("menu", new MenuScreen(this));
        this.setScreen(telas.get("menu"));
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        fonte.dispose();
        MusicaPrincipal.dispose();
        MusicaMenu.dispose();
    }
}
