package br.edu.metropolitrans.view.components.mission_alert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MissionAlert {

    public Texture icone;
    // status = 0 = desativado,1 = pendente e 2 = interagido
    public int status;
    public float x, y;
    public SpriteBatch batch;

    public MissionAlert(SpriteBatch batch) {
        this.batch = batch;
        this.status = 0;
    }

    public void render() {
        if (status == 1) {
            icone = new Texture(Gdx.files.internal("files/icons/iconDialogoAtivoNpc.png"));
        } else if (status == 2) {
            icone = new Texture(Gdx.files.internal("files/icons/iconCheckNpc.png"));
        } else {
            icone = null;
        }

        batch.begin();

        if (icone != null) {
            batch.draw(icone, x + 5, y + 48, 24, 24);
        }

        batch.end();
    }

}
