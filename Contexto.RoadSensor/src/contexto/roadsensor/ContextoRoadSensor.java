package contexto.roadsensor;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.context.interfaces.IRoadContext;
import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.interfaces.ERoadType;

public class ContextoRoadSensor extends Thing implements IRoadContext{

	protected BundleContext context = null;
	protected Dictionary<String, Object> props = new Hashtable<String, Object>();
	protected ServiceRegistration sr = null;
	
	
	public ContextoRoadSensor(BundleContext context, String id) {
		super(context, id);
		System.out.println("SE HA REGISTRADO EL ROADSENSOR");
		this.addImplementedInterface(IRoadContext.class.getName());
		this.props.put("id", id);
		this.context = context;
		this.setRoadType(ERoadType.UNKNOWN);
		this.sr = this.context.registerService(IRoadContext.class, this, props);
		this.registerThing();
	}
	
	@Override
	public ERoadType getRoadType() {
		return (ERoadType)this.props.get("type");
	}


	@Override
	public void setRoadType(ERoadType roadType) {
		this.props.put("type", roadType);
		this._updateProps();		
	}
	

	private void _updateProps() {
		if ( this.sr != null )
			this.sr.setProperties(this.props);
	}

}
