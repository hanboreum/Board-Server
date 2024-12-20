package com.example.boardserver.controller;

import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.PostSearchRequest;
import com.example.boardserver.dto.response.PostSearchResponse;
import com.example.boardserver.service.impl.PostSearchServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@Log4j2
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchServiceImpl postSearchService;

    //조회에 조건이 많을 때는 post 형식으로 사용하기도 한다.
    @PostMapping
    public PostSearchResponse search(@RequestBody PostSearchRequest request){
        List<PostDTO> dtos = postSearchService.getPosts(request);
        return new PostSearchResponse(dtos);
    }

    @GetMapping
    public PostSearchResponse searchByTagName(String tagName){
        log.info("searchByTagName: " + tagName);
        List<PostDTO> dtos = postSearchService.getPostsByTag(tagName);
        return new PostSearchResponse(dtos);
    }
}
