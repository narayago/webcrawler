package com.monzo.task;

import com.monzo.model.WebPage;
import com.monzo.model.WebCrawlRequest;
import io.micrometer.core.annotation.Timed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * An implementation of {@link RecursiveAction} to crawl pages recursively.
 *
 * @author Gowrisankar Narayana
 */
public class WebCrawlerAction extends RecursiveAction {

    private static final Logger LOG = LoggerFactory.getLogger(WebCrawlerAction.class);

    private WebCrawlRequest crawlRequest;

    private WebCrawlerManager crawlerManager;

    /**
     * Creates a {@code WebCrawlerAction} with given crawl request and manager.
     *
     * @param crawlRequest Page Crawl Request
     * @param crawlerManager Crawler Manager
     */
    public WebCrawlerAction(final WebCrawlRequest crawlRequest, final WebCrawlerManager crawlerManager) {
        this.crawlRequest = crawlRequest;
        this.crawlerManager = crawlerManager;
    }

    /**
     * Page crawler routine
     */
    @Override
    @Timed(value = "monzo.task.crawler.action.compute",description = "Recursive compute action")
    public void compute() {

        if (crawlerManager.isVisited(crawlRequest.getPage())) {
            return;
        }

        try {

            LOG.debug("Crawling page:{}", crawlRequest.getPage().getUrl());

            List<WebCrawlerAction> actions = new ArrayList<>();

            URL url = new URL(crawlRequest.getPage().getUrl());

            Document doc = Jsoup.connect(url.toString())
                    .timeout(10000)
                    .validateTLSCertificates(false)
                    .get();

            Elements links = doc.select("a[href~=[^#]*");

            if (links.isEmpty()) {
                LOG.debug("No links found in url:{}", crawlRequest.getPage().getUrl());
                crawlerManager.addVisited(crawlRequest.getPage());
                return;
            }

            //Visit links found on this page
            for (Element link : links) {

                String currentUrl = link.attr("abs:href");

                //Do not crawl if page url is blank or referring to parent page.
                if (StringUtils.isEmpty(currentUrl) || !new URL(currentUrl).getHost().equalsIgnoreCase(url.getHost())) {
                    continue;
                }

                WebPage childPage=WebPage.Builder.aWebPage().withUrl(currentUrl).withParent(crawlRequest.getPage()).build();

                if (crawlerManager.isVisited(childPage)) {
                    return;
                }

                //Do not crawl if url is same as original url or found in the exclusion list
                if (childPage.getUrl().equalsIgnoreCase(crawlRequest.getPage().getUrl()) || isExcluded(childPage.getUrl(), crawlRequest)) {
                    continue;
                }

                //Build web page crawl request and add to actions.
                WebCrawlRequest childPageReq = WebCrawlRequest.Builder.aWebPageCrawlRequest()
                        .withPage(childPage)
                        .withExclusionList(crawlRequest.getExclusionList())
                        .build();

                crawlRequest.getPage().addChild(childPage);

                actions.add(new WebCrawlerAction(childPageReq, crawlerManager));

            }

            crawlerManager.addVisited(crawlRequest.getPage());

            //A call to ForkJoinPool's invokeAll method to crawl all sub-pages found on this page.
            invokeAll(actions);

        } catch (Exception e) {
            LOG.error("Crawler action error {}", e.getMessage());
        }

    }


    /**
     * Check of current url is in the exclusion list.
     *
     * @param currentUrl page url
     * @param request    Web page crawl request object
     * @return boolean to indicate exclusion
     */
    private boolean isExcluded(final String currentUrl, final WebCrawlRequest request) {
        boolean flag = false;
        for (String urlPattern : request.getExclusionList()) {
            flag = currentUrl.contains(urlPattern);
            if (flag) {
                break;
            }
        }
        return flag;
    }

}
