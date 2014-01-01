package jadex.agentkeeper.ai.base.collectGold;

import jadex.agentkeeper.ai.enums.PlanType;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;

public class CollectGoldTask extends AbstractTask {

	private static final int DELAY_RESET_COUNT = 100;

	/** The destination property. */
	public static final String PROPERTY_TYPENAME = "collectGoldTask";

	/** The destination property. */
	public static final String PROPERTY_DESTINATION = "destination";

	/** The speed property of the moving object (units per second). */
	public static final String PROPERTY_DIG_SPEED = "digSpeed";
	
//	/** Current time stamp */
//	private long timestamp;
//
//	/** The time that has passed according to the environment executor. */
//	private long progress = 0;
//
//	private double onePerSecondDelta;

	/** The Delta **/
	private double delta = 0;
	
	double digSpeed;
	
	@Override
	public void start(ISpaceObject obj) {
		digSpeed = ((Number)getProperty(PROPERTY_DIG_SPEED)).doubleValue();
		super.start(obj);
	}
	
	// TODO: NO Clock in start of AbstractTask
//	public void start(IClockService clock, IEnvironmentSpace space) {
//		this.timestamp = clock.getTime();
//		this.delta = 0;
//	}

	@Override
	public void execute(IEnvironmentSpace space, ISpaceObject obj, long progress, IClockService clock) {
		obj.setProperty(ISObjStrings.PROPERTY_STATUS, PlanType.Types.DANCE );
		if(delta > DELAY_RESET_COUNT){
			delta = 0;
			this.setFinished(space, obj, true);
		}
		delta += digSpeed;
	}
	
//	private void updateProgress(IClockService clock) {
//		long currenttime = clock.getTime();
//		this.progress = currenttime - timestamp;
//		this.timestamp = currenttime;
//		onePerSecondDelta = progress * 0.001;
//		delta = delta + onePerSecondDelta;
//	}

}
