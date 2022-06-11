package br.com.puc.atp.peterson.biblioteca.bean;

import java.io.Serializable;

public abstract class Livro implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String ISBN;
	private int ano;
	private String autor;
	private String resenha;

	public Livro(String nome, String iSBN, int ano, String autor, String resenha) {
		super();
		this.nome = nome;
		ISBN = iSBN;
		this.ano = ano;
		this.autor = autor;
		this.resenha = resenha;
	}

	public abstract String idadeIndicada();

	@Override
	public String toString() {
		String retorno = "";
		retorno += "Nome: " + this.nome + "\n";
		retorno += "ISBN: " + this.ISBN + " anos\n";
		retorno += "Ano: " + this.ano + "\n";
		retorno += "Autor: " + this.autor + "\n";
		retorno += "Resenha: " + this.resenha + "\n";
		retorno += "Idade Indicada: " + idadeIndicada() + "\n";
		return retorno;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getResenha() {
		return resenha;
	}

	public void setResenha(String resenha) {
		this.resenha = resenha;
	}

}
