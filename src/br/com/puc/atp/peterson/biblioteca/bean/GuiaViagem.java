package br.com.puc.atp.peterson.biblioteca.bean;

public class GuiaViagem extends Livro {

	private static final long serialVersionUID = 1L;

	private String local;
	private boolean possuiMapas;

	public GuiaViagem(String nome, String ISBN, int ano, String autor, String resenha, String local,
			boolean possuiMapas) {
		super(nome, ISBN, ano, autor, resenha);
		this.local = local;
		this.possuiMapas = possuiMapas;
	}

	@Override
	public String idadeIndicada() {
		return "Todas idades";
	}

}
