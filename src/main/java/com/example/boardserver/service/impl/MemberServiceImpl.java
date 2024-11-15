package com.example.boardserver.service.impl;

import static com.example.boardserver.utils.SHA256Util.encryptSHA256;

import com.example.boardserver.dto.MemberDTO;
import com.example.boardserver.exception.DuplicateException;
import com.example.boardserver.mapper.MemberProfileMapper;
import com.example.boardserver.service.MemberService;
import com.example.boardserver.utils.SHA256Util;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberProfileMapper memberProfileMapper;

    @Override
    public void register(MemberDTO memberProfile) {
        boolean duplicatedResult = isDuplicated(memberProfile.getMemberId());
        if (duplicatedResult) {
            throw new DuplicateException("중복된 아이디");
        }

        memberProfile.setCreateTime(new Date());
        memberProfile.setPassword(encryptSHA256(memberProfile.getPassword()));
        int insertCount = memberProfileMapper.register(memberProfile);

        if (insertCount != 1) {
            log.error("REGISTER ERROR {}", memberProfile);
            throw new RuntimeException(
                    "insert member ERROR! 회원가입 메서드 확인하세요\n" + "Param : " + memberProfile);
        }
    }

    @Override
    public MemberDTO login(String id, String password) {
        String cryptoPassword = SHA256Util.encryptSHA256(password);
        MemberDTO memberInfo = memberProfileMapper.findByMemberIdAndPassword(id, cryptoPassword);
        return memberInfo;
    }

    @Override
    public boolean isDuplicated(String id) {
        return memberProfileMapper.idCheck(id) == 1; //1이면 중복
    }

    @Override
    public MemberDTO getMemberInfo(String memberId) {
        return memberProfileMapper.getMemberProfile(memberId);
    }

    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {
        String cryptoPassword = SHA256Util.encryptSHA256(beforePassword);
        log.info("Attempting to find user with id: {} and password hash: {}", id, cryptoPassword);
        MemberDTO memberInfo = memberProfileMapper.findByIdAndPassword(id, cryptoPassword);

        if (memberInfo != null) {
            memberInfo.setPassword(SHA256Util.encryptSHA256(afterPassword));
            int insertCount = memberProfileMapper.updatePassword(memberInfo);
        } else {
            log.error("update password ERROR {}", memberInfo);
            log.error("No user found with id: {} and password hash: {}", id, cryptoPassword);
            throw new IllegalArgumentException(
                    "update Password ERROR 메서드를 확인해주세요\n" + "Params" + memberInfo);
        }
    }

    @Override
    public void deleteId(String id, String password) {
        String cryptoPassword = SHA256Util.encryptSHA256(password);
        MemberDTO memberInfo = memberProfileMapper.findByIdAndPassword(id, cryptoPassword);

        if(memberInfo != null){
            memberProfileMapper.deleteMemberProfile(memberInfo.getMemberId());
        }else{
            log.error("delete ID ERROR {}", memberInfo);
            throw new RuntimeException("delete ID ERROR 메서드 확인 요함 \n" +  "Params" +memberInfo);
        }
    }
}
