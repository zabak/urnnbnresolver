package cz.incad.urnnbn.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.empire.db.DBColumnExpr;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.apache.empire.db.DBRecord;
import org.aplikator.server.descriptor.Application;
import org.aplikator.server.function.Executable;
import org.aplikator.server.function.FunctionParameters;
import org.aplikator.server.function.FunctionResult;
import org.aplikator.server.persistence.Persister;
import org.aplikator.server.persistence.PersisterFactory;

public class NacistData implements Executable {
    
    private Persister persister;
    private Structure s;
    private Connection conn;
    private String RDCZPredlohaSelect = "select id, urnnbnflag, urnnbn, idcislo, sigla1, digknihovna, skendjvu, skenjpeg, skengif, skentiff, skenpdf, skentxt, "
        +"rozsah, rozliseni, barevnahloubka, dostupnost, isbn, issn, ccnb, druhdokumentu, nazev, autor, vydavatel, rokvyd, mistovyd, url , publprac, publdate, financovano, cislozakazky "
        +" from Predloha where urnnbnflag = 1 "; 
    
    private String RDCZDigObjSelect = "select id, handler from digobj do left outer join xpreddigobj xdo on do.id = xdo.rDigObjekt where xdo.rPredloha= ?";//TODO pouzit pro dalsi Lokace
    private String RDCZInitUpdate = "update Predloha set urnnbnflag = 1 where urnnbnflag is null and financovano in ('norskeFondy','iop','VISK7', 'povodne')"; 
    private String RDCZInstSelect = "select value, cz from dlists where classname = 'cz.incad.nkp.digital.InsVlastnik'";
    private String RDCZKnihSelect = "select value, cz from dlists where classname = 'cz.incad.nkp.digital.InsDigitalniKnihovna'";
    
    
   
