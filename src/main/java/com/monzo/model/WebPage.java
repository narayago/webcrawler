package com.monzo.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.*;

/**
 * Web page data structure.
 *
 * @author Gowrisankar Narayana
 */
public class WebPage implements Serializable {

    private static final long serialVersionUID = 2880109446775667437L;

    /*
     * Page url
     */
    private String url;

    /*
     * Child web pages
     */
    private Set<WebPage> children = Collections.synchronizedSet(new HashSet<>());

    /*
     * Parent web page
     */
    private WebPage parent = null;

    public WebPage(final String url) {
        this.url = clean(url);
    }

    public WebPage(final String url,final WebPage parent) {
        this.url = clean(url);
        this.parent=parent;
    }

    public void addChild(final WebPage child) {
        child.setParent(this);
        this.children.add(child);
    }

    public Set<WebPage> getChildren() {
        return children;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private void setParent(WebPage parent) {
        this.parent = parent;
    }

    public WebPage getParent() {
        return parent;
    }

    private String clean(String url){

        if (StringUtils.isNotBlank(url))
        {
            if(url.endsWith("/")){
                url=url.substring(0, url.length() - 1);
            }
            else if(url.endsWith("/#")){
                url=url.substring(0, url.length() - 2);
            }
        }

        return url;

    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WebPage.class.getSimpleName() + "[", "]")
                .add("url='" + getUrl() + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return this.getUrl().equalsIgnoreCase(((WebPage) o).getUrl());

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getUrl())
                .toHashCode();
    }

    public static final class Builder {
        /*
         * Page url
         */
        private String url;
        /*
         * Child web pages
         */
        private Set<WebPage> children = Collections.synchronizedSet(new HashSet<>());
        /*
         * Parent web page
         */
        private WebPage parent = null;

        private Builder() {
        }

        public static Builder aWebPage() {
            return new Builder();
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withChildren(Set<WebPage> children) {
            this.children = children;
            return this;
        }

        public Builder withParent(WebPage parent) {
            this.parent = parent;
            return this;
        }

        public WebPage build() {
            WebPage webPage = new WebPage(url, parent);
            webPage.children = this.children;
            return webPage;
        }
    }
}
