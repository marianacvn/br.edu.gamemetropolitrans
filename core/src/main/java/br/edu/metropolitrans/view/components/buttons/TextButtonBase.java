package br.edu.metropolitrans.view.components.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TextButtonBase extends TextButton {

    public TextButtonBase(String text, String texturePath, Skin skin) {
        super(text, createTextButtonStyle(texturePath, skin, false));
    }

    public TextButtonBase(String text, String texturePath, Skin skin, boolean smallText) {
        super(text, createTextButtonStyle(texturePath, skin, smallText));
    }

    private static TextButtonStyle createTextButtonStyle(String texturePath, Skin skin, boolean smallText) {
        // Separa o .png do caminho da textura para verificar se existe um arquivo de
        // textura pressionada
        String texturePathPressed = texturePath;
        try {
            String texturePathOutExtension = texturePath.split(".png")[0];
            texturePathPressed = texturePathOutExtension + "_pressed.png";
        } catch (Exception e) {
        }

        // Carrega a textura do botão
        Texture buttonTexture = new Texture(texturePath);
        Drawable buttonDrawable = new TextureRegionDrawable(buttonTexture);

        // Carrega a textura do botão pressionado
        Texture buttonTexturePressed = new Texture(texturePathPressed);
        Drawable buttonDrawablePressed = new TextureRegionDrawable(buttonTexturePressed);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = buttonDrawable;
        textButtonStyle.down = buttonDrawablePressed;
        if (smallText) {
            textButtonStyle.font = skin.getFont("default_small");
        } else {
            textButtonStyle.font = skin.getFont("default");
        }
        textButtonStyle.fontColor = Color.WHITE;

        return textButtonStyle;
    }
}
