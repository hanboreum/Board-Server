package com.example.boardserver.service;

import com.example.boardserver.dto.CommentDTO;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.TagDTO;
import java.util.List;

public interface PostService {

    void register(String id, PostDTO dto);

    List<PostDTO> getMyProducts(int accountId);

    void updateProducts(PostDTO dto);

    void deleteProduct(int memberId, int productId);

    void registerComment(CommentDTO dto);

    void updateComment(CommentDTO dto);

    void deleteComment(int memberId, int commentId);

    void registerTag(TagDTO dto);

    void updateTag(TagDTO dto);

    void deletePostTag(int memberId, int tagId);
}
