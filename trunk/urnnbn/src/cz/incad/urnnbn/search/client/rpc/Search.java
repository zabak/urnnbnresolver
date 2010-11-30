package cz.incad.urnnbn.search.client.rpc;

import org.aplikator.client.rpc.Command;

@SuppressWarnings("serial")
public class Search implements Command<SearchResponse> {

    private String searchId;
    @SuppressWarnings("unused")
    private Search(){}


    public Search(String searchId) {
        super();
        this.searchId = searchId;
    }
    
    public String getSearchId() {
        return searchId;
    }

    
    
}
