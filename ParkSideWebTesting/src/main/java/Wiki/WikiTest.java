package Wiki;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

/* This test class tests all these four test scenarios.
 * Starting with Wikipedia's https://en.wikipedia.org/wiki/Metis_(mythology) page,
 * i) The headings listed in the 'Contents' box are used as headings on the page.
 * ii)The headings listed in the 'Contents' box have functioning hyperlink.
 * iii)Pop up text in the -> Personified concepts, ->'Nike'.
 * iv) Personified concepts ->'Nike', should take to a page that displays a family tree.
 */
public class WikiTest {
	WebDriver driver;

	 /* Set up method is used to set up the WebDriver and the base page
	 * (https://en.wikipedia.org/wiki/Metis_(mythology)) of the test
	 */
	public void setUp() {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize(); // maximize the window
		driver.get("Https://en.wikipedia.org/wiki/Metis_(mythology)"); // Go URL
	}

	@Test(groups = { "functionalTest", "all" })

	public void testHeadingsOfHyperLinks() {
		/*
		 * This method tests the headings listed in the 'Contents' box are used as
		 * headings on the page
		 */
		setUp();

		// Get the list of contents from the Wiki page in parentElements
		List<WebElement> parentElements = driver.findElements(By.xpath("//*[@id='toc']/ul/*"));

		String xpath = "//*[@id='toc']/ul/li";
		
		//Loop through the content list and verify  each element in the content list, is being displayed as heading when we click on it.

		for (int i = 1; i <= parentElements.size(); i++) {

			WebElement contentLink = driver.findElement(By.xpath(xpath + "[" + i + "]" + "/a"));
			contentLink.click();

			String[] headingsUnderContents = contentLink.getText().split(" ");
			String headingsUnderContentsForComparison = headingsUnderContents[1];

			if (headingsUnderContents.length > 2) {
				headingsUnderContentsForComparison = headingsUnderContents[1] + "_" + headingsUnderContents[2];
			}

			// Get the headings from the page with href attribute, split it by # and take
			// the heading name of the page

			String[] headingsOnThePage = contentLink.getAttribute("href").split("#");
			
			System.out.println("headingOnThePage**"+headingsOnThePage[1]);
			
			// Compare the headings on the page with headings on the content list

			if (headingsUnderContentsForComparison.equals(headingsOnThePage[1])) {
				assertTrue(true);
			}

		}
		tearDown();

	}

	@Test(groups = { "smokeTest", "all" })
	public void testFunctioningHyperLinks() {

		/*
		 * This method tests headings listed in the 'Contents' box have functioning
		 * hyperlink (checking for tag "/a").
		 */
		setUp();

		List<WebElement> col = driver.findElements(By.className("toclevel-1"));

		List<WebElement> parentElements = driver.findElements(By.xpath("//*[@id='toc']/ul/*"));

		String xpath = "//*[@id='toc']/ul/li";
        //Checking for the tag "/a"
		for (int i = 1; i <= parentElements.size(); i++) {

			WebElement contentLink = driver.findElement(By.xpath(xpath + "[" + i + "]" + "/a"));
			contentLink.click();
			System.out.print("Text within the table of contentslink" + contentLink.getText() + "\t");
			System.out.println("Anchorlink within the table of contentslink: " + contentLink.getAttribute("href"));

		}
		tearDown();

	}

	@Test(groups = { "functionalTest", "all" })
	public void testPersonifiedConceptsNikePopUpAndFamilyTreeValidation() throws InterruptedException {

		/*
		 * This method tests i)In the Personified concepts 'Nike' has a PopUp that
		 * contains the following text "Nike was a goddess who personified victor" ii)In
		 * the Personified concepts if we click on 'Nike' it displays Family_tree
		 */

		setUp();

		String xpath_of_personifiedConcepts = "//*[@id='mw-content-text']/div[1]/table[3]/tbody/tr[2]/td/div/ul/li[7]/a";
		String xpath_of_nike = "//*[@id='mw-content-text']/div[1]/div[4]/ul[3]/li[19]/a";
		WebElement personifiedConcept = driver.findElement(By.xpath(xpath_of_personifiedConcepts));
		personifiedConcept.click();
		WebElement nike = driver.findElement(By.xpath(xpath_of_nike));
		Actions actions = new Actions(driver);
		actions.moveToElement(nike).build().perform();
		driver.getPageSource().contains("Nike was a goddess who personified victory");
		nike.click();
		String xpath_of_familyTree = "//*[@id='Family_tree']";
		WebElement familyTree = driver.findElement(By.xpath(xpath_of_familyTree));
		tearDown();

	}

	public void tearDown() {
		driver.quit();
	}

}
