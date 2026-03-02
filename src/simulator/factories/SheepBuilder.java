package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.SelectionStrategy;
import simulator.model.Sheep;

public class SheepBuilder extends AnimalBuilder {
	
	
	
	public SheepBuilder(Factory<SelectionStrategy> bbf) {
        super("sheep", "Creates a sheep", bbf);
    }

    @Override
    protected Sheep createInstance(JSONObject data) {
        if (data.length() > 3) {
            throw new IllegalArgumentException(
                "Sheep: 'data' must have 3 or less keys, but was: " + data.toString()
            );
        } else { // TODO: subir más cosas a AnimalBuilder
    		SelectionStrategy mateStrategy = this.getStrategy(data, "mate_strategy");
    		SelectionStrategy dangerStrategy = this.getStrategy(data, "danger_strategy");
    		Vector2D position = this.getPosition(data);
            return new Sheep(mateStrategy, dangerStrategy, position);
        }
    }
}