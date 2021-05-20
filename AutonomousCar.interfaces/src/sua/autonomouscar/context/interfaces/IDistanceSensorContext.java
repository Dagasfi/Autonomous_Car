package sua.autonomouscar.context.interfaces;

public interface IDistanceSensorContext {
	
	public boolean isDistanceSensorWorking();
	
	public void setDistanceSensorWorkingMode(boolean isWorking); // Solo para simulación.
	
}
