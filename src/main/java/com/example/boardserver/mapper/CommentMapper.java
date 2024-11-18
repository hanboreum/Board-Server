package com.example.boardserver.mapper;

import com.example.boardserver.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 이 부분이 DB와 소통.
 * mapper.xml 과 매개변수 값을 맞춰줘야함
 */
@Mapper
public interface CommentMapper {

    public int register(CommentDTO commentDTO);

    public void updateComments(CommentDTO commentDTO);

    public void deletePostComment(int commentId);
}
