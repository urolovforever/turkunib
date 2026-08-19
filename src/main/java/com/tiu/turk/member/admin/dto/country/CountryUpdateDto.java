package com.tiu.turk.member.admin.dto.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Generated;

public class CountryUpdateDto {
    @NotNull(message="ID must not be blank")
    private @NotNull(message="ID must not be blank") Long id;
    @NotBlank(message="Name must not be blank")
    @Size(max=512, message="Name length must not exceed 256 characters")
    private @NotBlank(message="Name must not be blank") @Size(max=512, message="Name length must not exceed 256 characters") String name;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }
}

