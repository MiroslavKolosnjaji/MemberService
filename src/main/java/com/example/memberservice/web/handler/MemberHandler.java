package com.example.memberservice.web.handler;

import com.example.memberservice.dto.MemberDTO;
import com.example.memberservice.model.Member;
import com.example.memberservice.service.MemberService;
import com.example.memberservice.web.router.MemberRouterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * @author Miroslav Kološnjaji
 */
@Component
@RequiredArgsConstructor
public class MemberHandler {

    public static final String MEMBER_ID = "memberId";
    private final MemberService memberService;
    private final Validator validator;



    public Mono<ServerResponse> createMember(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MemberDTO.class).doOnNext(this::validate)
                .flatMap(memberService::save)
                .flatMap(savedMember ->
                        ServerResponse.created(UriComponentsBuilder.fromPath(MemberRouterConfig.MEMBER_PATH_ID)
                                .buildAndExpand(savedMember.getId()).toUri())
                                .build());
    }

    public Mono<ServerResponse> updateMember(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MemberDTO.class)
                .doOnNext(this::validate)
                .flatMap(memberService::update)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(memberDTO -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchMember(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MemberDTO.class)
                .flatMap(memberService::patch)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(memberDTO -> ServerResponse.noContent().build());
    }


    public Mono<ServerResponse> getMemberById(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(memberService.findById(Long.valueOf(serverRequest.pathVariable(MEMBER_ID)))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))), Member.class);
    }

    public Mono<ServerResponse> getAllMembers(ServerRequest serverRequest) {
        return ServerResponse.ok().body(memberService.findAll(), Member.class);
    }

    public Mono<ServerResponse> deleteMemeber(ServerRequest serverRequest) {
        return memberService.findById(Long.valueOf(serverRequest.pathVariable(MEMBER_ID)))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(memberDTO -> memberService.deleteById(memberDTO.getId()))
                .then(ServerResponse.noContent().build());

    }

    private void validate(MemberDTO memberDTO) {
        Errors errors = new BeanPropertyBindingResult(memberDTO, "memberDTO");
        validator.validate(memberDTO, errors);

        if (errors.hasErrors())
            throw new ServerWebInputException(errors.toString());
    }
}
