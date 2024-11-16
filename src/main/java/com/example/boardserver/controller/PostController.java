package com.example.boardserver.controller;


import com.example.boardserver.aop.LoginCheck;
import com.example.boardserver.aop.LoginCheck.UserType;
import com.example.boardserver.dto.MemberDTO;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.request.PostDeleteRequest;
import com.example.boardserver.dto.request.PostRequest;
import com.example.boardserver.dto.response.CommonResponse;
import com.example.boardserver.service.impl.MemberServiceImpl;
import com.example.boardserver.service.impl.PostServiceImpl;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Log4j2
public class PostController {

    private final PostServiceImpl postService;
    private final MemberServiceImpl memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = UserType.MEMBER)
    public ResponseEntity<CommonResponse<PostDTO>> registerPost(String accountId,
            @RequestBody PostDTO dto) {

        log.info("account ID: {}", accountId);

        postService.register(accountId, dto);
        CommonResponse response =
                new CommonResponse(HttpStatus.OK, "SUCCESS", "registerPost", dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("my-posts")
    @LoginCheck(type = UserType.MEMBER)
    public ResponseEntity<CommonResponse<List<PostDTO>>> myPostInfo(String accountId) {
        MemberDTO memberDTO = memberService.getMemberInfo(accountId);
        List<PostDTO> postDTOS = postService.getMyProducts(memberDTO.getId());
        CommonResponse response =
                new CommonResponse(HttpStatus.OK, "SUCCESS", "myPostInfo", postDTOS);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{postId}")
    @LoginCheck(type = UserType.MEMBER)
    public ResponseEntity<CommonResponse<PostDTO>> updatePost(String accountId,
            @PathVariable(name = "postId") int postId,
            @RequestBody PostRequest request) {
        log.info("REQUEST:: {}", request);
        MemberDTO memberDTO = memberService.getMemberInfo(accountId);
        PostDTO postDTO = PostDTO.builder()
                .id(postId)
                .name(request.getName())
                .contents(request.getContents())
                .views(request.getViews())
                .categoryId(request.getCategoryId())
                .memberId(memberDTO.getId())
                .fileId(request.getFileId())
                .updateTime(new Date())
                .build();
        postService.updateProducts(postDTO);
        CommonResponse commonResponse
                = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "updatePosts", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("{postId}")
    @LoginCheck(type = UserType.MEMBER)
    public ResponseEntity<CommonResponse<PostDeleteRequest>> deletePost(String accountId,
            @PathVariable(name = "postId") int postId,
            @RequestBody PostDeleteRequest request) {
        MemberDTO memberDTO = memberService.getMemberInfo(accountId);
        postService.deleteProduct(memberDTO.getId(), postId);
        CommonResponse commonResponse =
                new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deleteposts", request);
        return ResponseEntity.ok(commonResponse);
    }
}
