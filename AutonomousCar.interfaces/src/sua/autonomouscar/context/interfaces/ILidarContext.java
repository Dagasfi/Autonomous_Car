package sua.autonomouscar.context.interfaces;

public interface ILidarContext {

	public boolean isLidarWorking();
	
	public void setLidarWorkingMode(boolean isWorking); // Solo para simulación.
}
