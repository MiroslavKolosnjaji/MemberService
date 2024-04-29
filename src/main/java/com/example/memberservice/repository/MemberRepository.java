package com.example.memberservice.repository;

import com.example.memberservice.model.Member;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {
}
