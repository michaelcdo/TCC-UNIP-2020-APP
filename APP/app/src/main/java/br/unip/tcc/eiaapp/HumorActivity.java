package br.unip.tcc.eiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.unip.tcc.eiaapp.util.Constants;

public class HumorActivity extends AppCompatActivity {
    private TextView txt_Humor;
    private ImageView img_humor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humor);

        Button btnFeliz = findViewById(R.id.btn_feliz);
        Button btnConfuso = findViewById(R.id.btn_confuso);
        Button btnRaiva = findViewById(R.id.btn_raiva);
        Button btnTriste = findViewById(R.id.btn_triste);
        txt_Humor = findViewById(R.id.txt_humor);
        img_humor = findViewById(R.id.img_humor);

        btnFeliz.setOnClickListener(view -> {
            txt_Humor.setText(Constants.TXT_HUMOR_FELIZ);
            txt_Humor.setBackgroundResource(R.mipmap.backoground_textos);
            img_humor.setImageResource(R.mipmap.img_humor_4);
        });

        btnConfuso.setOnClickListener(view -> {
            txt_Humor.setText(Constants.TXT_HUMOR_CONFUSO);
            txt_Humor.setBackgroundResource(R.mipmap.backoground_textos);
            img_humor.setImageResource(R.mipmap.img_humor_1);
        });

        btnRaiva.setOnClickListener(view -> {
            txt_Humor.setText(Constants.TXT_HUMOR_RAIVA);
            txt_Humor.setBackgroundResource(R.mipmap.backoground_textos);
            img_humor.setImageResource(R.mipmap.img_humor_3);
        });

        btnTriste.setOnClickListener(view -> {
            txt_Humor.setText(Constants.TXT_HUMOR_TRISTE);
            txt_Humor.setBackgroundResource(R.mipmap.backoground_textos);
            img_humor.setImageResource(R.mipmap.img_humor_2);
        });
    }

}