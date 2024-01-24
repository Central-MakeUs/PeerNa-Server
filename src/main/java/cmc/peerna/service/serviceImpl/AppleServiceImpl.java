package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.AppleOAuthException;
import cmc.peerna.feign.AppleSocialFeign;
import cmc.peerna.feign.dto.ApplePublicKeyDTO;
import cmc.peerna.feign.dto.ApplePublicKeyListDTO;
import cmc.peerna.service.AppleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppleServiceImpl implements AppleService {
    private final AppleSocialFeign appleSocialFeign;

    @Override
    public String userIdFromApple(String identityToken) {
        ApplePublicKeyListDTO applePublicKeys = appleSocialFeign.getApplePublicKeys();
        ApplePublicKeyDTO matchesKey = null;
        try {
            JSONParser parser = new JSONParser();
            String[] decodeArr = identityToken.split("\\.");

            log.info("decode Arr의 값 : {}", decodeArr);
            String header = new String(Base64.getDecoder().decode(decodeArr[0]));

            JSONObject headerJson = (JSONObject) parser.parse(header);

            log.info("identity token의 전자서명 정보 : {}", headerJson);

            Object kid = headerJson.get("kid");
            Object alg = headerJson.get("alg");

            matchesKey = applePublicKeys.getMatchesKey(String.valueOf(alg), String.valueOf(kid));
        }catch (ParseException e){
            e.printStackTrace();
            throw new AppleOAuthException(ResponseStatus.WRONG_IDENTITY_TOKEN);
        }

        PublicKey publicKey = this.getPublicKey(matchesKey);

        Claims userInfo = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(identityToken).getBody();

        log.info("파싱된 유저의 정보 : {}", userInfo);

        String userId = userInfo.get("sub", String.class);

        return userId;
    }

    public PublicKey getPublicKey(ApplePublicKeyDTO applePublicKeyDTO) {

        log.info("전자서명을 위한 공개키 구성 재료 : {}", applePublicKeyDTO.toString());
        String nStr = applePublicKeyDTO.getN();
        String eStr = applePublicKeyDTO.getE();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr);
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr);

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKeyDTO.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Exception exception) {
            throw new AppleOAuthException(ResponseStatus.FAILED_TO_FIND_AVAILABLE_RSA);
        }
    }

}
