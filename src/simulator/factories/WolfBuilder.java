package simulator.factories;


import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.SelectionStrategy;
import simulator.model.Wolf;

public class WolfBuilder extends AnimalBuilder {
	

	public WolfBuilder(Factory<SelectionStrategy> bbf) {
		super("wolf", "Creates a wolf", bbf);
	}

	@Override
	protected Wolf createInstance(JSONObject data) {
        if (data.length() > 3) {
            throw new IllegalArgumentException(
                "Wolf: 'data' must have 3 or less keys, but was: " + data.toString()
            );
        } else {
        	SelectionStrategy mateStrategy = this.getStrategy(data, "mate_strategy");
    		SelectionStrategy dangerStrategy = this.getStrategy(data, "danger_strategy");
    		Vector2D position = this.getPosition(data);
            return new Wolf(mateStrategy, dangerStrategy, position);
        }
	}
}
