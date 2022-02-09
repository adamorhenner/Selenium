package br.com.alura.leilao.lance;

import br.com.alura.leilao.PageObject;

public class LancesPage extends PageObject {
	
	private static final String URL_LANCE = "http://localhost:8080/leiloes/new";
	
	public LancesPage() {
		super(null);
		this.browser.navigate().to(URL_LANCE);
	}

	public boolean isPaginaAtual() {
		return browser.getCurrentUrl().contains(URL_LANCE);
		
	}
	
	public boolean isTituloLeilaoVisivel () {
		return browser.getPageSource().contains("Dados do Leilao");
	}
	
}
