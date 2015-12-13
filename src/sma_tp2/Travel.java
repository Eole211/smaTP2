/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sma_tp2;

/**
 *
 * @author Léo
 */
public class Travel {

 
    public enum Destination{PARIS,TOKYO,LONDRES,DAKAR,BANGKOK};
    private Destination m_dest;
    private int m_minPrice;
    Travel(Destination dest,int price){
        m_minPrice=price;
        m_dest=dest;
    }
    
    
    
    
       /**
     * @return the m_dest
     */
    public Destination getDest() {
        return m_dest;
    }

    /**
     * @return the m_minPrice
     */
    public int geMinPrice() {
        return m_minPrice;
    }
}
