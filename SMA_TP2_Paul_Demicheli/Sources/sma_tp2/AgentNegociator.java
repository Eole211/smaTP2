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
 * c'est l'agent négociant un voyage avec l'acheteur
 * Il a un prix minimum de vente
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
        prixActuel = 1.3f*prixMin;
    }

     /**
      * Run du thread
      */
    @Override
    public void run() {
        boolean endNegoc=false;
        //ajout de la boîte au lettre
        LetterBox.getInstance().addAgent(this);
        
        //Négociations
        while (!endNegoc) {
            Msg m= waitForMsg();
            log(m.getRequest().toString() + " received");
            switch (m.getRequest()) {
                //Réception du proposition
                case ASK:
                    propClient = m.getInfo();
                    log("Prix :" + propClient);
                    
                    //Prise en compte de la propositiokn
                    handleProposal(propClient);
                    
                    //Est-ce qu'on accepte la proposition
                    if (acceptProp()) {
                        Msg tosend = new Msg(this, m_agentBuying, Msg.Request.ACCEPT, propClient);
                        LetterBox.getInstance().sendMessage(tosend);
                        log("Accepted : proceed transaction !");
                        logRed("!!! I accepted the proposition " + m.getInfo()+" of the buyer "+m_agentBuying.getName()+" for the destination "+m_travel.getDest()+": proceed transaction !");
                        endNegoc=true;
                        Thread.currentThread().interrupt();
                     //Est-ce que ça stagne et qu'on quitte
                    } else if (quitTrade()) {
                        Msg tosend = new Msg(this, m_agentBuying, Msg.Request.QUIT, null);
                        LetterBox.getInstance().sendMessage(tosend);
                          log("Stagnating, quitting transaction");
                            endNegoc=true;
                        Thread.currentThread().interrupt();
                      //Est -ce qu'on envoie une contreproposition ?  
                    } else {
                        Msg tosend = new Msg(this, m_agentBuying, Msg.Request.ASK, prixActuel);
                        LetterBox.getInstance().sendMessage(tosend);
                          log("send new proposal "+prixActuel);
                    }
                    break;
                 //Récepiton de message d'acceptation
                case ACCEPT:
                    logRed("!!! Buyer "+m_agentBuying.getName()+" accepted the proposition "+ m.getInfo()+"  for the destination "+m_travel.getDest()+": proceed transaction !");
                      endNegoc=true;
                        Thread.currentThread().interrupt();
                    break;
                    
                 //Réception d'un message QUIT
                 case QUIT:
                    log("Client quitted the transaction");
                    endNegoc=true;
                        Thread.currentThread().interrupt();
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
         if(SMA_TP2.s_verbose)
                System.out.println("\033[0mNegociator " + this.getName() + "(min "+prixMin+") : " + str);
    }
     public void logRed(String str) {
        System.out.println("\033[31mNegociator " + this.getName() + "(min "+prixMin+") : " + str);
    }

     /**
      * Prise en compte de la proposition
      * @param prop 
      */
    @Override 
    public void handleProposal(float prop) {
          
        log("prixactuel :"+prixActuel);
             prixActuel=(prixMin+prop+(2+nbPropositions)*prixActuel)/(4+nbPropositions);
            if (prixActuel < prixMin) {
               prixActuel = prixMin;
               log("prix minoré par prix Min :"+prixActuel);
        }
         
          prixPrec = prixActuel;
          nbPropositions++;
         
        
        /*
        log("prixactuel "+prixActuel);
        if (prop < 2 * prixMin / 3) {
            prixActuel = prixMin * 2 - nbPropositions * prixMin / 8;
           log("prop basse < 2*prinMin/3");
           log("=>prixActuel = prixMin * 2 - nbPropositions * prixMin / 8");
            log("new prix actuel ="+prixActuel);
        } else {
            log("prop ok");
            float facteur = 2 - nbPropositions / 5;
            prixActuel = prop + prixActuel * facteur / nbPropositions;
              log("new prixactuel"+prixActuel);
            if (prixActuel >= prixPrec) {
                  log("prixActuel >= prixPrec");
                prixActuel = prixPrec - prixMin / 8;
                 log("new prixactuel"+prixActuel);
                //prixActuel*= 2-seuilAccept; 
            }
        }
        if (prixActuel < prixMin) {
         
            prixActuel = prixMin;
               log("prix minoré par prix Min :"+prixActuel);
        }
        prixPrec = prixActuel;
        nbPropositions++;*/
    }

}
