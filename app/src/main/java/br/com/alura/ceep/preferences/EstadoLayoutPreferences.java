package br.com.alura.ceep.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.com.alura.ceep.model.Layout;

public class EstadoLayoutPreferences {
    private static final String KEY_LAYOUT = "LAYOUT";
    private final SharedPreferences preferences;

    public EstadoLayoutPreferences(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getEstadoLayout() {
        return preferences
                .getString(KEY_LAYOUT, Layout.LINEAR.toString());
    }

    @SuppressLint("CommitPrefEdits")
    public void saveEstadoLayout(Layout layout) {
        preferences
                .edit()
                .putString(KEY_LAYOUT, layout.toString())
                .apply();
    }

}
