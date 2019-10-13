package com.monzo.model;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class WebPageTest {

    @Test
    public void addChild() {
        WebPage webPage = new WebPage("https://monzo.com");
        WebPage childPage = new WebPage("https://monzo.com/features/travel");
        webPage.addChild(childPage);
        assertTrue("Child page does not exist", webPage.getChildren().contains(childPage));
    }

    @Test
    public void getChildren() {
        WebPage webPage = new WebPage("https://monzo.com");
        WebPage childPage = new WebPage("https://monzo.com/features/travel");
        webPage.addChild(childPage);
        assertEquals("Child pages does not exist", 1, webPage.getChildren().size());
    }

    @Test
    public void getUrl() {
        WebPage webPage = new WebPage("https://monzo.com");
        assertTrue("Url is not found", webPage.getUrl().equalsIgnoreCase("https://monzo.com"));
    }

    @Test
    public void getCleanUrl2() {
        WebPage webPage = new WebPage("https://monzo.com/");
        assertTrue("Url is not cleaned", webPage.getUrl().equalsIgnoreCase("https://monzo.com"));
    }

    @Test
    public void getCleanUrl3() {
        WebPage webPage = new WebPage("https://monzo.com/#");
        assertTrue("Url is not cleaned", webPage.getUrl().equalsIgnoreCase("https://monzo.com"));
    }


    @Test
    public void setUrl() {
        WebPage webPage = new WebPage("https://monzo.com/#");
        webPage.setUrl("https://monzo.com/contact");
        assertTrue("Url is not updated", webPage.getUrl().equalsIgnoreCase("https://monzo.com/contact"));
    }

    @Test
    public void getParent() {
        WebPage parentPage = new WebPage("https://monzo.com");
        WebPage childPage = new WebPage("https://monzo.com/features/travel",parentPage);
        parentPage.addChild(childPage);
        assertSame("Parent is not same", childPage.getParent(), parentPage);
    }

    @Test
    public void testBuilder(){
        WebPage parentPage = new WebPage("https://monzo.com");
        WebPage childPage = new WebPage("https://monzo.com/features/travel",parentPage);
        WebPage webPage=WebPage.Builder.aWebPage()
                .withChildren(Collections.singleton(childPage))
                .withParent(parentPage)
                .withUrl("https://monzo.com/features")
                .build();
        assertTrue("Parent and child not found",webPage.getParent()==parentPage && webPage.getChildren().contains(childPage));
    }

    @Test
    public void toStringTest() {
        WebPage webPage = new WebPage("https://monzo.com/#");
        assertNotNull("To string is not implemented", webPage.toString());
    }


}