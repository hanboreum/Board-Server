package com.example.boardserver.dto.request;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    private String name;
    private String contents;
    private int views;
    private int categoryId;
    private int memberId;
    private int fileId;
    private Date updateTime;
}
