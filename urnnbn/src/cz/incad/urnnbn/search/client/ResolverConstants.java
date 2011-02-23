package cz.incad.urnnbn.search.client;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;

public interface ResolverConstants extends Constants{
    @DefaultStringValue("Registr identifikátorů digitálních dokumentů")
    String subtitle();
    
    @DefaultStringValue("Hledat")
    String search();
    
    @DefaultStringValue("Administrace")
    String administration();
    
    @DefaultStringValue("Probíhá&nbsp;hledání&nbsp;...")
    String searching();
}