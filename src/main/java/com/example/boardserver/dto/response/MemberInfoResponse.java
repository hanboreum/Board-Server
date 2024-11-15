package com.example.boardserver.dto.response;

import com.example.boardserver.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoResponse {
    private MemberDTO memberDTO;
}
