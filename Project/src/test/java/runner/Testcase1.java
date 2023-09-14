package runner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import utils.EventHandler;
import utils.base64;
import utils.LoggerHandler;
import utils.Reporter;
import utils.Screenshot;

public class Testcase1 extends Base {
    java.util.logging.Logger log =  LoggerHandler.getLogger();
    base64 screenshotHandler = new base64();
    ExtentReports reporter = Reporter.generateExtentReport();;

@Test(priority = 1)
    public void productSearch() throws IOException {
    
        try {
            ExtentTest test = reporter.createTest("productSearch", "Execution of productSearch");
       
            driver.get(prop.getProperty("url"));
            log.info("Browser Navigated to the Register Page");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_TIME));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(PAGE_LOAD_TIME));
            driver.findElement(By.id("search")).sendKeys("Pants");

                Actions act=new Actions(driver);
                act.keyDown(Keys.ENTER)
                .release()
                .build()
                .perform();
                String ActualResult=driver.findElement(By.xpath("//a[@class='product-item-link']")).getText();
                Assert.assertTrue(ActualResult.contains("Pants"));
                test.pass("Test passed successfully");
               
        }
        catch (Exception ex) {
            ex.printStackTrace(); 
            ExtentTest test = reporter.createTest("SearchTest Exception");
            Screenshot.getScreenShot("SearchTest Exception");
            String base64Screenshot = screenshotHandler.captureScreenshotAsBase64(driver);
            test.log(Status.FAIL, "Unable to Perform the invalid Search", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
           
        }
        WebDriverListener listener = new EventHandler();
		driver = new EventFiringDecorator<>(listener).decorate(driver);
		return;
    }


@Test(dataProvider="SearchData",priority = 2)
    public void multipleSearchTest(String key) throws IOException {
        try{
            ExtentTest test = reporter.createTest("multipleSearchTest", "Execution for multipleSearchTest");
            driver.get(prop.getProperty("url"));
            driver.findElement(By.id("search")).sendKeys(key);
            Actions act=new Actions(driver);
            act.keyDown(Keys.ENTER)
            .release()
            .build()
            .perform();
            String ActualResult=driver.findElement(By.xpath("//a[@class='product-item-link']")).getText();
            Assert.assertTrue(ActualResult.contains(key));
            test.pass("Test passed successfully");
               

            }
         catch (Exception ex) {
                    ex.printStackTrace();
                    ExtentTest test = reporter.createTest("multiple SearchTest Test Exception");
                    Screenshot.getScreenShot("Login_Screenshot");
                    String base64Screenshot = screenshotHandler.captureScreenshotAsBase64(driver);
                    test.log(Status.FAIL, "multiple SearchTest", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                

                }

            }

@Test(priority=3)

    public void addToCartTest() throws InterruptedException, IOException {

    try{
        ExtentTest test = reporter.createTest("Add to cart Page", "Execution for add to cart");
        driver.get(prop.getProperty("url"));
        driver.findElement(By.linkText("Tees")).click();
        List<WebElement> searchResult=driver.findElements(By.xpath("//img[@class='product-image-photo']"));
        searchResult.get(2).click();
        driver.findElement(By.id("option-label-size-143-item-168")).click();
        driver.findElement(By.id("option-label-color-93-item-56")).click();
        driver.findElement(By.id("product-addtocart-button")).click();
        log.info("cart button clicked");
        Thread.sleep(5000);
        String actualResult = driver.findElement(By.xpath("//span[@class='counter-number']")).getText();
        Assert.assertEquals("1",actualResult);
        String base64Screenshot = screenshotHandler.captureScreenshotAsBase64(driver);
        test.pass("Test passed successfully", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
             
    }
    catch (Exception ex) {
        ex.printStackTrace();
        ExtentTest test = reporter.createTest("add To Cart Test");
        Screenshot.getScreenShot("addToCartTest");
        String base64Screenshot = screenshotHandler.captureScreenshotAsBase64(driver);
        test.log(Status.FAIL, "Unable to Perform add To CartTest", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());

    } 

}

@DataProvider(name="SearchData")

public Object[] getData(){
    Object[] data = new Object[3];
    data[0]="short";
    data[1]="Hoodie";
    data[2]="Sweatshirt";
return data;

}

@BeforeMethod
    public void beforeMethod() throws MalformedURLException {
        openBrowser();
        WebDriverListener listener = new EventHandler();
        driver = new EventFiringDecorator<>(listener).decorate(driver);

    }
@AfterMethod
    public void afterMethod() {
        driver.quit();
        reporter.flush();
        log.info("Browser closed");
    }
}

