package br.edu.metropolitrans.view.components.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TextButtonBase extends TextButton {
    public TextButtonBase(String text, String texturePath, Skin skin) {
        super(text, createTextButtonStyle(texturePath, skin));
    }

    private static TextButtonStyle createTextButtonStyle(String texturePath, Skin skin) {
        Texture buttonTexture = new Texture(texturePath);
        Drawable buttonDrawable = new TextureRegionDrawable(buttonTexture);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = buttonDrawable;
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.WHITE;

        return textButtonStyle;
    }
}
