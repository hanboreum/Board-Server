package com.example.boardserver.mapper;

import com.example.boardserver.dto.TagDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper {

    public int register(TagDTO dto);

    public void updateTags(TagDTO dto);

    public void deletePostTag(int postId);

    //태그와 게시글의 m:n 관계를 표시하기 위한 post tag table 에 정보를 저장하기 위함
    public void createPostTag(Integer tagId, Integer postId); //Integer?
}
