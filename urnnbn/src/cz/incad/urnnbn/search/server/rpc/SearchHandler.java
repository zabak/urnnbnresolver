package cz.incad.urnnbn.search.server.rpc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aplikator.server.Context;
import org.aplikator.server.persistence.Persister;
import org.aplikator.server.persistence.PersisterFactory;
import org.aplikator.server.rpc.CommandHandler;
import org.aplikator.server.util.UTF8ClassLoader;

import cz.incad.urnnbn.search.client.ResultNode;
import cz.incad.urnnbn.search.client.rpc.Search;
import cz.incad.urnnbn.search.client.rpc.SearchResponse;

public class SearchHandler implements CommandHandler<Search, SearchResponse> {
    
    private static final Logger LOG = Logger.getLogger(SearchHandler.class.getName());
    
    private static SearchHandler inst = new SearchHandler();
    
    public static SearchHandler get(){
        return inst;
    }
    
    private Persister persister;
    
    private SearchHandler(){
        this.persister = PersisterFactory.getPersister();
    }
    
    private UTF8ClassLoader UTF8cl = new UTF8ClassLoader();

    private String getLocalizedString(String key, String loc) {
        Locale locale = null;
        if ("en".equals(loc)){
            locale = Locale.ENGLISH;
        }else{
            locale = Locale.ROOT;
        }
        ResourceBundle rb = ResourceBundle.getBundle("resolver", locale, UTF8cl);
        if (rb != null) {
            try {
                return rb.getString(key);
            } catch (MissingResourceException ex) {
                return key;
            }
        }

        return key;
    }

    
    private static final String searchQuery = "select  ie.ie_id, ie.nazev, ie.autor, ie.isbn, ie.issn, ie.ccnb, ie.rocnik_periodika, ie.vydavatel, ie.rok_Vydani, ie.misto_vydani,  "+ 
    " dr.dr_id, dr.urnnbn, dr.cislo_rdcz, dr.format, dr.rozliseni, dr.barevnost, dr.dostupnost, dr.financovano, dr.cislo_zakazky "+
    " from digitalni_reprezentace dr left outer join intelektualni_entita ie on dr.intelektualni_entita = ie.ie_id "+
    " where (upper(dr.urnnbn) like upper(?) and dr.aktivni='1') or upper(ie.isbn) like upper(?) or upper(ie.issn) like upper(?) or upper(ie.ccnb) like upper(?) "; 

    private static final int MAX_RECORDS = 500;
    
    public SearchResponse execute(Search command, Context context) {
        Connection conn = null;
        if (command.getSearchId()!= null && !"".equals(command.getSearchId())){
            try{
                conn = persister.getJDBCConnection();
                PreparedStatement st = conn.prepareStatement(searchQuery);
                for (int i = 1;i<=4;i++){
                    st.setString(i, "%"+command.getSearchId()+"%");
                }
                ResultSet rs = st.executeQuery();
                List<Tuple> list = new ArrayList<Tuple>();
                int counter= 0;
                while (rs.next() && counter++ < MAX_RECORDS) {
                    Tuple tuple = new Tuple();
                    tuple.IE = addLine(getLocalizedString("nazev",command.getLocale()),rs.getObject("nazev"))+addLine(getLocalizedString("autor",command.getLocale()),rs.getObject("autor"))
                            +addLine(getLocalizedString("ccnb",command.getLocale()),rs.getObject("ccnb"))+addLine(getLocalizedString("ISBN",command.getLocale()),rs.getObject("isbn"))
                            +addLine(getLocalizedString("ISSN",command.getLocale()),rs.getObject("issn"))
                            +addLine(getLocalizedString("rocnik",command.getLocale()),rs.getObject("rocnik_periodika"))+addLine(getLocalizedString("vydavatel",command.getLocale()),rs.getObject("vydavatel"))
                            +addLine(getLocalizedString("rokVydani",command.getLocale()),rs.getObject("rok_vydani"))+addLine(getLocalizedString("mistoVydani",command.getLocale()),rs.getObject("misto_vydani"));
                    tuple.DR = addLine(getLocalizedString("urnnbn",command.getLocale()),rs.getObject("urnnbn"))+addLine(getLocalizedString("rdcz",command.getLocale()),rs.getObject("cislo_rdcz"))
                        +addLine(getLocalizedString("financovano",command.getLocale()),rs.getObject("financovano"))+addLine(getLocalizedString("cisloZakazky",command.getLocale()),rs.getObject("cislo_zakazky"))
                        +addLine(getLocalizedString("format",command.getLocale()),rs.getObject("format"))+addLine(getLocalizedString("rozliseni",command.getLocale()),rs.getObject("rozliseni"))
                        +addLine(getLocalizedString("barevnost",command.getLocale()),rs.getObject("barevnost"))+addLine(getLocalizedString("dostupnost",command.getLocale()),rs.getObject("dostupnost"));
                    addURLS(tuple.LOKS, rs.getInt("dr_id"),conn, command.getLocale());
                    list.add(tuple);
                }
                
                if (list.size() == 0){
                    return new SearchResponse(new ResultNode(getLocalizedString("nenalezeno",command.getLocale())));
                } else {
                    ResultNode result = (counter >= MAX_RECORDS) ? new ResultNode(getLocalizedString("overflow",command.getLocale())+" ( > "+MAX_RECORDS +" )"):new ResultNode(getLocalizedString("nalezeno",command.getLocale())+" ( "+list.size()+" )");
                    for (Tuple t: list){
                        ResultNode drNode = new ResultNode(t.DR);
                        for (String lok: t.LOKS){
                            drNode.addChild(new ResultNode(lok));
                        }
                        result.addChild(new ResultNode(t.IE).addChild(drNode));
                    }
                    return new SearchResponse(result);
                }
            }catch(Exception ex){
                LOG.log(Level.SEVERE,"Search error: ",ex);
            }finally{
                if (conn!= null){
                    try{
                        conn.close();
                    }catch(SQLException e){
                        
                    }
                }
            }
        }
        return new SearchResponse(new ResultNode(getLocalizedString("nenalezeno",command.getLocale())));
    }
    
