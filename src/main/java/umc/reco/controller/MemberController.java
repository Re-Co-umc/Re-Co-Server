package umc.reco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.reco.dto.request.EditProfileDto;
import umc.reco.dto.response.ProfileDto;
import umc.reco.entity.Member;
import umc.reco.exception.ExceptionResponse;
import umc.reco.exception.NotQualifiedDtoException;
import umc.reco.service.MemberService;

@RestController
@RequestMapping("/")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        return ResponseEntity.ok(memberService.getProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<?> editProfile(@RequestBody EditProfileDto editProfileDto) {
        try {
            return ResponseEntity.ok(memberService.editProfile(editProfileDto));
        } catch (NotQualifiedDtoException e) {
            return errorMessage(e.getMessage());
        }
    }
    @GetMapping("/my-list")
    public ResponseEntity<?> getMyList(){
        try{
            return ResponseEntity.ok(memberService.searchAll());
        }catch (NotQualifiedDtoException e){
            return errorMessage(e.getMessage());
        }
    }

    private static ResponseEntity<ExceptionResponse> errorMessage(String message) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(message));
    }
}
