package big6ix.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class BigPreferences {

    private static final String PREFS_NAME = "BigSettings";
    private static final String PREF_MUSIC_VOLUME = "music_volume";
    private static final String PREF_SOUND_EFFECTS_VOLUME = "sound_effects_volume";
    private static final String PREF_ROOMS_AMOUNT = "rooms_amount";

    protected Preferences getPrefs(){
        return Gdx.app.getPreferences(PREFS_NAME);
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

    public int getRoomsAmount() {
        return getPrefs().getInteger(PREF_ROOMS_AMOUNT, 10);
    }

    public void setRoomsAmout(int amount) {
        getPrefs().putInteger(PREF_ROOMS_AMOUNT, amount);
        getPrefs().flush();
    }
}
