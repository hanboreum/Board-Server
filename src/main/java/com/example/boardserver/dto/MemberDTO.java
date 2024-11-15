package com.example.boardserver.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {

    private Integer id;

    private String memberId;

    private String password;

    private String nickName;

    private boolean isAdmin;

    private Date createTime;

    private Date updateTime;

    private boolean isWithDraw;

    private Status status;

    public enum Status {
        DEFAULT, ADMIN, DELETE
    }


    public boolean hasNullDataBeforeSignup(MemberDTO memberDTO) {
        return memberDTO.getMemberId() == null || memberDTO.getPassword() == null
                || memberDTO.getNickName() == null;
    }
}
