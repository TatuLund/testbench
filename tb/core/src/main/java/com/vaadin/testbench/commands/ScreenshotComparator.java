package com.vaadin.testbench.commands;

import static com.vaadin.testbench.screenshot.ImageFileUtil.IMAGE_FILE_NAME_ENDING;
import static com.vaadin.testbench.screenshot.ImageFileUtil.createScreenshotDirectoriesIfNeeded;
import static com.vaadin.testbench.screenshot.ImageFileUtil.getErrorScreenshotFile;
import static com.vaadin.testbench.screenshot.ReferenceNameGenerator.PLATFORM_UNKNOWN;
import static com.vaadin.testbench.screenshot.ScreenshotProperties.getScreenshotComparisonTolerance;
import static com.vaadin.testbench.screenshot.ScreenshotProperties.getScreenshotReferenceDirectory;
import static com.vaadin.testbench.screenshot.ScreenshotProperties.getScreenshotRetriesMax;
import static com.vaadin.testbench.screenshot.ScreenshotProperties.getScreenshotRetryDelay;
import static org.openqa.selenium.OutputType.BYTES;
import static com.vaadin.dependencies.core.logger.Logger.getLogger;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.function.BiFunction;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import com.vaadin.dependencies.core.logger.HasLogger;
import com.vaadin.testbench.screenshot.ImageComparison;
import com.vaadin.testbench.screenshot.ReferenceNameGenerator.TestcaseInfo;

public class ScreenshotComparator implements HasLogger {

  private static Boolean supportsElementScreenshots = null;


  public static BiFunction<String, Capabilities, TestcaseInfo> convert() {
    return (referenceId , browserCapabilities) -> {
      Platform platform = browserCapabilities.getPlatform();
      String platformName;
      if (platform != null) {
        platformName = platform.toString().toLowerCase();
      } else {
        platformName = PLATFORM_UNKNOWN;
      }


      String versionString = browserCapabilities.getVersion();
      if (versionString.equals("")) {
        Object browserVersion = browserCapabilities.getCapability("browserVersion");
        if (browserVersion != null) {
          versionString = browserVersion.toString();
        }
      }
      String browserName = browserCapabilities.getBrowserName();


      return new TestcaseInfo(
          referenceId ,
          browserName ,
          platformName ,
          versionString
      );
    };
  }

  public static boolean compareScreen(String referenceId ,
                                      ImageComparison imageComparison ,
                                      TakesScreenshot takesScreenshot ,
                                      HasCapabilities driver)
      throws IOException {

    Capabilities capabilities = driver.getCapabilities();

    final TestcaseInfo info = convert().apply(referenceId , capabilities);


    for (int times = 0; times < getScreenshotRetriesMax(); times++) {
      boolean equal = imageComparison.imageEqualToReference(
          getScreenshot((TakesScreenshot) driver ,
                        takesScreenshot) ,
          info ,
          getScreenshotComparisonTolerance()
      );
      if (equal) {
        return true;
      }
      pause(getScreenshotRetryDelay());
    }
    return false;
  }

  public static boolean compareScreen(File reference ,
                                      ImageComparison imageComparison ,
                                      TakesScreenshot takesScreenshot)
      throws IOException {
    BufferedImage image = null;
    try {
      image = ImageIO.read(reference);
    } catch (IIOException e) {
      // Don't worry, an error screen shot will be generated that later
      // can be used as the reference
    }
    return compareScreen(image ,
                         reference.getName() ,
                         imageComparison ,
                         takesScreenshot);
  }

