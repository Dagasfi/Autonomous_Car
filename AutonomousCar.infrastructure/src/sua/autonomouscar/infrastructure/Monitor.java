package sua.autonomouscar.infrastructure;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.devices.interfaces.IMonitor;
import sua.autonomouscar.interfaces.IIdentifiable;

public class Monitor implements IMonitor{
	
	
	protected BundleContext context = null;
	protected Dictionary<String, Object> props = null;
	protected ServiceRegistration<?> s_reg = null;
	protected List<String> sensors = null;
	protected List<String> adaptProps = null;
	protected List<String> implementedMonitors = null;
	
	public Monitor(BundleContext context, String id) {
		this.context = context;
		this.props = new Hashtable<String, Object>();
		this.getDeviceProperties().put(IIdentifiable.ID, id);
		
		this.sensors = new ArrayList<String>();
		this.adaptProps = new ArrayList<String>();
	}
		
	protected BundleContext getBundleContext() {
		return this.context;
	}
	
	protected Dictionary<String, Object> getDeviceProperties() {
		return this.props;
	}
	
	// IDevice
	

	@Override
	public String getId() {
		return (String)this.getDeviceProperties().get(IIdentifiable.ID);
	}

	@Override
	public IMonitor setProperty(String propName, Object value) {
		this.getDeviceProperties().put(propName, value);
		if ( this.s_reg != null )
			this.s_reg.setProperties(this.getDeviceProperties());
		return this;
	}

	@Override
	public Object getProperty(String propName) {
		return this.getDeviceProperties().get(propName);
	}
	
	
	
	@Override
	public IMonitor addImplementedMonitor(String c) {
		this.implementedMonitors.add(c);
		return this;
	}
	
	@Override
	public IMonitor registerMonitor() {
		this.s_reg = this.getBundleContext().registerService(this.implementedMonitors.toArray(new String[this.implementedMonitors.size()]), this, this.getDeviceProperties());
		return this;
	}
	
	@Override
	public IMonitor unregisterMonitor() {
		if ( this.s_reg != null )
			this.s_reg.unregister();
		return this;
	}
}
