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
		String titulo = "Biblioteca - ATP - PUC - FPOO";
		String mensagem = "Bem vindo a Biblioteca\n Escolha uma das opções abaixo:";
		int opc, opc2;
		do {
			opc = JOptionPane.showOptionDialog(null, mensagem, titulo, JOptionPane.PLAIN_MESSAGE,
					JOptionPane.INFORMATION_MESSAGE, null, OpcoesTela.INICIAL.getOpcoes(), "Sair");
			switch (opc) {

			case 0: // Entrada de dados
				opc2 = JOptionPane.showOptionDialog(null, mensagem, titulo, JOptionPane.PLAIN_MESSAGE,
						JOptionPane.INFORMATION_MESSAGE, null, OpcoesTela.CADASTRAR.getOpcoes(), "Sair");
				switch (opc2) {
				case 0:
					livros.add((Livro) leLivroInfantil());
					break;
				case 1:
					livros.add((Livro) leGuiaViagem());
					break;
				case 2:
					livros.add((Livro) leLivroCulinaria());
					break;
				default:
					JOptionPane.showMessageDialog(null, "Tipo de livro para entrada NÃO escolhido!");
				}
				break;
			case 1: // Exibição dos dados
				if (livros.size() == 0) {
					JOptionPane.showMessageDialog(null, "Não existem livros cadastrados no momento");
					break;
				}
				// Cria uma nova lista e em seguida adiciona os dados nela
				listModel = new DefaultListModel();
				for (int i = 0; i < livros.size(); i++)
					listModel.addElement("Nome: " + livros.get(i).getNome() + "| Ano:" + livros.get(i).getAno());
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
				break;
			}
		} while (opc != 5);
	}

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

	public ArrayList<Livro> carregarLivros() {
		ArrayList<Livro> livrosTemp = new ArrayList<Livro>();

		ObjectInputStream inputStream = null;
		try {
			JFileChooser f = new JFileChooser();
			f.setFileSelectionMode(JFileChooser.FILES_ONLY); // Para escolher o arquivo
			f.showSaveDialog(null);
			inputStream = new ObjectInputStream(new FileInputStream(f.getSelectedFile()));
			Object obj = null;
			while ((obj = inputStream.readObject()) != null) {
				if (obj instanceof Livro) {
					livrosTemp.add((Livro) obj);
				}
			}
		} catch (EOFException ex) { // when EOF is reached
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
			if (list.getSelectedIndex() != -1)
				JOptionPane.showMessageDialog(null, livros.get(list.getSelectedIndex()).toString());
	}

	/*
	 * Metodo para montar todo formulario em um so painel, evitando ter varios
	 * pequenos paineis para preencher
	 */
	public String[] montaFormulario(String[] dadosIn) throws ParseException {
		String[] dadosOut = new String[dadosIn.length];
		ArrayList<JTextField> campos = new ArrayList<JTextField>();
		JPanel formulario = new JPanel(new GridLayout(0, 1));
		for (int i = 0; i < dadosIn.length; i++) {
			formulario.add(new JLabel(dadosIn[i]));
			if (dadosIn[i].equals("ISBN")) {
				JFormattedTextField campo = new JFormattedTextField(new MaskFormatter("###-#-##-######-#")); // Formatar
																												// campo
																												// ISBN
				campos.add(campo);
			} else if (dadosIn[i].equals("Ano")) {
				JFormattedTextField campo = new JFormattedTextField(new MaskFormatter("####")); // Formatar campo Ano, e
																								// evita a inserção de
																								// nao inteiros
				campos.add(campo);
			} else if (dadosIn[i].contains("?")) {
				formulario.remove(formulario.getComponentCount() - 1);
				JCheckBox checkBox = new JCheckBox(dadosIn[i], false);
				formulario.add(checkBox);
				JTextField campo = new JTextField();
				campo.setVisible(false);
				campos.add(campo);
			} else {
				JTextField campo = new JTextField(12);
				campos.add(campo);
			}
			formulario.add(campos.get(i));
		}

		int result = JOptionPane.showConfirmDialog(null, formulario, "Informe os dados do Livro:",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			int j = 0;
			for (int i = 0; i < formulario.getComponentCount() - 1; i++)
				if (formulario.getComponent(i) instanceof JTextField) {
					dadosOut[j] = ((JTextField) formulario.getComponent(i)).getText();
					j++;
				} else if (formulario.getComponent(i) instanceof JCheckBox) {
					dadosOut[j] = ((JCheckBox) formulario.getComponent(i)).isSelected() ? "1" : "0";
					break;
				}

		} else {
			montaFormulario(dadosIn);
		}
		return dadosOut;
	}

	public String[] lerValores(String[] nomeCampos) {
		String[] valores = new String[8];
		try {
			valores = montaFormulario(nomeCampos);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (valores[2] != null && valores[2].trim().equals(""))
			valores[2] = "1970";		
		return valores;
	}

	public Livro leLivroInfantil() {
		String[] nomeCampos = { "Nome", "ISBN", "Ano", "Autor", "Resenha", "Brinde", "Possu Atividades?" };
		String[] valores = lerValores(nomeCampos);
		LivroInfantil livro = new LivroInfantil(valores[0], valores[1], Integer.parseInt(valores[2]), valores[3],
				valores[4], valores[5], valores[6].equals("1"));
		return livro;
	}

	public Livro leGuiaViagem() {
		String[] nomeCampos = { "Nome", "ISBN", "Ano", "Autor", "Resenha", "Local", "Possu Mapas?" };
		String[] valores = lerValores(nomeCampos);
		GuiaViagem livro = new GuiaViagem(valores[0], valores[1], Integer.parseInt(valores[2]), valores[3], valores[4],
				valores[5], valores[6].equals("1"));
		return livro;
	}

	public Livro leLivroCulinaria() {
		String[] nomeCampos = { "Nome", "ISBN", "Ano", "Autor", "Resenha", "Região Culinaria", "Possu Sobremesas?" };
		String[] valores = lerValores(nomeCampos);
		LivroCulinaria livro = new LivroCulinaria(valores[0], valores[1], Integer.parseInt(valores[2]), valores[3],
				valores[4], valores[5], valores[6].equals("1"));
		return livro;
	}

	public static void main(String[] args) {
		Biblioteca biblioteca = new Biblioteca();
		biblioteca.iniciar();
	}
}
