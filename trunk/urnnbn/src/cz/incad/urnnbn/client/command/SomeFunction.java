package cz.incad.urnnbn.client.command;

import org.aplikator.client.command.BindableCommand;
import org.aplikator.client.descriptor.ServiceDTO;
import org.aplikator.client.widgets.TextFieldWidget;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

@SuppressWarnings("serial")
public class SomeFunction extends BindableCommand {

    @SuppressWarnings("unused")
    private SomeFunction() {
    }

    public SomeFunction(String tabName, ServiceDTO service) {
        super(tabName, service);
    }

    @Override
    public void execute() {
        if (contents == null) {
            VerticalPanel vSplit = new VerticalPanel();
            // vSplit.setSize("600px", "400px");
            vSplit.add(new TextFieldWidget("WizardField1", null,  false, null));
            vSplit.add(new TextFieldWidget("WizardField2", null, false, null));
            vSplit.add(new Button("Execute"));
            SimplePanel decPanel = new SimplePanel();
            // decPanel.setSize("800px", "450px");
            decPanel.setWidget(vSplit);
            contents = decPanel;
        }
        mainPanel.setContents(contents);
        mainPanel.setTitle(tabName);
        // mainPanel.add(decPanel, tabName);
        // mainPanel.selectTab(mainPanel.getWidgetCount()-1);

    }

}
