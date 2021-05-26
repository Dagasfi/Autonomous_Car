package reglas.l3;

import contexto.ads.ContextoADS;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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
import sua.autonomouscar.driving.interfaces.IL3_DrivingService;
import sua.autonomouscar.driving.interfaces.IL3_HighwayChauffer;
import sua.autonomouscar.driving.interfaces.IL3_TrafficJamChauffer;
import sua.autonomouscar.driving.interfaces.IParkInTheRoadShoulderFallbackPlan;
import sua.autonomouscar.driving.l3.highwaychauffer.L3_HighwayChauffer;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.rules.Rule;
import sua.autonomouscar.interaction.interfaces.IInteractionMechanism;
import sua.autonomouscar.interaction.interfaces.INotificationService;
import sua.autonomouscar.interfaces.EDriverAttention;
import sua.autonomouscar.interfaces.ERoadStatus;
import sua.autonomouscar.interfaces.ERoadType;
import sua.autonomouscar.interfaces.ESensorStatus;
import sua.autonomouscar.properties.PropList;
import sua.autonomouscar.properties.interfaces.IProperty;
import sua.autonomouscar.simulation.console.commands.L0_ManualDrivingConfigurator;
import sua.autonomouscar.simulation.console.commands.L1_AssistedDrivingConfigurator;
import sua.autonomouscar.simulation.console.commands.L2_AdaptiveCruiseControlConfigurator;
import sua.autonomouscar.simulation.console.commands.L3_CityChaufferConfigurator;
import sua.autonomouscar.simulation.console.commands.L3_HighwayChaufferConfigurator;
import sua.autonomouscar.simulation.console.commands.L3_TrafficJamChaufferConfigurator;

public class L3Rules extends Rule implements ServiceListener{
	
	protected BundleContext context = null;
	
	
	public static final String FRONTDS = "FrontDistanceSensor";
	public static final String REARDS = "RearDistanceSensor";
	public static final String LEFTDS = "LeftDistanceSensor";
	public static final String RIGHTDS = "RightDistanceSensor";
	public static final String FRONTLIDAR = "LIDAR-FrontDistanceSensor";
	public static final String REARLIDAR = "LIDAR-RearDistanceSensor";
	public static final String LEFTLIDAR = "LIDAR-LeftDistanceSensor";
	public static final String RIGHTLIDAR = "LIDAR-RightDistanceSensor";
	

	
	
	ERoadType road_type_prop_prev = null;
	ERoadStatus road_status_prop_prev = null;
	String ADSType_prev = null;
	int currentAutLevel_prev = -1;
	String maxAutLevel_prev = "" ;
	Dictionary<String,Object> devicesWorkingMode_prev = null;
	ESensorStatus sensor_status_prop_prev = null;
	EDriverAttention user_status_prev = null;
	int userLocation_prev = -1;
	
	
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
		EDriverAttention user_status = propertyList.getUser_status_prop();
		int userLocation= propertyList.getUser_location_prop();
		
		
		Dictionary<String,Object> devicesWorkingMode = propertyList.getDevicesWorking();
		ESensorStatus sensor_status_prop = propertyList.getSensor_status_prop();
		
