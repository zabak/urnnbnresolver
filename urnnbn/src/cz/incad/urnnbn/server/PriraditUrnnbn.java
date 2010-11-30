package cz.incad.urnnbn.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.empire.db.DBColumnExpr;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBJoinType;
import org.apache.empire.db.DBReader;
import org.apache.empire.db.DBRecord;
import org.aplikator.server.descriptor.Application;
import org.aplikator.server.function.Executable;
import org.aplikator.server.function.FunctionParameters;
import org.aplikator.server.function.FunctionResult;
import org.aplikator.server.persistence.Persister;
import org.aplikator.server.persistence.PersisterFactory;

public class PriraditUrnnbn implements Executable {
    
    private Persister persister;
    private Structure s;
    private Connection conn;
    
     
    private static final String RDCZupdateURNNBN = "update predloha set urnnbn = ?, urnnbnflag = 3 where idcislo = ?";
    @Override
    public FunctionResult execute(FunctionParameters parameters) {
        Connection RDCZconn = null;
        try{
            if (persister == null){
                persister = PersisterFactory.getPersister();
            }
            if (s== null){
                s = (Structure)Application.get();
            }
            if(conn == null){
                conn = persister.getJDBCConnection();
            }
            
            RDCZconn = getRDCZConnection();
            PreparedStatement RDCZst = RDCZconn.prepareStatement(RDCZupdateURNNBN);
            DBCommand cmd = s.db.createCommand();
            // Select required columns
            DBColumnExpr[] columns = new DBColumnExpr[]{s.digitalniReprezentace.getPrimaryKey().column, s.digitalniReprezentace.URNNBN.column, s.digitalniReprezentace.CISLO_RDCZ.column, s.instituce.PREFIX.column};   
            cmd.join(s.digitalniReprezentace.INSTITUCE.column, s.instituce.getPrimaryKey().column, DBJoinType.LEFT);
            cmd.select(columns);
            cmd.where(s.digitalniReprezentace.URNNBN.column.is(null));
            // Query Records and print output
            DBReader reader = new DBReader();
            try
            {
                // Open Reader
                System.out.println("Running Query:");
                System.out.println(cmd.getSelect());
                if (reader.open(cmd, conn) == false)
                    throw new RuntimeException(reader.getErrorMessage());
                // Print output
                DBRecord record = new DBRecord();
                while (reader.moveNext())
                {
                    String URNNBN = reader.getString(s.instituce.PREFIX.column) + getUrnnbn(reader.getInt(s.digitalniReprezentace.getPrimaryKey().column));
                    // Init updateable record
                    reader.initRecord(s.digitalniReprezentace.getTable(), record);
                    // reader
                    record.setValue(s.digitalniReprezentace.URNNBN.column, URNNBN);
                    record.setValue(s.digitalniReprezentace.PRIDELENO_DNE.column, new Date());
                    record.setValue(s.digitalniReprezentace.PRIDELENO_KYM.column, "admin");
                    record.update(conn);
                    conn.commit();
                    RDCZst.setString(1, URNNBN);
                    String cisloRDCZ = record.getString(s.digitalniReprezentace.CISLO_RDCZ.column);
                    RDCZst.setString(2,cisloRDCZ);
                    int result = RDCZst.executeUpdate();
                    RDCZconn.commit();
                }
                // Done
                

            } finally
            {
                // always close Reader
                reader.close();
            }
           
            return new FunctionResult("HOTOVO", true);
        }catch(Exception ex){
            return new FunctionResult("ERROR: "+ex.getMessage(), false);
        }finally{
            if (RDCZconn != null){
                try {
                    RDCZconn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null){
                try {
                    conn.close();
                    conn = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
   
    private static final char[] SYMBOLS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
   
    private String getUrnnbn(int id){
        int base = SYMBOLS.length;
        StringBuilder sb = new StringBuilder();
        while (id > 0){
            sb.insert(0, SYMBOLS[id%base]);
            id = id / base;
        }
        while (sb.length()<6){
            sb.insert(0, SYMBOLS[0]);
        }
        return sb.toString();
    }

    
    
    private DataSource RDCZdataSource = null;
    
    public Connection getRDCZConnection() throws SQLException{
        if (RDCZdataSource == null) {// lazy setup of datasource
            Context ctx = null;
            try {
                ctx = new InitialContext();
                RDCZdataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/RDCZDS");
            } catch (NamingException e) {
                throw new IllegalStateException("Error getting RDCZ datasource from JNDI.", e);
            }
        }
        Connection conn = RDCZdataSource.getConnection();
        conn.setAutoCommit(false);
        return conn;
    }

}
