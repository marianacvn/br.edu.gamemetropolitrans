package br.edu.metropolitrans.view.components.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ConfigManager {
    private static final String PREFS_NAME = "game_config";
    private static final String VOLUME_KEY = "volume";
    private static final String MUSIC_ACTIVE_KEY = "music_active";

    private Preferences prefs;

    public ConfigManager() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
    }

    public float getVolume() {
        return prefs.getFloat(VOLUME_KEY, 1.0f); // Valor padrão é 1.0 (volume máximo)
    }

    public void setVolume(float volume) {
        prefs.putFloat(VOLUME_KEY, volume);
        prefs.flush(); // Salva as preferências
    }

    public boolean isMusicActive() {
        return prefs.getBoolean(MUSIC_ACTIVE_KEY, true); // Valor padrão é true (música ativa)
    }

    public void setMusicActive(boolean active) {
        prefs.putBoolean(MUSIC_ACTIVE_KEY, active);
        prefs.flush(); // Salva as preferências
    }
}
