package sk.fiit.jim.decision.tactic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.fiit.jim.decision.situation.BallOnGoal;
import sk.fiit.jim.decision.situation.octan.MeIn1L;
import sk.fiit.jim.decision.situation.octan.MeIn1R;


public class Goalie extends Tactic {

	private ArrayList<String> prescribedSituations = new ArrayList<String>(
			Arrays.asList(MeIn1R.class.getName(), MeIn1L.class.getName(), BallOnGoal.class.getName()));
	private ArrayList<String> initSituations = new ArrayList<String>(
			Arrays.asList(MeIn1L.class.getName(), MeIn1R.class.getName()));
	private ArrayList<String> progressSituations = new ArrayList<String>(
			Arrays.asList(MeIn1L.class.getName(), MeIn1R.class.getName()));
	
	private static Goalie instance = null;
	
	public static Goalie getInstance() {
		if (instance == null) {
			instance = new Goalie();
		}
		return instance;
	}
	
	/*
	 * Condition which must be true when you start tactic
	 */
	public boolean getInitCondition(List<String> currentSituations) {
		boolean isStart = false;

		for (String a : currentSituations) {
			for (String b : this.initSituations) {
				if (a.equals(b)) {
					isStart = true;
					break;
				}
			}
		}
		return isStart;
	}

	/*
	 * Condition must be true if the given tactic is currently being executed
	 */
	public boolean getProgressCondition(List<String> currentSituations) {
		
		for (String a : currentSituations) {
                    if (this.progressSituations.contains(a))
                        return true;
          
		}
		return false;
	}

	/*
	 * Implementation of actual tactic
	 */
	//Todo #Task() #Solver() #Priority() | xmarkech 2013-12-10T20:38:13.4560000Z
	public void run() {            
//		Vector3D position_to_go = Vector3D.cartesian(10, 1, 0.4);
//		Planner.getPlan().Highskill_Queue.add(new GoToPosition(position_to_go,
//				new LambdaCallable() {
//			public boolean call() {
//				return EnvironmentModel.beamablePlayMode();
//			}
//		}));
	}

	/*
	 * Method is needed if more tactics initcondition evaluates to true
	 * Calculated on the basis HighSkill and current situation.
	 */
	public float getSuitability(List<String> currentSituations) {
		int numberOfMatch = 0;
		/*
		 * TODO - better check two lists ? Maybe HASH ? NumberOfMatch - one
		 * class for every strategy, not in all strategies
		 */

		for (String a : currentSituations) {
                    if (this.prescribedSituations.contains(a))
                        ++numberOfMatch;
		}

		/*
		 * ReturnFit returns number (NumberOfMatch) - 1,2,5 etc. NumberOfMatch
		 * in currentsituations and prescribedsituations
		 */
		return numberOfMatch/currentSituations.size();
	}

	@Override
	public List<String> getPrescribedSituations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int checkState(List<String> currentSituations) {
		// TODO Auto-generated method stub
		return 0;
	}

}
