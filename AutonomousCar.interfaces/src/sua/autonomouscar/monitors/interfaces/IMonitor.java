package sua.autonomouscar.monitors.interfaces;

import sua.autonomouscar.interfaces.IIdentifiable;

public interface IMonitor  extends IIdentifiable {
	
	public void notifyRule(String ruleName);
}
