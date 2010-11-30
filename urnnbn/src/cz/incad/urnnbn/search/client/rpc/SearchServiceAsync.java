package cz.incad.urnnbn.search.client.rpc;

import org.aplikator.client.rpc.Command;
import org.aplikator.client.rpc.Response;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>AplikatorService</code>.
 */
public interface SearchServiceAsync {
    
    <T extends Response> void execute(Command<T> action, AsyncCallback<T> callback); 
}