    @Override
    public FunctionResult execute(FunctionParameters parameters,  org.aplikator.server.Context context) {
       int counter = 0;
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
            
            loadInstitutionsMap();
            addNewInstitutions(RDCZconn);
            loadLibrariesMap();
            addNewLibraries(RDCZconn);
            
            //System.out.println("Searching new records in RDCZ");
            long start = System.currentTimeMillis();
            Statement RDCZst = RDCZconn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet RDCZrs = RDCZst.executeQuery(RDCZPredlohaSelect);
            //System.out.println("Search finished in: "+(System.currentTimeMillis()-start)+"ms");
            while(RDCZrs.next()){
                counter++;
                try{
                    //System.out.print("Count"+(counter)+":");
                    importRow(RDCZrs);
                    RDCZrs.updateInt("urnnbnflag", 2);
                    RDCZrs.updateRow();
                    RDCZconn.commit();
                }catch(Exception exx){
                    System.out.println(exx); 
                }
            }
            return new FunctionResult("IMPORTOVANO: "+counter, true);
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
    
    private void importRow (ResultSet RDCZrs) throws SQLException{
        //System.out.println("IDCISLO:"+RDCZrs.getString("idcislo")+", NAZEV:"+RDCZrs.getString("nazev"));
        DBRecord ie = new DBRecord();
        ie.create(s.intelektualniEntita.getTable(), conn);
        
        ie.setValue(s.intelektualniEntita.CCNB.column, RDCZrs.getString("ccnb"));
        ie.setValue(s.intelektualniEntita.ISBN.column, RDCZrs.getString("isbn"));
        ie.setValue(s.intelektualniEntita.ISSN.column, RDCZrs.getString("issn"));
        //ie.setValue(s.intelektualniEntita.JINY_ID.column, RDCZrs.getString("nazev"));//v RDCZ nic takového není
        ie.setValue(s.intelektualniEntita.DRUH_DOKUMENTU.column, RDCZrs.getString("druhdokumentu"));
        ie.setValue(s.intelektualniEntita.NAZEV.column, RDCZrs.getString("nazev"));
        ie.setValue(s.intelektualniEntita.AUTOR.column, RDCZrs.getString("autor"));
        ie.setValue(s.intelektualniEntita.VYDAVATEL.column, RDCZrs.getString("vydavatel"));
        ie.setValue(s.intelektualniEntita.ROK_VYDANI.column, RDCZrs.getString("rokvyd"));
        ie.setValue(s.intelektualniEntita.MISTO_VYDANI.column, RDCZrs.getString("mistovyd"));
        ie.setValue(s.intelektualniEntita.ROCNIK_PERIODIKA.column, RDCZrs.getString("rozsah"));
        ie.update(conn);
        int ie_id = ie.getInt(s.intelektualniEntita.getPrimaryKey().column);
        int institution_id = institutions.get(RDCZrs.getString("sigla1"));
        
        DBRecord dr = new DBRecord();
        dr.create(s.digitalniReprezentace.getTable(), conn);
        dr.setValue(s.digitalniReprezentace.INTELEKTUALNI_ENTITA.column, ie_id);
        dr.setValue(s.digitalniReprezentace.CISLO_RDCZ.column, RDCZrs.getString("idcislo"));
        dr.setValue(s.digitalniReprezentace.INSTITUCE.column, institution_id);
        dr.setValue(s.digitalniReprezentace.FORMAT.column, convertFormat(RDCZrs));
        dr.setValue(s.digitalniReprezentace.ROZLISENI.column, RDCZrs.getString("rozliseni"));
        dr.setValue(s.digitalniReprezentace.BAREVNOST.column, RDCZrs.getString("barevnahloubka"));
        dr.setValue(s.digitalniReprezentace.DOSTUPNOST.column, RDCZrs.getString("dostupnost"));
        dr.setValue(s.digitalniReprezentace.FINANCOVANO.column, RDCZrs.getString("financovano"));
        dr.setValue(s.digitalniReprezentace.CISLO_ZAKAZKY.column, RDCZrs.getString("cislozakazky"));
        dr.update(conn);
        int dr_id = dr.getInt(s.digitalniReprezentace.getPrimaryKey().column);
        int library_id = libraries.get(RDCZrs.getString("digknihovna"));
        
        String URL = RDCZrs.getString("url");
        if(URL!=null && !"".equals(URL)){
            DBRecord zv = new DBRecord();
            zv.create(s.zverejneno.getTable(), conn);
            zv.setValue(s.zverejneno.DIGITALNI_REPREZENTACE.column,dr_id );
            zv.setValue(s.zverejneno.DIGITALNI_KNIHOVNA.column, library_id);
            zv.setValue(s.zverejneno.URL.column, RDCZrs.getString("url"));
            zv.setValue(s.zverejneno.ZVEREJNENO_KYM.column, RDCZrs.getString("publprac"));
            zv.setValue(s.zverejneno.ZVEREJNENO_DNE.column, RDCZrs.getString("publdate"));
            zv.update(conn);
            
            
        }
        conn.commit();
        
        
    }
    
    private String convertFormat(ResultSet RDCZrs) throws SQLException{
        StringBuilder sb = new StringBuilder();
        if (RDCZrs.getBoolean("skendjvu")){
            sb.append("DJVU");
        }
        if (RDCZrs.getBoolean("skenjpeg")){
            if(sb.length()>0){
                sb.append(", ");
            }
            sb.append("JPEG");
        }
        if (RDCZrs.getBoolean("skengif")){
            if(sb.length()>0){
                sb.append(", ");
            }
            sb.append("GIF");
        }
        if (RDCZrs.getBoolean("skentiff")){
            if(sb.length()>0){
                sb.append(", ");
            }
            sb.append("TIFF");
        }
        if (RDCZrs.getBoolean("skenpdf")){
            if(sb.length()>0){
                sb.append(", ");
            }
            sb.append("PDF");
        }
        if (RDCZrs.getBoolean("skentxt")){
            if(sb.length()>0){
                sb.append(", ");
            }
            sb.append("TXT");
        }
        return sb.toString();
    }
    
    private void addNewInstitutions(Connection RDCZconn) throws SQLException{
        Statement RDCZst = RDCZconn.createStatement();
        ResultSet RDCZrs = RDCZst.executeQuery(RDCZInstSelect);
        try{
            while(RDCZrs.next()){
               String SIGLA = RDCZrs.getString("value");
               String nazev = RDCZrs.getString("cz");
               if (!institutions.containsKey(SIGLA)){
                   createInstitution(SIGLA,nazev);
               }
            }
        }finally{
            RDCZrs.close();
        }
    }
    
    private void createInstitution(String SIGLA, String nazev) throws SQLException{
        DBRecord dr = new DBRecord();
        dr.create(s.instituce.getTable(), conn);
        dr.setValue(s.instituce.SIGLA.column, SIGLA);
        dr.setValue(s.instituce.NAZEV.column, nazev);
        dr.setValue(s.instituce.PREFIX.column, "URN:NBN:CZ:"+SIGLA+":");
        dr.update(conn);
        conn.commit();
        Integer id = dr.getInt(s.instituce.getPrimaryKey().column);
        institutions.put(SIGLA,id);
    }
    
    private Map<String, Integer> institutions;

    private void loadInstitutionsMap(){
        institutions = new HashMap<String, Integer>();
        DBCommand cmd = s.db.createCommand();
        // Select required columns
        DBColumnExpr[] columns = new DBColumnExpr[]{s.instituce.getPrimaryKey().column, s.instituce.SIGLA.column};   
        cmd.select(columns);
        // Query Records and print output
        DBReader reader = new DBReader();
        try
        {
            // Open Reader
            //System.out.println("Running Query:");
            //System.out.println(cmd.getSelect());
            if (reader.open(cmd, conn) == false)
                throw new RuntimeException(reader.getErrorMessage());
            while (reader.moveNext())
            {
                institutions.put(reader.getString(s.instituce.SIGLA.column), reader.getInt(s.instituce.getPrimaryKey().column));
                
            }

        } finally
        {
            // always close Reader
            reader.close();
        }
    }
    
    
    private void addNewLibraries(Connection RDCZconn) throws SQLException{
        Statement RDCZst = RDCZconn.createStatement();
        ResultSet RDCZrs = RDCZst.executeQuery(RDCZKnihSelect);
        try{
            while(RDCZrs.next()){
               String kod = RDCZrs.getString("value");
               String nazev = RDCZrs.getString("cz");
               if (!libraries.containsKey(kod)){
                   createLibrary(kod,nazev);
               }
            }
        }finally{
            RDCZrs.close();
        }
    }
    
    private void createLibrary(String kod, String nazev) throws SQLException{
        DBRecord dr = new DBRecord();
        dr.create(s.digitalniKnihovna.getTable(), conn);
        dr.setValue(s.digitalniKnihovna.KODRDCZ.column, kod);
        dr.setValue(s.digitalniKnihovna.NAZEV.column, nazev);
        dr.update(conn);
        conn.commit();
        Integer id = dr.getInt(s.digitalniKnihovna.getPrimaryKey().column);
        libraries.put(kod,id);
    }
    
    private Map<String, Integer> libraries;

    private void loadLibrariesMap(){
        libraries = new HashMap<String, Integer>();
        DBCommand cmd = s.db.createCommand();
        // Select required columns
        DBColumnExpr[] columns = new DBColumnExpr[]{s.digitalniKnihovna.getPrimaryKey().column, s.digitalniKnihovna.KODRDCZ.column};   
        cmd.select(columns);
        // Query Records and print output
        DBReader reader = new DBReader();
        try
        {
            // Open Reader
            //System.out.println("Running Query:");
            //System.out.println(cmd.getSelect());
            if (reader.open(cmd, conn) == false)
                throw new RuntimeException(reader.getErrorMessage());
            while (reader.moveNext())
            {
                libraries.put(reader.getString(s.digitalniKnihovna.KODRDCZ.column), reader.getInt(s.digitalniKnihovna.getPrimaryKey().column));
                
            }

        } finally
        {
            // always close Reader
            reader.close();
        }
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
