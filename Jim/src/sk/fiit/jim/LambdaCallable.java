package sk.fiit.jim;


/*
 * Interface is created to simulate lambda expression in Java using "method-passing" as arguments with help of anonymous inner classes
 * Example use:
 * new GoToPosition(position_to_go, new LambdaCallable(){ 
				public boolean call(){
					return EnvironmentModel.beamablePlayMode();
   				}
   		      });
   		      
 * In GoToPosition constructor:
 * public GoToPosition(Vector3D position, LambdaCallable validity_proc) {
 * 	this.validity_proc = validity_proc;
 * }
 * 
 * Call the execution: 
 * validity_proc.call();
 * 
 * @author Martin Markech
 */

public interface LambdaCallable {
	  public boolean call();
}