package com.servicehub.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.servicehub.Employee.MyInquiryActivity;
import com.servicehub.Employee.NewInquiryActivity;
import com.servicehub.R;

import java.util.Map;

//Created by Dharvik on 6/25/2016.

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static String Noty_type;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        remoteMessage.getCollapseKey();
        sendNotification(remoteMessage.getData());
    }

    //I/Message Body: {postid=25, status=1, bedge=3, title=message send, message=Service Book}

    private void sendNotification(Map<String, String> messageBody) {
        Log.i("Message Body", "" + messageBody);
        String message = "", postid = "", title = "";
        String status = "";

        try {

            status = messageBody.get("status");

            switch (status) {
                case "1":
                    message = messageBody.get("message");
                    postid = messageBody.get("postid");
                    title = messageBody.get("title");
                    Log.e("postiddd", postid);
                    break;
                case "2":
                    message = messageBody.get("message");
                    postid = messageBody.get("postid");
                    title = messageBody.get("title");
                    Log.e("postiddd", postid);
                    break;
                case "3":
                    message = messageBody.get("message");
                    postid = messageBody.get("postid");
                    title = messageBody.get("title");
                    Log.e("postiddd", postid);
                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();
            message = "";
        }
        Log.e("hellloo", "msg aayoo ");
        Log.i("Message Body", "" + messageBody);

        Intent noti_Intent = null;
        switch (status) {
            case "1":

                noti_Intent = new Intent(this, NewInquiryActivity.class);
                Log.i("intent", "done");
                noti_Intent.putExtra("postid", postid);

                break;
            case "2":

                noti_Intent = new Intent(this, My_order.class);
                Log.i("intent", "done");
                noti_Intent.putExtra("postid", postid);

            case "3":

                noti_Intent = new Intent(this, My_order.class);
                Log.i("intent", "done");
                noti_Intent.putExtra("postid", postid);

        }

        noti_Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent intent = PendingIntent.getActivity(this, 0, noti_Intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("ServiceHub")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(intent)
                .setWhen(System.currentTimeMillis());
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = getResources().getColor(R.color.colorGreen);
            builder.setSmallIcon(R.drawable.app_icon);
            builder.setColor(color);
        } else {
            builder.setSmallIcon(R.drawable.app_icon);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
