package Semantico;

import Lexico.Token;

public class AnalisadorSemanticoException {

	public void VariavelDeclaradaException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SEMANTICO]\n" + "Oxe, tem algo errado ai : " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ", \nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Existe uma variavel de mesmo nome declarada.");
	}

	public void VariavelNaoDeclaradaException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SEMANTICO]\n" + "Oxe, tem algo errado ai : " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ", \nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Declara essa variavel homi!");
	}

	public void TipoInvalidoIntException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SEMANTICO]\n" + "Oxe, tem algo errado ai : " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ", \nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Tipo nao compativel com INT");
		
	}public void AtribuicaoException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SEMANTICO]\n" + "Oxe esquecesse de nada não? " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ",\nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Atribui isso direito!");
	}

	public void TipoInvalidoCharException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SEMANTICO]\n" + "Oxe, tem algo errado ai : " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ", \nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Tipo CHAR so e compativel com CHAR");
	}

	public void ExpressaoRelacionalException(Token lookAHead) throws Exception {
		throw new Exception("\n[ERRO SEMANTICO]\n" + "Oxe, tem algo errado ai : " + "ERRO na linha "
				+ lookAHead.getLinha() + ", coluna " + lookAHead.getColuna() + ", \nlogo ali pertinho de: "
				+ lookAHead.getLexema() + " : Tipos nao compativeis na expressao relacional");
	}
}
