package monitor.ads;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import sua.autonomouscar.context.interfaces.IADSContext;
import sua.autonomouscar.context.interfaces.IRoadContext;
import sua.autonomouscar.infrastructure.monitors.Monitor;
import sua.autonomouscar.monitors.interfaces.IMonitor;
import sua.autonomouscar.properties.PropList;
import sua.autonomouscar.properties.interfaces.IProperty;

public class ControladorADS extends Monitor implements ServiceListener{
	
	protected BundleContext context = null;

	public ControladorADS(BundleContext context, String id) {
		super(context, id);
		this.context = context;
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		// TODO Auto-generated method stub
		IADSContext contextoADS = (IADSContext) this.context.getService(event.getServiceReference());
		int autonomousDrivingServiceLevel = contextoADS.getADSLevel();
		String autonomousDrivingServiceType = contextoADS.getADSType();
				
		ServiceReference ref = this.context.getServiceReference(IProperty.class.getName());
		if ( ref == null ) {
			System.out.println("[Monitor] - No PropList found, nothing to do!");
			return;
		}
		PropList propertiesList = (PropList) this.context.getService(ref);
		
		
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			propertiesList.setCurrentADSLevel_prop(autonomousDrivingServiceLevel);
			propertiesList.setCurrentADSType_prop(autonomousDrivingServiceType);
			System.out.println("[Monitor] - Propiedad de adaptacion actualizada: CurrentADSLevel_prop=" + autonomousDrivingServiceLevel);			
			System.out.println("[Monitor] - Propiedad de adaptacion actualizada: CurrentADSType_prop=" + autonomousDrivingServiceType);			
			break;
		default:
			break;
		}
	}

}
