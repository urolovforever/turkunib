package com.tiu.turk.member.web.mapper;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.image.mapper.ImageMapper;
import com.tiu.turk.member.common.entity.CountryEntity;
import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.member.web.dto.CountryWebDto;
import com.tiu.turk.member.web.dto.MemberWebDto;
import com.tiu.turk.member.web.mapper.MemberWebMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberWebMapperImpl
implements MemberWebMapper {
    @Autowired
    private ImageMapper imageMapper;

    public MemberWebDto toDto(MemberEntity entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        String name = null;
        ImageDto image = null;
        String url = null;
        Integer founded = null;
        Integer studentsNumber = null;
        Integer facultiesNumber = null;
        String address = null;
        String additionalInfo = null;
        CountryWebDto country = null;
        BigDecimal latitude = null;
        BigDecimal longitude = null;
        id = entity.getId();
        name = entity.getName();
        image = this.imageMapper.toDto(entity.getImage());
        url = entity.getUrl();
        founded = entity.getFounded();
        studentsNumber = entity.getStudentsNumber();
        facultiesNumber = entity.getFacultiesNumber();
        address = entity.getAddress();
        additionalInfo = entity.getAdditionalInfo();
        country = this.countryEntityToCountryWebDto(entity.getCountry());
        latitude = entity.getLatitude();
        longitude = entity.getLongitude();
        MemberWebDto memberWebDto = new MemberWebDto(id, name, image, url, founded, studentsNumber, facultiesNumber, address, additionalInfo, country, latitude, longitude);
        return memberWebDto;
    }

    public List<MemberWebDto> toDto(List<MemberEntity> entities) {
        if (entities == null) {
            return null;
        }
        ArrayList<MemberWebDto> list = new ArrayList<MemberWebDto>(entities.size());
        for (MemberEntity memberEntity : entities) {
            list.add(this.toDto(memberEntity));
        }
        return list;
    }

    public Map<Long, List<MemberWebDto>> toDtoMap(Map<Long, List<MemberEntity>> entityMap) {
        if (entityMap == null) {
            return null;
        }
        LinkedHashMap<Long, List<MemberWebDto>> map = LinkedHashMap.newLinkedHashMap(entityMap.size());
        for (Map.Entry<Long, List<MemberEntity>> entry : entityMap.entrySet()) {
            Long key = entry.getKey();
            List value = this.toDto(entry.getValue());
            map.put(key, value);
        }
        return map;
    }

    protected CountryWebDto countryEntityToCountryWebDto(CountryEntity countryEntity) {
        if (countryEntity == null) {
            return null;
        }
        Long id = null;
        String name = null;
        ImageDto image = null;
        id = countryEntity.getId();
        name = countryEntity.getName();
        image = this.imageMapper.toDto(countryEntity.getImage());
        CountryWebDto countryWebDto = new CountryWebDto(id, name, image);
        return countryWebDto;
    }
}

