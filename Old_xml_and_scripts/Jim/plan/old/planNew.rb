########  planNew.rb   ########
# @author: Team High 5    #
#############################

import Java::sk.fiit.jim.agent.models.EnvironmentModel
import Java::sk.fiit.jim.agent.models.TacticalInfo
import Java::sk.fiit.jim.agent.models.WorldModel
import Java::sk.fiit.jim.agent.models.AgentModel

class PlanNew < Plan
   
  def initialize
    super
    @param1 = 0.65
    @ShootInDistFromGoal = 5
  end
  
  def replan
    @agentModel = AgentModel.getInstance()
    
    @kick_target = @agentInfo.kickTarget()
    @kick_target_phi =  @kick_target.getPhi()
    @ball = WorldModel.getInstance.getBall.getRelativePosition
    
    @plan.clear
    if EnvironmentModel.beamablePlayMode
      @agentInfo.loguj('Beam');
      @queue.clear
      @plan << Beam.new
      
    elsif (@agentModel.falled?)
      @agentInfo.loguj('Get Up');
      @queue.clear
      #@plan << GetUp.new(Proc.new{@agentModel.on_ground?})
      @plan << GetUp.new(nil)
      
    elsif (ball_unseen > 3 )
      @agentInfo.loguj('Localize');
      @queue.clear
      @plan << Localize.new(Proc.new{not see_ball?})
        
    elsif (TacticalInfo.getInstance.bIsBallOurs())
      if(TacticalInfo.getInstance.bIsBallNearestToMe())
        attack
      else
        defence
      end
    else
      defence
    end
      
  end
  
  def defence
    @agentInfo.loguj('Defence');
    @agentInfo.loguj('Go to ball');
    @plan << WalkNew.new(false,@param1,0,Proc.new{not @agentModel.falled?})
  end
  
  def attack    
    @agentInfo.loguj('Attack');
    
    if (@ball.r < @param1 and turned_to_goal? )
      if(@agentInfo.kickTarget.r < @ShootInDistFromGoal)
        @agentInfo.loguj('Kick');
        @queue.clear
        @plan << Kick.new
      else
        @agentInfo.loguj('Go with ball');
        @queue.clear
        @plan << WalkNew.new(true,0,0,Proc.new{turned_to_goal? and @agentInfo.kickTarget.r > @ShootInDistFromGoal})
      end
    elsif (@ball.r > @param1)
        puts @ball.r
        @agentInfo.loguj('Go to ball');
        @queue.clear
        @plan << WalkNew.new(false,@param1,1,Proc.new{not @agentModel.falled?})
        
    elsif (!turned_to_goal? )
        @agentInfo.loguj('Turn');
        @queue.clear
        @plan << Turn.new(@kick_target,Proc.new{not turned_to_goal?})
    else
        puts "shit"
    end
  end
  
end