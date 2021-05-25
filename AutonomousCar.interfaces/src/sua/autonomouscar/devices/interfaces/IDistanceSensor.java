package sua.autonomouscar.devices.interfaces;

public interface IDistanceSensor {
	
	public int getDistance();

	public IDistanceSensor setDistance(int distance); // for simulation purposes only

	public void setWorking(boolean mode);
	public boolean isWorking();
	
}
