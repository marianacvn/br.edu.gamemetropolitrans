package br.edu.metropolitrans;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import br.edu.metropolitrans.controller.Controller;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.ObjetoInterativo;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.model.maps.Mapas;
import br.edu.metropolitrans.view.screens.MenuScreen;

public class MetropoliTrans extends Game {

    /**
     * Diretório base dos arquivos do jogo
     */
    public String DIRETORIO_BASE_ARQUIVOS = "files/";

    /**
     * Palco principal do jogo
     */
    public Stage estagioPrincipal;

    /**
     * SpriteBatch do jogo
     */
    public SpriteBatch batch;

    /**
     * Fonte personalizada do jogo
     */
    public BitmapFont fonte;

    /**
     * Música do menu
     */
    public Music MusicaMenu;

    /**
     * Lista de telas do jogo
     */
    public HashMap<String, Screen> telas = new HashMap<>();

    /**
     * Personagem do jogo
     */
    public Personagem personagem;

    /**
     * Lista de NPCs do jogo
     */
    public ArrayList<Npc> npcs;

    /**
     * Objetos interativos
     */
    public ObjetoInterativo objeto, objetoSairSala;

    /**
     * Mapas do jogo
     */
    public Mapas mapas;

    /**
     * Usado para renderizar o mapa
     */
    public OrthogonalTiledMapRenderer mapaRenderizador;
    
    /**
     * Objetos de colisão do mapa
     */
    public MapObjects objetosColisao;

    /**
     * Retângulos de colisão
     */
    public Array<Rectangle> retangulosColisao;

    /**
     * Controle do jogo
     */
    public Controller controller;

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

        // Carrega a música do menu
        MusicaMenu = Gdx.audio.newMusic(Gdx.files.internal("files/songs/lofi-ambient.mp3"));
        MusicaMenu.setLooping(true);
        MusicaMenu.setVolume(0.5f);
        
        // Carrega o mapa
        mapas = new Mapas();// Carrega o mapa
        mapaRenderizador = new OrthogonalTiledMapRenderer(mapas.mapa, 1, batch);

        // Carrega o personagem
        personagem = new Personagem(640, 250, estagioPrincipal);
        Personagem.setLimitacaoMundo(Mapas.MAPA_LARGURA, Mapas.MAPA_ALTURA);

        // Carrega os NPCs
        // Adiciona os npcs em um array
        npcs = new ArrayList<Npc>();

        // Carrega os Npcs
        npcs.add(new Npc("maria", 280, 1050, "maria/sprite.png", estagioPrincipal ));
        npcs.add(new Npc("betania", 150, 400, "betania/sprite.png", estagioPrincipal, 1));
        npcs.add(new Npc("bruna", 1190, 200, "bruna/sprite.png", estagioPrincipal));
        npcs.add(new Npc("antonio", 1500, 1000, "antonio/sprite.png", estagioPrincipal));
        npcs.add(new Npc("heberto", 150, 200, "heberto/sprite.png", estagioPrincipal, 1));
        npcs.add(new Npc("jose", 90, 1450, "jose/sprite.png", estagioPrincipal));
        npcs.add(new Npc("josinaldo", 2090, 150, "josinaldo/sprite.png", estagioPrincipal));
        npcs.add(new Npc("paulo", 1500, 100, "paulo/sprite.png", estagioPrincipal));
        npcs.add(new Npc("juliana", 1200, 1250, "juliana/sprite.png", estagioPrincipal));

        // Adiciona os npcs no array de colisão
        personagem.npcs = npcs;

        objeto = new ObjetoInterativo("entradaPrefeitura", 32, 220, "background-transparent.png",
                estagioPrincipal);

        // Inicia a reprodução da música do menu
        MusicaMenu.play();

        // Cria o controle do jogo
        controller = new Controller(this);

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
        MusicaMenu.dispose();
        estagioPrincipal.dispose();
    }
}
