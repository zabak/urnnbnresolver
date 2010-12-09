package cz.incad.urnnbn.search.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultNode implements Serializable {
    
   
    /**
     * 
     */
    private static final long serialVersionUID = 6519041328319048015L;

    private String contents;
    
    private List<ResultNode> children;
    
    public ResultNode(){
        
    }

    
    public ResultNode(String contents){
        //this.contents = "<span>"+contents+"</span>";
        this.contents = contents;
    }
    
    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public List<ResultNode> getChildren() {
        return children;
    }

    public ResultNode addChild(ResultNode child) {
        if (children == null){
            children = new ArrayList<ResultNode>();
        }
        this.children.add(child);
        return this;
    }
    
    

}
