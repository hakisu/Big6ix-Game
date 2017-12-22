package big6ix.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class BigPreferences {

    private static final String PREFS_NAME = "BigSettings";
    private static final String PREF_FULLSCREEN_ENABLED = "fullscreen_enabled";
    private static final String PREF_MUSIC_VOLUME = "music_volume";
    private static final String PREF_SOUND_EFFECTS_VOLUME = "sound_effects_volume";


    protected Preferences getPrefs(){
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public boolean isFullscreenEnabled(){
        return getPrefs().getBoolean(PREF_FULLSCREEN_ENABLED, true);
    }

    public void setFullscreenEnabled(boolean fullscreenEnabled){
        getPrefs().putBoolean(PREF_FULLSCREEN_ENABLED, fullscreenEnabled);
        getPrefs().flush();
    }

    public float getMusicVolume(){
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    public void setMusicVolume(float volume){
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        getPrefs().flush();
    }

    public float getSoundEffectsVolume(){
        return getPrefs().getFloat(PREF_SOUND_EFFECTS_VOLUME, 0.5f);
    }

    public void setSoundEffectsVolume(float volume){
        getPrefs().putFloat(PREF_SOUND_EFFECTS_VOLUME, volume);
        getPrefs().flush();
    }
}
