package cz.incad.urnnbn.server;

import org.aplikator.client.data.ListItem;
import org.aplikator.client.descriptor.PropertyType;
import org.aplikator.server.descriptor.Application;
import org.aplikator.server.descriptor.Blob;
import org.aplikator.server.descriptor.Collection;
import org.aplikator.server.descriptor.Entity;
import org.aplikator.server.descriptor.Property;
import org.aplikator.server.descriptor.Reference;

public class Structure extends Application {

    public class IntelektualniEntita extends Entity {
        public final Property CCNB;
        public final Property ISBN;
        public final Property ISSN;
        public final Property JINY_ID;
        public final Property DRUH_DOKUMENTU;
        public final Property NAZEV;
        public final Property DALSI_NAZEV;
        public final Property AUTOR;
        public final Property KORPORACE;
        public final Property AKCE;
        public final Property VYDAVATEL;
        public final Property ROK_VYDANI;
        public final Property MISTO_VYDANI;
        public final Property ROCNIK_PERIODIKA;
        public final Property CISLO_PERIODIKA;
        public final Property FINANCOVANO;
        public final Property CISLO_ZAKAZKY;
        public Collection DIGITALNI_REPREZENTACE;

        public IntelektualniEntita() {
            super("IntelektualniEntita", "INTELEKTUALNI_ENTITA", "IE_ID", Structure.this);
            CCNB= addProperty("CCNB", PropertyType.STRING, 255, false);
            ISBN = addProperty("ISBN", PropertyType.STRING, 255, false);
            ISSN= addProperty("ISSN", PropertyType.STRING, 255, false);
            JINY_ID= addProperty("JINY_ID", PropertyType.STRING, 255, false);
            DRUH_DOKUMENTU= addProperty("DRUH_DOKUMENTU", PropertyType.STRING, 255, false);
            NAZEV= addProperty("NAZEV", PropertyType.STRING, 1000, false);
            DALSI_NAZEV= addProperty("DALSI_NAZEV", PropertyType.STRING, 1000, false);
            AUTOR= addProperty("AUTOR", PropertyType.STRING, 300, false);
            KORPORACE= addProperty("KORPORACE", PropertyType.STRING, 300, false);
            AKCE= addProperty("AKCE", PropertyType.STRING, 300, false);
            VYDAVATEL= addProperty("VYDAVATEL", PropertyType.STRING, 200, false);
            ROK_VYDANI= addProperty("ROK_VYDANI", PropertyType.STRING, 200, false);
            ROCNIK_PERIODIKA= addProperty("ROCNIK_PERIODIKA", PropertyType.STRING, 200, false);
            CISLO_PERIODIKA= addProperty("CISLO_PERIODIKA", PropertyType.STRING, 200, false);
            MISTO_VYDANI= addProperty("MISTO_VYDANI", PropertyType.STRING, 255, false);
            FINANCOVANO= addProperty("FINANCOVANO", PropertyType.STRING, 255, false);
            CISLO_ZAKAZKY= addProperty("CISLO_ZAKAZKY", PropertyType.STRING, 255, false);
            
            addIndex("CCNB_IDX", false, CCNB);
            addIndex("ISBN_IDX", false, ISBN);
            addIndex("ISSN_IDX", false, ISSN);
            addIndex("JINY_ID_IDX", false, JINY_ID);
            addIndex("NAZEV_IDX", false, NAZEV);
            addIndex("AUTOR_IDX", false, AUTOR);
            addIndex("ROCNIK_PERIODIKA_IDX", false, ROCNIK_PERIODIKA);
            addIndex("CISLO_PERIODIKA_IDX", false, CISLO_PERIODIKA);
        }

    }

    public class DigitalniReprezentace extends Entity {
        public final Property CISLO_RDCZ;
        public final Property URNNBN;
        public final Property PRIDELENO_DNE;
        public final Property PRIDELENO_KYM;
        public final Property AKTIVNI;
        public final Property FORMAT;
        public final Property ROZSAH;
        public final Property ROZLISENI;
        public final Property BAREVNOST;
        public final Property DOSTUPNOST;
        public Reference INTELEKTUALNI_ENTITA;
        public Collection ZVEREJNENO;
        public Reference INSTITUCE;

