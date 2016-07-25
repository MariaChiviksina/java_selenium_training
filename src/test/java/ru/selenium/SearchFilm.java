package ru.selenium;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

/**
 * Created by Maria on 20.07.2016.
 */
public class SearchFilm extends TestNgTestBase {
    private WebDriverWait wait;
    private String filmIwant = "Jaws";
    private boolean acceptNextAlert = true;


    @BeforeSuite
    public void login() {
        driver.get(baseUrl + "/php4dvd/");
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("submit")).click();
        addFilm(filmIwant);
    }

    @Test // фильм найден
    public void testFoundFilm() throws Exception {
        List<WebElement> films = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("movie_box")));
        WebElement searchField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("q")));
        searchField.clear();
        searchField.sendKeys(filmIwant + Keys.RETURN);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        Boolean rsltsInvsbl = wait.until(ExpectedConditions.invisibilityOfAllElements(films));
        Assert.assertTrue(rsltsInvsbl);
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

        List<WebElement> searchResult = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("movie_box")));
        for (WebElement webElement : searchResult) {
            Assert.assertTrue(filmIwant.equals(webElement.findElement(By.className("title")).getText()));
        }
    }

    @Test // фильм не найден
    public void testNotFoundFilm(){
        List<WebElement> films = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("movie_box")));

        WebElement searchField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("q")));
        searchField.clear();
        String filmNotFound = "klflvk";
        searchField.sendKeys(filmNotFound + Keys.RETURN);

        Boolean rsltsInvsbl = wait.until(ExpectedConditions.invisibilityOfAllElements(films));
        Assert.assertTrue(rsltsInvsbl);

        WebElement notFound = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("content")));
        String title = notFound.getText();

        Assert.assertTrue(title.equals("No movies where found."));
    }

    @AfterSuite
    public void testDeleteFilm() throws Exception {
        WebElement searchField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("q")));
        searchField.clear();
        searchField.sendKeys(Keys.RETURN);

        WebElement cover = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("movie_box")));
        cover.click();
        WebElement removeBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"Remove\"]")));
        removeBtn.click();

        assertTrue(closeAlertAndGetItsText().matches("^Are you sure you want to remove this[\\s\\S]$"));

        WebElement results1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("results")));
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        List<WebElement> films1 = results1.findElements(By.className("movie_box"));
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

    }

    private void addFilm(String filmName){
        WebElement addMovieBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"Add movie\"]")));
        addMovieBtn.click();

        WebElement imdbSearch = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("imdbsearch")));
        imdbSearch.clear();
        imdbSearch.sendKeys(filmName);

        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        driver.findElement(By.linkText(filmName)).click();

        WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"Save\"]")));
        saveBtn.click();

        WebElement logoBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("center")));
        logoBtn.click();
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }

}
