/*
 * Copyright 2000-2018 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.combobox.testbench.test;

import static com.vaadin.flow.component.combobox.testbench.test.ComboBoxView.*;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.common.testbench.test.AbstractIT;
import com.vaadin.testbench.addons.junit5.extensions.container.ContainerInfo;
import com.vaadin.testbench.addons.junit5.extensions.unittest.VaadinWebUnitTest;
import junit.com.vaadin.testbench.tests.testUI.GenericTestPageObject;

@VaadinWebUnitTest
public class ComboBoxIT extends AbstractIT {

  public static class PO extends GenericTestPageObject {

    public PO(WebDriver webdriver , ContainerInfo containerInfo) {
      super(webdriver , containerInfo);
    }

    @Override
    public void loadPage() {
      loadPage(NAV);
    }
  }



  @VaadinWebUnitTest
  public void getLabel(PO po) throws Exception {
    po.loadPage();

    final  ComboBoxElement comboBoxWithText = po.comboBox().id(TEXT);
    final ComboBoxElement comboBoxWithNoText = po.comboBox().id(NOTEXT);
    final ComboBoxElement comboBoxWithBeans = po.comboBox().id(BEANS);
    final ComboBoxElement comboBoxWithLazy = po.comboBox().id(LAZY);

    Assertions.assertEquals("" , comboBoxWithNoText.getLabel());
    Assertions.assertEquals("Text" , comboBoxWithText.getLabel());
    Assertions.assertEquals("Lazy" , comboBoxWithLazy.getLabel());
    Assertions.assertEquals("Persons" , comboBoxWithBeans.getLabel());
  }

  @VaadinWebUnitTest
  public void selectByText(PO po) throws Exception {
    po.loadPage();

    final  ComboBoxElement comboBoxWithText = po.comboBox().id(TEXT);
    final ComboBoxElement comboBoxWithNoText = po.comboBox().id(NOTEXT);
    final ComboBoxElement comboBoxWithBeans = po.comboBox().id(BEANS);
    final ComboBoxElement comboBoxWithLazy = po.comboBox().id(LAZY);

    Assertions.assertEquals("" , comboBoxWithNoText.getSelectedText());
    Assertions.assertEquals("" , comboBoxWithText.getSelectedText());
    Assertions.assertEquals("" , comboBoxWithLazy.getSelectedText());
    Assertions.assertEquals("" , comboBoxWithBeans.getSelectedText());

    comboBoxWithNoText.selectByText("Item 1");
    Assertions.assertEquals("1. ComboBox 'null' value is now Item 1" ,
                            getLogRow(po,0));
    Assertions.assertEquals("Item 1" , comboBoxWithNoText.getSelectedText());

    comboBoxWithText.selectByText("Item 18");
    Assertions.assertEquals("2. ComboBox 'Text' value is now Item 18" ,
                            getLogRow(po,0));
    Assertions.assertEquals("Item 18" , comboBoxWithText.getSelectedText());

    comboBoxWithLazy.selectByText("Item 400");
    Assertions.assertEquals("3. ComboBox 'Lazy' value is now Item 400" ,
                            getLogRow(po,0));
    Assertions.assertEquals("Item 400" , comboBoxWithLazy.getSelectedText());

    comboBoxWithBeans.selectByText("Doe, John");
    Assertions.assertEquals(
        "4. ComboBox 'Persons' value is now Person [firstName=John, lastName=Doe, age=20]" ,
        getLogRow(po,0));
    Assertions.assertEquals("Doe, John" , comboBoxWithBeans.getSelectedText());
  }

  @VaadinWebUnitTest
  public void getSelectedText(PO po) {
    po.loadPage();

    final ComboBoxElement comboBoxWithTextWithPreSelectedValue = po.comboBox().id(TEXT_WITH_PRE_SLELECTED_VALUE);
    final ComboBoxElement comboBoxWithNoTextWithPreSelectedValue = po.comboBox().id(NOTEXT_WITH_PRE_SLELECTED_VALUE);
    final ComboBoxElement comboBoxLazyWithPreSelectedValue = po.comboBox().id(LAZY_WITH_PRE_SLELECTED_VALUE);
    final ComboBoxElement comboBoxWithBeansWithPreSelectedValue = po.comboBox().id(BEANS_WITH_PRE_SLELECTED_VALUE);

    Assertions.assertEquals(PRE_SELECTED_VALUE_FOR_COMBOBOX_WITHOUT_TEXT ,
                            comboBoxWithNoTextWithPreSelectedValue.getSelectedText());
    Assertions.assertEquals(PRE_SELECTED_VALUE_FOR_COMBOBOX_WITH_TEXT ,
                            comboBoxWithTextWithPreSelectedValue.getSelectedText());
    Assertions.assertEquals(PRE_SELECTED_VALUE_FOR_COMBOBOX_LAZY ,
                            comboBoxLazyWithPreSelectedValue.getSelectedText());
    Assertions.assertEquals(PRE_SELECTED_PERSON_FOR_COMBOBOX_WITH_BEANS.getLastName() + ", " +
                            PRE_SELECTED_PERSON_FOR_COMBOBOX_WITH_BEANS.getFirstName() ,
                            comboBoxWithBeansWithPreSelectedValue.getSelectedText());
  }

  @VaadinWebUnitTest
  public void openCloseIsOpenPopup(PO po) throws Exception {
    po.loadPage();

    final  ComboBoxElement comboBoxWithText = po.comboBox().id(TEXT);
    comboBoxWithText.openPopup();
    Assertions.assertTrue(comboBoxWithText.isPopupOpen());
    comboBoxWithText.closePopup();
    Assertions.assertFalse(comboBoxWithText.isPopupOpen());
  }

  @VaadinWebUnitTest
  public void getPopupSuggestions(PO po) throws Exception {
    po.loadPage();

    final  ComboBoxElement comboBoxWithText = po.comboBox().id(TEXT);
    final ComboBoxElement comboBoxWithNoText = po.comboBox().id(NOTEXT);
    final ComboBoxElement comboBoxWithBeans = po.comboBox().id(BEANS);

    Assertions.assertArrayEquals(
        IntStream.range(0 , 20).mapToObj(i -> "Item " + i).toArray() ,
        comboBoxWithNoText.getOptions().toArray());
    Assertions.assertArrayEquals(
        IntStream.range(0 , 20).mapToObj(i -> "Item " + i).toArray() ,
        comboBoxWithText.getOptions().toArray());
    Assertions.assertArrayEquals(
        new String[]{"Doe, John" , "Johnson, Jeff" , "Meyer, Diana"} ,
        comboBoxWithBeans.getOptions().toArray());
  }

  @VaadinWebUnitTest
  public void filter(PO po) {
    po.loadPage();

    final  ComboBoxElement comboBoxWithText = po.comboBox().id(TEXT);
    final ComboBoxElement comboBoxWithNoText = po.comboBox().id(NOTEXT);
    final ComboBoxElement comboBoxWithBeans = po.comboBox().id(BEANS);
    Assertions.assertEquals("" , comboBoxWithNoText.getFilter());
    Assertions.assertEquals("" , comboBoxWithText.getFilter());
    Assertions.assertEquals("" , comboBoxWithBeans.getFilter());

    comboBoxWithNoText.setFilter("2");
    Assertions.assertArrayEquals(new String[]{"Item 2" , "Item 12"} ,
                                 comboBoxWithNoText.getOptions().toArray());
    comboBoxWithText.setFilter("2");
    Assertions.assertArrayEquals(new String[]{"Item 2" , "Item 12"} ,
                                 comboBoxWithText.getOptions().toArray());
    comboBoxWithBeans.setFilter("Jo");
    Assertions.assertArrayEquals(new String[]{"Doe, John" , "Johnson, Jeff"} ,
                                 comboBoxWithBeans.getOptions().toArray());

  }

}
