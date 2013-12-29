package jadex.agentkeeper.view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jadex.agentkeeper.game.state.buildings.Treasury;
import jadex.agentkeeper.game.state.creatures.SimpleCreatureState;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.game.userinput.magicSpells.ImpCreationSpell;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.util.ISpaceObject;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.view.selection.SelectionMode;
import jadex.agentkeeper.log.PerformanceTracker;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.SpellType;
import jadex.extension.envsupport.environment.ISpaceController;
import jadex.extension.envsupport.math.Vector2Int;
import jadex.extension.envsupport.observer.graphics.jmonkey.MonkeyApp;
import jadex.extension.envsupport.observer.graphics.jmonkey.appstate.gui.DefaultGuiController;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.scene.Node;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;


/**
 * The Methods from all GUI Input are implemented here
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 */
public class KeeperGuiController extends DefaultGuiController
{


	private Node				rootNode;

	private boolean				toggleStats	= true;
	

	private boolean showStats = false;
	private boolean showGrid = false;
	private boolean showBars = false;

	private ISpaceController	spaceController;

	private MonkeyApp			app;

	private SimpleCreatureState	creatureState;

	private SimplePlayerState	playerState;
	
	private TextRenderer manaStatusRenderer;
	
	private TextRenderer goldStatusRenderer;
	
	private TextRenderer claimedTilesRender;
	
	private TextRenderer impR;
	
	private TextRenderer goblinR;
	
	private TextRenderer warlockR;
	
	private TextRenderer orcR;
	
	private Map<String,List<String>> tabImageMapping = new HashMap<String,List<String>>();
	
	private Map<String,List<String>> buildingImageMapping = new HashMap<String,List<String>>();
	
	private Map<String,List<String>> settingImageMapping = new HashMap<String,List<String>>();
	
	private Map<String,List<String>> spellImageMapping = new HashMap<String,List<String>>();

	public KeeperGuiController(SimpleApplication app, ISpaceController spacecontroller)
	{
		this.app = (MonkeyApp)app;
		rootNode = this.app.getRootNode();

		this.spaceController = spacecontroller;
		this.creatureState = (SimpleCreatureState)spaceController.getProperty(ISpaceStrings.CREATURE_STATE);
		this.playerState = (SimplePlayerState)spaceController.getProperty(ISpaceStrings.PLAYER_STATE);
		
		tabImageMapping.put(Tabs.SETTINGS, Arrays.asList("auto-repair.png", "auto-repair_selected.png"));
		tabImageMapping.put(Tabs.CREATURE, Arrays.asList("monsterCreation.png","monsterCreation_selected.png"));
		tabImageMapping.put(Tabs.BUILDING, Arrays.asList("building.png","building_selected.png"));
		tabImageMapping.put(Tabs.CREATE_IMP, Arrays.asList("imp_spell.png","imp_spell_selected.png"));
		
		buildingImageMapping.put(Buildings.Lair, Arrays.asList("bed.png","bed_selected.png"));
		buildingImageMapping.put(Buildings.Hatchery, Arrays.asList("hatchery.png","hatchery_selected.png"));
		buildingImageMapping.put(Buildings.Treasury, Arrays.asList("treasury.png","treasury_selected.png"));
		buildingImageMapping.put(Buildings.Trainingsroom, Arrays.asList("trainingroom.png","trainingroom_selected.png"));
		buildingImageMapping.put(Buildings.Library, Arrays.asList("library.png","library_selected.png"));
		
		settingImageMapping.put(Settings.Fullscreen, Arrays.asList("fullscreen.png","fullscreen_selected.png"));
		settingImageMapping.put(Settings.Bars, Arrays.asList("show_monster_bars.png","show_monster_bars_selected.png"));
		settingImageMapping.put(Settings.LogChart, Arrays.asList("performance_chart.png","performance_chart_selected.png"));
		settingImageMapping.put(Settings.Grid, Arrays.asList("grid.png","grid_selected.png"));
		settingImageMapping.put(Settings.Stats, Arrays.asList("stats.png","stats_selected.png"));
		
//		spellImageMapping.put(Spell.CreateImp, Arrays.asList("imp_spell.png","imp_spell_selected.png"));
	}
	float testCounter = 0.0f;
	
	public void calculateGoldPercentageAndDraw() {
		if (Treasury.currentAmount > 0) {
			testCounter = ((float)Treasury.currentAmount / (float)Treasury.totalPossibleAmount);
		} else {
			testCounter = 0.0f;
		}
		setGoldPercentage(testCounter);
	}

