package br.edu.metropolitrans.view.components.buttons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButtonBase extends ImageButton {
    public Texture texture;
    public Drawable drawable;
    public Label label;

    public ButtonBase(String texturePath, Label label) {
        super(new TextureRegionDrawable(new Texture(texturePath)));
        this.texture = new Texture(texturePath);
        this.drawable = new TextureRegionDrawable(this.texture);
        this.label = label;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        this.getStyle().imageUp = drawable;
    }

}
