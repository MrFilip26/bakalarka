package sk.fiit.jim.decision.situation.octan;

import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.decision.situation.Situation;

/**
 * @author Samuel Benkovic
 * Situation is true if Ball is in octan
 *		  
 *		  Enemy
 * ========___========
 * -------------------
 * 		4L	|	4R
 * -------------------
 * 		3L	|	3R
 * -------------------
 * 		2L	|	2R
 * -------------------
 * 		1L	|	1R
 * ========___========
 *         Our
 *         
 * @return boolean - Returns true only if ball is in octan L1
 */

public class BallIn4L extends Situation {
	
	public static final double POSITION_MIN_X = 8;
	public static final double POSITION_MIN_Y = 0;
	
	
	public boolean checkSituation() {
		if ((WorldModel.getInstance().getBall().getPosition().getX() > BallIn4L.POSITION_MIN_X)
				&& (WorldModel.getInstance().getBall().getPosition().getY() >= BallIn4L.POSITION_MIN_Y)) {
			return true;
		}
		return false;
	}
}
