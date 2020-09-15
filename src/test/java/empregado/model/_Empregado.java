package empregado.model;

import empregado.type.Sexo;
import empregado.type.TipoContratacao;

public class _Empregado {
	
	public String nome, cpf, cargo, salario, admissao, comissao, departamento = "1"; 
	public Sexo sexo;
	public TipoContratacao contratacao;
	
	public _Empregado(String nome, String cpf, String cargo, String comissao, String salario, String admissao, Sexo sexo, TipoContratacao contratacao) {
		this.nome = nome;
		this.cargo = cargo;
		this.cpf = cpf;
		this.comissao = comissao;
		this.salario = salario;
		this.admissao = admissao;
		this.sexo = sexo;
		this.contratacao = contratacao;
	}
	
}
