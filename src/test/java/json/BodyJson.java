package json;

import org.json.JSONObject;
import empregado.model._Empregado;


public class BodyJson {

	public static JSONObject gerarBody(_Empregado empregado) {
		JSONObject empregadoJson = new JSONObject();
		empregadoJson.put("nome", empregado.nome);
		empregadoJson.put("cpf", empregado.cpf);
		empregadoJson.put("admissao", empregado.admissao);
		empregadoJson.put("cargo", empregado.cargo);
		empregadoJson.put("comissao", empregado.comissao);
		empregadoJson.put("salario", empregado.salario);
		empregadoJson.put("departamentoId", empregado.departamento);
		empregadoJson.put("sexo", empregado.sexo.getValue());
		empregadoJson.put("tipoContratacao", empregado.contratacao.getValue());
		return empregadoJson;
	}

}
