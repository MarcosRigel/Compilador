/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladorl3;

/**
 *
 * @author tarci
 */
public class CompiladorL3 {
    public static void main(String[] args) {
        Lexico lexico = new Lexico("C:\\Users\\Marcos Rigel\\OneDrive\\Área de Trabalho\\CompiladorL3\\src\\compiladorl3\\codigo.txt");
        Sintatico2 sintatico = new Sintatico2(lexico);
        sintatico.S();
    }    
}

