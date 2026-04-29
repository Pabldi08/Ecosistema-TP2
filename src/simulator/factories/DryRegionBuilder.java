package simulator.factories;

import org.json.JSONObject;

import simulator.model.DryRegion;
import simulator.model.Region;

public class DryRegionBuilder extends Builder<Region>{

	public DryRegionBuilder() {
		super("dry", "Dry region builder");
	}
	
	@Override
	protected void fillInData(JSONObject o) {  
		o.put("r1", "food increase factor (optional, default 2.0)");
		o.put("r2", "initial amount of food (optional, default 100.0)");
		o.put("n", "food increase factor (optional, default 2.0)");
		o.put("m", "initial amount of food (optional, default 100.0)");	
	} 

	@Override
	protected Region createInstance(JSONObject data) {
		double r1 = data.optDouble("r1", 0.95);
		double r2 = data.optDouble("r2", 0.95);
		double n = data.optDouble("n", 10);
		double m = data.optDouble("m", 5);		
		return new DryRegion(n,m,r1,2);
	}

}
