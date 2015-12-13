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
public abstract class Agent extends Thread{
        protected int m_timeCheck = 100;
        protected int m_quitIncrement=0;
        protected int m_maxStagn=5;
     public abstract void handleProposal(float prop);
     
        public Msg waitForMsg() {
        List<Msg> msgs = LetterBox.getInstance().readMessages(this);
        while (msgs.isEmpty()) {
            try {
                Thread.sleep(m_timeCheck);
            } catch (InterruptedException ex) {
                Logger.getLogger(AgentNegociator.class.getName()).log(Level.SEVERE, null, ex);
            }
            msgs = LetterBox.getInstance().readMessages(this);
        }
        return msgs.get(0);
    }

}
