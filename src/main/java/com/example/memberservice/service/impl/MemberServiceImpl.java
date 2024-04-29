package com.example.memberservice.service.impl;

import com.example.memberservice.dto.MemberDTO;
import com.example.memberservice.mapper.MemberMapper;
import com.example.memberservice.repository.MemberRepository;
import com.example.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;


    @Override
    public Mono<MemberDTO> save(MemberDTO memberDTO) {
        return memberRepository.save(memberMapper.memberDTOToMember(memberDTO))
                .map(memberMapper::memberToMemberDTO);
    }

    @Override
    public Mono<MemberDTO> update(MemberDTO memberDTO) {
        return memberRepository.findById(memberDTO.getId())
                .map(foundMember -> {
                    foundMember.setFirstName(memberDTO.getFirstName());
                    foundMember.setLastName(memberDTO.getLastName());
                    foundMember.setEmail(memberDTO.getEmail());
                    foundMember.setPhone(memberDTO.getPhone());
                    return foundMember;
                }).flatMap(memberRepository::save)
                .map(memberMapper::memberToMemberDTO);
    }

    @Override
    public Mono<MemberDTO> patch(MemberDTO memberDTO) {
        return null;
    }

    @Override
    public Mono<MemberDTO> findById(Long id) {
        return memberRepository.findById(id).map(memberMapper::memberToMemberDTO);
    }

    @Override
    public Flux<MemberDTO> findAll() {
        return memberRepository.findAll().map(memberMapper::memberToMemberDTO);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return memberRepository.deleteById(id);
    }
}
