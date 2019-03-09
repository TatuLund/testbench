/**
 * Copyright © 2017 Sven Ruppert (sven.ruppert@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vaadin.testbench.addons.junit5.extensions.container;

import static com.vaadin.testbench.addons.junit5.extensions.container.ExtensionContextFunctions.containerInfo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import com.vaadin.dependencies.core.logger.HasLogger;

public class ContainerInfoExtension implements BeforeEachCallback, HasLogger {

  private ContainerInfo containerInfo;

  public int port() {
    return containerInfo.port();
  }

  public String host() {
    return containerInfo.host();
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    logger().info("ContainerInfoExtension - beforeEach ");
    containerInfo = containerInfo().apply(extensionContext);
    logger().info("ContainerInfoExtension - " + containerInfo);
  }
}
