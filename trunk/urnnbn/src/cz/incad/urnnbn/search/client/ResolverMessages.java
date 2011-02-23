package cz.incad.urnnbn.search.client;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.Messages.DefaultMessage;

public interface ResolverMessages extends Messages {
    @DefaultMessage("Databáze&nbsp;obsahuje:&nbsp;"/*+"<b>"+ info.get("ieCount")+"</b>&nbsp;intelektuálních&nbsp;entit,&nbsp;"*/+
            "<b>{0}</b>&nbsp;digitálních&nbsp;dokumentů&nbsp;")
    String statistics(String digitalReps);
}