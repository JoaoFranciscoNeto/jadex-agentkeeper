package jadex.agentkeeper.game.userinput.magicSpells;

import jadex.agentkeeper.ai.UpdateStatusTask;
import jadex.agentkeeper.game.state.creatures.SimpleCreatureState;
import jadex.agentkeeper.init.map.process.IMap;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.extension.envsupport.environment.IObjectTask;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.HashMap;

public class ImpCreationSpell {

	private Grid2D spaceObjects;

	public ImpCreationSpell(Grid2D spaceObjects) {
		this.spaceObjects = spaceObjects;
	}

	public void zauberImp(IVector2 zielpos, int kosten) {
		if (begehbar((Vector2Int) zielpos)) {
			String type = "imp";
			
			HashMap<String, Object> props = new HashMap<String, Object>();
			//props.put("type", "imp");
			props.put("spieler", "1");
			props.put(jadex.agentkeeper.util.ISpaceObject.Properties.AWAKE, 100.0);
			props.put(jadex.agentkeeper.util.ISpaceObject.Properties.FED, 100.0);
			props.put(jadex.agentkeeper.util.ISpaceObject.Properties.HAPPINESS, 100.0);
			props.put(ISObjStrings.PROPERTY_OWNER, "1");
			props.put(Space2D.PROPERTY_POSITION, zielpos);
			props.put(Space2D.PROPERTY_POSITION, new Vector2Double(zielpos.getXAsInteger(), zielpos.getYAsInteger()));
			props.put(ISObjStrings.PROPERTY_INTPOSITION, zielpos);
			props.put(ISObjStrings.PROPERTY_LEVEL, "L1");
			
			SimpleCreatureState	creatureState =  (SimpleCreatureState) spaceObjects.getProperty(ISpaceStrings.CREATURE_STATE);
			creatureState.addCreature(IMap.IMP);
			
			ArrayList<IObjectTask> list = new ArrayList<IObjectTask>();
			list.add(new UpdateStatusTask());

			// todo: level, owner
			spaceObjects.createSpaceObject(type, props, list);
			for(Object obj : spaceObjects.getSpaceObjects()){
				if (obj instanceof ISpaceObject) {
					ISpaceObject currentField = (ISpaceObject) obj;
					if(currentField.getType().equals("imp")){
						System.out.println(obj);
					}
				}
			}
			System.out.println("");
			spaceObjects.removeSpaceProcess(spaceObjects.getProperty(ISpaceProcess.ID));
//			int altmana = (Integer) spaceObjects.getProperty("mana");
//			int neumana = altmana - kosten;
//			
//			// GesamtGold anpassen
//			spaceObjects.setProperty("mana", neumana);
			// GUIInformierer.aktuallisierung();
		}
	}

	private boolean begehbar(Vector2Int punkt) {
		for (Object o : spaceObjects.getSpaceObjectsByGridPosition(punkt, null)) {
			if (o instanceof ISpaceObject) {
				ISpaceObject currentField = (ISpaceObject) o;
				if (currentField.getType().equals(MapType.CLAIMED_PATH.name())) {
					return true;
				}
			}
		}

		return false;
	}
}
