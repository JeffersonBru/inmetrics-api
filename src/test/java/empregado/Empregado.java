package empregado;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.withArgs;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.is;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import conig_restassured.BaseTest;
import empregado.model._Empregado;
import suporte.LeitorProperties;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Empregado extends BaseTest{

	static HelperEmpregado hlpEmp;
	static _Empregado empregado;
	static String application;
	static int empregadoId;
	String basePath = "empregado";
	
	@BeforeClass
	public static void init() {
		hlpEmp = new HelperEmpregado();
		empregado = hlpEmp.gerarEmpregadoRandom();
		application = new LeitorProperties("config.properties").getProperties().getProperty("application");
	}

	@Test
	public void ct01_cadastrarEmpregadoValoresInvalidos() {
		empregado.salario = "1";
		empregado.comissao = "1";
			given()
				.headers("Authorization", application)
				.body(hlpEmp.gerarBody(empregado).toString())
				.basePath(basePath)
			.when()
				.post("cadastrar")
				.prettyPeek()
			.then()
				.statusCode(400)
				.body("[0]",containsStringIgnoringCase("deve estar no padrão 1.000,00"))
				.body("[1]",containsStringIgnoringCase("deve estar no padrão 1.000,00"));
	}
	
	@Test
	public void ct02_cadastrarEmpregadoComSucesso() {
		empregado = hlpEmp.gerarEmpregadoRandom();
		empregadoId = given()
				.headers("Authorization", application)
				.body(hlpEmp.gerarBody(empregado).toString())
				.basePath(basePath)
			.when()
				.post("cadastrar")
				.prettyPeek()
			.then()
				.statusCode(202)
				.body("nome", is(equalTo(empregado.nome)))
				.body("cpf", is(equalTo(empregado.cpf)))
				.body("sexo", is(equalTo(empregado.sexo.getValue())))
				.body("salario", is(equalTo(empregado.salario)))
				.body("cargo", is(equalTo(empregado.cargo)))
				.body("comissao", is(equalTo(empregado.comissao)))
				.body("admissao", is(equalTo(empregado.admissao)))
				.body("tipoContratacao", is(equalTo(empregado.contratacao.getValue())))
				.extract().path("empregadoId");
	}

	@Test
	public void ct03_buscarEmpregadoCadastrado() {
		given()
			.headers("Authorization", application)
			.pathParam("empregadoId", empregadoId)
			.basePath(basePath)
		.when()
			.get("list/{empregadoId}")
		.then()
			.statusCode(202)
			.body("nome", is(equalTo(empregado.nome)))
			.body("cpf", is(equalTo(empregado.cpf)))
			.body("sexo", is(equalTo(empregado.sexo.getValue())))
			.body("salario", is(equalTo(empregado.salario)))
			.body("cargo", is(equalTo(empregado.cargo)))
			.body("comissao", is(equalTo(empregado.comissao)))
			.body("admissao", is(equalTo(empregado.admissao)))
			.body("tipoContratacao", is(equalTo(empregado.contratacao.getValue())));
	}
	
	@Test
	public void ct04_buscasEmpregadoInexistente() {
		given()
			.headers("Authorization", application)
			.pathParam("empregadoId", "00")
			.basePath(basePath)
		.when()
			.get("list/{empregadoId}")
		.then()
			.statusCode(400)
			.body(is(equalTo("Empregado não cadastrado")));
	}

	@Test
	public void ct05_buscasTodosOsEmpregados() {
		given()
			.headers("Authorization", application)
			.basePath(basePath)
		.when()
			.get("list_all")
		.then()
			.statusCode(200)
			.body("find {it.nome == '%s'}.sexo", withArgs(empregado.nome), is(equalTo(empregado.sexo.getValue())))
			.body("find {it.nome == '%s'}.cpf", withArgs(empregado.nome), is(equalTo(empregado.cpf)))
			.body("find {it.nome == '%s'}.cargo", withArgs(empregado.nome), is(equalTo(empregado.cargo)))
			.body("find {it.nome == '%s'}.admissao", withArgs(empregado.nome), is(equalTo(empregado.admissao)))
			.body("find {it.nome == '%s'}.salario", withArgs(empregado.nome), is(equalTo(empregado.salario)))
			.body("find {it.nome == '%s'}.comissao", withArgs(empregado.nome), is(equalTo(empregado.comissao)))
			.body("find {it.nome == '%s'}.tipoContratacao", withArgs(empregado.nome), is(equalTo(empregado.contratacao.getValue())));
	}
	
	@Test
	public void ct06_buscasEmpregadoCadastradoSemAutenticar() {
		given()
			.pathParam("empregadoId", empregadoId)
			.basePath(basePath)
		.when()
			.get("list/{empregadoId}")
		.then()
			.statusCode(401)
			.body("status", is(equalTo(401)))
			.body("error", is(equalTo("Unauthorized")))
			.body("message", is(equalTo("Unauthorized")))
			.body("path", is(equalTo("/empregado/list/"+empregadoId)));
	}

	@Test
	public void ct07_editarEmpregado() {
		empregado = new HelperEmpregado().gerarEmpregadoRandom();
		given()
			.headers("Authorization", application)
			.body(hlpEmp.gerarBody(empregado).toString())
			.pathParam("empregadoId", empregadoId)
			.basePath(basePath)
		.when()
			.put("alterar/{empregadoId}")
		.then()
			.statusCode(202)
			.body("nome", is(equalTo(empregado.nome)))
			.body("cpf", is(equalTo(empregado.cpf)))
			.body("sexo", is(equalTo(empregado.sexo.getValue())))
			.body("salario", is(equalTo(empregado.salario)))
			.body("cargo", is(equalTo(empregado.cargo)))
			.body("comissao", is(equalTo(empregado.comissao)))
			.body("admissao", is(equalTo(empregado.admissao)))
			.body("tipoContratacao", is(equalTo(empregado.contratacao.getValue())));
	}

	@Test
	public void ct08_editarEmpregadoSemAutenticar() {
		given()
			.body(hlpEmp.gerarBody(empregado).toString())
			.pathParam("empregadoId", empregadoId)
			.basePath(basePath)
		.when()
			.put("alterar/{empregadoId}")
		.then()
			.statusCode(401)
			.body("status", is(equalTo(401)))
			.body("error", is(equalTo("Unauthorized")))
			.body("message", is(equalTo("Unauthorized")))
			.body("path", is(equalTo("/empregado/alterar/"+empregadoId)));
	}

	@Test
	public void ct09_buscarEmpregadoEditado() {
		given()
			.headers("Authorization", application)
			.pathParam("empregadoId", empregadoId)
			.basePath(basePath)
		.when()
			.get("list/{empregadoId}")
		.then()
			.statusCode(202)
			.body("nome", is(equalTo(empregado.nome)))
			.body("cpf", is(equalTo(empregado.cpf)))
			.body("sexo", is(equalTo(empregado.sexo.getValue())))
			.body("salario", is(equalTo(empregado.salario)))
			.body("cargo", is(equalTo(empregado.cargo)))
			.body("comissao", is(equalTo(empregado.comissao)))
			.body("admissao", is(equalTo(empregado.admissao)))
			.body("tipoContratacao", is(equalTo(empregado.contratacao.getValue())));
	}
	
	@Test
	public void ct10_deletarEmpregadoInexistente() {
		given()
			.headers("Authorization", application)
			.pathParam("empregadoId", "00")
		.when()
			.delete("empregado/deletar/{empregadoId}")
		.then()
			.statusCode(400)
			.body(is(equalTo("Empregado não cadastrado")));
	}

	@Test
	public void ct11_deletarEmpregado() {
		given()
			.headers("Authorization", application)
			.pathParam("empregadoId", empregadoId)
		.when()
			.delete("empregado/deletar/{empregadoId}")
		.then()
			.statusCode(202);
	}

	@Test
	public void ct12_deletarSemAutenticar() {
		given()
			.pathParam("empregadoId", empregadoId)
			.basePath(basePath)
		.when()
			.delete("deletar/{empregadoId}")
		.then()
			.statusCode(401)
			.body("status", is(equalTo(401)))
			.body("error", is(equalTo("Unauthorized")))
			.body("message", is(equalTo("Unauthorized")))
			.body("path", is(equalTo("/empregado/deletar/"+empregadoId)));
	}

	@Test
	public void ct13_cadastrarSemAutenticar() {
		given()
			.body(hlpEmp.gerarBody(empregado).toString())
			.basePath(basePath)
			.when()
			.post("cadastrar")
		.then()
			.statusCode(401)
			.body("status", is(equalTo(401)))
			.body("error", is(equalTo("Unauthorized")))
			.body("message", is(equalTo("Unauthorized")))
			.body("path", is(equalTo("/empregado/cadastrar")));
	}

	@Test
	public void ct14_buscarSemAutenticar() {
		given()
			.basePath(basePath)
			.get("list_all")
		.then()
			.statusCode(401)
			.body("status", is(equalTo(401)))
			.body("error", is(equalTo("Unauthorized")))
			.body("message", is(equalTo("Unauthorized")))
			.body("path", is(equalTo("/empregado/list_all")));
	}

	@Test
	public void ct15_cadastroDemEmpregadoSemBody() {
		given()
			.headers("Authorization", application)
			.basePath(basePath)
		.when()
			.post("cadastrar")
			.then()
			.statusCode(400);
	}
	
}
