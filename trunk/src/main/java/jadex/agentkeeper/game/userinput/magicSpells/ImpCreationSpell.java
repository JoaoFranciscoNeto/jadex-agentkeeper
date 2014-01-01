package jadex.agentkeeper.game.userinput.magicSpells;

import jadex.agentkeeper.ai.UpdateStatusTask;
import jadex.agentkeeper.game.state.creatures.SimpleCreatureState;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.init.map.process.IMap;
import jadex.agentkeeper.util.ISO;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.util.Neighborhood;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.extension.envsupport.environment.IObjectTask;
import jadex.extension.envsupport.environment.ISpaceObject;
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

	public void createImp(IVector2 zielpos, int costs) {
		if (isWalkableSector((Vector2Int) zielpos)) {
			SimplePlayerState playerState = (SimplePlayerState) spaceObjects.getProperty(ISO.Objects.PLAYER_STATE);
			int new_mana = (int) (playerState.getMana() - costs);
			if (new_mana > 0) {
				// change Total Mana Amount
				playerState.setMana(new_mana);

				String type = "imp";

				HashMap<String, Object> props = new HashMap<String, Object>();
				// props.put("type", "imp");
				props.put("spieler", "1");
				props.put(ISO.Properties.AWAKE, 100.0);
				props.put(ISO.Properties.FED, 100.0);
				props.put(ISO.Properties.HAPPINESS, 100.0);
				props.put(ISObjStrings.PROPERTY_OWNER, "1");
				props.put(Space2D.PROPERTY_POSITION, zielpos);
				props.put(Space2D.PROPERTY_POSITION, new Vector2Double(zielpos.getXAsInteger(), zielpos.getYAsInteger()));
				props.put(ISObjStrings.PROPERTY_INTPOSITION, zielpos);
				props.put(ISObjStrings.PROPERTY_LEVEL, "L1");

				SimpleCreatureState creatureState = (SimpleCreatureState) spaceObjects.getProperty(ISpaceStrings.CREATURE_STATE);
				creatureState.addCreature(IMap.IMP);

				ArrayList<IObjectTask> list = new ArrayList<IObjectTask>();
				list.add(new UpdateStatusTask());

				// todo: level, owner
				spaceObjects.createSpaceObject(type, props, list);
			}
		}
	}

	private boolean isWalkableSector(Vector2Int punkt) {
		for (Object o : spaceObjects.getSpaceObjectsByGridPosition(punkt, null)) {
			if (o instanceof ISpaceObject) {
				ISpaceObject currentField = (ISpaceObject) o;
				if (Neighborhood.isWalkableForDigging(currentField)) {
					return true;
				}
			}
		}

		return false;
	}
}
