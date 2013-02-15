package jadex.agentkeeper.view;

import jadex.agentkeeper.game.userinput.UserEingabenManager;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.view.selection.SelectionArea;
import jadex.agentkeeper.view.selection.SelectionBox;
import jadex.agentkeeper.view.selection.SelectionHandler;
import jadex.agentkeeper.view.selection.SelectionHandlingKeyListener;
import jadex.extension.envsupport.environment.ISpaceController;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.math.IVector3;
import jadex.extension.envsupport.observer.graphics.jmonkey.MonkeyApp;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.control.UpdateControl;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Line;
import com.jme3.water.WaterFilter;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 * The customized-szene Setup spezific for AgentKeeper, using the JMonkey AppState-Concept
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 *
 */
public class GeneralAppState extends AbstractAppState
{

	private MonkeyApp			app;

	private MonkeyApp			monkeyapp;

	private Node				rootNode;

	private AssetManager		assetManager;

	private AppStateManager		stateManager;

	private InputManager		inputManager;

	private ViewPort			viewPort;

	private DirectionalLight	dl;

	private ISpaceController	spaceController;
	
	private SelectionHandler handler;
	
	private UserEingabenManager	usermanager;

	



	public void initialize(AppStateManager stateManager, Application app)
	{
		super.initialize(stateManager, app);
		this.app = (MonkeyApp)app;
		this.rootNode = this.app.getRootNode();
		this.assetManager = this.app.getAssetManager();
		this.stateManager = this.app.getStateManager();
		this.inputManager = this.app.getInputManager();
		this.viewPort = this.app.getViewPort();
		this.monkeyapp = (MonkeyApp)app;
		this.spaceController = monkeyapp.getSpaceController();
		this.usermanager = (UserEingabenManager) spaceController.getProperty("uem");
		this.handler = new SelectionHandler((MonkeyApp)app, this);

		setup();

	}
	
	public void update(float tpf)
	{
		handler.updateHandler();
		dl.setDirection(monkeyapp.getCamera().getDirection().setY(-0.0f));
		

		
		
	}

	public void updateInfoText(String selectedObject)
	{
		Element infotext = this.app.getNiftyDisplay().getNifty().getCurrentScreen().findElementByName("infotext");
		TextRenderer textRender = infotext.getRenderer(TextRenderer.class);
		textRender.setText(selectedObject);
		
	}

	public void setup()
	{
		dl = new DirectionalLight();
		dl.setName("sun");
//		dl.setDirection(new Vector3f(-0.51f, -1.0f, -0.51f).normalizeLocal());
		
//		dl.setDirection(new Vector3f(-0.07735026f, -0.97735026f, -0.07735026f));
		dl.setDirection(new Vector3f(-0.57f, -0.01835026f, -0.57f).normalizeLocal());
//		dl.setColor(new ColorRGBA(0.5f, 0.4f, 0.4f, 1f).multLocal(0.5f));
		dl.setColor(ColorRGBA.White.multLocal(0.9f));
		this.app.getRootNode().addLight(dl);
		
		AmbientLight al = new AmbientLight();
		// al.setColor(new ColorRGBA(1.7f,2.2f,3.2f,1f));
		al.setColor(ColorRGBA.White.multLocal(0.4f));
		this.app.getRootNode().addLight(al);
		
		
		//Water Filter:
		Vector3f lightDir = new Vector3f(1f, -2f, 1f);
		WaterFilter water = new WaterFilter(rootNode, lightDir);
		water.setWaterHeight(-2.5f);
		water.setUseFoam(false);
		water.setUseRipples(true);
		water.setDeepWaterColor(ColorRGBA.Black.mult(0.1f));
		water.setWaterColor(ColorRGBA.Black.mult(0.15f));
		water.setWaterTransparency(0.001f);
		water.setMaxAmplitude(0.3f);
		water.setReflectionDisplace(0.2f);
		water.setWaveScale(0.008f);
		water.setSpeed(0.1f);
		water.setShoreHardness(1.0f);
		water.setRefractionConstant(0.2f);

		water.setShininess(0.3f);
		water.setSunScale(1.1f);

		water.setLightColor(ColorRGBA.Red.mult(0.1f).set(ColorRGBA.Orange.mult(0.1f).r, ColorRGBA.Orange.mult(0.1f).g, ColorRGBA.Orange.mult(0.1f).b, 0.01f));
		water.setColorExtinction(new Vector3f(10.0f, 20.0f, 30.0f));
		this.app.getFpp().addFilter(water);
	}


	/**
	 * @return the spaceController
	 */
	public ISpaceController getSpaceController()
	{
		return spaceController;
	}


	/**
	 * @param spaceController the spaceController to set
	 */
	public void setSpaceController(ISpaceController spaceController)
	{
		this.spaceController = spaceController;
	}

	public SpaceObject getSpaceObjectById(long idlong)
	{
		return (SpaceObject)spaceController.getSpaceObject(idlong);
	}

	public void userSubmit(SelectionArea selectionArea)
	{
		usermanager.destoryWalls(selectionArea);
		
	}


}