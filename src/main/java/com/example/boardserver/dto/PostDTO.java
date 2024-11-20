package com.example.boardserver.dto;
import java.util.List;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private int id;
    private String name;
    private int isAdmin;
    private String contents;
    private Date createTime;
    private int views;
    private int categoryId;
    private int memberId;
    private int fileId;
    private Date updateTime;
    private List<TagDTO> tags;
}
/**
 * comment 와는 1:n
 * tag 와는 M:N
 */