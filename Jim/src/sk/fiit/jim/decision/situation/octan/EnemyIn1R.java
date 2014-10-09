package sk.fiit.jim.decision.situation.octan;

import java.util.List;

import sk.fiit.jim.agent.models.Player;
import sk.fiit.jim.agent.models.WorldModel;
import sk.fiit.jim.decision.situation.Situation;


/**
 * @author Samuel Benkovic
 * Situation is true if any enemy stand in this octan.
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
 * @return boolean - Returns true only if enemy is in octan R1
 */

public class EnemyIn1R extends Situation {
	
	public static final double POSITION_MAX_X = -8;
	public static final double POSITION_MAX_Y = 0;

	private List<Player> opponents = WorldModel.getInstance()
			.getOpponentPlayers();

	public boolean checkSituation() {
		for (Player p : opponents) {
			if (p.getPosition().getX() < EnemyIn1R.POSITION_MAX_X
					&& p.getPosition().getY() < EnemyIn1R.POSITION_MAX_Y) {
				return true;
			}
		}
		return false;
	}
}
