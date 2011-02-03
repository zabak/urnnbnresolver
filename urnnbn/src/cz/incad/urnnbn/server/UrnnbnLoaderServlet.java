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
            
            
            nacistData = new Function("NacistData", new NacistData());
            priraditUrnnbn = new Function("PriraditUrnnbn", new PriraditUrnnbn());
            
            digitalniKnihovnaArr = new Arrangement(struct.digitalniKnihovna);
            digitalniKnihovnaArr.addProperty(struct.digitalniKnihovna.NAZEV).addProperty(struct.digitalniKnihovna.URL).addProperty(struct.digitalniKnihovna.ADRESA);
            digitalniKnihovnaArr.setSortProperty(struct.digitalniKnihovna.NAZEV);
            digitalniKnihovnaArr.setForm(createDigitalniKnihovnaForm());
           
            instituceArr = new Arrangement(struct.instituce);
            instituceArr.addProperty(struct.instituce.SIGLA).addProperty(struct.instituce.NAZEV).addProperty(struct.instituce.LINK).addProperty(struct.instituce.PREFIX);
            instituceArr.setForm(createInstituceForm());
           
            
            zverejnenoArr = new Arrangement(struct.zverejneno);
            zverejnenoArr.addProperty(struct.zverejneno.URL).addProperty(struct.zverejneno.ZVEREJNENO_DNE).addProperty(struct.zverejneno.ZVEREJNENO_KYM).addProperty(struct.zverejneno.DIGITALNI_KNIHOVNA.relate(struct.digitalniKnihovna.NAZEV));
            zverejnenoArr.setForm(createZverejnenoForm());
           
            
            digitalniReprezentaceArr = new Arrangement(struct.digitalniReprezentace);
            digitalniReprezentaceArr.addProperty(struct.digitalniReprezentace.URNNBN).addProperty(struct.digitalniReprezentace.CISLO_RDCZ);
            digitalniReprezentaceArr.setForm(createDigitalniReprezentaceForm());
            
            
            
            intelektualniEntitaArr = new Arrangement(struct.intelektualniEntita);
            intelektualniEntitaArr.addProperty(struct.intelektualniEntita.NAZEV).addProperty(struct.intelektualniEntita.CCNB);
            intelektualniEntitaArr.setForm(createIntelektualniEntitaForm());
            
            
            
            System.out.println("ApplicationLoader 3");
            //CLIENT SIDE MENU
            ApplicationDTO applicationDescriptor = ApplicationDTO.get();
            ServiceDTO places = new ServiceDTO("Tabulky");
            places.addAction(new ActionDTO("DigitalniKnihovna", new ListEntities( "DigitalniKnihovna", places, digitalniKnihovnaArr.getId()) ));
            places.addAction(new ActionDTO("Instituce", new ListEntities( "Instituce", places, instituceArr.getId()) ));
            places.addAction(new ActionDTO("Entita", new ListEntities( "Entita", places, intelektualniEntitaArr.getId()) ));
            places.addAction(new ActionDTO("DigitalniReprezentace", new ListEntities( "DigitalniReprezentace", places, digitalniReprezentaceArr.getId()) ));
            ServiceDTO functions = new ServiceDTO("Funkce");
            functions.addAction(new ActionDTO("NacistData", new ExecuteFunction(  functions, nacistData.getFunctionDTO(null)) ));//TODO null context
            functions.addAction(new ActionDTO("PriraditURNNBN", new ExecuteFunction(  functions, priraditUrnnbn.getFunctionDTO(null)) ));//TODO null context
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
                .addChild(new TextField(struct.digitalniKnihovna.KODRDCZ))
                .addChild(new TextField(struct.digitalniKnihovna.NAZEV).setWidth("30em"))
                .addChild(new TextField(struct.digitalniKnihovna.URL).setWidth("50em"))
            )
            .addChild(new TextArea(struct.digitalniKnihovna.ADRESA))
        );
        return form;
    }
    
    
    private Form createInstituceForm(){
        Form form = new Form();
        form.setLayout ( new VerticalPanel()
            .addChild(new HorizontalPanel()
                .addChild(new TextField(struct.instituce.SIGLA))
                .addChild(new TextField(struct.instituce.NAZEV))
            ).addChild(new HorizontalPanel()
                .addChild(new TextField(struct.instituce.PREFIX))
                .addChild(new TextField(struct.instituce.LINK))
            )
        );
        return form;
    }
    
    private Form createZverejnenoForm(){
        Form form = new Form();
        form.setLayout ( new VerticalPanel()
            .addChild(new TextField(struct.zverejneno.URL).setWidth("30em"))
            .addChild(new HorizontalPanel()
                .addChild(new DateField(struct.zverejneno.ZVEREJNENO_DNE))
                .addChild(new TextField(struct.zverejneno.ZVEREJNENO_KYM).setWidth("30em"))
            ).addChild(new RefButton(struct.zverejneno.DIGITALNI_KNIHOVNA, digitalniKnihovnaArr,
                 new HorizontalPanel()
                     .addChild(new TextField(struct.zverejneno.DIGITALNI_KNIHOVNA.relate(struct.digitalniKnihovna.NAZEV)).setWidth("30em"))
                     .addChild(new TextField(struct.zverejneno.DIGITALNI_KNIHOVNA.relate(struct.digitalniKnihovna.URL)).setWidth("30em"))
                 )
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
                .addChild(new TextField(struct.digitalniReprezentace.CISLO_RDCZ).setWidth("20em"))
                .addChild(new TextField(struct.digitalniReprezentace.URNNBN))
                .addChild(new CheckBox(struct.digitalniReprezentace.AKTIVNI))
                .addChild(priraditUrnnbn)
            ).addChild(new HorizontalPanel()
                .addChild(new DateField(struct.digitalniReprezentace.PRIDELENO_DNE))
                .addChild(new TextField(struct.digitalniReprezentace.PRIDELENO_KYM))
            ).addChild(new HorizontalPanel()
                .addChild(new TextField(struct.digitalniReprezentace.DOSTUPNOST).setWidth("30em"))
                .addChild(new TextField(struct.digitalniReprezentace.BAREVNOST).setWidth("30em"))
                .addChild(new TextField(struct.digitalniReprezentace.FORMAT).setWidth("30em"))
            ).addChild(new HorizontalPanel()
                .addChild(new TextField(struct.digitalniReprezentace.ROZLISENI).setWidth("30em"))
                .addChild(new TextField(struct.digitalniReprezentace.ROZSAH).setWidth("30em"))
            ).addChild(new HorizontalPanel()
                .addChild(new RefButton(struct.digitalniReprezentace.INSTITUCE, instituceArr,
                    new HorizontalPanel()
                        .addChild(new TextField(struct.digitalniReprezentace.INSTITUCE.relate(struct.instituce.NAZEV)).setWidth("30em"))
                    )
                ).addChild(new RepeatedForm(struct.digitalniReprezentace.ZVEREJNENO, zverejnenoArr))
            )
        );
        return form;
    }
    
    private Form createIntelektualniEntitaForm(){
        Form form = new Form();
        form.setLayout ( new VerticalPanel()
            .addChild(new HorizontalPanel()
                .addChild(new TextField(struct.intelektualniEntita.CCNB).setWidth("30em"))
                .addChild(new TextField(struct.intelektualniEntita.ISBN).setWidth("30em"))
            ).addChild(new HorizontalPanel()
                .addChild(new TextField(struct.intelektualniEntita.ISSN).setWidth("30em"))
                .addChild(new TextField(struct.intelektualniEntita.JINY_ID).setWidth("30em"))
            ).addChild(new TextField(struct.intelektualniEntita.NAZEV).setWidth("80em"))
            .addChild(new TextField(struct.intelektualniEntita.DALSI_NAZEV).setWidth("80em"))
            .addChild(new HorizontalPanel()
                .addChild(new TextField(struct.intelektualniEntita.AUTOR).setWidth("30em"))
                .addChild(new TextField(struct.intelektualniEntita.KORPORACE).setWidth("30em"))
                .addChild(new TextField(struct.intelektualniEntita.AKCE).setWidth("30em"))
            ).addChild(new HorizontalPanel()
                .addChild(new TextField(struct.intelektualniEntita.ROCNIK_PERIODIKA).setWidth("20em"))
                .addChild(new TextField(struct.intelektualniEntita.CISLO_PERIODIKA).setWidth("20em"))
            ).addChild(new TextField(struct.intelektualniEntita.DRUH_DOKUMENTU).setWidth("30em"))
            .addChild(new HorizontalPanel()
                .addChild(new TextField(struct.intelektualniEntita.VYDAVATEL).setWidth("30em"))
                .addChild(new TextField(struct.intelektualniEntita.MISTO_VYDANI).setWidth("30em"))
                .addChild(new TextField(struct.intelektualniEntita.ROK_VYDANI).setWidth("30em"))
            ).addChild(new HorizontalPanel()
                .addChild(new TextField(struct.intelektualniEntita.FINANCOVANO).setWidth("20em"))
                .addChild(new TextField(struct.intelektualniEntita.CISLO_ZAKAZKY).setWidth("20em"))
            ).addChild(new RepeatedForm(struct.intelektualniEntita.DIGITALNI_REPREZENTACE, digitalniReprezentaceArr))
        );
        return form;
    }
    
    
   
}
