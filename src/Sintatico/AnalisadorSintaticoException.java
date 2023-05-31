package Sintatico;

import Lexico.Token;

public class AnalisadorSintaticoException {

	public void IntException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Declara o int na main homi!");
	}

	public void MainException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Declara essa main homi!");
	}

	public void AbreParentesesException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : cade o '(' homi!");
	}

	public void FechaParentesesException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Cade o ')' homi!");
	}

	public void EoFException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Cade o fim do arquivo homi!");
	}

	public void AbreChaveException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Cade o '{' homi!");
	}

	public void FechaChaveException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + " ERRtO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Cade o '}' homi!");
	}

	public void IdentificadorException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Declara essa variável homi!");
	}

	public void PontoVirgulaException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " :Cade o ';' homi! ");
	}


	public void WhileException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Cade o 'while' depois do '}' homi!");
	}

	public void OperadorRelacionalException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Erro na operacao relacional !");
	}

	public void FatorException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SINTATICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Fator invalido !");
	}
}
