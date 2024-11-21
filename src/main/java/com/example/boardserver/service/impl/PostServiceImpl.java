package com.example.boardserver.service.impl;

import com.example.boardserver.dto.CommentDTO;
import com.example.boardserver.dto.MemberDTO;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.TagDTO;
import com.example.boardserver.exception.BoardServerException;
import com.example.boardserver.mapper.CommentMapper;
import com.example.boardserver.mapper.MemberProfileMapper;
import com.example.boardserver.mapper.PostMapper;
import com.example.boardserver.mapper.TagMapper;
import com.example.boardserver.service.PostService;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final MemberProfileMapper memberProfileMapper;
    private final CommentMapper commentMapper;
    private final TagMapper tagMapper;

    @Override
    @CacheEvict(value = "getPosts", allEntries = true)
    public void register(String id, PostDTO postDTO) {
        MemberDTO memberDTO = memberProfileMapper.getMemberProfile(id);
        postDTO.setMemberId(memberDTO.getId());
        postDTO.setCreateTime(new Date());

        log.info("Account Id service : {}", id);
        if (memberDTO != null) {
            try {
                postMapper.register(postDTO);
            }catch (RuntimeException e) {
                log.error("POST REGISTER ERROR! {}", postDTO);
                throw new BoardServerException(HttpStatus.BAD_REQUEST, e.getMessage());
            }

            int postId = postDTO.getId();
            // 생성됨 post 객체에서 태크 리스트 생성
            for (int i = 0; i < postDTO.getTags().size(); i++) {
                log.info("postID::::{}", postId);

                TagDTO tagDTO = postDTO.getTags().get(i);
                // 태그가 아직 등록되지 않았으므로, 먼저 태그를 등록
                tagMapper.register(tagDTO);

                // 태그가 등록되었으므로 이제 tag_id를 얻을 수 있음
                int tagId = tagDTO.getId();
                log.info("tagID::::{}", tagId);

                // M:N 관계에서 post와 tag를 연결
                tagMapper.createPostTag(tagId, postId);

            }

        } else {
            log.error("POST REGISTER ERROR! {}", postDTO);
            throw new RuntimeException(
                    "else - register ERROR! 상품 등록 메서드를 확인해주세요\n" + "Params : " + postDTO);
        }
    }

    @Override
    public List<PostDTO> getMyProducts(int accountId) {
        List<PostDTO> dtoList =null;
        try {
            postMapper.selectMyProducts(accountId);
        }catch (RuntimeException e){
            log.error("getMyProducts ERROR! {}", e);
            throw new BoardServerException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return dtoList;
    }

    @Override
    public void updateProducts(PostDTO dto) {
        log.info("REQUEST:: {}", dto);

        if (dto != null && dto.getId() != 0 && dto.getMemberId() != 0) {
            try {
                postMapper.updateProducts(dto);
            }catch (RuntimeException e){
                log.error("POST UPDATE ERROR! {}", dto);
                throw new RuntimeException("update ERROR!\n" + dto);
            }
        } else {
            log.error("else - POST UPDATE ERROR! {}", dto);
            throw new RuntimeException("update ERROR!\n" + dto);
        }
    }

    @Override
    public void deleteProduct(int memberId, int productId) {
        if (memberId != 0 && productId != 0) {
            try {
                postMapper.deleteProduct(productId);
            }catch (RuntimeException e){
                log.error("POST DELETE ERROR! {}", productId);
                throw new RuntimeException("delete ERROR!\n" + productId);
            }
        } else {
            log.error("else - POST DELETE ERROR! {}", productId);
            throw new RuntimeException("delete ERROR!\n" + productId);
        }
    }

    /**
     * comment
     */
    @Override
    public void registerComment(CommentDTO dto) {
        if (dto.getPostId() != 0) {
            commentMapper.register(dto);
        } else {
            log.error("registerComment ERROR! {}", dto);
            throw new RuntimeException(
                    "registerComment ERROR! 댓글 추가 메서드를 확인해주세요\n" + "Params : " + dto);
        }
    }

    @Override
    public void updateComment(CommentDTO dto) {
        if (dto != null) {
            commentMapper.updateComments(dto);
        } else {
            log.error("updateComment ERROR! {}", dto);
            throw new RuntimeException("updateComment ERROR! 댓글 변경 메서드를 확인해주세요\n" +
                    "Params : " + dto);
        }
    }

    @Override
    public void deleteComment(int memberId, int commentId) {
        if (memberId != 0 && commentId != 0) {
            commentMapper.deletePostComment(commentId);
        } else {
            log.error("deletePostComment ERROR! {}", commentId);
            throw new RuntimeException(
                    "deletePostComment ERROR! 댓글 삭제 메서드를 확인해주세요\n" + "Params : " + commentId);
        }
    }

    /**
     * Tag
     */
    @Override
    public void registerTag(TagDTO dto) {
        if (dto.getPostId() != 0) { //dto != null ?
            tagMapper.register(dto);
        } else {
            log.error("registerTag ERROR! {}", dto);
            throw new RuntimeException("registerTag ERROR! 태그 추가 메서드를 확인해주세요\n"
                    + "Params : " + dto);
        }
    }

    @Override
    public void updateTag(TagDTO dto) {
        if (dto != null) {
            tagMapper.updateTags(dto);
        } else {
            log.error("updateTag ERROR! {}", dto);
            throw new RuntimeException("updateTag ERROR! 태그 변경 메서드를 확인해주세요\n"
                    + "Params : " + dto);
        }
    }

    @Override
    public void deletePostTag(int memberId, int tagId) {
        if (memberId != 0 && tagId != 0) {
            tagMapper.deletePostTag(tagId);
        } else {
            log.error("deletePostTag ERROR! {}", tagId);
            throw new RuntimeException("deletePostTag ERROR! 태그 삭제 메서드를 확인해주세요\n"
                    + "Params : " + tagId);
        }
    }
}

