/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sma_tp2;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Léo
 */
public class AgentOffering {
    private String m_company;
    private final List<Travel> m_travels;
    private AgentBuying m_currentBuying=null;
    private static LinkedList<AgentOffering> s_agentOffering=new LinkedList<AgentOffering>();
    
    /**
     * @param travels
     */
    public AgentOffering(List<Travel> travels){
        m_travels = travels;
        s_agentOffering.add(this);
    }
    

    
    public AgentNegociator engageNegociations(AgentBuying ab, Travel.Destination dest){
        Travel traneg=null;
        for(Travel tra : m_travels){
            if(tra.getDest()==dest){
                traneg=tra;
            }
        }
        
        if(traneg!=null){
            AgentNegociator an= new AgentNegociator(ab, traneg);
            an.start();
            return an;
        }
        else
            return null;
    }
    
    
    public String getCompany() {
        return m_company;
    }
    
    public boolean hasDestination(Travel.Destination dest){
       for(Travel t :m_travels){
           if(t.getDest()==dest){
               return true;
           }        
       }
       return false;
    }
    
    public static LinkedList<AgentOffering> getAgentsOffering(){
        return s_agentOffering;
    }
            
        
    
}
