package com.tiu.turk.member.web.dto;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.member.web.dto.CountryWebDto;
import java.math.BigDecimal;

public record MemberWebDto(Long id, String name, ImageDto image, String url, Integer founded, Integer studentsNumber, Integer facultiesNumber, String address, String additionalInfo, CountryWebDto country, BigDecimal latitude, BigDecimal longitude) {
}
