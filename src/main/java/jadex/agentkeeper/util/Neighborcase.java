package jadex.agentkeeper.util;

import jadex.extension.envsupport.math.Vector2Int;

import java.util.EnumSet;


public enum Neighborcase
{
	//Default
	CASE1(new Vector2Int(-1, -1), 1),
	CASE2(new Vector2Int(0, -1), 2),
	CASE3(new Vector2Int(1, -1), 4 ),
	CASE4(new Vector2Int(1, 0), 8 ),
	CASE5(new Vector2Int(1, 1), 16),
	CASE6(new Vector2Int(0, 1), 32),
	CASE7(new Vector2Int(-1, 1), 64),
	CASE8(new Vector2Int(-1, 0), 128),
	
	//Extended
	CASE9(new Vector2Int(-2, -2), -1),
	CASE10(new Vector2Int(-2, -1), -1),
	CASE11(new Vector2Int(-2, 0), -1),
	CASE12(new Vector2Int(-2, 1), -1),
	CASE13(new Vector2Int(-2, 2), -1),
	CASE14(new Vector2Int(-1, -2), -1),
	CASE15(new Vector2Int(-1, 2), -1),
	CASE16(new Vector2Int(0, -2), -1),
	CASE17(new Vector2Int(0, 2), -1),
	CASE18(new Vector2Int(1, -2), -1),
	CASE19(new Vector2Int(1, 2), -1),
	CASE20(new Vector2Int(2, -2), -1),
	CASE21(new Vector2Int(2, -1), -1),
	CASE22(new Vector2Int(2, 0), -1),
	CASE23(new Vector2Int(2, 1), -1),
	CASE24(new Vector2Int(2, 2), -1);


	
	
	private Vector2Int vector;
	private int value;
	
	private Neighborcase(Vector2Int vector, int value)
	{
		this.vector = vector;
		this.value = value;
	}
	
	 /**
	   * @return  get an enum subset
	   */
	  public static EnumSet<Neighborcase> getSimple(){
	    return EnumSet.of(CASE2, CASE4, CASE6, CASE8);
	  }
	  
	  /**
	   * @return  get an enum subset
	   */
	  public static EnumSet<Neighborcase> getDefault(){
	    return EnumSet.range(CASE1, CASE8);
	  }
	
	

	/**
	 * @return the vector
	 */
	public Vector2Int getVector()
	{
		return vector;
	}

	/**
	 * @param vector the vector to set
	 */
	public void setVector(Vector2Int vector)
	{
		this.vector = vector;
	}

	/**
	 * @return the value
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value)
	{
		this.value = value;
	}

}


