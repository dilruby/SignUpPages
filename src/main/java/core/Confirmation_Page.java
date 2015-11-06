package core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

public class Confirmation_Page {
	WebDriver driver;

	public Confirmation_Page(WebDriver wd) {
		driver = wd;
	}

	// method to open page
	public void launchBrowser(String url) {
		driver.get(url);
	} // launchBrowser

	// method to click on Submit button
	public void clickSubmit() {
		driver.findElement(By.id("id_submit_button")).click();
	} // clickSubmit

	// method to submit form
	public void submitForm(String fname, String lname, String email, String phone, String gender, String state,
			Boolean terms, String cterms, String ctitle) {
		launchBrowser("http://learn2test.net/qa/apps/sign_up/v1/");
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.findElement(By.id("id_fname")).clear();
		driver.findElement(By.id("id_fname")).sendKeys(fname);
		driver.findElement(By.id("id_lname")).clear();
		driver.findElement(By.id("id_lname")).sendKeys(lname);
		driver.findElement(By.id("id_email")).clear();
		driver.findElement(By.id("id_email")).sendKeys(email);
		driver.findElement(By.id("id_phone")).clear();
		driver.findElement(By.id("id_phone")).sendKeys(phone);
		if (gender.equalsIgnoreCase("male")) {
			driver.findElement(By.id("id_g_radio_01")).click();
		} else if (gender.equalsIgnoreCase("female")) {
			driver.findElement(By.id("id_g_radio_02")).click();
		}
		if (terms == true) {
			driver.findElement(By.id("id_checkbox")).click();
		}
		if (state.isEmpty()) {
		} else {
			new Select(driver.findElement(By.id("id_state"))).selectByVisibleText(state);
		}
		clickSubmit();

		String fnameConfActual = driver.findElement(By.id("id_fname_conf")).getText();
		String lnameConfActual = driver.findElement(By.id("id_lname_conf")).getText();
		String emailConfActual = driver.findElement(By.id("id_email_conf")).getText();
		String phoneConfActual = driver.findElement(By.id("id_phone_conf")).getText();
		String genderConfActual = driver.findElement(By.id("id_gender_conf")).getText();
		String stateConfActual = driver.findElement(By.id("id_state_conf")).getText();
		String termsConfActual = driver.findElement(By.id("id_terms_conf")).getText();
		assertEquals(driver.getTitle(), ctitle);
		assertEquals(fname, fnameConfActual);
		assertEquals(lname, lnameConfActual);
		assertEquals(email, emailConfActual);
		assertEquals(phone, phoneConfActual);
		assertEquals(gender, genderConfActual);
		assertEquals(state, stateConfActual);
		assertEquals(cterms, termsConfActual);
	} // submit_form

	// method for back button verification
	public void verifyBackButton(String idButton, String titleExpected) {
		launchBrowser("http://learn2test.net/qa/apps/sign_up/v1/conformation.php");
		driver.findElement(By.id(idButton)).click();
		assertEquals(driver.getTitle(), titleExpected);

	} // verifyBackButton

} // class Confirmation_Page
