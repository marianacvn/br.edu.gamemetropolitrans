package br.edu.metropolitrans.view.components.buttons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ImageButtonBase  extends ImageButton{

    public ImageButtonBase(String upTexturePath, String checkedTexturePath) {
        super(createImageButtonStyle(upTexturePath, checkedTexturePath));
    }

    private static ImageButtonStyle createImageButtonStyle(String upTexturePath, String checkedTexturePath) {
        Texture upTexture = new Texture(upTexturePath);
        Texture checkedTexture = new Texture(checkedTexturePath);
        Drawable upDrawable = new TextureRegionDrawable(upTexture);
        Drawable checkedDrawable = new TextureRegionDrawable(checkedTexture);

        ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
        imageButtonStyle.imageUp = upDrawable;
        imageButtonStyle.imageChecked = checkedDrawable;

        return imageButtonStyle;
    }
}
