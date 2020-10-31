package br.unip.tcc.eiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.unip.tcc.eiaapp.DTO.HumorDTO;
import br.unip.tcc.eiaapp.DTO.MensagemWatsonDTO;
import br.unip.tcc.eiaapp.DTO.UserDTO;
import br.unip.tcc.eiaapp.util.CallAPI;
import br.unip.tcc.eiaapp.util.Constants;
import br.unip.tcc.eiaapp.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GraficoActivity extends AppCompatActivity {

    private CallAPI callApi;
    private GraphView graph;
    private UserDTO user;
    private List<HumorDTO> humores;
    private ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        Util.hideSystemUI(getWindow().getDecorView());

        btnMenu = findViewById(R.id.btn_menu);

        btnMenu.setOnClickListener(view -> {
            Util.showMenu(view,GraficoActivity.this,R.menu.menu_historico);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        callApi = retrofit.create(CallAPI.class);

        graph = (GraphView) findViewById(R.id.graph);
        graph.setVisibility(View.INVISIBLE);

        user = Util.carregaUser(GraficoActivity.this);
        obtemHumores(user);

        //Botões da barra de navegação
        Button btnVoltar = findViewById(R.id.btn_voltar);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnChat = findViewById(R.id.btn_chat);

        btnChat.setOnClickListener(view ->{
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("telaOrigem","grafico");
            startActivity(intent);
        });
        btnVoltar.setOnClickListener(view ->{
            Intent intent = new Intent(this, HumorActivity.class);
            startActivity(intent);
        });
        btnSave.setOnClickListener(view ->{
            doPhotoPrint(view.getRootView().getRootView().getRootView());
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Util.hideSystemUI(getWindow().getDecorView());
    }

    public void carregaSeries(){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        for(int i = 0; i < humores.size(); i++) {
            Util.atualizaId(humores.get(i).getIdUser(),GraficoActivity.this);
            series.appendData(new DataPoint(i, humores.get(i).getCodHumor()),true,4);
        }
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        graph.addSeries(series);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    if(value % 1 == 0) {
                        Date dt = humores.get((int) value).getDtGravacao();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dt);
                        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1);
                    }
                    return "";
                    //return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        graph.getViewport().setMinY(0.5);
        graph.getViewport().setMaxY(4.5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxX(humores.size()-0.5);
        graph.getViewport().setMinX(-0.5);
        graph.getViewport().setXAxisBoundsManual(true);
    }

    private void obtemHumores(UserDTO user){
        Call<List<HumorDTO>> call = callApi.obtemHumores(user);

        call.enqueue(new Callback<List<HumorDTO>>() {

            @Override
            public void onResponse(Call<List<HumorDTO>> call, Response<List<HumorDTO>> response) {
                if(!response.isSuccessful()) {
                    return;
                }
                List<HumorDTO> humoresRet = response.body();
                humores = humoresRet;
                carregaSeries();
                graph.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<List<HumorDTO>> call, Throwable t) {
                return;
            }
        });
    }
    private void doPhotoPrint(View view) {
        PrintHelper photoPrinter = new PrintHelper(GraficoActivity.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = screenShot(view);
        photoPrinter.printBitmap("historico", bitmap);
    }
    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}