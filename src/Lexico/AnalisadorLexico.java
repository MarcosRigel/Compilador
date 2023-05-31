package Lexico;

import java.io.FileReader;
import java.io.IOException;

public class AnalisadorLexico {
	private static FileReader arquivo;
	private static AnalisadorLexicoException Exception = new AnalisadorLexicoException();
	private static int linha = 1;
	private static int coluna = 0;
	private static char c = ' ';
	private static final char EoF = '\uffff';
	private static Enum palavraReservada;

	public AnalisadorLexico(FileReader arquivo) {
		this.arquivo = arquivo;
	}

	public static Token nextToken() throws Exception {
		
			while (c != EoF) {// CONDIวรO PARA RODAR O ANALISADOR LEXICO
				String lexema = "";// VAI ARMAZENAR O QUE FOR LIDO EM C

				while (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
					getNextToken();// ANDA UM TOKEN
				}

				if (c == EoF) {
					return new Token(Gramatica.EOF, "EndOfFile", linha, coluna);

				}
				// PALAVRA RESERVADA/ IDENTIFICADOR
				// VERIFICA SE ษ PALAVRA RESERVADA OU IDENTIFICADOR
				else if (isLetter(c) || c == '_') {// SE FOR LETRA OU _

					while (isLetter(c) || isDigit(c) || c == '_') {// SE FOR LETRA OU NUMERO OU _
						lexema += c;// GRAVA O CARACTERE LIDO EM LEXEMA
						getNextToken();
					}

					palavraReservada = Token.isPalavraReservada(lexema);// SE FOR PALAVRA RESERVADA
					return new Token(palavraReservada, lexema, linha, coluna);// CRIA UM NOVO TOKEN COM A PALAVRA
																				// RESERVADA

				}
				// CHAR
				// VERIFICA SE O CARACTERE LIDO ษ TIPO CHAR
				else if (c == '\'') {
					lexema += c;
					getNextToken();

					// SE O CHAR TIVER MAL FORMADO
					if (!(isLetter(c) || isDigit(c))) {
						Exception.CharException(linha, coluna, lexema);
					}

					lexema += c;
					getNextToken();

					// SE O CHAR TIVER MAL FORMADO
					if (c != '\'') {
						Exception.CharException(linha, coluna, lexema);
					}

					lexema += c;
					getNextToken();
					return new Token(Gramatica.TIPOCHAR, lexema, linha, coluna);// CRIA NOVO TOKEN COM O TIPO CHAR

				}
				// FLOAT / INT
				// VERIFICA SE O CARACTERE LIDO ษ TIPO INT OU FLOAT
				if (isDigit(c)) { // CASO INT OU FLOAT
					while (isDigit(c)) {
						lexema += c;
						getNextToken();
					}
					if (c == '.') {// FLOAT
						lexema += c;
						getNextToken();
						if (isDigit(c)) {
							while (isDigit(c)) {// 1..
								lexema += c;
								getNextToken();
								if (c == '.') {
									lexema += c;
									Exception.FloatException(linha, coluna, lexema);
								}
							}
							return new Token(Gramatica.TIPOFLOAT, lexema, linha, coluna);
						} else {
							Exception.FloatException(linha, coluna, lexema);
						}

					} else {
						return new Token(Gramatica.TIPOINT, lexema, linha, coluna);
					}

				}

				// OPERADORES RELACIONAIS

				// VERIFICA SE O CARACTERE LIDO ษ MENOR
				else if (c == '<') {
					lexema += c;
					getNextToken();

					// VERIFICA SE O PROXIMO CARACTERE LIDO ษ IGUAL
					if (c == '=') {
						lexema += c;
						getNextToken();
						return new Token(Gramatica.OPERADOR_RELACIONAL_MENORIGUAL, lexema, linha, coluna);

					} else {
						return new Token(Gramatica.OPERADOR_RELACIONAL_MENOR, lexema, linha, coluna);
					}

				}

				// VERIFICA SE O CARACTERE LIDO ษ MAIOR
				else if (c == '>') {
					lexema += c;
					getNextToken();
					// VERIFICA SE O PROXIMO CARACTERE LIDO ษ IGUAL
					if (c == '=') {
						lexema += c;
						getNextToken();
						return new Token(Gramatica.OPERADOR_RELACIONAL_MAIORIGUAL, lexema, linha, coluna);
					} else {
						return new Token(Gramatica.OPERADOR_RELACIONAL_MAIOR, lexema, linha, coluna);
					}

				}
				// VERIFICA SE O CARACTERE LIDO ษ EXCLAMACAO
				else if (c == '!') {
					lexema += c;
					getNextToken();

					// VERIFICA SE O CARACTERE LIDO ษ DIFERNETE DE IGUAL
					if (c != '=') {
						lexema += c;
						getNextToken();
						Exception.DeferencaException(linha, coluna, lexema);

					}
					// VERIFICA SE O PROXIMO CARACTERE LIDO ษ IGUAL
					else {
						lexema += c;
						getNextToken();
						return new Token(Gramatica.OPERADOR_RELACIONAL_DIFERENCA, lexema, linha, coluna);
					}

				}
				// OPERADORES ARITMETICOS
				// VERIFICA SE O CARACTERE LIDO ษ ADIวรO
				else if (c == '+') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.OPERADOR_ARITMETICO_SOMA, lexema, linha, coluna);

				}
				// VERIFICA SE O CARACTERE LIDO ษ SUBTRACAO
				else if (c == '-') {
					lexema += c;
					getNextToken();

					// CRIA NOVO TOKEN COM OPERADOR ARITIMETICO SUBTRACAO
					return new Token(Gramatica.OPERADOR_ARITMETICO_SUBTRACAO, lexema, linha, coluna);
				}
				// VERIFICA SE O CARACTERE LIDO ษ MULTIPLICACAO

