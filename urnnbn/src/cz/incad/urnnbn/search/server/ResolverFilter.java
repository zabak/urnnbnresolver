package cz.incad.urnnbn.search.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.incad.urnnbn.search.server.rpc.SearchHandler;


public class ResolverFilter implements Filter {

    private FilterConfig filterConfig = null;
    private SearchHandler searchHandler;
    
    public void init(FilterConfig filterConfig)  throws ServletException {
       this.filterConfig = filterConfig;
       searchHandler = SearchHandler.get();
    }
    
    public void destroy() {
       this.filterConfig = null;
       searchHandler = null;
    }
    
    public void doFilter(ServletRequest request,
       ServletResponse response, FilterChain chain) 
       throws IOException, ServletException {
       if (filterConfig == null)
          return;
       String path = ((HttpServletRequest)request).getServletPath();
       if (path != null && path.toUpperCase().startsWith("/URN:NBN:CZ")){
           String library = ((HttpServletRequest)request).getParameter("library");
           if (library == null){
               ((HttpServletResponse)response).sendRedirect("Main.jsp#"+path.substring(1));
               //filterConfig.getServletContext().getRequestDispatcher("Main.jsp#"+path.substring(1)).forward(request, response);
           }else{
               ((HttpServletResponse)response).sendRedirect(searchHandler.findRedirect(path.substring(1), library));
           }
       }else{
           chain.doFilter(request, response);
       }
    }
}
