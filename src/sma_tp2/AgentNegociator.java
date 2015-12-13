/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sma_tp2;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Léo
 */
public class AgentNegociator extends Agent {
    
    float prixMin;
    float prixActuel = 3 * prixMin / 2;
    int nbPropositions = 1;
//float propClient = 0;
    float prixPrec = prixMin * 2 - nbPropositions * prixMin / 8;
    float seuilAccept = 1.05f;
    private float m_oldPropClient=0;
    private float m_seuilQuit = 1.02f;
    private Travel m_travel;
    private AgentBuying m_agentBuying;
    float  propClient;

     public AgentNegociator(AgentBuying ab, Travel travel) {
        m_travel = travel;
        this.m_agentBuying = ab;
        prixMin = m_travel.geMinPrice();
    }

    @Override
    public void run() {
        LetterBox.getInstance().addAgent(this);
        while (true) {
            Msg m= waitForMsg();
            log(m.getRequest().toString() + " received");
            switch (m.getRequest()) {
                case ASK:
                    propClient = m.getInfo();
                    log("Prix :" + propClient);
                    handleProposal(propClient);
                    if (acceptProp()) {
                        Msg tosend = new Msg(this, m_agentBuying, Msg.Request.ACCEPT, propClient);
                        LetterBox.getInstance().sendMessage(tosend);
                        log("Accepted : proceed transaction !");
                        this.destroy();
                    } else if (quitTrade()) {
                        Msg tosend = new Msg(this, m_agentBuying, Msg.Request.QUIT, null);
                        LetterBox.getInstance().sendMessage(tosend);
                          log("Stagnating, quitting transaction");
                        this.destroy();
                    } else {
                        Msg tosend = new Msg(this, m_agentBuying, Msg.Request.ASK, prixActuel);
                        LetterBox.getInstance().sendMessage(tosend);
                          log("send new proposal"+prixActuel);
                    }
                    break;
                case ACCEPT:
                    log("Accepted : proceed transaction !");
                    this.destroy();
                    break;
                 case QUIT:
                    log("Client quitted the transaction");
                    this.destroy();
                    break;
            }
            m_oldPropClient = propClient;
        }

    }

 
    public boolean acceptProp() {
        return (prixActuel <= propClient || prixActuel < propClient * seuilAccept);
    }

    public boolean quitTrade() {
     /*   log("prop client :"+propClient);
        log("old client :"+m_oldPropClient);*/  
        
       if (propClient < m_oldPropClient * m_seuilQuit){
            if(m_quitIncrement<m_maxStagn){
                m_quitIncrement++;
                return false;
            }
                return true;
       }
       else{
           m_quitIncrement=0;
           return false;
       }
       
    }

    public void log(String str) {
        System.out.println("Negociator " + this.getName() + " :" + str);
    }

    @Override 
    public void handleProposal(float prop) {
        if (prop < 2 * prixMin / 3) {
            prixActuel = prixMin * 2 - nbPropositions * prixMin / 8;
        } else {
            float facteur = 2 - nbPropositions / 5;
            prixActuel = prop + prixActuel * facteur / nbPropositions;
            if (prixActuel >= prixPrec) {
                prixActuel = prixPrec - prixMin / 8;
                //prixActuel*= 2-seuilAccept; 
            }
        }
        if (prixActuel < prixMin) {
            prixActuel = prixMin;
        }
        prixPrec = prixActuel;
        nbPropositions++;
    }

}
