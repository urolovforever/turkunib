package com.tiu.turk.member.admin.dto.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Generated;

public class CountryCreateDto {
    @NotBlank(message="Name must not be blank")
    @Size(max=512, message="Name length must not exceed 512 characters")
    private @NotBlank(message="Name must not be blank") @Size(max=512, message="Name length must not exceed 512 characters") String name;

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }
}

