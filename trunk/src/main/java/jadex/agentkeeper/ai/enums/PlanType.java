package jadex.agentkeeper.ai.enums;

public enum PlanType
{
	SLEEP, GETLAIR, WALK, IDLE, EAT, PATROL;
	
	public interface Types {
		public final static String DIG ="Dig";
		public final static String DANCE ="Dance";
	}
}
