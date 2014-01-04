package jadex.agentkeeper.ai.base.collectGold;

import jadex.agentkeeper.ai.AbstractBeingBDI;
import jadex.agentkeeper.ai.AbstractBeingBDI.AchieveMoveToSector;
import jadex.agentkeeper.ai.creatures.AbstractCreatureBDI;
import jadex.agentkeeper.ai.imp.ImpBDI;
import jadex.agentkeeper.ai.imp.ImpBDI.AchieveFillTreasury;
import jadex.agentkeeper.ai.pathfinding.AStarSearch;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.game.state.map.TileChanger;
import jadex.agentkeeper.game.state.missions.Task;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.util.ISO;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.TileInfo;
import jadex.agentkeeper.worldmodel.structure.building.TreasuryInfo;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;
import jadex.bdiv3.runtime.IPlan;
import jadex.commons.IResultCommand;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.extension.envsupport.environment.ISpaceObject;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Plan
public class FillTreasuryPlan {

	private static final String IMP_LOCAL_TASK = "ImpLocalTask";

	@PlanCapability
	protected AbstractBeingBDI capa;

	@PlanCapability
	protected ImpBDI impBdi;

	@PlanAPI
	protected IPlan iplan;

	@PlanAPI
	protected IPlan rplan;

	private Object digtaskid;

	private Grid2D environment;

	private SpaceObject currentTaskSpaceObject;

	private SimplePlayerState playerState;

	private SimpleMapState mapState;
	
	private final int GOLD_AMOUNT_PER_GOLD_VEIN = 1000;

	@PlanReason
	protected AchieveFillTreasury goal;

	@PlanBody
	public IFuture<Void> body() {

		final Future<Void> retb = new Future<Void>();

		Task newImpTask = goal.getTarget();
		if (newImpTask != null) {
			System.out.println(newImpTask.getTaskType());
			environment = capa.getEnvironment();
			playerState = (SimplePlayerState) environment.getProperty(ISO.Objects.PLAYER_STATE);
			mapState = (SimpleMapState) environment.getProperty(ISO.Objects.BUILDING_STATE);
			capa.getMySpaceObject().setProperty(IMP_LOCAL_TASK, newImpTask);

			reachTargetDestination(newImpTask).addResultListener(new DelegationResultListener<Void>(retb));
		} else {
			System.out.println("The Task was null, this should not happen. But we don't break up.");
			retb.setResult(null);
		}
		return retb;
	}

