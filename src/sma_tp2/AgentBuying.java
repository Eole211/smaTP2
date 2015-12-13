/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sma_tp2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Léo
 */
public class AgentBuying extends Agent {

    //List<String> m_bannedCompanies;
    //List<String> m_likedCompanies;
    private static final Object m_semAo = new Object();
    private static List<AgentOffering> m_offerings;
    private float m_oldPropFournisseur=Float.MAX_VALUE;
    private float m_seuilQuit = 1.04f;
    private Travel.Destination m_dest;
    private int m_startValue;
    private boolean m_satisfied=false;
    private AgentOffering m_currentOffering = null;
    private AgentNegociator m_currentNegociator=null;

    float prixMax = 1000;
    float prixDep = prixMax / 3;
    float prixActuel = prixDep;
    float nbPropositions = 1;
    float propFournisseur = 0;
    float seuilAccept = 1.04f;

    public void init(List<AgentOffering> ao) {
        m_offerings = ao;
        LetterBox.getInstance().addAgent(this);
    }

    AgentBuying(Travel.Destination dest, int price) {
        m_dest = dest;
        prixMax = price;
        prixDep=prixMax/3;
        prixActuel=prixDep;
    }

    
    
    @Override
    public void run() {
        LetterBox.getInstance().addAgent(this);
        while(!m_satisfied){
            waitForAnOffer(); 
             Msg tosend = new Msg(this, m_currentNegociator, Msg.Request.ASK, prixActuel);
                                LetterBox.getInstance().sendMessage(tosend);
                                  log("send new proposal"+prixActuel);
            while (true) {
                Msg m = waitForMsg();
                if(m.getEmitter()==m_currentNegociator){
                    log(m.getRequest().toString() + " received");
                    switch (m.getRequest()) {
                        case ASK:
                            propFournisseur = m.getInfo();
                  
                            log("Prix :" + propFournisseur);
                            handleProposal(propFournisseur);
                            if (acceptProp()) {
                                tosend = new Msg(this, m_currentNegociator, Msg.Request.ACCEPT, propFournisseur);
                                LetterBox.getInstance().sendMessage(tosend);
                                log("Accepted : proceed transaction !");
                                this.destroy();
                            } else if (quitTrade()) {
                                tosend = new Msg(this, m_currentNegociator, Msg.Request.QUIT, null);
                                LetterBox.getInstance().sendMessage(tosend);
                                  log("Stagnating, quitting transaction");
                                this.destroy();
                            } else {
                                tosend = new Msg(this, m_currentNegociator, Msg.Request.ASK, prixActuel);
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
                    m_oldPropFournisseur = this.propFournisseur;
                }
                else{
                    log("msg received from an other negociator");
                }
            }
        }
    }
    
    
    
    @Override
    public void handleProposal(float prop) {
   
        prixActuel = prixActuel + prixDep / nbPropositions;
        if (prixActuel > prixMax) {
            prixActuel = prixMax;
        }
        nbPropositions++;
    }

    public boolean acceptProp() {
        return (prixActuel >= propFournisseur || prixActuel > propFournisseur * seuilAccept);
    }

    public boolean quitTrade() {
          if (propFournisseur > m_oldPropFournisseur * m_seuilQuit){
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

    public void waitForAnOffer(){
        while(m_currentOffering==null){
            findAnOffer();
            try {
                Thread.sleep(m_timeCheck);
            } catch (InterruptedException ex) {
                Logger.getLogger(AgentBuying.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    public void findAnOffer() {
        Random rnd = new Random();
        ArrayList<AgentOffering> offersWithDest = new ArrayList<>();
        for (AgentOffering ao : m_offerings) {
            if (ao.hasDestination(m_dest)) {
                offersWithDest.add(ao);
            }
        }
        int index = rnd.nextInt(offersWithDest.size());
        m_currentNegociator=offersWithDest.get(index).engageNegociations(this,  this.m_dest);
        m_currentOffering = offersWithDest.get(index);
        
    }
    
    public void log(String str) {
        System.out.println("Buyer " + this.getName() + " :" + str);
    }

}
