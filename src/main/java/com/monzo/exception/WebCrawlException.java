package com.monzo.exception;

import java.io.Serializable;

/**
 * Exception that gets thrown when page crawling fails.
 *
 * @author Gowrisankar Narayana
 */
public class WebCrawlException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 3274015313229431408L;

    /**
     * Constructor for WebCrawlException.
     * @param message the detail message
     */
    public WebCrawlException(String message) {
        super(message);
    }

}