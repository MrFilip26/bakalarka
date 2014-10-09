########	plan.rb	 ########
#	   @author: Team17	    #
#############################

class PlanCreateFormation < Plan

  def replan
 	@plan.clear
	@tacticalInfo.isOnPosition();
	@tacticalInfo.isInPositionArea();
	@tacticalInfo.setMyFormPosition();
	@tacticalInfo.getFormPosition();
	
    if EnvironmentModel.beamablePlayMode
    	@agentInfo.loguj('Beam');
    	@plan << Beam.new
    	@beamed = true
     elsif (me.on_ground? or me.is_lying_on_back? or me.is_lying_on_belly?)
    	@agentInfo.loguj('GetUp');
     	@plan << GetUp.new(Proc.new{me.on_ground?})
    elsif (! @tacticalInfo.isOnPosition and ! @tacticalInfo.isInPositionArea)
    	@agentInfo.loguj('Walk');
    	@plan << FormationHelper.getHighSkillToGoToFormation
    elsif (not see_ball?) 
   		@agentInfo.loguj('Localize');
    	@plan << Localize.new(Proc.new{(not see_ball?)})
    elsif (@tacticalInfo.isInPositionArea)
  		@agentInfo.loguj('Koniec');
  		@plan << LowSkill.new("rollback")
    end

  end
end