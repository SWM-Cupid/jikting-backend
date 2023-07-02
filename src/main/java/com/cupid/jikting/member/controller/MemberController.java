package com.cupid.jikting.member.controller;

import com.cupid.jikting.member.dto.*;
import com.cupid.jikting.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest) {
        memberService.signup(signupRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MemberResponse> get() {
        return ResponseEntity.ok().body(memberService.get(1L));
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> getProfile() {
        return ResponseEntity.ok().body(memberService.getProfile(1L));
    }

    @PatchMapping
    public ResponseEntity<Void> update(@RequestBody NicknameUpdateRequest nicknameUpdateRequest) {
        memberService.update(nicknameUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody MemberProfileUpdateRequest memberProfileUpdateRequest) {
        memberService.updateProfile(memberProfileUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        memberService.updatePassword(passwordUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/image")
    public ResponseEntity<Void> updateImage(@RequestPart("file") MultipartFile multipartFile) {
        memberService.updateImage(multipartFile);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody PasswordRequest passwordRequest) {
        memberService.withdraw(1L, passwordRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search/username/code")
    public ResponseEntity<Void> createVerificationCodeForSearchUsername(@RequestBody UsernameSearchVerificationCodeRequest usernameSearchVerificationCodeRequest) {
        memberService.createVerificationCodeForSearchUsername(usernameSearchVerificationCodeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search/username/verification")
    public ResponseEntity<UsernameResponse> verifyForSearchUsername(@RequestBody VerificationRequest verificationRequest) {
        return ResponseEntity.ok().body(memberService.verifyForSearchUsername(verificationRequest));
    }

    @PostMapping("/reset/password/code")
    public ResponseEntity<Void> createVerificationCodeForResetPassword(@RequestBody PasswordResetVerificationCodeRequest passwordResetVerificationCodeRequest) {
        memberService.createVerificationCodeForResetPassword(passwordResetVerificationCodeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset/password/verification")
    public ResponseEntity<Void> verifyForResetPassword(@RequestBody VerificationRequest verificationRequest) {
        memberService.verifyForResetPassword(verificationRequest);
        return ResponseEntity.ok().build();
    }
}
