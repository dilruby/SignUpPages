package core;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;

public class SignUp_Page {
	WebDriver driver;

	public SignUp_Page(WebDriver wd) {
		driver = wd;
	}
	// method to open page
				public void launchBrowser(String url) {
					driver.get(url);
				}
				
				// method to click on Submit button
				public void clickSubmit() {
					driver.findElement(By.id("id_submit_button")).click();
				} 
	

	// method for title validation
	public void verifyTitle(String titleExpected, String url) {
		launchBrowser(url);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		String titleActual = driver.getTitle();
		assertEquals(titleExpected, titleActual);
	} // verify_title

	// method for application title validation
	public void verifyAppTitle(String appTitleExpected, String url) {
		launchBrowser(url);
		String appTitleActual = driver.findElement(By.id("id_f_title")).getText();
		assertEquals(appTitleExpected, appTitleActual);
	} // verify_app_title

	// method for link validation
	public void verifyLink(String titleLinkExpected, String url, String idImage) {
		launchBrowser(url);
		driver.findElement(By.id(idImage)).click();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		ArrayList<String> allTabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(allTabs.get(1));
		String titleLinkActual = driver.getTitle();
		driver.close();
		driver.switchTo().window(allTabs.get(0));
		assertEquals(titleLinkExpected, titleLinkActual);
	} // verify_link
	

	// method for quote validation
	public boolean verifyQuote() {
		driver.findElement(By.id("id_quotes")).getText();
		String quote = driver.findElement(By.id("id_quotes")).getText();

		if (quote != null && quote.length() < 103 && quote.length() > 67) {
			return true;
		} else {
			return false;
		}

	} // verify_quote

	// method for error handling
	public void verifyErrorHandling(String fname, String lname, String email, String phone, String errorExpected) {

		driver.findElement(By.id("id_fname")).clear();
		driver.findElement(By.id("id_fname")).sendKeys(fname);
		driver.findElement(By.id("id_lname")).clear();
		driver.findElement(By.id("id_lname")).sendKeys(lname);
		driver.findElement(By.id("id_email")).clear();
		driver.findElement(By.id("id_email")).sendKeys(email);
		driver.findElement(By.id("id_phone")).clear();

		clickSubmit();
		
		if (fname.matches("^[a-zA-Z,.'-]{3,30}$")) {
			if (lname.matches("^[a-zA-Z,.'-]{3,30}$")) {
				if (email.matches("[a-zA-Z0-9]{2,}@([0-9a-zA-Z][-\\w]*\\.)+[a-zA-Z]{2,6}")) {
					if (phone.matches("^\\(?[\\d]{3}\\)?[\\s-]?[\\d]{3}[\\s-]?[\\d]{4}$")) {
					} else {
						String errorActual = driver.findElement(By.id("ErrorLine")).getText();
						assertEquals(errorExpected, errorActual);
					}
				} else {
					String errorActual = driver.findElement(By.id("ErrorLine")).getText();
					assertEquals(errorExpected, errorActual);
				}
			} else {
				String errorActual = driver.findElement(By.id("ErrorLine")).getText();
				assertEquals(errorExpected, errorActual);
			}
		} else {
			String errorActual = driver.findElement(By.id("ErrorLine")).getText();
			assertEquals(errorExpected, errorActual);
		}

	} // verify_error_handling
	
	// method for current city and state validation
	public void verifyCurrentCityState() throws Exception {

		String actualCityState = readActualCityState();

		driver.get("http://learn2test.net/qa/apps/sign_up/v0/");
		driver.findElement(By.id("id_current_location")).isDisplayed();
		String expectedCityState = driver.findElement(By.id("id_current_location")).getText();
		assertEquals(expectedCityState, actualCityState);
	} // verifyCurrentCityState

