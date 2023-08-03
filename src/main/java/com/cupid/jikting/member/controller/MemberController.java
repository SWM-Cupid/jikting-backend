package com.cupid.jikting.member.controller;

import com.cupid.jikting.jwt.service.JwtService;
import com.cupid.jikting.member.dto.*;
import com.cupid.jikting.member.service.MemberService;
import com.cupid.jikting.member.service.SmsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final JwtService jwtService;
    private final SmsService smsService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest signupRequest) {
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
    public ResponseEntity<Void> update(@RequestHeader("Authorization") String token, @RequestBody NicknameUpdateRequest nicknameUpdateRequest) {
        memberService.update(jwtService.extractValidMemberProfileId(token), nicknameUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestHeader("Authorization") String token, @RequestBody MemberProfileUpdateRequest memberProfileUpdateRequest) {
        memberService.updateProfile(jwtService.extractValidMemberProfileId(token), memberProfileUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestHeader("Authorization") String token, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        memberService.updatePassword(jwtService.extractValidMemberProfileId(token), passwordUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/image")
    public ResponseEntity<Void> updateImage(@RequestPart("file") MultipartFile multipartFile) {
        memberService.updateImage(multipartFile);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        memberService.withdraw(1L, withdrawRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/username/check")
    public ResponseEntity<Void> checkDuplicatedUsername(@RequestBody UsernameCheckRequest usernameCheckRequest) {
        memberService.checkDuplicatedUsername(usernameCheckRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/nickname/check")
    public ResponseEntity<Void> checkDuplicatedNickname(@RequestBody NicknameCheckRequest nicknameCheckRequest) {
        memberService.checkDuplicatedNickname(nicknameCheckRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/code")
    public ResponseEntity<Void> createVerificationCodeForSignup(@RequestBody SignUpVerificationCodeRequest signUpVerificationCodeRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        smsService.createVerificationCodeForSignup(signUpVerificationCodeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verification")
    public ResponseEntity<Void> verifyPhoneForSignup(@RequestBody VerificationRequest verificationRequest) {
        memberService.verifyPhoneForSignup(verificationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/username/search/code")
    public ResponseEntity<Void> createVerificationCodeForSearchUsername(@RequestBody UsernameSearchVerificationCodeRequest usernameSearchVerificationCodeRequest) {
        memberService.createVerificationCodeForSearchUsername(usernameSearchVerificationCodeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/username/search/verification")
    public ResponseEntity<UsernameResponse> verifyForSearchUsername(@RequestBody VerificationRequest verificationRequest) {
        return ResponseEntity.ok().body(memberService.verifyForSearchUsername(verificationRequest));
    }

    @PostMapping("/password/reset/code")
    public ResponseEntity<Void> createVerificationCodeForResetPassword(@RequestBody PasswordResetVerificationCodeRequest passwordResetVerificationCodeRequest) {
        memberService.createVerificationCodeForResetPassword(passwordResetVerificationCodeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset/verification")
    public ResponseEntity<Void> verifyForResetPassword(@RequestBody VerificationRequest verificationRequest) {
        memberService.verifyForResetPassword(verificationRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        memberService.resetPassword(passwordResetRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/company/code")
    public ResponseEntity<Void> createVerificationCodeForCompany(@RequestBody CompanyVerificationCodeRequest companyVerificationCodeRequest) {
        memberService.createVerificationCodeForCompany(companyVerificationCodeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/company/verification")
    public ResponseEntity<Void> verifyForCompany(@RequestBody VerificationRequest verificationRequest) {
        memberService.verifyForCompany(verificationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/company/card")
    public ResponseEntity<Void> verifyCardForCompany(@RequestPart CompanyVerificationRequest companyVerificationRequest, @RequestPart(value = "file") MultipartFile multipartFile) {
        memberService.verifyCardForCompany(companyVerificationRequest, multipartFile);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        memberService.login(loginRequest);
        return ResponseEntity.ok().build();
    }
}
