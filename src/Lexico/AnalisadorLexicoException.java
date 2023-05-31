package Lexico;

public class AnalisadorLexicoException extends Exception {
	// ERRO CHAR
	public void CharException(int linha, int coluna, String lexema) throws Exception {
		throw new Exception("\n[ERRO LEXICO]"+"\nERRO na linha " + linha + ", coluna " + coluna + ", ultimo token lido: " + lexema
				+ " :char mal formado!");
	}

	// ERRO FLOAT
	public void FloatException(int linha, int coluna, String lexema) throws Exception {
		throw new Exception("\n[ERRO LEXICO]"+"\nERRO na linha " + linha + ", coluna " + coluna + ", ultimo token lido: " + lexema
				+ " :float mal formado!");
	}

	// ERRO DIFERENÇA
	public void DeferencaException(int linha, int coluna, String lexema) throws Exception {
		throw new Exception("\n[ERRO LEXICO]"+"\nERRO na linha " + linha + ", coluna " + coluna + ", ultimo token lido: " + lexema
				+ " :diferenca mal formada!");
	}

	// ERRO CARACTERE INVALIDO
	public void NotValidException(int linha, int coluna, String lexema) throws Exception {
		throw new Exception("\n[ERRO LEXICO]"+"\nERRO na linha " + linha + ", coluna " + coluna + ", ultimo token lido: " + lexema
				+ " :caractere invalido!");
	}

}


