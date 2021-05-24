package monitor.Road_type;


import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.context.interfaces.IRoadContext;
import sua.autonomouscar.infrastructure.monitors.Monitor;
import sua.autonomouscar.interfaces.ERoadType;
import sua.autonomouscar.monitors.interfaces.IMonitor;
import sua.autonomouscar.properties.PropList;
import sua.autonomouscar.properties.interfaces.IProperty;

public class ControladorRoadType extends Monitor implements ServiceListener{

	protected BundleContext context = null;

	public ControladorRoadType(BundleContext context, String id)
	{
		super(context, id);
		this.context = context;
	}
	
	
	@Override
	public void serviceChanged(ServiceEvent event) {
		// TODO Auto-generated method stub
		IRoadContext contextoRoad = (IRoadContext) this.context.getService(event.getServiceReference());
		ERoadType roadType = contextoRoad.getRoadType();
				
		ServiceReference ref = this.context.getServiceReference(IProperty.class.getName());
		if ( ref == null ) {
			System.out.println("[Monitor] - No PropList found, nothing to do!");
			return;
		}
		PropList propertiesList = (PropList) this.context.getService(ref);
		
		
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			propertiesList.setRoad_type_prop(roadType);
			System.out.println("[Monitor] - Propiedad de adaptacion actualizada: road_prop=" + roadType);			
			break;
		default:
			break;
		}
	}

}
