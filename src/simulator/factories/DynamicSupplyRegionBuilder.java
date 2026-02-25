package simulator.factories;

import org.json.JSONObject;

import simulator.model.Constants;
import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region>{

	public DynamicSupplyRegionBuilder() {
		super("dynamic", "Creates a dynamic region");
	}
	
	@Override
	protected void fillInData(JSONObject o) {  
		o.put("factor", "food increase factor (optional, default 2.0)");
		o.put("food", "initial amount of food (optional, default 100.0)");	
	} 


	@Override
	protected Region createInstance(JSONObject data) {
        if (data.length() > 2) {
            throw new IllegalArgumentException(
                "DynamicSupplyRegion: 'data' must have 2 keys, but was: " + data.toString()
            );
        } else {
        	double factor = Constants.FACTOR;
        	double food = Constants.INIT_FOOD;
        	if(!data.isNull("factor")) {
        		factor = data.getDouble("factor");
        	}
        	if(!data.isNull("food")) {
        		food = data.getDouble("food");
        	}
        	return new DynamicSupplyRegion(food, factor);
        }	
	}

}
