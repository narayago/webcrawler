package com.monzo.service;

import com.monzo.model.WebCrawlRequest;

public interface WebCrawlerService {

    void generateSitemap(final WebCrawlRequest pageCrawlRequest);

}
