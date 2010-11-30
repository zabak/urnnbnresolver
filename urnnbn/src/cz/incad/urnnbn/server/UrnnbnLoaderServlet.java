package cz.incad.urnnbn.server;

import javax.servlet.ServletException;

import org.aplikator.client.command.ExecuteFunction;
import org.aplikator.client.command.ListEntities;
import org.aplikator.client.descriptor.ActionDTO;
import org.aplikator.client.descriptor.ApplicationDTO;
import org.aplikator.client.descriptor.ServiceDTO;
import org.aplikator.server.ApplicationLoaderServlet;
import org.aplikator.server.descriptor.Application;
import org.aplikator.server.descriptor.Arrangement;
import org.aplikator.server.descriptor.CheckBox;
import org.aplikator.server.descriptor.DateField;
import org.aplikator.server.descriptor.Form;
import org.aplikator.server.descriptor.Function;
import org.aplikator.server.descriptor.HorizontalPanel;
import org.aplikator.server.descriptor.QueryGenerator;
import org.aplikator.server.descriptor.RefButton;
import org.aplikator.server.descriptor.RepeatedForm;
import org.aplikator.server.descriptor.TextArea;
import org.aplikator.server.descriptor.TextField;
import org.aplikator.server.descriptor.VerticalPanel;

@SuppressWarnings("serial")
public class UrnnbnLoaderServlet extends ApplicationLoaderServlet {
    Structure struct;
     
    Arrangement digitalniKnihovnaArr;
    Arrangement instituceArr;
    Arrangement zverejnenoArr;
    Arrangement digitalniReprezentaceArr;
    Arrangement intelektualniEntitaArr;
    Function nacistData;
    Function priraditUrnnbn;
   

    @Override
    public void init() throws ServletException {
        try {
            System.out.println("ApplicationLoader started");
            //SERVER SIDE
            System.out.println("ApplicationLoader 1");
            struct = (Structure)Application.get();
            System.out.println("ApplicationLoader 2");
            
            digitalniKnihovnaArr = new Arrangement(struct.digitalniKnihovna);
            digitalniKnihovnaArr.setReadableName(struct.digitalniKnihovna.getName());
            digitalniKnihovnaArr.addProperty(struct.digitalniKnihovna.NAZEV).addProperty(struct.digitalniKnihovna.URL).addProperty(struct.digitalniKnihovna.ADRESA);
            digitalniKnihovnaArr.setSortProperty(struct.digitalniKnihovna.NAZEV);
            digitalniKnihovnaArr.queryGenerator = new QueryGenerator.Empty();
            digitalniKnihovnaArr.form = createDigitalniKnihovnaForm();
           
            instituceArr = new Arrangement(struct.instituce);
            instituceArr.setReadableName(struct.instituce.getName());
            instituceArr.addProperty(struct.instituce.SIGLA).addProperty(struct.instituce.NAZEV).addProperty(struct.instituce.LINK).addProperty(struct.instituce.PREFIX);
            instituceArr.queryGenerator = new QueryGenerator.Empty();
            instituceArr.form = createInstituceForm();
           
            
            zverejnenoArr = new Arrangement(struct.zverejneno);
            zverejnenoArr.setReadableName(struct.zverejneno.getName());
            zverejnenoArr.addProperty(struct.zverejneno.URL).addProperty(struct.zverejneno.ZVEREJNENO_DNE).addProperty(struct.zverejneno.ZVEREJNENO_KYM).addProperty(struct.zverejneno.DIGITALNI_KNIHOVNA.relate(struct.digitalniKnihovna.NAZEV));
            zverejnenoArr.queryGenerator = new QueryGenerator.Empty();
            zverejnenoArr.form = createZverejnenoForm();
           
            
            digitalniReprezentaceArr = new Arrangement(struct.digitalniReprezentace);
            digitalniReprezentaceArr.setReadableName(struct.digitalniReprezentace.getName());
            digitalniReprezentaceArr.addProperty(struct.digitalniReprezentace.URNNBN).addProperty(struct.digitalniReprezentace.CISLO_RDCZ);
            digitalniReprezentaceArr.queryGenerator = new QueryGenerator.Empty();
            digitalniReprezentaceArr.form = createDigitalniReprezentaceForm();
            
            
            
            intelektualniEntitaArr = new Arrangement(struct.intelektualniEntita);
            intelektualniEntitaArr.setReadableName(struct.intelektualniEntita.getName());
            intelektualniEntitaArr.addProperty(struct.intelektualniEntita.NAZEV).addProperty(struct.intelektualniEntita.CCNB);
            intelektualniEntitaArr.queryGenerator = new QueryGenerator.Empty();
            intelektualniEntitaArr.form = createIntelektualniEntitaForm();
            
            nacistData = new Function(new NacistData());
            priraditUrnnbn = new Function(new PriraditUrnnbn());
            
            
            System.out.println("ApplicationLoader 3");
            //CLIENT SIDE MENU
            ApplicationDTO applicationDescriptor = ApplicationDTO.get();
            ServiceDTO places = new ServiceDTO("Tabulky");
            places.addAction(new ActionDTO("DigitalniKnihovna", new ListEntities( "DigitalniKnihovna", places, digitalniKnihovnaArr.getId()) ));
            places.addAction(new ActionDTO("Instituce", new ListEntities( "Instituce", places, instituceArr.getId()) ));
            places.addAction(new ActionDTO("Entita", new ListEntities( "Entita", places, intelektualniEntitaArr.getId()) ));
            places.addAction(new ActionDTO("DigitalniReprezentace", new ListEntities( "DigitalniReprezentace", places, digitalniReprezentaceArr.getId()) ));
            ServiceDTO functions = new ServiceDTO("Funkce");
            functions.addAction(new ActionDTO("NacistData", new ExecuteFunction( "NacistData", functions, nacistData.getId()) ));
            functions.addAction(new ActionDTO("PriraditURNNBN", new ExecuteFunction( "PriraditURNNBN", functions, priraditUrnnbn.getId()) ));
            applicationDescriptor.addService(places);
            applicationDescriptor.addService(functions);
            System.out.println("ApplicationLoader finished");
        } catch (Exception ex) {
            System.out.println("ApplicationLoader error:" + ex);
            throw new ServletException("ApplicationLoader error: ", ex);
        }
    }
    

