package com.tiu.turk.search;

/** One row on the public search results page. */
public record SearchResult(String type, String title, String snippet, String url) {
}
