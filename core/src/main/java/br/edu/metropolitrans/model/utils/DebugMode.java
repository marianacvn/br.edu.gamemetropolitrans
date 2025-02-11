package br.edu.metropolitrans.model.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import br.edu.metropolitrans.MetropoliTrans;
import br.edu.metropolitrans.model.actors.Vehicle;
import br.edu.metropolitrans.view.screens.GameScreen;

public class DebugMode {
    public static TipoDebug DEBUG_MODE = TipoDebug.NENHUM;
    public static boolean INFRACOES_ATIVAS = false; // TODO: Mudar ao finalizar o desenvolvimento

    public enum TipoDebug {
        LOG, UI, COMPLETO, NENHUM
    }

    public static void mostrarLog(String tag, String message) {
        if (DEBUG_MODE == TipoDebug.LOG || DEBUG_MODE == TipoDebug.COMPLETO) {
            Gdx.app.log(tag, message);
        }
    }

    /**
     * Desenha formas para debug
     */
    public static void debugUI(GameScreen gameScreen, MetropoliTrans jogo) {
        if (DEBUG_MODE == TipoDebug.UI || DEBUG_MODE == TipoDebug.COMPLETO) {
            DebugMode.mostrarLog("Teste", "Missão: " + jogo.controller.MISSAO);

            // Desenha quadrados da colisão para debug
            gameScreen.renderizadorForma.begin(ShapeRenderer.ShapeType.Line);
            gameScreen.renderizadorForma.setColor(1, 0, 0, 1);

            // Desenha retângulos de colisão do mapa
            if (jogo.retangulosColisao != null) {
                for (Rectangle retangulo : jogo.retangulosColisao) {
                    gameScreen.renderizadorForma.rect(retangulo.x, retangulo.y, retangulo.width, retangulo.height);
                }
            }

            // Desenha os retângulos de colisão da pista
            for (Rectangle retangulo : jogo.retangulosPista) {
                gameScreen.renderizadorForma.rect(retangulo.x, retangulo.y, retangulo.width, retangulo.height);
            }

            // Desenha polígono de colisão do personagem
            if (jogo.personagem != null) {
                Polygon personagemPoligono = jogo.personagem.getLimitePoligono();
                gameScreen.renderizadorForma.polygon(personagemPoligono.getTransformedVertices());
            }

            // Desenha polígonos de colisão dos NPCs
            if (jogo.personagem.npcs != null) {
                jogo.personagem.npcs.forEach((nome, npc) -> {
                    Polygon npcPoligono = npc.getLimitePoligono();
                    gameScreen.renderizadorForma.polygon(npcPoligono.getTransformedVertices());
                });
            }

            // Desenha polígonos de colisão dos veículos
            if (jogo.vehicles != null) {
                for (Vehicle veiculo : jogo.vehicles.values()) {
                    Polygon veiculoPoligono = veiculo.getLimitePoligono();
                    if (veiculoPoligono != null)
                        gameScreen.renderizadorForma.polygon(veiculoPoligono.getTransformedVertices());
                }
            }

            // Desenha o polígono de colisão do objeto interativo
            if (jogo.objetosInterativos.get("objeto") != null) {
                Polygon objetoPoligono = jogo.objetosInterativos.get("objeto").getLimitePoligono();
                gameScreen.renderizadorForma.polygon(objetoPoligono.getTransformedVertices());
            }

            // Desenha o polígono de colisão do objeto interativo
            if (jogo.objetosInterativos.get("objetoSairSala") != null) {
                Polygon objetoPoligono = jogo.objetosInterativos.get("objetoSairSala").getLimitePoligono();
                gameScreen.renderizadorForma.polygon(objetoPoligono.getTransformedVertices());
            }
            gameScreen.renderizadorForma.end();
        }
    }

}
