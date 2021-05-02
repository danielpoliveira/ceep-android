package br.com.alura.ceep.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class EstadoLaunchAppPreferences {

    public static final String FIRST_LAUNCH_APP_STATE = "FIRST_LAUNCH_APP_STATE";
    private final SharedPreferences preferences;

    public EstadoLaunchAppPreferences(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean verificaSeIniciouPelaPrimeiraVez() {
        return preferences
                .getBoolean(FIRST_LAUNCH_APP_STATE, false);
    }

    public void setInicioPrimeiraVez() {
        preferences
                .edit()
                .putBoolean(FIRST_LAUNCH_APP_STATE, true)
                .apply();
    }
}

