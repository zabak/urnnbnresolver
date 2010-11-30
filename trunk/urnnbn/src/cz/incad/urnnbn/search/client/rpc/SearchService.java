package cz.incad.urnnbn.search.client.rpc;

import org.aplikator.client.rpc.Command;
import org.aplikator.client.rpc.Response;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("searchService")
public interface SearchService extends RemoteService {
    
    <T extends Response> T execute(Command<T> command);
} 
