package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region>{

	public DynamicSupplyRegionBuilder() {
		super("dynamic", "Creates a dynamic region");
	}

	@Override
	protected Region createInstance(JSONObject data) {
        if (data.length() >= 2) {
            throw new IllegalArgumentException(
                "DynamicSupplyRegion: 'data' must have 2 keys, but was: " + data.toString()
            );
        } else {
        	double factor = 2.5;
        	double food = 1000.0;
        	if(!data.isNull("factor")) {
        		factor = data.getDouble("factor");
        	}
        	if(!data.isNull("food")) {
        		food = data.getDouble("food");
        	}
        	return new DynamicSupplyRegion(factor, food);
        }	
	}

}
