package com.tiu.turk.member.admin.service;

import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.service.ImageService;
import com.tiu.turk.member.common.entity.CountryEntity;
import com.tiu.turk.member.common.repository.CountryRepository;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CountryService {
    private final CountryRepository countryRepository;
    private final ImageService imageService;

    public Page<CountryEntity> getAllCountries(int page, int size) {
        return this.countryRepository.findAll((Pageable)PageRequest.of((int)page, (int)size));
    }

    public List<CountryEntity> getAllCountries() {
        return this.countryRepository.findAll();
    }

    public CountryEntity getCountryById(Long id) {
        return (CountryEntity)this.countryRepository.findById(id).orElseThrow(() -> new RuntimeException("Country not found with id: " + id));
    }

    public CountryEntity createCountry(MultipartFile image, CountryEntity country, Long authorId) throws IOException {
        if (image != null && !image.isEmpty()) {
            String fileName = image.getOriginalFilename() != null ? image.getOriginalFilename() : "image";
            ImageEntity storedImage = this.imageService.store(image, fileName, "member_country", authorId);
            country.setImage(storedImage);
        }
        country.setAuthor(new UserEntity(authorId));
        country.setCreatedAt(LocalDateTime.now());
        country.setUpdatedAt(LocalDateTime.now());
        return (CountryEntity)this.countryRepository.save(country);
    }

    public CountryEntity updateCountry(Long id, MultipartFile image, CountryEntity country, Long authorId) throws IOException {
        CountryEntity existingCountry = this.getCountryById(id);
        Long imageIdToDelete = null;
        if (image != null && !image.isEmpty()) {
            if (existingCountry.getImage() != null) {
                imageIdToDelete = existingCountry.getImage().getId();
            }
            String fileName = image.getOriginalFilename() != null ? image.getOriginalFilename() : "image";
            ImageEntity storedImage = this.imageService.store(image, fileName, "member_country", authorId);
            existingCountry.setImage(storedImage);
        }
        existingCountry.setName(country.getName());
        existingCountry.setAuthor(new UserEntity(authorId));
        existingCountry.setUpdatedAt(LocalDateTime.now());
        existingCountry = (CountryEntity)this.countryRepository.save(existingCountry);
        if (imageIdToDelete != null) {
            this.imageService.deleteImage(imageIdToDelete);
        }
        return existingCountry;
    }

    public void deleteCountry(Long id) throws IOException {
        CountryEntity country = this.getCountryById(id);
        Long imageIdToDelete = country.getImage() != null ? country.getImage().getId() : null;
        this.countryRepository.delete(country);
        if (imageIdToDelete != null) {
            this.imageService.deleteImage(imageIdToDelete);
        }
    }

    @Generated
    public CountryService(CountryRepository countryRepository, ImageService imageService) {
        this.countryRepository = countryRepository;
        this.imageService = imageService;
    }
}

