package cz.incad.urnnbn.search.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.aplikator.server.persistence.Persister;
import org.aplikator.server.persistence.PersisterFactory;

public class StatisticService {
    
    Persister persister = PersisterFactory.getPersister();
    
    
    public int getIECount(){
        return getCount("intelektualni_entita");
    }
    
    public int getDRCount(){
        return getCount("digitalni_reprezentace");
    }
    
    private int getCount(String tableName){
        Connection conn = null;
        try{
            conn = persister.getJDBCConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select count(*) from "+tableName+";");
            if (rs.next()) {
                return rs.getInt(1);
            }
        }catch(Exception ex){
            System.out.println("Stats error: "+ex);
        }finally{
            if (conn!= null){
                try{
                    conn.close();
                }catch(SQLException e){
                    
                }
            }
        }
        return 0;
    }

}
