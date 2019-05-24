package com.vaadin.testbench.tests.ui.demo;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.testbench.addons.junit5.pageobject.VaadinPageObject;

import static com.vaadin.testbench.tests.ui.demo.MainView.BTN_CLICK_ME;
import static com.vaadin.testbench.tests.ui.demo.MainView.LB_CLICK_COUNT;
import static java.lang.Integer.valueOf;

public class VaadinAppPageObject extends VaadinPageObject {

    public ButtonElement btnClickMe() {
        return $(ButtonElement.class).id(BTN_CLICK_ME);
    }

    public SpanElement lbClickCount() {
        $(SpanElement.class).waitForFirst();
        return $(SpanElement.class).id(LB_CLICK_COUNT);
    }

    public void click() {
        btnClickMe().click();
    }

    public String clickCountAsString() {
        return lbClickCount().getText();
    }

    public int clickCount() {
        return valueOf(clickCountAsString());
    }
}