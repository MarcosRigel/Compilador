package Sintatico;

import Lexico.Token;

public class AnalisadorSintaticoException {

	public void IntException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha "
				+ token.getLinha()
				+ "\n✖✖✖ Faltou declarar o int na assinatura do metodo principal ✖✖✖");
	}

	public void MainException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha "
				+ token.getLinha() 
				+ "\n✖✖✖ Faltou declarar a main na assinatura do metodo principal ✖✖✖");
	}

	public void AbreParentesesException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha " 
				+ token.getLinha()
				+ "\n✖✖✖ Faltou declarar '(' ✖✖✖");
	}

	public void FechaParentesesException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha "
				+ token.getLinha()
				+ "\n✖✖✖ Faltou declarar ')' ✖✖✖");
	}

	public void EoFException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha " 
				+ token.getLinha() 
				+ "\n✖✖✖ Faltou o fim do arquivo! ERRO EOF ✖✖✖");
	}

	public void AbreChaveException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha "
				+ token.getLinha()
				+ "\n✖✖✖ Faltou declarar '{' ✖✖✖");
	}

	public void FechaChaveException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha "
				+ token.getLinha()
				+ "\n✖✖✖ Faltou declarar '}' ✖✖✖");
	}

	public void IdentificadorException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha "
				+ token.getLinha()
				+ "\n✖✖✖ Faltou declarar a variavel ✖✖✖");
	}

	public void PontoVirgulaException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha "
				+ token.getLinha()
				+ "\n✖✖✖ faltou declarar ';' ✖✖✖");
	}


	public void WhileException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha " 
				+ token.getLinha()
				+ "\n✖✖✖ depois do '}' faltou declarar o 'while' ✖✖✖");
	}

	public void OperadorRelacionalException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha " 
				+ token.getLinha()
				+ "\n✖✖✖ Erro na operacao relacional ✖✖✖");
	}

	public void FatorException(Token token) throws Exception {
		throw new Exception("\nErro Sintatico\n" 
				+ "linha " 
				+ token.getLinha()
			    + "\n✖✖✖ invalido ✖✖✖");
	}
}
