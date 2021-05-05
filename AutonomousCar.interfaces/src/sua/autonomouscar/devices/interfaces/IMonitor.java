package sua.autonomouscar.devices.interfaces;

import java.util.List;

import sua.autonomouscar.interfaces.IIdentifiable;

public interface IMonitor extends IIdentifiable{

	public IMonitor setProperty(String propName, Object value);
	public Object getProperty(String propName);
		
	public IMonitor registerMonitor();
	public IMonitor unregisterMonitor();
	public IMonitor addImplementedMonitor(String c);

}
