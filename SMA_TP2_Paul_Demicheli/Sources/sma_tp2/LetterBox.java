package sma_tp2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Léo on 02-11-15.
 */
public class LetterBox {
    HashMap<Agent,List<Msg>> m_box=new HashMap<>();
    HashMap<Agent,Object> m_semaphores= new HashMap<>();

    private static LetterBox INSTANCE=new LetterBox();

    public static LetterBox getInstance(){
        return INSTANCE;
    }

    public void init(List<Agent> agents){
        for(Agent a :agents){
            m_box.put(a,new LinkedList<Msg>());
            m_semaphores.put(a,new Object());
        }
    }
    
    public void addAgent(Agent a){
         m_box.put(a,new LinkedList<Msg>());
          m_semaphores.put(a,new Object());
    }

    public void sendMessage(Msg m){
    if(m.getDest()==null ){
        int i=0;
    }  
    else{
        synchronized(m_semaphores.get(m.getDest())){
           // if(dest!=null && m)
            m_box.get(m.getDest()).add(m);
        }
    }
    }

    public List<Msg> readMessages(Agent a){
        synchronized(m_semaphores.get(a)){
        List<Msg> msgs=m_box.get(a);
        m_box.put(a, new LinkedList<Msg>());
        return msgs;
        }
    }

    private LetterBox(){

    }



}
