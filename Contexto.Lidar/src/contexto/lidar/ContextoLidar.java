package contexto.lidar;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.context.interfaces.IDistanceSensorContext;
import sua.autonomouscar.context.interfaces.ILidarContext;
import sua.autonomouscar.infrastructure.Thing;

public class ContextoLidar extends Thing implements ILidarContext{


	protected BundleContext context = null;
	protected Dictionary<String, Object> props = new Hashtable<String, Object>();
	protected ServiceRegistration sr = null;
	
	public ContextoLidar(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(ILidarContext.class.getName());
		this.props.put("id", id);
		this.context = context;
		this.setLidarWorkingMode(true);
		this.sr = this.context.registerService(ILidarContext.class, this, props);
	}

	@Override
	public boolean isLidarWorking() {
		return (boolean)this.props.get("isWorking");
	}

	@Override
	public void setLidarWorkingMode(boolean isWorking) {
		this.props.put("isWorking", isWorking);
		this._updateProps();
	}

	private void _updateProps() {
		if ( this.sr != null )
			this.sr.setProperties(this.props);
	}

}
