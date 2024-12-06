package br.edu.metropolitrans.view.components.dialog;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class DialogBase extends Dialog {

    private VerticalGroup dialogContent;
    private Image npcImage;
    private Label bodyTextLabel;
    private Label.LabelStyle labelStyle;
    private TextButton.TextButtonStyle buttonStyle;


    public DialogBase(String title, Label.LabelStyle labelStyle, TextButton.TextButtonStyle buttonStyle) {
        super(title, new WindowStyle(labelStyle.font, labelStyle.fontColor, null));
        this.labelStyle = labelStyle;
        this.buttonStyle = buttonStyle;
        dialogContent = new VerticalGroup();
        getContentTable().add(dialogContent).expand().fill();
    }

    public void setDialogText(String text) {
        Label dialogLabel = new Label(text, labelStyle);
        dialogContent.clear();
        dialogContent.addActor(dialogLabel);
    }

    public void setBodyText(String text) {
        if (bodyTextLabel == null) {
            bodyTextLabel = new Label(text, labelStyle);
            dialogContent.addActor(bodyTextLabel);
        } else {
            bodyTextLabel.setText(text);
        }
    }

    public void setNpcImage(String imagePath) {
        Texture npcTexture = new Texture(imagePath);
        if (npcImage == null) {
            npcImage = new Image(new TextureRegionDrawable(npcTexture));
            getContentTable().add(npcImage).left().padRight(10);
        } else {
            npcImage.setDrawable(new TextureRegionDrawable(npcTexture));
        }
    }

    public void addButton(String buttonText, ClickListener listener) {
        TextButton button = new TextButton(buttonText, buttonStyle);
        button.addListener(listener);
        getButtonTable().add(button).pad(10);
    }

    public void clearButtons() {
        getButtonTable().clear();
    }
}
