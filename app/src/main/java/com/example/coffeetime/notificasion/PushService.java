    package com.example.coffeetime.notificasion;


    import android.content.Intent;
    import android.util.Log;

    import com.google.firebase.messaging.FirebaseMessagingService;
    import com.google.firebase.messaging.RemoteMessage;

    import java.util.Map;

    public class PushService extends FirebaseMessagingService {

        public static final String INTENT_FILTER = "PUSH_EVENT";
        public static final String KEY_ACTION = "action";
        public static final String KEY_MESSAGE = "message";
        public static final String KEY_BODY = "body";
        public static final String ACTIONS_SHOW_MESSAGE = "show_message";
        public static final String KEY_TITLE = "title";

        @Override
        public void onNewToken(String newToken) {
            super.onNewToken(newToken);
        }

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);
            Intent intent = new Intent(INTENT_FILTER);

            // Получаем данные из поля notification
            if (remoteMessage.getNotification() != null) {
                String title = remoteMessage.getNotification().getTitle();
                String body = remoteMessage.getNotification().getBody();
                if (title != null && body != null) {
                    intent.putExtra(KEY_TITLE, title);
                    intent.putExtra(KEY_BODY, body);
                    Log.d("NotificationData", "Title: " + title + ", Body: " + body);
                }
            }

            // Получаем данные из поля data
            Map<String, String> customData = remoteMessage.getData();
            if (customData != null) {
                for (Map.Entry<String, String> entry : customData.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    intent.putExtra(key, value);
                    Log.d("CustomData", "Key: " + key + ", Value: " + value);
                }
            }

            sendBroadcast(intent);
        }



    }