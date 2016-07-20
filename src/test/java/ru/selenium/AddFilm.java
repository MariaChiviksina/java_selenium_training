package ru.selenium;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

public class AddFilm extends TestNgTestBase{

  private boolean acceptNextAlert = true;

  @Test //добавить фильм с правильными данными
  public void testAddFilm() throws Exception {
    driver.get(baseUrl + "/php4dvd/#!/sort/name%20asc/");
    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    WebDriverWait wait =  new WebDriverWait(driver, 30);

    login();

    List<WebElement> films = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("movie_box")));
    int countBeforAdd = films.size();

    WebElement addMovieBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"Add movie\"]")));
    addMovieBtn.click();

    WebElement imdbSearch = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("imdbsearch")));
    imdbSearch.clear();
    imdbSearch.sendKeys("Everybody Wants Some!!");

    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.linkText("Everybody Wants Some!!")).click();

    WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"Save\"]")));
    saveBtn.click();

    WebElement logoBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("center")));
    logoBtn.click();

    List<WebElement> films1 = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("movie_box")));
    int countAfterAdd = films1.size();

    Assert.assertEquals(countBeforAdd+1, countAfterAdd);
  }

  @Test // удалить фильм
  public void testDeleteFilm() throws Exception {
    driver.get(baseUrl + "/php4dvd/#!/sort/name%20asc/");
    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    WebDriverWait wait =  new WebDriverWait(driver, 30);

    List<WebElement> films = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("movie_box")));
    int countBeforAdd = films.size();

    WebElement cover = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("movie_cover")));
    cover.click();

    WebElement removeBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"Remove\"]")));
    removeBtn.click();

    assertTrue(closeAlertAndGetItsText().matches("^Are you sure you want to remove this[\\s\\S]$"));

    List<WebElement> films1 = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("movie_box")));
    int countAfterAdd = films1.size();

    Assert.assertEquals(countBeforAdd-1, countAfterAdd);
  }

  @Test // добавить фильм с плохими данными
  public void testAddFilmWithoutYear() throws Exception {
    driver.get(baseUrl + "/php4dvd/#!/sort/name%20asc/");
    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    WebDriverWait wait = new WebDriverWait(driver, 30);

    WebElement addMovieBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"Add movie\"]")));
    addMovieBtn.click();

    WebElement imdbSearch = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("imdbsearch")));
    imdbSearch.clear();
    imdbSearch.sendKeys("Everybody Wants Some!!");

    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.cssSelector("td.title")).click();
    driver.findElement(By.name("year")).clear();
    driver.findElement(By.name("year")).sendKeys("");
    driver.findElement(By.cssSelector("img[alt=\"Save\"]")).click();

    WebElement error = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error")));
    Assert.assertTrue(error.isDisplayed());
  }

  private void login() {
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("admin");
    driver.findElement(By.name("password")).clear();
    driver.findElement(By.name("password")).sendKeys("admin");
    driver.findElement(By.name("submit")).click();
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