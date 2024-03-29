package com.cupid.jikting.member.controller;

import com.cupid.jikting.common.support.AuthorizedVariable;
import com.cupid.jikting.member.dto.*;
import com.cupid.jikting.member.entity.Member;
import com.cupid.jikting.member.handler.LoginSuccessHandler;
import com.cupid.jikting.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final LoginSuccessHandler loginSuccessHandler;

    @PostMapping
    public ResponseEntity<LoginResponse> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletResponse response) throws IOException {
        Member member = memberService.signup(signupRequest);
        loginSuccessHandler.loginAfterSignup(response, member);
        return ResponseEntity.ok().body(LoginResponse.from(member));
    }

    @GetMapping
    public ResponseEntity<MemberResponse> get(@AuthorizedVariable Long memberProfileId) {
        return ResponseEntity.ok().body(memberService.get(memberProfileId));
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> getProfile(@AuthorizedVariable Long memberProfileId) {
        return ResponseEntity.ok().body(memberService.getProfile(memberProfileId));
    }

    @PatchMapping
    public ResponseEntity<Void> update(@AuthorizedVariable Long memberProfileId, @RequestBody NicknameUpdateRequest nicknameUpdateRequest) {
        memberService.update(memberProfileId, nicknameUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile")
    public ResponseEntity<Void> updateProfile(@AuthorizedVariable Long memberProfileId,
                                              @RequestPart(required = false) MultipartFile file,
                                              @RequestPart MemberProfileUpdateRequest memberProfileUpdateRequest) throws IOException {
        memberService.updateProfile(memberProfileId, file, memberProfileUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@AuthorizedVariable Long memberProfileId, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        memberService.updatePassword(memberProfileId, passwordUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@AuthorizedVariable Long memberProfileId, @RequestBody WithdrawRequest withdrawRequest) {
        memberService.withdraw(memberProfileId, withdrawRequest);
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
    public ResponseEntity<Void> createVerificationCodeForSignup(@RequestBody SignUpVerificationCodeRequest signUpVerificationCodeRequest)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        memberService.createVerificationCodeForSignup(signUpVerificationCodeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verification")
    public ResponseEntity<Void> verifyPhoneForSignup(@RequestBody PhoneVerificationRequest phoneVerificationRequest) {
        memberService.verifyPhoneForSignup(phoneVerificationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/username/search/code")
    public ResponseEntity<Void> createVerificationCodeForSearchUsername(@RequestBody UsernameSearchVerificationCodeRequest usernameSearchVerificationCodeRequest)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        memberService.createVerificationCodeForSearchUsername(usernameSearchVerificationCodeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/username/search/verification")
    public ResponseEntity<UsernameResponse> verifyForSearchUsername(@RequestBody VerificationPhoneRequest verificationPhoneRequest) {
        return ResponseEntity.ok().body(memberService.verifyForSearchUsername(verificationPhoneRequest));
    }

    @PostMapping("/password/reset/code")
    public ResponseEntity<Void> createVerificationCodeForResetPassword(@RequestBody PasswordResetVerificationCodeRequest passwordResetVerificationCodeRequest)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        memberService.createVerificationCodeForResetPassword(passwordResetVerificationCodeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset/verification")
    public ResponseEntity<Void> verifyForResetPassword(@RequestBody VerificationPhoneRequest verificationPhoneRequest) {
        memberService.verifyForResetPassword(verificationPhoneRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        memberService.resetPassword(passwordResetRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/company/code")
    public ResponseEntity<Void> createVerificationCodeForCompany(@AuthorizedVariable Long memberProfileId,
                                                                 @RequestBody CompanyVerificationCodeRequest companyVerificationCodeRequest)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        memberService.createVerificationCodeForCompany(memberProfileId, companyVerificationCodeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/company/verification")
    public ResponseEntity<Void> verifyForCompany(@AuthorizedVariable Long memberProfileId, @RequestBody VerificationEmailRequest verificationEmailRequest) {
        memberService.verifyForCompany(memberProfileId, verificationEmailRequest);
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

    @PostMapping("/company/block")
    public ResponseEntity<Void> blockCompany(@AuthorizedVariable Long memberProfileId) {
        memberService.blockCompany(memberProfileId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/report/{reportMemberProfileId}")
    public ResponseEntity<Void> report(@AuthorizedVariable Long memberProfileId, @PathVariable Long reportMemberProfileId, @RequestBody ReportMessageRequest reportMessageRequest) {
        memberService.report(memberProfileId, reportMemberProfileId, reportMessageRequest);
        return ResponseEntity.ok().build();
    }
}
