package cz.incad.urnnbn.search.server.rpc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aplikator.client.rpc.Command;
import org.aplikator.client.rpc.Response;
import org.aplikator.server.impl.ContextImpl;
import org.aplikator.server.rpc.CommandHandler;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cz.incad.urnnbn.search.client.rpc.Search;
import cz.incad.urnnbn.search.client.rpc.SearchService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SearchServiceImpl extends RemoteServiceServlet implements SearchService {
    
    private final Map<Class<? extends Command<?>>, CommandHandler<? extends Command<?>, ? extends Response>> handlers = new HashMap<Class<? extends Command<?>>, CommandHandler<? extends Command<?>, ? extends Response>>();
    
    {//init block - register command handlers
        
        handlers.put(Search.class,  SearchHandler.get());
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Response> T execute(Command<T> command){
        CommandHandler<Command<T>, T> handler = (CommandHandler<Command<T>, T>)handlers.get(command.getClass());
        HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
        HttpServletResponse httpServletResponse = this.getThreadLocalResponse();
        return handler.execute(command, new ContextImpl(httpServletRequest, httpServletResponse, null));
    }
    
}
