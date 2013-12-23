package jadex.agentkeeper.view;

import jadex.agentkeeper.game.state.creatures.SimpleCreatureState;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.view.selection.SelectionMode;
import jadex.agentkeeper.log.PerformanceTracker;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.extension.envsupport.environment.ISpaceController;
import jadex.extension.envsupport.observer.graphics.jmonkey.MonkeyApp;
import jadex.extension.envsupport.observer.graphics.jmonkey.appstate.gui.DefaultGuiController;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.scene.Node;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
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

	public KeeperGuiController(SimpleApplication app, ISpaceController spacecontroller)
	{
		this.app = (MonkeyApp)app;
		rootNode = this.app.getRootNode();

		this.spaceController = spacecontroller;
		this.creatureState = (SimpleCreatureState)spaceController.getProperty(ISpaceStrings.CREATURE_STATE);
		this.playerState = (SimplePlayerState)spaceController.getProperty(ISpaceStrings.PLAYER_STATE);


	}


	/** Nifty GUI ScreenControl methods */
	public void bind(Nifty nifty, Screen screen)
	{
		this.nifty = nifty;
		this.screen = screen;
	}


	public void fireFullscreen()
	{
		app.fireFullscreen();
	}

	public void changeShowBars()
	{
		boolean bars = (Boolean)spaceController.getProperty("showBars");
		spaceController.setProperty("showBars", !bars);
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
		this.playerState.setSelectionMode(SelectionMode.IMPMODE);
	}
	
	public void selectLair()
	{
		this.playerState.setBuilding(MapType.LAIR);
	}
	
	public void selectTreasury()
	{
		this.playerState.setBuilding(MapType.TREASURY);
	}
	
	public void selectHatchery()
	{
		this.playerState.setBuilding(MapType.HATCHERY);
	}
	
	public void selectTrainingroom()
	{
		this.playerState.setBuilding(MapType.TRAININGROOM);
	}
	
	public void selectLibrary()
	{
		this.playerState.setBuilding(MapType.LIBRARY);
	}

	public void setPerform()
	{

		app.getStateManager().getState(StatsAppState.class).setDisplayStatView(toggleStats);
		app.getStateManager().getState(StatsAppState.class).setDisplayFps(toggleStats);
		toggleStats = !toggleStats;

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
	}


	public void quitGame()
	{
		app.stop();
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

	}

	public void onEndScreen()
	{
	}


}