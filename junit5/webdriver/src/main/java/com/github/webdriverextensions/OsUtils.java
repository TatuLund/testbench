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
package com.github.webdriverextensions;


import org.openqa.selenium.Platform;

public class OsUtils {

  private OsUtils() { }

  public static boolean isWindows() {
    return Platform.getCurrent().is(Platform.WINDOWS);
  }

  public static boolean isWindows10() {
    return Platform.getCurrent().is(Platform.WIN10);
  }

  public static boolean isMac() {
    return Platform.getCurrent().is(Platform.MAC);
  }

  public static boolean isLinux() {
    return Platform.getCurrent().is(Platform.LINUX);
  }

  public static boolean isCurrentPlatform(String platform) {
    try {
      return Platform.getCurrent().is(Platform.valueOf(platform));
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static boolean is64Bit() {
    return System.getProperty("sun.arch.data.model",
                              System.getProperty("com.ibm.vm.bitmode")
    ).equals("64");
  }
}
