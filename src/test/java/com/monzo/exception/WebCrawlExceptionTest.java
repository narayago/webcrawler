package com.monzo.exception;

import org.junit.Assert;
import org.junit.Test;

public class WebCrawlExceptionTest {

    @Test
    public void test_exception(){

        WebCrawlException exception=new WebCrawlException("test message");

        Assert.assertEquals("test message",exception.getMessage());

    }


}
