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
		
			while (c != EoF) {
				String lexema = "";

				while (c == ' ' || c == '\n' || c == '\r' || c == '\t') {
					getNextToken();
				}

				if (c == EoF) {
					return new Token(Gramatica.EOF, "EndOfFile", linha, coluna);

				}
				
				else if (isLetter(c) || c == '_') {

					while (isLetter(c) || isDigit(c) || c == '_') {
						lexema += c;
						getNextToken();
					}

					palavraReservada = Token.isPalavraReservada(lexema);
					return new Token(palavraReservada, lexema, linha, coluna);

				}
				
				else if (c == '\'') {
					lexema += c;
					getNextToken();

					if (!(isLetter(c) || isDigit(c))) {
						Exception.CharException(linha, lexema);
					}

					lexema += c;
					getNextToken();

					if (c != '\'') {
						Exception.CharException(linha, lexema);
					}

					lexema += c;
					getNextToken();
					return new Token(Gramatica.TIPOCHAR, lexema, linha, coluna);

				}
				if (isDigit(c)) { 
					while (isDigit(c)) {
						lexema += c;
						getNextToken();
					}
					if (c == '.') {
						lexema += c;
						getNextToken();
						if (isDigit(c)) {
							while (isDigit(c)) {
								lexema += c;
								getNextToken();
								if (c == '.') {
									lexema += c;
									Exception.FloatException(linha, lexema);
								}
							}
							return new Token(Gramatica.TIPOFLOAT, lexema, linha, coluna);
						} else {
							Exception.FloatException(linha, lexema);
						}

					} else {
						return new Token(Gramatica.TIPOINT, lexema, linha, coluna);
					}

				}

				else if (c == '<') {
					lexema += c;
					getNextToken();

					if (c == '=') {
						lexema += c;
						getNextToken();
						return new Token(Gramatica.OPERADOR_RELACIONAL_MENORIGUAL, lexema, linha, coluna);

					} else {
						return new Token(Gramatica.OPERADOR_RELACIONAL_MENOR, lexema, linha, coluna);
					}

				}

				else if (c == '>') {
					lexema += c;
					getNextToken();
					if (c == '=') {
						lexema += c;
						getNextToken();
						return new Token(Gramatica.OPERADOR_RELACIONAL_MAIORIGUAL, lexema, linha, coluna);
					} else {
						return new Token(Gramatica.OPERADOR_RELACIONAL_MAIOR, lexema, linha, coluna);
					}

				}
				else if (c == '!') {
					lexema += c;
					getNextToken();

					if (c != '=') {
						lexema += c;
						getNextToken();
						Exception.DeferencaException(linha, lexema);

					}
					else {
						lexema += c;
						getNextToken();
						return new Token(Gramatica.OPERADOR_RELACIONAL_DIFERENCA, lexema, linha, coluna);
					}

				}

				else if (c == '+') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.OPERADOR_ARITMETICO_SOMA, lexema, linha, coluna);

				}
				else if (c == '-') {
					lexema += c;
					getNextToken();

					return new Token(Gramatica.OPERADOR_ARITMETICO_SUBTRACAO, lexema, linha, coluna);
				}

				else if (c == '*') {
					lexema += c;
					getNextToken();

					return new Token(Gramatica.OPERADOR_ARITMETICO_MULTIPLICACAO, lexema, linha, coluna);

				}
				else if (c == '/') {
					lexema += c;
					getNextToken();

					return new Token(Gramatica.OPERADOR_ARITMETICO_DIVISAO, lexema, linha, coluna);

				}
				else if (c == '=') {
					lexema += c;
					getNextToken();

					if (c == '=') {
						lexema += c;
						getNextToken();
						return new Token(Gramatica.OPERADOR_RELACIONAL_IGUAL, lexema, linha, coluna);
					} else {
						return new Token(Gramatica.OPERADOR_ARITMETICO_ATRIBUICAO, lexema, linha, coluna);
					}
				}

				else if (c == '(') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_ABREPARENTESES, lexema, linha, coluna);

				}

				else if (c == ')') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_FECHAPARENTESES, lexema, linha, coluna);

				}

				else if (c == '{') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_ABRECHAVE, lexema, linha, coluna);

				}

				else if (c == '}') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_FECHACHAVE, lexema, linha, coluna);

				}

				else if (c == ';') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_PONTOVIRGULA, lexema, linha, coluna);

				}

				else if (c == ',') {
					lexema += c;
					getNextToken();
					return new Token(Gramatica.CARACTER_ESPECIAL_VIRGULA, lexema, linha, coluna);
				}
			
				else {
					lexema += c;
					Exception.NotValidException(linha, lexema);
				}

			}

			return new Token(Gramatica.EOF, "EndOfFile", linha, coluna);

		}
	
	private static boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z');
	}

	private static boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

	private static void getNextToken() throws IOException {
		c = (char) arquivo.read();

		isCont();
	}

	private static void isCont() {

		if (c == '\t') {
			coluna += 4;

		}
		else if (c == '\n') {
			linha += 1;
			coluna = 0;
		} else {
			coluna += 1;
		}
	}

}
