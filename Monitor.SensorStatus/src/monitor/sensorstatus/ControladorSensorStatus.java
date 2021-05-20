package monitor.sensorstatus;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.context.interfaces.IDistanceSensorContext;
import sua.autonomouscar.infrastructure.monitors.Monitor;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ESensorStatus;
import sua.autonomouscar.monitors.interfaces.IMonitor;
import sua.autonomouscar.properties.PropList;
import sua.autonomouscar.properties.interfaces.IProperty;

public class ControladorSensorStatus extends Monitor implements ServiceListener{

	protected BundleContext context = null;
	
	public ControladorSensorStatus(BundleContext context, String id) {
		super(context, id);
		this.context = context;
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		
		IDistanceSensorContext sensorDistContext = (IDistanceSensorContext)  this.context.getService(event.getServiceReference());
		boolean distanceSensorWorking = sensorDistContext.isDistanceSensorWorking();
		boolean lidarSensorWorking = false;
		
		//Falta pillar el del LIDAR.
		
		
		ServiceReference ref = this.context.getServiceReference(IProperty.class.getName());
		if ( ref == null ) {
			System.out.println("[Monitor] - No PropList found, nothing to do!");
			return;
		}
		PropList propertiesList = (PropList) this.context.getService(ref);
		
		
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			if(!distanceSensorWorking && !lidarSensorWorking) {
				propertiesList.setSensor_status_prop(ESensorStatus.No_Distance_Sensor);
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: sensor_status_prop=" + ESensorStatus.No_Distance_Sensor);			
					
			}else if(!distanceSensorWorking) {
				propertiesList.setSensor_status_prop(ESensorStatus.Distance_sensor_error);
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: sensor_status_prop=" + ESensorStatus.Distance_sensor_error);			
					
			}else if(!lidarSensorWorking) {
				propertiesList.setSensor_status_prop(ESensorStatus.Lidar_error);
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: sensor_status_prop=" + ESensorStatus.Lidar_error);			
			}
			
			//Aqui falla algo. Alguna de las condiciones sobra. Investigarlo please
			 if(distanceSensorWorking || lidarSensorWorking) {
				propertiesList.setSensor_status_prop(ESensorStatus.OK);
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: sensor_status_prop=" + ESensorStatus.OK);			
			 }
						
			break;

		default:
			break;
		}
		
		
		
	}

}