		switch (event.getType()) {
		case ServiceEvent.MODIFIED:
		case ServiceEvent.REGISTERED:
			
			//REGLAS DE ADAPTACIÓN ADS.
			
			if(currentAutLevel == 3) {
				// Comprobamos unicamente si ha cambiado el tipo de via. Sino estariamos 
				// ejecutando la regla cada mínimo cambio en las propiedades.
				if(road_type_prop_prev != road_type_prop
						&&  (road_type_prop == ERoadType.STD_ROAD
						|| road_type_prop == ERoadType.OFF_ROAD)) {
					//REGLA DE ADAPTACION L3_1
					this.execute_L3_1(maxAutLevel);
					System.out.println("[Reglas] - Efectuamos L3_1");
				}
				if(maxAutLevel.equals("3")) {
					//Comprobamos el estado de la via.
					
					if(road_status_prop_prev == ERoadStatus.FLUID
							&& road_status_prop == ERoadStatus.JAM
							&& ADSType.equals(ContextoADS.HIGHWAY)) {
						this.execute_L3_2();
						System.out.println("[Reglas] - Efectuamos L3_2");
					}
					
					if(ADSType_prev.equals(ContextoADS.HIGHWAY)
							&& ADSType.equals(ContextoADS.CITY)
							&& road_status_prop == ERoadStatus.FLUID) {
						//ESTANDO EN HIGHWAY, PASAMOS A CITY.
						this.execute_L3_3();
						System.out.println("[Reglas] - Efectuamos L3_3");
					}
					if(ADSType.equals(ContextoADS.JAM)
							&& road_type_prop.equals(ERoadType.HIGHWAY)
							&& road_status_prop_prev.equals(ERoadStatus.JAM)
							&& road_status_prop.equals(ERoadStatus.FLUID)) {
						//ESTANDO EN TrafficJAM y estando la carretera congestionada, se descongesitona
						this.execute_L3_4();
						System.out.println("[Reglas] - Efectuamos L3_4");
					}
					
					if(ADSType.equals(ContextoADS.JAM)
							&& road_type_prop_prev.equals(ERoadType.HIGHWAY)
							&& road_status_prop.equals(ERoadStatus.JAM)
							&& road_type_prop.equals(ERoadType.CITY)) {
						this.execute_L3_5();
						System.out.println("[Reglas] - Efectuamos L3_5");
					}
					
					if(ADSType.equals(ContextoADS.CITY)
							&& road_type_prop_prev.equals(ERoadType.CITY)
							&& road_type_prop.equals(ERoadType.HIGHWAY)) {
						// ACTIVAMOS Trafic jam o highway, al salir de la ciudad.
						this.execute_L3_6(road_status_prop);
						System.out.println("[Reglas] - Efectuamos L3_6");
					}
					
					
				}else if(maxAutLevel.equals("2")) {
						//ACtivamos alguno de Aut level 2.
						this.activate_onlyL2();
						System.out.println("[Reglas] - Activamos " + ContextoADS.LANEKEEPING);
			}else if(maxAutLevel.equals("1")) {
				//ACtivamos alguno de Aut level 2.
				this.activate_onlyL1();
				System.out.println("[Reglas] - Activamos " + ContextoADS.ASSISTED);
			}else if(maxAutLevel.equals("0")) {
				//ACtivamos alguno de Aut level 2.
				this.activate_onlyL0();
				System.out.println("[Reglas] - Activamos " + ContextoADS.MANUAL);
			}
				
				
				
				// REGLAS DE ADAPTACIÓN PARA SERVICIOS
				
				if(!sensor_status_prop.equals(ESensorStatus.OK)
						&& !(sensor_status_prop.equals(ESensorStatus.UNKNOWN))) {
					// SI NO ESTA TODO OK, ACTUAMOS.
					this.execute_L3_7(ADSType, sensor_status_prop, road_type_prop);
					System.out.println("[Reglas] - Efectuamos L3_7");
				}
				
				// REGLAS PARA LAS INTERACCIONES
				
				
				if(user_status_prev != user_status) {
					switch (user_status) {
					case Attentive:
						// Esta atento, manos en volante y en el asiento.
						this.execute_Interact_1_atentive(ADSType);
						this.activate_seatVibration("Driver");
						this.activate_wheelVibration();
						System.out.println("[Reglas] - Efectuamos INTERACT1_attentive");
						break;
					case Slept:
						this.execute_Interact_1_slept(ADSType);
						this.activate_seatVibration("Driver");
						this.activate_wheelVibration();
						System.out.println("[Reglas] - Efectuamos INTERACT1_Slept");
						break;
					case Not_Attentive:
						this.execute_Interact_1_no_frente(ADSType);
						this.activate_seatVibration("Driver");
						this.activate_wheelVibration();
						System.out.println("[Reglas] - Efectuamos INTERACT1_No_frente");
						// Interact 1 _ no frente
						// SI CONDUCTOR, SI WHEEL
						break;
					case Hands_on_wheel:
						this.activate_wheelVibration();
						this.activate_seatVibration("Driver");
						System.out.println("[Reglas] - Efectuamos INTERACT2_Wheel");
						//SI CONDUCTOR, SI WHEEL
						break;
					case Not_hands_on_wheel:
						this.deactivate_wheelVibration();
						this.activate_seatVibration("Driver");
						System.out.println("[Reglas] - Efectuamos INTERACT2_No_Hands_Wheel");

						// SI CONDUCTOR, NO WHEEL
						break;
					case Not_in_Driver_seat:
						this.deactivate_seatVibration("Driver");
						this.deactivate_wheelVibration();
						System.out.println("[Reglas] - Efectuamos INTERACT2_No_driver_seat");
						// NO CONDUCTOR, NO WHEEL
						break;
					default:
						break;
					}	
				}
			}
					
			else if(currentAutLevel <= 2) {
				if(maxAutLevel.equals("3")) {
					switch (road_type_prop) {
						case CITY:
							this.activate_onlyL3(0);
							System.out.println("[Reglas] - Activamos " + ContextoADS.CITY);
							break;
						case HIGHWAY:
							if(road_status_prop.equals(ERoadStatus.FLUID)) {
								this.activate_onlyL3(1);
								System.out.println("[Reglas] - Activamos " + ContextoADS.HIGHWAY);
							}else if(road_status_prop.equals(ERoadStatus.JAM)) {
								this.activate_onlyL3(2);
								System.out.println("[Reglas] - Activamos " + ContextoADS.JAM);
							}
							break;
						default:
							break;
							
					}
				}
				else if(maxAutLevel.equals("2")) {
					if(currentAutLevel <= 1) {
						//ACtivamos alguno de Aut level 2.
						this.activate_onlyL2();
						System.out.println("[Reglas] - Activamos " + ContextoADS.LANEKEEPING);
					}
				} else if(maxAutLevel.equals("1")) {
					if(currentAutLevel == 0) {
						this.activate_onlyL1();
						System.out.println("[Reglas] - Activamos " + ContextoADS.ASSISTED);
					}
				}
			}	
			
			System.out.println("[Reglas] - Regla de adaptación");
			System.out.println("\t[Reglas] - " + currentAutLevel + " -- " + maxAutLevel);
			
			break;
		default:
			break;
		}
		
