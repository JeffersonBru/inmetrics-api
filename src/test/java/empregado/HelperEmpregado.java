package empregado;

import java.text.NumberFormat;
import java.util.Locale;

import empregado.model._Empregado;
import empregado.type.Sexo;
import empregado.type.TipoContratacao;
import suporte.GeradorCPF;
import suporte.Suporte;

public class HelperEmpregado extends Suporte {

	private String cargos[] = new String[] { "Analista de sistemas", "Analista de Testes", "QA Engineer",
			"Arquiteto de Testes", "Automatizador de Testes", "Testador", "Professor" };

	public _Empregado gerarEmpregadoRandom() {
		Locale localeBR = new Locale("pt","BR");
		NumberFormat dinheiro = NumberFormat.getCurrencyInstance(localeBR);
		return new _Empregado(
				"Empregado_".concat(obterData("ddMMyyyyHHmmss")),
				new GeradorCPF().geraCPFComMask(),
				random(cargos),
				dinheiro.format(Integer.parseInt(obterData("hmms"))).replace("R$ ", ""),
				dinheiro.format(Integer.parseInt(obterData("hmmS"))).replace("R$ ", ""),
				obterData("dd/MM/yyyy"),
				Sexo.getRandom(),
				TipoContratacao.getRandom()
				);
	}

}
