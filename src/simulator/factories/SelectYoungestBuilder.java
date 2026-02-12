package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectYoungest;
import simulator.model.SelectionStrategy;

public class SelectYoungestBuilder extends Builder<SelectionStrategy> {
	public SelectYoungestBuilder() {
        super("youngest", "Selects the youngest animal in the list");
    }

    @Override
    protected SelectionStrategy createInstance(JSONObject data) {
    	
        if (data == null) {
            throw new IllegalArgumentException("SelectYoungestBuilder: 'data' cannot be null");
        }
        // debe ser vacío según el enunciado
        if (data.length() != 0) {
            throw new IllegalArgumentException(
                "SelectYoungestBuilder: 'data' must be empty {}, but was: " + data.toString()
            );
        }
        return new SelectYoungest();
    }
}
