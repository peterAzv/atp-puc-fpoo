package br.com.puc.atp.peterson.biblioteca;

/*
 * ATP - AULA FUNDAMENTOS DA PROGRAMAÇÃO ORIENTADA A OBJETOS - PUC 2022
 * ALUNOS:  
 * CARLOS EDUARDO DELGADO FERREIRA  
 * GABRIEL LUIZ BATISTA PASSOS 
 * PETERSON DE AZEVEDO
 */
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;

import br.com.puc.atp.peterson.biblioteca.bean.GuiaViagem;
import br.com.puc.atp.peterson.biblioteca.bean.Livro;
import br.com.puc.atp.peterson.biblioteca.bean.LivroCulinaria;
import br.com.puc.atp.peterson.biblioteca.bean.LivroInfantil;
import br.com.puc.atp.peterson.biblioteca.utils.OpcoesTela;

public class Biblioteca implements ListSelectionListener {
	private ArrayList<Livro> livros; // Armazenar a lista de livros
	/*
	 * Para exibição do conteudo em formato de listas
	 */
	DefaultListModel listModel = new DefaultListModel();
	JList list = new JList(listModel);

	public Biblioteca() {
		this.livros = new ArrayList<Livro>();
	}

	/*
	 * Carrega a tela inicial da biblioteca
	 */
	public void iniciar() {
		String titulo = "Biblioteca - ATP - PUC - FPOO"; // Define um titulo
		int opc, opc2; // 2 opções padroes
		do {
			String mensagem = "Bem vindo a Biblioteca\n Atualmente, " + livros.size()
					+ " Livro(s) cadastrado(s) \n\nEscolha uma das opções abaixo:"; // A mensagem padrao para caixa de
																					// dialogo
			// Carrega primeiro painel com uma mensagem e titulo definidos acima, define o
			// tipo ser uma mensagem comum, sem icone, e pega as opções de um ENUM, a opção
			// default é Sair
			opc = JOptionPane.showOptionDialog(null, mensagem, titulo, JOptionPane.PLAIN_MESSAGE,
					JOptionPane.INFORMATION_MESSAGE, null, OpcoesTela.INICIAL.getOpcoes(), "Sair");

			switch (opc) {
			case 0: // Caso a opção seja a primeira carrega uma nova tela de dialogo com a opção de
					// qual livro escolher
				opc2 = JOptionPane.showOptionDialog(null, mensagem, titulo, JOptionPane.PLAIN_MESSAGE,
						JOptionPane.INFORMATION_MESSAGE, null, OpcoesTela.CADASTRAR.getOpcoes(), "Sair");
				Livro livro = null; // Cria uma variavel livro aqui, para evitar codigo repetido dentro do switch
				switch (opc2) { // Define qual o tipo de livro a ser cadastrado
				case 0:
					livro = leLivroInfantil();
					break;
				case 1:
					livro = leGuiaViagem();
					break;
				case 2:
					livro = leLivroCulinaria();
					break;
				default:
					JOptionPane.showMessageDialog(null, "Tipo de livro para entrada NÃO escolhido!");
				}
				if (livro != null) // Se a entrada de livro retornar um objeto não nulo, ele adiciona a lista
					livros.add(livro);
				break;
			case 1: // Exibição dos dados
				if (livros.size() == 0) {
					JOptionPane.showMessageDialog(null, "Não existem livros cadastrados no momento");
					break;
				}
				// Cria uma nova lista e em seguida adiciona os dados nela
				listModel = new DefaultListModel();
				for (int i = 0; i < livros.size(); i++)
					listModel.addElement("Nome:" + livros.get(i).getNome() + "| Ano:" + livros.get(i).getAno());
				// Configurações da visualização da lista
				list = new JList(listModel);
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permite selecionar so 1
				list.setLayoutOrientation(JList.VERTICAL); // Exibição vertical
				list.setVisibleRowCount(10); // Padrao exibir 10
				list.addListSelectionListener(this); // Coloca o listener de click na lista.

				JScrollPane listScroller = new JScrollPane(list); // Configura o painel para lista
				listScroller.setPreferredSize(new Dimension(550, 580));
				JScrollPane listScrollPane = new JScrollPane(list);
				JOptionPane.showConfirmDialog(null, listScrollPane, "Selecione um livro para ver os detalhes",
						JOptionPane.OK_CANCEL_OPTION);
				break;
			case 2: // Limpar a lista
				if (livros.size() == 0) {
					JOptionPane.showMessageDialog(null, "Não existem livros cadastrados no momento");
					break;
				}
				livros.clear();
				JOptionPane.showMessageDialog(null, "Lista limpa com sucesso!");
				break;
			case 3: // Salvar Lista em disco
				if (livros.size() == 0) {
					JOptionPane.showMessageDialog(null, "Não existem livros cadastrados no momento");
					break;
				}
				salvarLivrosEmDisco(livros);
				JOptionPane.showMessageDialog(null, "Livros Salvos com sucesso!");
				break;
			case 4: // Recupera Dados
				livros = carregarLivros();
				if (livros.size() == 0) {
					JOptionPane.showMessageDialog(null, "Sem dados para apresentar.");
					break;
				}
				JOptionPane.showMessageDialog(null, "Livros carregados com sucesso!");
				break;
			default:
				JOptionPane.showMessageDialog(null, "Fim do aplicativo Biblioteca - ATP");
				opc = 5;
				break;
			}
		} while (opc != 5);
	}

