package com.example.boardserver.service;

import com.example.boardserver.dto.PostDTO;
import java.util.List;

public interface PostService {

    void register(String id, PostDTO dto);

    List<PostDTO> getMyProducts(int accountId);

    void updateProducts(PostDTO dto);

    void deleteProduct(int memberId, int productId);
}
