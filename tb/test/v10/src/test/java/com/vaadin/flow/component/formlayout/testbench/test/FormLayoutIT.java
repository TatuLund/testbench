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
package com.vaadin.flow.component.formlayout.testbench.test;


import static com.vaadin.flow.component.formlayout.testbench.test.FormLayoutView.NAV;

import org.junit.jupiter.api.Assertions;
import com.vaadin.flow.component.common.testbench.test.AbstractIT;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.addons.junit5.extensions.unittest.VaadinWebUnitTest;
import junit.com.vaadin.testbench.tests.testUI.GenericTestPageObject;

@VaadinWebUnitTest
public class FormLayoutIT extends AbstractIT {

  @VaadinWebUnitTest
  public void findInside(GenericTestPageObject po) throws Exception {
    po.loadPage(NAV);

    final FormLayoutElement formLayout = po.formLayout().id(FormLayoutView.DEFAULT);

    Assertions.assertEquals(3 , po.$(TextFieldElement.class).all().size());
    Assertions.assertEquals(2 ,
                            formLayout.$(TextFieldElement.class).all().size());
  }

}
