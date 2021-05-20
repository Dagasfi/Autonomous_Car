package sua.autonomouscar.context.interfaces;

import sua.autonomouscar.interfaces.ERoadStatus;

public interface ICongestionContext {
	
	public ERoadStatus getCongestion();
	public void setCongestion(ERoadStatus type);
	
}
