package br.edu.metropolitrans.model.actors.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Mapas {

    public final String ARQUIVOS = "files/tileset/";
    public TiledMap mapa;
    public TiledMap sala;
    public static int MAPA_LARGURA = 5120;
    public static int MAPA_ALTURA = 5120;
    public static int SALA_LARGURA = 640;
    public static int SALA_ALTURA = 640;

    public Mapas() {
        this.mapa = new TmxMapLoader().load(ARQUIVOS + "map2/map2.tmx");
        this.sala = new TmxMapLoader().load(ARQUIVOS + "room.tmx");
    }

}