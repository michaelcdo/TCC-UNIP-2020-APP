package br.unip.tcc.eiaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import br.unip.tcc.eiaapp.DTO.MensagemWatsonDTO;
import br.unip.tcc.eiaapp.DTO.MessageDTO;
import br.unip.tcc.eiaapp.util.CallAPI;
import br.unip.tcc.eiaapp.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private Toast toast;
    private long lastBackPressTime = 0;
    private EditText inputMensagem;
    GroupAdapter adapter = new GroupAdapter();
    private MensagemWatsonDTO mensagemWatsonDTO;
    private CallAPI callApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RecyclerView rv = findViewById(R.id.recycler_chat);
        inputMensagem = findViewById(R.id.input_mensagem);
        Button btnChat = findViewById(R.id.btnEnviarChat);

        btnChat.setOnClickListener(view -> sendMessage());

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        callApi = retrofit.create(CallAPI.class);

    }

    private void sendMessage(){
        String text = inputMensagem.getText().toString();

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(ChatActivity.this);
        Long idUser = myPreferences.getLong("idUser",0);

        MessageDTO messageEntrada = new MessageDTO();
        messageEntrada.setWatson(false);
        messageEntrada.setText(text);

        adapter.add(new MessageItem(messageEntrada));
        inputMensagem.setText(null);

        MensagemWatsonDTO msg = new MensagemWatsonDTO(idUser, text, "");

        enviaMensagem(msg);
    }

    private class MessageItem extends Item<ViewHolder>{
        private final MessageDTO message;

        private MessageItem(MessageDTO message){
            this.message = message;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView txtMsg = viewHolder.itemView.findViewById(R.id.txt_message);

            txtMsg.setText(message.getText());
        }

        @Override
        public int getLayout() {
            return message.isWatson() ? R.layout.item_to_layout : R.layout.item_from_layout;
        }
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
    }

    private void adicionaMensagemWatson(MensagemWatsonDTO mensagemWatsonDTO){
        if(!mensagemWatsonDTO.getMensagemRetorno().equals("")) {
            MessageDTO messageRetorno = new MessageDTO();
            messageRetorno.setWatson(true);
            messageRetorno.setText(mensagemWatsonDTO.getMensagemRetorno());
            SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(ChatActivity.this);
            Long idUser = myPreferences.getLong("idUser",0);

            if(idUser == 0 && mensagemWatsonDTO.getIdUser()>0){
                SharedPreferences.Editor myEditor = myPreferences.edit();
                myEditor.putLong("idUser", mensagemWatsonDTO.getIdUser());
                myEditor.commit();
            }
            adapter.add(new MessageItem(messageRetorno));
        }
    }

    private void enviaMensagem(MensagemWatsonDTO mensagem){
        mensagemWatsonDTO = mensagem;
        Call<MensagemWatsonDTO> call = callApi.enviaMensagem(mensagem);

        call.enqueue(new Callback<MensagemWatsonDTO>() {
            @Override
            public void onResponse(Call<MensagemWatsonDTO> call, Response<MensagemWatsonDTO> response) {
                if(!response.isSuccessful()){
                    MensagemWatsonDTO mensagemResposta = new MensagemWatsonDTO(0l,"","Erro");
                    adicionaMensagemWatson(mensagemResposta);
                    return;
                }
                MensagemWatsonDTO mensagemResposta = response.body();
                adicionaMensagemWatson(mensagemResposta);
            }

            @Override
            public void onFailure(Call<MensagemWatsonDTO> call, Throwable t) {
                MensagemWatsonDTO mensagemResposta = new MensagemWatsonDTO(0l,"",t.getMessage());
                adicionaMensagemWatson(mensagemResposta);

                Log.i("teste",t.getMessage());
                return;
            }
        });
    }
}