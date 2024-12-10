package com.hotel.automation.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.hotel.automation.utils.ScreenshotUtils;

public class HotelSearchAutomation {
    public static void main(String[] args) {
        // Set up ChromeDriver path
        WebDriverManager.chromedriver().setup();;
        WebDriver driver = new ChromeDriver();

        try {

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loadingStateContent")));

            // 1. Navigate to the travel website
            driver.get("https://www.makemytrip.com/hotels/");
            driver.manage().window().maximize();

            // Wait for the sign-up modal to appear and close it
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='commonModal__close']"))).click();

            // Proceed with the rest of the script
            // Wait for the overlay to disappear (if any)
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loadingStateContent")));

            // Search for hotels in a specific city
            WebElement cityInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("city")));
            cityInput.click();
            // Wait for the input field with the title to be clickable
            WebElement whereToStayInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@title='Where do you want to stay?']")));
            whereToStayInput.sendKeys("Jaipur");
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[1]//b[contains(text(),'Jaipur')]"))).click();

            // Select check-in and check-out dates
            // Calculate the date +5 days from today
            LocalDate currentDate = LocalDate.now();
            LocalDate checkInDate = currentDate.plusDays(5);
            LocalDate checkOutDate = currentDate.plusDays(6);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy");
            String checkInDateString = checkInDate.format(formatter);
            String checkOutDateString = checkOutDate.format(formatter);

            System.out.println("Selecting check in date: " + checkInDateString);
            System.out.println("Selecting check out date: " + checkOutDateString);

            // Choose a check-in date (e.g., 10th of the current month)
            WebElement checkInDateElement = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='DayPicker-Day' and @aria-label='" + checkInDateString + "']")
            ));
            checkInDateElement.click();

            // Choose a check-out date (e.g., 15th of the current month)
            WebElement checkOutDateElement = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='DayPicker-Day' and @aria-label='" + checkOutDateString + "']")
            ));
            checkOutDateElement.click();

            // Select room and guest details
            // Locate the "Apply" button
            WebElement applyButton = driver.findElement(By.xpath("//button[contains(text(),'Apply')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", applyButton);

            // Wait for the button to be clickable and then click it
            // Click on search button
            WebElement searchButton = driver.findElement(By.id("hsw_search_button"));
            searchButton.click();
            ScreenshotUtils.takeScreenshot(driver, "step1_search_results.png");

            // 2. Select the 5th hotel from the search results
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='hotelListingContainer']")));
            WebElement fifthHotel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='hotelListingContainer']//div[@id= 'Listing_hotel_4']")));
            fifthHotel.click();

            // Switch to new tab (hotel details page)
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
            }
            ScreenshotUtils.takeScreenshot(driver, "step2_selected_hotel.png");

            // 3. Choose the first room for the selected hotel
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='packageroomcontainer']//div[@id='room0']//p[contains(text(),'Select Room')]"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'Review your Booking')]")));
            ScreenshotUtils.takeScreenshot(driver, "step3_selected_room.png");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
