package tests;

import base.BaseTest;
import config.FrameworkConfig;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorArithmeticTest extends BaseTest {
    private static final String CALCULATOR_PACKAGE = "com.sec.android.app.popupcalculator";
    private static final Pattern RESULT_NUMBER =
            Pattern.compile("^(Minus\\s+)?([-+]?\\d[\\d,]*(?:\\.\\d+)?)", Pattern.CASE_INSENSITIVE);
    private static final By DISPLAY = AppiumBy.id(CALCULATOR_PACKAGE + ":id/calc_edt_formula");
    private static final By RESULT = AppiumBy.id(CALCULATOR_PACKAGE + ":id/calc_tv_result");
    private static final By CLEAR = AppiumBy.id(CALCULATOR_PACKAGE + ":id/calc_keypad_btn_clear");
    private static final By EQUALS = AppiumBy.id(CALCULATOR_PACKAGE + ":id/calc_keypad_btn_equal");
    private static final By DECIMAL_POINT = AppiumBy.id(CALCULATOR_PACKAGE + ":id/calc_keypad_btn_dot");
    private static final By SIGN = AppiumBy.id(CALCULATOR_PACKAGE + ":id/calc_keypad_btn_plusminus");

    @Override
    protected String defaultAppType() {
        return "calculator";
    }

    @DataProvider(name = "arithmeticOperations")
    public Object[][] arithmeticOperations() {
        BigDecimal firstNumber = new BigDecimal(FrameworkConfig.calculatorFirstNumber());
        BigDecimal secondNumber = new BigDecimal(FrameworkConfig.calculatorSecondNumber());
        if (secondNumber.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("calculator.second.number must not be zero.");
        }
        return new Object[][]{
                {Operation.ADD, firstNumber, secondNumber},
                {Operation.SUBTRACT, firstNumber, secondNumber},
                {Operation.MULTIPLY, firstNumber, secondNumber},
                {Operation.DIVIDE, firstNumber, secondNumber}
        };
    }

    @Test(dataProvider = "arithmeticOperations")
    public void calculatesDynamicNumbers(Operation operation, BigDecimal first, BigDecimal second) {
        tap(CLEAR);
        enterNumber(first);
        tap(operation.button);
        enterNumber(second);
        tap(EQUALS);

        String resultText = readResult();
        BigDecimal actual;
        try {
            actual = new BigDecimal(resultText.replace(",", "").replace("\u2212", "-"));
        } catch (NumberFormatException e) {
            throw new AssertionError("Calculator displayed a non-numeric result: '" + resultText + "'.", e);
        }
        BigDecimal expected = operation.calculate(first, second);
        Assert.assertEquals(actual.compareTo(expected), 0,
                first + " " + operation.symbol + " " + second + " should equal " + expected
                        + ", but calculator displayed " + actual);
    }

    private void enterNumber(BigDecimal number) {
        BigDecimal absolute = number.abs().stripTrailingZeros();
        if (number.signum() < 0) {
            tap(SIGN);
        }
        String digits = absolute.toPlainString();
        for (char character : digits.toCharArray()) {
            if (character == '.') {
                tap(DECIMAL_POINT);
            } else {
                int digit = Character.digit(character, 10);
                if (digit < 0) {
                    throw new IllegalArgumentException("Unsupported character in calculator input: " + character);
                }
                tap(AppiumBy.id(CALCULATOR_PACKAGE + ":id/calc_keypad_btn_0" + digit));
            }
        }
    }

    private void tap(By locator) {
        getDriver().findElement(locator).click();
    }

    private String readResult() {
        var resultElement = getDriver().findElement(RESULT);
        String resultText = normalize(resultElement.getText());
        if (resultText.isBlank()) {
            resultText = normalize(resultElement.getAttribute("text"));
        }
        var displayElement = getDriver().findElement(DISPLAY);
        String displayText = normalize(displayElement.getText());
        if (displayText.isBlank()) {
            displayText = normalize(displayElement.getAttribute("text"));
        }
        String visibleResult = resultText.isBlank() ? displayText : resultText;
        if (visibleResult.isBlank()) {
            throw new AssertionError("Calculator result was empty. UI source after calculation:\n"
                    + getDriver().getPageSource());
        }
        Matcher number = RESULT_NUMBER.matcher(visibleResult.replace("\u2212", "-").trim());
        if (!number.find()) {
            throw new AssertionError("Calculator result did not start with a number: '" + visibleResult + "'.");
        }
        return number.group(1) == null ? number.group(2) : "-" + number.group(2);
    }

    private String normalize(String text) {
        if (text == null || text.equalsIgnoreCase("null")) {
            return "";
        }
        return text.trim();
    }

    public enum Operation {
        ADD("+", "calc_keypad_btn_add") {
            @Override
            BigDecimal calculate(BigDecimal first, BigDecimal second) {
                return first.add(second);
            }
        },
        SUBTRACT("-", "calc_keypad_btn_sub") {
            @Override
            BigDecimal calculate(BigDecimal first, BigDecimal second) {
                return first.subtract(second);
            }
        },
        MULTIPLY("×", "calc_keypad_btn_mul") {
            @Override
            BigDecimal calculate(BigDecimal first, BigDecimal second) {
                return first.multiply(second);
            }
        },
        DIVIDE("÷", "calc_keypad_btn_div") {
            @Override
            BigDecimal calculate(BigDecimal first, BigDecimal second) {
                return first.divide(second, 8, RoundingMode.HALF_UP).stripTrailingZeros();
            }
        };

        private final String symbol;
        private final By button;

        Operation(String symbol, String resourceId) {
            this.symbol = symbol;
            this.button = AppiumBy.id(CALCULATOR_PACKAGE + ":id/" + resourceId);
        }

        abstract BigDecimal calculate(BigDecimal first, BigDecimal second);
    }
}
