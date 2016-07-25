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
    private WebDriverWait wait;
    private String FilmName = "Jaws";

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
  }

  @Test //добавить фильм с правильными данными
  public void testAddFilm() throws Exception {
      WebElement results = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("results")));

      driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
      List<WebElement> films = results.findElements(By.className("movie_box")); // посчитать количество фильмов, если они есть
      int countBeforeAdd = films.size();
      driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

      WebElement addMovieBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"Add movie\"]")));
      addMovieBtn.click();

      WebElement imdbSearch = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("imdbsearch")));
      imdbSearch.clear();
//      FilmName = "Jaws";
      imdbSearch.sendKeys(FilmName);

      driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
      driver.findElement(By.linkText(FilmName)).click();

      WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"Save\"]")));
      saveBtn.click();

      WebElement logoBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("center")));
      logoBtn.click();
      List<WebElement> films1 = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("movie_box")));
      int countAfterAdd = films1.size();

      Assert.assertEquals(countAfterAdd , countBeforeAdd + 1);
  }

  @Test // удалить фильм
  public void testDeleteFilm() throws Exception {
      List<WebElement> films = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("movie_box"))); // посчитать количество фильмов, если они есть
      int countBeforeDelete = films.size();

      WebElement cover = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("movie_cover")));
      cover.click();
      WebElement removeBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"Remove\"]")));
      removeBtn.click();

      assertTrue(closeAlertAndGetItsText().matches("^Are you sure you want to remove this[\\s\\S]$"));

      WebElement results1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("results")));
      driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
      List<WebElement> films1 = results1.findElements(By.className("movie_box"));
      int countAfterDelete = films1.size();
      driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

      Assert.assertEquals(countAfterDelete, countBeforeDelete-1);
  }
      @Test // добавить фильм с плохими данными
      public void testAddFilmWithoutYear() throws Exception {
        WebElement addMovieBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("img[alt=\"Add movie\"]")));
        addMovieBtn.click();

        WebElement imdbSearch = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("imdbsearch")));
        imdbSearch.clear();
        imdbSearch.sendKeys(FilmName);

        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        driver.findElement(By.cssSelector("td.title")).click();
        driver.findElement(By.name("year")).clear();
        driver.findElement(By.name("year")).sendKeys("");
        driver.findElement(By.cssSelector("img[alt=\"Save\"]")).click();

        WebElement error = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error")));
        Assert.assertTrue(error.isDisplayed());
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
