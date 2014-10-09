require 'singleton'
include_class Java::sk.fiit.jim.agent.models.EnvironmentModel

class TestInitPlan
  include Singleton
    
  def initialize
    @plan = []
  end
  
  def control
  end
  
end


