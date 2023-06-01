package Semantico;

import Lexico.Token;

public class AnalisadorSemanticoException {

	public void VariavelDeclaradaException(Token token) throws Exception {
		throw new Exception("\nErro Semantico\n"
				+ "linha "
				+ token.getLinha() 
				+ "\n✖✖✖ Existe uma variavel com o mesmo nome ✖✖✖");
	}

	public void VariavelNaoDeclaradaException(Token token) throws Exception {
		throw new Exception("\nErro Semantico\n"
				+ "linha "
				+ token.getLinha()
				+ "\n✖✖✖ Faltou declarar a variavel ✖✖✖");
	}

	public void TipoInvalidoIntException(Token token) throws Exception {
		throw new Exception("\nErro Semantico\n"
				+ "linha "
				+ token.getLinha() 
				+ "\n✖✖✖ Tipo nao compativel com int ✖✖✖");
		
	}public void AtribuicaoException(Token token) throws Exception {
		throw new Exception("\nErro Semantico\n" 
				+ "linha "
				+ token.getLinha() 
				+ "\n✖✖✖ Faltou a atribuicao ✖✖✖");
	}

	public void TipoInvalidoCharException(Token token) throws Exception {
		throw new Exception("\nErro Semantico\n" 
				+ "linha "
				+ token.getLinha() 
				+ "\n✖✖✖ Tipo CHAR so e compativel com CHAR ✖✖✖");
	}

	public void ExpressaoRelacionalException(Token token) throws Exception {
		throw new Exception("\nErro Semantico\n" 
				+ "linha "
				+ token.getLinha() 
				+ "\n✖✖✖  Tipos nao compativeis na expressao relacional ✖✖✖");
	}
}
