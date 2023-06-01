package application;


import java.io.FileReader;

import Sintatico.AnalisadorSintatico;

public class Program {

	public static void main(String[] args) throws Exception {
		
		FileReader arquivo;
		AnalisadorSintatico analisadorSintatico;
		arquivo = new FileReader("Entrada");
		
		analisadorSintatico = new AnalisadorSintatico(arquivo);
		analisadorSintatico.parser();		
	}
}