		/**
		 * Actualizamos las propiedades en este punto de tiempo para poder
		 * consultar los cambios relevantes.
		 */
		road_type_prop_prev = road_type_prop;
		road_status_prop_prev = road_status_prop;
		ADSType_prev = ADSType;
		currentAutLevel_prev = currentAutLevel;
		maxAutLevel_prev = maxAutLevel;
		devicesWorkingMode_prev = devicesWorkingMode;
		sensor_status_prop_prev = sensor_status_prop;
		user_status_prev = user_status;
	}
	
	private void activate_onlyL0() {
		L0_ManualDrivingConfigurator.configure(context);
		L0_ManualDrivingConfigurator.start(context);
		IADSContext currentADSLevel = OSGiUtils.getService(context, IADSContext.class);

		currentADSLevel.setADSLevel(1);
		currentADSLevel.setADSType(ContextoADS.ASSISTED);
	}
	
	
	private void activate_onlyL1() {
		L1_AssistedDrivingConfigurator.configure(context);
		L1_AssistedDrivingConfigurator.start(context);
		IADSContext currentADSLevel = OSGiUtils.getService(context, IADSContext.class);

		currentADSLevel.setADSLevel(1);
		currentADSLevel.setADSType(ContextoADS.ASSISTED);
	}
	

	private void activate_onlyL2() {
		L2_AdaptiveCruiseControlConfigurator.configure(context);
		L2_AdaptiveCruiseControlConfigurator.start(context);
		IADSContext currentADSLevel = OSGiUtils.getService(context, IADSContext.class);
		currentADSLevel.setADSLevel(2);
		currentADSLevel.setADSType(ContextoADS.LANEKEEPING);
	}
	
	private void activate_onlyL3(int type) {
		
		IADSContext currentADSLevel = OSGiUtils.getService(context, IADSContext.class);
		
		switch(type) {
		case 0:
			// CITY
			L3_CityChaufferConfigurator.configure(context);
			L3_CityChaufferConfigurator.start(context);
			currentADSLevel.setADSLevel(3);
			currentADSLevel.setADSType(ContextoADS.CITY);
			break;
		case 1:
			// HIGHWAY
			L3_HighwayChaufferConfigurator.configure(context);
			L3_HighwayChaufferConfigurator.start(context);

			currentADSLevel.setADSLevel(3);
			currentADSLevel.setADSType(ContextoADS.HIGHWAY);
			break;
		case 2:
			// TrafficJam
			L3_TrafficJamChaufferConfigurator.configure(context);
			L3_TrafficJamChaufferConfigurator.configure(context);
			currentADSLevel.setADSLevel(3);
			currentADSLevel.setADSType(ContextoADS.JAM);
			break;
		}
	}
	
	private void activate_wheelVibration() {
		INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);
		if(!notificationService.getMechanisms().contains("SteeringWheel_HapticVibration")){
			notificationService.getMechanisms().add("SteeringWheel_HapticVibration");
		}
	}
	
	private void activate_seatVibration(String type) {
		INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);
		if(!notificationService.getMechanisms().contains(type + "Seat_HapticVibration")){
			notificationService.getMechanisms().add(type + "Seat_HapticVibration");
		}
	}
	
	private void deactivate_wheelVibration() {
		INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);
		if(notificationService.getMechanisms().contains("SteeringWheel_HapticVibration")){
			notificationService.getMechanisms().remove("SteeringWheel_HapticVibration");
		}
	}
	
	private void deactivate_seatVibration(String type) {
		INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);
		if(notificationService.getMechanisms().contains(type + "Seat_HapticVibration")){
			notificationService.getMechanisms().remove(type + "Seat_HapticVibration");
		}
	}
	
	
	private void execute_Interact_1_atentive(String ADSType) {
		IL3_DrivingService il3_drivingService = null;
		if(ADSType.equals(ContextoADS.JAM)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);
		}else if(ADSType.equals(ContextoADS.CITY)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_CityChauffer.class);
		}else if(ADSType.equals(ContextoADS.HIGHWAY)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		}	
		INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);
		List<IInteractionMechanism> mechanisms = OSGiUtils.getServices(context, IInteractionMechanism.class);
		if ( mechanisms != null && mechanisms.size() > 0 ) {
			
			for(IInteractionMechanism m : mechanisms) {
				if(!m.equals("DriverDisplay_VisualText")
						|| !m.equals("SteeringWheel_HapticVibration")
						|| !m.equals("DriverDisplay_VisualIcon")) {
					notificationService.removeInteractionMechanism(m.getId());
					
				}
			}
			if(!notificationService.getMechanisms().contains("DriverDisplay_VisualText")) {
				notificationService.addInteractionMechanism("DriverDisplay_VisualText");
			}
			if(!notificationService.getMechanisms().contains("SteeringWheel_HapticVibration")) {
				notificationService.addInteractionMechanism("SteeringWheel_HapticVibration");
			}
			if(!notificationService.getMechanisms().contains("DriverDisplay_VisualIcon")) {
				notificationService.addInteractionMechanism("DriverDisplay_VisualIcon");
			}
		}
		il3_drivingService.setNotificationService(notificationService.getId());
	}
	
	private void execute_Interact_1_slept(String ADSType) {
		IL3_DrivingService il3_drivingService = null;
		if(ADSType.equals(ContextoADS.JAM)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);
		}else if(ADSType.equals(ContextoADS.CITY)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_CityChauffer.class);
		}else if(ADSType.equals(ContextoADS.HIGHWAY)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		}	
		INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);
		List<IInteractionMechanism> mechanisms = OSGiUtils.getServices(context, IInteractionMechanism.class);
		if ( mechanisms != null && mechanisms.size() > 0 ) {			
			for(IInteractionMechanism m : mechanisms) {
				if(!m.equals("Speakers_AuditoryBeep")
						|| !m.equals("Speakers_AuditorySound")
						|| !m.equals("SteeringWheel_HapticVibration")
						|| !m.equals("DriverSeat_HapticVibration")) {
					
					notificationService.removeInteractionMechanism(m.getId());
				}
			}
			if(!notificationService.getMechanisms().contains("Speakers_AuditoryBeep")) {
				notificationService.addInteractionMechanism("Speakers_AuditoryBeep");
			}
			if(!notificationService.getMechanisms().contains("Speakers_AuditorySound")) {
				notificationService.addInteractionMechanism("Speakers_AuditorySound");
			}
			if(!notificationService.getMechanisms().contains("SteeringWheel_HapticVibration")) {
				notificationService.addInteractionMechanism("SteeringWheel_HapticVibration");
			}
			if(!notificationService.getMechanisms().contains("DriverSeat_HapticVibration")) {
				notificationService.addInteractionMechanism("DriverSeat_HapticVibration");
			}
		}
		il3_drivingService.setNotificationService(notificationService.getId());
	}
	
	private void execute_Interact_1_no_frente(String ADSType) {
		IL3_DrivingService il3_drivingService = null;
		if(ADSType.equals(ContextoADS.JAM)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);
		}else if(ADSType.equals(ContextoADS.CITY)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_CityChauffer.class);
		}else if(ADSType.equals(ContextoADS.HIGHWAY)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		}	
		INotificationService notificationService = OSGiUtils.getService(context, INotificationService.class);
		List<IInteractionMechanism> mechanisms = OSGiUtils.getServices(context, IInteractionMechanism.class);
		if ( mechanisms != null && mechanisms.size() > 0 ) {
			
			for(IInteractionMechanism m : mechanisms) {
				if(!m.equals("Speakers_AuditoryBeep")
						|| !m.equals("Speakers_AuditorySound")
						|| !m.equals("SteeringWheel_HapticVibration")
						|| !m.equals("DashboardDisplay_VisualText")
						|| !m.equals("DashboardDisplay_VisualIcon")
						|| !m.equals("Dashboard_VisualIcon")) {
					notificationService.removeInteractionMechanism(m.getId());
					
				}
			}
			if(!notificationService.getMechanisms().contains("Speakers_AuditoryBeep")) {
				notificationService.addInteractionMechanism("Speakers_AuditoryBeep");
			}
			if(!notificationService.getMechanisms().contains("Speakers_AuditorySound")) {
				notificationService.addInteractionMechanism("Speakers_AuditorySound");
			}
			if(!notificationService.getMechanisms().contains("SteeringWheel_HapticVibration")) {
				notificationService.addInteractionMechanism("SteeringWheel_HapticVibration");
			}
			if(!notificationService.getMechanisms().contains("DashboardDisplay_VisualText")) {
				notificationService.addInteractionMechanism("DashboardDisplay_VisualText");
			}
			if(!notificationService.getMechanisms().contains("DashboardDisplay_VisualIcon")) {
				notificationService.addInteractionMechanism("DashboardDisplay_VisualIcon");
			}
			if(!notificationService.getMechanisms().contains("Dashboard_VisualIcon")) {
				notificationService.addInteractionMechanism("Dashboard_VisualIcon");
			}
		}
		il3_drivingService.setNotificationService(notificationService.getId());
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
		IL3_HighwayChauffer theL3HighwayChaufferService = OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		IL3_TrafficJamChauffer theL3TrafficJamChaufferService = OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);

		IADSContext currentADSLevel = OSGiUtils.getService(context, IADSContext.class);

		theL3TrafficJamChaufferService.stopDriving();

		theL3HighwayChaufferService.startDriving();
		currentADSLevel.setADSLevel(3);
		currentADSLevel.setADSType(ContextoADS.HIGHWAY);
	}
	
	private void execute_L3_5() {
		IL3_CityChauffer theL3CityChaufferService = OSGiUtils.getService(context, IL3_CityChauffer.class);
		IL3_TrafficJamChauffer theL3TrafficJamChaufferService = OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);

		IADSContext currentADSLevel = OSGiUtils.getService(context, IADSContext.class);

		theL3TrafficJamChaufferService.stopDriving();

		theL3CityChaufferService.startDriving();
		currentADSLevel.setADSLevel(3);
		currentADSLevel.setADSType(ContextoADS.HIGHWAY);
	}
	
	private void execute_L3_6(ERoadStatus roadStatus) {
		
		IL3_HighwayChauffer theL3HighwayChaufferService = OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		IL3_CityChauffer theL3CityChaufferService = OSGiUtils.getService(context, IL3_CityChauffer.class);
		IL3_TrafficJamChauffer theL3TrafficJamChaufferService = OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);

		IADSContext currentADSLevel = OSGiUtils.getService(context, IADSContext.class);

		theL3CityChaufferService.stopDriving();
		
		if(roadStatus.equals(ERoadStatus.FLUID)) {
			theL3HighwayChaufferService.startDriving();
			currentADSLevel.setADSLevel(3);
			currentADSLevel.setADSType(ContextoADS.HIGHWAY);
		}else if(roadStatus.equals(ERoadStatus.JAM)) {
			theL3TrafficJamChaufferService.startDriving();
			currentADSLevel.setADSLevel(3);
			currentADSLevel.setADSType(ContextoADS.JAM);
		}
	}
	
	private void changeLidarSensors(IL3_DrivingService il3_drivingService) {
		//Cambiamos los lidar por DistanceSensors.
		il3_drivingService.setFrontDistanceSensor(this.FRONTDS);
		il3_drivingService.setRearDistanceSensor(this.REARDS);
		il3_drivingService.setLeftDistanceSensor(this.LEFTDS);
		il3_drivingService.setRightDistanceSensor(this.RIGHTDS);
	}
	
	private void changeDistanceSensors(IL3_DrivingService il3_drivingService) {
		il3_drivingService.setFrontDistanceSensor(this.FRONTLIDAR);
		il3_drivingService.setRearDistanceSensor(this.REARLIDAR);
		il3_drivingService.setLeftDistanceSensor(this.LEFTLIDAR);
		il3_drivingService.setRightDistanceSensor(this.RIGHTLIDAR);
	}
	private void executeFallbackPlan(IL3_DrivingService il3_drivingService, ERoadType road_type_prop) {
		
		IEmergencyFallbackPlan theEmergencyFallbackPlan = OSGiUtils.getService(context, IEmergencyFallbackPlan.class);
		IParkInTheRoadShoulderFallbackPlan theParkInTheRoadShoulderFallbackPlan = OSGiUtils.getService(context, IParkInTheRoadShoulderFallbackPlan.class);
		// Park in the roadshoulder -- debe ser STD o Highway.
		if(road_type_prop.equals(ERoadType.HIGHWAY) 
				|| road_type_prop.equals(ERoadType.STD_ROAD) ) {
			il3_drivingService.setFallbackPlan(theParkInTheRoadShoulderFallbackPlan.getId());
			il3_drivingService.stopDriving();
			theParkInTheRoadShoulderFallbackPlan.startDriving();
		}else {
			il3_drivingService.setFallbackPlan(theEmergencyFallbackPlan.getId());
			il3_drivingService.stopDriving();
			theEmergencyFallbackPlan.startDriving();
		}
	}
	
	private void execute_L3_7(String ADSType, ESensorStatus errorType, ERoadType road_type_prop) {
		System.out.println("L3_7:: " +errorType);
		IL3_DrivingService il3_drivingService = null;
		if(ADSType.equals(ContextoADS.JAM)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_TrafficJamChauffer.class);
		}else if(ADSType.equals(ContextoADS.CITY)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_CityChauffer.class);
		}else if(ADSType.equals(ContextoADS.HIGHWAY)) {
			il3_drivingService =  OSGiUtils.getService(context, IL3_HighwayChauffer.class);
		}	
		
		if(errorType.equals(ESensorStatus.No_Distance_Sensor)) {
			//No se pueden cambiar los sensores.
			executeFallbackPlan(il3_drivingService, road_type_prop);
		}else {
			if(errorType.equals(ESensorStatus.Lidar_error)) {
				//ERROR DE LIDAR, CAMBIAMOS.
				this.changeLidarSensors(il3_drivingService);
			}else if(errorType.equals(ESensorStatus.Distance_sensor_error)) {
				this.changeDistanceSensors(il3_drivingService);
			}
		}	
	}
	
}
