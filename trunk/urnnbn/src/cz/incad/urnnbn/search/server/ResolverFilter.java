package cz.incad.urnnbn.search.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class ResolverFilter implements Filter {

    private FilterConfig filterConfig = null;
    
    public void init(FilterConfig filterConfig) 
       throws ServletException {
       this.filterConfig = filterConfig;
    }
    public void destroy() {
       this.filterConfig = null;
    }
    public void doFilter(ServletRequest request,
       ServletResponse response, FilterChain chain) 
       throws IOException, ServletException {
       if (filterConfig == null)
          return;
       String path = ((HttpServletRequest)request).getServletPath();
       System.out.println("REQUEST:"+path);
       if (path != null && path.startsWith("/URN:NBN:CZ")){
           filterConfig.getServletContext().getRequestDispatcher("/Main.jsp").forward(request, response);
       }else{
           chain.doFilter(request, response);
       }
    }
}
