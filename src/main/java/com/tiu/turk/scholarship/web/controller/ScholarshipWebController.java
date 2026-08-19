package com.tiu.turk.scholarship.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The public scholarships pages were retired: the site only accepts Orhun Exchange
 * applications, and the yearly Orhun calls (still stored as scholarship records and
 * managed in the admin panel) are shown on the Orhun Exchange Program page instead.
 * Old links keep working via a redirect.
 */
@Controller
@RequestMapping(value={"/{lang:en|tr|kz|kg|uz|hg|az}/scholarships"})
public class ScholarshipWebController {

    @GetMapping(value={"", "/", "/{id}", "/{id}/{slug}"})
    public String redirectToOrhunPage(@PathVariable(value="lang") String lang) {
        return "redirect:/" + lang + "/orhun";
    }
}
