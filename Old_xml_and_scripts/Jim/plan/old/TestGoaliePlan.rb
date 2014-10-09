require 'singleton'
include_class Java::sk.fiit.jim.agent.models.EnvironmentModel

class TestGoaliePlan
  include Singleton
    
  def initialize
    @plan = []
    @plan << LowSkillTest.new("fall_left")
    @beamed = false
  end
  
  def control
    begin
      @plan[0].execute unless @plan.empty?
    end
  end
  
    
end

