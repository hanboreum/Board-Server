package com.example.boardserver.mapper;

import com.example.boardserver.dto.MemberDTO;
import java.lang.reflect.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberProfileMapper {

    public MemberDTO getMemberProfile(@Param("id") String id); //memberId

    int insertMemberProfile(@Param("id") String id, @Param("password") String password,
            @Param("name") String name, @Param("address") String address,
            @Param("createTime") String createTime, @Param("updateTime") String updateTime);

    int deleteMemberProfile(@Param("id") String id);

    public int register(MemberDTO memberDTO);

    public MemberDTO findByIdAndPassword(@Param("id") String id, @Param("password") String password);

    public MemberDTO findByMemberIdAndPassword(@Param("memberId") String memberId,
            @Param("password") String password);

    int idCheck(@Param("id") String id);

    public int updatePassword(MemberDTO memberDTO);

    public int updateAddress(MemberDTO memberDTO);
}
