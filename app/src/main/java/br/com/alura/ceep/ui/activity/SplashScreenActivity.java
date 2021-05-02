package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import br.com.alura.ceep.R;
import br.com.alura.ceep.preferences.EstadoLaunchAppPreferences;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int duracaoInicioMilis = verificaSeJaIniciouPelaPrimeiraVez();
        delayScreen(duracaoInicioMilis);
    }

    private int verificaSeJaIniciouPelaPrimeiraVez() {
        EstadoLaunchAppPreferences preferences = new EstadoLaunchAppPreferences(this);
        boolean iniciouPelaPrimeiraVez = preferences.verificaSeIniciouPelaPrimeiraVez();

        int duracaoInicioMilis = 500;
        if(!iniciouPelaPrimeiraVez) {
            preferences.setInicioPrimeiraVez();
            duracaoInicioMilis = 2000;
        }

        return duracaoInicioMilis;
    }

    private void delayScreen(int duracaoMilis) {
        new Handler().postDelayed(this::vaiParaListaNotas, duracaoMilis);
    }

    private void vaiParaListaNotas() {
        Intent intent = new Intent(SplashScreenActivity.this, ListaNotasActivity.class);
        startActivity(intent);
        finish();
    }
}
