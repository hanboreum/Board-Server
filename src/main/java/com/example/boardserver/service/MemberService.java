package com.example.boardserver.service;

import com.example.boardserver.dto.MemberDTO;


public interface MemberService {

    void register(MemberDTO memberProfile); //회원가입

    MemberDTO login(String id, String password); //로그인

    boolean isDuplicated(String id); //중복 확인

    MemberDTO getMemberInfo(String memberId); //유저정보 조회

    void updatePassword(String id, String beforePassword, String afterPassword); //pw 변경

    void deleteId(String id, String password);
}
