package com.example.memberservice.web.handler;

import com.example.memberservice.dto.MemberDTO;
import com.example.memberservice.model.Member;
import com.example.memberservice.web.router.MemberRouterConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberEndPointTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testCreateMember() {
        webTestClient.post()
                .uri(MemberRouterConfig.MEMBER_PATH)
                .body(Mono.just(getTestMember(null)), Member.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/api/member/4");
    }

    @Test
    void testCreateMemberBadRequest() {
        var memberDTO  = getTestMember(null);
        memberDTO.setFirstName("");
        memberDTO.setLastName("");


        webTestClient.post()
                .uri(MemberRouterConfig.MEMBER_PATH)
                .body(Mono.just(memberDTO), Member.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testUpdateMember() {
        var memberDTO = getTestMember(1L);
        memberDTO.setFirstName("JOSHUA");

        webTestClient.put()
                .uri(MemberRouterConfig.MEMBER_PATH_ID, memberDTO.getId())
                .body(Mono.just(memberDTO), Member.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateMemberBadRequest() {
        var memberDTO = getTestMember(1L);
        memberDTO.setFirstName("");

        webTestClient.put()
                .uri(MemberRouterConfig.MEMBER_PATH_ID, memberDTO.getId())
                .body(Mono.just(memberDTO), Member.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateMemberNotFound() {
        var memberDTO = getTestMember(1L);
        memberDTO.setFirstName("");

        webTestClient.put()
                .uri(MemberRouterConfig.MEMBER_PATH_ID, 99)
                .body(Mono.just(memberDTO), Member.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(4)
    void testPatchMemberIdFound() {
       var memberDTO = getTestMember(1L);

        webTestClient.put()
                .uri(MemberRouterConfig.MEMBER_PATH_ID, memberDTO.getId())
                .body(Mono.just(memberDTO), Member.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testPatchMemberIdNotFound() {

        webTestClient.put()
                .uri(MemberRouterConfig.MEMBER_PATH_ID, 99)
                .body(Mono.just(getTestMember(1L)), Member.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(1)
    void testGetMemberById() {
        webTestClient.get()
                .uri(MemberRouterConfig.MEMBER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(MemberDTO.class);
    }
    @Test
    void testGetMemberByIdNotFound() {
        webTestClient.get()
                .uri(MemberRouterConfig.MEMBER_PATH_ID, 99)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(2)
    void testGetAllMembers() {
        webTestClient.get()
                .uri(MemberRouterConfig.MEMBER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));
    }

    @Test
    void testDeleteMember() {
        var memberDTO = getTestMember(1L);

        webTestClient.delete()
                .uri(MemberRouterConfig.MEMBER_PATH_ID, memberDTO.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteMemberNotFound() {
                webTestClient.delete()
                .uri(MemberRouterConfig.MEMBER_PATH_ID, 99)
                .exchange()
                .expectStatus().isNotFound();
    }

    private Member getTestMember(Long id) {
        return Member.builder()
                .id(id)
                .firstName("John")
                .lastName("Smith")
                .phone("123456789")
                .email("test@example.com")
                .build();
    }
}