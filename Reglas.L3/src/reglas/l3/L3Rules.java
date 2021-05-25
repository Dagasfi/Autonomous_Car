package reglas.l3;

import contexto.ads.ContextoADS;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.context.interfaces.IADSContext;
import sua.autonomouscar.driving.interfaces.IEmergencyFallbackPlan;
import sua.autonomouscar.driving.interfaces.IL0_ManualDriving;
import sua.autonomouscar.driving.interfaces.IL1_AssistedDriving;
import sua.autonomouscar.driving.interfaces.IL2_AdaptiveCruiseControl;
import sua.autonomouscar.driving.interfaces.IL2_LaneKeepingAssist;
import sua.autonomouscar.driving.interfaces.IL3_CityChauffer;
import sua.autonomouscar.driving.interfaces.IL3_HighwayChauffer;
import sua.autonomouscar.driving.interfaces.IL3_TrafficJamChauffer;
import sua.autonomouscar.driving.interfaces.IParkInTheRoadShoulderFallbackPlan;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.rules.Rule;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;
import sua.autonomouscar.properties.PropList;
import sua.autonomouscar.properties.interfaces.IProperty;

public class L3Rules extends Rule implements ServiceListener{
	
	protected BundleContext context = null;
	
	ERoadType road_type_prop_prev = null;
	ERoadStatus road_status_prop_prev = null;
	String ADS_type_prev = null;

	public L3Rules(BundleContext context, String id) {
		super(context, id);
		this.context = context;
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		
		IProperty propertyList = (IProperty) this.context.getService(event.getServiceReference());
				
		ERoadType road_type_prop = propertyList.getRoad_type_prop();
		ERoadStatus road_status_prop = propertyList.getCongestion_prop();
		
		String maxAutLevel = propertyList.getMaxAutonomyLevel();
		int currentAutLevel = propertyList.getCurrentADSLevel();
		String ADSType = propertyList.getCurrentADSType();
		
		// int localizacion =  propertyList.getUser_location_prop();

		
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			
			if(currentAutLevel == 3) {
				// Comprobamos unicamente si ha cambiado el tipo de via. Sino estariamos 
				// ejecutando la regla cada mínimo cambio en las propiedades.
				if(road_type_prop_prev != road_type_prop
						&&  road_type_prop == ERoadType.STD_ROAD
						|| road_type_prop == ERoadType.OFF_ROAD) {
					//REGLA DE ADAPTACION L3_1
					System.out.println("[Reglas] - Efectuamos L3_1");
					this.execute_L3_1(maxAutLevel);
				}
				
				//Comprobamos el estado de la via.
				
				if(road_status_prop_prev == ERoadStatus.FLUID
						&& road_status_prop == ERoadStatus.JAM
						&& ADS_type_prev.equals(ContextoADS.HIGHWAY)
						&& ADSType.equals(ContextoADS.HIGHWAY)) {
					System.out.println("[Reglas] - Efectuamos L3_2");
					this.execute_L3_2();
				}
				
				if(ADS_type_prev.equals(ContextoADS.HIGHWAY) && ADSType.equals(ContextoADS.CITY)) {
					//ESTANDO EN HIGHWAY, PASAMOS A CITY.
					this.execute_L3_3();
				}
				
				
				if(ADSType.equals(ContextoADS.JAM)
						&& road_type_prop.equals(ERoadType.HIGHWAY)
						&& road_status_prop_prev.equals(ERoadStatus.JAM)
						&& road_status_prop.equals(ERoadStatus.FLUID)) {
					//ESTANDO EN TrafficJAM y estando la carretera congestionada, se descongesitona
					this.execute_L3_4();
				}
				
				if(ADSType.equals(ContextoADS.JAM)
						&& road_type_prop_prev.equals(ERoadType.HIGHWAY)
						&& road_status_prop_prev.equals(ERoadStatus.JAM)
						&& road_type_prop.equals(ERoadType.CITY)) {
					this.execute_L3_5();
				}
				
				if(ADSType.equals(ContextoADS.CITY)
						&& road_type_prop_prev.equals(ERoadType.CITY)
						&& road_type_prop.equals(ERoadType.HIGHWAY)) {
					// ACTIVAMOS Trafic jam o highway, al salir de la ciudad.
					this.execute_L3_6(road_status_prop);
				}
				
				
			}
			
			System.out.println("[Reglas] - Regla de adaptación=" + "");			
			break;
		default:
			break;
		}
		
