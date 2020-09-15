package empregado;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import conig_restassured.BaseTest;
import empregado.model._Empregado;
import json.BodyJson;
import suporte.LeitorProperties;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Empregado extends BaseTest{

	String basePath = "empregado";
	static _Empregado empregado;
	static String application;
	static int empregadoId;
	
	@BeforeClass
	public static void init() {
		empregado = new HelperEmpregado().gerarEmpregadoRandom();
		application = new LeitorProperties("config.properties").getProperties().getProperty("application");
	}

	@Test
	public void ct01_cadastrarEmpregadoComSucesso() {
		empregadoId = given()
				.headers("Authorization", application)
				.body(BodyJson.gerarBody(empregado).toString())
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
	public void ct02_buscarEmpregadoCadastrado() {
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
	public void ct03_buscasEmpregadoCadastradoSemAutenticar() {
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
	public void ct04_buscasTodosOsEmpregados() {
		given()
			.headers("Authorization", application)
			.basePath(basePath)
		.when()
			.get("list_all")
		.then()
			.statusCode(200);
	}

	@Test
	public void ct05_editarEmpregado() throws IOException, Exception {
		empregado = new HelperEmpregado().gerarEmpregadoRandom();
		given()
			.headers("Authorization", application)
			.body(BodyJson.gerarBody(empregado).toString())
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
	public void ct06_editarEmpregadoSemAutenticar() {
		given()
			.body(BodyJson.gerarBody(empregado).toString())
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
	public void ct07_buscarEmpregadoEditado() {
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
	public void ct08_deletarEmpregado() {
		given()
			.headers("Authorization", application)
			.pathParam("empregadoId", empregadoId)
		.when()
			.delete("empregado/deletar/{empregadoId}")
		.then()
			.statusCode(202);
	}

	@Test
	public void ct09_deletarSemAutenticar() {
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
	public void ct10_cadastrarSemAutenticar() {
		given()
			.body(BodyJson.gerarBody(empregado).toString())
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
	public void ct11_buscarSemAutenticar() {
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
	public void ct12_cadastroDemEmpregadoSemBody() {
		given()
			.headers("Authorization", application)
			.basePath(basePath)
		.when()
			.post("cadastrar")
			.then()
			.statusCode(400);
	}
	
}
