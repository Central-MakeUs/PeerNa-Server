package cmc.peerna.fcm.service;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.NotificationException;
import cmc.peerna.fcm.dto.FcmAOSMessage;
import cmc.peerna.feign.FCMFeignClient;
import cmc.peerna.feign.dto.FCMResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmService {
    private final ObjectMapper objectMapper;
    private final FCMFeignClient fcmFeignClient;
    Logger logger = LoggerFactory.getLogger(FcmService.class);

    private static final String FIREBASE_KEY_PATH = "firebase/firebase-adminsdk-key.json";
    @Value("${firebase.project.id}")
    private String projectId;


    @Transactional
    public void sendMessageTo(String targetToken, String title, String body, String targetNotification) throws IOException {
        String aosMessage = makeAOSMessage(targetToken, title, body, targetNotification);

        FCMResponseDto fcmResponse = fcmFeignClient.getFCMResponse("Bearer " + getAccessToken(),aosMessage);
        logger.info("성공? : {}",fcmResponse);
        logger.info("보낸 메세지 : {}",aosMessage);
    }

    private String makeAOSMessage(String targeToken, String title, String body, String targetNotification) throws JsonParseException, JsonProcessingException {
        FcmAOSMessage fcmMessage = FcmAOSMessage.builder()
                .message(
                        FcmAOSMessage.Message.builder()
                                .token(targeToken).
                                data(FcmAOSMessage.Data.builder()
                                        .title(title)
                                        .body(body)
                                        .targetNotificationPK(targetNotification).build()
                                ).
                                build()
                )
                .validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken(){
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FIREBASE_KEY_PATH).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch(IOException e){
            throw new NotificationException(ResponseStatus.FCM_ACCESS_TOKEN_REQUEST_ERROR);
        }
    }
}
