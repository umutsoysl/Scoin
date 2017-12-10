package com.umutsoysal.devakademi;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.umutsoysal.devakademi.Bildirim.BildirimIntent;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    public static Scoin scoin=null;
    private String JSON_URL = "https://devakademi.sahibinden.com/ticker";
    private String JSON_URL2 = "https://devakademi.sahibinden.com/history";
    Dialog dialog;
    final Context context = this;
    public static int boyut=0;
    Timer timer;
    public static String mesajim="sad";
    Adapter adapter;
    public static double deger=0;
    public static double b=0;
    public static float sonuc=0;
    int id;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();


    public static String[] Scoin_fiyat ;
    public static String[] Scoin_date ;

    public static String[] db_fiyat ;
    public static String[] db_date ;
    ArrayList<HashMap<String, String>> SCoins;
    public static int control_Chart=0;
    TextView fiyat,oran,date,dusenFiyatorani,mesaj,tempText;
    ImageView oranimage,alarmKur;
    ListView liste;
    BarChart mBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fiyat=(TextView)findViewById(R.id.last);
        date=(TextView)findViewById(R.id.date);
        oran=(TextView)findViewById(R.id.textView3);
        dusenFiyatorani=(TextView)findViewById(R.id.dusenFiyat);
        oranimage=(ImageView)findViewById(R.id.imageView2);
        liste=(ListView)findViewById(R.id.liste);
        alarmKur=(ImageView)findViewById(R.id.alarmKur);
        mesaj=(TextView)findViewById(R.id.mesaj);
        tempText=(TextView)findViewById(R.id.historyText);


        new ScoinHistory().execute();
        new DownloadJSON().execute();



        mBarChart = (BarChart) findViewById(R.id.barchart);


        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            DownloadJSON performBackgroundTask = new DownloadJSON();
                            Database db = new Database(getApplicationContext());

                            SCoins = db.Kurlar();
                            if(SCoins.size()>0) {

                                mBarChart.setVisibility(View.VISIBLE);
                                tempText.setVisibility(View.VISIBLE);
                                db_date = new String[SCoins.size()];
                                db_fiyat = new String[SCoins.size()];

                                boyut = SCoins.size() - 1;


                                    mesaj.setVisibility(View.INVISIBLE);


                                String color[]=new String[]{"#FF7286A1","#FFB385E4","#FFD4943A","#FF61CA2D","#EB9E9E9E","#FFFF4081"};

                                for (int i = 0; i < SCoins.size(); i++) {
                                    db_date[i] = SCoins.get(SCoins.size() - i - 1).get("tarih");
                                    db_fiyat[i] = SCoins.get(SCoins.size() - i - 1).get("fiyat");
                                    if(i<5) {
                                        control_Chart++;
                                        mBarChart.addBar(new BarModel(db_date[i].substring(0, 10), Float.parseFloat(db_fiyat[i]), Color.parseColor(color[i])));
                                    }
                                    if(control_Chart>5)
                                    {
                                        mBarChart.clearChart();
                                        control_Chart=0;
                                    }

                                }
                                b=Double.parseDouble(db_fiyat[boyut - 1]);


                            }


                            mBarChart.startAnimation();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 50000); //execute in every 50000 ms




        alarmKur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarm_dialog_show(scoin.value);
            }
        });





    }

    private class ScoinHistory extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Timber.i("onPreExecute ..");

        }

        @Override
        protected String doInBackground(String... params) {
            Timber.i("doInBackground ..");
            GetJson(JSON_URL2,1);




            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Timber.i("onPostExecute ..");

            adapter = new Adapter(MainActivity.this,Scoin_fiyat, Scoin_date);
            liste.setAdapter(adapter);

        }
    }





    private class DownloadJSON extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Timber.i("onPreExecute ..");

        }

        @Override
        protected String doInBackground(String... params) {
            Timber.i("doInBackground ..");
            GetJson(JSON_URL,0);


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Timber.i("onPostExecute ..");
            fiyat.setText(scoin.value+"  $");

            Date dates = new Date(Long.parseLong(scoin.date)*1L);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC+3"));
            String formattedDate = sdf.format(dates);
            date.setText(formattedDate);


            sonuc= (float) (deger-b);
            float syuzde= (float) ((sonuc*100)*-1/deger);

            dusenFiyatorani.setText( sonuc+ " $");
            oran.setText("% "+ String.valueOf(syuzde).substring(0,6));
            if (sonuc>0) {
                oranimage.setBackgroundResource(R.drawable.up);
            } else {
                oranimage.setBackgroundResource(R.drawable.down);
            }
            Database db=new Database(getApplicationContext());
            db.islemEkle(scoin.value,formattedDate);
            db.close();
        }
    }







    public void GetJson(String get_url,int a)
    {
        // İNDİRME İŞİNİ BURADA YAPACAĞIZ
        String jsonData = null;

        try {
            jsonData = run(get_url);
            Timber.i("JSON VERİMİZ : > " + jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // JSON VERİYİ NULL DEĞİLSE JAVA NESNELERİNE DÖNÜŞTÜR
        if (null != jsonData) {
            Timber.i("jsonData null değil ..");
            // VERİYİ GSON İLE NESNELERE DÖNÜŞTÜR
            Gson gson = new Gson();



                if(a==1)
                {
                    Type listType = new TypeToken<List<Scoin>>(){}.getType();
                    List<Scoin> posts = (List<Scoin>) gson.fromJson(jsonData, listType);

                    Scoin_fiyat=new String[20];
                    Scoin_date=new String[20];

                    for(int i=0;i<20;i++)
                    {
                        Date date = new Date(Long.parseLong(posts.get(i).date)*1L);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC +3"));
                        String formattedDate = sdf.format(date);

                        Scoin_fiyat[i]=posts.get(i).value.toString();
                        Scoin_date[i]=(formattedDate);
                    }

                }
                else
                {
                    scoin = gson.fromJson(jsonData, Scoin.class);
                    deger=Double.parseDouble(scoin.value);
                    String aq="asd";

                    Database db = new Database(getApplicationContext());

                    SCoins = db.Kurlar();
                    if(SCoins.size()>0) {
                        db_date = new String[SCoins.size()];
                        db_fiyat = new String[SCoins.size()];

                        boyut = SCoins.size() - 1;



                        for (int i = 0; i < SCoins.size(); i++) {
                            db_date[i] = SCoins.get(SCoins.size() - i - 1).get("tarih");
                            db_fiyat[i] = SCoins.get(SCoins.size() - i - 1).get("fiyat");
                        }
                         b=Double.parseDouble(db_fiyat[boyut]);


                    }

                }

                String as="stop";

        }

    }

    private String run(String url) throws IOException {
        Timber.i("run ..");
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public void alarm_dialog_show(final String last1)
    {

        dialog = new Dialog(MainActivity.this,android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alarm_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(true);


        final FrameLayout spinner =(FrameLayout) dialog.findViewById(R.id.spinner);
        final TextView up=(TextView)dialog.findViewById(R.id.text_up);
        final TextView down=(TextView)dialog.findViewById(R.id.text_down);
        final TextView spinnerText=(TextView)dialog.findViewById(R.id.spinnerText);
        final EditText fiyat=(EditText)dialog.findViewById(R.id.fiyat);
        final LinearLayout menu=(LinearLayout)dialog.findViewById(R.id.menu);
        final RelativeLayout close=(RelativeLayout)dialog.findViewById(R.id.close);
        final Button create_alarm=(Button)dialog.findViewById(R.id.btn_oky);
        final Button delete_alarm=(Button)dialog.findViewById(R.id.btn_cancel);

        int width= MainActivity.this.getResources().getDisplayMetrics().widthPixels;
        int height= MainActivity.this.getResources().getDisplayMetrics().heightPixels;

        create_alarm.setMinimumWidth(width/3+(width/4)/14);
        delete_alarm.setMinimumWidth(width/3+(width/4)/14);

        create_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startService(new Intent(MainActivity.this, BildirimIntent.class));
                addNotification(mesajim);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Alarm Oluşturuldu!",
                        Toast.LENGTH_LONG).show();
            }
        });

        delete_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        fiyat.setText(last1);
        fiyat.setSelection(last1.length());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerText.setText("");
                menu.setVisibility(View.VISIBLE);
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerText.setText(up.getText());
                menu.setVisibility(View.GONE);
                mesajim ="Scoin "+fiyat.getText()+" fiyatının üstüne çıktı";
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerText.setText(down.getText());
                menu.setVisibility(View.GONE);
                mesajim ="Scoin "+fiyat.getText()+" fiyatının altına düştü";
            }
        });

        dialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void addNotification(String mesaj)
    {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.coin)
                        .setContentTitle(Html.fromHtml("<b>Scoin Turkey</b>"))
                        .setContentText(mesaj);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
}
