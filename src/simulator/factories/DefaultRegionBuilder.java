package simulator.factories;

import org.json.JSONObject;

import simulator.model.DefaultRegion;
import simulator.model.Region;

public class DefaultRegionBuilder extends Builder<Region>{

	public DefaultRegionBuilder() {
		super("default", "Creates a default region");
	}
	
	protected void fillInData(JSONObject o) {}

	@Override
	protected Region createInstance(JSONObject data) {
        if (data.length() != 0) {
            throw new IllegalArgumentException(
                "DefaultRegion: 'data' must be empty {}, but was: " + data.toString()
            );
        }
        return new DefaultRegion();
	}

}