	public void setGoldPercentage(float percent) {
		Element goldIcon_grey = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("GoldIcon_grey");
		int maxIconSize = 32;
		int percentSize = (int) (maxIconSize * percent);
		if(percentSize > maxIconSize) {
			percentSize =  maxIconSize;
		}
		NiftyImage image = goldIcon_grey.getRenderer(ImageRenderer.class).getImage();
		image.getImageMode().setParameters("subImageDirect:0,0,"+(maxIconSize-percentSize)+",32");
		goldIcon_grey.setWidth((maxIconSize-percentSize));
	}


	/** Nifty GUI ScreenControl methods */
	public void bind(Nifty nifty, Screen screen)
	{
		this.nifty = nifty;
		this.screen = screen;
	}

	private boolean isFullscreen = false; 

	public void fireFullscreen()
	{
		app.fireFullscreen();
		if(isFullscreen){
			setImageToIcon("FireButton","fullscreen.png");
			isFullscreen = false;
		} else {
			setImageToIcon("FireButton","notfullscreen.png");
			isFullscreen = true;
		}
	}

	public void changeShowBars()
	{
		boolean bars = (Boolean)spaceController.getProperty("showBars");
		spaceController.setProperty("showBars", !bars);
		showBars = !showBars;
		setSettingsSelected(Settings.Bars, showBars);
	}
	
	public void guiActive()
	{
		app.setGuiActive(true);
	}

	public void options()
	{
		
		// spaceController.getSpaceObjectsByGridPosition(new Vector2Int(10, 10),
		// null);
	}
	
	public void setImpMode()
	{
		setTabSelected( Tabs.CREATURE );
		this.playerState.setSelectionMode(SelectionMode.IMPMODE);
	}
	
	
	public void setBuildMode()
	{
		setTabSelected( Tabs.BUILDING);
		deselectOtherSelections("noSelection", buildingImageMapping);
		// TODO: set BuildMode, reason to do that is not clear ..., cause the buildings do that ?
	}
	
	public void selectLair()
	{
		this.playerState.setBuilding(MapType.LAIR);
		setBuildingSelected( Buildings.Lair);
	}
	
	public void selectTreasury()
	{
		this.playerState.setBuilding(MapType.TREASURY);
		setBuildingSelected(Buildings.Treasury);
	}
	
	public void selectHatchery()
	{
		this.playerState.setBuilding(MapType.HATCHERY);
		setBuildingSelected(Buildings.Hatchery);
	}
	
	public void selectTrainingroom()
	{
		this.playerState.setBuilding(MapType.TRAININGROOM);
		setBuildingSelected( Buildings.Trainingsroom);
	}
	
	public void selectLibrary()
	{
		this.playerState.setBuilding(MapType.LIBRARY);
		setBuildingSelected(Buildings.Library);
	}
	
	public void setPerform()
	{

		app.getStateManager().getState(StatsAppState.class).setDisplayStatView(toggleStats);
		app.getStateManager().getState(StatsAppState.class).setDisplayFps(toggleStats);
		toggleStats = !toggleStats;
		showStats = !showStats;
		setSettingsSelected(Settings.Stats, showStats);
	}

	public void setGrid()
	{
		if(rootNode.getChild("gridNode") != null)
		{
			rootNode.detachChild(((MonkeyApp)app).getGridNode());

		}
		else
		{
			rootNode.attachChild(((MonkeyApp)app).getGridNode());
		}
		showGrid = !showGrid;
		setSettingsSelected(Settings.Grid, showGrid);
	}


	public void quitGame()
	{
		
		app.stop(true);
		System.out.println("stop?");
//		app.getContext().destroy(true);
//		System.exit(0);
	}


	
	/**
	 * Button Event(see DungeonHud.xml), trigger print Performance Chart.
	 * 
	 */
	public void printLogAndChart(){
		PerformanceTracker.printPerformanceChart();
	}
	
	int stop = 40;

    public void update(float tpf) {
		
		if(stop==0)
		{
			stop = 40;
			updateGuiElements();
		}

		stop--;
		PerformanceTracker.logframeRate(app, playerState,creatureState);

    }
    

	private void updateGuiElements()
	{
		String manatext = ""+(int)playerState.getMana();
		String goldtext = ""+(int)playerState.getGold();
		while(manatext.length()<=7)
		{
			manatext = "0".concat(manatext);
		}
		
		while(goldtext.length()<=7)
		{
			goldtext = "0".concat(goldtext);
		}
		goldStatusRenderer.setText(goldtext);
		manaStatusRenderer.setText(manatext);
		impR.setText("" + creatureState.getCreatureCount(InitMapProcess.IMP));
		goblinR.setText("" + creatureState.getCreatureCount(InitMapProcess.GOBLIN));
		warlockR.setText("" + creatureState.getCreatureCount(InitMapProcess.WARLOCK));
		orcR.setText("" + creatureState.getCreatureCount(InitMapProcess.TROLL));
		claimedTilesRender.setText("+"+playerState.getClaimedSectors());
		this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("goldstatus");
		calculateGoldPercentageAndDraw();
		
	}


