package Sintatico;

import Lexico.Token;

public class AnalisadorSintaticoException {

	public void IntException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha " + lookAHead.getLinha()
				+ "\n✖✖✖ Faltou declarar o int na assinatura do metodo principal ✖✖✖");
	}

	public void MainException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha " + lookAHead.getLinha() 
				+ "\n✖✖✖ Faltou declarar a main na assinatura do metodo principal ✖✖✖");
	}

	public void AbreParentesesException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha " + lookAHead.getLinha()
				+ "\n✖✖✖ Faltou declarar '(' ✖✖✖");
	}

	public void FechaParentesesException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha " + lookAHead.getLinha()
				+ "\n✖✖✖ Faltou declarar ')' ✖✖✖");
	}

	public void EoFException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha " + lookAHead.getLinha() 
				+ "\n✖✖✖ Faltou o fim do arquivo! ERRO EOF ✖✖✖");
	}

	public void AbreChaveException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha " + lookAHead.getLinha()
				+ "\n✖✖✖ Faltou declarar '{' ✖✖✖");
	}

	public void FechaChaveException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha "+ lookAHead.getLinha()
				+ "\n✖✖✖ Faltou declarar '}' ✖✖✖");
	}

	public void IdentificadorException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha "+ lookAHead.getLinha()
				+ "\n✖✖✖ Faltou declarar a variavel ✖✖✖");
	}

	public void PontoVirgulaException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha "+ lookAHead.getLinha()
				+ "\n✖✖✖ faltou declarar ';' ✖✖✖");
	}


	public void WhileException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha " + lookAHead.getLinha()
				+ "\n✖✖✖ depois do '}' faltou declarar o 'while' ✖✖✖");
	}

	public void OperadorRelacionalException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha " + lookAHead.getLinha()
				+ "\n✖✖✖ Erro na operacao relacional ✖✖✖");
	}

	public void FatorException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Sintatico\n" + "ERRO na linha " + lookAHead.getLinha()
			   + "\n✖✖✖ invalido ✖✖✖");
	}
}
