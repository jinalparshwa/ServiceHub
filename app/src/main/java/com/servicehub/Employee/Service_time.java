package com.servicehub.Employee;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.servicehub.Controller.Configr;
import com.servicehub.Controller.Connection;
import com.servicehub.Controller.Constants;
import com.servicehub.Controller.Time_service;
import com.servicehub.Custom_Compo.CustomTextview;
import com.servicehub.Model.Model_New_Inquiry;
import com.servicehub.Model.Model_time;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Service_time extends AppCompatActivity {

    RelativeLayout r_stop;
    ImageView back;
    Context context = this;
    Pref_Master pref;
    CustomTextview start_time;
    CustomTextview end_time;
    int hours;
    Model_time model = new Model_time();
    String inq_id;
    Intent timerService;
    long currentTime, duration = 0;
    CustomTextview tv_stop;

    @Override
    protected void onStart() {
        super.onStart();
        timerService = new Intent(this, Time_service.class);
        //Register broadcast if service is already running
        if (isMyServiceRunning(Time_service.class)) {
            registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.BROADCAST_ACTION));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_timer);
        pref = new Pref_Master(context);

        model = (Model_time) getIntent().getSerializableExtra("start_time");

        back = (ImageView) findViewById(R.id.back);
        tv_stop = (CustomTextview) findViewById(R.id.tv_stop);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (!isMyServiceRunning(Time_service.class)) {
            tv_stop.setText("Start");
        } else {
            tv_stop.setText("Stop");
        }
        r_stop = (RelativeLayout) findViewById(R.id.r_stop);
        r_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMyServiceRunning(Time_service.class)) {
                    timerService.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    timerService.putExtra(Constants.TIMER.DURATION, duration);
                    timerService.putExtra(Constants.TIMER.HOURS, hours);
                    pref.setTime(hours);
                    startService(timerService);
                    registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.BROADCAST_ACTION));
                    tv_stop.setText("Stop");
                } else {

                    stop_time();
                }

            }
        });
        start_time = (CustomTextview) findViewById(R.id.start_time);
        end_time = (CustomTextview) findViewById(R.id.end_time);
        start_time.setText(model.getStart_time());
        hours = Integer.parseInt(model.getHours());
        inq_id = model.getInq_id();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isMyServiceRunning(Time_service.class)) {
            //Resets timer if no service is running
            end_time.setText("00:00:00");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isMyServiceRunning(Time_service.class)) {
            unregisterReceiver(broadcastReceiver);
            Log.d(Service_time.class.getSimpleName(), "unregistered broadcast");
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!updateUI(intent)) {
                if (!updateUI(timerService)) {
                    timerService.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                    startService(timerService);
                    showTimerCompleteNotification();
                }
            }
        }
    };

    public boolean updateUI(Intent intent) {
        if (!intent.hasExtra(Constants.TIMER.CURRENT_TIME)) return false;

        this.currentTime = intent.getLongExtra(Constants.TIMER.CURRENT_TIME, 0L);

        if (this.currentTime == duration) {
            end_time.setText("00:00:00");
            Toast.makeText(this, "Timer done", Toast.LENGTH_SHORT).show();
            pref.setStr_inquiry("10101");
            stop_job();
            return false;
        }

        int secs = (int) (currentTime / 1000);
        int minutes = secs / 60;
        int hours = (int) ((currentTime / (1000 * 60 * 60)) % 24);
        end_time.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes % 60) + ":" + String.format("%02d", secs % 60));
        return true;
    }


    private void showTimerCompleteNotification() {
        Intent resultIntent = new Intent(this, Service_time.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        //.setContentIntent(resultPendingIntent)
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle("Timer Done!")
                        .setContentText("Congrats")
                        .setColor(Color.BLACK)
                        .setLights(Color.BLUE, 500, 500)
                        .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                        .setDefaults(NotificationCompat.DEFAULT_SOUND)
                        .setStyle(new NotificationCompat.InboxStyle());

        // Gets an instance of the NotificationManager service
        final NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, mBuilder.build());

        //Cancel the notification after a little while
        Handler h = new Handler();
        long delayInMilliseconds = 5000;

        h.postDelayed(new Runnable() {
            public void run() {
                mNotifyMgr.cancel(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE);
            }
        }, delayInMilliseconds);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void stop_time() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to End Your job?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pref.setStr_inquiry("10101");
                        if (isMyServiceRunning(Time_service.class)) {
                            end_time.setText("00:00:00");
                            timerService.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                            startService(timerService);
                            unregisterReceiver(broadcastReceiver);
                        }
                        tv_stop.setText("Stop");
                        stop_job();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        String myclr = "#19b41e";
        Button bq = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        bq.setTextColor(Color.parseColor(myclr));

        Button bq1 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        bq1.setTextColor(Color.parseColor(myclr));


    }

    public void stop_job() {


        String url = Configr.app_url + "completeinquiry/" + inq_id;

        Map<String, String> params = new HashMap<>();

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {
                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Service_time.this, MyEarningActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  signup.setClickable(true);

            }
        };
        Connection.getconnectionVolley(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    public void timer() {
        CountDownTimer timer = new CountDownTimer(hours * 3600000, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                end_time.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }

            public void onFinish() {
                end_time.setText("Time Up");
            }
        };
        timer.start();
    }
}


