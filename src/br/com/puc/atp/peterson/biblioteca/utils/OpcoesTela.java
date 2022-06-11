package br.com.puc.atp.peterson.biblioteca.utils;
/*
 * ENUM criado para guardar as opções das telas em um Object
 */
public enum OpcoesTela {
	INICIAL(new Object[]{"1 - Cadastrar novo Livro", "2 - Exibir Livros", "3 - Limpar base de Livros",
			"4 - Gravar Livros", "5 - Carregar Livros", "6 - Sair"}),
	CADASTRAR(new Object[]{"1 - Livro Infantil","2 - Guia de Viagens","3 - Livro de Culinaria"});
    
	private Object[] opcoes;

	OpcoesTela(Object[] opcoes){
		 this.opcoes = opcoes;
	}

	public Object[] getOpcoes() {
		return opcoes;
	}

}
