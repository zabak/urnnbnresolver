package cz.incad.urnnbn.search.client.rpc;

import org.aplikator.client.rpc.Command;

@SuppressWarnings("serial")
public class Search implements Command<SearchResponse> {

    private String searchId;
    
    private String locale;
    @SuppressWarnings("unused")
    private Search(){}


    public Search(String searchId, String locale) {
        super();
        this.searchId = searchId;
        this.locale = locale;
    }
    
    public String getSearchId() {
        return searchId;
    }

    public String getLocale(){
        return locale;
    }
    
}
