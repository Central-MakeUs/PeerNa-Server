//package cmc.peerna.fcm;
//
//import cmc.peerna.apiResponse.code.ResponseStatus;
//import cmc.peerna.apiResponse.exception.handler.MemberException;
//import cmc.peerna.apiResponse.exception.handler.NotificationException;
//import cmc.peerna.domain.FcmToken;
//import cmc.peerna.domain.Member;
//import cmc.peerna.domain.Notification;
//import cmc.peerna.domain.enums.NotificationType;
//import cmc.peerna.repository.FcmTokenRepository;
//import cmc.peerna.repository.MemberRepository;
//import com.google.auth.oauth2.GoogleCredentials;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class FCMService {
//
//
//    private static final String PREFIX_ACCESS_TOKEN = "Bearer ";
//    private static final String PREFIX_FCM_REQUEST_URL = "https://fcm.googleapis.com/v1/projects/";
//    private static final String POSTFIX_FCM_REQUEST_URL = "/messages:send";
//    private static final String FIREBASE_KEY_PATH = "firebase/firebase-adminsdk-key.json";
//    private static final String GOOGLE_AUTH_URL = "https://www.googleapis.com/auth/cloud-platform";
////    private static final Map<NotificationType, Function<Notification, NotificationMessageGenerator>> GENERATOR_MAP =
////            Map.of(
////                    NotificationType.EVENT, EventNotificationMessageGenerator::new,
////                    NotificationType.COMMENT, CommentNotificationMessageGenerator::new
////            );
//
//
//    private final FcmTokenRepository fcmTokenRepository;
//    private final MemberRepository memberRepository;
//
//    private final RestTemplate restTemplate;
//
//    @Value("${firebase.project.id}")
//    private String projectId;
//
//    public void sendMessageTo(final Long receiverId, final Notification notification) {
//
//        // 알림 요청 받는 사람의 FCM Token이 존재하는지 확인
//        final FcmToken fcmToken = fcmTokenRepository.findByMemberId(receiverId)
//                .orElseThrow(() -> new NotificationException(ResponseStatus.FCM_TOKEN_NOT_FOUND));
//
//        // 메시지 생성
//        final String message = makeMessage(fcmToken.getToken(), notification);
//
//        final HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//
//        //OAuth 2.0 사용
//        httpHeaders.add(HttpHeaders.AUTHORIZATION, PREFIX_ACCESS_TOKEN + getAccessToken());
//
//        final HttpEntity<String> httpEntity = new HttpEntity<>(message, httpHeaders);
//
//        final String fcmRequestUrl = PREFIX_FCM_REQUEST_URL + projectId + POSTFIX_FCM_REQUEST_URL;
//
//        final ResponseEntity<String> exchange = restTemplate.exchange(
//                fcmRequestUrl,
//                HttpMethod.POST,
//                httpEntity,
//                String.class
//        );
//    }
//
//
//    private String makeMessage(final String targetToken, final Notification notification) {
//
//        final Long senderId = notification.getSenderId();
//        final Member sender = memberRepository.findById(senderId)
//                .orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));
//
//        final Data messageData = new Data(
//                sender.getName(), senderId.toString(),
//                notification.getReceiverId().toString(), notification.getMessage(),
//                sender.getOpenProfileUrl()
//        );
//
//        final Message message = new Message(messageData, targetToken);
//
//        final FcmMessage fcmMessage = new FcmMessage(DEFAULT_VALIDATE_ONLY, message);
//
//        try {
//            return objectMapper.writeValueAsString(fcmMessage);
//        } catch (JsonProcessingException e) {
//            log.error("메세지 보낼 때 JSON 변환 에러", e);
//            throw new NotificationException(CONVERTING_JSON_ERROR);
//        }
//    }
//
//
//
//
//
////    private String getAccessToken() {
////        try {
////            final GoogleCredentials googleCredentials = GoogleCredentials
////                    .fromStream(new ClassPathResource(FIREBASE_KEY_PATH).getInputStream())
////                    .createScoped(List.of(GOOGLE_AUTH_URL));
////
////            googleCredentials.refreshIfExpired();
////
////            return googleCredentials.getAccessToken().getTokenValue();
////        } catch (IOException e) {
////            throw new NotificationException(ResponseStatus.FCM_ACCESS_TOKEN_REQUEST_ERROR);
////        }
////    }
//
//
//
////     FCM 서버 AccessToken 발급
//
//    private String getAccessToken(){
//        try {
//            GoogleCredentials googleCredentials = GoogleCredentials
//                    .fromStream(new ClassPathResource(FIREBASE_KEY_PATH).getInputStream())
//                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
//            googleCredentials.refreshIfExpired();
//            return googleCredentials.getAccessToken().getTokenValue();
//        } catch(IOException e){
//            throw new NotificationException(ResponseStatus.FCM_ACCESS_TOKEN_REQUEST_ERROR);
//        }
//    }
//
//}
