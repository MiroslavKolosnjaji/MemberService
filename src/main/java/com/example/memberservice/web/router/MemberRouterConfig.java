package com.example.memberservice.web.router;

import com.example.memberservice.web.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author Miroslav Kološnjaji
 */
@Configuration
@RequiredArgsConstructor
public class MemberRouterConfig {
    public static final String MEMBER_PATH = "/api/member";
    public static final String MEMBER_PATH_ID = MEMBER_PATH + "/{member_id}";

    private final MemberHandler memberHandler;

    @Bean
    public RouterFunction<ServerResponse> memberRoutes() {
        return route()
                .POST(MEMBER_PATH, memberHandler::createMember)
                .GET(MEMBER_PATH_ID, memberHandler::getMember)
                .PUT(MEMBER_PATH_ID, memberHandler::updateMember)
                .DELETE(MEMBER_PATH_ID, memberHandler::deleteMemeber)
                .build();

    }
}
