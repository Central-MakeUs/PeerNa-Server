package cmc.peerna.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmService {
    Logger logger = LoggerFactory.getLogger(FcmService.class);

    public void testFCMService(String fcmToken)
    {
        logger.info("받은 FCM 토큰 값 : " + fcmToken);
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(
                        Notification.builder()
                                .setTitle("PeerNa FCM 테스트입니당")
                                .setBody("테스트 성공했나요??")
                                .build())
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("the response of request FCM : {}",response);
        }catch (FirebaseMessagingException e){
            e.printStackTrace();
        }
    }
}
