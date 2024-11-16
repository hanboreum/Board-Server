package com.example.boardserver.dto.response;

import com.example.boardserver.dto.PostDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {

    private List<PostDTO> dtos;
}
