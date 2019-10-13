package com.monzo.task;

import com.monzo.model.WebPage;
import com.monzo.model.WebCrawlRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;

/**
 * Web page crawler manager.
 *
 * @author Gowrisankar Narayana
 */
public class WebCrawlerManager implements Serializable {

    private static final long serialVersionUID = -9209623430540097730L;

    private static final Logger LOG = LoggerFactory.getLogger(WebCrawlerManager.class);

    private final Collection<WebPage> visitedPages= Collections.synchronizedSet(new HashSet<>());

    private WebCrawlRequest crawlRequest;

    private ForkJoinPool crawlerPool;

    /**
     * Takes root web page and recursively crawls sub pages until entire website is crawled.
     *
     * @param crawlRequest Crawl request
     */
    public WebCrawlerManager(final WebCrawlRequest crawlRequest) {
        this.crawlRequest = crawlRequest;
        crawlerPool = new ForkJoinPool(8);
    }

    /**
     * Starts page crawling
     */
    public void crawl() {
        LOG.debug("Crawler manager start");
        crawlerPool.invoke(new WebCrawlerAction(this.crawlRequest, this));
    }

    /**
     * To verify if page has already been visited.
     *
     * @param page Web Page
     * @return return if page is visited
     */
    boolean isVisited(WebPage page) {
        return visitedPages.contains(page);
    }

    /**
     * Once a page is visited successfully add to visit list.
     *
     * @param page Web Page
     */
    synchronized void addVisited(final WebPage page) {
        visitedPages.add(page);
    }

}
