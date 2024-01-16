package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.web.dto.requestDto.RootRequestDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/health")
    public String healthCheck() {
        return "I'm health!";
    }

    @PostMapping("/ping")
    public ResponseDto<String> postTest(@RequestBody RootRequestDto.PostTestDto body) {
        if (body.getBody().equals("peerna")) {
            throw new MemberException(ResponseStatus.WRONG_POST_TEST);
        }
        return ResponseDto.of(body.getBody());
    }
}
