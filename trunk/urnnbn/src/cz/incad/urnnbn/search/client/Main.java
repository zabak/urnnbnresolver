package cz.incad.urnnbn.search.client;

import org.aplikator.client.rpc.Callback;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
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
        public static final MainResources INSTANCE =  GWT.create(MainResources.class);
        
        @Source("Main.css")
        public MainCss css();
    }
    
    public interface MainCss extends CssResource {
        public String searchPanel();
        public String headerPanel();
        public String mainPanel();
        
    }
    
    static{
        MainResources.INSTANCE.css().ensureInjected();
    }
    
    public static final SearchServiceAsync searchService = GWT.create(SearchService.class);
    
    private DockLayoutPanel mainPanel;
    private HorizontalPanel searchPanel;
    private ScrollPanel resultsPanel;
    private HorizontalPanel footerPanel;
    
    private TextBox searchBox;
    
	private Button searchButton;
	
	private Widget resultsWidget;
	
	private Label header;
	
	public void onModuleLoad() {
		RootLayoutPanel rootPanel = RootLayoutPanel.get();
		header = new Label("URN:NBN Resolver");
		header.addStyleName(MainResources.INSTANCE.css().headerPanel());
		mainPanel = new DockLayoutPanel(Unit.PX);
		searchPanel = new HorizontalPanel();
		searchPanel.addStyleName(MainResources.INSTANCE.css().searchPanel());
		searchPanel.setSpacing(2);
		
		resultsWidget = new HTML("Zadejte hledaný identifikátor v některém z následujících tvarů a klikněte na tlačítko <b>Hledat</b>:<br>"+
		        "<b>URN:NBN</b> (např: URN:NBN:CZ:ABA000:00002K )<br>"+
		        "<b>čČNB</b> (např: cnb001753438 )<br>"+
		        "<b>ISBN</b> (např: 80-7051-047-1 )<br>"+
		        "<b>ISSN</b> (např: 1802-6265, 0862-5921 )<br>");
		resultsPanel = new ScrollPanel(resultsWidget);
		resultsPanel.addStyleName(MainResources.INSTANCE.css().mainPanel());
        
		footerPanel = new HorizontalPanel();
		footerPanel.getElement().getStyle().setMargin(2, Unit.PX);
        
		searchButton = new Button();
		searchBox = new TextBox();
		searchBox.setWidth("90%");
		
		searchPanel.add(new Label("Identifikátor"));
		searchPanel.add(searchBox);
		searchPanel.add(searchButton);
		Label strut = new Label("");
		searchPanel.add(strut);
		searchPanel.setCellWidth(searchBox, "80%");
		searchPanel.setCellWidth(strut, "20%");
		footerPanel.add(new Anchor("Administrace", false, "Urnnbn.html"));
		//footerPanel.add(new Label("Administrace"));
		
		mainPanel.addNorth(searchPanel, 40);
		mainPanel.insertNorth(header , 60, searchPanel);
		mainPanel.addSouth(footerPanel, 25);
		mainPanel.add(resultsPanel);
		rootPanel.add(mainPanel);
		searchButton.setText("Hledat");
		searchButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
			    searchService.execute(new Search(searchBox.getText()), new Callback<SearchResponse>() {
		            public void process(SearchResponse resp) {
		                //ApplicationDTO application = resp.getApplication();
		               /* treeModel = new MainMenuTreeViewModel( application,  contents,  selectionModel);
		                
		                menu = new CellTree(treeModel, null);
		                menu.setAnimationEnabled(true);
		                menu.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		             // Prefetch examples when opening the Category tree nodes.
		                final List<Category> prefetched = new ArrayList<Category>();
		                 menu.addOpenHandler(new OpenHandler<TreeNode>() {
		                  public void onOpen(OpenEvent<TreeNode> event) {
		                    Object value = event.getTarget().getValue();
		                    if (!(value instanceof Category)) {
		                      return;
		                    }

		                    Category category = (Category) value;
		                    if (!prefetched.contains(category)) {
		                      prefetched.add(category);
		                      Prefetcher.prefetch(category.getSplitPoints());
		                    }
		                  }
		                });

		                // Always prefetch.
		                Prefetcher.start();
*/
		                Tree tree = new Tree();
		                HTML rootLabel = new HTML(resp.getResults().getContents());
		                
		                TreeItem root = tree.addItem(rootLabel);
		                parseNode(resp.getResults(), root);
		                
		                resultsPanel.setWidget(tree); 
		            }
		        });

			    
			    
			    
			    
			    
				
			}
		});
	}
	
	private void parseNode (ResultNode node, TreeItem root){
	    if (node.getChildren()==null){
	        return;
	    }
	    for (ResultNode child: node.getChildren()){
	        parseNode(child, root.addItem(new HTML(child.getContents())));
	    }
	}
}
