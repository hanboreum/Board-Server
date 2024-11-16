package com.example.boardserver.service.impl;

import com.example.boardserver.dto.MemberDTO;
import com.example.boardserver.dto.PostDTO;
import com.example.boardserver.mapper.MemberProfileMapper;
import com.example.boardserver.mapper.PostMapper;
import com.example.boardserver.service.PostService;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final MemberProfileMapper memberProfileMapper;

    @Override
    public void register(String id, PostDTO postDTO) {
        MemberDTO memberDTO = memberProfileMapper.getMemberProfile(id);
        postDTO.setMemberId(memberDTO.getId());
        postDTO.setCreateTime(new Date());

        log.info("Account Id service : {}", id);
        if (memberDTO != null) {
            postMapper.register(postDTO);
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
        }else{
            log.error("POST UPDATE ERROR! {}", dto);
            throw new RuntimeException("update ERROR!\n" +dto);
        }
    }

    @Override
    public void deleteProduct(int memberId, int productId) {
        if(memberId != 0 && productId != 0){
            postMapper.deleteProduct(productId);
        }else{
            log.error("POST DELETE ERROR! {}", productId);
            throw new RuntimeException("delete ERROR!\n" +productId);
        }
    }
}
