package jadex.agentkeeper.ai.creatures;

import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.AbstractBeingBDI.PerformIdle;
import jadex.agentkeeper.ai.AbstractBeingBDI.PerformPatrol;
import jadex.agentkeeper.ai.base.PatrolPlan;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.structure.building.TrainingRoomInfo;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanAborted;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanFailed;
import jadex.bdiv3.annotation.PlanReason;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;


@Plan
public class TrainingPlan
{


	@PlanCapability
	protected AbstractCreatureBDI	capa;

	@PlanAPI
	protected IPlan					rplan;

	@PlanReason
	protected PerformIdle			goal;

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
		System.out.println("training plan ! !");
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

		System.out.println("reachTrainingsRoom");
		if(targetTrainingsRoom != null)
		{
			final TrainingRoomInfo info = (TrainingRoomInfo)buildingState.getTileAtPos(targetTrainingsRoom);

			// spaceObject.setProperty(ISObjStrings.PROPERTY_GOAL,
			// PlanType.EAT);
			IFuture<AchieveMoveToSector> fut = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(targetTrainingsRoom, new Vector2Double(0.00, -0.45)));

			fut.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret)
			{
				public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt)
				{
					spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Attack");
					// System.out.println("at pos");
					rplan.waitFor(1000).addResultListener(new DelegationResultListener<Void>(ret)
					{
						public void customResultAvailable(Void result)
						{
							spaceObject.setProperty(ISObjStrings.PROPERTY_STATUS, "Attack");
							Double experience = (Double)spaceObject.getProperty(ISObjStrings.PROPERTY_EXPERIENCE);
							experience += 0.1;
							spaceObject.setProperty(ISObjStrings.PROPERTY_EXPERIENCE, experience);

							IFuture<PerformPatrol> fut2 = rplan.dispatchSubgoal(capa.new PerformPatrol());

							fut2.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.PerformPatrol, Void>(ret)
							{
								@Override
								public void customResultAvailable(PerformPatrol result)
								{
									System.out.println("now we make the patrol plan");
									ret.setResult(null);

								}

							});


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
