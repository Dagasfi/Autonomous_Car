package monitor.autonomouslevel;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.infrastructure.devices.DistanceSensor;
import sua.autonomouscar.infrastructure.devices.HumanSensors;
import sua.autonomouscar.infrastructure.devices.LineSensor;
import sua.autonomouscar.infrastructure.devices.RoadSensor;
import sua.autonomouscar.infrastructure.monitors.Monitor;
import sua.autonomouscar.monitors.interfaces.IMonitor;
import sua.autonomouscar.properties.PropList;
import sua.autonomouscar.properties.interfaces.IProperty;

public class ControladorAutonomousLevel extends Monitor implements ServiceListener{

	protected BundleContext context = null;
	
	protected Dictionary<String, Object> sensorWorking = new Hashtable<String, Object>();

	public static final String DISTANCE = "distance";
	public static final String LINE = "line";
	public static final String HUMAN = "human";
	public static final String ROAD = "road";
	

	
	public ControladorAutonomousLevel(BundleContext context, String id) {
		super(context, id);
		this.context = context;
	}
	
	
	private boolean everySensorOk (String type) {
		boolean retorno = true;
		for (Entry<String, Object> entry : ((Hashtable<String, Object>) this.sensorWorking).entrySet()) {
			if(entry.getKey().toLowerCase().contains(type)) {
				if(!(boolean)entry.getValue()) {
					retorno = false;
				}
			}
			
		}
		return retorno;
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		Object service = this.context.getService(event.getServiceReference());
		if(service instanceof DistanceSensor) {
			DistanceSensor sensor = (DistanceSensor) this.context.getService(event.getServiceReference());
			sensorWorking.put(sensor.getId(), sensor.isWorking());
		}
		if(service instanceof LineSensor) {
			LineSensor sensor = (LineSensor) this.context.getService(event.getServiceReference());
			sensorWorking.put(sensor.getId(), sensor.isWorking());
		}
		if(service instanceof HumanSensors) {
			HumanSensors sensor = (HumanSensors) this.context.getService(event.getServiceReference());
			sensorWorking.put(sensor.getId(), sensor.isWorking());
		}
		if(service instanceof RoadSensor) {
			RoadSensor sensor = (RoadSensor) this.context.getService(event.getServiceReference());
			sensorWorking.put(sensor.getId(), sensor.isWorking());
		}
		

		ServiceReference ref = this.context.getServiceReference(IProperty.class.getName());
		if ( ref == null ) {
			System.out.println("[Monitor] - No PropList found, nothing to do!");
			return;
		}
		PropList propertiesList = (PropList) this.context.getService(ref);
		
		
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			
			
			//L1 Necesita Distancia, Linea y NotificationService
			//L2 Necesita además, del Engine y del Steering.
			//L3 Necesita además. del RoadSensor y de IHumanSensors
			ServiceReference refEngine 		= this.context.getServiceReference(IEngine.class.getName());
			ServiceReference refSteering 	= this.context.getServiceReference(ISteering.class.getName());
			
			
			if(!this.everySensorOk(ControladorAutonomousLevel.DISTANCE) || !this.everySensorOk(ControladorAutonomousLevel.LINE)) {
				//No tenemos acceso a los sensores de distancia o line sensors: Nos vamos a L0
				propertiesList.setMaxAutonomyLevel(PropList.LEVELAUTONOMY0);;
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: max_autonomy_level="+PropList.LEVELAUTONOMY0);			
			}else if(refEngine == null || refSteering == null) {
				//NO podemos acceder al Engine o steering, nos vamos a L1.
				propertiesList.setMaxAutonomyLevel(PropList.LEVELAUTONOMY1);;
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: max_autonomy_level="+PropList.LEVELAUTONOMY1);			
			}else if(!this.everySensorOk(ControladorAutonomousLevel.HUMAN) || !this.everySensorOk(ControladorAutonomousLevel.ROAD)) {
				//Funciona todo excepto el Human y el Road. Vamos al 2.
				propertiesList.setMaxAutonomyLevel(PropList.LEVELAUTONOMY2);;
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: max_autonomy_level="+PropList.LEVELAUTONOMY2);
			}else if (this.everySensorOk("")){
				//Funciona todo.
				propertiesList.setMaxAutonomyLevel(PropList.LEVELAUTONOMY3);;
				System.out.println("[Monitor] - Propiedad de adaptacion actualizada: max_autonomy_level="+PropList.LEVELAUTONOMY3);			
			
			}
			break;

		default:
			break;
		}
		
		
		
		
		
	}

	
}