        public DigitalniReprezentace() {
            super("DigitalniReprezentace", "DIGITALNI_REPREZENTACE", "DR_ID", Structure.this);
            CISLO_RDCZ= addProperty("CISLO_RDCZ", PropertyType.STRING, 255, false);
            URNNBN= addProperty("URNNBN", PropertyType.STRING, 50, false);
            PRIDELENO_DNE= addProperty("PRIDELENO_DNE", PropertyType.DATE, 0, false);
            PRIDELENO_KYM= addProperty("PRIDELENO_KYM", PropertyType.STRING, 50, false);
            AKTIVNI= addProperty("AKTIVNI", PropertyType.BOOLEAN, 0, false);
            FORMAT= addProperty("FORMAT", PropertyType.STRING, 200, false);
            ROZSAH= addProperty("ROZSAH", PropertyType.STRING, 200, false);
            ROZLISENI= addProperty("ROZLISENI", PropertyType.STRING, 200, false);
            BAREVNOST= addProperty("BAREVNOST", PropertyType.STRING, 200, false);
            DOSTUPNOST= addProperty("DOSTUPNOST", PropertyType.STRING, 200, false);
           
            INSTITUCE = addReference(instituce, "INSTITUCE");
            
            addIndex("CISLO_RDCZ_IDX", true, CISLO_RDCZ);
            addIndex("URNNBN_IDX", true, URNNBN);
            addIndex("AKTIVNI_IDX", false, AKTIVNI);
           
        }

    }
    
    public class Zverejneno extends Entity {
        public final Property URL;
        public final Property ZVEREJNENO_DNE;
        public final Property ZVEREJNENO_KYM;
        public Reference DIGITALNI_KNIHOVNA;
        public Reference DIGITALNI_REPREZENTACE;
        
        public Zverejneno() {
            super("Zverejneno", "ZVEREJNENO", "ZVEREJNENO_ID", Structure.this);
            URL= addProperty("URL", PropertyType.STRING, 200, false);
            ZVEREJNENO_DNE= addProperty("ZVEREJNENO_DNE", PropertyType.DATE, 0, false);
            ZVEREJNENO_KYM= addProperty("ZVEREJNENO_KYM", PropertyType.STRING, 255, false);
            DIGITALNI_KNIHOVNA = addReference(digitalniKnihovna, "DIGITALNI_KNIHOVNA");
            addIndex("URL_IDX", false, URL);
           
        }

    }
    
    public class Instituce extends Entity {
        public final Property SIGLA;
        public final Property NAZEV;
        public final Property LINK;
        public final Property PREFIX;
        
        public Instituce() {
            super("Instituce", "INSTITUCE", "INSTITUCE_ID", Structure.this);
            SIGLA= addProperty("SIGLA", PropertyType.STRING, 20, false);
            NAZEV= addProperty("NAZEV", PropertyType.STRING, 200, false);
            LINK= addProperty("LINK", PropertyType.STRING, 200, false);
            PREFIX= addProperty("PREFIX", PropertyType.STRING, 50, false);
            addIndex("SIGLA_IDX", true, SIGLA);
            addIndex("NAZEV_INSTITUCE_IDX", false, NAZEV);
        }
    }
    
    public class DigitalniKnihovna extends Entity {
        public final Property KODRDCZ;
        public final Property NAZEV;
        public final Property URL;
        public final Property ADRESA;
        
        public DigitalniKnihovna() {
            super("DigitalniKnihovna", "DIGITALNI_KNIHOVNA", "DK_ID", Structure.this);
            KODRDCZ= addProperty("KODRDCZ", PropertyType.STRING, 20, false);
            NAZEV= addProperty("NAZEV", PropertyType.STRING, 200, false);
            URL= addProperty("URL", PropertyType.STRING, 200, false);
            ADRESA= addProperty("ADRESA", PropertyType.STRING, 400, false);
            addIndex("NAZEV_KNIHOVNY_IDX", false, NAZEV);
            addIndex("KOD_KNIHOVNY_IDX", true, KODRDCZ);
        }
    }

    public final DigitalniKnihovna digitalniKnihovna = new DigitalniKnihovna();
    public final Instituce instituce = new Instituce();
    public final Zverejneno zverejneno = new Zverejneno();
    public final DigitalniReprezentace digitalniReprezentace = new DigitalniReprezentace();
    public final IntelektualniEntita intelektualniEntita = new IntelektualniEntita();

    public Structure() {
        zverejneno.DIGITALNI_REPREZENTACE = zverejneno.addReference(digitalniReprezentace, "DIGITALNI_REPREZENTACE");
        digitalniReprezentace.ZVEREJNENO = digitalniReprezentace.addReverseCollection("DIGITALNI_REPREZENTACE", zverejneno, zverejneno.DIGITALNI_REPREZENTACE);
        digitalniReprezentace.INTELEKTUALNI_ENTITA = digitalniReprezentace.addReference(intelektualniEntita, "INTELEKTUALNI_ENTITA");
        intelektualniEntita.DIGITALNI_REPREZENTACE = intelektualniEntita.addReverseCollection("INTELEKTUALNI_ENTITA", digitalniReprezentace, digitalniReprezentace.INTELEKTUALNI_ENTITA);
    }
}
