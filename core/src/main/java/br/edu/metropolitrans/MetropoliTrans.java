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
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import br.edu.metropolitrans.controller.Controller;
import br.edu.metropolitrans.model.actors.ExplosionAnimation;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.ObjetoInterativo;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.model.actors.Vehicle;
import br.edu.metropolitrans.model.maps.Mapas;
import br.edu.metropolitrans.model.utils.DebugMode;
import br.edu.metropolitrans.view.components.mission_modal.MissionComponents;
import br.edu.metropolitrans.view.font.FontBase;
import br.edu.metropolitrans.view.screens.GameScreen;
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
    public Music MusicaMenu;

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
    public ArrayList<Npc> npcs;

    /**
     * Map de Veículos do jogo
     */
    public HashMap<String, Vehicle> vehicles = new HashMap<>();

    /**
     * Objetos interativos
     */
    public ObjetoInterativo objeto, objetoChao, objetoSairSala, objetoMissao, objetoPlaca1, objetoPlaca2, objetoPlaca3,
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

    public ExplosionAnimation explosao;

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
        MusicaMenu = Gdx.audio.newMusic(Gdx.files.internal("files/songs/lofi-ambient.mp3"));
        MusicaMenu.setLooping(true);
        MusicaMenu.setVolume(0.5f);

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

        // Carrega o objeto interativo do PC no mapa room
        objetoPc = new ObjetoInterativo("pc", 1020, 1470, "background-transparent.png", estagioPrincipal);
        // objetoPc.setVisible(false);

        // Carrega o personagem
        personagem = new Personagem(250, 860, estagioPrincipal);
        Personagem.setLimitacaoMundo(Mapas.MAPA_LARGURA, Mapas.MAPA_ALTURA);

        // Carrega os NPCs
        // Adiciona os npcs em um array
        npcs = new ArrayList<Npc>();

        // Carrega os Npcs
        npcs.add(new Npc("maria", 280, 1220, "maria/sprite.png", estagioPrincipal, true));
        npcs.add(new Npc("betania", 264, 200, "betania/sprite.png", estagioPrincipal, false));
        npcs.add(new Npc("bruna", 1190, 200, "bruna/sprite.png", estagioPrincipal, false));
        npcs.add(new Npc("antonio", 1485, 1130, "antonio/sprite.png", estagioPrincipal, false));
        npcs.add(new Npc("heberto", 25, 650, "heberto/sprite.png", estagioPrincipal, 1, false));
        npcs.add(new Npc("jose", 90, 1450, "jose/sprite.png", estagioPrincipal, false));
        npcs.add(new Npc("josinaldo", 2090, 150, "josinaldo/sprite.png", estagioPrincipal, false));
        npcs.add(new Npc("paulo", 1500, 100, "paulo/sprite.png", estagioPrincipal, false));
        npcs.add(new Npc("juliana", 1200, 1130, "juliana/sprite.png", estagioPrincipal, false));

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
            new Vehicle("sport-blue-car", 385, 1500, 20, "sport-blue-sprite.png", estagioPrincipal,
                    List.of("B-2*16"), true));
            
        // Instância animação de explosão
        explosao = new ExplosionAnimation(1350, 1350, estagioPrincipal);
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
        MusicaMenu.play();

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

    public void inicializarComponentesMissao() {
        float baseX = ((GameScreen.TELA_LARGURA - 530) / 2);
        float baseY = (GameScreen.TELA_ALTURA - 400) / 2;

        MissionComponents missao0 = new MissionComponents(0, this);
        missao0.adicionarTituloMissao("Missão " + controller.MISSAO + ": ", baseX + 15, baseY);
        missionComponents.put("missao0", missao0);
        missao0.adicionarOpcaoImagem("mission0-option1", "mission0-option1.png", true, baseX + 5,
                baseY + 230, false);
        missao0.adicionarOpcaoImagem("mission0-option2", "mission0-option2.png", false, baseX + 65,
                baseY + 35, false);
        missao0.adicionarImagemCena("mission0-scene.png", baseX + 180, baseY + 15);
        missionComponents.put("missao0", missao0);

        MissionComponents missao1 = new MissionComponents(1, this);
        missao1.adicionarTituloMissao("Missão " + controller.MISSAO + ": ", baseX + 15, baseY);
        missao1.adicionarOpcaoImagem("mission1-option1", "mission1-option1_reduced.png", false, baseX + 15,
                baseY + 265, true);
        missao1.adicionarOpcaoImagem("mission1-option2", "mission1-option2_reduced.png", false, baseX + 15,
                baseY + 265 - 50 - 15, true);
        missao1.adicionarOpcaoImagem("mission1-option3", "mission1-option3_reduced.png", true, baseX + 15,
                baseY + 200 - 50 - 15, true);
        missao1.adicionarOpcaoImagem("mission1-option4", "mission1-option4_reduced.png", false, baseX + 15,
                baseY + 135 - 50 - 15, true);
        missao1.adicionarOpcaoImagem("mission1-option5", "mission1-option5_reduced.png", false, baseX + 15,
                baseY + 70 - 50 - 15, true);
        missao1.adicionarImagemCena("mission1-scene.png", baseX + 150, baseY + 15);
        missionComponents.put("missao1", missao1);

        MissionComponents missao2 = new MissionComponents(2, this);
        missao2.adicionarTituloMissao("Missão " + controller.MISSAO + ": ", baseX + 15, baseY);
        missao2.adicionarOpcaoImagem("mission2-option1", "mission2-option1.png", false, baseX + 15,
                baseY + 265, true);
        missao2.adicionarOpcaoImagem("mission2-option2", "mission2-option2.png", false, baseX + 15,
                baseY + 265 - 50 - 15, true);
        missao2.adicionarOpcaoImagem("mission2-option3", "mission2-option3.png", false, baseX + 15,
                baseY + 200 - 50 - 15, true);
        missao2.adicionarOpcaoImagem("mission2-option4", "mission2-option4.png", true, baseX + 15,
                baseY + 135 - 50 - 15, true);
        missao2.adicionarOpcaoImagem("mission2-option5", "mission2-option5.png", false, baseX + 15,
                baseY + 70 - 50 - 15, true);
        missao2.adicionarImagemCena("mission2-scene.png", baseX + 150, baseY + 15);
        missionComponents.put("missao2", missao2);

        MissionComponents missao3 = new MissionComponents(3, this);
        missao3.adicionarTituloMissao("Missão " + controller.MISSAO + ": ", baseX + 15, baseY);
        missao3.adicionarOpcaoImagem("mission3-option1", "mission3-option1.png", false, baseX + 15,
                baseY + 265, true);
        missao3.adicionarOpcaoImagem("mission3-option2", "mission3-option2.png", false, baseX + 15,
                baseY + 265 - 50 - 15, true);
        missao3.adicionarOpcaoImagem("mission3-option3", "mission3-option3.png", false, baseX + 15,
                baseY + 200 - 50 - 15, true);
        missao3.adicionarOpcaoImagem("mission3-option4", "mission3-option4.png", false, baseX + 15,
                baseY + 135 - 50 - 15, true);
        missao3.adicionarOpcaoImagem("mission3-option5", "mission3-option5.png", true, baseX + 15,
                baseY + 70 - 50 - 15, true);
        missao3.adicionarImagemCena("mission3-scene.png", baseX + 150, baseY + 15);
        missionComponents.put("missao3", missao3);
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

        for (Npc npc : npcs) {
            if (npc != null)
                npc.dispose();
        }
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
        if (MusicaMenu != null) {
            MusicaMenu.dispose();
        }
        if (estagioPrincipal != null) {
            estagioPrincipal.dispose();
        }
    }
}
