package Main;


import java.io.FileReader;

import Sintatico.AnalisadorSintatico;

public class Main {

	public static void main(String[] args) throws Exception {
		
		FileReader arquivo;
		AnalisadorSintatico analisadorSintatico;
		arquivo = new FileReader("Input");
		
		analisadorSintatico = new AnalisadorSintatico(arquivo);
		analisadorSintatico.parser();		
	}
}
