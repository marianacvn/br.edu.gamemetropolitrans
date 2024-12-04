package br.edu.metropolitrans.model.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Mapas {

    public final String ARQUIVOS = "files/tileset/";
    public TiledMap mapa;
    public TiledMap sala;
    public static int MAPA_LARGURA = 2560;
    public static int MAPA_ALTURA = 2560;

    public Mapas() {
        this.mapa = new TmxMapLoader().load(ARQUIVOS + "map.tmx");
        this.sala = new TmxMapLoader().load(ARQUIVOS + "room.tmx");
    }

}
