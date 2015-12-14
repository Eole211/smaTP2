/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sma_tp2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Agent acheteur : cet agent veut acheté un billet pour une destination précise
 * avec un prix inférieur à prixMax
 * @author Léo
 */
public class AgentBuying extends Agent {

    //List<String> m_bannedCompanies;
    //List<String> m_likedCompanies;
    private static final Object m_semAo = new Object();
    private static List<AgentOffering> m_offerings;
    private float m_oldPropFournisseur=Float.MAX_VALUE;
    private final float m_seuilQuit = 1.04f;
    private Travel.Destination m_dest;
    private int m_startValue;
    private boolean m_satisfied=false;
    private AgentOffering m_currentOffering = null;
    private AgentNegociator m_currentNegociator=null;
    private static final LinkedList<AgentBuying> s_agentsBuying=new LinkedList<>();
    
    float prixMax ;
    float prixDep ;
    float prixActuel;
    float nbPropositions = 1;
    float propFournisseur = 0;
    float seuilAccept = 1.04f;

    /**
     * Initialise l'agent avec la liste des agents offrantss
     * @param ao 
     */
    public void init(List<AgentOffering> ao) {
        m_offerings = ao;
        LetterBox.getInstance().addAgent(this);
    }
    
    /**
     * Initialise tous les AgentsBuying
     * @param ao 
     */
    public static void initAndStartAll(List<AgentOffering> ao){
        for(AgentBuying ab : s_agentsBuying){
            ab.init(ao);
        }   
        
         for(AgentBuying ab : s_agentsBuying){
            ab.start();
        }   
        
    }
    
    /**
     * Constructeur
     * @param dest
     * @param price 
     */
    AgentBuying(Travel.Destination dest, int price) {
        m_dest = dest;
        prixMax = price;
        prixDep=prixMax*0.6f;
        prixActuel=prixDep;
        s_agentsBuying.add(this);
    }
    
    /**
     * Réinitilisation lorsqu'on change d'agent négociateur
     */
    public void reinit(){
        prixDep=prixMax*0.6f;
        prixActuel=prixDep;
    }

    
    /**
     * Run du thread
     */
    @Override
    public void run() {
        boolean endNegoc=false;
        LetterBox.getInstance().addAgent(this);
        
        //Boucle s'exécutant jusqu'à l'achat d'un billet
        while(!m_satisfied){    
           //recherche d'une offre
            waitForAnOffer(); 
            //Envoie d'une première proposition
             Msg tosend = new Msg(this, m_currentNegociator, Msg.Request.ASK, prixActuel);
                                LetterBox.getInstance().sendMessage(tosend);
                                  log("send new proposal"+prixActuel);
            //négociations
            while (!endNegoc) {
                //attente d'un message
                Msg m = waitForMsg();
                if(m.getEmitter()==m_currentNegociator){
                    log(m.getRequest().toString() + " received");
                    switch (m.getRequest()) {
                        //réception d'une proposition
                        case ASK:
                            propFournisseur = m.getInfo();
                            log("Prix :" + propFournisseur);
                            //prise en compte de la proposition
                            handleProposal(propFournisseur);
             
                            //Est-ce qu'on l'accepte ?
                            if (acceptProp()) {
                                tosend = new Msg(this, m_currentNegociator, Msg.Request.ACCEPT, propFournisseur);
                                LetterBox.getInstance().sendMessage(tosend);
                                log("Accepted : proceed transaction !");
                                 logGreen("!!! I accepted the proposition " + m.getInfo()+" of the negociator "+m_currentNegociator.getName()+"  for the destination "+m_dest+" : proceed transaction !");
                                m_satisfied=true;
                                endNegoc=true;  
                                Thread.currentThread().interrupt(); 
                             //Est-ce que ça stagne et que l'on quitte ?
                            } else if (quitTrade()) {
                                tosend = new Msg(this, m_currentNegociator, Msg.Request.QUIT, null);
                                LetterBox.getInstance().sendMessage(tosend);
                                  log("Stagnating, quitting transaction");
                                  endNegoc=true;
                                  m_satisfied=false;   
                            //Est-ce qu'on renvoie une contreproposition
                            } else {
                                tosend = new Msg(this, m_currentNegociator, Msg.Request.ASK, prixActuel);
                                LetterBox.getInstance().sendMessage(tosend);
                                  log("send new proposal "+prixActuel);
                            }
                            break;
                       //Réception d'un message d'acceptance
                        case ACCEPT:
                            log("Prop Accepted by the negociator : proceed transaction !");
                             logGreen("!!! Negociator "+m_currentNegociator.getName()+" accepted the proposition "+ m.getInfo()+"  for the destination "+m_dest+" : proceed transaction !");
                           endNegoc=true;
                           m_satisfied=true;
                            Thread.currentThread().interrupt();
                         
                            break;
                        //Réception d'un message QUIT
                         case QUIT:
                            log("Client quitted the transaction");                     
                           endNegoc=true;
                            break;
                    }
                    m_oldPropFournisseur = this.propFournisseur;
                }
                else{
                    log("msg received from an other negociator");
                }
            }
            m_currentOffering=null;
            m_currentNegociator=null;
            reinit();
            endNegoc=false;
        }
    }
    
    //Prise en compte de la proposition
    @Override
    public void handleProposal(float prop) {
       log("prix actuel :"+prixActuel);
       if(prop>prixMax){
             log("prop>prixMax");
            prixActuel = prixActuel + prixDep / 3*nbPropositions;
       }
        else{ log("prop<prixMax");
            if((new Random().nextInt(10)) >3){
               prixActuel +=(prop-prixActuel)/8;
                log("try to lower more");
            }
            else{
                 prixActuel = prixActuel + prixDep / 3*nbPropositions;
            }
        }       
        if (prixActuel > prixMax) {
            prixActuel = prixMax;
           log("majoré par prixMax :"+prixActuel);
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
        if(SMA_TP2.s_verbose)
            System.out.println("\033[0mBuyer " + this.getName() + "(max "+prixMax+") : " + str);
    }
    
     public void logGreen(String str) {
        System.out.println("\033[32mBuyer " + this.getName() + "(max "+prixMax+") : " + str);
    }

}
