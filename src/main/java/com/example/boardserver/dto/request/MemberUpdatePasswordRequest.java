package com.example.boardserver.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdatePasswordRequest {

    @NonNull
    private String beforePassword;

    @NonNull
    private String afterPassword;


}
