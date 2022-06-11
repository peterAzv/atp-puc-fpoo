package br.com.puc.atp.peterson.biblioteca.bean;

public class LivroCulinaria extends Livro {

	private static final long serialVersionUID = 1L;

	private String regiaoCulinaria;
	private boolean possuiSobremesas; // Doce, salgada

	public LivroCulinaria(String nome, String iSBN, int ano, String autor, String resenha, String regiaoCulinaria,
			boolean possuiSobremesas) {
		super(nome, iSBN, ano, autor, resenha);
		this.regiaoCulinaria = regiaoCulinaria;
		this.possuiSobremesas = possuiSobremesas;
	}

	@Override
	public String idadeIndicada() {
		return "A partir de 12 anos";
	}

	public String getRegiaoCulinaria() {
		return regiaoCulinaria;
	}

	public void setRegiaoCulinaria(String regiaoCulinaria) {
		this.regiaoCulinaria = regiaoCulinaria;
	}

	public boolean isPossuiSobremesas() {
		return possuiSobremesas;
	}

	public void setPossuiSobremesas(boolean possuiSobremesas) {
		this.possuiSobremesas = possuiSobremesas;
	}

}
