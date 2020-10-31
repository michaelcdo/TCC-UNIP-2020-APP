package br.unip.tcc.eiaapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;

import androidx.annotation.MenuRes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.unip.tcc.eiaapp.ChatActivity;
import br.unip.tcc.eiaapp.DTO.MessageDTO;
import br.unip.tcc.eiaapp.DTO.UserDTO;
import br.unip.tcc.eiaapp.EmergenciaActivity;
import br.unip.tcc.eiaapp.GraficoActivity;
import br.unip.tcc.eiaapp.EntretenimentoActivity;
import br.unip.tcc.eiaapp.HumorActivity;
import br.unip.tcc.eiaapp.R;
import br.unip.tcc.eiaapp.SuporteActivity;
import br.unip.tcc.eiaapp.TermoUsoActivity;

public class Util {
    public static void hideSystemUI(View decorView) {
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static UserDTO carregaUser(Context ctx){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        UserDTO user = new UserDTO();
        user.setId(myPreferences.getLong("idUser",0));
        user.setNome(myPreferences.getString("nomeUser",null));
        user.setEmail(myPreferences.getString("emailUser",null));
        user.setIdade(myPreferences.getInt("idadeUser",0));
        user.setTelefone(myPreferences.getString("telefoneUser",null));
        user.setSexo(myPreferences.getString("sexoUser",null));

        return user;
    }
    public static void atualizaId(long id, Context ctx){
        UserDTO user = carregaUser(ctx);
        if(user.getId() <= 0 && id>0){
            user.setId(id);
            user = Util.atualizaUser(user, ctx);
        }
    }
    public static UserDTO atualizaUser(UserDTO user, Context ctx){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putLong("idUser", user.getId());
        Log.i("teste", user.getId()+"");
        myEditor.putString("nomeUser", user.getNome());
        myEditor.putString("emailUser", user.getEmail());
        myEditor.putInt("idadeUser", user.getIdade());
        myEditor.putString("telefoneUser", user.getTelefone());
        myEditor.putString("sexoUser", user.getSexo());
        myEditor.commit();

        return user;
    }

    public static String getTimeStamp(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        String hr = "";
        String mn = "";

        hr = calendar.get(Calendar.HOUR_OF_DAY) + "";
        mn = calendar.get(Calendar.MINUTE) + "";
        if(hr.length()==1)
            hr = "0" + hr;

        if(mn.length()==1)
            mn = "0" + mn;

        return hr + ":" + mn;

    }
    public static String getData(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        String dia = "";
        String mes = "";
        String ano = "";

        dia = calendar.get(Calendar.DAY_OF_MONTH) + "";
        mes = calendar.get(Calendar.MONTH) + "";
        ano = calendar.get(Calendar.YEAR) + "";
        if(dia.length()==1)
            dia = "0" + dia;

        if(mes.length()==1)
            mes = "0" + mes;

        return dia + "/" + mes+ "/" + ano;

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void salvaHistorico(MessageDTO message, Context ctx){
        String messageString = message.getText()+";"+message.getTimestamp()+";"+message.isWatson()+";"+message.getData();

        String historico = getHistoricoString(ctx);
        historico+=messageString + "¬";

        setHistoricoString(ctx,historico);

    }
    public static void limpaHistorico(Context ctx){
        setHistoricoString(ctx,"");
    }
    public static ArrayList<MessageDTO> carregaMensagens(Context ctx){
        String historico = getHistoricoString(ctx);
        ArrayList<MessageDTO> messages = new ArrayList<>();
        for (String msg: historico.split("¬")){
            MessageDTO message = fromString(msg);
            if(message!=null){
                messages.add(message);
            }
        }
        return messages;
    }
    private static MessageDTO fromString(String msg){
        MessageDTO message = new MessageDTO();

        if(msg.length()>0){
            try {
                message.setText(msg.split(";")[0]);
                message.setTimestamp(msg.split(";")[1]);
                message.setWatson(Boolean.parseBoolean(msg.split(";")[2]));
                message.setData(msg.split(";")[3]);
                return message;
            }catch (Exception e){
                return null;
            }
        }

        return null;
    }
    private static String getHistoricoString(Context ctx){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return myPreferences.getString("historicoChat","");
    }

    private static void setHistoricoString(Context ctx, String historico){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString("historicoChat", historico);
        myEditor.commit();
    }

    public static void showMenu(View v, Context ctx, @MenuRes int menu) {
        PopupMenu popup = new PopupMenu(ctx, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = null;
                switch (menuItem.getItemId()) {
                    case R.id.emergencia:
                        intent = new Intent(ctx, EmergenciaActivity.class);
                        ctx.startActivity(intent);
                        return true;
                    case R.id.chat_menu:
                        intent = new Intent(ctx, ChatActivity.class);
                        ctx.startActivity(intent);
                        return true;
                    case R.id.suporte:
                        intent = new Intent(ctx, SuporteActivity.class);
                        ctx.startActivity(intent);
                        return true;
                    case R.id.entretenimento:
                        intent = new Intent(ctx, EntretenimentoActivity.class);
                        ctx.startActivity(intent);
                        return true;
                    case R.id.historico:
                        intent = new Intent(ctx, GraficoActivity.class);
                        ctx.startActivity(intent);
                        return true;
                    case R.id.termo_uso_menu:
                        intent = new Intent(ctx, TermoUsoActivity.class);
                        intent.putExtra("visual", true);
                        ctx.startActivity(intent);
                        return true;
                    case R.id.humor:
                        intent = new Intent(ctx, HumorActivity.class);
                        ctx.startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(menu);
        popup.show();
    }

    public static void discar(String telefone, Context ctx) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + telefone));
        ctx.startActivity(intent);
    }

    public static void enviarEmail(String email, Context ctx) {
        String uriText =
                "mailto:"+ email +
                        "?subject=" + Uri.encode("EIA-API") +
                        "&body=" + Uri.encode("");

        Uri uri = Uri.parse(uriText);

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);
        ctx.startActivity(Intent.createChooser(sendIntent, "Send email"));
    }
}

