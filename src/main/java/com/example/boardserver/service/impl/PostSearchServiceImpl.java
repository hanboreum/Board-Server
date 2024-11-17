package com.example.boardserver.service.impl;

import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.PostSearchRequest;
import com.example.boardserver.mapper.PostSearchMapper;
import com.example.boardserver.service.PostSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private final PostSearchMapper postSearchMapper;

    @Async
    @Cacheable(value = "getPosts",
            key = "'getPosts' + #request.getName() + #request.getCategoryId()",
            unless = "#result == null")
    @Override
    public List<PostDTO> getPosts(PostSearchRequest request) {
        List<PostDTO> dtos = null;
        try {
            dtos = postSearchMapper.selectPosts(request);
        } catch (RuntimeException e) {
            log.error("select 실패 {}", e
                    .getMessage());
            log.error(e);
        }
        return dtos;
    }
}
