package monitor.congestion;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.context.interfaces.ICongestionContext;
import sua.autonomouscar.devices.interfaces.IRoadSensor;
import sua.autonomouscar.driving.l0.manual.L0_ManualDriving;
import sua.autonomouscar.infrastructure.monitors.Monitor;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;
import sua.autonomouscar.monitors.interfaces.IMonitor;
import sua.autonomouscar.properties.PropList;
import sua.autonomouscar.properties.interfaces.IProperty;

public class ControladorCongestion extends Monitor implements ServiceListener{
	
	
	protected BundleContext context = null;
	
	public ControladorCongestion(BundleContext context, String id) {
		super(context, id);
		this.context = context;
	}


	@Override
	public void serviceChanged(ServiceEvent event) {
		IRoadSensor contextoCarretera = (IRoadSensor) this.context.getService(event.getServiceReference());
		ERoadStatus congestionStatus = contextoCarretera.getRoadStatus();
		ERoadType roadType = contextoCarretera.getRoadType();
				
		ServiceReference ref = this.context.getServiceReference(IProperty.class.getName());
		if ( ref == null ) {
			System.out.println("[Monitor] - No PropList found, nothing to do!");
			return;
		}
		PropList propertiesList = (PropList) this.context.getService(ref);
		
		
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			propertiesList.setCongestion_prop(congestionStatus);
			propertiesList.setRoad_type_prop(roadType);
			System.out.println("[Monitor] - Propiedad de adaptacion actualizada: congestion_prop=" + congestionStatus);			
			System.out.println("[Monitor] - Propiedad de adaptacion actualizada: road_type_prop=" + roadType);			
			break;
		default:
			break;
		}
		
	}
	
	

}
