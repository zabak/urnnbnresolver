package cz.incad.urnnbn.search.client;

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
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
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

    private DockLayoutPanel mainPanel;
    private HorizontalPanel searchPanel;
    private ScrollPanel resultsPanel;
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
    
    private Widget languageSwitch;

    public void onModuleLoad() {
        RootLayoutPanel rootPanel = RootLayoutPanel.get();
        mainPanel = new DockLayoutPanel(Unit.PX);
        headerPanel = new HorizontalPanel();
        headerPanel.addStyleName(MainResources.INSTANCE.css().headerPanel());
        headerTitlePanel = new VerticalPanel();
        headerTitlePanel.addStyleName(MainResources.INSTANCE.css().headerTitlePanel());
        headerTitle = new HTML("<a href=\"./\" style=\"text-decoration:none;\"><span style=\"color: black;\">URN</span><span style=\"color: red;\">:</span><span style=\"color: black;\">NBN Resolver</span></a>");
        headerTitle.addStyleName(MainResources.INSTANCE.css().headerTitle());
        headerSubtitle = new Label("Registr identifikátorů digitálních dokumentů");
        headerSubtitle.addStyleName(MainResources.INSTANCE.css().headerSubtitle());
       // headerLogo = new Image(MainResources.INSTANCE.logoBooks());
        headerLogo = new Image("search/logo_books.png");

        searchPanel = new HorizontalPanel();
        searchPanel.addStyleName(MainResources.INSTANCE.css().searchPanel());
        searchPanel.setSpacing(4);

        searchButton = new Button();
        searchButton.setText("Hledat");
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
        searchStatistic.setHTML("Databáze&nbsp;obsahuje:&nbsp;"/*+"<b>"+ info.get("ieCount")+"</b>&nbsp;intelektuálních&nbsp;entit,&nbsp;"*/+
                "<b>"+ info.get("drCount")+"</b>&nbsp;digitálních&nbsp;dokumentů&nbsp;") ;

        tree = new Tree();
        Widget helpLabel = new HTML("Zadejte hledaný identifikátor v některém z následujících tvarů a klikněte na tlačítko <span style=\"color: black;\">Hledat</span>:<br><br>" 
                + "<span style=\"color: black;\">URN:NBN</span> (např.:&nbsp;<span>URN:NBN:CZ:ABA010:00000P</span>&nbsp;)<br>" 
                + "<span style=\"color: black;\">čČNB</span> (např.:&nbsp;<span>cnb001753438</span>&nbsp;)<br>" 
                + "<span style=\"color: black;\">ISBN</span> (např.:&nbsp;<span>80-7051-047-1</span>&nbsp;)<br>"
                + "<span style=\"color: black;\">ISSN</span> (např.:&nbsp;<span>1802-6265</span>&nbsp;)<br>");
        
        tree.addItem(helpLabel);
        
        resultsPanel = new ScrollPanel(tree);
        resultsPanel.addStyleName(MainResources.INSTANCE.css().mainPanel());

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
        
        languageSwitch = new Hyperlink("English","EN");
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
        footerPanel.add(new Anchor("Administrace", false, "Urnnbn.html"));
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
        mainPanel.add(resultsPanel);
        rootPanel.add(mainPanel);
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
        tree.addItem(new HTML("Probíhá&nbsp;hledání..."));
        searchService.execute(new Search(searchBox.getText().trim()), new Callback<SearchResponse>() {
            public void process(SearchResponse resp) {
                tree.removeItems();
                HTML rootLabel = new HTML(resp.getResults().getContents());
                TreeItem root = tree.addItem(rootLabel);
                parseNode(resp.getResults(), root);
                //root.setState(true,true);
                //resultsPanel.setWidget(tree);
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
}
