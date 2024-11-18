package com.example.boardserver.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private int id;
    private String contents;
    private int postId;
    private int subCommentId;
}
