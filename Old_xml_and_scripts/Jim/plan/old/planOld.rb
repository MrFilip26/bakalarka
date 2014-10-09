########	planOld.rb	 ########
#	@author: Androids		#
#############################

class PlanOld < Plan
   
  def initialize
    super
  end
  
  def replan
    @plan.clear
    if EnvironmentModel.beamablePlayMode
      @queue.clear
      @plan << Beam.new
      @beamed = true
    elsif (me.on_ground? or me.is_lying_on_back? or me.is_lying_on_belly?)
      @queue.clear
      @plan << GetUp.new(Proc.new{me.on_ground?})
    elsif @queue.empty?
         #@plan << LowSkill.new("padvpred")
         #@plan << LowSkill.new("padvzad")
         #@plan << LowSkill.new("walk_fine_fast2")
         #@plan << LowSkill.new("kick_straight_edge_r")
         #@plan << LowSkill.new("kick_right")
         
         #@plan << LowSkill.new("turnleft90")
         #@plan << LowSkill.new("turnleft90")
         #@plan << LowSkill.new("turn_left_cont_20")
         #@plan << LowSkill.new("turn_left_cont_5")
         #@plan << Trajectory.new()
         @queue << WalkBehindBall.new()
         @queue << LowSkill.new("rollback")
         
         #@plan << Walk.new(:ball, Proc.new{is_ball_mine? or (not see_ball?)})
         
         #@plan << LowSkill.new("walk_fine_fast2_optimized")
         
         #@plan << LowSkill.new("sit_down")
         #@plan << LowSkill.new("sit_stand")
         #@plan << LowSkill.new("turnleft90")
         #@plan << LowSkill.new("kick_right")
         #@plan << LowSkill.new("fall_right")
         
         #@plan << LowSkill.new("walk_fine_fast1")
         #@plan << LowSkill.new("walk_fine_fast2")
         #@plan << LowSkill.new("walk_fine_slow")
         #@plan << LowSkill.new("walk_fine_back")
         #@plan << LowSkill.new("walk_fine_fast1_optimized")
         #@plan << LowSkill.new("walk_fine_fast2_optimized")
         #@plan << LowSkill.new("walkback2")
      else
        @plan << @queue.shift
      end
  end
end