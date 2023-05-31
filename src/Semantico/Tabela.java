package Semantico;
import java.util.Iterator;
import java.util.Stack;

import Lexico.Token;

public class Tabela {
    private Stack<Variavel> tabelaDeTokens;
   

    public Tabela(){
            this.tabelaDeTokens = new Stack<>();
    }


    public boolean contains(Token token){

        Iterator<Variavel> variables = tabelaDeTokens.iterator();

        while(variables.hasNext()){
            Variavel v = variables.next();
            if(token.getLexema().equals(v.getLexema())){
                return true;
            }
        }
        return false;
    }

    public boolean exist(Variavel variavel){
        Iterator<Variavel> variables = tabelaDeTokens.iterator();
        
        while(variables.hasNext()){
            Variavel v = variables.next();
            
            if(variavel.getLexema().equals(v.getLexema())){
                if(variavel.getBlocoDeDeclaracao().equals(v.getBlocoDeDeclaracao())){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void empilha(Variavel variavel){
        tabelaDeTokens.push(variavel);        
    }

    public void desempilha(Integer bloco){
        Iterator<Variavel> iterator;		
        iterator = tabelaDeTokens.iterator();
        int cont = 0;

        if(!tabelaDeTokens.isEmpty()){
            while(iterator.hasNext()){

                if(iterator.next().getBlocoDeDeclaracao() == bloco){
                    cont++;
                }
            }
            while(cont > 0){
                    tabelaDeTokens.pop();
                    cont--;
            }			
        }
    }
    
    public Variavel get(Token token, Integer bloco){
        Variavel variavel = null;
        
        for(Variavel v : tabelaDeTokens){
            
            if(v.getLexema().equals(token.getLexema())){
                variavel = v;
                if(v.getBlocoDeDeclaracao().equals(bloco)){
                    variavel = v;
                    break;
                }
            }            
        }        
        return variavel;
    }
}
