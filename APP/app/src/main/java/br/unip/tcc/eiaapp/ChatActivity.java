package br.unip.tcc.eiaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.GroupieViewHolder;

import java.util.ArrayList;

import br.unip.tcc.eiaapp.DTO.HumorDTO;
import br.unip.tcc.eiaapp.DTO.MensagemWatsonDTO;
import br.unip.tcc.eiaapp.DTO.MessageDTO;
import br.unip.tcc.eiaapp.DTO.UserDTO;
import br.unip.tcc.eiaapp.util.CallAPI;
import br.unip.tcc.eiaapp.util.Constants;
import br.unip.tcc.eiaapp.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private Toast toast;
    private long lastBackPressTime = 0;
    private EditText inputMensagem;
    private GroupAdapter adapter = new GroupAdapter();
    private CallAPI callApi;
    private UserDTO user;
    private String telaOrigem = "";
    private ImageButton btnMenu;
    private TextView txt_nome_chat;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        telaOrigem = getIntent().getStringExtra("telaOrigem");

        btnMenu = findViewById(R.id.btn_menu);

        btnMenu.setOnClickListener(view -> {
            Util.showMenu(view,ChatActivity.this,R.menu.menu_chat);
        });

        this.user = Util.carregaUser(ChatActivity.this);

        txt_nome_chat = findViewById(R.id.txt_nome_chat);

        String nomeIdadeUser = "";
        if(user.getNome()!= null && !user.getNome().equals("")) {
            nomeIdadeUser = "Sr(a). " + user.getNome();
        }
        if(user.getIdade()>0) {
            nomeIdadeUser += ", " + user.getIdade() + " anos";
        }
        txt_nome_chat.setText(nomeIdadeUser);

        rv = findViewById(R.id.recycler_chat);

        inputMensagem = findViewById(R.id.input_mensagem);
        Button btnChat = findViewById(R.id.btnEnviarChat);

        inputMensagem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    Util.hideKeyboard(ChatActivity.this);
                    sendMessage();

                    return true;
                }
                return false;
            }
        });

        btnChat.setOnClickListener(view -> sendMessage());

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        carregaChat();
        moveToBottom();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        callApi = retrofit.create(CallAPI.class);

        int codHumor = getIntent().getIntExtra("humor_selecionado",0);
        if(codHumor>0){
            HumorDTO humor = new HumorDTO();
            humor.setCodHumor(codHumor);
            humor.setIdUser(this.user.getId());
            humor.setCodRetorno(0);
            gravaHumor(humor);
        }

    }

    public void carregaChat(){
        ArrayList<MessageDTO> messages = Util.carregaMensagens(ChatActivity.this);
        for (MessageDTO msg: messages) {
            adapter.add(new ChatActivity.MessageItem(msg));
        }
    }
    private void moveToBottom(){
        if(adapter.getGroupCount()>0){
            rv.scrollToPosition(adapter.getGroupCount() - 1);
        }
    }
    private void sendMessage(){
        String text = inputMensagem.getText().toString();
        if(text.trim().length()>0) {
            MessageDTO messageEntrada = new MessageDTO();
            messageEntrada.setWatson(false);
            messageEntrada.setText(text);
            messageEntrada.setTimestamp(Util.getTimeStamp());
            messageEntrada.setData(Util.getData());

            adapter.add(new MessageItem(messageEntrada));
            Util.salvaHistorico(messageEntrada, ChatActivity.this);

            inputMensagem.setText(null);

            MensagemWatsonDTO msg = new MensagemWatsonDTO(this.user.getId(), text);

            enviaMensagem(msg);
            moveToBottom();
        }
    }

    private class MessageItem extends Item<GroupieViewHolder>{
        private final MessageDTO message;

        private MessageItem(MessageDTO message){
            this.message = message;
        }

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
            TextView txtMsg = viewHolder.itemView.findViewById(R.id.txt_message);
            TextView txtTime = viewHolder.itemView.findViewById(R.id.txt_time);

            txtMsg.setText(message.getText());
            txtTime.setText(message.getTimestamp());
        }

        @Override
        public int getLayout() {
            return message.isWatson() ? R.layout.item_from_layout : R.layout.item_to_layout;
        }
    }

    @Override
    public void onBackPressed() {
        voltarTela();
        return;
    }
    private void voltarTela(){
        if(telaOrigem!=null && telaOrigem.equals("grafico")) {
            Intent intent = new Intent(this, GraficoActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, HumorActivity.class);
            startActivity(intent);
        }
    }
    private void adicionaMensagemWatson(MensagemWatsonDTO mensagemWatsonDTO){
        if(mensagemWatsonDTO.getMensagemRetorno() != null && mensagemWatsonDTO.getMensagemRetorno().size()>0) {
            for (String msg: mensagemWatsonDTO.getMensagemRetorno()) {
                MessageDTO messageRetorno = new MessageDTO();
                messageRetorno.setWatson(true);
                messageRetorno.setText(msg);
                messageRetorno.setTimestamp(Util.getTimeStamp());
                messageRetorno.setData(Util.getData());
                adapter.add(new MessageItem(messageRetorno));
                moveToBottom();
                Util.salvaHistorico(messageRetorno, ChatActivity.this);
            }
        }
    }

    private void enviaMensagem(MensagemWatsonDTO mensagem){
        Call<MensagemWatsonDTO> call = callApi.enviaMensagem(mensagem);

        call.enqueue(new Callback<MensagemWatsonDTO>() {
            @Override
            public void onResponse(Call<MensagemWatsonDTO> call, Response<MensagemWatsonDTO> response) {
                if(!response.isSuccessful()){
                    adicionaMensagemErro();
                    return;
                }
                MensagemWatsonDTO mensagemResposta = response.body();
                Util.atualizaId(mensagemResposta.getIdUser(),ChatActivity.this);
                adicionaMensagemWatson(mensagemResposta);
            }

            @Override
            public void onFailure(Call<MensagemWatsonDTO> call, Throwable t) {
                adicionaMensagemErro();
                return;
            }
        });
    }
    private void adicionaMensagemErro(){
        MensagemWatsonDTO mensagemResposta = new MensagemWatsonDTO(0l,"");
        ArrayList<String> msgRet = new ArrayList<>();
        msgRet.add("Desculpe!");
        mensagemResposta.setMensagemRetorno(msgRet);
        adicionaMensagemWatson(mensagemResposta);
    }
    private void gravaHumor(HumorDTO humorDTO){
        Call<HumorDTO> call = callApi.gravaHumor(humorDTO);

        call.enqueue(new Callback<HumorDTO>() {
            @Override
            public void onResponse(Call<HumorDTO> call, Response<HumorDTO> response) {
                if(!response.isSuccessful()) {
                    toast = Toast.makeText(ChatActivity.this, "Erro ao gravar os dados de humor.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                HumorDTO humor = response.body();
                Util.atualizaId(humor.getIdUser(),ChatActivity.this);
                if(humor.getCodRetorno()!=0){
                    toast = Toast.makeText(ChatActivity.this, "Erro ao gravar os dados de humor.", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    toast = Toast.makeText(ChatActivity.this, "Humor gravado com sucesso.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                return;
            }

            @Override
            public void onFailure(Call<HumorDTO> call, Throwable t) {
                toast = Toast.makeText(ChatActivity.this, "Erro ao gravar os dados de humor.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        });
    }
}