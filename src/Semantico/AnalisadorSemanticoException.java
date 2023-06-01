package Semantico;

import Lexico.Token;

public class AnalisadorSemanticoException {

	public void VariavelDeclaradaException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Semantico\n" + "ERRO na linha "
				+ lookAHead.getLinha() + "\n✖✖✖ Existe uma variavel com o mesmo nome ✖✖✖");
	}

	public void VariavelNaoDeclaradaException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Semantico\n" + "ERRO na linha "
				+ lookAHead.getLinha()+ "\n✖✖✖ Faltou declarar a variavel ✖✖✖");
	}

	public void TipoInvalidoIntException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Semantico\n" + "ERRO na linha "
				+ lookAHead.getLinha() + "\n✖✖✖ Tipo nao compativel com int ✖✖✖");
		
	}public void AtribuicaoException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Semantico\n" + "ERRO na linha "
				+ lookAHead.getLinha() + "\n✖✖✖ Faltou a atribuicao ✖✖✖");
	}

	public void TipoInvalidoCharException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Semantico\n" + "ERRO na linha "
				+ lookAHead.getLinha() + "\n✖✖✖ Tipo CHAR so e compativel com CHAR ✖✖✖");
	}

	public void ExpressaoRelacionalException(Token lookAHead) throws Exception {
		throw new Exception("\nErro Semantico\n" + "ERRO na linha "
				+ lookAHead.getLinha() + "\n✖✖✖  Tipos nao compativeis na expressao relacional ✖✖✖");
	}
}
