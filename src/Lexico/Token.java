package Lexico;

public class Token {
	private int linha;
	private int coluna;
	private Enum gramatica;
	private String lexema;
	
	public Token() {
		this.linha = 0;
		this.coluna = 0;
	}
	public Token(Enum gramatica, String lexema, int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
		this.gramatica = gramatica;
		this.lexema = lexema;
	}

	
	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	public Enum getGramatica() {
		return gramatica;
	}

	public String getLexema() {
		return lexema;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}

	public void setGramatica(Enum gramatica) {
		this.gramatica = gramatica;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	@Override
	public String toString() {
		return "TOKEN | " + gramatica + " = " + lexema;
	}
	public void exibirInformacoes() {
		System.out.println("Lexema: "+getLexema()+"\nLinha: " +getLinha()+"\nColuna: "+getColuna() + "\\nDescricao: "+getGramatica());
	}
	public static Enum isPalavraReservada(String lexema) {
		switch (lexema) {
		case "main":
			return Gramatica.PALAVRA_RESERVADA_MAIN;
		case "int":
			return Gramatica.PALAVRA_RESERVADA_INT;
		case "float":
			return Gramatica.PALAVRA_RESERVADA_FLOAT;
		case "char":
			return Gramatica.PALAVRA_RESERVADA_CHAR;
		case "if":
			return Gramatica.PALAVRA_RESERVADA_IF;
		case "else":
			return Gramatica.PALAVRA_RESERVADA_ELSE;
		case "while":
			return Gramatica.PALAVRA_RESERVADA_WHILE;
		case "do":
			return Gramatica.PALAVRA_RESERVADA_DO;
		case "for":
			return Gramatica.PALAVRA_RESERVADA_FOR;
		default:
			return Gramatica.IDENTIFICADOR;
		}

	}
}