    private Form createDigitalniKnihovnaForm(){
        Form form = new Form();
        form.setLayout ( new VerticalPanel()
            .addChild(new HorizontalPanel()
                    .addChild(new TextField(struct.digitalniKnihovna.NAZEV))
                    .addChild(new TextField(struct.digitalniKnihovna.URL))
            )
            
            .addChild(new TextArea(struct.digitalniKnihovna.ADRESA))
        );
        return form;
    }
    
    
    private Form createInstituceForm(){
        Form form = new Form();
        form.setLayout ( new VerticalPanel()
            .addChild(new TextField(struct.instituce.SIGLA))
            .addChild(new TextField(struct.instituce.NAZEV))
            .addChild(new TextField(struct.instituce.LINK))
            .addChild(new TextField(struct.instituce.PREFIX))
        );
        return form;
    }
    
    private Form createZverejnenoForm(){
        Form form = new Form();
        form.setLayout ( new VerticalPanel()
            .addChild(new TextField(struct.zverejneno.URL))
            .addChild(new DateField(struct.zverejneno.ZVEREJNENO_DNE))
            .addChild(new TextField(struct.zverejneno.ZVEREJNENO_KYM))
            .addChild(new RefButton(struct.zverejneno.DIGITALNI_KNIHOVNA, digitalniKnihovnaArr,
                    new HorizontalPanel()
                        .addChild(new TextField(struct.zverejneno.DIGITALNI_KNIHOVNA.relate(struct.digitalniKnihovna.NAZEV)))
                        .addChild(new TextField(struct.zverejneno.DIGITALNI_KNIHOVNA.relate(struct.digitalniKnihovna.URL))))
            )
        );
        return form;
    }
    
    private Form createDigitalniReprezentaceForm(){
        Form form = new Form();
        form.setLayout ( new VerticalPanel()
        /*.addChild(new RefButton(struct.digitalniReprezentace.INTELEKTUALNI_ENTITA, intelektualniEntitaArr,
                new HorizontalPanel()
                    .addChild(new TextField(struct.digitalniReprezentace.INTELEKTUALNI_ENTITA.relate(struct.intelektualniEntita.NAZEV)))
                    )
        )*/
            .addChild(new HorizontalPanel()
            
                .addChild(new TextField(struct.digitalniReprezentace.CISLO_RDCZ))
                .addChild(new TextField(struct.digitalniReprezentace.URNNBN))
                .addChild(new CheckBox(struct.digitalniReprezentace.AKTIVNI))
                .addChild(new DateField(struct.digitalniReprezentace.PRIDELENO_DNE))
                .addChild(new TextField(struct.digitalniReprezentace.PRIDELENO_KYM))
                
            )
            .addChild(new HorizontalPanel()
                .addChild(new TextField(struct.digitalniReprezentace.DOSTUPNOST))
                .addChild(new TextField(struct.digitalniReprezentace.BAREVNOST))
                .addChild(new TextField(struct.digitalniReprezentace.FORMAT))
                .addChild(new TextField(struct.digitalniReprezentace.ROZLISENI))
                .addChild(new TextField(struct.digitalniReprezentace.ROZSAH))
            )
            .addChild(new RefButton(struct.digitalniReprezentace.INSTITUCE, instituceArr,
                    new HorizontalPanel()
                        .addChild(new TextField(struct.digitalniReprezentace.INSTITUCE.relate(struct.instituce.NAZEV)))
                        )
            )
            .addChild(new RepeatedForm(struct.digitalniReprezentace.ZVEREJNENO, zverejnenoArr))
        );
        return form;
    }
    
    private Form createIntelektualniEntitaForm(){
        Form form = new Form();
        form.setLayout ( new VerticalPanel()
            .addChild(new TextField(struct.intelektualniEntita.NAZEV))
            .addChild(new HorizontalPanel()
                .addChild(new TextField(struct.intelektualniEntita.CCNB))
                .addChild(new TextField(struct.intelektualniEntita.ISBN))
            )
            .addChild(new TextField(struct.intelektualniEntita.DALSI_NAZEV))
            .addChild(new HorizontalPanel()
                .addChild(new TextField(struct.intelektualniEntita.AUTOR))
                .addChild(new TextField(struct.intelektualniEntita.KORPORACE))
                .addChild(new TextField(struct.intelektualniEntita.AKCE))
            )
            .addChild(new TextField(struct.intelektualniEntita.DRUH_DOKUMENTU))
            .addChild(new HorizontalPanel()
                .addChild(new TextField(struct.intelektualniEntita.VYDAVATEL))
                .addChild(new TextField(struct.intelektualniEntita.MISTO_VYDANI))
                .addChild(new TextField(struct.intelektualniEntita.ROK_VYDANI))
            )
            .addChild(new RepeatedForm(struct.intelektualniEntita.DIGITALNI_REPREZENTACE, digitalniReprezentaceArr))
        );
        return form;
    }
    
    
   
}
