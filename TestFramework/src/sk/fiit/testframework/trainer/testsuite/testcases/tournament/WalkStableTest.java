/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.fiit.testframework.trainer.testsuite.testcases.tournament;

import java.util.logging.Level;
import java.util.logging.Logger;

import sk.fiit.robocup.library.geometry.Vector3;
import sk.fiit.testframework.communication.agent.AgentData;
import sk.fiit.testframework.communication.agent.AgentJim;
import sk.fiit.testframework.communication.agent.AgentJim.TeamSide;
import sk.fiit.testframework.communication.agent.AgentManager;
import sk.fiit.testframework.monitor.AgentMonitor;
import sk.fiit.testframework.monitor.AgentMonitorMessage;
import sk.fiit.testframework.monitor.AgentMonitorMessage.HighSkill;
import sk.fiit.testframework.monitor.IAgentMonitorListener;
import sk.fiit.testframework.parsing.models.PlayMode;
import sk.fiit.testframework.trainer.testsuite.TestCase;
import sk.fiit.testframework.trainer.testsuite.TestCaseResult;
import sk.fiit.testframework.worldrepresentation.models.Player;
import sk.fiit.testframework.worldrepresentation.models.SimulationState;

public class WalkStableTest extends TestCase implements IAgentMonitorListener {

	private static Logger logger = Logger.getLogger(WalkStableTest.class.getName());

	private AgentJim agent;
	private AgentData agentData;
	private Vector3 initPos;
	private double startTime;
	private Vector3 lastLocation;

	private int trestneSekundy;
	private double lastGroundTouchTime;
	
	private boolean started;
	private double fieldLength;
	private double fieldWidth;
	
	private double playerLocationXBeforePutBehindBorder = -100;

	public WalkStableTest()  {
		super();
		AgentMonitor.setMessageListener(1, "ANDROIDS", this, AgentMonitorMessage.TYPE_HIGHSKILL);
	}

	@Override
	public boolean init() {
		super.init();
		try {
			fieldLength = monitor.getSimulationState().getEnvironmentInfo().getFieldLength();
			fieldWidth = monitor.getSimulationState().getEnvironmentInfo().getFieldWidth();
//			initPos = new Vector3(fieldLength/4 * -2 + 5, 1, 0.4);	//-5.5, 1, 0.4
			initPos = new Vector3(-5, 1, 0.4);

			agent = AgentManager.getManager().getAgent(5, TeamSide.LEFT.toString(), false); // non blocking call - if agent does not exist then is null

			if(agent == null) {
				agentData = new AgentData(1, TeamSide.LEFT, "ANDROIDS");
			} else {
				agentData = agent.getAgentData();
			}
			
			logger.info("Test initialized - waiting for PlayOn mode.");
			return true;
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Unable to initialize test", ex);
			return false;
		}

	}

	@Override
	public boolean isStopCriterionMet(SimulationState ss) {
		Player p = null;
		if (!started) {
			if (ss.getGameStateInfo().getPlayMode() == PlayMode.PlayOn.ordinal()) {
				try {
					logger.info("Test measurement started");
					started = true;
					//server.setBallPosition(new Vector3(1 + 5, 1, 0.4).asPoint3D());
					//server.setAgentPosition(agentData, initPos.asPoint3D());
					p = ss.getScene().getPlayers().get(0);
					
					if (agent != null) {
//						agent.invokeMove("walk_fine_fast2_optimized");
					}
					Thread.sleep(500);

					// kontrola ci je na zaciatku ihriska
					if (monitor.getSimulationState().getScene().getPlayers().get(0).getLocation().getXYDistanceFrom(initPos) > 2) return false;

					startTime = getElapsedTime();
					lastGroundTouchTime = startTime;
					lastLocation = p.getLocation();
					trestneSekundy = 0;
				//} catch (IOException e) {
				//	logger.log(Level.FINE, "Error running test", e);
				} catch (InterruptedException e) {
					logger.log(Level.FINE, "Error running test", e);
				}
				startTime = getElapsedTime();
			}
			return false;
		} else {
			p = ss.getScene().getPlayers().get(0);
			
			if (playerBehindBorder(p)) {	// controls if player is put behind border by server
				playerLocationXBeforePutBehindBorder = lastLocation.getX();
				return true;
			}
			
			if (p.isOnGround() && (getElapsedTime() - lastGroundTouchTime) > 1) {
				lastGroundTouchTime = getElapsedTime();
				trestneSekundy += 10;
			}
			
			if (lastLocation != null) { // kontrola ci nebol resetnuty serverom (ci nepresiel privelku vzdialenost za jeden refresh)
				if (lastLocation.getXYDistanceFrom(p.getLocation()) > 1) {
					return true;
				}
			}
			if (p.getLocation().getX() >= 0 || getElapsedTime() - startTime > 180)
				return true;
			
			lastLocation = p.getLocation();
			
			return false;
		}
	}
	
