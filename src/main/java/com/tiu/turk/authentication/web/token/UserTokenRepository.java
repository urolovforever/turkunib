package com.tiu.turk.authentication.web.token;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserTokenEntity, Long> {
    Optional<UserTokenEntity> findByTokenHashAndPurpose(String tokenHash, TokenPurpose purpose);
}
