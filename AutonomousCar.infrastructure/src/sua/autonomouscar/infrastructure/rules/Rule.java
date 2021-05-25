package sua.autonomouscar.infrastructure.rules;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.infrastructure.Thing;
import sua.autonomouscar.rules.interfaces.IRule;

public abstract class Rule extends Thing implements IRule{

	public Rule(BundleContext context, String id) {
		super(context, id);
		this.addImplementedInterface(IRule.class.getName());
	}

}
