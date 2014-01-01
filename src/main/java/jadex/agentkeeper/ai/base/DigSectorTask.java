package jadex.agentkeeper.ai.base;

import jadex.agentkeeper.util.ISObjStrings;
import jadex.bridge.service.types.clock.IClockService;
import jadex.extension.envsupport.environment.AbstractTask;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceObject;

public class DigSectorTask extends AbstractTask {

	/** The destination property. */
	public static final String PROPERTY_TYPENAME = "digSectorTask";

	/** The destination property. */
	public static final String PROPERTY_DESTINATION = "destination";

	/** The speed property of the moving object (units per second). */
	public static final String PROPERTY_SPEED = "speed";
	
	private int digTimer = 0;

	@Override
	public void execute(IEnvironmentSpace space, ISpaceObject obj, long progress, IClockService clock) {
		
		
		obj.setProperty(ISObjStrings.PROPERTY_STATUS, "Dig");
		digTimer++;
		if(digTimer > 50){
			this.setFinished(space, obj, true);
		}
		
	}

}
