package com.example.boardserver.controller;

import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.dto.MemberDTO;
import com.example.boardserver.dto.request.MemberDeleteId;
import com.example.boardserver.dto.request.MemberLoginRequest;
import com.example.boardserver.dto.request.MemberUpdatePasswordRequest;
import com.example.boardserver.dto.response.LoginResponse;
import com.example.boardserver.dto.response.MemberInfoResponse;
import com.example.boardserver.service.impl.MemberServiceImpl;
import com.example.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberServiceImpl memberService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody MemberDTO memberDTO) {
        if (memberDTO.hasNullDataBeforeSignup(memberDTO)) {
            throw new RuntimeException("회원가입 정보를 확인해주세요");
        }
        memberService.register(memberDTO);
    }

    @PostMapping("sign-in")
    public HttpStatus login(@RequestBody MemberLoginRequest request, HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String memberId = request.getMemberId();
        String password = request.getPassword();
        MemberDTO memberInfo = memberService.login(memberId, password);
        String id = memberInfo.getId().toString();

        log.info("MEmber ID: {}", memberId);
        if (memberInfo == null) {
            return HttpStatus.NOT_FOUND;
        } else if (memberInfo != null) {
            LoginResponse response = LoginResponse.success(memberInfo);
            if (memberInfo.getStatus() == (MemberDTO.Status.ADMIN)) {
                SessionUtil.setLoginAdminId(session, id);
            } else {
                SessionUtil.setLoginMemberId(session, id);
            }

            responseEntity = new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
        } else {
            throw new RuntimeException("Login ERROR 없거나 지워진 유저 정보");
        }
        return HttpStatus.OK;
    }

    @GetMapping("my-info")
    public MemberInfoResponse memberInfo(HttpSession session) {
        String id = SessionUtil.getLoginMemberId(session);
        if (id == null) {
            id = SessionUtil.getLoginAdminId(session);
        }
        log.info("memberID: {}", id);
        MemberDTO memberInfo = memberService.getMemberInfo(id);
        return new MemberInfoResponse(memberInfo);
    }

    @PutMapping("logout")
    public void logout(HttpSession session) {
        SessionUtil.clear(session);
    }

    @PatchMapping("password")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<LoginResponse> updateMemberPassword(String accountId,
            @RequestBody MemberUpdatePasswordRequest request, HttpSession session) {

        ResponseEntity<LoginResponse> responseEntity = null;
        String Id = accountId;
        String beforePassword = request.getBeforePassword();
        String afterPassword = request.getAfterPassword();

        log.info("memberId : {}", Id);
        try {
            memberService.updatePassword(Id, beforePassword, afterPassword);
            MemberDTO memberInfo = memberService.login(Id, afterPassword);
            LoginResponse response = LoginResponse.success(memberInfo);
            ResponseEntity.ok(new ResponseEntity<LoginResponse>(response, HttpStatus.OK));
        } catch (IllegalArgumentException e) {
            log.error("updatePassword 실패", e);
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<LoginResponse> deleteId(@RequestBody MemberDeleteId memberDeleteId,
            HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String Id = SessionUtil.getLoginMemberId(session);

        log.info("Request ID and password: {}, {}", memberDeleteId.getId(),
                memberDeleteId.getPassword());

        try {
            MemberDTO userInfo = memberService.login(Id, memberDeleteId.getPassword());
            memberService.deleteId(Id, memberDeleteId.getPassword());
            LoginResponse loginResponse = LoginResponse.success(userInfo);
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.info("deleteID 실패");
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
