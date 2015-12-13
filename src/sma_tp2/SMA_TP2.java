/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sma_tp2;

import java.util.LinkedList;

/**
 *
 * @author Léo
 */
public class SMA_TP2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        AgentBuying ab = new AgentBuying(Travel.Destination.PARIS, 580);
        AgentOffering ao;
        LinkedList<Travel> travList=new LinkedList<Travel>();
        travList.add(new Travel(Travel.Destination.PARIS,532));
        ao = new AgentOffering(travList);
        ab.init(AgentOffering.getAgentsOffering());
        ab.start();
    }
    
}
