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
	private Token lookAHead;
	private Tabela tabela;
	private Integer bloco = 0;

	public AnalisadorSintatico(FileReader arquivo) {
		this.arquivo = arquivo;
		this.analisadorLexico = new AnalisadorLexico(arquivo);
		this.exception = new AnalisadorSintaticoException();
		this.analisadorSemanticoException = new AnalisadorSemanticoException();
		this.tabela = new Tabela();
	}
	private void nextToken() throws Exception { // ok
		lookAHead = AnalisadorLexico.nextToken();
	}

	private boolean comandoAux() {
		if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_DO) {
			return true;
		} else if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_WHILE) {
			return true;
		} else if (lookAHead.getGramatica() == Gramatica.IDENTIFICADOR) {
			return true;
		} else if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_IF) {
			return true;
		} else if (lookAHead.getGramatica() == Gramatica.CARACTER_ESPECIAL_ABRECHAVE) {
			return true;
		}
		return false;
	}

	private boolean comandoAuxBasico() {

		if (lookAHead.getGramatica() == Gramatica.IDENTIFICADOR) {
			return true;
		} else if (lookAHead.getGramatica() == Gramatica.CARACTER_ESPECIAL_ABRECHAVE) {
			return true;
		}
		return false;
	}

	public void comandoBasico() throws Exception {
		// <comando_basico> ::= <atribuicao> | <bloco>

		if (lookAHead.getGramatica() == Gramatica.CARACTER_ESPECIAL_ABRECHAVE) {
			bloco();

		} else if (lookAHead.getGramatica() == Gramatica.IDENTIFICADOR) {
			if (!tabela.contains(lookAHead)) {
				analisadorSemanticoException.VariavelNaoDeclaradaException(lookAHead);
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

		} else if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_IF) {
			nextToken();

			if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_ABREPARENTESES) {
				exception.AbreParentesesException(lookAHead);
			}
			nextToken();

			Variavel expRelacional = expressaoRelacional();

			String label = Label.NovoLabel();
			System.out.println("if ( " + expRelacional.getLexema() + " ) GO TO -> " + label + ":");

			if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES) {
				exception.FechaParentesesException(lookAHead);
			}
			nextToken();

			String label2 = Label.NovoLabel();
			System.out.println("SENÃO GO TO -> " + label2 + ":\n");

			System.out.println(label + ":");
			comando();

			if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_ELSE) {
				nextToken();
				System.out.println(label2 + ":");
			comando();
			}
			
			
		}
	}

	private boolean declaracaoVariaveisAux() {

		if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_INT) {
			return true;
		} else if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_FLOAT) {
			return true;
		} else if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_CHAR) {
			return true;
		}
		return false;
	}

	private void declaracaoVariaveis() throws Exception {

		Token tipoDeclaracao = lookAHead;

		nextToken();

		if (lookAHead.getGramatica() != Gramatica.IDENTIFICADOR) {
			exception.IdentificadorException(lookAHead);
		}

		// Adiciona uma variavel a tabela de variaveis declaradas
		Variavel variavel = new Variavel(lookAHead.getLexema(), tipoDeclaracao.getGramatica(), bloco, lookAHead);

		if (tabela.exist(variavel)) {
			this.analisadorSemanticoException.VariavelDeclaradaException(variavel.getToken());
		}

		this.tabela.empilha(variavel);

		nextToken();

		if (lookAHead.getGramatica() == Gramatica.CARACTER_ESPECIAL_VIRGULA) {
			while (lookAHead.getGramatica() == Gramatica.CARACTER_ESPECIAL_VIRGULA) {
				nextToken();

				if (lookAHead.getGramatica() != Gramatica.IDENTIFICADOR) {
					exception.IdentificadorException(lookAHead);
				}
				// Adiciona uma variavel a tabela de variaveis declaradas
				variavel = new Variavel(lookAHead.getLexema(), tipoDeclaracao.getGramatica(), bloco, lookAHead);
				this.tabela.empilha(variavel);

				nextToken();
			}
		}
		if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_PONTOVIRGULA) {
			exception.PontoVirgulaException(lookAHead);
		}
		nextToken();
	}

	private boolean iteracaoAux() {
		if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_WHILE) {
			return true;

		} else if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_DO) {
			return true;
		}
		return false;
	}

	public void iteracao() throws Exception {
		// <iteração> ::= while "("<expr_relacional>")" <comando> | do <comando> while
		// "("<expr_relacional>")"";"

		if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_WHILE) {
			nextToken();

			if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_ABREPARENTESES) {
				exception.AbreParentesesException(lookAHead);
			}
			nextToken();

			String label = Label.NovoLabel();
			System.out.println(label + ":");

			Variavel expRelacional = expressaoRelacional();

			String label2 = Label.NovoLabel();
			System.out.println("WHILE (" + expRelacional.getLexema() + ")  GO TO -> " + label2 + ":\n");

			if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES) {
				exception.FechaParentesesException(lookAHead);
			}
			nextToken();
			System.out.println(label2 + ":");
			comando();

		} else if (lookAHead.getGramatica() == Gramatica.PALAVRA_RESERVADA_DO) {
			nextToken();

			String label = Label.NovoLabel();

			System.out.println("DO");
			System.out.println(label + ":");

			comando();

			if (lookAHead.getGramatica() != Gramatica.PALAVRA_RESERVADA_WHILE) {
				exception.WhileException(lookAHead);
			}
			nextToken();

			if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_ABREPARENTESES) {
				exception.AbreParentesesException(lookAHead);
			}
			nextToken();

			Variavel expRelacional = expressaoRelacional();

			String label2 = Label.NovoLabel();
			System.out.println("WHILE (" + expRelacional.getLexema() + ") GO TO -> " + label2);
			System.out.println("SENÃO -> " + label + ":\n");
			System.out.println(label2 + ":\n");

			if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES) {
				exception.FechaParentesesException(lookAHead);
			}
			nextToken();

			if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_PONTOVIRGULA) {
				exception.PontoVirgulaException(lookAHead);
			}
			nextToken();
		}

	}

	public void parser() { 
		// <programa> / int main() <bloco>

		try {
			nextToken();

			if (lookAHead.getGramatica() != Gramatica.PALAVRA_RESERVADA_INT) {
				exception.IntException(lookAHead);
			}
			nextToken();

			if (lookAHead.getGramatica() != Gramatica.PALAVRA_RESERVADA_MAIN) {
				exception.MainException(lookAHead);
			}
			nextToken();

			if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_ABREPARENTESES) {
				exception.AbreParentesesException(lookAHead);
			}
			nextToken();

			if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES) {
				exception.FechaParentesesException(lookAHead);
			}
			nextToken();

			bloco();

			if (lookAHead.getGramatica() != Gramatica.EOF) {
				exception.EoFException(lookAHead);
			}
			else {
				System.out.println("COMPILADO COM SUCESSO");
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void bloco() throws Exception { 
		 //<bloco>  "{" {<decl_var>}* {<comando>}* "}" 
		if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_ABRECHAVE) {
			exception.AbreChaveException(lookAHead);
		}
		nextToken();
		this.bloco += 1;

		while (declaracaoVariaveisAux()) {
			declaracaoVariaveis();
		}

		while (comandoAux()) {
			comando();
		}

		if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHACHAVE) {
			exception.FechaChaveException(lookAHead);
		}
		nextToken();

		this.tabela.desempilha(bloco);
		this.bloco -= 1;
	}

	public void atribuicao() throws Exception {
		// <atribuição> ::= <id> "=" <expr_arit> ";"

		if (lookAHead.getGramatica() == Gramatica.IDENTIFICADOR) {
			if (!tabela.contains(lookAHead)) {
				analisadorSemanticoException.VariavelNaoDeclaradaException(lookAHead);
			}

			// Busca na tabela a variavel
			Variavel operando1 = tabela.get(lookAHead, bloco);
			nextToken();

			if (lookAHead.getGramatica() != Gramatica.OPERADOR_ARITMETICO_ATRIBUICAO) {
				analisadorSemanticoException.AtribuicaoException(lookAHead);
			}
			nextToken();

			Variavel operando2 = expressaoAritmeticaAux();

			
			if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {
				if (!operando2.getTipo().equals(Gramatica.TIPOINT)
						&& !operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {

					analisadorSemanticoException.TipoInvalidoIntException(operando2.getToken());
				}
				System.out.println(operando1.getLexema() + " = " + operando2.getLexema() + "\n");
			} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {
				if (operando2.getTipo().equals(Gramatica.TIPOINT)
						|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)) {

					Variavel variavel = new Variavel(operando2.getLexema(), operando2.getTipo(),
							operando2.getBlocoDeDeclaracao(), operando2.getToken());
					String temp = Label.NovaTemp();
					System.out.print(temp + " = ");
					cast(variavel);
					variavel.setLexema(temp);

					System.out.println(operando1.getLexema() + " = " + variavel.getLexema() + "\n");
				
				} else {
					System.out.println(operando1.getLexema() + " = " + operando2.getLexema() + "\n");
				}
			} else if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)) {
				if (!operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						&& !operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
				}
				System.out.println(operando1.getLexema() + " = " + operando2.getLexema() + "\n");
			}

			if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_PONTOVIRGULA) {
				exception.PontoVirgulaException(lookAHead);
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
		System.out.println(temp + " = " + operando1.getLexema() + opRelacional.getLexema() + operando2.getLexema());
		operando1.setLexema(temp);
		return operando1;
	}

	public Variavel expressaoAritmeticaAux() throws Exception {
		Variavel operando1 = termoAux();
		Token operador = lookAHead;
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
					System.out.print(temp + " = ");
					cast(variavel);
					variavel.setLexema(temp);

					String temp2 = Label.NovaTemp();
					System.out.println(
							temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando2.getLexema());
					variavel.setLexema(temp2);

					return variavel;

				} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());

				} else {
					String temp = Label.NovaTemp();
					System.out.println(
							temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());

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
					System.out.print(temp + " = ");
					cast(variavel);
					variavel.setLexema(temp);

					String temp2 = Label.NovaTemp();
					System.out.println(
							temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando1.getLexema());
					variavel.setLexema(temp2);

					return variavel;

				} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
				} else {
					String temp = Label.NovaTemp();
					System.out.println(
							temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
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
			System.out.println(temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
			operando2.setLexema(temp);
			return operando2;
		}
		return operando1;
	}

	public Variavel expressaoAritmetica() throws Exception {
		Variavel variavel = null;

		if (lookAHead.getGramatica() == Gramatica.OPERADOR_ARITMETICO_SOMA) {
			nextToken();
			Variavel operando1 = termoAux();
			Token operador = lookAHead;
			Variavel operando2 = expressaoAritmetica();

			if (operando2 != null) {

				if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)
						|| operando1.getTipo().equals(Gramatica.TIPOINT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOFLOAT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {

						variavel = new Variavel(operando1.getLexema(), operando1.getTipo(),
								operando1.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						System.out.print(temp + " = ");
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						System.out.println(
								temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando2.getLexema());
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						System.out.println(
								temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
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
						System.out.print(temp + " = ");
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						System.out.println(
								temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando1.getLexema());
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						System.out.println(
								temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
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
				System.out.println(temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
				operando2.setLexema(temp);
				return operando2;
			}
			return operando1;

		} else if (lookAHead.getGramatica() == Gramatica.OPERADOR_ARITMETICO_SUBTRACAO) {
			nextToken();
			Variavel operando1 = termoAux();
			Token operador = lookAHead;
			Variavel operando2 = expressaoAritmetica();

			if (operando2 != null) {

				if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)
						|| operando1.getTipo().equals(Gramatica.TIPOINT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOFLOAT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {

						variavel = new Variavel(operando1.getLexema(), operando1.getTipo(),
								operando1.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						System.out.print(temp + " = ");
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						System.out.println(
								temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando2.getLexema());
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						System.out.println(
								temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
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
						System.out.print(temp + " = ");
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						System.out.println(
								temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando1.getLexema());
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						System.out.println(
								temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
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
				System.out.println(temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
				operando2.setLexema(temp);
				return operando2;
			}
			return operando1;
		}
		return null;
	}

	public Variavel termoAux() throws Exception {
		Variavel operando1 = fator();
		Token operador = lookAHead; 
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
					System.out.print(temp + " = ");
					cast(variavel);
					variavel.setLexema(temp);

					String temp2 = Label.NovaTemp();
					System.out.println(
							temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando2.getLexema());
					variavel.setLexema(temp2);

					return variavel;

				} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
				} else {
					String temp = Label.NovaTemp();
					System.out.println(
							temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
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
					System.out.print(temp + " = ");
					cast(variavel);
					variavel.setLexema(temp);

					String temp2 = Label.NovaTemp();
					System.out.println(
							temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando1.getLexema());
					variavel.setLexema(temp2);

					return variavel;

				} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
						|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

					analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
				} else {
					String temp = Label.NovaTemp();
					System.out.println(
							temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
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
			System.out.println(temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
			operando2.setLexema(temp);
			return operando2;
		}

		return operando1;
	}

	public Variavel termo() throws Exception {
		Variavel variavel = null;

		if (lookAHead.getGramatica() == Gramatica.OPERADOR_ARITMETICO_MULTIPLICACAO) {
			nextToken();
			Variavel operando1 = termoAux();
			Token operador = lookAHead; 
			Variavel operando2 = termo();

			if (operando2 != null) {

				if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)
						|| operando1.getTipo().equals(Gramatica.TIPOINT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOFLOAT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {

						variavel = new Variavel(operando1.getLexema(), operando1.getTipo(),
								operando1.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						System.out.print(temp + " = ");
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						System.out.println(
								temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando2.getLexema());
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						System.out.println(
								temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
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
						System.out.print(temp + " = ");
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						System.out.println(
								temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando1.getLexema());
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						System.out.println(
								temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
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
				System.out.println(temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
				operando2.setLexema(temp);
				return operando2;
			}

			return operando1;

		} else if (lookAHead.getGramatica() == Gramatica.OPERADOR_ARITMETICO_DIVISAO) {
			nextToken();
			Variavel operando1 = termoAux();
			Token operador = lookAHead; 
			Variavel operando2 = termo();

			if (operando2 != null) {

				if (operando1.getTipo().equals(Gramatica.PALAVRA_RESERVADA_INT)
						|| operando1.getTipo().equals(Gramatica.TIPOINT)) {

					if (operando2.getTipo().equals(Gramatica.TIPOFLOAT)
							|| operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_FLOAT)) {

						variavel = new Variavel(operando1.getLexema(), operando1.getTipo(),
								operando1.getBlocoDeDeclaracao(), operando2.getToken());
						String temp = Label.NovaTemp();
						System.out.print(temp + " = ");
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						System.out.println(
								temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando2.getLexema());
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						System.out.println(
								temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
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
						System.out.print(temp + " = ");
						cast(variavel);
						variavel.setLexema(temp);

						String temp2 = Label.NovaTemp();
						System.out.println(
								temp2 + " = " + variavel.getLexema() + operador.getLexema() + operando1.getLexema());
						variavel.setLexema(temp2);

						return variavel;

					} else if (operando2.getTipo().equals(Gramatica.PALAVRA_RESERVADA_CHAR)
							|| operando2.getTipo().equals(Gramatica.TIPOCHAR)) {

						analisadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
					} else {
						String temp = Label.NovaTemp();
						System.out.println(
								temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
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
				System.out.println(temp + " = " + operando1.getLexema() + operador.getLexema() + operando2.getLexema());
				operando2.setLexema(temp);
				return operando2;
			}
			return operando1;
		}
		return null;
	}

	public Token operadorRelacional() throws Exception {

		if (lookAHead.getGramatica() == Gramatica.OPERADOR_RELACIONAL_MAIOR) {
			Token opRelacional = lookAHead;
			nextToken();
			return opRelacional;

		} else if (lookAHead.getGramatica() == Gramatica.OPERADOR_RELACIONAL_MAIORIGUAL) {
			Token opRelacional = lookAHead;
			nextToken();
			return opRelacional;

		} else if (lookAHead.getGramatica() == Gramatica.OPERADOR_RELACIONAL_MENOR) {
			Token opRelacional = lookAHead;
			nextToken();
			return opRelacional;

		} else if (lookAHead.getGramatica() == Gramatica.OPERADOR_RELACIONAL_MENORIGUAL) {
			Token opRelacional = lookAHead;
			nextToken();
			return opRelacional;

		} else if (lookAHead.getGramatica() == Gramatica.OPERADOR_RELACIONAL_IGUAL) {
			Token opRelacional = lookAHead;
			nextToken();
			return opRelacional;

		} else if (lookAHead.getGramatica() == Gramatica.OPERADOR_RELACIONAL_DIFERENCA) {
			Token opRelacional = lookAHead;
			nextToken();
			return opRelacional;

		} else {
			exception.OperadorRelacionalException(lookAHead);
		}
		return null;
	}

	public Variavel fator() throws Exception {
		//<fator> ::= "(" <expr_arit> ")" | <id> | <real> | <inteiro> | <char>

		if (lookAHead.getGramatica() == Gramatica.CARACTER_ESPECIAL_ABREPARENTESES) {
			nextToken();
			Variavel variavel = expressaoAritmeticaAux();

			if (lookAHead.getGramatica() != Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES) {
				exception.FechaParentesesException(lookAHead);
			}
			nextToken();
			return variavel;

		} else if (lookAHead.getGramatica() == Gramatica.IDENTIFICADOR) {
			if (!tabela.contains(lookAHead)) {
				analisadorSemanticoException.VariavelNaoDeclaradaException(lookAHead);
			}
			Variavel v = tabela.get(lookAHead, bloco);
			Variavel variavel = new Variavel(lookAHead.getLexema(), v.getTipo(), v.getBlocoDeDeclaracao(), lookAHead);
			nextToken();
			return variavel;

		} else if (lookAHead.getGramatica() == Gramatica.TIPOFLOAT) {
			Variavel variavel = new Variavel(lookAHead.getLexema(), Gramatica.TIPOFLOAT, this.bloco, lookAHead);
			nextToken();
			return variavel;

		} else if (lookAHead.getGramatica() == Gramatica.TIPOINT) {
			Variavel variavel = new Variavel(lookAHead.getLexema(), Gramatica.TIPOINT, this.bloco, lookAHead);
			nextToken();
			return variavel;

		} else if (lookAHead.getGramatica() == Gramatica.TIPOCHAR) {
			Variavel variavel = new Variavel(lookAHead.getLexema(), Gramatica.TIPOCHAR, this.bloco, lookAHead);
			nextToken();
			return variavel;

		} else {
			exception.FatorException(lookAHead);
		}
		return null;
	}
	public void cast(Variavel variavel) {
		System.out.println("(Float) " + variavel.getLexema());
		variavel.setTipo(Gramatica.TIPOFLOAT);
	}
}