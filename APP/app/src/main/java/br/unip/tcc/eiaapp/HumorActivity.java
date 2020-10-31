package br.unip.tcc.eiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import br.unip.tcc.eiaapp.DTO.HumorDTO;
import br.unip.tcc.eiaapp.DTO.UserDTO;
import br.unip.tcc.eiaapp.util.CallAPI;
import br.unip.tcc.eiaapp.util.Constants;
import br.unip.tcc.eiaapp.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HumorActivity extends AppCompatActivity {
    private TextView txt_Humor;
    private ImageView img_humor;
    private ImageButton btn_avancar_humor;
    private TextView txt_nome_humor;
    private Toast toast;
    private int humorSelecionado = 0;
    private long lastBackPressTime = 0;
    private long lastBackPressTimeHumor = 0;
    private UserDTO user;
    private CallAPI callApi;
    private ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humor);

        Util.hideSystemUI(getWindow().getDecorView());
        user = Util.carregaUser(HumorActivity.this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        callApi = retrofit.create(CallAPI.class);

        String func = getIntent().getStringExtra("func");
        if(func != null && func.equals("cadUser")){
            this.cadastraUsuario(user);
        }

        Button btnFeliz = findViewById(R.id.btn_feliz);
        Button btnConfuso = findViewById(R.id.btn_confuso);
        Button btnRaiva = findViewById(R.id.btn_raiva);
        Button btnTriste = findViewById(R.id.btn_triste);
        txt_Humor = findViewById(R.id.txt_humor);
        img_humor = findViewById(R.id.img_humor);
        btn_avancar_humor = findViewById(R.id.btn_avancar_humor);
        txt_nome_humor = findViewById(R.id.txt_nome_humor);
        btnMenu = findViewById(R.id.btn_menu);

        String nomeIdadeUser = "";
        if(user.getNome()!= null && !user.getNome().equals("")) {
            nomeIdadeUser = "Sr(a). " + user.getNome();
        }
        if(user.getIdade()>0) {
            nomeIdadeUser += ", " + user.getIdade() + " anos";
        }
        txt_nome_humor.setText(nomeIdadeUser);

        btnFeliz.setOnClickListener(view -> {
            txt_Humor.setText(Constants.TXT_HUMOR_FELIZ);
            img_humor.setImageResource(R.drawable.img_humor_4);
            this.humorSelecionado = 4;
        });

        btnConfuso.setOnClickListener(view -> {
            txt_Humor.setText(Constants.TXT_HUMOR_CONFUSO);
            img_humor.setImageResource(R.drawable.img_humor_1);
            this.humorSelecionado = 3;
        });

        btnRaiva.setOnClickListener(view -> {
            txt_Humor.setText(Constants.TXT_HUMOR_RAIVA);
            img_humor.setImageResource(R.drawable.img_humor_3);
            this.humorSelecionado = 2;
        });

        btnTriste.setOnClickListener(view -> {
            txt_Humor.setText(Constants.TXT_HUMOR_TRISTE);
            img_humor.setImageResource(R.drawable.img_humor_2);
            this.humorSelecionado = 1;
        });

        btn_avancar_humor.setOnClickListener(view -> {
            if(this.humorSelecionado == 0 && this.lastBackPressTimeHumor < System.currentTimeMillis() - 4000){
                toast = Toast.makeText(HumorActivity.this, "Nenhum humor será inserido, clique novamente para confirmar.", Toast.LENGTH_SHORT);
                toast.show();
                this.lastBackPressTimeHumor = System.currentTimeMillis();
            }else{
                if (toast != null) {
                    toast.cancel();
                }
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("humor_selecionado", humorSelecionado);
                intent.putExtra("telaOrigem","humor");
                startActivity(intent);
            }
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
        });
        btnMenu.setOnClickListener(view -> {
            Util.showMenu(view,HumorActivity.this,R.menu.menu_humor);
        });
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Util.hideSystemUI(getWindow().getDecorView());
    }

    private void cadastraUsuario(UserDTO user){
        Call<UserDTO> call = callApi.gravaUsuario(user);

        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if(!response.isSuccessful()) {
                    toast = Toast.makeText(HumorActivity.this, "Erro ao gravar os dados do usuário.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                UserDTO user = response.body();
                Util.atualizaUser(user, HumorActivity.this);
                return;
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                toast = Toast.makeText(HumorActivity.this, "Erro ao gravar os dados do usuário.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        });

    }
}