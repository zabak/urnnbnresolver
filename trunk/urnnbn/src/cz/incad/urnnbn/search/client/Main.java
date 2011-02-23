package cz.incad.urnnbn.search.client;

import java.util.Arrays;

import org.aplikator.client.descriptor.ActionDTO;
import org.aplikator.client.impl.MainMenuTreeViewModel.Category;
import org.aplikator.client.rpc.Callback;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.incad.urnnbn.search.client.rpc.Search;
import cz.incad.urnnbn.search.client.rpc.SearchResponse;
import cz.incad.urnnbn.search.client.rpc.SearchService;
import cz.incad.urnnbn.search.client.rpc.SearchServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {
    public interface MainResources extends ClientBundle {
        public static final MainResources INSTANCE = GWT.create(MainResources.class);

        @Source("logo_books.png")
        ImageResource logoBooks();

        @Source("logo_nkp.gif")
        ImageResource logoNKP();

        @Source("logo_incad.gif")
        ImageResource logoIncad();

        @Source("Main.css")
        public MainCss css();
    }

    public interface MainCss extends CssResource {
        public String searchPanel();
        
        public String searchBox();
        
        public String searchButton();
        
        public String searchStatistic();

        public String headerPanel();

        public String headerTitle();

        public String headerSubtitle();

        public String headerTitlePanel();
        
        public String languageSwitch();

        public String mainPanel();

        public String footerPanel();

    }
    
    static {
        MainResources.INSTANCE.css().ensureInjected();
    }

    public static final SearchServiceAsync searchService = GWT.create(SearchService.class);
    public static final ResolverConstants constants = GWT.create(ResolverConstants.class);
    public static final ResolverMessages messages = GWT.create(ResolverMessages.class);
    
    private DockLayoutPanel mainPanel;
    private HorizontalPanel searchPanel;
    private ScrollPanel resultsPanel;
    private Frame iframe;
    private Tree tree;
    private HorizontalPanel footerPanel;
    private HorizontalPanel headerPanel;
    private VerticalPanel headerTitlePanel;
    private TextBox searchBox;
    private Button searchButton;
    private HTML searchStatistic;
    private Label headerTitle;
    private Label headerSubtitle;
    private Image headerLogo;
    
    private LocaleInfo localeInfo;
    
    private Widget languageSwitch;

    public void onModuleLoad() {
        localeInfo = LocaleInfo.getCurrentLocale();
        RootLayoutPanel rootPanel = RootLayoutPanel.get();
        mainPanel = new DockLayoutPanel(Unit.PX);
        headerPanel = new HorizontalPanel();
        headerPanel.addStyleName(MainResources.INSTANCE.css().headerPanel());
        headerTitlePanel = new VerticalPanel();
        headerTitlePanel.addStyleName(MainResources.INSTANCE.css().headerTitlePanel());
        headerTitle = new HTML("<a href=\""+("en".equals(localeInfo.getLocaleName())?"?locale=en":"")+"\" style=\"text-decoration:none;\"><span style=\"color: black;\">URN</span><span style=\"color: red;\">:</span><span style=\"color: black;\">NBN Resolver</span></a>");
        headerTitle.addStyleName(MainResources.INSTANCE.css().headerTitle());
        headerSubtitle = new Label(constants.subtitle());
        headerSubtitle.addStyleName(MainResources.INSTANCE.css().headerSubtitle());
       // headerLogo = new Image(MainResources.INSTANCE.logoBooks());
        headerLogo = new Image("search/logo_books.png");

        searchPanel = new HorizontalPanel();
        searchPanel.addStyleName(MainResources.INSTANCE.css().searchPanel());
        searchPanel.setSpacing(4);

        searchButton = new Button();
        searchButton.setText(constants.search());
        searchButton.addStyleName(MainResources.INSTANCE.css().searchButton());
        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                doSearch();
            }
        });

        searchBox = new TextBox();
        searchBox.addStyleName(MainResources.INSTANCE.css().searchBox());
        searchBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()){
                    doSearch();
                }
            }
        });
        
        searchStatistic = new HTML();
        searchStatistic.addStyleName(MainResources.INSTANCE.css().searchStatistic());
        Dictionary info = Dictionary.getDictionary("info");
        searchStatistic.setHTML(messages.statistics( info.get("drCount")));

        tree = new Tree();
         
        
        resultsPanel = new ScrollPanel(tree);
        resultsPanel.addStyleName(MainResources.INSTANCE.css().mainPanel());
        
        
        
        iframe = new Frame("../urnnbnhelp"+("en".equals(localeInfo.getLocaleName())?"/en":""));
        //iframe = new Frame(GWT.getModuleBaseURL().replace("search","urnnbnhelp"));
        //System.out.println("BASE:"+GWT.getModuleBaseURL().replace("urnnbn", "").replace("search","urnnbnhelp"));
        iframe.setSize("100%", "100%");
       
        
        footerPanel = new HorizontalPanel();
        footerPanel.addStyleName(MainResources.INSTANCE.css().footerPanel());

        headerPanel.add(headerTitlePanel);
        Label headerSpacer = new Label("");
        headerPanel.add(headerSpacer);
        headerSpacer.setWidth("100px");
        //headerPanel.setCellWidth(headerSpacer, "100px");

        headerPanel.add(headerLogo);
        Label headerStrut = new Label("");
        headerPanel.add(headerStrut);
        headerPanel.setCellWidth(headerStrut, "100%");
        if ("en".equals(localeInfo.getLocaleName())){
            languageSwitch = new Anchor("Česky","?locale=cs");
        }else{
            languageSwitch = new Anchor("English","?locale=en");
        }
        //languageSwitch.setWidth("100px");
        languageSwitch.addStyleName(MainResources.INSTANCE.css().languageSwitch());
        headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        headerPanel.add(languageSwitch);
                
        headerTitlePanel.add(headerTitle);
        headerTitlePanel.add(headerSubtitle);
        searchPanel.add(searchBox);
        searchPanel.add(searchButton);
        Label searchStrut = new Label("");
        searchPanel.add(searchStrut);
        searchPanel.setCellWidth(searchStrut, "100%");
        searchPanel.add(searchStatistic) ;
        
        footerPanel.setWidth("100%");
        footerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        footerPanel.add(new Anchor(constants.administration(), false, "Urnnbn.html", "_blank"));
        Label footerStrut = new Label("");
        footerPanel.add(footerStrut);
        footerPanel.setCellWidth(footerStrut, "90%");
        
        footerPanel.add(new HTML("Powered&nbsp;by&nbsp;Aplikator"));
        
        Label footerStrutA = new Label("");
        footerPanel.add(footerStrutA);
        footerPanel.setCellWidth(footerStrutA, "1%");

        Image logoIncad = new Image(MainResources.INSTANCE.logoIncad());
        logoIncad.getElement().getStyle().setProperty("verticalAlign", "bottom");
        footerPanel.add(logoIncad);
        
        
        Label footerStrutB = new Label("");
        footerPanel.add(footerStrutB);
        footerPanel.setCellWidth(footerStrutB, "1%");

        Image logoNKP = new Image(MainResources.INSTANCE.logoNKP());
        logoNKP.getElement().getStyle().setProperty("verticalAlign", "bottom");
        footerPanel.add(logoNKP);
        
        mainPanel.addNorth(searchPanel, 38);
        mainPanel.insertNorth(headerPanel, 100, searchPanel);
        mainPanel.addSouth(footerPanel, 40);
        mainPanel.add(iframe);
        rootPanel.add(mainPanel);
        
     // Setup a history handler to reselect the associate menu item.
        final ValueChangeHandler<String> historyHandler = new ValueChangeHandler<
            String>() {
          public void onValueChange(ValueChangeEvent<String> event) {
            searchBox.setText(event.getValue());
            doSearch();
          }
        };
        History.addValueChangeHandler(historyHandler);

        // Show the initial example.
        if (History.getToken().length() > 0) {
          History.fireCurrentHistoryState();
        } 
        
        Scheduler sch = new SchedulerImpl();
        sch.scheduleDeferred(new Command()
        {
            public void execute()
            {
                searchBox.setFocus(true);
            }
        });

    }
    
       
   
    private void doSearch(){
        tree.removeItems();
        tree.addItem(new HTML(constants.searching()));
        //centralPanel.setWidget(resultsPanel);
        mainPanel.remove(iframe);
        mainPanel.add(resultsPanel);
        searchService.execute(new Search(searchBox.getText().trim(), localeInfo.getLocaleName()), new Callback<SearchResponse>() {
            public void process(SearchResponse resp) {
                tree.removeItems();
                HTML rootLabel = new HTML(resp.getResults().getContents());
                TreeItem root = tree.addItem(rootLabel);
                parseNode(resp.getResults(), root);
                //root.setState(true,true);
                
                History.newItem(searchBox.getText().trim(), false);
            }
        });
    }

    private void parseNode(ResultNode node, TreeItem root) {
        if (node.getChildren() == null) {
            return;
        }
        for (ResultNode child : node.getChildren()) {
            parseNode(child, root.addItem(new HTML(child.getContents())));
        }
        root.setState(true, true);
    }
    
    
    private Widget createHelpWidget(){
        VerticalPanel help = new VerticalPanel();
        Widget helpHeader = new HTML( "Upozornění - Resolver URN:NBN je v současnosti v pilotním provozu na datech uložených v NK ČR.<br>"
                + "URN:NBN resolver je aplikace, která umožňuje přidělení, evidenci, správu a vyhledávání trvalých identifikátorů URN:NBN "
                + "pro digitální dokumenty. Resolver udržuje informace přidružené k těmto identifikátorům, zejména základní bibliografické "
                + "a technické údaje. Aplikace URN:NBN resolver je určena pouze pro identifikátory URN:NBN vzniklé v českém prostředí "
                + "(tedy začínajících prefixem urn:nbn:cz).<p>"
                + "Autoritou pro přidělování URN:NBN:CZ je Národní knihovna ČR, která přiřazuje tyto identifikátory s využitím resolveru.<p>"
        );
        
        DisclosurePanel vyhledavani = new DisclosurePanel("Vyhledávání"); 
        vyhledavani.setOpen(true);
        vyhledavani.setAnimationEnabled(true);
        vyhledavani.setContent(new HTML(" Aplikace umožňuje vyhledávat digitální dokumenty primárně podle identifikátoru URN:NBN, dále podle "
                + "identifikátoru čČNB (číslo české národní bibliografie) a čísel ISSN, ISBN (včetně jejich tištěné předlohy).<p>"
                + "<span style=\"color: black;\">URN:NBN</span> (např.:&nbsp;<span>urn:nbn:cz:aba000:0010vv</span>&nbsp;)<br>" 
                + "<span style=\"color: black;\">čČNB</span> (např.:&nbsp;<span>cnb001726942</span>&nbsp;)<br>" 
                + "<span style=\"color: black;\">ISBN</span> (např.:&nbsp;<span>80-7051-047-1</span>&nbsp;)<br>"
                + "<span style=\"color: black;\">ISSN</span> (např.:&nbsp;<span>1803-4217</span>&nbsp;)<br><p>"
                + "Aplikace vyhledává podle jakéhokoliv zadaného řetězce, počet zobrazených nalezených dokumentů je omezen na 500. K přesnému "
                + "vyhledání konkrétního dokumentu je nutné zadat identifikátor v jeho úplném znění podle příkladů výše uvedených.<p>"
                + "Resolver je primárně pouze zprostředkující služba, nezajišťuje zpřístupňování dokumentů. Poskytuje URL odkazy na vlastní "
                + "dokumenty v digitálních knihovnách."
        ));
        
        DisclosurePanel podminky = new DisclosurePanel("Podmínky přidělení identifikátoru URN:NBN"); 
        podminky.setAnimationEnabled(true);
        podminky.setContent(new HTML("URN:NBN může získat pouze digitální dokument, který je/bude uložen v digitálním repozitáři Národní knihovny ČR.<br>"
                + "V aktuální fázi přidělujeme URN:NBN pouze digitalizovaným dokumentům, tj. dokumentům, které vznikly digitalizací tištěné "
                + "předlohy (tištěné monografie, tištěné seriály).<br>"
                + "Archivace v repozitáři NK je povinná pro dokumenty digitalizované z financí rozpočtu NK ČR, dále pro dokumenty vzniklé v projektech "
                + "VISK7, Norské fondy a Povodně (hrazeno MK). Dokumenty z těchto projektů dostaly identifikátory URN:NBN.<p>"
                + "Pokud chce v této chvíli jiná instituce než NK ČR pro své digitální dokumenty identifikátor URN:NBN, musí uložit digitální kopii "
                + "identifikovaného dokumentu do repozitáře NK ČR. V dalších fázích vývoje se počítá s otevřením systému pro další instituce, "
                + "bez podmínky uložení dokumentů v NK ČR.<p>"
                + "Dokument označený identifikátorem URN:NBN musí odpovídat datovému modelu pro URN:NBN."
        ));
         
        DisclosurePanel datovyModel = new DisclosurePanel("Datový model URN:NBN pro digitalizované dokumenty"); 
        datovyModel.setAnimationEnabled(true);
        datovyModel.setContent(new HTML("Pro datový model zavádíme dva základní pojmy: intelektuální entita a digitální reprezentace.<br>"
                + "Intelektuální entitou se zde rozumí tištěná předloha pro digitalizační aktivity.<br>"
                + "Digitální reprezentace je množina všech počítačových souborů, jejichž reprodukcí (například v internetovém prohlížeči) získáme "
                + "jednu intelektuální entitu a která je jako tento jeden celek (jako jedna intelektuální entita) archivována a zpřístupňována "
                + "uživatelům. Např. sto souborů ve formátu JPEG představujících digitalizovanou verzi tištěné knihy Máchův Máj.<br>"
                + "V našem pilotním projektu vznikly všechny digitální reprezentace digitalizací, tedy zatím všechny digitální reprezentace "
                + "jsou digitalizované dokumenty."
        ));
        
        DisclosurePanel zdrojeDat = new DisclosurePanel("Zdroje dat"); 
        zdrojeDat.setAnimationEnabled(true);
        zdrojeDat.setContent(new HTML("V pilotním provozu jsou hlavním zdrojem dat pro resolver metadata z "
                + "<a href=\"http://www.registrdigitalizace.cz\" target=\"_blank\">Registru digitalizace.</a><br>"
                + "V budoucnosti bude resolver rozšířen o možnost sklízet data i z jiných externích aplikací pravděpodobně přes protokol OAI-PMH. "
                + "Další rozvoj bude záviset na spolupráci s externími subjekty."
        ));
        
        HTML helpFooter = new HTML("Pilotní aplikace vznikla v roce 2010 v rámci Výzkumného záměru "
                + "<a href=\"http://www.isvav.cz/researchPlanDetail.do?rowId=MK00002322102\" target=\"_blank\">MK00002322102</a>");
        
            
        help.add(helpHeader);
        help.add(vyhledavani);
        help.add(podminky);
        help.add(datovyModel);
        help.add(zdrojeDat);
        help.add(helpFooter);
        return help;
    }
 
}