	// method for reading actual City and State from XML file
	private String readActualCityState() throws Exception {
		String ip = readStringFromUrl("http://checkip.amazonaws.com");
		String urlForIp = generateUrlForIp(ip);
		Document doc_xml = readXmlDocumentFromUrl(urlForIp);

		XPath x = XPathFactory.newInstance().newXPath();
		String latitude = x.compile("geoPlugin/geoplugin_latitude").evaluate(doc_xml);
		String longitude = x.compile("geoPlugin/geoplugin_longitude").evaluate(doc_xml);

		String urlForLatitudeAndLongitude = generateUrlForLatitudeAndLongitude(latitude, longitude);
		Document doc = readXmlDocumentFromUrl(urlForLatitudeAndLongitude);

		XPath xpath = XPathFactory.newInstance().newXPath();

		return xpath.compile("//display_location/full").evaluate(doc);
	} // readActualCityState

	// method for generation URL for current IP
	
	private String generateUrlForIp(String ip) {
		return "http://www.geoplugin.net/xml.gp?ip=" + ip;
	} // generateUrlForIp

	// method for generation URL for Latitude and Longitude of the current location
	private String generateUrlForLatitudeAndLongitude(String latitude, String longitude) {
		return "http://api.wunderground.com/api/8a75c2aa5ba78758/conditions/q/" + latitude + "," + longitude + ".xml";
	} // generateUrlForLatitudeAndLongitude

	// method for reading string from URL
	private String readStringFromUrl(String url) throws IOException {
		URL myip = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(myip.openStream()));
		return in.readLine();
	} // readStringFromUrl

	// method for reading XML document from URL
	private Document readXmlDocumentFromUrl(String url) throws IOException, Exception {
		DocumentBuilderFactory f_xml = DocumentBuilderFactory.newInstance();
		DocumentBuilder b_xml = f_xml.newDocumentBuilder();
		Document document = b_xml.parse(url);
		document.getDocumentElement().normalize();
		return document;
	} // readXmlDocumentFromUrl

	// method for weather icon validation
	public void verifyCurrentWeather() throws IOException, Exception {
		WebElement image = driver.findElement(By.xpath("//td[1]/img"));
		String src = image.getAttribute("src");
		int ind = src.lastIndexOf("/");
		String filenameExpected = src.substring(ind + 1);

		String ip = readStringFromUrl("http://checkip.amazonaws.com");
		String urlForIp = generateUrlForIp(ip);
		Document doc_xml = readXmlDocumentFromUrl(urlForIp);

		XPath x = XPathFactory.newInstance().newXPath();
		String latitude = x.compile("geoPlugin/geoplugin_latitude").evaluate(doc_xml);
		String longitude = x.compile("geoPlugin/geoplugin_longitude").evaluate(doc_xml);

		String urlForLatitudeAndLongitude = generateUrlForLatitudeAndLongitude(latitude, longitude);
		Document doc = readXmlDocumentFromUrl(urlForLatitudeAndLongitude);

		XPath xpath = XPathFactory.newInstance().newXPath();

		String weather_url = xpath.compile("//icon_url").evaluate(doc);
		int index = weather_url.lastIndexOf("/");
		String filenameActual = weather_url.substring(index + 1);

		assertEquals(filenameExpected, filenameActual);
	} // verifyCurrentWeather

	// method for temperature validation
	public void verifyTemperature() throws IOException, Exception {
		String ip = readStringFromUrl("http://checkip.amazonaws.com");
		String urlForIp = generateUrlForIp(ip);
		Document doc_xml = readXmlDocumentFromUrl(urlForIp);

		XPath x = XPathFactory.newInstance().newXPath();
		String latitude = x.compile("geoPlugin/geoplugin_latitude").evaluate(doc_xml);
		String longitude = x.compile("geoPlugin/geoplugin_longitude").evaluate(doc_xml);

		String urlForLatitudeAndLongitude = generateUrlForLatitudeAndLongitude(latitude, longitude);
		Document doc = readXmlDocumentFromUrl(urlForLatitudeAndLongitude);

		XPath xpath = XPathFactory.newInstance().newXPath();
		String temp_actual = xpath.compile("//temp_f").evaluate(doc) + " ℉";
		String temp_expected = driver.findElement(By.id("id_temperature")).getText();
		assertEquals(temp_expected, temp_actual);
	} // verifyTemperature
		
	// method to verify copyright
			public void verifyCopyright(String copyrightExpected){
				driver.findElement(By.id("copyright")).getText();
				String copyrightActual = driver.findElement(By.id("copyright")).getText();
				assertEquals (copyrightExpected,copyrightActual);
				}
}
