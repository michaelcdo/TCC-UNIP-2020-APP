package br.unip.tcc.eiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import br.unip.tcc.eiaapp.DTO.UserDTO;
import br.unip.tcc.eiaapp.util.Util;

public class PrimeiraTelaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeira_tela);

        Util.hideSystemUI(getWindow().getDecorView());
        Util.atualizaUser(new UserDTO(),PrimeiraTelaActivity.this);
        Util.limpaHistorico(PrimeiraTelaActivity.this);

        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return;
        }


    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        Util.hideSystemUI(getWindow().getDecorView());
        try {
            TimeUnit.SECONDS.sleep(2);
            Intent intent = new Intent(this, BemVindoActivity.class);
            startActivity(intent);
        }catch (Exception e){

        }
    }
}