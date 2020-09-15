package empregado.type;

import java.util.Random;

public enum TipoContratacao {
	
	CLT("clt"),
	CNPJ("pj");
	
	private String value;
	
	private TipoContratacao(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	 /**
	  * Obter categoria de forma randomizada
	  * @return
	  */
	 public static TipoContratacao getRandom() {
       Random random = new Random();
       return values()[random.nextInt(values().length)];
	 }

}
