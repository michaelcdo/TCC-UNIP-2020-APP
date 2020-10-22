package br.unip.tcc.eiaapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CancellationException;

import br.unip.tcc.eiaapp.ChatActivity;
import br.unip.tcc.eiaapp.DTO.MessageDTO;
import br.unip.tcc.eiaapp.DTO.UserDTO;

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

        return user;
    }

    public static UserDTO atualizaUser(UserDTO user, Context ctx){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putLong("idUser", user.getId());
        myEditor.putString("nomeUser", user.getNome());
        myEditor.putString("emailUser", user.getEmail());
        myEditor.putInt("idadeUser", user.getIdade());
        myEditor.commit();

        return user;
    }

    public static String getData(){
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

    }



}

