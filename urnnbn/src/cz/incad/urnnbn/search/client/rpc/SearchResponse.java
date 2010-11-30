package cz.incad.urnnbn.search.client.rpc;

import org.aplikator.client.rpc.Response;

import cz.incad.urnnbn.search.client.ResultNode;

@SuppressWarnings("serial")
public class SearchResponse implements Response {
    
    private ResultNode results;
    
    @SuppressWarnings("unused")
    private SearchResponse(){}
    
    public SearchResponse(ResultNode r){
        results = r;
    }

    public ResultNode getResults() {
        return results;
    }

}
