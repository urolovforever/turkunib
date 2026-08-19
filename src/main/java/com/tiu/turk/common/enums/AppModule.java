package com.tiu.turk.common.enums;

public enum AppModule {
    NEWS("News"),
    NEWS_CATEGORY("News Category"),
    EVENT("Event"),
    BANNER("Banner"),
    PHOTO_GALLERY("Photo Gallery"),
    MEDIA("Media"),
    MEMBER_COUNTRY("Member's Country"),
    MEMBER("Member"),
    STATIC_PAGE("Static Page"),
    SCHOLARSHIP("Scholarship"),
    RESEARCH_PROJECT("Research Project"),
    WEBINAR("Webinar"),
    FAQ("FAQ"),
    PUBLICATION("Publication"),
    LEADERSHIP_MEMBER("Leadership Member"),
    KEY_DATE("Key Date"),
    INSTITUTIONAL_DOCUMENT("Institutional Document"),
    PRE_DEPARTURE_RESOURCE("Pre-departure Resource");

    private final String humanReadableName;

    private AppModule(String humanReadableName) {
        this.humanReadableName = humanReadableName;
    }

    public String getHumanReadableName() {
        return this.humanReadableName;
    }
}

