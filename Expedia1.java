package com.nal.SeleniumBasics;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Expedia1 {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\shash\\Documents\\Edureka_Workspace\\SeleniumProject\\Drivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		// Navigate to Expedia web portal
		driver.get("https://www.expedia.com");

		// In the expedia home page, click on flights link
		WebElement flights = driver.findElement(By.linkText("Flights"));
		flights.click();

		// Click on round trip link
		WebElement roundTrip = driver.findElement(By.linkText("Roundtrip"));
		roundTrip.click();

		// Click on leaving from search box and enter the airport name
		driver.findElement(By.xpath("//button[@data-stid = 'location-field-leg1-origin-menu-trigger']")).click();
		WebElement leavingFromSearchBox = driver.findElement(By.id("location-field-leg1-origin"));
		leavingFromSearchBox.sendKeys("Los Angeles (QLA-All Airports)");
		leavingFromSearchBox.sendKeys(Keys.RETURN);

		// Click on going to search box and enter the airport name
		driver.findElement(By.xpath("//button[@data-stid = 'location-field-leg1-destination-menu-trigger']")).click();
		WebElement goingToSearchBox = driver.findElement(By.id("location-field-leg1-destination"));
		goingToSearchBox.sendKeys("Las Vegas (LAS-All Airports)");
		goingToSearchBox.sendKeys(Keys.RETURN);

		// Click on departing date field and select the date
		driver.findElement(By.xpath("//button[@id='d1-btn']")).click();
		extractedDate(driver, "October 2020", "23");

		// Click on returning date filed and select the date
		driver.findElement(By.xpath("//button[@id='d2-btn']")).click();
		extractedDate(driver, "October 2020", "25");

		// On clicking search button, the select your departure flight page is displayed
		driver.findElement(By.xpath("//button[contains(text(),'Search')]")).click();

		// Screen1
		// Click on the evening button check box where flight departs from LA (Depatrure
		// time-Los Angeles)
		dismissPopUp(driver);
		clickOn(driver, driver.findElement(By.xpath("//input[@id='leg0-evening-departure']")), 15);
		dismissPopUp(driver);

		// Since the price is sorted by default from low to high, I will select the
		// first element.
		// Click on the first element Select button
		WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement selectCard = driver.findElement(By.xpath("//li[@data-test-id = 'offer-listing'][1]"));
		wait.until(ExpectedConditions
				.elementToBeClickable(selectCard.findElement(By.xpath("//button[@data-test-id= 'select-button']"))))
				.click();
		dismissPopUp(driver);

		// On clicking select button, the select your return to Los Angeles page is
		// displayed
		wait.until(ExpectedConditions
				.elementToBeClickable(selectCard.findElement(By.xpath("//button[@data-test-id= 'select-button-1']"))))
				.click();

		// Screen2
		// Click on the evening button check box where flight arrives back to LA
		// (Arrival time-Los Angeles)
		clickOn(driver, driver.findElement(By.xpath("//input[@id='leg1-evening-arrival']")), 15);
		dismissPopUp(driver);

		// Click on the first element Select button
		WebElement selectCard1 = driver.findElement(By.xpath("//li[@data-test-id = 'offer-listing'][1]"));
		wait.until(ExpectedConditions
				.elementToBeClickable(selectCard1.findElement(By.xpath("//button[@data-test-id= 'select-button']"))))
				.click();

		// On clicking add hotels button, a combination price list of the hotels + flight charge is displayed
		clickOn(driver, driver.findElement(By.xpath(" //a[@data-test-id='xsellAddHotelNow']")), 15);

		
		String winHandleBefore = driver.getWindowHandle();
		
		// Switch to new Window
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}

		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		List<WebElement> priceList = driver
				.findElements(By.xpath("//ul[@class = 'hotel-price']//li[contains(@class, 'actualPrice')]"));
		for (WebElement price : priceList) {
			System.out.println(price.getText());
		}

		driver.close();
		//Switch back to old window
		driver.switchTo().window(winHandleBefore);
		driver.quit();

	}
	//Handling the Website review Pop Up
	public static void dismissPopUp(WebDriver driver) {
		try {
			driver.findElement(By.xpath("//button[contains(text(),'No')]")).click();
		} catch (Exception e) {
		}
	}
	
	//Method to select on a specific date includes webdriver wait (Explicit wait)
	public static void extractedDate(WebDriver driver, String month, String exp_Date) {
		while (true) {
			WebElement mothParam = driver.findElement(By.xpath("//div[@class = 'uitk-new-date-picker-month'][1]/h2"));
			WebDriverWait wait = new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.visibilityOf(mothParam));
			String text = mothParam.getText();
			if (text.equals(month)) {
				break;
			} else {
				driver.findElement(By.xpath("//div[@class='uitk-calendar']//button[2]")).click();
			}
		}
		List<WebElement> allDate = driver.findElements(By.xpath("//div[1]/table/tbody/tr/td/button[1]"));
		for (WebElement currDate : allDate) {
			if (currDate.getAttribute("data-day").equals(exp_Date)) {
				currDate.click();
				break;
			}
		}
		driver.findElement(By.xpath("//button[@data-stid = 'apply-date-picker']")).click();
	}
	
	//Method to click on a webelement which includes webdriver wait (Explicit wait) 
	public static void clickOn(WebDriver driver, WebElement locator, long timeout) {
		new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.elementToBeClickable(locator));
		locator.click();
	}

}
