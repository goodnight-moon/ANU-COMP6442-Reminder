package com.anu.dolist.notify;


import android.app.Notification ;
import android.app.NotificationChannel ;
import android.app.NotificationManager ;
import android.content.BroadcastReceiver ;
import android.content.Context ;
import android.content.Intent ;

import com.anu.dolist.Constants;

/**
 * reference: https://www.tutorialspoint.com/how-to-create-everyday-notifications-at-certain-time-in-android
 */
public class MyNotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;

    public void onReceive (Context context , Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;

        // 1: get content
        Notification notification = intent.getParcelableExtra( NOTIFICATION );
        int id = intent.getIntExtra( NOTIFICATION_ID ,  0);

        // 2:  create channel
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel( Constants.NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }


        // 3: show
        assert notificationManager != null;
        notificationManager.notify(id , notification);
    }
}
