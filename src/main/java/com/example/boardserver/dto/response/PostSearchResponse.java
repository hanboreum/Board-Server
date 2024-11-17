package com.example.boardserver.dto.response;

import com.example.boardserver.dto.PostDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostSearchResponse {

    private List<PostDTO> dtos;
}
