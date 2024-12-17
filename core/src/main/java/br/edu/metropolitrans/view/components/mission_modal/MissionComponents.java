package br.edu.metropolitrans.view.components.mission_modal;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import br.edu.metropolitrans.view.font.FontBase;

public class MissionComponents {

    public int missaoId;
    public Stage stage;
    public Label titulo;
    public List<ImageButton> botoesPlacas;

    public MissionComponents(int missaoId) {
        this.missaoId = missaoId;
        this.stage = new Stage();
        botoesPlacas = new ArrayList<>();
    }

    public void adicionarTituloMissao(String textoTitulo, float x, float y) {
        // Carrega a fonte do título
        BitmapFont fonteTitulo = FontBase.getInstancia().getFonte(30, FontBase.Fontes.PADRAO);

        // Cria o título
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fonteTitulo;
        labelStyle.fontColor = Color.BLACK;
        titulo = new Label(textoTitulo, labelStyle);
        titulo.setPosition(x, stage.getHeight() - titulo.getHeight() - y - 20);

        stage.addActor(titulo);
    }

    public void adicionarOpcaoPlaca(String caminhoImagem, boolean correta, float x, float y) {
        // Skin skin = new Skin();
        Texture texture = new Texture(Gdx.files.internal("files/missionComponents/" + caminhoImagem));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(texture);
        ImageButton botaoPlaca = new ImageButton(style);

        botaoPlaca.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (correta) {
                    // Lógica para resposta correta
                    Gdx.app.log("MissionComponents","Resposta correta!");
                } else {
                    // Lógica para resposta incorreta
                    Gdx.app.log("MissionComponents","Resposta incorreta!");
                }
                return true;
            }
        });
        
        botaoPlaca.setPosition(x, y);
        botoesPlacas.add(botaoPlaca);
        stage.addActor(botaoPlaca);
    }

}
