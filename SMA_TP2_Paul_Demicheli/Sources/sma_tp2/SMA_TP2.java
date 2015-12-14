/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sma_tp2;

import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author Léo
 */
public class SMA_TP2 {
    
    public static boolean s_verbose=true;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      String a="a";
      while(!a.equalsIgnoreCase("y")&&!a.equalsIgnoreCase("n")){
       System.out.println("Display only results (y/n)?");
        a = input.next();
        }
      if(a.equalsIgnoreCase("y")){
         s_verbose=false;
      }
        
       
        
        
        // TODO code application logic here
        AgentBuying ab = new AgentBuying(Travel.Destination.PARIS, 560);
        AgentBuying ab2 = new AgentBuying(Travel.Destination.PARIS, 450);
        AgentBuying ab3 = new AgentBuying(Travel.Destination.BANGKOK, 850);
        AgentBuying ab4 = new AgentBuying(Travel.Destination.BANGKOK, 910);
        AgentOffering ao;
        
        LinkedList<Travel> travList=new LinkedList<>();
        travList.add(new Travel(Travel.Destination.PARIS,552));
        travList.add(new Travel(Travel.Destination.BANGKOK,800));
         ao = new AgentOffering(travList);
         
        LinkedList<Travel> travList2=new LinkedList<>();
        travList2.add(new Travel(Travel.Destination.PARIS,400));
        travList2.add(new Travel(Travel.Destination.BANGKOK,900));
         ao = new AgentOffering(travList2);
            
         
        AgentBuying.initAndStartAll(AgentOffering.getAgentsOffering());
    }
    
}
