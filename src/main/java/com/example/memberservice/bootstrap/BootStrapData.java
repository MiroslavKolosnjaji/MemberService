package com.example.memberservice.bootstrap;

import com.example.memberservice.model.Member;
import com.example.memberservice.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Miroslav Kološnjaji
 */
@Component
@AllArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {
        memberRepository.deleteAll().doOnSuccess(success -> loadCityData()).subscribe();
    }

    private void loadCityData() {

        memberRepository.count().subscribe(count -> {
            Member member1 = createMember("John", "Smith", "123456789", "test@example.com");
            Member member2 = createMember("Anna", "Thompson", "153253789", "anna@example.com");
            Member member3 = createMember("George", "Johnson", "123654324", "george.johnson@example.com");

            memberRepository.save(member1).subscribe();
            memberRepository.save(member2).subscribe();
            memberRepository.save(member3).subscribe();

        });


    }

    private Member createMember(String firstName, String lastName, String phone, String email) {
        return Member.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .build();
    }


}
