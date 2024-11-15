package com.example.boardserver.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class MemberDeleteId {

    @NonNull
    private String id;

    @NonNull
    private String password;
}
