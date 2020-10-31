package br.unip.tcc.eiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import br.unip.tcc.eiaapp.util.Util;

public class EntretenimentoActivity extends AppCompatActivity {

    private ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entretenimento);

        Util.hideSystemUI(getWindow().getDecorView());

        btnMenu = findViewById(R.id.btn_menu);

        btnMenu.setOnClickListener(view -> {
            Util.showMenu(view,EntretenimentoActivity.this,R.menu.menu_entrenimento);
        });

        //Botões da barra de navegação
        Button btnVoltar = findViewById(R.id.btn_voltar);
        Button btnChat = findViewById(R.id.btn_chat);
        Button btnGraph = findViewById(R.id.btn_graph);

        btnChat.setOnClickListener(view ->{
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("telaOrigem","humor");
            startActivity(intent);
        });

        btnGraph.setOnClickListener(view ->{
            Intent intent = new Intent(this, GraficoActivity.class);
            startActivity(intent);
        });
        btnVoltar.setOnClickListener(view ->{
            Intent intent = new Intent(this, HumorActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Util.hideSystemUI(getWindow().getDecorView());
    }
}