	private IFuture<Void> reachTargetDestination(final Task currentImpTask) {
		final Future<Void> ret = new Future<Void>();

		final Vector2Int currentImpTaskPosition = currentImpTask.getTargetPosition();

		Collection<ISpaceObject> spaceObjectsByGridPosition = environment.getSpaceObjectsByGridPosition(currentImpTask.getTargetPosition(), null);
		for (ISpaceObject spaceObject : spaceObjectsByGridPosition) {
			for (MapType mapType : MapType.getOnlySolids()) {
				if (mapType.toString().equals(spaceObject.getType())) {
					currentTaskSpaceObject = (SpaceObject) spaceObject;
				}
			}
		}

		if (currentImpTaskPosition != null && currentTaskSpaceObject != null) {
			
			final Vector2Int nextTreasureChest = findNextTreasureChest(currentImpTaskPosition, GOLD_AMOUNT_PER_GOLD_VEIN);
			final TreasuryInfo hinfo = (TreasuryInfo)mapState.getTileAtPos((Vector2Int)nextTreasureChest);
			hinfo.setLocked(true);
			// went to the position where the imp can dig from
			if (nextTreasureChest != null) {
				IFuture<AchieveMoveToSector> reachSectorToDigFrom = rplan.dispatchSubgoal(capa.new AchieveMoveToSector(nextTreasureChest));

				reachSectorToDigFrom.addResultListener(new ExceptionDelegationResultListener<AbstractCreatureBDI.AchieveMoveToSector, Void>(ret) {
					public void customResultAvailable(AbstractCreatureBDI.AchieveMoveToSector amt) {
						claimWall(currentImpTask).addResultListener(new DelegationResultListener<Void>(ret) {
							public void customResultAvailable(Void result) {
								// add new Tile and remove the old, claim the
								// sector ground
								
								
								
								TileChanger tilechanger = new TileChanger(environment);
								String neighborhood = (String) currentTaskSpaceObject.getProperty(ISO.Properties.NEIGHBORHOOD);
								tilechanger.addParameter("bearbeitung", new Integer(0)).addParameter(ISO.Properties.STATUS, "byImpCreated").addParameter(ISO.Properties.CLICKED, false)
										.addParameter(ISO.Properties.LOCKED, false).addParameter(ISO.Properties.NEIGHBORHOOD, neighborhood)
										.addParameter(ISO.Properties.INTPOSITION, currentImpTaskPosition)
										.addParameter(ISO.Properties.DOUBLE_POSITION, new Vector2Double(currentImpTaskPosition.getXAsDouble(), currentImpTaskPosition.getYAsDouble()))
										.changeTile(currentImpTask.getTargetPosition(), MapType.DIRT_PATH, new ArrayList<MapType>(Arrays.asList(MapType.GOLD_DROPED)));

								// imp stop claiming the sector ground
								capa.getMySpaceObject().setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");

								playerState.addGold(GOLD_AMOUNT_PER_GOLD_VEIN);
								environment.getSpaceObject(hinfo.getSpaceObjectId()).setProperty(ISObjStrings.PROPERTY_STATUS, "hasGold");
								hinfo.addAmount(GOLD_AMOUNT_PER_GOLD_VEIN);
								
								hinfo.setLocked(false);
								ret.setResult(null);
							}
						});
					}
				});
			} else {
				// TODO: fail!! sector from task should be reachable for destroy
				// => catch
				System.out.println("fail!! sector from task should be reachable for clame wall ");
				capa.getMySpaceObject().setProperty(ISObjStrings.PROPERTY_STATUS, "Idle");
				rplan.abort();
			}
			
		} else {
			System.out.println("FillTreasury: Task has no Int Position");
			rplan.abort();
		}
		return ret;
	}

	private IFuture<Void> claimWall(final Task currentImpTask) {
		final Future<Void> ret = new Future<Void>();

		Map<String, Object> props = new HashMap<String, Object>();
		props.put(CollectGoldTask.PROPERTY_DESTINATION, currentImpTask.getTargetPosition());
		props.put(CollectGoldTask.PROPERTY_DIG_SPEED, impBdi.getMyWorkingSpeed());

		digtaskid = capa.getEnvironment().createObjectTask(CollectGoldTask.PROPERTY_TYPENAME, props, capa.getMySpaceObject().getId());
		iplan.invokeInterruptable(new IResultCommand<IFuture<Void>, Void>() {
			public IFuture<Void> execute(Void args) {
				return capa.getEnvironment().waitForTask(digtaskid, capa.getMySpaceObject().getId());
			}
		}).addResultListener(new DelegationResultListener<Void>(ret));

		return ret;
	}

	/**
	 * @param mypos
	 * @param amount
	 * @return
	 */
	private synchronized  Vector2Int findNextTreasureChest(IVector2 mypos, int amount) {
		HashMap<Vector2Int, TileInfo> treasuries = mapState.getTypes(MapType.TREASURY);

		ArrayList<Vector2Int> chests = new ArrayList<Vector2Int>(treasuries.keySet());

		AStarSearch aStarSearch;
		int minTotalCosts = Integer.MAX_VALUE;
		int totalCosts;
		Vector2Int tmp, minCostsChestPosition = null;
		for (Vector2Int vec : chests) {
			TreasuryInfo hinfo = (TreasuryInfo) mapState.getTileAtPos((Vector2Int) vec);

			if (hinfo.getAmount() + amount <= TreasuryInfo.MAX_AMOUNT && !hinfo.isLocked()) {
				aStarSearch = new AStarSearch(mypos, vec, environment, true);
				totalCosts = aStarSearch.gibPfadKosten();
				tmp = vec;
				if (totalCosts < minTotalCosts) {
					minTotalCosts = totalCosts;
					minCostsChestPosition = tmp;
				}
			}
		}
		return minCostsChestPosition;
	}

}
