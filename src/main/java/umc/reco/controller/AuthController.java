package umc.reco.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.reco.dto.request.LoginDto;
import umc.reco.dto.response.TokenDto;
import umc.reco.exception.ExceptionResponse;
import umc.reco.exception.NotQualifiedDtoException;
import umc.reco.service.MemberService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            return memberService.join(loginDto);
        } catch (NotQualifiedDtoException e) {
            return errorMessage(e.getMessage());
        }
    }

    private static ResponseEntity<ExceptionResponse> errorMessage(String message) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(message));
    }
}
