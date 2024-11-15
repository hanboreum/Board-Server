package com.example.boardserver.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequest {

    @NonNull
    private String memberId;

    @NonNull
    private String password;

}
