package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider { // Lets us read the data
    ChromeDriver driver;
    Wrappers wrappers;
    SoftAssert softAssert;

    /*
     * TODO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in
     * instructions
     */

    /*
     * Do not change the provided methods unless necessary, they will help in
     * automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        // Initialize the wrappers object with the driver
        wrappers = new Wrappers(driver);
    }

    @BeforeMethod
    public void setUp() {
        softAssert = new SoftAssert();
    }

    @AfterTest
    public void endTest() {
        driver.close();
        driver.quit();

    }

    @Test
    public void testCase01() {
    // Go to YouTube.com
    wrappers.navigateTo("https://www.youtube.com");

    // Assert that the URL is correct
    String currentUrl = wrappers.getCurrentUrl();
    softAssert.assertEquals(currentUrl, "https://www.youtube.com/", "The URL is not correct.");
    System.out.println("Checked the URL is correct");

    // Click on "About" at the bottom of the sidebar
    try {
    wrappers.clickElement(By.linkText("About"));

    // Wait until the about page loads and get some text to assert
    String aboutText = wrappers.waitForElement(By.cssSelector("h1")).getText();

    // Print the message on the screen
    System.out.println("About Page Header: " + aboutText);
    } catch (Exception e) {
    System.out.println("The 'About' link was not found: " + e.getMessage());
    softAssert.fail("The 'About' link was not found.");
    }

    // Print the message on the screen
    WebElement aboutText1 = driver.findElement(By.xpath("//*[@class='ytabout__content']//p[1]"));
    System.out.println("About Page Message: " + aboutText1.getText());
    WebElement aboutText2 = driver.findElement(By.xpath("//*[@class='ytabout__content']//p[2]"));
    System.out.println(aboutText2.getText());
        
    }

    @Test
    public void testCase02() {
        // Add logging to track the test execution
        System.out.println("Starting testCase02");

        // Go to YouTube.com
        wrappers.navigateTo("https://www.youtube.com");
        System.out.println("Navigated to YouTube");

        // Click on the "Films" tab
        wrappers.clickElement(By.xpath("//yt-formatted-string[@class='title style-scope ytd-guide-entry-renderer' and text()='Movies']"));
        System.out.println("Clicked on 'Movies' tab");

        wrappers.waitForPageLoad();

        // Scroll to the "Top Selling" section and continue scrolling to the extreme right
        WebElement topSellingSection = wrappers.waitForElement(
                By.xpath("//div[@id='title-text']//a[@class='yt-simple-endpoint style-scope ytd-shelf-renderer']"));
        System.out.println("Got top selling section");

        WebElement TopSelling = driver.findElement(By.xpath("//*[@id='right-arrow']//button"));
        while (TopSelling.isDisplayed()) {

            TopSelling.click();

        }
     System.out.println("Scrolled to the extreme right in 'Top Selling' section");        

        List<WebElement> topSellingMovies = topSellingSection.findElements(By.xpath("//div[@id='scroll-container']//div[@id='items']//ytd-grid-movie-renderer"));
        System.out.println("Got Top selling movies:"+topSellingMovies.size());

        List<WebElement> genre = driver.findElements(
                By.xpath("//*[@class='grid-movie-renderer-metadata style-scope ytd-grid-movie-renderer']"));
        List<WebElement> label = driver.findElements(By.xpath(
                "//*[@class='badge badge-style-type-simple style-scope ytd-badge-supported-renderer style-scope ytd-badge-supported-renderer']"));

        //Apply a Soft Assert on whether the movie is marked “A” for Mature or not

        WebElement matureLabel = label.get(label.size() - 1);        
        softAssert.assertTrue(matureLabel.getText().contains("A"), "Movie is  not  marked 'A' for Mature");
        System.out.println("Checked Movie rating is :"+matureLabel.getText());

        
        // Apply Soft assert on whether the movie is either "Comedy" or "Animation"
        WebElement genreLabel = genre.get(genre.size() - 1);
        String genreText = genreLabel.getText();        
        softAssert.assertTrue(genreText.contains("Comedy") || genreText.contains("Animation"),
                "Movie is  not in 'Comedy' or 'Animation' genre");
         
        System.out.println("Checked Movie Genre is Comedy or Animation");

        System.out.println("testCase02 completed");
    }

    @Test
    public void testCase03() {
    // Add logging to track the test execution
    System.out.println("Starting testCase03");

    // Go to YouTube.com
    wrappers.navigateTo("https://www.youtube.com");
    System.out.println("Navigated to YouTube");

    // Click on the "Music" tab
    wrappers.clickElement(By.xpath("//yt-formatted-string[@class='title style-scope ytd-guide-entry-renderer' and text()='Music']"));
    System.out.println("Clicked on 'Music' tab");

    // Find the first section in the Music tab
    WebElement firstSection = wrappers.waitForElement(By.xpath("//div[@id='items' and @class='style-scope yt-horizontal-list-renderer']"));
    System.out.println("Got the first section in the 'Music' tab");

    // Click the right arrow button to go to the extreme right
    WebElement rightArrowButton =
    firstSection.findElement(By.xpath("//ytd-button-renderer[@class='style-scope yt-horizontal-list-renderer arrow' and @button-next='']"));
    while (true) {
    try {
    rightArrowButton.click();
    System.out.println("Clicked right arrow button");
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    wait.until(ExpectedConditions.elementToBeClickable(rightArrowButton));
    } catch (Exception e) {
    System.out.println("Reached the extreme right of the section");
    break;
    }
    }
    wrappers.waitForPageLoad();
    

    // Print the name of the playlist

    List<WebElement> playlists =
    firstSection.findElements(By.xpath("//ytd-item-section-renderer[@class='style-scope ytd-section-list-renderer'][1]//h3[@class='style-scope ytd-compact-station-renderer']"));
    System.out.println("playlists size"+playlists.size());

    if (!playlists.isEmpty()) {
    WebElement lastPlaylist = playlists.get(playlists.size() - 1);
    String playlistName = lastPlaylist.getText();
    System.out.println("Name of the playlist: " + playlistName);

    // Assert on whether the number of tracks listed is less than or equal to 50
    List<WebElement> tracks = lastPlaylist.findElements(By.xpath("//ytd-item-section-renderer[@class='style-scope ytd-section-list-renderer'][1]//p[@id='video-count-text']"));
    String trackCount = tracks.get(tracks.size()-1).getText();
    softAssert.assertTrue(Integer.parseInt(trackCount.split(" ")[0].trim()) <= 50, "The number of tracks listed is greater than 50.");
    System.out.println("Number of tracks listed: " + trackCount);
    System.out.println("Checked number of tracks listed is less than or equal to 50");
    } else {
    System.out.println("No playlists found in the first section.");
    softAssert.fail("No playlists found in the first section.");
    }
    
    System.out.println("testCase03 completed");
    }

    @Test
    public void testCase04() throws Exception {
    // Add logging to track the test execution
    System.out.println("Starting testCase04");

    // Go to YouTube.com
    wrappers.navigateTo("https://www.youtube.com");
    System.out.println("Navigated to YouTube");

    // Click on the "News" tab
    wrappers.clickElement(By.xpath("//yt-formatted-string[text()='News']"));
    System.out.println("Clicked on 'News' tab");

    wrappers.waitForPageLoad();

    wrappers.scrollToBottom();
    wrappers.waitForElement(By.xpath("(//div[@id='title-container'])[7]"));

    wrappers.scrollIntoView(driver.findElement(By.xpath("(//div[@id='title-container'])[7]")));

    // Wait for the "Latest News Posts" to load
    List<WebElement> newsPosts = driver.findElements(By.xpath("(//div[@id='contents'])[15]/ytd-rich-item-renderer"));
    System.out.println("Found " + newsPosts.size() + " news posts");

    // Initialize sum of likes
    int totalLikes = 0;

    // Iterate over the first 3 news posts
    for (int i = 0; i < 3 && i < newsPosts.size(); i++) {
    WebElement post = newsPosts.get(i);

    // Print the title
    WebElement titleElement =
    post.findElement(By.xpath("//a[@id='author-text']/span"));
    String title = titleElement.getText();
    System.out.println("Title of post " + (i + 1) + ": " + title);

    // Print the body (description)
    WebElement bodyElement =
    post.findElement(By.xpath("//div[@id='post-text']"));
    String body = bodyElement.getText();
    System.out.println("Body of post " + (i + 1) + ": " + body);

    // Get the number of likes
    double likes = 0;
    try {
    WebElement likesElement = post.findElement(By.xpath("//span[@id='vote-count-middle']"));
    String likesText = likesElement.getText();
    
     likes = parseLikes(likesText);
     System.out.println(likes);
    } catch (NoSuchElementException e) {
    // If no likes element is found, assume 0 likes
    System.out.println("No likes found for post " + (i + 1) + ", assuming 0    likes");
    }
    

     System.out.println("Likes for post " + (i + 1) + ": " + likes);
     totalLikes += likes;
    }

    System.out.println("Total likes for the first 3 posts: " + totalLikes);

    // Assert all
    softAssert.assertAll();
    System.out.println("testCase04 completed");
    }
    public static double parseLikes(String likesText) {
        likesText = likesText.trim().toUpperCase(); // Remove any surrounding whitespace and ensure uppercase
        double number;

        if (likesText.endsWith("K")) {
            number = Double.parseDouble(likesText.replace("K", "")) * 1_000;
        } else if (likesText.endsWith("M")) {
            number = Double.parseDouble(likesText.replace("M", "")) * 1_000_000;
        } else if (likesText.endsWith("B")) {
            number = Double.parseDouble(likesText.replace("B", "")) * 1_000_000_000;
        } else {
            number = Double.parseDouble(likesText);
        }

        return number;
    }

    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void testCase05(String searchItem) {
    // Add logging to track the test execution
    System.out.println("Starting testCase04 for search item: " + searchItem);

    // Go to YouTube.com
    wrappers.navigateTo("https://www.youtube.com");
    System.out.println("Navigated to YouTube");

    // Enter the search item into the search bar and press Enter
    WebElement searchBox = wrappers.waitForElement(By.name("search_query"));
    searchBox.sendKeys(searchItem);
    searchBox.submit();
    System.out.println("Searched for: " + searchItem);

    // Wait for the search results to load
    wrappers.waitForElement(By.id("video-title"));
    System.out.println("Video title"+driver.findElement(By.id("video-title")).getText());

    wrappers.waitForPageLoad();
    // Scroll through the search results until the sum of video views reaches 10    Crores (100 million)
    long totalViews = 0;
    while (totalViews < 100000000) {
    List<WebElement> videoElements = driver.findElements(By.id("metadata-line"));
    for (WebElement video : videoElements) {
    try {

    // Extract the view count from the metadata line
    List<WebElement> metadataSpans = video.findElements(By.tagName("span"));
    for (WebElement span : metadataSpans) {
    String text = span.getText();
    if (text.contains("views")) {
    long views = parseViews(text);
    totalViews += views;

    if (totalViews >= 100000000) {
    break;
    }
    }
    }
    }
    catch (NoSuchElementException e) {
    // Handle cases where the view element is not found
    System.out.println("View element not found for a video.");
    }
    }

    // Scroll down to load more results
    wrappers.scrollToBottom();
    wrappers.waitForPageLoad();
    System.out.println("Scrolled to load more results. Total views so far: " +totalViews);
    }

    System.out.println("Total views reached: " + totalViews + " for search item: " + searchItem);
    }

    private long parseViews(String viewsText) {
    viewsText = viewsText.toLowerCase().replaceAll("[^\\d\\.km]", "");

    if (viewsText.endsWith("k")) {
    return (long) (Double.parseDouble(viewsText.replace("k", "")) * 1_000);
    } else if (viewsText.endsWith("m")) {
    return (long) (Double.parseDouble(viewsText.replace("m", "")) * 1_000_000);
    } else {
    return Long.parseLong(viewsText.replaceAll("[^\\d]", ""));
    }
    }

}