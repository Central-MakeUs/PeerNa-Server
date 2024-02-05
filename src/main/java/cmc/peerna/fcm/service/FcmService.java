package cmc.peerna.fcm.service;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.FcmException;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.domain.FcmToken;
import cmc.peerna.domain.Member;
import cmc.peerna.repository.FcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmService {
    Logger logger = LoggerFactory.getLogger(FcmService.class);

    private final FcmTokenRepository fcmTokenRepository;

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
            throw new FcmException(ResponseStatus.FCM_SEND_MESSAGE_ERROR);
        }
    }

    public void sendFcmMessage(Member receiver, String title, String body) {
        Optional<FcmToken> fcmToken = fcmTokenRepository.findByMember(receiver);
        if(!fcmToken.isPresent()){
            return;
        }

        if (receiver.isPushAgree() == false) {
            return;
        }

        String token = fcmToken.get().getToken();
        logger.info("받은 FCM 토큰 값 : " + token);
        Message message = Message.builder()
                .setToken(token)
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("the response of request FCM : {}",response);
        } catch (FirebaseMessagingException e) {
            throw new FcmException(ResponseStatus.FCM_SEND_MESSAGE_ERROR);
        }
    }
}
