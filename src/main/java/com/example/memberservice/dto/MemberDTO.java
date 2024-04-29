package com.example.memberservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Size(min = 9, max = 9)
    private String phone;
    @NotBlank
    private String email;
}
