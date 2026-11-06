# Appium Mobile Automation Framework (Java + Maven)
## Step-by-Step Prompting Guide for AI-Assisted Development

This document contains a set of structured prompts designed to generate a production-grade, open-source **Appium Mobile Automation Framework** for **Android and iOS** using **Java**, **Maven**, and **TestNG**.

---

### Machine Pre-requisites Assumed
* Java JDK (25)
* Apache Maven
* Node.js & npm
* Appium Server (`npm install -g appium`)
* Android Studio (with Android SDK & Emulators configured)

---

### Step 1: Project Architecture & Dependencies (`pom.xml`)

> **Prompt:**
> "Create a complete Maven project structure for a mobile automation framework in Java using Appium (Java Client v9+), TestNG, and ExtentReports.
> 
> Include a `pom.xml` configured with dependencies for:
> - `appium-java-client`
> - `selenium-java`
> - `testng`
> - `extentreports`
> - `slf4j-simple`
> 
> Set up a clean folder structure following the Page Object Model (POM):
> - `src/main/java/base`: Base class handling AppiumDriver initialization for local Android (UiAutomator2) and iOS (XCUITest) execution.
> - `src/main/java/pages`: Placeholder page object classes.
> - `src/main/java/utils`: Utilities for explicit waits, mobile gestures, and screenshot capture on failure.
> - `src/test/java/tests`: Test class directory.
> - `src/test/resources`: TestNG XML suite file (`testng.xml`) and configuration properties file (`config.properties`)."

---

### Step 2: Driver Management & Cross-Platform Configuration

> **Prompt:**
> "Write the `BaseTest.java` class in Java using TestNG for an Appium mobile automation framework.
> 
> Requirements:
> - Support cross-platform capabilities via TestNG `@Parameters` or a `config.properties` file (switching between Android UiAutomator2 and iOS XCUITest).
> - Configure `UiAutomator2Options` for Android and `XCUITestOptions` for iOS with sensible defaults (e.g., `setApp()`, `setAutomationName()`, `setPlatformName()`, `setDeviceName()`).
> - Implement `@BeforeMethod` to initialize the `AppiumDriver` pointing to the local Appium server (`http://127.0.0.1:4723/`) and `@AfterMethod` to safely quit the driver session.
> - Implement a thread-safe `ThreadLocal<AppiumDriver>` wrapper to allow seamless parallel test execution in the future."

---

### Step 3: Native App Testing (Android ApiDemos)

> **Prompt:**
> "Write a sample native app test case in Java using TestNG and the Page Object Model (POM) for the open-source Android `ApiDemos-debug.apk` application.
> 
> Requirements:
> - Create a `HomePage` class extending a base page object, using `AppiumBy` locators to navigate from the main menu to **Views** -> **Controls** -> **Dark Theme**.
> - Create a `ControlsPage` class to interact with native elements (e.g., clicking checkboxes, selecting radio buttons, and typing into text fields).
> - Write a TestNG test class `NativeControlsTest.java` that executes these actions and adds assertions to verify the UI states. Use explicit waits instead of thread sleeps."

---

### Step 4: Mobile Web Application Testing (Chrome / Safari)

> **Prompt:**
> "Write a sample mobile web test case in Java using Appium and TestNG that automates a mobile browser session (Google Chrome on Android / Safari on iOS).
> 
> Requirements:
> - Configure Appium capabilities specifically for mobile web testing (`platformName`, `browserName='Chrome'`, and standard browser configurations).
> - Write a test that navigates to a public open-source demo site (e.g., `https://the-internet.herokuapp.com/login`).
> - Implement a helper method to locate the username/password fields and login button using standard Selenium locators (`By.id`, `By.cssSelector`), perform login, and assert successful redirection."

---

### Step 5: Hybrid App Context Switching & Validation

> **Prompt:**
> "Write a guide and sample code snippet in Java explaining how to handle **Hybrid Mobile Applications** using Appium.
> 
> Specifically, provide a utility method or test snippet demonstrating:
> 1. How to list all available execution contexts using `driver.getContextHandles()`.
> 2. How to switch from the `NATIVE_APP` context to the `WEBVIEW` context (e.g., `driver.context("WEBVIEW_com.example.app")`).
> 3. How to interact with HTML elements inside the WebView using standard locators while in web context, and how to switch back to native mode when interacting with native bottom bars or dialogs."

---

### Step 6: Test Suite Configuration & ExtentReports Integration

> **Prompt:**
> "Provide the configuration and code needed to generate rich HTML execution reports and run tests via TestNG XML:
> 
> 1. Write the XML content for `testng.xml` configured to run test classes in parallel with thread-count settings.
> 2. Create an ExtentReports utility class (`ExtentReportManager.java`) using `ExtentSparkReporter` that hooks into TestNG `@BeforeSuite`, `@AfterSuite`, and `@AfterMethod` (capturing screenshots automatically when a test fails and attaching them to the HTML report)."

---

### Bonus Step: Common Mobile Gestures Utility

> **Prompt:**
> "Write a Java utility class `MobileGestures.java` using Appium 9+ W3C Actions API for performing common mobile gestures:
> - `scrollDown()` and `scrollUp()`
> - `swipeLeft()` and `swipeRight()`
> - `longPress(WebElement element)`
> - `dragAndDrop(WebElement source, WebElement target)`"
```

eof

I have generated the `Appium_Java_Framework_Prompts.md` file containing all the structured prompts categorized from initial setup to execution reporting and gestures.

Would you like me to start generating the complete codebase for any of these steps directly?