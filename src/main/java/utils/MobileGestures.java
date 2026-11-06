package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;

public final class MobileGestures {
    private static final Duration SWIPE_DURATION = Duration.ofMillis(600);
    private final AppiumDriver driver;
    private final PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

    public MobileGestures(AppiumDriver driver) {
        this.driver = driver;
    }

    public void scrollDown() {
        var size = driver.manage().window().getSize();
        swipe(size.width / 2, (int) (size.height * 0.8), size.width / 2, (int) (size.height * 0.25));
    }

    public void scrollUp() {
        var size = driver.manage().window().getSize();
        swipe(size.width / 2, (int) (size.height * 0.25), size.width / 2, (int) (size.height * 0.8));
    }

    public void swipeLeft() {
        var size = driver.manage().window().getSize();
        swipe((int) (size.width * 0.85), size.height / 2, (int) (size.width * 0.15), size.height / 2);
    }

    public void swipeRight() {
        var size = driver.manage().window().getSize();
        swipe((int) (size.width * 0.15), size.height / 2, (int) (size.width * 0.85), size.height / 2);
    }

    public void longPress(WebElement element) {
        Rectangle bounds = element.getRect();
        int centerX = bounds.getX() + bounds.getWidth() / 2;
        int centerY = bounds.getY() + bounds.getHeight() / 2;
        Sequence press = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofSeconds(1)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(press));
    }

    public void dragAndDrop(WebElement source, WebElement target) {
        Rectangle sourceBounds = source.getRect();
        Rectangle targetBounds = target.getRect();
        int sourceX = sourceBounds.getX() + sourceBounds.getWidth() / 2;
        int sourceY = sourceBounds.getY() + sourceBounds.getHeight() / 2;
        int targetX = targetBounds.getX() + targetBounds.getWidth() / 2;
        int targetY = targetBounds.getY() + targetBounds.getHeight() / 2;

        Sequence drag = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), sourceX, sourceY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(500)))
                .addAction(finger.createPointerMove(SWIPE_DURATION, PointerInput.Origin.viewport(), targetX, targetY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(drag));
    }

    private void swipe(int startX, int startY, int endX, int endY) {
        Sequence swipe = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(SWIPE_DURATION, PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(swipe));
    }
}
