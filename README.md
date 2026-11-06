# Appium Java Mobile Automation Framework

Java 17+ / Maven framework for Android (UiAutomator2) and iOS (XCUITest) automation with TestNG, Selenium, and ExtentReports.

## Prerequisites

- JDK 17 or newer and Maven
- Node.js and Appium 2 with the relevant driver installed (`uiautomator2` for Android or `xcuitest` for iOS)
- A running emulator/simulator or connected device
- Android SDK for Android automation
- Chrome on Android or Safari on iOS for the mobile web sample
- The Android ApiDemos application installed on the Android device, or its APK configured via `app.path`

## Configuration

Edit [`src/test/resources/config.properties`](src/test/resources/config.properties), or override individual entries with JVM system properties, for example:

```shell
mvn test -Dplatform=android -Ddevice.name="Pixel_8_API_35"
```

Key settings are `platform` (`android` or `ios`), `appium.server.url`, `device.name`, `app.path`, and `browser.name`. For Android native execution, configure `app.path` to an ApiDemos APK or use the package/activity values. For iOS native execution, set `app.path` to an app bundle or set `ios.bundle.id`.

The TestNG suite runs native and mobile-web samples as parallel tests. Parallel Appium sessions should use separate devices; if only one device is available, set `thread-count="1"` in `src/test/resources/testng.xml`. The native ApiDemos sample is Android-only and is skipped when `platform=ios`; the mobile-web sample uses Chrome on Android and Safari on iOS. Start Appium before running:

```shell
mvn test
```

Reports are written to `target/reports/extent-report.html`; failure screenshots are stored in `target/screenshots/`.

Override the calculator operands for all four operations with Maven system properties, for example:

```shell
mvn test -Dcalculator.first.number=27.5 -Dcalculator.second.number=5
```

The calculator test targets the Samsung Calculator package configured in `config.properties` and requires that app to be installed on the Android device.
To run only the calculator tests, use:

```shell
mvn '-Dtest=tests.CalculatorArithmeticTest' test
```

## Included samples

- `NativeControlsTest`: navigates ApiDemos Views → Controls → Dark Theme and checks a checkbox, radio button, and text field.
- `MobileWebLoginTest`: logs in to the Internet demo site and checks for its secure-area heading.
- `CalculatorArithmeticTest`: runs addition, subtraction, multiplication, and division against the Samsung Calculator with configurable decimal operands.
- `MobileGestures`: W3C touch actions for scrolling, swiping, long press, and drag-and-drop.
- `WaitUtils` and `ScreenshotUtils`: explicit waits and screenshot capture.

## Hybrid application context switching

Use the available context handles to find a WebView, switch to it, and use Selenium locators for its DOM. Switch back to `NATIVE_APP` before interacting with native controls:

```java
Set<String> contexts = HybridContextUtils.availableContexts(driver);
String webView = HybridContextUtils.switchToWebView(driver, Duration.ofSeconds(20));
driver.findElement(By.id("webview-element")).click();
HybridContextUtils.switchToNative(driver);
```

`switchToWebView` waits for a context whose name starts with `WEBVIEW` and returns the selected context name. Native gestures and native element locators should be used only after returning to the native context.
