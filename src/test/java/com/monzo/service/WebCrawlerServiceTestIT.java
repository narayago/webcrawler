package com.monzo.service;

import com.monzo.model.WebPage;
import com.monzo.model.WebCrawlRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebCrawlerServiceTestIT {

    @Autowired
    private WebCrawlerService webCrawlerService;

    @Test
    public void test_generateSitemap() {

        webCrawlerService.generateSitemap(
                WebCrawlRequest.Builder.aWebPageCrawlRequest()
                        .withPage(new WebPage("https://monzo.com"))
                        .withExclusionList(new HashSet<>(Arrays.asList("/blog","/jobs","/help")))
                        .build()
        );

    }
}
