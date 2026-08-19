package com.tiu.turk.translation.repository;

import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TranslationTaskRepository
extends JpaRepository<TranslationTaskEntity, Long> {
    @Query(value="SELECT t FROM TranslationTaskEntity t\nWHERE t.status='PENDING' AND t.module=:module AND t.targetLocale IN :locales\nORDER BY t.id\n")
    public Page<TranslationTaskEntity> findPendingForModule(@Param(value="module") AppModule var1, @Param(value="locales") List<TranslationLocale> var2, Pageable var3);

    @Modifying
    @Query(value="UPDATE TranslationTaskEntity t SET\nt.status = 'DONE',\nt.errorMessage = null,\nt.updatedAt = CURRENT_TIMESTAMP\nWHERE t.module=:module AND t.entityId=:eid AND t.targetLocale=:loc AND t.sourceHash=:hash\n")
    public void markDone(@Param(value="module") AppModule var1, @Param(value="eid") Long var2, @Param(value="loc") TranslationLocale var3, @Param(value="hash") String var4);

    @Transactional
    @Modifying(clearAutomatically=true)
    @Query(value="INSERT INTO translation_tasks (module_name, entity_id, source_hash, source_locale, target_locale, status, attempt, started_at, updated_at)\nVALUES (:module, :eid, :hash, :src, :tgt, 'PENDING', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)\nON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = VALUES(updated_at)\n", nativeQuery=true)
    public void upsertPending(@Param(value="module") String var1, @Param(value="eid") Long var2, @Param(value="hash") String var3, @Param(value="src") String var4, @Param(value="tgt") String var5);

    @Query(value="SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END\nFROM TranslationTaskEntity t\nWHERE t.entityId = :eid AND t.module = :module\nAND t.targetLocale = :locale AND t.sourceHash = :hash\n")
    public boolean isTaskExists(@Param(value="eid") Long var1, @Param(value="module") AppModule var2, @Param(value="locale") TranslationLocale var3, @Param(value="hash") String var4);

    public Page<TranslationTaskEntity> findAllByModule(AppModule var1, Pageable var2);

    public Page<TranslationTaskEntity> findAllByModuleAndEntityId(AppModule var1, Long var2, Pageable var3);
}

