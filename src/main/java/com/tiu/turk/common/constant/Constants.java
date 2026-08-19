package com.tiu.turk.common.constant;

import org.jsoup.safety.Safelist;

public final class Constants {
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String NEWS_CATEGORY_MODULE_NAME = "news_category";
    public static final String NEWS_MODULE_NAME = "news";
    public static final String MEDIA_MODULE_NAME = "media";
    public static final String PHOTO_GALLERY_MODULE_NAME = "photo_gallery";
    public static final String MEMBER_COUNTRY_MODULE_NAME = "member_country";
    public static final String MEMBER_MODULE_NAME = "member";
    public static final String BANNER_MODULE_NAME = "banner";
    public static final String TRANSLATION_INITIALIZED_MESSAGE = "Translation tasks initialized.";
    public static final String JSOUP_IFRAME_TAG = "iframe";
    public static final Safelist JSOUP_SAFELIST = Safelist.relaxed().addTags(new String[]{"iframe"}).addAttributes("iframe", new String[]{"src", "width", "height", "allowfullscreen", "frameborder"}).addProtocols("iframe", "src", new String[]{"https", "http"}).addAttributes(":all", new String[]{"style"});

    private Constants() {
    }
}

