package jadex.agentkeeper.view;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;

public class CostUpdater {
	
	private static Element goldCosts;
	private static Element manaCosts;
	
	private static TextRenderer currentGoldCosts;
	
	private static TextRenderer currentManaCosts;
	
	public static void initStatitCostElements(Screen screen) {
		
		goldCosts = screen.findElementByName("goldcosts");
		currentGoldCosts = goldCosts.getRenderer(TextRenderer.class);
		
		manaCosts = screen.findElementByName("manacosts");
		currentManaCosts = manaCosts.getRenderer(TextRenderer.class);
	}
	
    public static void updateGoldCosts(int newGoldCosts){
    	currentGoldCosts.setText("-".concat(newGoldCosts+""));
    }
    
    public static void setGoldCostsVisible(){
    	goldCosts.setVisible(true);
    }
    
    public static void setGoldCostsInVisible(){
    	goldCosts.setVisible(false);
    }
    
    public static void updateManaCosts(int newGoldCosts){
    	currentManaCosts.setText("-".concat(newGoldCosts+""));
    }
    
    
    public static void setManaCostsVisible(){
    	manaCosts.setVisible(true);
    }
    
    public static void setManaCostsInVisible(){
    	manaCosts.setVisible(false);
    }
    
}