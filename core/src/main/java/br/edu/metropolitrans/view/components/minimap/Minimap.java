package br.edu.metropolitrans.view.components.minimap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.model.maps.Mapas;

public class Minimap {
    private MetropoliTrans jogo;
    public Texture background, background2;
    public Texture personagemTexture;
    private float x, y;
    private float minimapaLargura = 200;
    private float minimapaAltura = 200;

    public Minimap(float x, float y, MetropoliTrans jogo) {
        this.jogo = jogo;
        this.personagemTexture = jogo.personagem.minimapaPonto;
        this.x = x;
        this.y = y;
        background = new Texture(Gdx.files.internal("files/others/map.png"));
        background2 = new Texture(Gdx.files.internal("files/others/map2.png"));
    }

    public void render(Personagem personagem) {
        // Se o minimapa estiver visível, desenha o minimapa
        if (jogo.controller.minimapaIsVisible) {
            jogo.batch.begin();

            if (jogo.controleMissao.ativaCamadaMissao4) {
                jogo.batch.draw(background2, x, y, minimapaLargura, minimapaAltura);
            } else {
                jogo.batch.draw(background, x, y, minimapaLargura, minimapaAltura);
            }

            // Obter as coordenadas do personagem no mapa grande
            float personagemX = personagem.getX();
            float personagemY = personagem.getY();

            // Converter as coordenadas para o minimapa
            float minimapaX = x + (personagemX / Mapas.MAPA_LARGURA) * minimapaLargura;
            float minimapaY = y + (personagemY / Mapas.MAPA_ALTURA) * minimapaAltura;

            // Desenhar a textura do personagem no minimapa
            float tamanhoPersonagem = 24; // Tamanho da imagem do personagem

            // Desenha o personagem no minimapa
            jogo.batch.draw(personagemTexture, minimapaX - tamanhoPersonagem / 2, minimapaY - tamanhoPersonagem / 2,
                    tamanhoPersonagem, tamanhoPersonagem);

            // Posiciona os NPCS no minimapa
            personagem.npcs.forEach((nome, npc) -> {
                if (npc.minimapaPonto != null && npc.statusAlertaMissao == 1) {
                    float npcX = npc.getX();
                    float npcY = npc.getY();
                    float minimapaNpcX = x + (npcX / Mapas.MAPA_LARGURA) * minimapaLargura;
                    float minimapaNpcY = y + (npcY / Mapas.MAPA_ALTURA) * minimapaAltura;
                    jogo.batch.draw(npc.minimapaPonto, minimapaNpcX - tamanhoPersonagem / 2,
                            minimapaNpcY - tamanhoPersonagem / 2, tamanhoPersonagem, tamanhoPersonagem);
                }
            });

            jogo.batch.end();
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
