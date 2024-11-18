package com.example.boardserver.service.impl;

import com.example.boardserver.dto.CommentDTO;
import com.example.boardserver.dto.MemberDTO;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.dto.TagDTO;
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
            postMapper.register(postDTO);
            int postId = postDTO.getId();
            // 생성됨 post 객체에서 태크 리스트 생성
            for(int i=0; i<postDTO.getTags().size(); i++){
                log.info("postID::::{}",postId );
                TagDTO tagDTO = postDTO.getTags().get(i);

                tagDTO.setPostId(postId); //임의로 추가

                tagMapper.register(tagDTO);
                Integer tagId = tagDTO.getId();
                // M:N 관계 테이블 생성
                tagMapper.createPostTag(tagId, postId);
            }

        } else {
            log.error("POST REGISTER ERROR! {}", postDTO);
            throw new RuntimeException(
                    "register ERROR! 상품 등록 메서드를 확인해주세요\n" + "Params : " + postDTO);
        }
    }

    @Override
    public List<PostDTO> getMyProducts(int accountId) {
        List<PostDTO> dtoList = postMapper.selectMyProducts(accountId);
        return dtoList;
    }

    @Override
    public void updateProducts(PostDTO dto) {
        log.info("REQUEST:: {}", dto);
        if (dto != null && dto.getId() != 0 && dto.getMemberId() != 0) {
            postMapper.updateProducts(dto);
        } else {
            log.error("POST UPDATE ERROR! {}", dto);
            throw new RuntimeException("update ERROR!\n" + dto);
        }
    }

    @Override
    public void deleteProduct(int memberId, int productId) {
        if (memberId != 0 && productId != 0) {
            postMapper.deleteProduct(productId);
        } else {
            log.error("POST DELETE ERROR! {}", productId);
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

