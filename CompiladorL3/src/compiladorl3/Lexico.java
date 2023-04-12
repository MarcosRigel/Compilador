/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladorl3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tarci
 */
public class Lexico {
    private char[] conteudo;
    private int indiceConteudo;
    
    public Lexico(String caminhoCodigoFonte){
        try {
            String conteudoStr = new String(Files.readAllBytes(Paths.get(caminhoCodigoFonte)));
            this.conteudo = conteudoStr.toCharArray();
            this.indiceConteudo = 0;                        
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
    }
    
    //Retorna próximo char
    private char nextChar(){
        return this.conteudo[this.indiceConteudo++];
    }
    
    //Verifica existe próximo char ou chegou ao final do código fonte
    private boolean hasNextChar(){
        return indiceConteudo < this.conteudo.length;
    }
    
    //Retrocede o índice que aponta para o "char da vez" em uma unidade
    private void back(){
        this.indiceConteudo--;
    }
    
    //Identificar se char é letra minúscula    
    private boolean isLetra(char c){
        return (c >= 'a') && (c <= 'z');
    }
    
    private boolean isLetraMaiuscula(char c ) {
    	return (c >= 'A') && (c <= 'Z');
    }
    
    //Identificar se char é dígito
    private boolean isDigito(char c){
        return (c >= '0') && (c <= '9');
    }
    
    private boolean isAritimetico(char c) {
    	return (c == '+') || (c == '-') || (c == '*') || (c == '/') || (c == '(') || (c == ')') || (c == '{') || (c == '}') || (c == ',')|| (c == ';');
    }
    
    private boolean isOperadorAtribuicao(char c) {
    	return (c == '=');
    }
    
    private boolean isOperadorRelacional(char c) {
    	return (c == '>') || (c == '<');
    }
    
    //Método retorna próximo token válido ou retorna mensagem de erro.
    public Token nextToken(){
        Token token = null;
        char c;
        int estado = 0;
        
        StringBuffer lexema = new StringBuffer();
        while(this.hasNextChar()){
            c = this.nextChar();
            
            switch(estado){
               	case 0:
                   if(c == ' ' || c == '\t' || c == '\n' || c == '\r' ){ //caracteres de espaço em branco ASCII tradicionais 
                        estado = 0;
                    }
                    else if(this.isLetra(c) || c == '_'){
                        lexema.append(c);
                        estado = 1;
                    }
                    else if(this.isDigito(c)){
                        lexema.append(c);
                        estado = 2;
                    }
                    else if(c == ')' || c == '(' || c == '{' || c == '}' || c == ',' || c == ';'){
                        lexema.append(c);
                        estado = 5;
                    } 
                    else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                    	lexema.append(c);
                    	return new Token(lexema.toString(), Token.TIPO_OPERADOR_ARITMETICO);
                    } 
                    else if(c == '=') {
                    	lexema.append(c);
                    	c = this.nextChar();
                    	if(c == '=') {
                    		lexema.append(c);
                    		estado = 8;
                    	}
                    	else {
                    		this.back();
                    		return new Token(lexema.toString(), Token.TIPO_OPERADOR_ATRIBUICAO);
                    	}
                    }
                    else if(c == '>'){ // tem que testar se o proximo token é um '='
                    	lexema.append(c); 
                    	c = this.nextChar();
                    	if(c == '=') {
                    		lexema.append(c);
                    		estado = 8;
                    	} else {
                    		this.back();
                    		return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                    	}
                    }
                    else if(c == '<') {
                    	lexema.append(c); 
                    	c = this.nextChar();
                    	if(c == '=') {
                    		lexema.append(c);
                    		estado = 8;
                    	}
                    	else if(c == '>') {
                    		lexema.append(c);
                    		estado = 8;
                    	}
                    	else {
                    		this.back();
                    		return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                    	}
                    }
                    else if(c == '\'') {
                    	lexema.append(c);
                    	c = this.nextChar();
                    	if(this.isDigito(c) || this.isLetra(c) || this.isLetraMaiuscula(c)) {
                    		lexema.append(c);
                    		estado = 9;
                    	} 
                    	else if(c == '\'') {
                    		lexema.append(c);
                    		estado = 10;
                    	}
                    	else {
                    		lexema.append(c);
                    		c = this.nextChar();
                    		System.err.println("Erro: char mal formado " + lexema.toString());
                    	}
                    }
                    else if(c == '$') {
                        lexema.append(c);
                        estado = 99;
                        this.back();
                    }
                    else if(c == '@') {
                    	lexema.append(c);
                    	c = this.nextChar();
                    	System.err.println("Erro: token invalido " + lexema.toString());
                    }
                    else{
                        lexema.append(c);
                        c = this.nextChar();
                        System.err.println("Erro: token inválido \"" + lexema.toString() + "\"");
                    }
                    break;
                case 1:
                    if(this.isLetra(c) || this.isDigito(c) || c == '_'){
                        lexema.append(c);
                        estado = 1;
                    }
                    else if(lexema.toString().equals("int") || 
                    		lexema.toString().equals("double") || 
                    		lexema.toString().equals("float") || 
                    		lexema.toString().equals("char") || 
                    		lexema.toString().equals("if") || 
                    		lexema.toString().equals("else") || 
                    		lexema.toString().equals("main") || 
                    		lexema.toString().equals("do") || 
                    		lexema.toString().equals("while")|| 
                    		lexema.toString().equals("for")) {
                    	this.back();
                        return new Token(lexema.toString(), Token.TIPO_PALAVRA_RESERVADA); 
                    }
                    else{
                    	this.back();
                        return new Token(lexema.toString(), Token.TIPO_IDENTIFICADOR);  
                    }
                    break;
                case 2:
                    if(this.isDigito(c)){
                        lexema.append(c);
                        estado = 2;
                    }else if(c == '.'){
                        lexema.append(c);
                        estado = 3;
                    }else{
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_INTEIRO);
                    }
                    break;
                case 3:
                    if(this.isDigito(c)){
                        lexema.append(c);
                        estado = 4;
                    }else{
                        lexema.append(c);
                        c = this.nextChar();
                        System.err.println("Erro: numero float invalido " + lexema.toString());
                    }
                    break;
                case 4:
                    if(this.isDigito(c)){
                        lexema.append(c);
                        estado = 4;
                    }else{
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_REAL);
                    }
                    break;
                case 5:
                	if (this.isAritimetico(c)) {
                		lexema.append(c);
                		estado = 5;
                	}
                	else {
                		this.back();
                		return new Token(lexema.toString(), Token.TIPO_CARACTER_ESPECIAL);
                	}
                	break;
                case 6:
                	if (this.isAritimetico(c)) {
                		this.back();
                	}
                	break;
                case 7:
                	if (this.isOperadorAtribuicao(c)) {
                		lexema.append(c);
                		estado = 7;
                	}
                	else {
                		this.back();
                		return new Token(lexema.toString(), Token.TIPO_OPERADOR_ATRIBUICAO);
                	}
                	break;
                case 8:
                	if(this.isOperadorRelacional(c)) {
                		lexema.append(c);
                		estado = 8;
                	} else {
                		this.back();
                		return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                	}
                	break;
                case 9: 
                	if(c == '\'') {
                		lexema.append(c);
                		return new Token(lexema.toString(), Token.TIPO_CHAR);
                	}
                	else if(c == '@') {
                		lexema.append(c);
                		c = this.nextChar();
                		System.err.println("Erro: char mal formado " + lexema.toString());
                	}
                	else if(c != '\'')  {
                		lexema.append(c);
                		System.err.println("Erro: char mal formado " + lexema.toString());
                	}
                	break;
                case 10:
                	if(isLetra(c) || isDigito(c) || isLetraMaiuscula(c) || c == '!' || c == ' ' || c == '?' || c == '@' || c == '#' || c == '$' || c == '+' || c == '-' || c == '/' || c == '`' || c == '*') {
                		lexema.append(c);
                	}
                	else if(c == '\'') {
                		c = this.nextChar();
                		lexema.append(c);
                		if(c == '\'') {
                			lexema.append(c);
                			return new Token(lexema.toString(), Token.TIPO_STRING); 
                		}
                		else {
                			System.err.println("Erro: string mal formatada " + lexema.toString());
                		}
                	}
                	else {
                		System.err.println("Erro: string mal formatada " + lexema.toString());
                	}
                	break;
                case 99:
                    return new Token(lexema.toString(), Token.TIPO_FIM_CODIGO); 
            	}
            }
        
        return token;
    }   
}
