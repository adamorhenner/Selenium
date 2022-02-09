package br.com.alura.leilao.login;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginTest {

	private LoginPage paginaDeLogin;
	
    @BeforeEach
    public void beforeEach(){
    	this.paginaDeLogin = new LoginPage();
    }

    @AfterEach
    public void afterEach(){
        this.paginaDeLogin.fechar();
    }

    @Test
    public void deveriaEfetuarLoginComDadosValidos() {
    	paginaDeLogin.preencherFormularioDeLogin("fulano", "pass");
    	paginaDeLogin.efetuaLogin();

        Assert.assertFalse(paginaDeLogin.isPaginaDeLogin());
        Assert.assertEquals("fulano", paginaDeLogin.getNomeUsuarioLogado());
    }

    @Test
    public void naoDeveriaLogarComDadosInvalidos() {
    	paginaDeLogin.preencherFormularioDeLogin("invalido", "123123");
    	paginaDeLogin.efetuaLogin();
    	

        Assert.assertTrue(paginaDeLogin.isPaginaDeLoginComDadosInvalidos());
        Assert.assertTrue(paginaDeLogin.contemTexto("Usuario e senha invalidos."));
        Assert.assertNull(paginaDeLogin.getNomeUsuarioLogado());
    }
    
    
    @Test
    public void naoDeveriaAcessarPaginaRestritaSemEstaLogado() {
    	paginaDeLogin.navegaParaPaginaDeLances();
    	
        Assert.assertTrue(paginaDeLogin.isPaginaDeLogin());
        Assert.assertFalse(paginaDeLogin.contemTexto("Dados do Leilao."));

    }
    
}
