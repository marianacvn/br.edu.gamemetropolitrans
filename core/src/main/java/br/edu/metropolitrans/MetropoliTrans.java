package br.edu.metropolitrans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import br.edu.metropolitrans.controller.Controller;
import br.edu.metropolitrans.controller.MissionController;
import br.edu.metropolitrans.model.ConfigData;
import br.edu.metropolitrans.model.GameData;
import br.edu.metropolitrans.model.GameDataNpc;
import br.edu.metropolitrans.model.GameDataPersonagem;
import br.edu.metropolitrans.model.actors.BasicAnimation;
import br.edu.metropolitrans.model.actors.Npc;
import br.edu.metropolitrans.model.actors.ObjetoInterativo;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.model.actors.Vehicle;
import br.edu.metropolitrans.model.connection.SaveManager;
import br.edu.metropolitrans.model.dao.ConfigDAO;
import br.edu.metropolitrans.model.dao.GameDataDAO;
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
     * Efeitos sonoros de status
     */
    public Music efeitoAcerto, efeitoErro, efeitoNotificacao, efeitoCancelar, efeitoConfirmar, efeitoMoeda;

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
    public HashMap<String, ObjetoInterativo> objetosInterativos = new HashMap<>();

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

    /**
     * Controle de missão
     */
    public MissionController controleMissao;

    public HashMap<String, MissionComponents> missionComponents = new HashMap<>();

    public BasicAnimation explosao, bike;

    public void inicializarJogo() {
        estagioPrincipal = new Stage();
        batch = new SpriteBatch();

        // Carrega os NPCs
        npcs = new HashMap<>();

        fonte = FontBase.getInstancia().getFonte(30, FontBase.Fontes.PADRAO);

        // Carrega a configuração do jogo
        ConfigData config = ConfigDAO.carregarConfig();
        DebugMode.INFRACOES_ATIVAS = config.isTrafficViolations();
        DebugMode.DEBUG_MODE = DebugMode.TipoDebug.valueOf(config.getTypeDebugMode());

        // Carrega a música do menu
        musicaMenu = Gdx.audio.newMusic(Gdx.files.internal("files/songs/lofi-ambient.mp3"));
        musicaMenu.setLooping(true);
        musicaMenu.setVolume((float) config.getVolume());

        efeitoBuzina = Gdx.audio.newMusic(Gdx.files.internal("files/songs/buzina.mp3"));
        efeitoBuzina.setLooping(true);
        efeitoBuzina.setVolume((float) config.getVolume());

        // Carrega os efeitos sonoros
        efeitoAcerto = Gdx.audio.newMusic(Gdx.files.internal("files/songs/acerto.mp3"));
        efeitoAcerto.setLooping(false);
        efeitoAcerto.setVolume((float) config.getVolume());

        efeitoErro = Gdx.audio.newMusic(Gdx.files.internal("files/songs/perder.mp3"));
        efeitoErro.setLooping(false);
        efeitoErro.setVolume((float) config.getVolume());

        efeitoNotificacao = Gdx.audio.newMusic(Gdx.files.internal("files/songs/notificar.mp3"));
        efeitoNotificacao.setLooping(false);
        efeitoNotificacao.setVolume((float) config.getVolume());

        efeitoCancelar = Gdx.audio.newMusic(Gdx.files.internal("files/songs/cancelar.mp3"));
        efeitoCancelar.setLooping(false);
        efeitoCancelar.setVolume((float) config.getVolume());

        efeitoConfirmar = Gdx.audio.newMusic(Gdx.files.internal("files/songs/confirmar.mp3"));
        efeitoConfirmar.setLooping(false);
        efeitoConfirmar.setVolume((float) config.getVolume());

        efeitoMoeda = Gdx.audio.newMusic(Gdx.files.internal("files/songs/moeda.mp3"));
        efeitoMoeda.setLooping(false);
        efeitoMoeda.setVolume((float) config.getVolume());

        // Carrega o mapa
        mapas = new Mapas();// Carrega o mapa
        mapaRenderizador = new OrthogonalTiledMapRenderer(mapas.mapa, 1, batch);

        // Inicia a reprodução da música do menu
        musicaMenu.play();

        // Carrega os valores padrões do jogo
        setValoresDafault();

        // Carrega os dados do jogo
        atualizarJogoPorSaveGameData("game-data.json");

        // Cria o controle do jogo
        controller = new Controller(this);
        
        // Inicia o controle de missão
        controleMissao = new MissionController(this);
    }

    /**
     * Realiza a reinicialização do jogo
     */
    public void reiniciarJogo() {
        SaveManager.voltarSaveParaEstadosIniciais(1);

        DebugMode.mostrarLog("MetropoliTrans", "Reposicionando componentes do jogo e carregando dados padrões...");
        setValoresDafault();

        // Reposiciona o personagem para a posição inicial
        GameData gameData = GameDataDAO.carregarDadosJogo("game-data.json");

        // Atualiza os NPCs
        for (GameDataNpc npc : gameData.getNpcs()) {
            npcs.put(npc.getKey(), new Npc(npc.getKey(), npc.getX(), npc.getY(), npc.getKey() + "/sprite.png",
                    estagioPrincipal, npc.getStatusAlertaMissao(), npc.isTemAnimacao()));
        }

        // Atualiza o personagem
        personagem.setPosition(gameData.getPersonagem().getX(), gameData.getPersonagem().getY());
        personagem.moedas = gameData.getPersonagem().getMoedas();
        personagem.xp = gameData.getPersonagem().getXp();
        personagem.tipoInfracao = null;
        personagem.infracoes = gameData.getPersonagem().getInfracoes();
        personagem.npcs = npcs;
        personagem.setValoresDafault();

        // Atualiza o controller
        if (controller != null) {
            controller.MISSAO = gameData.getMissaoAtual();
            controller.inicializar();
        }

        // Atualiza o MissionController
        controleMissao.setValoresDafault();
    }

    private void setValoresDafault() {
        // Carrega o objeto interativo das missões
        objetosInterativos.put("objetoMissao", new ObjetoInterativo("alertaMissao", 1290, 1245, "mission-alert.png",
                estagioPrincipal, false));

        int horizontalObjX = 1705;
        int horizontalObjY = 1388;
        objetosInterativos.put("objetoMissaoHorizontal",
                new ObjetoInterativo("alertaHorizontal", horizontalObjX, horizontalObjY, "mission-alert-horizontal.png",
                        estagioPrincipal, false));

        // Carrega o objeto do chão
        objetosInterativos.put("objetoChao",
                new ObjetoInterativo("chao", 320, 1210, "asphalt-obj.png", estagioPrincipal));

        // Carrega o objeto interativo da placa das missões
        objetosInterativos.put("objetoPlaca1",
                new ObjetoInterativo("placa", 1290, 1245, "mission1-result.png", estagioPrincipal, false));

        objetosInterativos.put("objetoHorizontal2", new ObjetoInterativo("horizontal2", horizontalObjX, horizontalObjY,
                "mission2-result.png", estagioPrincipal, false));

        objetosInterativos.put("objetoPlaca3",
                new ObjetoInterativo("placa3", 380, 1450, "mission3-result.png", estagioPrincipal, false));

        objetosInterativos.put("objetoPlaca5",
                new ObjetoInterativo("placa5", 1956, 512, "mission5-result.png", estagioPrincipal, false));

        objetosInterativos.put("objetoPlaca6",
                new ObjetoInterativo("placa6", 1480, 256, "mission6-result.png", estagioPrincipal, false));

        objetosInterativos.put("objetoPlaca7",
                new ObjetoInterativo("placa7", 488, 200, "mission7-result.png", estagioPrincipal, false));

        // Carrega o objeto interativo do PC no mapa room
        objetosInterativos.put("objetoPc",
                new ObjetoInterativo("pc", 1020, 1470, "background-transparent.png", estagioPrincipal));

        // Carrega o personagem
        // personagem = new Personagem(250, 860, estagioPrincipal);
        Personagem.setLimitacaoMundo(Mapas.MAPA_LARGURA, Mapas.MAPA_ALTURA);

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
        objetosInterativos.put("objeto",
                new ObjetoInterativo("entradaPrefeitura", 100, 760, "background-transparent.png", estagioPrincipal));
    }

    public void atualizarJogoPorSaveGameData(String tipo) {
        GameData gameData = GameDataDAO.carregarDadosJogo(tipo);

        // Atualiza os NPCs
        for (GameDataNpc npc : gameData.getNpcs()) {
            npcs.put(npc.getKey(), new Npc(npc.getKey(), npc.getX(), npc.getY(), npc.getKey() + "/sprite.png",
                    estagioPrincipal, npc.getStatusAlertaMissao(), npc.isTemAnimacao()));
        }

        // Atualiza o personagem
        personagem = new Personagem(gameData.getPersonagem().getX(), gameData.getPersonagem().getY(), estagioPrincipal);
        personagem.setPosition(gameData.getPersonagem().getX(), gameData.getPersonagem().getY());
        personagem.moedas = gameData.getPersonagem().getMoedas();
        personagem.xp = gameData.getPersonagem().getXp();
        personagem.tipoInfracao = gameData.getPersonagem().getTipoInfracao() != null
                ? Personagem.TipoInfracao.valueOf(gameData.getPersonagem().getTipoInfracao())
                : null;
        personagem.infracoes = gameData.getPersonagem().getInfracoes();
        personagem.atualizarSpritePersonagem(gameData.getPersonagem().getSprite());
        personagem.npcs = npcs;

        if (controller != null)
            controller.MISSAO = gameData.getMissaoAtual();
    }

    public void salvarJogo(String tipo) {
        // Salva os NPCs
        List<GameDataNpc> dataNpcs = new ArrayList<>();
        for (Npc npc : npcs.values()) {
            GameDataNpc gameDataNpc = new GameDataNpc();
            gameDataNpc.setKey(npc.nome);
            gameDataNpc.setX((int) npc.getX());
            gameDataNpc.setY((int) npc.getY());
            gameDataNpc.setStatusAlertaMissao(npc.statusAlertaMissao);
            gameDataNpc.setTemAnimacao(npc.nome.equals("maria") ? true : false);
            dataNpcs.add(gameDataNpc);
        }

        // Salva o personagem
        GameDataPersonagem dataPersonagem = new GameDataPersonagem();
        dataPersonagem.setX((int) personagem.getX());
        dataPersonagem.setY((int) personagem.getY());
        dataPersonagem.setMoedas(personagem.moedas);
        dataPersonagem.setXp(personagem.xp);
        dataPersonagem.setTipoInfracao(personagem.tipoInfracao != null ? personagem.tipoInfracao.toString() : null);
        dataPersonagem.setInfracoes(personagem.infracoes);
        dataPersonagem.setSprite(personagem.selectedCharacter);

        GameData gameData = new GameData(
                controller.MISSAO,
                dataPersonagem,
                dataNpcs);

        GameDataDAO.salvarDadosJogo(gameData, tipo);
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
    public void create() {
        // Inicia a tela de loading do game
        this.setScreen(new LoadingScreen(this));

        // Define o cursor personalizado
        Pixmap pixmap = new Pixmap(Gdx.files.internal("files/mouse/mouse.png"));
        Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();
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
        objetosInterativos.forEach((nome, objeto) -> {
            if (objeto != null)
                objeto.dispose();
        });
        objetosInterativos.clear();

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
        if (efeitoBuzina != null) {
            efeitoBuzina.dispose();
        }
        if (efeitoAcerto != null) {
            efeitoAcerto.dispose();
        }
        if (efeitoErro != null) {
            efeitoErro.dispose();
        }
        if (efeitoNotificacao != null) {
            efeitoNotificacao.dispose();
        }
        if (efeitoCancelar != null) {
            efeitoCancelar.dispose();
        }
        if (efeitoConfirmar != null) {
            efeitoConfirmar.dispose();
        }
        if (efeitoMoeda != null) {
            efeitoMoeda.dispose();
        }
        if (estagioPrincipal != null) {
            estagioPrincipal.dispose();
        }
    }
}
