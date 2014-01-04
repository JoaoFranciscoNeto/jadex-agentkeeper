package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.base.MoveTask;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI.MaintainCreatureFed;
import jadex.agentkeeper.ai.enums.PlanType;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.structure.building.HatcheryInfo;
import jadex.agentkeeper.worldmodel.structure.building.TrainingRoomInfo;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanAborted;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanFailed;
import jadex.bdiv3.annotation.PlanReason;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.IResultCommand;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.environment.space2d.Space2D;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.HashMap;
import java.util.Map;

@Plan
public class TrainingPlan {

	

	@PlanCapability
	protected AbstractCreatureBDI	capa;

	@PlanAPI
	protected IPlan					rplan;

	@PlanReason
	protected MaintainCreatureFed	goal;

	protected SpaceObject			spaceObject;

	protected SimpleMapState		buildingState;

	protected Grid2D				environment;

	private Object					mtaskid;

	@PlanAPI
	protected IPlan					iplan;

	/**
	 * The plan body.
	 */
	@PlanBody
	public IFuture<Void> body()
	{
		final Future<Void> ret = new Future<Void>();

		spaceObject = (SpaceObject)capa.getMySpaceObject();

		environment = capa.getEnvironment();

		buildingState = (SimpleMapState)environment.getProperty(ISpaceStrings.BUILDING_STATE);

		reachTrainingsRoom(buildingState).addResultListener(new DelegationResultListener<Void>(ret));

		return ret;

	}

	private IFuture<Void> reachTrainingsRoom(final SimpleMapState buldingState)
	{
		final Future<Void> ret = new Future<Void>();
		final Future<SpaceObject> myChicken = new Future<SpaceObject>();

		// TODO: Only get closest
		final Vector2Int targetTrainingsRoom = buildingState.getClosestTrainigsRoomWithTrainingObject(capa.getMyPosition(), environment);


		if(targetTrainingsRoom != null)
		{
			final TrainingRoomInfo info = (TrainingRoomInfo)buildingState.getTileAtPos(targetTrainingsRoom);

			spaceObject.setProperty(ISObjStrings.PROPERTY_GOAL, PlanType.EAT);
			IFuture<AchieveMoveToSector> fut = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(targetTrainingsRoom));
			// System.out.println("- - - - - start walking to bed - - - - - ");
			fut.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret)
			{
				public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt)
				{
					spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");
					// System.out.println("at pos");
					rplan.waitFor(100).addResultListener(new DelegationResultListener<Void>(ret)
					{
						
						public void customResultAvailable(Void result)
						{
							spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Attack");
							DelegationResultListener<SpaceObject> eatlistener = new DelegationResultListener<SpaceObject>(myChicken)
							{
								public void customResultAvailable(final SpaceObject chickenresult) {
									
									spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Attack");
									ret.setResult(null);
								}

							};

//							Map<String,Object> params = new HashMap<String,Object>();
//							params.put("Monster", spaceObject);
//							params.put("Target", info);
//
//							environment.performSpaceAction("eat", params, eatlistener);


						}

					});
				}
			});
		}
		else
		{
			spaceObject.setProperty(ISObjStrings.PROPERTY_MORAL, 0);
			rplan.abort();
		}


		return ret;
	}

	@PlanFailed
	@PlanAborted
	public void cleanupMoveToChickenTask()
	{
		if(mtaskid != null)
		{
			System.out.println("cleanup task aborted:" + iplan.getId() + " " + mtaskid);
			capa.getEnvironment().removeObjectTask(mtaskid, spaceObject.getId());
		}
	}
	
}
