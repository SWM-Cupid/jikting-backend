package com.cupid.jikting.member.controller;

import com.cupid.jikting.member.dto.MemberProfileResponse;
import com.cupid.jikting.member.dto.MemberResponse;
import com.cupid.jikting.member.dto.MemberUpdateRequest;
import com.cupid.jikting.member.dto.SignupRequest;
import com.cupid.jikting.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> update(@RequestBody MemberUpdateRequest memberUpdateRequest) {
        memberService.update(memberUpdateRequest);
        return ResponseEntity.ok().build();
    }
}
