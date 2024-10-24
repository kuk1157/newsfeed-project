package com.sparta.iinewsfeedproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NameRequestDto {
    @NotBlank
    private String name;
}
