/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactions.lock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.ls.LSException;

import transactions.Tr_Manager;

/**
 *
 * @author pucks
 */
public class Lock_Manager {
    
    // columns: 0 - dado, 1 - id transaction, 2 - lock type
    Map<String, List<String>> Lock_Table = new HashMap<>();
    Wait_Q waitq = new Wait_Q();
    // columns: 0 - dado, 1 - queue of who's waiting for access the dado
    Map<String, Wait_Q> dado_queue = new HashMap<String, Wait_Q>();
    String shared, exclusive;
    
    Lock_Manager() {
        shared = "S";
        exclusive = "X";
    }
    
    String isShared(){
        return shared;
    }
    
    String isExclusive(){
        return exclusive;
    }

    
    
    void LS(String idTransaction, String dado){
        // Insere um bloqueio no modo compartilhado(S) na Lock Table 
        // sobre o item "dado" para a transacao "idTransaction" se puder, 
        // caso contrario cria/atualiza a Wait_Q do dado em questao com a transacao.
        
        List<String> ls = new ArrayList<>(); //id_transaction and lock_type
        ls.add(idTransaction);
        ls.add(isShared());
                                            //falta associar com timestamp
        if(dado_queue.isEmpty()==false){
            if(dado_queue.containsValue(dado)){
                System.out.println("A fila já contém o dado.");
                waitq = dado_queue.get(dado);
                waitq.addToQueue(idTransaction, isShared());
            }

            if(dado_queue.containsValue(dado)==false && Lock_Table.isEmpty()==false){
                waitq.addToQueue(idTransaction, isShared());
                dado_queue.put(dado, waitq);
            }

        } else {
            if(Lock_Table.containsValue(dado)){
                if(Lock_Table.containsValue(ls)){
                    System.out.println("A Tabela de bloqueios já bloqueou esta transação.");
                }
            } else {
                Lock_Table.put(dado,ls);
            }
        }
        
    }
    
    void LX(String idTransaction, String dado){ 
        // Insere um bloqueio no modo exclusivo na Lock Table sobre o 
        // item "dado" para a transacao idTransaction se puder, caso 
        // contrario cria/atualiza a Wait_Q de "dado" com a transacao.
        // * Há uma politica anti-deadlock tal que se tiver SHARED para uma transacao de 
        // TIMESTAMP n-1 e uma transacao de TIMESTAMP n quiser acesso exclusivo, 
        // esta é abortada.
    }

    void U(String idTransaction, String dado){
        // Apaga o bloqueio da transacao sobre o item dado na Lock Table.
        if (Lock_Table.containsValue(dado)){
            Lock_Table.remove(idTransaction);
        }
        if (waitq.containsValue(idTransaction)){
            waitq.removeFromQueue(idTransaction);
        }
    }
    
    void WaitDie(String idTransaction1, String idTransaction2, String dado){
        // Chame o metodo getTimestamp() do Tr_Manager para
        // pegar o timestamp das duas transacoes e decidir o que fazer
        // Seja Ti a transacao já com locked, e Tj a que acabou de chegar,
        // a decisao vai de acordo com as seguintes regras:
        // 1 - Se o timestamp Ti < timestamp Tj, Tj é abortada (ABORTED)
        // 2 - Caso contrario, Tj vai pra Wait_Q do dado
        //
        // * Essa decisao vale tanto para Exclusive sobre Shared 
        // como para Shared sobre Exclusive
        int time_one = getTimestamp(idTransaction1);
        int time_two = getTimestamp(idTransaction2);

        // if(){
        //     if(time_one<time_two){

        //     }
    
    }


}
