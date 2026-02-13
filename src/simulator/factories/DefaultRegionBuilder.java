package simulator.factories;

import org.json.JSONObject;

import simulator.model.DefaultRegion;
import simulator.model.Region;
import simulator.model.SelectFirst;

public class DefaultRegionBuilder extends Builder<Region>{

	public DefaultRegionBuilder() {
		super("default", "Creates a default region");
	}

	@Override
	protected Region createInstance(JSONObject data) {
		if (data == null) {
            throw new IllegalArgumentException("DefaultRegion: 'data' cannot be null");
        }
        // debe ser vacío según el enunciado
        if (data.length() != 0) {
            throw new IllegalArgumentException(
                "DefaultRegion: 'data' must be empty {}, but was: " + data.toString()
            );
        }
        return new DefaultRegion();
	}

}
