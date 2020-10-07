package br.unip.tcc.eiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PrimeiraTelaActivity extends AppCompatActivity {

    private EditText inputNome;
    private TextView labelPular;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeira_tela);

        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return;
        }

        inputNome = findViewById(R.id.input_name);
        labelPular = findViewById(R.id.label_pular);

        labelPular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(PrimeiraTelaActivity.this,ChatActivity.class);
                Intent intent = new Intent(PrimeiraTelaActivity.this,HumorActivity.class);
                startActivity(intent);

            }
        });

        inputNome.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String text = inputNome.getText().toString();

                    if(text.length() > 0) {
                        Intent intent = new Intent(PrimeiraTelaActivity.this, CadastroActivity.class);
                        startActivity(intent);
                    }else{
                        toast = Toast.makeText(PrimeiraTelaActivity.this, "Informe seu nome.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    return true;
                }
                return false;
            }
        });
    }
}