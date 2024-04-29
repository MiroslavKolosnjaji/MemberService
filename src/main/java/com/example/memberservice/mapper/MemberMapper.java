package com.example.memberservice.mapper;

import com.example.memberservice.dto.MemberDTO;
import com.example.memberservice.model.Member;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface MemberMapper {

    MemberDTO memberToMemberDTO(Member member);
    Member memberDTOToMember(MemberDTO memberDTO);

}