	/*
	 * Metodo para salvar a lista de livros e disco
	 */
	public void salvarLivrosEmDisco(ArrayList<Livro> mamiferos) {
		ObjectOutputStream outputStream = null;
		try {
			JFileChooser f = new JFileChooser();
			f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Para escolher o diretorio
			f.showSaveDialog(null);
			outputStream = new ObjectOutputStream(new FileOutputStream(f.getSelectedFile() + "/biblioteca.dados"));
			for (int i = 0; i < livros.size(); i++)
				outputStream.writeObject(mamiferos.get(i));
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "Impossível criar arquivo!");
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/*
	 * Metodo utilizado para carregar os livros de um arquivo em disco
	 */
	public ArrayList<Livro> carregarLivros() {
		ArrayList<Livro> livrosTemp = new ArrayList<Livro>(); // Cria uma lista temporaria
		ObjectInputStream inputStream = null; // Um novo input Stream
		try {
			JFileChooser f = new JFileChooser(); // Uma nova instancia do dialogo para escolher o diretorio e arquivo
			f.setFileSelectionMode(JFileChooser.FILES_ONLY); // Para escolher o arquivo
			f.showSaveDialog(null); // Exibe a caixa de dialogo
			inputStream = new ObjectInputStream(new FileInputStream(f.getSelectedFile()));
			Object obj = null;
			while ((obj = inputStream.readObject()) != null) {
				if (obj instanceof Livro) {
					livrosTemp.add((Livro) obj);
				}
			}
		} catch (EOFException ex) {
			System.out.println("Fim de arquivo.");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "Arquivo com livros NÃO existe!");
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}

		}
		return livrosTemp;
	}

	/*
	 * Listener da Lista
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false)
			if (list.getSelectedIndex() != -1) // Se clicar em um dos itens da lista
				JOptionPane.showMessageDialog(null, livros.get(list.getSelectedIndex()).toString()); // Exibe um painel
																										// contendo o
																										// resultado do
																										// metodo
																										// toString()
	}

	/*
	 * Metodo para montar todo formulario em um so painel, evitando ter varios
	 * pequenos paineis para preencher
	 */
	public String[] montaFormulario(String[] dadosIn) throws ParseException {
		boolean isValido = true;
		String[] dadosOut = new String[dadosIn.length]; // Array de dados para saida
		ArrayList<JTextField> campos = new ArrayList<JTextField>(); // Uma lista de campos
		JPanel formulario = new JPanel(new GridLayout(0, 1)); // Define um painel novo e o layout na vertical
																// (gridlayout(0,1))
		for (int i = 0; i < dadosIn.length; i++) {
			formulario.add(new JLabel(dadosIn[i]));
			if (dadosIn[i].equals("ISBN")) { // Validação e mascara do campo ISBN
				JFormattedTextField campo = new JFormattedTextField(new MaskFormatter("###-#-##-######-#")); // Formatar
																												// campo
																												// ISBN
				campos.add(campo);
			} else if (dadosIn[i].equals("Ano")) { // Validação e mascara do campo Ano
				JFormattedTextField campo = new JFormattedTextField(new MaskFormatter("####")); // Formatar campo Ano, e
																								// evita a inserção de
																								// nao inteiros
				campos.add(campo);
			} else if (dadosIn[i].contains("?")) { // Como padrao deixei as label boolean com um ponto de interrogação
													// sinalizando que sera um checkbox
				formulario.remove(formulario.getComponentCount() - 1);
				JCheckBox checkBox = new JCheckBox(dadosIn[i], false);
				formulario.add(checkBox);
				JTextField campo = new JTextField(); // Mesmo tendo criado um checkbox acima, preciso criar um campo
														// invisivel para armazenar o valor e atribuir depois
				campo.setVisible(false);
				campos.add(campo);
			} else {
				JTextField campo = new JTextField(12);
				campos.add(campo);
			}
			formulario.add(campos.get(i));
		}

		int result = JOptionPane.showConfirmDialog(null, formulario, "Informe os dados do Livro:",
				JOptionPane.OK_CANCEL_OPTION); // Monta caixa de dialogo
		if (result == JOptionPane.OK_OPTION) { // Verifica se a opção pressionada foi OK
			int j = 0;
			for (int i = 0; i < formulario.getComponentCount() - 1; i++)
				if (formulario.getComponent(i) instanceof JTextField) { // Checa se component percorrido é do tipo campo
																		// de texto, para só assim tentar capturar o
																		// valor digitado
					dadosOut[j] = ((JTextField) formulario.getComponent(i)).getText();
					if ((dadosOut[j] == null || (dadosOut[j]).trim().equals("")) // Para validação de campos em branco
							&& formulario.getComponent(i).isVisible())
						isValido = false;
					j++;
				} else if (formulario.getComponent(i) instanceof JCheckBox) { // Caso seja do tipo checkbox sabemos que
																				// é um boolean e convertemos a resposta
					dadosOut[j] = ((JCheckBox) formulario.getComponent(i)).isSelected() ? "1" : "0";
					break;
				}
		} else { // Caso pressione outro botao que nao ok, cancela a entrada de dados
			JOptionPane.showMessageDialog(null, "Entrada de dados cancelada");
			return null; // Caso tenha fechado ou cancelado, retornar nulo
		}
		if (!isValido) { // Se o formulario for invalido (se houver cmapos em branco) ele manda digitar
							// novamente
			JOptionPane.showMessageDialog(null, "Campos em branco");
			dadosOut = montaFormulario(dadosIn);
		}
		return dadosOut;

	}

	/*
	 * Metodo que recebe uma lista de campos e envia para o formulario montar o
	 * painel
	 */
	public String[] lerValores(String[] nomeCampos) {
		String[] valores = new String[8];
		try {
			valores = montaFormulario(nomeCampos);
			return valores;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Livro leLivroInfantil() {
		String[] nomeCampos = { "Nome", "ISBN", "Ano", "Autor", "Resenha", "Brinde", "Possu Atividades?" }; // Lista de
																											// opcoes e
																											// campos
		String[] valores = lerValores(nomeCampos);
		if (valores != null) { // Se os valores vierem devidamente preenchidos cria uma nova instancia do tipo
								// de livro
			LivroInfantil livro = new LivroInfantil(valores[0], valores[1], Integer.parseInt(valores[2]), valores[3],
					valores[4], valores[5], valores[6].equals("1"));
			return livro;
		}
		return null;

	}

	public Livro leGuiaViagem() {
		String[] nomeCampos = { "Nome", "ISBN", "Ano", "Autor", "Resenha", "Local", "Possu Mapas?" }; // Lista de opcoes
																										// e campos
		String[] valores = lerValores(nomeCampos);
		if (valores != null) { // Se os valores vierem devidamente preenchidos cria uma nova instancia do tipo
								// de livro
			GuiaViagem livro = new GuiaViagem(valores[0], valores[1], Integer.parseInt(valores[2]), valores[3],
					valores[4], valores[5], valores[6].equals("1"));
			return livro;
		}
		return null;
	}

	public Livro leLivroCulinaria() {
		String[] nomeCampos = { "Nome", "ISBN", "Ano", "Autor", "Resenha", "Região Culinaria", "Possu Sobremesas?" }; // Lista
																														// de
																														// opcoes
																														// e
																														// campos
		String[] valores = lerValores(nomeCampos);
		if (valores != null) { // Se os valores vierem devidamente preenchidos cria uma nova instancia do tipo
								// de livro
			LivroCulinaria livro = new LivroCulinaria(valores[0], valores[1], Integer.parseInt(valores[2]), valores[3],
					valores[4], valores[5], valores[6].equals("1"));
			return livro;
		}
		return null;

	}

	/*
	 * Metodo principal
	 */
	public static void main(String[] args) {
		Biblioteca biblioteca = new Biblioteca(); // Instancia uma nova biblioteca
		biblioteca.iniciar(); // Inicia o programa
	}
}
