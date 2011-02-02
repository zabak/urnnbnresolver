package cz.incad.urnnbn.search.server.rpc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.aplikator.server.Context;
import org.aplikator.server.persistence.Persister;
import org.aplikator.server.rpc.CommandHandler;

import cz.incad.urnnbn.search.client.ResultNode;
import cz.incad.urnnbn.search.client.rpc.Search;
import cz.incad.urnnbn.search.client.rpc.SearchResponse;

public class SearchHandler implements CommandHandler<Search, SearchResponse> {
    
    private Persister persister;
    
    public SearchHandler(Persister p){
        this.persister=p;
    }
    
    
    private static final String searchQuery = "select  ie.ie_id, ie.nazev, ie.autor, ie.isbn, ie.issn, ie.ccnb, ie.rocnik_periodika, ie.vydavatel, ie.rok_Vydani, ie.misto_vydani, "+ 
    " dr.dr_id, dr.urnnbn, dr.cislo_rdcz, dr.format, dr.rozliseni, dr.barevnost, dr.dostupnost "+
    " from digitalni_reprezentace dr left outer join intelektualni_entita ie on dr.intelektualni_entita = ie.ie_id "+
    " where (dr.urnnbn like ? and dr.aktivni='1') or ie.isbn like ? or ie.issn like ? or ie.ccnb like ? "; 

    //and dr.aktivni=1
    
    public SearchResponse execute(Search command, Context context) {
        Connection conn = null;
        try{
            conn = persister.getJDBCConnection();
            PreparedStatement st = conn.prepareStatement(searchQuery);
            for (int i = 1;i<=4;i++){
                st.setString(i, command.getSearchId()+"%");
            }
            ResultSet rs = st.executeQuery();
            List<Tuple> list = new ArrayList<Tuple>();
            
            while (rs.next()) {
                Tuple tuple = new Tuple();
                tuple.IE = addLine("Název",rs.getObject("nazev"))+addLine("Autor",rs.getObject("autor"))
                        +addLine("čČNB",rs.getObject("ccnb"))+addLine("ISBN",rs.getObject("isbn"))+addLine("ISSN",rs.getObject("issn"))
                        +addLine("Ročník",rs.getObject("rocnik_periodika"))+addLine("Vydavatel",rs.getObject("vydavatel"))
                        +addLine("Rok vydání",rs.getObject("rok_vydani"))+addLine("Místo vydání",rs.getObject("misto_vydani"));
                tuple.DR = addLine("URN:NBN",rs.getObject("urnnbn"))+addLine("Číslo RDCZ",rs.getObject("cislo_rdcz"));
                addURLS(tuple.LOKS, rs.getInt("dr_id"),conn);
                list.add(tuple);
            }
            
            if (list.size() == 0){
                return new SearchResponse(new ResultNode("Nebyly nalezeny žádné záznamy"));
            } else {
                ResultNode result = new ResultNode("Nalezené záznamy ("+list.size()+"):");
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
            System.out.println("Search error: "+ex);
        }finally{
            if (conn!= null){
                try{
                    conn.close();
                }catch(SQLException e){
                    
                }
            }
        }
        return new SearchResponse(new ResultNode("Nebyly nalezeny žádné záznamy"));
        //return new SearchResponse(new ResultNode("<span style='font-size:40px;'>"+command.getSearchId()+"</span>").addChild(new ResultNode("potomek")).addChild(new ResultNode("<a href='http://www.amaio.com' target='_blank'>http://www.amaio.com</a>").addChild(new ResultNode("<b>pod</b>potomek<br>dvouřádkový")).addChild(new ResultNode("druhý<b>pod</b>potomek<br>dvouřádkový"))));
    }
    
    private String addLine(String label, Object value){
        if (value == null){
            return "";
        }else{
            return label+": <span style=\"color: black;\">"+value+"</span><br>";
        }
    }
    
    private static class Tuple {
        public String IE;
        public String DR;
        public List<String> LOKS=new ArrayList<String>();
    }
    
    
    private static final String selectZverejneno = "select url from zverejneno where digitalni_reprezentace = ?";
    private void addURLS(List<String> loks, Integer id, Connection conn) throws SQLException{
        ResultSet rs = null;
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(selectZverejneno);
            st.setInt(1, id);
            rs = st.executeQuery();
            while (rs.next()){
                String url = rs.getString("url");
                loks.add( addLine("URL", "<a href='"+url+"' target='_blank'>"+url+"</a>"));
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

}
