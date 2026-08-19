package com.tiu.turk.batch.io.leadershipmember.writer;

import com.tiu.turk.batch.io.common.dto.TranslationResult;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.leadership.common.entity.LeadershipMemberEntity;
import com.tiu.turk.leadership.common.entity.LeadershipMemberTranslationEntity;
import com.tiu.turk.leadership.common.repository.LeadershipMemberRepository;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeadershipMemberTranslationWriter
implements ItemWriter<TranslationResult> {
    private final LeadershipMemberRepository leadershipMemberRepository;
    private final TranslationTaskRepository taskRepository;

    @Override
    public void write(Chunk<? extends TranslationResult> chunk) throws Exception {
        for (TranslationResult item : chunk) {
            if (item.module() != AppModule.LEADERSHIP_MEMBER) {
                continue;
            }
            LeadershipMemberEntity m = this.leadershipMemberRepository.findById(item.entityId())
                    .orElseThrow(() -> new IllegalArgumentException("Leadership member not found: " + item.entityId()));
            LeadershipMemberTranslationEntity translation = Optional.ofNullable(m.getTranslations().get(item.targetLocale()))
                    .orElseGet(LeadershipMemberTranslationEntity::new);
            if (translation.getId() == null) {
                translation.setLeadershipMember(m);
                translation.setLocale(item.targetLocale());
                translation.setCreatedAt(LocalDateTime.now());
            }
            Map<String, String> fields = item.translatedFields();
            if (fields != null && !fields.isEmpty()) {
                if (fields.containsKey("position")) translation.setPosition(fields.get("position"));
                if (fields.containsKey("organization")) translation.setOrganization(fields.get("organization"));
                if (fields.containsKey("bio")) translation.setBio(fields.get("bio"));
            }
            translation.setUpdatedAt(LocalDateTime.now());
            m.getTranslations().put(item.targetLocale(), translation);
            this.leadershipMemberRepository.save(m);
            this.taskRepository.markDone(AppModule.LEADERSHIP_MEMBER, item.entityId(), item.targetLocale(), item.sourceHash());
        }
    }
}
