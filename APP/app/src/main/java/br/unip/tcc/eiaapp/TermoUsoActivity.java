package br.unip.tcc.eiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import br.unip.tcc.eiaapp.util.Util;

public class TermoUsoActivity extends AppCompatActivity {

    ImageButton btnRecusa;
    ImageButton btnAceita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termo_uso);

        Util.hideSystemUI(getWindow().getDecorView());

        Boolean visual = getIntent().getBooleanExtra("visual", false);

        btnRecusa = findViewById(R.id.btn_recusa);
        btnAceita = findViewById(R.id.btn_aceita);

        btnRecusa.setOnClickListener(view -> {
            Intent intent = new Intent(this, BemVindoActivity.class);
            startActivity(intent);
        });

        if(visual){
            btnRecusa.setVisibility(View.INVISIBLE);
        }
        btnAceita.setOnClickListener(view -> {
            Intent intent = new Intent(this, HumorActivity.class);
            if(!visual) {
                intent.putExtra("func", "cadUser");
            }
            startActivity(intent);
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Util.hideSystemUI(getWindow().getDecorView());
    }
}