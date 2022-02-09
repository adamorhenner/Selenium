package br.com.alura.leilao;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PageObject {

	protected WebDriver browser;

	public PageObject(WebDriver browser) {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver98.exe");
		if (browser == null) {
			this.browser = new ChromeDriver();
		} else {
			this.browser = browser;
		}
	}
	
	
	public void fechar() {
		this.browser.quit();
	}
}
