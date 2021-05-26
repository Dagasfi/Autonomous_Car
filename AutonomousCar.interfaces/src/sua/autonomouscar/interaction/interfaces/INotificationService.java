package sua.autonomouscar.interaction.interfaces;

import java.util.List;

import sua.autonomouscar.interfaces.IIdentifiable;

public interface INotificationService extends IIdentifiable {
	
	public INotificationService notify(String message);
	
	public INotificationService addInteractionMechanism(String mechanism);
	public INotificationService removeInteractionMechanism(String mechanism);
	
	public List<String> getMechanisms();	

}
