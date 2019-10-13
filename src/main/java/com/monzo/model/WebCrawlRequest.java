package com.monzo.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Page crawl request model.
 *
 * @author Gowrisankar Narayana
 */
public class WebCrawlRequest implements Serializable {

    /*
     * Web page to crawl
     */
    private WebPage page;

    /*
     * Exclusion list - e.g., /blog /help etc.,
     */
    private Set<String> exclusionList= new HashSet<>();

    public WebPage getPage() {
        return page;
    }

    public void setPage(WebPage page) {
        this.page = page;
    }

    public Set<String> getExclusionList() {
        return exclusionList;
    }

    public void setExclusionList(Set<String> exclusionList) {
        this.exclusionList = exclusionList;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WebCrawlRequest.class.getSimpleName() + "[", "]")
                .add("page=" + page.getUrl())
                .toString();
    }

    public static final class Builder {
        /*
         * Web page to crawl
         */
        private WebPage page;

        /*
         * Exclusion list - e.g., /blog /help etc.,
         */
        private Set<String> exclusionList= new HashSet<>();


        private Builder() {
        }

        public static Builder aWebPageCrawlRequest() {
            return new Builder();
        }

        public Builder withPage(WebPage page) {
            this.page = page;
            return this;
        }

        public Builder withExclusionList(Set<String> exclusionList) {
            this.exclusionList = exclusionList;
            return this;
        }

        public WebCrawlRequest build() {
            WebCrawlRequest webPageCrawlRequest = new WebCrawlRequest();
            webPageCrawlRequest.setPage(page);
            webPageCrawlRequest.exclusionList = this.exclusionList;
            return webPageCrawlRequest;
        }
    }
}