	public void onStartScreen()
	{
		
		Element goldT = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("goldstatus");
		this.goldStatusRenderer = goldT.getRenderer(TextRenderer.class);
		
		
		Element manaT = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("manastatus");
		this.manaStatusRenderer = manaT.getRenderer(TextRenderer.class);
		
		Element claimedT = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("claimedtiles");
		this.claimedTilesRender = claimedT.getRenderer(TextRenderer.class);

		Element impText = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("imp_total");
		this.impR = impText.getRenderer(TextRenderer.class);

		Element goblinT = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("goblin_total");
		this.goblinR = goblinT.getRenderer(TextRenderer.class);

		Element warlockT = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("warlock_total");
		this.warlockR = warlockT.getRenderer(TextRenderer.class);

		Element orcT = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("troll_total");
		this.orcR = orcT.getRenderer(TextRenderer.class);
		
		Element thiefT = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("thief_total");
		TextRenderer thiefR = thiefT.getRenderer(TextRenderer.class);
		thiefR.setText("" + creatureState.getCreatureCount(InitMapProcess.THIEF));
		
		// TODO: Dynamic price in hint of building
//		Element lairs = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("Build_Lair");
//		Effect effect = new Effect( nifty, false, false, false, "1", "2", "3", true, EffectEventId.onStartHover);
//        effect.init( lairs, null, null, new TimeProvider(), new LinkedList() );
//        lairs.registerEffect(EffectEventId.onStartHover, effect);
//		System.out.println( lairs.registerEffect(theId, e); );

	}

	public void onEndScreen()
	{
	}
	
	
	public void setTabSelected(String seletedSettings) {
		String settingsPng = tabImageMapping.get(seletedSettings).get(1);
		setImageToIcon(seletedSettings,settingsPng);
		deselectOtherSelections(seletedSettings, tabImageMapping);
	}
	
	public void setBuildingSelected(String seletedSettings) {
		String settingsPng = buildingImageMapping.get(seletedSettings).get(1);
		setImageToIcon(seletedSettings,settingsPng);
		deselectOtherSelections(seletedSettings, buildingImageMapping);
	}
	
	public void setSettingsSelected(String seletedSettings, boolean b) {
		
		String settingsPng = b ? settingImageMapping.get(seletedSettings).get(1) : settingImageMapping.get(seletedSettings).get(0);
		setImageToIcon(seletedSettings,settingsPng);
	}
	
	private void setImageToIcon(String elementID, String settingsPng) {
		Element settingsBtn = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName(elementID);
		// get the ImageRenderer
		ImageRenderer imageRenderer = settingsBtn.getRenderer(ImageRenderer.class);
		// change the image
		imageRenderer.setImage(nifty.getRenderEngine().createImage("/gui/images/new/"+settingsPng, false));
	}
	
	private void deselectOtherSelections(String selectedTab, Map<String, List<String>> imageMapping){
		for(String tab : imageMapping.keySet()){
			if(!selectedTab.equals(tab)) {
				String settingsPng = imageMapping.get(tab).get(0);
				
				Element settingsBtn = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName(tab);
				// get the ImageRenderer
				ImageRenderer imageRenderer = settingsBtn.getRenderer(ImageRenderer.class);
				// change the image
				imageRenderer.setImage(nifty.getRenderEngine().createImage("/gui/images/new/"+settingsPng, false));
			}
		}
	}
	
	
	public void createImp(){
		playerState.setSpell(SpellType.ImpCreation);
		setTabSelected( Tabs.CREATE_IMP );
	}
	
	/**
	 * All available menu-tabs in Gui.
	 * 
	 * @author jens.hantke
	 *
	 */
	interface Tabs {
		public final static String SETTINGS = "DefaultS";
		public final static String CREATURE = "UnitsS";
		public final static String BUILDING = "BuildingsS";
		public final static String CREATE_IMP= "CreateImp";
	}
	
	/**
	 * All available buildings in Gui.
	 * 
	 * @author jens.hantke
	 *
	 */
	interface Buildings {
		public final static String Lair= "Build_Lair";
		public final static String Hatchery= "Build_Hatchery";
		public final static String Treasury= "Build_Treasury";
		public final static String Trainingsroom= "Build_Trainingroom";
		public final static String Library= "Build_Library";
	}
	/**
	 * All available settings in Gui.
	 * 
	 * @author jens.hantke
	 *
	 */
	interface Settings {
		public final static String Fullscreen= "FireButton";
		public final static String Bars= "ShowBarsButton";
		public final static String Grid= "ShowGridButton";
		public final static String LogChart= "PrintLogAndChat";
		public final static String Stats= "ShowStatsButton";
	}
//	
//	interface Spell {
//		public final static String CreateImp= "CreateImp";
//	}


}