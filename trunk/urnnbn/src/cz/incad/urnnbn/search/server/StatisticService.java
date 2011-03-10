package cz.incad.urnnbn.search.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aplikator.server.persistence.Persister;
import org.aplikator.server.persistence.PersisterFactory;

public class StatisticService {
    
    private static final Logger LOG = Logger.getLogger(StatisticService.class.getName());
    
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
            String qs = "select count(*) from "+tableName;
            ResultSet rs = st.executeQuery(qs);
            if (rs.next()) {
                int retval = rs.getInt(1);
                return retval;
            }
        }catch(Exception ex){
            LOG.log(Level.SEVERE, "Stats error: ", ex);
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
