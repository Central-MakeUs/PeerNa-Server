package cmc.peerna.service;

import java.io.IOException;
public interface AppleService {
    String userIdFromApple(String identityToken) throws IOException;

}
