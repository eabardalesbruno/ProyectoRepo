package com.proriberaapp.ribera.utils.emails;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EmailTemplateProcessor {
    public static String inlineStyles(String html) {
        Document document = Jsoup.parse(html);
        Elements styles = document.select("style");

        for (Element style : styles) {
            String[] rules = style.html().split("}");
            for (String rule : rules) {
                if (!rule.trim().isEmpty()) {
                    String[] parts = rule.split("\\{");
                    if (parts.length == 2) {
                        String selectors = parts[0].trim();
                        String declarations = parts[1].trim();
                        for (String selector : selectors.split(",")) {
                            Elements elements = document.select(selector.trim());
                            for (Element element : elements) {
                                String existingStyle = element.attr("style");
                                element.attr("style", existingStyle + " " + declarations);
                            }
                        }
                    }
                }
            }
        }

        styles.remove();
        return document.html();
    }
}
