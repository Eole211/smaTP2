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
public class Model {
    private List<AgentOffering> m_agentsOffering=new LinkedList<>();
    private List<AgentBuying> m_agentsBuying=new LinkedList<>();
   
    private void addAgentOffering(AgentOffering oe){
        m_agentsOffering.add(oe);
    }
    
     private void addAgentBuying(AgentBuying be){
       m_agentsBuying.add(be);
    }
    
    public void startOffers(){
         for(AgentBuying ab : m_agentsBuying){
            ab.init(m_agentsOffering);
            ab.start();
        }
    }

    /**
     * @return the m_agentsOffering
     */
    public List<AgentOffering> getAgentsOffering() {
        return m_agentsOffering;
    }

    
    
    
    
}
