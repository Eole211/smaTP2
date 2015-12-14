package sma_tp2;
/**
 * Created by Léo on 02-11-15.
 */
public class Msg {

    public static enum Request{ASK,ACCEPT,QUIT};


    private Agent m_emitter;
    private Agent m_dest;
    private Request m_request ;
  //  private Action m_action;
    private Object m_info;



    public Msg(Agent emit, Agent dest,Request request, Object info){
        m_emitter=emit;
        m_dest=dest;
        m_request=request;
        m_info=info;
    }



       /**
     * @return the m_request
     */
    public Request getRequest() {
        return m_request;
    }


    /**
     * @return the m_info
     */
    
 
     public <T> T getInfo() {
        return  (T)m_info;
    }

    /**
     * @return the m_dest
     */
    public Agent getDest() {
        return m_dest;
    }
    
    public Agent getEmitter(){
        return m_emitter;
    }

    /**
     * @param m_dest the m_dest to set
     */
    public void setDest(Agent m_dest) {
        this.m_dest = m_dest;
    }


}
