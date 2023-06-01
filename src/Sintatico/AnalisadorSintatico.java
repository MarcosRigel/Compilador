package Sintatico;

import java.io.FileReader;

import Lexico.AnalisadorLexico;
import Lexico.Token;
import Lexico.Gramatica;
import Semantico.AnalisadorSemanticoException;
import Semantico.Tabela;
import Semantico.Variavel;


public class AnalisadorSintatico {
	private AnalisadorLexico analisadorLexico;
	private AnalisadorSintaticoException exception;
	private AnalisadorSemanticoException analisadorSemanticoException;
	private FileReader arquivo;
	private Token token;
	private Tabela tabela;
	private Integer bloco = 0;

	public AnalisadorSintatico(FileReader arquivo) {
		this.arquivo = arquivo;
		this.analisadorLexico = new AnalisadorLexico(arquivo);
		this.exception = new AnalisadorSintaticoException();
		this.analisadorSemanticoException = new AnalisadorSemanticoException();
		this.tabela = new Tabela();
	}
	private void nextToken() throws Exception {
		token = AnalisadorLexico.nextToken();
	}

	private boolean comandoAux() {
		if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_DO) {
			return true;
		} else if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_WHILE) {
			return true;
		} else if (token.getGramatica() == Gramatica.IDENTIFICADOR) {
			return true;
		} else if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_IF) {
			return true;
		} else if (token.getGramatica() == Gramatica.CARACTER_ESPECIAL_ABRECHAVE) {
			return true;
		}
		return false;
	}

	private boolean comandoAuxBasico() {

		if (token.getGramatica() == Gramatica.IDENTIFICADOR) {
			return true;
		} else if (token.getGramatica() == Gramatica.CARACTER_ESPECIAL_ABRECHAVE) {
			return true;
		}
		return false;
	}

	public void comandoBasico() throws Exception {
		// <comando_basico> ::= <atribuicao> | <bloco>

		if (token.getGramatica() == Gramatica.CARACTER_ESPECIAL_ABRECHAVE) {
			bloco();

		} else if (token.getGramatica() == Gramatica.IDENTIFICADOR) {
			if (!tabela.contains(token)) {
				analisadorSemanticoException.VariavelNaoDeclaradaException(token);
			}
			atribuicao();
		}
	}

	private void comando() throws Exception {
		// <comando>  <comando_basico> | <iteracao> | if "("<expr_relacional>")"
		// <comando> {else <comando>}?

		if (comandoAuxBasico()) {
			comandoBasico();

		} else if (iteracaoAux()) {
			iteracao();

		} else if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_IF) {
			nextToken();

			if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_ABREPARENTESES) {
				exception.AbreParentesesException(token);
			}
			nextToken();

			Variavel expRelacional = expressaoRelacional();

			String label = Label.NovoLabel();
			
			if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES) {
				exception.FechaParentesesException(token);
			}
			nextToken();

			String label2 = Label.NovoLabel();
			comando();

			if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_ELSE) {
				nextToken();
			comando();
			}
			
			
		}
	}

	private boolean declaracaoVariaveisAux() {

		if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_INT) {
			return true;
		} else if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_FLOAT) {
			return true;
		} else if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_CHAR) {
			return true;
		}
		return false;
	}

	private void declaracaoVariaveis() throws Exception {

		Token tipoDeclaracao = token;

		nextToken();

		if (token.getGramatica() != Gramatica.IDENTIFICADOR) {
			exception.IdentificadorException(token);
		}

		// Adiciona uma variavel a tabela de variaveis declaradas
		Variavel variavel = new Variavel(token.getLexema(), tipoDeclaracao.getGramatica(), bloco, token);

		if (tabela.exist(variavel)) {
			this.analisadorSemanticoException.VariavelDeclaradaException(variavel.getToken());
		}

		this.tabela.empilha(variavel);

		nextToken();

		if (token.getGramatica() == Gramatica.CARACTER_ESPECIAL_VIRGULA) {
			while (token.getGramatica() == Gramatica.CARACTER_ESPECIAL_VIRGULA) {
				nextToken();

				if (token.getGramatica() != Gramatica.IDENTIFICADOR) {
					exception.IdentificadorException(token);
				}
				// Adiciona uma variavel a tabela de variaveis declaradas
				variavel = new Variavel(token.getLexema(), tipoDeclaracao.getGramatica(), bloco, token);
				this.tabela.empilha(variavel);

				nextToken();
			}
		}
		if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_PONTOVIRGULA) {
			exception.PontoVirgulaException(token);
		}
		nextToken();
	}

	private boolean iteracaoAux() {
		if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_WHILE) {
			return true;

		} else if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_DO) {
			return true;
		}
		return false;
	}

	public void iteracao() throws Exception {
		// <itera��o> ::= while "("<expr_relacional>")" <comando> | do <comando> while
		// "("<expr_relacional>")"";"

		if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_WHILE) {
			nextToken();

			if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_ABREPARENTESES) {
				exception.AbreParentesesException(token);
			}
			nextToken();

			String label = Label.NovoLabel();
			
			Variavel expRelacional = expressaoRelacional();

			String label2 = Label.NovoLabel();
			
			if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES) {
				exception.FechaParentesesException(token);
			}
			nextToken();
			comando();

		} else if (token.getGramatica() == Gramatica.PALAVRA_RESERVADA_DO) {
			nextToken();

			String label = Label.NovoLabel();

			
			comando();

			if (token.getGramatica() != Gramatica.PALAVRA_RESERVADA_WHILE) {
				exception.WhileException(token);
			}
			nextToken();

			if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_ABREPARENTESES) {
				exception.AbreParentesesException(token);
			}
			nextToken();

			Variavel expRelacional = expressaoRelacional();

			String label2 = Label.NovoLabel();
			if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES) {
				exception.FechaParentesesException(token);
			}
			nextToken();

			if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_PONTOVIRGULA) {
				exception.PontoVirgulaException(token);
			}
			nextToken();
		}

	}

	public void parser() { 
		// <programa> / int main() <bloco>
		try {
			nextToken();

			if (token.getGramatica() != Gramatica.PALAVRA_RESERVADA_INT) {
				exception.IntException(token);
			}
			nextToken();

			if (token.getGramatica() != Gramatica.PALAVRA_RESERVADA_MAIN) {
				exception.MainException(token);
			}
			nextToken();

			if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_ABREPARENTESES) {
				exception.AbreParentesesException(token);
			}
			nextToken();

			if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES) {
				exception.FechaParentesesException(token);
			}
			nextToken();

			bloco();

			if (token.getGramatica() != Gramatica.EOF) {
				exception.EoFException(token);
			}
			else {
				System.out.println("Colera do Dragao !");
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void bloco() throws Exception { 
		 //<bloco>  "{" {<decl_var>}* {<comando>}* "}" 
		if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_ABRECHAVE) {
			exception.AbreChaveException(token);
		}
		nextToken();
		this.bloco += 1;

		while (declaracaoVariaveisAux()) {
			declaracaoVariaveis();
		}

		while (comandoAux()) {
			comando();
		}

		if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHACHAVE) {
			exception.FechaChaveException(token);
		}
		nextToken();

		this.tabela.desempilha(bloco);
		this.bloco -= 1;
	}

	public void atribuicao() throws Exception {
		if (token.getGramatica() == Gramatica.IDENTIFICADOR) {
			if (!tabela.contains(token)) {
				analisadorSemanticoException.VariavelNaoDeclaradaException(token);
			}
			Variavel operando1 = tabela.get(token, bloco);
			nextToken();

			if (token.getGramatica() != Gramatica.OPERADOR_ARITMETICO_ATRIBUICAO) {
				analisadorSemanticoException.AtribuicaoException(token);
			}
			nextToken();

			Variavel operando2 = expressaoAritmeticaAux();

			
			if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {
				if (!operando2.getTipo().equals(Gramatica.TIPOINT)
						&& !operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {

					analisadorSemanticoException.TipoInvalidoIntException(operando2.getToken());
				}
			} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {
				if (operando2.getTipo().equals(Gramatica.TIPOINT)
						|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {

					Variavel variavel = new Variavel(operando2.getLexema(), operando2.getTipo(),
							operando2.getBlocoDeDeclaracao(), operando2.getToken());
					String temp = Label.NovaTemp();
					cast(variavel);
					variavel.setLexema(temp);

					
				
				}
			} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)) {
				if (!operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						&& !operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
				}
				
			}

			if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_PONTOVIRGULA) {
				exception.PontoVirgulaException(token);
			}
			nextToken();
		}
	}

	public Variavel expressaoRelacional() throws Exception {
		// <expr_relacional> ::= <expr_arit> <op_relacional> <expr_arit>

		Variavel operando1 = expressaoAritmeticaAux();

		Token opRelacional = operadorRelacional();

		Variavel operando2 = expressaoAritmeticaAux();

		if (operando1.getTipo().equals(Gramatica.TIPOCHAR)
				|| operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)) {

			if (!operando2.getTipo().equals(Gramatica.TIPOCHAR)
					&& !operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)) {

				analisadorSemanticoException.ExpressaoRelacionalException(operando2.getToken());
			}
		} else {
			if (!operando1.getTipo().equals(Gramatica.TIPOCHAR)
					&& !operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)) {

				if (operando2.getTipo().equals(Gramatica.TIPOCHAR)
						|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)) {

					analisadorSemanticoException.ExpressaoRelacionalException(operando2.getToken());
				}
			}
		}
		String temp = Label.NovaTemp();
		operando1.setLexema(temp);
		return operando1;
	}

	public Variavel expressaoAritmeticaAux() throws Exception {
		Variavel operando1 = termoAux();
		Token operador = token;
		Variavel operando2 = expressaoAritmetica();
		Variavel variavel = null;

		if (operando2 != null) {

			if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)
					|| operando1.getTipo().equals(Gramatica.TIPOINT)) {

				if (operando2.getTipo().equals(Gramatica.TIPOFLOAT)
						|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {

					variavel = new Variavel(operando1.getLexema(), operando1.getTipo(),
							operando1.getBlocoDeDeclaracao(), operando2.getToken());
					String temp = Label.NovaTemp();
					
					cast(variavel);
					variavel.setLexema(temp);

					String temp2 = Label.NovaTemp();
					variavel.setLexema(temp2);

					return variavel;

				} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());

				} else {
					String temp = Label.NovaTemp();
					operando2.setLexema(temp);
					return operando2;
				}
			} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)
					|| operando1.getTipo().equals(Gramatica.TIPOFLOAT)) {

				if (operando2.getTipo().equals(Gramatica.TIPOINT)
						|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {

					variavel = new Variavel(operando2.getLexema(), operando2.getTipo(),
							operando2.getBlocoDeDeclaracao(), operando2.getToken());
					String temp = Label.NovaTemp();
				
					cast(variavel);
					variavel.setLexema(temp);

					String temp2 = Label.NovaTemp();
					variavel.setLexema(temp2);

					return variavel;

				} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
				} else {
					String temp = Label.NovaTemp();
					operando2.setLexema(temp);
					return operando2;
				}
			} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
					|| operando1.getTipo().equals(Gramatica.TIPOCHAR)) {

				if (!operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						&& !operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
				}
			}
			String temp = Label.NovaTemp();
			operando2.setLexema(temp);
			return operando2;
		}
		return operando1;
	}

	public Variavel expressaoAritmetica() throws Exception {
		Variavel variavel = null;

		if (token.getGramatica() == Gramatica.OPERADOR_ARITMETICO_SOMA) {
			nextToken();
			Variavel operando1 = termoAux();
			Token operador = token;
			Variavel operando2 = expressaoAritmetica();

			if (operando2 != null) {

				if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)
						|| operando1.getTipo().equals(Gramatica.TIPOINT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOFLOAT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {

						variavel = new Variavel(operando1.getLexema(), operando1.getTipo(),
								operando1.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						operando2.setLexema(temp);
						return operando2;
					}
				} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)
						|| operando1.getTipo().equals(Gramatica.TIPOFLOAT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOINT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {

						variavel = new Variavel(operando2.getLexema(), operando2.getTipo(),
								operando2.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						operando2.setLexema(temp);
						return operando2;
					}
				} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando1.getTipo().equals(Gramatica.TIPOCHAR)) {

					if (!operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							&& !operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					}
				}
				String temp = Label.NovaTemp();
				operando2.setLexema(temp);
				return operando2;
			}
			return operando1;

		} else if (token.getGramatica() == Gramatica.OPERADOR_ARITMETICO_SUBTRACAO) {
			nextToken();
			Variavel operando1 = termoAux();
			Token operador = token;
			Variavel operando2 = expressaoAritmetica();

			if (operando2 != null) {

				if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)
						|| operando1.getTipo().equals(Gramatica.TIPOINT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOFLOAT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {

						variavel = new Variavel(operando1.getLexema(), operando1.getTipo(),
								operando1.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						operando2.setLexema(temp);
						return operando2;
					}
				} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)
						|| operando1.getTipo().equals(Gramatica.TIPOFLOAT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOINT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {

						variavel = new Variavel(operando2.getLexema(), operando2.getTipo(),
								operando2.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						operando2.setLexema(temp);
						return operando2;
					}
				} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando1.getTipo().equals(Gramatica.TIPOCHAR)) {

					if (!operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							&& !operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					}
				}
				String temp = Label.NovaTemp();
				operando2.setLexema(temp);
				return operando2;
			}
			return operando1;
		}
		return null;
	}

	public Variavel termoAux() throws Exception {
		Variavel operando1 = fator();
		Token operador = token; 
		Variavel operando2 = termo();
		Variavel variavel = null;

		if (operando2 != null) {

			if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)
					|| operando1.getTipo().equals(Gramatica.TIPOINT)) {

				if (operando2.getTipo().equals(Gramatica.TIPOFLOAT)
						|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {

					variavel = new Variavel(operando1.getLexema(), operando1.getTipo(),
							operando1.getBlocoDeDeclaracao(), operando2.getToken());
					String temp = Label.NovaTemp();
					cast(variavel);
					variavel.setLexema(temp);

					String temp2 = Label.NovaTemp();
					variavel.setLexema(temp2);

					return variavel;

				} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
				} else {
					String temp = Label.NovaTemp();
					operando2.setLexema(temp);
					return operando2;
				}
			} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)
					|| operando1.getTipo().equals(Gramatica.TIPOFLOAT)) {

				if (operando2.getTipo().equals(Gramatica.TIPOINT)
						|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {

					variavel = new Variavel(operando2.getLexema(), operando2.getTipo(),
							operando2.getBlocoDeDeclaracao(), operando2.getToken());
					String temp = Label.NovaTemp();
					cast(variavel);
					variavel.setLexema(temp);

					String temp2 = Label.NovaTemp();
					variavel.setLexema(temp2);

					return variavel;

				} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
				} else {
					String temp = Label.NovaTemp();
					operando2.setLexema(temp);
					return operando2;
				}
			} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
					|| operando1.getTipo().equals(Gramatica.TIPOCHAR)) {

				if (!operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						&& !operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
				}
			}
			String temp = Label.NovaTemp();
			operando2.setLexema(temp);
			return operando2;
		}

		return operando1;
	}

	public Variavel termo() throws Exception {
		Variavel variavel = null;

		if (token.getGramatica() == Gramatica.OPERADOR_ARITMETICO_MULTIPLICACAO) {
			nextToken();
			Variavel operando1 = termoAux();
			Token operador = token; 
			Variavel operando2 = termo();

			if (operando2 != null) {

				if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)
						|| operando1.getTipo().equals(Gramatica.TIPOINT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOFLOAT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {

						variavel = new Variavel(operando1.getLexema(), operando1.getTipo(),
								operando1.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						operando2.setLexema(temp);
						return operando2;
					}
				} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)
						|| operando1.getTipo().equals(Gramatica.TIPOFLOAT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOINT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {

						variavel = new Variavel(operando2.getLexema(), operando2.getTipo(),
								operando2.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						operando2.setLexema(temp);
						return operando2;
					}
				} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando1.getTipo().equals(Gramatica.TIPOCHAR)) {

					if (!operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							&& !operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					}
				}
				String temp = Label.NovaTemp();
				operando2.setLexema(temp);
				return operando2;
			}

			return operando1;

		} else if (token.getGramatica() == Gramatica.OPERADOR_ARITMETICO_DIVISAO) {
			nextToken();
			Variavel operando1 = termoAux();
			Token operador = token; 
			Variavel operando2 = termo();

			if (operando2 != null) {

				if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)
						|| operando1.getTipo().equals(Gramatica.TIPOINT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOFLOAT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {

						variavel = new Variavel(operando1.getLexema(), operando1.getTipo(),
								operando1.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						operando2.setLexema(temp);
						return operando2;
					}
				} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)
						|| operando1.getTipo().equals(Gramatica.TIPOFLOAT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOINT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {

						variavel = new Variavel(operando2.getLexema(), operando2.getTipo(),
								operando2.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						operando2.setLexema(temp);
						return operando2;
					}
				} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando1.getTipo().equals(Gramatica.TIPOCHAR)) {

					if (!operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							&& !operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					}
				}
				String temp = Label.NovaTemp();
				operando2.setLexema(temp);
				return operando2;
			}
			return operando1;
		}
		return null;
	}

	public Token operadorRelacional() throws Exception {

		if (token.getGramatica() == Gramatica.OPERADOR_RELACIONAL_MAIOR) {
			Token opRelacional = token;
			nextToken();
			return opRelacional;

		} else if (token.getGramatica() == Gramatica.OPERADOR_RELACIONAL_MAIORIGUAL) {
			Token opRelacional = token;
			nextToken();
			return opRelacional;

		} else if (token.getGramatica() == Gramatica.OPERADOR_RELACIONAL_MENOR) {
			Token opRelacional = token;
			nextToken();
			return opRelacional;

		} else if (token.getGramatica() == Gramatica.OPERADOR_RELACIONAL_MENORIGUAL) {
			Token opRelacional = token;
			nextToken();
			return opRelacional;

		} else if (token.getGramatica() == Gramatica.OPERADOR_RELACIONAL_IGUAL) {
			Token opRelacional = token;
			nextToken();
			return opRelacional;

		} else if (token.getGramatica() == Gramatica.OPERADOR_RELACIONAL_DIFERENCA) {
			Token opRelacional = token;
			nextToken();
			return opRelacional;

		} else {
			exception.OperadorRelacionalException(token);
		}
		return null;
	}

	public Variavel fator() throws Exception {
		//<fator> ::= "(" <expr_arit> ")" | <id> | <real> | <inteiro> | <char>

		if (token.getGramatica() == Gramatica.CARACTER_ESPECIAL_ABREPARENTESES) {
			nextToken();
			Variavel variavel = expressaoAritmeticaAux();

			if (token.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES) {
				exception.FechaParentesesException(token);
			}
			nextToken();
			return variavel;

		} else if (token.getGramatica() == Gramatica.IDENTIFICADOR) {
			if (!tabela.contains(token)) {
				analisadorSemanticoException.VariavelNaoDeclaradaException(token);
			}
			Variavel v = tabela.get(token, bloco);
			Variavel variavel = new Variavel(token.getLexema(), v.getTipo(), v.getBlocoDeDeclaracao(), token);
			nextToken();
			return variavel;

		} else if (token.getGramatica() == Gramatica.TIPOFLOAT) {
			Variavel variavel = new Variavel(token.getLexema(), Gramatica.TIPOFLOAT, this.bloco, token);
			nextToken();
			return variavel;

		} else if (token.getGramatica() == Gramatica.TIPOINT) {
			Variavel variavel = new Variavel(token.getLexema(), Gramatica.TIPOINT, this.bloco, token);
			nextToken();
			return variavel;

		} else if (token.getGramatica() == Gramatica.TIPOCHAR) {
			Variavel variavel = new Variavel(token.getLexema(), Gramatica.TIPOCHAR, this.bloco, token);
			nextToken();
			return variavel;

		} else {
			exception.FatorException(token);
		}
		return null;
	}
	public void cast(Variavel variavel) {
		variavel.setTipo(Gramatica.TIPOFLOAT);
	}
}