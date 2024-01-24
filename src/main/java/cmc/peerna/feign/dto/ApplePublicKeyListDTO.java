package cmc.peerna.feign.dto;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.AppleOAuthException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApplePublicKeyListDTO {

    private List<ApplePublicKeyDTO> keys;

    public ApplePublicKeyDTO getMatchesKey(String alg, String kid) {
        return this.keys
                .stream()
                .filter(k -> k.getAlg().equals(alg) && k.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new AppleOAuthException(ResponseStatus.FAILED_TO_FIND_AVAILABLE_RSA));
    }
}
