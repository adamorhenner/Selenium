# Selenium 🔬

## Caso projeto não venha com a pasta padrão de teste:

![Untitled](Selenium%209a49c2fc6ff44ad882d0288a79bc68e0/Untitled.png)

![Untitled](Selenium%209a49c2fc6ff44ad882d0288a79bc68e0/Untitled%201.png)

![Untitled](Selenium%209a49c2fc6ff44ad882d0288a79bc68e0/Untitled%202.png)

Dentro do `src/test/java` cria a classe `HelloWorldSelenium` seu pacote será: `br.com.alura.leilao`

## Instalçao do selenium:

→ Via *Maven*

**Firefox:**

```java
<!-- https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-firefox-driver -->
		<dependency>
			<groupId>org.seleniumhq.selenium</groupId>
			<artifactId>selenium-firefox-driver</artifactId>
			<version>3.141.59</version>
		</dependency>
```

**Chrome :** 

```java
<!-- https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-chrome-driver -->
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-chrome-driver</artifactId>
    <version>3.141.59</version>
</dependency>
```

Baixar WebDrive:

[ChromeDriver - WebDriver for Chrome - Downloads](https://chromedriver.chromium.org/downloads)

criar pasta “drivers” no projeto, e colocar lá.

!*importante baixar a versão coerente com a do seu navegador!*

```java
package br.com.alura.leilao;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class HelloWorldSelenium {

    @Test
    public void hello() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver98.exe");
        WebDriver browser = new ChromeDriver();
        browser.navigate().to("http://localhost:8080/leiloes");
        browser.quit();
    }
}
```

Efetuando teste de Login:

```java
package br.com.alura.leilao.login;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginTest {
	
	@Test
	public void deveriaEfetuarLoginComDadosValidos() {
	    System.setProperty("webdriver.chrome.driver", "drivers/chromedriver98.exe");
	    WebDriver browser = new ChromeDriver();
	    browser.navigate().to("http://localhost:8080/login");
	    browser.findElement(By.id("username")).sendKeys("fulano");
	    browser.findElement(By.id("password")).sendKeys("pass");
	    browser.findElement(By.id("login-form")).submit();
	    

	    Assert.assertFalse(browser.getCurrentUrl().equals("http://localhost:8080/login"));
	    Assert.assertEquals("fulano", browser.findElement(By.id("usuario-logado")).getText());
	    browser.quit();
	}

}
```

explicação:

`browser.navigate().to("http://localhost:8080/login");` → para ir para a pagina de login

`browser.findElement(By.id("username")).sendKeys("fulano");` → para procurar pelo elemento de id = username e escrever “fulano”

`browser.findElement(By.id("password")).sendKeys("pass");` → para procurar pelo elemento de id= password e escrever “pass”

`browser.findElement(By.id("login-form")).submit();` → para procurar pelo elemento de id = login-form e submeter o formulario

Assertivas:

`Assert.assertFalse(browser.getCurrentUrl().equals("http://localhost:8080/login"));` → Assertiva para verificar se a url é igual a “http://localhost:8080/login”

`Assert.assertEquals("fulano", browser.findElement(By.id("usuario-logado")).getText());`  → Assertiva para verificar o nome do usuario é igual a “fulano”

### Teste com dados invalidos:

Dessa vez, desejamos permanecer na página de login - portanto, ao invés de `assertFalse()`
, verificaremos com `assertTrue()`se o browser se mantêm no endereço correto.

precisamos verificar se a mensagem de erro é exibida na página. Para isso, precisaremos recuperar o elemento, algo que poderíamos fazer com o `findElement(By.Id())`. Porém, para conhecermos outros métodos da API do Selenium, vamos tentar outra forma.

Outra maneira de recuperar elementos em uma página é com o método `browser.getPageSource()`, que devolve uma string com todo o código fonte da página. A partir dele, podemos usar o `contains()` para verificarmos se a mensagem "Usuário e senha inválidos." está presente na página.

```java
Assert.assertEquals("", browser.findElement(By.id("usuario-logado")).getText());
```

```java
Assert.assertThrows(NoSuchElementException.class, () -> browser.findElement(By.id("usuario-logado")));
```

Assim fica o código: 

```java
@Test
	public void naoDeveriaLogarComDadosInvalidos() {
	    System.setProperty("webdriver.chrome.driver", "drivers/chromedriver98.exe");
	    WebDriver browser = new ChromeDriver();
	    browser.navigate().to("http://localhost:8080/login");
	    browser.findElement(By.id("username")).sendKeys("invalido");
	    browser.findElement(By.id("password")).sendKeys("123123");
	    browser.findElement(By.id("login-form")).submit();
	    

	    Assert.assertTrue(browser.getCurrentUrl().equals("http://localhost:8080/login?error"));
	    Assert.assertTrue(browser.getPageSource().contains("Usuário e senha inválidos"));
	    Assert.assertThrows(NoSuchElementException.class, () -> browser.findElement(By.id("usuario-logado")));
	    browser.quit();
	}
```

para não precisar abrindo e ficar fechando o browser em todo metodo que for criado, iremos refatorar:

```java
package br.com.alura.leilao.login;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginTest {

    private static final String URL_LOGIN = "http://localhost:8080/login";
    private WebDriver browser;

    @BeforeAll
    public static void beforeAll() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver98.exe");
    }

    @BeforeEach
    public void beforeEach(){
        this.browser = new ChromeDriver();
        browser.navigate().to(URL_LOGIN);

    }

    @AfterEach
    public void afterEach(){
        this.browser.quit();
    }

    @Teste
    public void deveriaEfetuarLoginComDadosValidos() {
        browser.findElement(By.id("username")).sendKeys("fulano");
        browser.findElement(By.id("password")).sendKeys("pass");
        browser.findElement(By.id("login-form")).submit();

        Assert.assertFalse(browser.getCurrentUrl().equals(URL_LOGIN));
        Assert.assertEquals("fulano", browser.findElement(By.id("usuario-logado")).getText());
    }

    @Test
    public void naoDeveriaLogarComDadosInvalidos() {
        browser.findElement(By.id("username")).sendKeys("invalido");
        browser.findElement(By.id("password")).sendKeys("123123");
        browser.findElement(By.id("login-form")).submit();

        Assert.assertTrue(browser.getCurrentUrl().equals("http://localhost:8080/login?error"));
        System.out.println("passou");
        Assert.assertTrue(browser.getPageSource().contains("Usuario e senha invalidos."));
        Assert.assertThrows(NoSuchElementException.class, () -> browser.findElement(By.id("usuario-logado")));
    }
}
```

**Ultimo cenario de login:**  *o usuario não deve acessar um recurso restrito sem estar autenticado*

```java
@Test
    public void naoDeveriaAcessarPaginaRestritaSemEstaLogado() {
    	this.browser.navigate().to("http://localhost:8080/leiloes/2");
    	
        Assert.assertTrue(browser.getCurrentUrl().equals("http://localhost:8080/login"));
        Assert.assertFalse(browser.getPageSource().contains("Dados do Leilao"));

    }
```

# Page Object

[](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/)

Importante para boas praticas, dar manutenção em códigos, melhora a legibilidade.

Iremos extrair de dentro da classe de teste o acesso a API do Selenium, e criaremos uma nova classe: `LoginPage` → nela que vamos esconder todo o acesso ao webDrive, API do Selenium e a manipulação da pagina de login.

**Antes:**

```java
public class LoginTest {

    private static final String URL_LOGIN = "http://localhost:8080/login";
    private WebDriver browser;

    @BeforeAll
    public static void beforeAll() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver98.exe");
    }

    @BeforeEach
    public void beforeEach(){
        this.browser = new ChromeDriver();
        browser.navigate().to(URL_LOGIN);

    }

    @AfterEach
    public void afterEach(){
        this.browser.quit();
    }

    @Test
    public void deveriaEfetuarLoginComDadosValidos() {
        browser.findElement(By.id("username")).sendKeys("fulano");
        browser.findElement(By.id("password")).sendKeys("pass");
        browser.findElement(By.id("login-form")).submit();

        Assert.assertFalse(browser.getCurrentUrl().equals(URL_LOGIN));
        Assert.assertEquals("fulano", browser.findElement(By.id("usuario-logado")).getText());
    }

    @Test
    public void naoDeveriaLogarComDadosInvalidos() {
        browser.findElement(By.id("username")).sendKeys("invalido");
        browser.findElement(By.id("password")).sendKeys("123123");
        browser.findElement(By.id("login-form")).submit();

        Assert.assertTrue(browser.getCurrentUrl().equals("http://localhost:8080/login?error"));
        System.out.println("passou");
        Assert.assertTrue(browser.getPageSource().contains("Usuario e senha invalidos."));
        Assert.assertThrows(NoSuchElementException.class, () -> browser.findElement(By.id("usuario-logado")));
    }
    
    
    @Test
    public void naoDeveriaAcessarPaginaRestritaSemEstaLogado() {
    	this.browser.navigate().to("http://localhost:8080/leiloes/2");
    	
        Assert.assertTrue(browser.getCurrentUrl().equals("http://localhost:8080/login"));
        Assert.assertFalse(browser.getPageSource().contains("Dados do Leilao"));

    }
    
}
```

**Depois:**

```java
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
```

A Classe `LoginPage`:

```java
package br.com.alura.leilao.login;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginPage {
    
	private static final String URL_LOGIN = "http://localhost:8080/login";
    private WebDriver browser;
    
    public LoginPage() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver98.exe");
        this.browser = new ChromeDriver();
        browser.navigate().to(URL_LOGIN);
	}
    
    public static void beforeAll() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver98.exe");
    }
    
    public void beforeEach() {
        this.browser = new ChromeDriver();
        browser.navigate().to(URL_LOGIN);
    }

	public void fechar() {
		this.browser.quit();
	}

	public void preencherFormularioDeLogin(String username, String password) {
        browser.findElement(By.id("username")).sendKeys(username);
        browser.findElement(By.id("password")).sendKeys(password);
	}

	public void efetuaLogin() {
        browser.findElement(By.id("login-form")).submit();
	}

	public boolean isPaginaDeLogin() {
		return browser.getCurrentUrl().equals(URL_LOGIN);
	}

	public Object getNomeUsuarioLogado() {
		try {
			return browser.findElement(By.id("usuario-logado")).getText();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public void navegaParaPaginaDeLances() {
		this.browser.navigate().to("http://localhost:8080/leiloes/2");		
	}

	public boolean contemTexto(String texto) {
		return browser.getPageSource().contains(texto);
	}

	public boolean isPaginaDeLoginComDadosInvalidos() {
		return browser.getCurrentUrl().equals(URL_LOGIN + "?error");
	}
    
}
```

# Navegando entre page Object:

Testando cadastro de um leilão Precisa criar uma nova pagina de teste `LeiloesTeste` Necessario outro page objetc estilo LoginPage, agr sera `LeiloesPage`

`LeiloesPage`:

```java
public class LeiloesPage {
    
	private static final String URL_CADASTRO_LEILAO = "http://localhost:8080/leiloes/new";
    private WebDriver browser;
    
    public LeiloesPage(WebDriver browser) {
        this.browser = browser;
	}
    
	public void fechar() {
		this.browser.quit();
	} 
```

`LeiloesTeste`:

```java
public class LeilaosTeste {
	
	private LeiloesPage paginaDeLeiloes;
	
    @AfterEach
    public void afterEach(){
        this.paginaDeLeiloes.fechar();
    }
```

```java
@Test
    public void deveriaCadastrarLeilao() {
    	LoginPage paginaDeLogin = new LoginPage();
    	paginaDeLogin.preencherFormularioDeLogin("fulano", "pass");
    	this.paginaDeLeiloes = paginaDeLogin.efetuaLogin();
    	CadastroLeilaoPage paginaDeCadastro = paginaDeLeiloes.carregarFormulario();
    }
```

vou ter que alterar o metodo `efetuarLogin()` , ele está retornando void, e terá que retornar um `LeiloesPage`

```java
public LeiloesPage efetuaLogin() {
        browser.findElement(By.id("login-form")).submit();
        return new LeiloesPage(browser);
	}
```

```java
public LeiloesPage(WebDriver browser) {
        this.browser = browser;
	}
```

Para ir a pagina de cadastrar novo leilão :

    	`CadastroLeilaoPage paginaDeCadastro = paginaDeLeiloes.carregarFormulario();`

```java
public CadastroLeilaoPage carregarFormulario() {
		this.browser.navigate().to(URL_CADASTRO_LEILAO);
		return  new CadastroLeilaoPage(browser);
	}
```

```java
public class CadastroLeilaoPage {
    
    private WebDriver browser;
    
    public CadastroLeilaoPage(WebDriver browser) {
        this.browser = browser;
	}
    
	public void fechar() {
		this.browser.quit();
	}

}
```

```java
@Test
    public void deveriaCadastrarLeilao() {
    	LoginPage paginaDeLogin = new LoginPage();
    	paginaDeLogin.preencherFormularioDeLogin("fulano", "pass");
    	this.paginaDeLeiloes = paginaDeLogin.efetuaLogin();
    	CadastroLeilaoPage paginaDeCadastro = paginaDeLeiloes.carregarFormulario();
    }
```

O objetivo era so chegar no formulario, para chegar na pagina de formulario e cadastrar um leilão, primeiro tenho que logar, quando logo caio na pagina de listagem e da pagina de listagem navego para o formulario de leilões, foi isso que fizemos até agora.

Continuando o metodo... precisamos  preencher os campos do formulario e enviar o formulario. E verificar se o leilão foi cadastrado.

![Untitled](Selenium%209a49c2fc6ff44ad882d0288a79bc68e0/Untitled%203.png)

```java
@Test
    public void deveriaCadastrarLeilao() {
    	LoginPage paginaDeLogin = new LoginPage();
    	paginaDeLogin.preencherFormularioDeLogin("fulano", "pass");
    	this.paginaDeLeiloes = paginaDeLogin.efetuaLogin();
    	CadastroLeilaoPage paginaDeCadastro = paginaDeLeiloes.carregarFormulario();
    	
    	String hoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    	String nome = "Leilao do dia " + hoje;
    	String valor = "500.00";
    	
}
```

`cadastrarLeilao(nome, valor, hoje)`:

```java
public LeiloesPage cadastrarLeilao(String nome, String valorInicial, String dataAbertura) {
		this.browser.findElement(By.id("nome")).sendKeys(nome);
		this.browser.findElement(By.id("valorInicial")).sendKeys(valorInicial);
		this.browser.findElement(By.id("dataAbertura")).sendKeys(dataAbertura);
		this.browser.findElement(By.id("button-submit")).submit();
		
		return new LeiloesPage(browser);
		
	}
```

Quando chamarmos o metodo, seremos redirecionados para a lista de leiloes, por isso precisa devolver um objeto tipo `LeiloesPage` , retornando uma nova instancia desse objeto recebendo o mesmo browser.

Podemos reatribuir a variavel `paginaDeLeiloes` recebendo o retorno do metodo `cadastrarLeilao()`

```java
@Test
    public void deveriaCadastrarLeilao() {
    	LoginPage paginaDeLogin = new LoginPage();
    	paginaDeLogin.preencherFormularioDeLogin("fulano", "pass");
    	this.paginaDeLeiloes = paginaDeLogin.efetuaLogin();
    	CadastroLeilaoPage paginaDeCadastro = paginaDeLeiloes.carregarFormulario();
    	
    	String hoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    	String nome = "Leilao do dia " + hoje;
    	String valor = "500.00";
    	
    	this.paginaDeLeiloes = paginaDeCadastro.cadastrarLeilao(nome, valor, hoje);
}
```

Precisamos saber se a ultima linha da tabela é a novas informações cadastrada

```java
@Test
    public void deveriaCadastrarLeilao() {
    	LoginPage paginaDeLogin = new LoginPage();
    	paginaDeLogin.preencherFormularioDeLogin("fulano", "pass");
    	this.paginaDeLeiloes = paginaDeLogin.efetuaLogin();
    	CadastroLeilaoPage paginaDeCadastro = paginaDeLeiloes.carregarFormulario();
    	
    	String hoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    	String nome = "Leilao do dia " + hoje;
    	String valor = "500.00";
    	
    	this.paginaDeLeiloes = paginaDeCadastro.cadastrarLeilao(nome, valor, hoje);
    	
    	Assert.assertTrue(paginaDeLeiloes.isLeilaoCadastrado(nome, valor, hoje));
    	
    }
```

Para recuperarmos a última linha dessa tabela, podemos fazer uma navegação utilizando o seletor CSS. No método `isLeilaoCadastrado()`, chamaremos o `findElement()`, mas dessa vez usando `By.cssSelector()`. Passaremos para ele o parâmetro `"tabela-leiloes tbody tr:lastchilde"`, onde `#tabela-leiloes` é o ID da tabela, `tbody`, que representa as linhas do corpo, `tr`, que representa as linhas, e `last-child`, o pseudoelemento que nos retornará a última linha. Esses são conceitos de CSS.

```java
public boolean isLeilaoCadastrado(String nome, String valor, String data) {
		WebElement linhaDaTabela = this.browser.findElement(By.cssSelector("#tabela-leiloes tbody tr:last-child"));
		WebElement colunaNome = linhaDaTabela.findElement(By.cssSelector("td:nth-child(1)"));
		WebElement colunaDataAbertura = linhaDaTabela.findElement(By.cssSelector("td:nth-child(2)"));
		WebElement colunaValorInicial = linhaDaTabela.findElement(By.cssSelector("td:nth-child(3)"));
		
		return colunaNome.getText().equals(nome) 
				&& colunaDataAbertura.getText().equals(data) 
				&& colunaValorInicial.getText().equals(valor);
	}
```

## Testando Validações

cénario: entraremos na pagina de cadastro e clicaremos em “salvar” sem preencher nenhum campo, continuaremos na mesma pagina, testaremos se as validações são executadas e o cadastro não é bem sucedido quando alguem tenta cadastar um leilão com valores inválidos.

iremos trazer os codigo  iniciais para um beforeEach, pois tal codigo iriam se repetir.

```java
@BeforeEach
	public void beforeEach() {
		LoginPage paginaDeLogin = new LoginPage();
		paginaDeLogin.preencherFormularioDeLogin("fulano", "pass");
		this.paginaDeLeiloes = paginaDeLogin.efetuaLogin();
		this.paginaDeCadastro = paginaDeLeiloes.carregarFormulario();
		
	}
```

Iremos preencher nosso formulario com valores “vazios”e apos isso desejaremos verificar se, apos o envio das informações, continuaremos na pagina de leiloes e se as mensagens de erro estao visiveis na pagina.

```java
		@Test
    public void deveriaValidarCadastroDeLeilao() {
    	this.paginaDeLeiloes = paginaDeCadastro.cadastrarLeilao("", "", "");
    	
    	Assert.assertTrue(this.paginaDeLeiloes.isPaginaAtual());
```

`isPaginaAtual()` da classe `paginaDeLeilões`:

```java

private static final String URL_LEILOES = "http://localhost:8080/leiloes";
//....
public boolean isPaginaAtual() {
		return browser.getCurrentUrl().contentEquals(URL_LEILOES);
	}
```

Precisaremos validar se as mensagens de erro estão aparecendo e existem várias maneiras de verificarmos a presença dessas mensagens, por exemplo inspecionando o elemento ou verificando o código-fonte da própria pagina.:

```java

@Test
    public void deveriaValidarCadastroDeLeilao() {
    	this.paginaDeLeiloes = paginaDeCadastro.cadastrarLeilao("", "", "");
    	
    	Assert.assertFalse(this.paginaDeCadastro.isPaginaAtual());
    	Assert.assertTrue(this.paginaDeLeiloes.isPaginaAtual());
    	Assert.assertTrue(this.paginaDeCadastro.isMensagensDeValidacoesVisiveis());
    }
```

Usaremos a segunda abordagem. criaremos uma variavel `pageSource` do tipo string recebendo `browser.getPageSource()` a partir dela, usaremos o método `constains()` para verificarmos se as quatro mensagens de validação exibidas na página estão presentes no código, e retornaremos o resultado dessa verificação.

 `isMensagensDeValidacoesVisiveis()` :

```java
public boolean isMensagensDeValidacoesVisiveis() {
		String pageSource = browser.getPageSource();
		return pageSource.contains("minimo 3 caracteres")
				&& pageSource.contains("nao deve estar em branco")
				&& pageSource.contains("deve ser um valor maior de 0.1")
				&& pageSource.contains("deve ser uma data no formato dd/MM/yyyy");
	}
```

```java
@Test
    public void deveriaValidarCadastroDeLeilao() {
    	this.paginaDeLeiloes = paginaDeCadastro.cadastrarLeilao("", "", "");
    	
    	Assert.assertFalse(this.paginaDeCadastro.isPaginaAtual());
    	Assert.assertTrue(this.paginaDeLeiloes.isPaginaAtual());
    	Assert.assertTrue(this.paginaDeCadastro.isMensagensDeValidacoesVisiveis());
    }
```

    	`Assert.assertFalse(this.paginaDeCadastro.isPaginaAtual());` → verifica se ele esta na pagina de cadastro

    	`Assert.assertTrue(this.paginaDeLeiloes.isPaginaAtual());` → verifica se esta na pagina de leilão

Refatorando Pages Objects:

A melhor forma de refatorar é reparando nas classe Page, o que temos em comum e que se repetem, no caso teremos `WebDriver` , `fechar()` e o trecho: 

```java
  System.setProperty("webdriver.chrome.driver", "drivers/chromedriver98.exe");
  this.browser = new ChromeDriver();
```

Nos iremos implementar uma nova classe e fazer para ela ser a classe pai do nossos pageObject

```java
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
```

Sendo assim, organizaremos nossas classes Page:

`LeiloesPage():`

```java
package br.com.alura.leilao.leiloes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import br.com.alura.leilao.PageObject;

public class LeiloesPage extends PageObject {

	private static final String URL_CADASTRO_LEILAO = "http://localhost:8080/leiloes/new";
	private static final String URL_LEILOES = "http://localhost:8080/leiloes";

	public LeiloesPage(WebDriver browser) {
		super(browser);
	}

	public CadastroLeilaoPage carregarFormulario() {
		this.browser.navigate().to(URL_CADASTRO_LEILAO);
		return new CadastroLeilaoPage(browser);
	}

	public boolean isLeilaoCadastrado(String nome, String valor, String data) {
		WebElement linhaDaTabela = this.browser.findElement(By.cssSelector("#tabela-leiloes tbody tr:last-child"));
		WebElement colunaNome = linhaDaTabela.findElement(By.cssSelector("td:nth-child(1)"));
		WebElement colunaDataAbertura = linhaDaTabela.findElement(By.cssSelector("td:nth-child(2)"));
		WebElement colunaValorInicial = linhaDaTabela.findElement(By.cssSelector("td:nth-child(3)"));
		
		return colunaNome.getText().equals(nome) 
				&& colunaDataAbertura.getText().equals(data) 
				&& colunaValorInicial.getText().equals(valor);
	}

	public boolean isPaginaAtual() {
		return browser.getCurrentUrl().contentEquals(URL_LEILOES);
	}
	
	

	
}
```

`LoginPage():`

```java
package br.com.alura.leilao.login;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;

import br.com.alura.leilao.PageObject;
import br.com.alura.leilao.leiloes.LeiloesPage;

public class LoginPage extends PageObject{
    
	private static final String URL_LOGIN = "http://localhost:8080/login";
    
    public LoginPage() {
    	super(null);
        browser.navigate().to(URL_LOGIN);
	}
    
    public static void beforeAll() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver98.exe");
    }
    
    public void beforeEach() {
        this.browser = new ChromeDriver();
        browser.navigate().to(URL_LOGIN);
    }

	public void preencherFormularioDeLogin(String username, String password) {
        browser.findElement(By.id("username")).sendKeys(username);
        browser.findElement(By.id("password")).sendKeys(password);
	}

	public LeiloesPage efetuaLogin() {
        browser.findElement(By.id("login-form")).submit();
        return new LeiloesPage(browser);
	}

	public boolean isPaginaDeLogin() {
		return browser.getCurrentUrl().equals(URL_LOGIN);
	}

	public Object getNomeUsuarioLogado() {
		try {
			return browser.findElement(By.id("usuario-logado")).getText();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public void navegaParaPaginaDeLances() {
		this.browser.navigate().to("http://localhost:8080/leiloes/2");		
	}

	public boolean contemTexto(String texto) {
		return browser.getPageSource().contains(texto);
	}

	public boolean isPaginaDeLoginComDadosInvalidos() {
		return browser.getCurrentUrl().equals(URL_LOGIN + "?error");
	}
    
}
```

`LancePage():`

```java
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
```

`CadastroLeilaoPage():`

```java
package br.com.alura.leilao.leiloes;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.com.alura.leilao.PageObject;

public class CadastroLeilaoPage extends PageObject{
    
	private static final String URL_CADASTRO_LEILAO = "http://localhost:8080/leiloes/new";

    
    public CadastroLeilaoPage(WebDriver browser) {
    	super(browser);
    }
    
	public LeiloesPage cadastrarLeilao(String nome, String valorInicial, String dataAbertura) {
		this.browser.findElement(By.id("nome")).sendKeys(nome);
		this.browser.findElement(By.id("valorInicial")).sendKeys(valorInicial);
		this.browser.findElement(By.id("dataAbertura")).sendKeys(dataAbertura);
		this.browser.findElement(By.id("button-submit")).submit();
		
		return new LeiloesPage(browser);
		
	}

	public boolean isPaginaAtual() {
		return browser.getCurrentUrl().equals(URL_CADASTRO_LEILAO);
	}

	public boolean isMensagensDeValidacoesVisiveis() {
		String pageSource = browser.getPageSource();
		return pageSource.contains(" 3 caracteres")
				&& pageSource.contains(" deve estar em branco")
				&& pageSource.contains("deve ser um valor maior de 0.1")
				&& pageSource.contains("deve ser uma data no formato dd/MM/yyyy");
	}

}
```

Caso de Tempo:

Podemos ter uma aplicação que chama um API ou serviço externo que cause lentidão, ou uma requisição Ajax.

Uma maneira de lidar com isso é configurar o timeout do selenium, fazendo com que ele espere um determinado tempo antes de acusar uma falha no teste.

na nossa classe `PageObject`, assim que for instanciado um browser, chamaremos o metodo `browser.manage()`.

```java
public PageObject(WebDriver browser) {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver98.exe");
		if (browser == null) {
			this.browser = new ChromeDriver();
		} else {
			this.browser = browser;
		}

	this.browser.manege().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	}
```

Existem outros `timeouts` que podem ser configurados, como o `pageLoadTimeout()` . Quando nossas páginas demoram um pouco a carregar, podemos, por exemplo, fazer com que o Selenium espere 10 segundos antes de efetuar os testes e lançar os erros.