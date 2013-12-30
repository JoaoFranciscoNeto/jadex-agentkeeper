package jadex.agentkeeper.ai.creatures.imp;

import java.util.Map;

import jadex.agentkeeper.game.state.missions.Auftrag;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.game.state.missions.TaskType;
import jadex.agentkeeper.init.map.process.InitMapProcess;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.bdi.runtime.IGoal;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;

/**
 * TODO: Refractor in English and as BDIv3 Agent-Plan
 * 
 * @author Philip Willuweit p.willuweit@gmx.de
 *
 */
@Deprecated
public class WandabbauPlan extends ImpPlan
{
	public static int	ABBAUZEIT	= 45;

	public WandabbauPlan()
	{
		_verbrauchsgrad = 0;
	}

	@Override
	public void aktion()
	{
		ladAuftrag();

		SpaceObject field = InitMapProcess.getFieldTypeAtPos(_zielpos, grid);

		if(((Boolean)field.getProperty("locked")))
		{
			waitForTick();
		}

//		if(!((Boolean)field.getProperty("locked")))
//		{
			field.setProperty("locked", true);
			
			erreicheZiel(_zielpos, false);

			_avatar.setProperty("status", "Dig");

			bearbeite(_zielpos, ABBAUZEIT);
			
			
//		}


		// Click effekt entfernen
		for(Object o : (grid.getSpaceObjectsByGridPosition(_zielpos, null)))
		{
			if(o instanceof ISpaceObject)
			{
				SpaceObject currentGridTile = (SpaceObject)o;
				currentGridTile.setProperty("clicked", false);
				
				Map props = currentGridTile.getProperties();
				props.put("clicked", false);
				String type = currentGridTile.getType();
				grid.createSpaceObject(type, props, null);
				grid.destroySpaceObject(currentGridTile.getId());
				// no visual Feedback
			}
		}

		if(isCorrectField(_zielpos, MapType.GOLD))
		{
			setze(_zielpos, MapType.GOLD_DROPED, true);

			IGoal sammele = createGoal(Auftragsverwalter.GOLDSAMMELN);
			Auftrag auf = new Auftrag(Auftragsverwalter.GOLDSAMMELN, _zielpos);
			taskPoolManager.addTask(TaskType.COLLECT_GOLD, _zielpos);
			sammele.getParameter("auftrag").setValue(auf);
			dispatchSubgoalAndWait(sammele);
		}
		else
		{
			setze(_zielpos, MapType.DIRT_PATH, true);
			auftragsverwalter.neuerAuftrag(Auftragsverwalter.BESETZEN, _zielpos);
			taskPoolManager.addTask(TaskType.CLAIM_SECTOR, _zielpos);
		}
		taskPoolManager.updateReachableSelectedSectors(_zielpos);
		auftragsverwalter.updatePosition(_zielpos);
		
		_ausfuehr = false;
		
		field.setProperty("locked", false);
	}

	@Override
	public void passed()
	{
		super.passed();
		taskPoolManager.addTask(TaskType.CLAIM_SECTOR, _zielpos);
		auftragsverwalter.neuerAuftrag(Auftragsverwalter.BESETZEN, _zielpos);
	}

}