		road_type_prop_prev = road_type_prop;
		road_status_prop_prev = road_status_prop;
	}
	
	
	private void execute_L3_1(String maxAutLevel) {
		IL1_AssistedDriving theL1AssistedDrivingService = OSGiUtils.getService(context, IL1_AssistedDriving.class);
		IL2_LaneKeepingAssist theL2LaneKeepingAssistService = OSGiUtils.getService(context, IL2_LaneKeepingAssist.class);
		IL2_AdaptiveCruiseControl theL2AdaptiveCruiserControlService = OSGiUtils.getService(context, IL2_AdaptiveCruiseControl.class);
		IL3_CityChauffer theL3CityChaufferService = OSGiUtils.getService(context, IL3_CityChauffer.class);
		IL3_HighwayChauffer theL3HighwayChaufferService = OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		IL3_TrafficJamChauffer theL3TrafficJamChaufferService = OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);
		IEmergencyFallbackPlan theEmergencyFallbackPlan = OSGiUtils.getService(context, IEmergencyFallbackPlan.class);
		IParkInTheRoadShoulderFallbackPlan theParkInTheRoadShoulderFallbackPlan = OSGiUtils.getService(context, IParkInTheRoadShoulderFallbackPlan.class);
		IL0_ManualDriving theL0ManualDrivingService = OSGiUtils.getService(context, IL0_ManualDriving.class);
		
		IADSContext currentADSLevel = OSGiUtils.getService(context, IADSContext.class);
		
		theL3HighwayChaufferService.stopDriving();
		theL3TrafficJamChaufferService.stopDriving();			
		theL3CityChaufferService.stopDriving();
		theEmergencyFallbackPlan.stopDriving();
		theParkInTheRoadShoulderFallbackPlan.stopDriving();
		theL0ManualDrivingService.stopDriving();
		theL1AssistedDrivingService.stopDriving();
		theL2AdaptiveCruiserControlService.stopDriving();
		theL2LaneKeepingAssistService.stopDriving();

		if(maxAutLevel.equals(PropList.LEVELAUTONOMY3) || maxAutLevel.equals(PropList.LEVELAUTONOMY2)) {
			//ACtivamos L2
			theL2LaneKeepingAssistService.startDriving();
			currentADSLevel.setADSLevel(2);
			currentADSLevel.setADSType(ContextoADS.LANEKEEPING);
		}else if(maxAutLevel.equals(PropList.LEVELAUTONOMY1)) {
			//Activamos L1
			theL1AssistedDrivingService.startDriving();
			currentADSLevel.setADSLevel(1);
			currentADSLevel.setADSType(ContextoADS.ASSISTED);
		}else {
			/////////
			// Deberia ocurrir? Pasamos a L0.
			/////////
			theL0ManualDrivingService.startDriving();
			currentADSLevel.setADSLevel(0);
			currentADSLevel.setADSType(ContextoADS.MANUAL);

		}
	}

	private void execute_L3_2() {
		IL3_HighwayChauffer theL3HighwayChaufferService = OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		IL3_TrafficJamChauffer theL3TrafficJamChaufferService = OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);

		IADSContext currentADSLevel = OSGiUtils.getService(context, IADSContext.class);

		theL3HighwayChaufferService.stopDriving();

		theL3TrafficJamChaufferService.startDriving();
		currentADSLevel.setADSLevel(3);
		currentADSLevel.setADSType(ContextoADS.JAM);
	}

	private void execute_L3_3() {
		IL3_HighwayChauffer theL3HighwayChaufferService = OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		IL3_CityChauffer theL3CityChaufferService = OSGiUtils.getService(context, IL3_CityChauffer.class);

		IADSContext currentADSLevel = OSGiUtils.getService(context, IADSContext.class);

		theL3HighwayChaufferService.stopDriving();

		theL3CityChaufferService.startDriving();
		currentADSLevel.setADSLevel(3);
		currentADSLevel.setADSType(ContextoADS.CITY);
	}
	
	private void execute_L3_4() {
		return;
	}
	
	private void execute_L3_5() {
		return;
	}
	
	private void execute_L3_6(ERoadStatus roadStatus) {
		if(roadStatus.equals(ERoadStatus.FLUID)) {
			//Debe activar L3_HIGHWAYCHAUFFER
		}else if(roadStatus.equals(ERoadStatus.JAM)) {
			//Debe activar L3_JAMCHAUFFER

		}
	}
	
	
}
