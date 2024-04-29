package com.example.memberservice.service;

import com.example.memberservice.dto.MemberDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Miroslav Kološnjaji
 */
public interface MemberService {

    Mono<MemberDTO> save(MemberDTO memberDTO);
    Mono<MemberDTO> update(MemberDTO memberDTO);
    Mono<MemberDTO> patch(MemberDTO memberDTO);
    Mono<MemberDTO> findById(Long id);
    Flux<MemberDTO> findAll();
    Mono<Void> deleteById(Long id);
}
