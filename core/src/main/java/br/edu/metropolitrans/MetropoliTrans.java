package br.edu.metropolitrans;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import br.edu.metropolitrans.controller.Controller;
import br.edu.metropolitrans.model.actors.BasicAnimation;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.ObjetoInterativo;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.model.actors.Vehicle;
import br.edu.metropolitrans.model.maps.Mapas;
import br.edu.metropolitrans.model.utils.DebugMode;
import br.edu.metropolitrans.view.components.mission_modal.MissionComponents;
import br.edu.metropolitrans.view.font.FontBase;
import br.edu.metropolitrans.view.screens.LoadingScreen;

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
    public Music musicaMenu;

    /**
     * Efeito sonoro de buzina
     */
    public Music efeitoBuzina;

    /**
     * Map de telas do jogo
     */
    public HashMap<String, Screen> telas = new HashMap<>();

    /**
     * Personagem do jogo
     */
    public Personagem personagem;

    /**
     * Lista de NPCs do jogo
     */
    public HashMap<String, Npc> npcs;

    /**
     * Map de Veículos do jogo
     */
    public HashMap<String, Vehicle> vehicles = new HashMap<>();

    /**
     * Objetos interativos
     */
    public ObjetoInterativo objeto, objetoChao, objetoSairSala, objetoMissao, objetoPlaca1, objetoPlaca2, objetoPlaca3,
            objetoPlaca5, objetoPlaca6,
            objetoPc;

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

    public MapObjects objetosPista;

    /**
     * Retângulos de colisão
     */
    public Array<Rectangle> retangulosColisao;

    public Array<Rectangle> retangulosPista;

    /**
     * Controle do jogo
     */
    public Controller controller;

    public HashMap<String, MissionComponents> missionComponents = new HashMap<>();

    public BasicAnimation explosao, bike;

    @Override
    public void create() {
        // Inicia a tela de loading do game
        this.setScreen(new LoadingScreen(this));

    }

    public void inicializarJogo() {
        estagioPrincipal = new Stage();
        batch = new SpriteBatch();

        fonte = FontBase.getInstancia().getFonte(30, FontBase.Fontes.PADRAO);

        // Carrega a música do menu
        musicaMenu = Gdx.audio.newMusic(Gdx.files.internal("files/songs/lofi-ambient.mp3"));
        musicaMenu.setLooping(true);
        musicaMenu.setVolume(0.5f);

        efeitoBuzina = Gdx.audio.newMusic(Gdx.files.internal("files/songs/buzina.mp3"));
        efeitoBuzina.setLooping(true);
        efeitoBuzina.setVolume(0.5f);

        // Carrega o mapa
        mapas = new Mapas();// Carrega o mapa
        mapaRenderizador = new OrthogonalTiledMapRenderer(mapas.mapa, 1, batch);

        // Carrega o objeto interativo das missões
        objetoMissao = new ObjetoInterativo("alertaMissao", 1290, 1245, "mission-alert.png",
                estagioPrincipal);
        objetoMissao.setVisible(false);

        // Carrega o objeto do chão
        objetoChao = new ObjetoInterativo("chao", 320, 1210, "asphalt-obj.png", estagioPrincipal);

        // Carrega o objeto interativo da placa das missões
        objetoPlaca1 = new ObjetoInterativo("placa", 1290, 1245, "mission1-result.png", estagioPrincipal);
        objetoPlaca1.setVisible(false);

        objetoPlaca2 = new ObjetoInterativo("placa2", 1700, 1450, "mission2-result.png", estagioPrincipal);
        objetoPlaca2.setVisible(false);

        objetoPlaca3 = new ObjetoInterativo("placa3", 380, 1450, "mission3-result.png", estagioPrincipal);
        objetoPlaca3.setVisible(false);

        objetoPlaca5 = new ObjetoInterativo("placa5", 1956, 512, "mission5-result.png", estagioPrincipal);
        objetoPlaca5.setVisible(false);

        objetoPlaca6 = new ObjetoInterativo("placa6", 1480, 256, "mission6-result.png", estagioPrincipal);
        objetoPlaca6.setVisible(false);

        // Carrega o objeto interativo do PC no mapa room
        objetoPc = new ObjetoInterativo("pc", 1020, 1470, "background-transparent.png", estagioPrincipal);
        // objetoPc.setVisible(false);

        // Carrega o personagem
        personagem = new Personagem(250, 860, estagioPrincipal);
        Personagem.setLimitacaoMundo(Mapas.MAPA_LARGURA, Mapas.MAPA_ALTURA);

        // Carrega os NPCs
        // Adiciona os npcs em um array
        npcs = new HashMap<>();

        // Carrega os Npcs
        npcs.put("maria", new Npc("maria", 280, 1220, "maria/sprite.png", estagioPrincipal, true));
        npcs.put("betania", new Npc("betania", 264, 200, "betania/sprite.png", estagioPrincipal, false));
        npcs.put("bruna", new Npc("bruna", 1185, 1850, "bruna/sprite.png", estagioPrincipal, false));
        npcs.put("antonio", new Npc("antonio", 1485, 1130, "antonio/sprite.png", estagioPrincipal, false));
        npcs.put("heberto", new Npc("heberto", 25, 650, "heberto/sprite.png", estagioPrincipal, 1, false));
        npcs.put("jose", new Npc("jose", 90, 1450, "jose/sprite.png", estagioPrincipal, false));
        npcs.put("josinaldo", new Npc("josinaldo", 2090, 150, "josinaldo/sprite.png", estagioPrincipal, false));
        npcs.put("paulo", new Npc("paulo", 1500, 100, "paulo/sprite.png", estagioPrincipal, false));
        npcs.put("juliana", new Npc("juliana", 1185, 1130, "juliana/sprite.png", estagioPrincipal, false));

        // Adiciona os npcs no array de colisão
        personagem.npcs = npcs;

        // Carrega os veículos
        vehicles.put(
                "taxi",
                new Vehicle("taxi", 1266, 1000, 50, "taxi-sprite.png", estagioPrincipal,
                        List.of("C-8*32", "E-8*32", "B-10*32"), true));
        vehicles.put(
                "basic-car",
                new Vehicle("basic-car", 394, 940, 100, "car-sprite.png", estagioPrincipal,
                        List.of("C-10*32", "D-12*32", "B-11*32"), true));
        vehicles.put(
                "onibus",
                new Vehicle("onibus", 0, 1260, 50, "bus-sprite.png", estagioPrincipal,
                        List.of("D-9*32", "B-18*32"), true));
        vehicles.put(
                "basic-car-2",
                new Vehicle("basic-car-2", 2086, 1340, 100, "sedan-gray-sprite.png", estagioPrincipal,
                        List.of("E-24*32"), true));
        vehicles.put(
                "basic-car-3",
                new Vehicle("basic-car-2", 1300, 582, 100, "coupe-midnight-sprite.png", estagioPrincipal,
                        List.of("C-24*32"), true));
        vehicles.put(
                "compact-car",
                new Vehicle("compact-car", 380, 1335, 0, "compact-red-sprite.png", estagioPrincipal,
                        List.of("E-1*32"), true));
        vehicles.put(
                "sport-blue-car",
                new Vehicle("sport-blue-car", 385, 1515, 20, "sport-blue-sprite.png", estagioPrincipal,
                        List.of("B-2*16"), true));
        vehicles.put(
                "black-viper-car",
                new Vehicle("black-viper-car", 1840, 50, 150, "black-viper-sprite.png", estagioPrincipal,
                        List.of("C-20*32"), true));

        // Instância animação de explosão
        explosao = new BasicAnimation(1350, 1350, estagioPrincipal);
        String[] nomeArquivos = {
                "files/animation/explosion/4.png",
                "files/animation/explosion/5.png",
                "files/animation/explosion/6.png",
                "files/animation/explosion/7.png",
                "files/animation/explosion/8.png",
                "files/animation/explosion/9.png",
                "files/animation/explosion/10.png"
        };
        explosao.carregaAnimacaoDeArquivos(
                nomeArquivos, 0.1f, true);
        explosao.setVisible(false);

        // Carrega os objetos interativos
        objeto = new ObjetoInterativo("entradaPrefeitura", 100, 760, "background-transparent.png",
                estagioPrincipal);

        // Inicia a reprodução da música do menu
        musicaMenu.play();

        // Cria o controle do jogo
        controller = new Controller(this);
    }

    public void reiniciarJogo() {

        setPausado(true);
        DebugMode.mostrarLog("MetropoliTrans", "Parando o jogo e voltando para o Menu...");

        // Troca a tela voltando para a tela de início
        trocarTela("menu");

        // Reinicia os valores Padrões
        DebugMode.mostrarLog("MetropoliTrans", "Reiniciando os valores padrões.");
        controller.MISSAO = 0;

        // Limpa todas as telas
        DebugMode.mostrarLog("MetropoliTrans", "Excluindo todas as telas...");
        telas.clear();

        // Retoma o jogo
        setPausado(false);
        DebugMode.mostrarLog("MetropoliTrans", "Retomando o jogo...");

        // Inicializa o jogo novamente
        DebugMode.mostrarLog("MetropoliTrans", "Inicializando o jogo novamente...");
        inicializarJogo();
    }

    public void trocarTela(String tela) {
        this.setScreen(telas.get(tela));
    }

    public void setPausado(boolean pausado) {
        if (pausado) {
            Gdx.app.postRunnable(() -> Gdx.graphics.setContinuousRendering(false));
        } else {
            Gdx.app.postRunnable(() -> Gdx.graphics.setContinuousRendering(true));
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        // OBS.: É necessário entender se o dispose náo está impedindo o funcionamento
        // do jogo
        // Descarte de telas
        for (Screen screen : telas.values()) {
            if (screen != null) {
                screen.dispose();
            }
        }
        telas.clear();

        // Descarte de atores
        if (personagem != null)
            personagem.dispose();

        npcs.forEach((nome, npc) -> {
            if (npc != null)
                npc.dispose();
        });
        npcs.clear();

        for (Vehicle vehicle : vehicles.values()) {
            if (vehicle != null)
                vehicle.dispose();
        }
        vehicles.clear();

        // Descarte de objetos interativos
        if (objeto != null)
            objeto.dispose();
        if (objetoChao != null)
            objetoChao.dispose();
        if (objetoSairSala != null)
            objetoSairSala.dispose();
        if (objetoMissao != null)
            objetoMissao.dispose();
        if (objetoPlaca1 != null)
            objetoPlaca1.dispose();
        if (objetoPc != null)
            objetoPc.dispose();

        // Descarte de mapas
        if (mapas != null && mapas.mapa != null)
            mapas.mapa.dispose();

        if (mapas != null && mapas.sala != null)
            mapas.sala.dispose();

        // Descarte de renderizadores
        if (mapaRenderizador != null)
            mapaRenderizador.dispose();

        // Dispose of other resources
        if (batch != null) {
            batch.dispose();
        }
        if (fonte != null) {
            fonte.dispose();
        }
        if (musicaMenu != null) {
            musicaMenu.dispose();
        }
        if (estagioPrincipal != null) {
            estagioPrincipal.dispose();
        }
    }
}
