package com.tiu.turk.search;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.event.common.entity.EventEntity;
import com.tiu.turk.event.common.entity.EventTranslationEntity;
import com.tiu.turk.event.common.repository.EventRepository;
import com.tiu.turk.faq.common.repository.FaqRepository;
import com.tiu.turk.faq.web.dto.FaqView;
import com.tiu.turk.news.common.entity.NewsEntity;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;
import com.tiu.turk.news.common.repository.NewsRepository;
import com.tiu.turk.publication.common.repository.PublicationRepository;
import com.tiu.turk.publication.web.dto.PublicationView;
import com.tiu.turk.research.common.repository.ResearchProjectRepository;
import com.tiu.turk.research.web.dto.ResearchProjectView;
import com.tiu.turk.scholarship.common.repository.ScholarshipRepository;
import com.tiu.turk.scholarship.web.dto.ScholarshipView;
import com.tiu.turk.webinar.common.entity.WebinarEntity;
import com.tiu.turk.webinar.common.entity.WebinarTranslationEntity;
import com.tiu.turk.webinar.common.repository.WebinarRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {
    private static final int PER_TYPE = 8;

    private final NewsRepository newsRepository;
    private final EventRepository eventRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final ResearchProjectRepository researchProjectRepository;
    private final WebinarRepository webinarRepository;
    private final PublicationRepository publicationRepository;
    private final FaqRepository faqRepository;
    private final MessageSource messageSource;

    @Transactional(readOnly = true)
    public List<SearchResult> search(String lang, String rawQuery) {
        List<SearchResult> results = new ArrayList<>();
        if (rawQuery == null || rawQuery.trim().length() < 2) {
            return results;
        }
        TranslationLocale locale = TranslationLocale.toLocaleOrDefault(lang);
        Locale msgLocale = Locale.forLanguageTag(locale.getCode());
        String q = "%" + rawQuery.trim().toLowerCase() + "%";

        // News (old module: all text in i18n)
        for (NewsEntity n : limit(this.newsRepository.search(q))) {
            NewsTranslationEntity t = localeOrEn(n.getTranslations().get(locale), n.getTranslations().get(TranslationLocale.EN));
            if (t == null) continue;
            results.add(new SearchResult(type("news", msgLocale), t.getTitle(), snippet(t.getDescription(), t.getContent()),
                    url(lang, "news", n.getId())));
        }
        // Events (old module)
        for (EventEntity e : limit(this.eventRepository.search(q))) {
            EventTranslationEntity t = localeOrEn(e.getTranslations().get(locale), e.getTranslations().get(TranslationLocale.EN));
            if (t == null) continue;
            results.add(new SearchResult(type("event", msgLocale), t.getTitle(), snippet(t.getDescription(), null),
                    url(lang, "events", e.getId())));
        }
        // Scholarships (new module: base EN + i18n)
        this.scholarshipRepository.search(q).stream().limit(PER_TYPE).forEach(s -> {
            ScholarshipView v = new ScholarshipView(s, locale);
            results.add(new SearchResult(type("scholarship", msgLocale), v.getTitle(), snippet(v.getDescription(), v.getContent()),
                    url(lang, "scholarships", s.getId())));
        });
        // Research projects
        this.researchProjectRepository.search(q).stream().limit(PER_TYPE).forEach(p -> {
            ResearchProjectView v = new ResearchProjectView(p, locale);
            results.add(new SearchResult(type("research", msgLocale), v.getTitle(), snippet(v.getDescription(), v.getContent()),
                    url(lang, "research", p.getId())));
        });
        // Webinars (new module)
        for (WebinarEntity w : limit(this.webinarRepository.search(q))) {
            WebinarTranslationEntity wt = w.getTranslations().get(locale);
            String title = (locale != TranslationLocale.EN && wt != null && notBlank(wt.getTitle())) ? wt.getTitle() : w.getTitle();
            String desc = (locale != TranslationLocale.EN && wt != null && notBlank(wt.getDescription())) ? wt.getDescription() : w.getDescription();
            String content = (locale != TranslationLocale.EN && wt != null && notBlank(wt.getContent())) ? wt.getContent() : w.getContent();
            results.add(new SearchResult(type("webinar", msgLocale), title, snippet(desc, content), url(lang, "webinars", w.getId())));
        }
        // Publications (new module; no detail page -> list)
        this.publicationRepository.search(q).stream().limit(PER_TYPE).forEach(p -> {
            PublicationView v = new PublicationView(p, locale);
            results.add(new SearchResult(type("publication", msgLocale), v.getTitle(), snippet(v.getDescription(), null),
                    "/" + lang + "/publications/"));
        });
        // FAQ (new module; single page)
        this.faqRepository.search(q).stream().limit(PER_TYPE).forEach(f -> {
            FaqView v = new FaqView(f, locale);
            results.add(new SearchResult(type("faq", msgLocale), v.getQuestion(), snippet(v.getAnswer(), null),
                    "/" + lang + "/faq/"));
        });
        return results;
    }

    private <T> List<T> limit(List<T> list) {
        return list.size() > PER_TYPE ? list.subList(0, PER_TYPE) : list;
    }

    private static <T> T localeOrEn(T forLocale, T en) {
        return forLocale != null ? forLocale : en;
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private String type(String key, Locale locale) {
        return this.messageSource.getMessage("search.type." + key, null, key, locale);
    }

    private static String url(String lang, String segment, Long id) {
        return "/" + lang + "/" + segment + "/" + id;
    }

    /** Plain-text snippet: prefer description, fall back to content; strip tags; truncate. */
    private static String snippet(String primary, String fallback) {
        String text = notBlank(primary) ? primary : (notBlank(fallback) ? fallback : "");
        text = text.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
        return text.length() > 180 ? text.substring(0, 180) + "…" : text;
    }
}