	/**
	 * Checks if player is out of the field.
	 * 
	 * @author xsuchac
	 * @author A55-Kickers
	 * 
	 * @param player
	 * @return true if player is behind any border of field, else false.
	 */
	private boolean playerBehindBorder(Player player) {
		double playerLocationX = player.getLocation().getX();
		double playerLocationY = player.getLocation().getY();
		
		if (Math.abs(playerLocationX) > (fieldLength / 2) 
				|| Math.abs(playerLocationY) > (fieldWidth / 2)) {
			return true;
		}
		return false;
	}
	
	/* stary kod zle fungujuci, zakomentoval: xsuchac a55-kickers
	@Override
	public TestCaseResult evaluate(SimulationState ss) {
		Player p = ss.getScene().getPlayers().get(0);
		logger.fine("player location.x: " + p.getLocation().getX() + " time " + (getElapsedTime() - startTime));
		logger.fine("+ trestne sekundy " + trestneSekundy + " nezapocitane do finalu");
		if (p.getLocation().getX() >= 0) { // uspesne presiel polku ihriska
			logger.fine("result " + (getElapsedTime() - startTime) + " trestne " + trestneSekundy 
					+ " dokopy " + (getElapsedTime() - startTime + trestneSekundy));
			return new TestCaseResult(getElapsedTime() - startTime);
		} else if (lastLocation.getXYDistanceFrom(p.getLocation()) > 1) { // pohol sa velmi rychlo - pravdepodobne bol resetnuty
			return new TestCaseResult(-1);
		} else { // nepresiel polku ihriska -> linearna aproximacia
			double time = getElapsedTime() - startTime;
			time = 180;
			if (lastLocation.getXYDistanceFrom(p.getLocation()) > 1) {	// pohol sa velmi rychlo - pravdepodobne bol resetnuty
				
			}
			double currentPlayerLocation = p.getLocation().getX();
			double traveled = 0;
			if (currentPlayerLocation < 0) {
				traveled = fieldLength/4 * - Math.abs(currentPlayerLocation);
			} else {
				traveled = (fieldLength/2) - (fieldLength/4 * - Math.abs(currentPlayerLocation));
			}
			
			double result = (fieldLength/2) /  traveled * time;
			logger.fine("result " + result + " trestne " + trestneSekundy + " dokopy " + (result + trestneSekundy));
			return new TestCaseResult(result);
		}
	}
	*/
	
	@Override
	public TestCaseResult evaluate(SimulationState ss) {
		Player player = ss.getScene().getPlayers().get(0);
		double currentPlayerLocationX = player.getLocation().getX();
		double resultTime = getElapsedTime() - startTime;
		logger.fine("player location.x: " + currentPlayerLocationX);
		logger.fine("Time: " + resultTime);
		
		if (currentPlayerLocationX >= 0) { // uspesne presiel polku ihriska
			logger.info("Attempt successful.");
			logger.info("Time: " + resultTime);
			logger.info("+ penalization: " + trestneSekundy + " time+penalization: " + (resultTime + trestneSekundy));
			return new TestCaseResult(resultTime + trestneSekundy);
		} else { // nepresiel polku ihriska -> linearna aproximacia
			double maxTime = 180;
			double traveled = Math.abs(initPos.getX()) - Math.abs(currentPlayerLocationX);
			if (Math.abs(playerLocationXBeforePutBehindBorder) <= (fieldLength / 2)) {
				traveled = Math.abs(initPos.getX()) - Math.abs(playerLocationXBeforePutBehindBorder);
			}
			double result = -5000;
			
			if (traveled != 0) {
				result = (Math.abs(initPos.getX()) * maxTime) / traveled;
			}
			
			logger.info("Attempt not successful, result was calculated approximately.");
			logger.info("Result: " + result);
			logger.info("+ penalization: " + trestneSekundy + ", time+penalization: " + (result + trestneSekundy));
			return new TestCaseResult(result + trestneSekundy);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		AgentMonitor.removeMessageListener(1, "ANDROIDS", this);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.fine("Test case destroyed");
	}

	@Override
	public void receivedMessage(int uniform, String team, AgentMonitorMessage message) {
		// registrovany bol listener iba na typ LOWSKILL takze ClassCasting je bezpecny
		AgentMonitorMessage.HighSkill msg = (HighSkill) message;
		switch(msg.action) {
		case start:
			//				msg.move_name a msg.playertime
			break;
		case stop:
			//				msg.move_name a msg.playertime
			break;
		}
	}

}
