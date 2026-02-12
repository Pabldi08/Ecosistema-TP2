package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectClosest;
import simulator.model.SelectionStrategy;

public class SelectClosestBuilder extends Builder<SelectionStrategy> {
	public SelectClosestBuilder() {
        super("closest", "Selects the closest animal in the list");
    }

    @Override
    protected SelectionStrategy createInstance(JSONObject data) {
    	
        if (data == null) {
            throw new IllegalArgumentException("SelectClosestBuilder: 'data' cannot be null");
        }
        // debe ser vacío según el enunciado
        if (data.length() != 0) {
            throw new IllegalArgumentException(
                "SelectClosestBuilder: 'data' must be empty {}, but was: " + data.toString()
            );
        }
        return new SelectClosest();
    }
}