				else if (c == '*') {
					lexema += c;
					getNextToken();

					return new Token(Gramatica.OPERADOR_ARITMETICO_MULTIPLICACAO, lexema, linha, coluna);

				}
				// VERIFICA SE O CARACTERE LIDO ษ DIVISAO
				else if (c == '/') {
					lexema += c;
					getNextToken();

					// CRIA NOVO TOKEN COM OPERADOR ARITIMETICO DIV
					return new Token(Gramatica.OPERADOR_ARITMETICO_DIVISAO, lexema, linha, coluna);

				}
				// VERIFICA SE O CARACTERE LIDO ษ IGUAL
				else if (c == '=') {
					lexema += c;
					getNextToken();

					// // VERIFICA SE O PROXIMO CARACTERE LIDO ษ OUTRO IGUAL
					if (c == '=') {
						lexema += c;
						getNextToken();
						return new Token(Gramatica.OPERADOR_RELACIONAL_IGUAL, lexema, linha, coluna);
					} else {
						return new Token(Gramatica.OPERADOR_ARITMETICO_ATRIBUICAO, lexema, linha, coluna);
					}
				}

				// CARACTERES ESPECIAIS
				// VERIFICA SE O CARACTERE LIDO ษ ABRE PARENTESES
				else if (c == '(') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_ABREPARENTESES, lexema, linha, coluna);

				}

				// VERIFICA SE O CARACTERE LIDO ษ FECHA PARENTESES
				else if (c == ')') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES, lexema, linha, coluna);

				}

				// VERIFICA SE O CARACTERE LIDO ษ ABRE CHAVE
				else if (c == '{') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_ABRECHAVE, lexema, linha, coluna);

				}

				// VERIFICA SE O CARACTERE LIDO ษ FECHA CHAVE
				else if (c == '}') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_FECHACHAVE, lexema, linha, coluna);

				}

				// VERIFICA SE O CARACTERE LIDO ษ PONTO E VIRGULA
				else if (c == ';') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_PONTOVIRGULA, lexema, linha, coluna);

				}

				// VERIFICA SE O CARACTERE LIDO ษ VIRGULA
				else if (c == ',') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_VIRGULA, lexema, linha, coluna);
				}
			
				else {
					lexema += c;
					Exception.NotValidException(linha, coluna, lexema);
				}

			}

			return new Token(Gramatica.EOF, "EndOfFile", linha, coluna);

		}
	
	

	// FUNCAO PARA IDENTIFICAR LETRAS MINUSCULAS
	private static boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z');
	}

	// FUNCAO PARA IDENTIFICAR NUMEROS
	private static boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

	// PROCEDIMENTO PARA LER OS CARACTERES
	private static void getNextToken() throws IOException {
		c = (char) arquivo.read();

		isCont();
	}

	// PROCEDIMENTO PARA TRATAR TAB E QUEBRA DE LINHA
	// CONTA AS LINHAS E COLUNAS
	private static void isCont() {

		// CASO DE UM TAB CONTA 4 COLUNAS
		if (c == '\t') {
			coluna += 4;

		}
		// CASO DE UMA QUEBRA DE LINHA CONTA MAIS UMA LINHA E ZERO COLUNAS
		else if (c == '\n') {
			linha += 1;
			coluna = 0;
		} else {
			coluna += 1;
		}
	}

}