    private String addLine(String label, Object value){
        if (value == null){
            return "";
        }else{
            return label+": <span style=\"color: black;\">"+breakLine(value.toString())+"</span><br>";
        }
    }
    
    private static final int LINE_LENGTH = 160; 
    
    private String breakLine(String value){
        if (value == null) return null;
        if (value.length()>LINE_LENGTH){
            int spacePosition = value.indexOf(" ", LINE_LENGTH);
            if (spacePosition <= 0){
                spacePosition = LINE_LENGTH;
            }
            return value.substring(0, spacePosition)+"<br>&nbsp;&nbsp;&nbsp;"+breakLine(value.substring(spacePosition+1));
        }else{
            return value;
        }
    }
    
    private static class Tuple {
        public String IE;
        public String DR;
        public List<String> LOKS=new ArrayList<String>();
    }
    
    
    private static final String selectZverejneno = "select url from zverejneno where digitalni_reprezentace = ?";
    
    private void addURLS(List<String> loks, Integer id, Connection conn, String loc) throws SQLException{
        ResultSet rs = null;
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(selectZverejneno);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()){
                String url = rs.getString("url");
                loks.add( addLine(getLocalizedString("URL",loc), "<a href='"+url+"' target='_blank'>"+url+"</a>"));
            }
        }finally{
            if (rs != null){
                rs.close();
            }
            if (st != null){
                st.close();
            }
        }
    }
    
    
    public String findRedirect(String id, String library ) {
        Connection conn = null;
        try{
            conn = persister.getJDBCConnection();
            PreparedStatement st = conn.prepareStatement(searchQuery);
            for (int i = 1;i<=4;i++){
                st.setString(i,"%"+ id+"%");
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return findURL(library, rs.getInt("dr_id"),conn);
            }
        }catch(Exception ex){
            LOG.log(Level.SEVERE,"Search error: ",ex);
        }finally{
            if (conn!= null){
                try{
                    conn.close();
                }catch(SQLException e){
                    
                }
            }
        }
        return null;
    }
    
    private String findURL(String library,  Integer id, Connection conn) throws SQLException{
        if (library == null) return null;
        library = library.toUpperCase();
        ResultSet rs = null;
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(selectZverejneno);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()){
                String url = rs.getString("url");
                if (url!= null && url.toUpperCase().contains(library)){
                    return url;
                }
            }
        }finally{
            if (rs != null){
                rs.close();
            }
            if (st != null){
                st.close();
            }
        }
        return null;
    }


}
