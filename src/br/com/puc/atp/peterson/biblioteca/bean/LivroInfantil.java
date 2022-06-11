package br.com.puc.atp.peterson.biblioteca.bean;

public class LivroInfantil extends Livro {

	private static final long serialVersionUID = 1L;

	private String brinde;
	private boolean possuiAtividades;

	public LivroInfantil(String nome, String ISBN, Integer ano, String autor, String resenha, String brinde,
			boolean possuiAtividades) {
		super(nome, ISBN, ano, autor, resenha);
		this.brinde = brinde;
		this.possuiAtividades = possuiAtividades;
	}

	@Override
	public String idadeIndicada() {
		return "Indicado para crianças a partir de 3 anos";
	}

	public String getBrinde() {
		return brinde;
	}

	public void setBrinde(String brinde) {
		this.brinde = brinde;
	}

	public boolean isPossuiAtividades() {
		return possuiAtividades;
	}

	public void setPossuiAtividades(boolean possuiAtividades) {
		this.possuiAtividades = possuiAtividades;
	}

}
