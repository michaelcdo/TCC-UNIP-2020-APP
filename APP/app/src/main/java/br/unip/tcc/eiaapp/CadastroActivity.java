package br.unip.tcc.eiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.Calendar;
import java.util.Date;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import br.unip.tcc.eiaapp.DTO.UserDTO;
import br.unip.tcc.eiaapp.util.Util;

public class CadastroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText input_email;
    private EditText input_telefone;
    private EditText input_dt_nasc;
    private Spinner input_sexo;
    private ImageButton btn_cadastro;
    private String sexo;
    private String nome;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = getIntent().getStringExtra("userNome");

        input_email = findViewById(R.id.input_email);
        input_telefone = findViewById(R.id.input_telefone);
        input_dt_nasc = findViewById(R.id.input_dt_nasc);
        input_sexo = findViewById(R.id.input_sexo);
        btn_cadastro = findViewById(R.id.btn_cadastro);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genero, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        input_sexo.setAdapter(adapter);
        input_sexo.setOnItemSelectedListener(this);

        input_telefone.clearFocus();

        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(input_telefone,smf);
        input_telefone.addTextChangedListener(mtw);

        SimpleMaskFormatter smf2 = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw2 = new MaskTextWatcher(input_dt_nasc,smf2);
        input_dt_nasc.addTextChangedListener(mtw2);

        btn_cadastro.setOnClickListener(view -> {
            Boolean erro = false;
            String mensagem = "";

            String dtNasc = input_dt_nasc.getText().toString();
            int idade = 0;
            if(dtNasc.length()>0){
                if(dtNasc.split("/").length>0){
                    if(Integer.parseInt(dtNasc.split("/")[2]) < 1910){
                        erro = true;
                        mensagem = "Insira um data de nascimento valida";
                    }
                }else{
                    erro = true;
                    mensagem = "Insira um data de nascimento valida";
                }
                if(!erro){
                    try {
                        int ano = Integer.parseInt(dtNasc.split("/")[2]);
                        int mes = Integer.parseInt(dtNasc.split("/")[1]);
                        int dia = Integer.parseInt(dtNasc.split("/")[0]);

                        idade = getIdade(ano,mes,dia);
                    }catch (Exception e){

                    }
                }
            }

            String telefone = input_telefone.getText().toString();
            if(telefone.length()> 0 && telefone.length()<11){
                erro = true;
                mensagem = "Insira um telefone valido";
            }

            String email = input_email.getText().toString();
            if(email.length()>0) {
                erro = !isValidEmailAddress(email);
                mensagem = "Insira um e-mail valido";
            }

            if(!erro) {
                UserDTO user = new UserDTO();
                user.setEmail(email);
                user.setSexo(sexo);
                user.setTelefone(telefone);
                user.setNome(nome);
                user.setIdade(idade);
                user.setId(Util.carregaUser(CadastroActivity.this).getId());

                Util.atualizaUser(user, CadastroActivity.this);

                Intent intent = new Intent(this, TermoUsoActivity.class);
                startActivity(intent);
            }else{
                toast = Toast.makeText(CadastroActivity.this, mensagem, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }
    public static int getIdade(int ano, int mes, int dia) {
        Calendar cData = Calendar.getInstance();
        Calendar cHoje= Calendar.getInstance();
        cData.set(ano, mes, dia);
        cData.set(Calendar.YEAR, cHoje.get(Calendar.YEAR));
        int idade = cData.after(cHoje) ? -1 : 0;
        cData.set(ano, mes, dia);
        idade += cHoje.get(Calendar.YEAR) - cData.get(Calendar.YEAR);
        return idade;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int color = 0;
        if (i == 0){
            color = getResources().getColor(R.color.colorPrimary);
            sexo = "";
        }else{
            color = getResources().getColor(android.R.color.black);
            ((TextView) adapterView.getChildAt(0)).setTextColor(color);
            sexo = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}