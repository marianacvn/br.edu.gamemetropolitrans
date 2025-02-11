package br.edu.metropolitrans.view.components.minimap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.actors.Personagem;
import br.edu.metropolitrans.model.maps.Mapas;

public class Minimap {
    public Texture background, background2;
    public Texture personagemTexture;
    // private Texture toggleButtonTexture;
    private float x, y;
    private MetropoliTrans jogo;
    private float minimapaLargura = 200;
    private float minimapaAltura = 200;
    public boolean isVisible;
    // private Rectangle toggleButtonBounds;

    public Minimap(float x, float y, MetropoliTrans jogo) {
        this.jogo = jogo;
        this.personagemTexture = jogo.personagem.minimapaPonto;
        // this.toggleButtonTexture = new
        // Texture(Gdx.files.internal("files/itens/seta.png"));
        this.x = x;
        this.y = y;
        this.isVisible = true;
        // this.toggleButtonBounds = new Rectangle(Gdx.graphics.getWidth() - 34, 10, 24,
        // 24); // necessário
        background = new Texture(Gdx.files.internal("files/others/map.png"));
        background2 = new Texture(Gdx.files.internal("files/others/map2.png"));
    }

    public void render(Personagem personagem) {
        // Se o minimapa estiver visível, desenha o minimapa
        if (isVisible) {
            jogo.batch.begin();

            if (jogo.controller.controleMissao.ativaCamadaMissao4) {
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