  public static boolean compareScreen(BufferedImage reference ,
                                      String referenceName ,
                                      ImageComparison imageComparison ,
                                      TakesScreenshot takesScreenshot)
      throws IOException {

    for (int times = 0; times < getScreenshotRetriesMax(); times++) {
      BufferedImage screenshotImage = ImageIO
          .read(new ByteArrayInputStream(
              takesScreenshot.getScreenshotAs(BYTES)));
      if (reference == null) {
        // Store the screenshot in the errors directory and fail the
        // test
        createScreenshotDirectoriesIfNeeded()
            .apply(null)
            .ifPresentOrElse(aVoid -> getLogger(ScreenshotComparator.class).info("Screenshot Directories are OK.") ,
                             failed -> getLogger(ScreenshotComparator.class).warning(failed));

        ImageIO.write(screenshotImage , IMAGE_FILE_NAME_ENDING ,
                      getErrorScreenshotFile().apply(referenceName));
        getLogger(ScreenshotComparator.class)
            .warning("No reference found for " + referenceName
                    + " in "
                    + getScreenshotReferenceDirectory());
        return false;
      }
      if (imageComparison.imageEqualToReference(screenshotImage ,
                                                reference , referenceName ,
                                                getScreenshotComparisonTolerance())) {
        return true;
      }
      pause(getScreenshotRetryDelay());
    }
    return false;
  }

  /**
   * Captures a screenshot of the given screen (if parameter is a driver) or
   * the given element (if the parameter is a WebElement).
   *
   * @param driver            The web driver, capable of taking screenshots
   * @param screenshotContext The context of the screenshot, either a driver for a full page
   *                          screenshot or an element for a screenshot of only that element
   * @return a captured image
   * @throws IOException
   */
  private static BufferedImage getScreenshot(TakesScreenshot driver ,
                                             TakesScreenshot screenshotContext) throws IOException {
    boolean elementScreenshot = (screenshotContext instanceof WebElement);

    if (elementScreenshot && supportsElementScreenshots == null) {
      // Detect if the driver supports element screenshots or not
      try {
        byte[] screenshotBytes = screenshotContext
            .getScreenshotAs(BYTES);
        supportsElementScreenshots = true;
        return ImageIO.read(new ByteArrayInputStream(screenshotBytes));
      } catch (UnsupportedCommandException e) {
        supportsElementScreenshots = false;
      } catch (WebDriverException e) {
        if (e.getCause() instanceof UnsupportedCommandException) {
          supportsElementScreenshots = false;
        } else {
          throw e;
        }
      }
    }

    if (elementScreenshot && ! supportsElementScreenshots) {
      // Driver does not support element screenshots, get whole screen
      // and crop
      BufferedImage image = ImageIO.read(new ByteArrayInputStream(
          driver.getScreenshotAs(BYTES)));
      return cropToElement((WebElement) screenshotContext , image);
    } else {
      // Element or full screen image
      return ImageIO.read(new ByteArrayInputStream(
          screenshotContext.getScreenshotAs(BYTES)));
    }
  }

  /**
   * Crops the image to show only the element. If the element is partly off
   * screen, crops to show the part of the element which is in the screenshot
   *
   * @param element    the element to retain in the screenshot
   * @param fullScreen the full screen image
   * @return
   * @throws IOException
   */
  public static BufferedImage cropToElement(WebElement element ,
                                            BufferedImage fullScreen) throws IOException {
    Point loc = element.getLocation();
    Dimension size = element.getSize();
    int x = loc.x, y = loc.y;
    int w = size.width;
    int h = size.height;

    if (x >= 0 && x < fullScreen.getWidth()) {
      // X loc on screen
      // Get the part of the element which is on screen
      w = Math.min(fullScreen.getWidth() - x , w);
    } else {
      throw new IOException("Element x is outside the screenshot (x: " + x
                            + ", y: " + y + ")");
    }

    if (y >= 0 && y < fullScreen.getHeight()) {
      // Y loc on screen
      // Get the part of the element which is on screen
      h = Math.min(fullScreen.getHeight() - y , h);
    } else {
      throw new IOException("Element y is outside the screenshot (x: " + x
                            + ", y: " + y + ")");
    }

    return fullScreen.getSubimage(x , y , w , h);
  }


  private static void pause(int delay) {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
    }
  }

}
