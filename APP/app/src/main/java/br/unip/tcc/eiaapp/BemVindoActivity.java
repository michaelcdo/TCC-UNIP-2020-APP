package br.unip.tcc.eiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.unip.tcc.eiaapp.util.Util;

public class BemVindoActivity extends AppCompatActivity {

    private EditText inputNome;
    private TextView labelPular;
    private Toast toast;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bem_vindo);

        if(Util.carregaUser(BemVindoActivity.this).getId()!=0){
            Intent intent = new Intent(BemVindoActivity.this, HumorActivity.class);
            startActivity(intent);
        }

        inputNome = findViewById(R.id.input_name);
        labelPular = findViewById(R.id.label_pular);

        labelPular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BemVindoActivity.this,TermoUsoActivity.class);
                startActivity(intent);

            }
        });

        inputNome.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String nome = inputNome.getText().toString();

                    if(nome.length() > 0) {
                        Intent intent = new Intent(BemVindoActivity.this, CadastroActivity.class);
                        intent.putExtra("userNome", nome);
                        startActivity(intent);
                    }else{
                        toast = Toast.makeText(BemVindoActivity.this, "Informe seu nome.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Pressione o Botão Voltar novamente para fechar o Aplicativo.", Toast.LENGTH_SHORT);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            Intent intent = new Intent(this, PrimeiraTelaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Exit me", true);
            startActivity(intent);
            finish();
        }
        return;
    }
}