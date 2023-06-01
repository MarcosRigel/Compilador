package Lexico;

public class AnalisadorLexicoException extends Exception {
	public void CharException(int linha, String lexema) throws Exception {
		throw new Exception("\nErro lexico\n"
				+"linha "
				+ linha
				+ "\n✖✖✖ char mal formado! ✖✖✖");
	}

	public void FloatException(int linha, String lexema) throws Exception {
		throw new Exception("\nErro lexico\n" 
				+"linha " 
				+ linha 
				+ "\n✖✖✖ float mal formado! ✖✖✖");
	}

	public void DeferencaException(int linha, String lexema) throws Exception {
		throw new Exception("\nErro lexico\n"
				+"linha " 
				+ linha
				+ "\n✖✖✖ Atribuição mal formada ✖✖✖");
	}

	public void NotValidException(int linha, String lexema) throws Exception {
		throw new Exception("\nErro lexico\n"
				+"linha "
				+ linha 
				+ "\n✖✖✖ caractere invalido ✖✖✖");
	}

}
