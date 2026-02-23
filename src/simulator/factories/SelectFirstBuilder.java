package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;

public class SelectFirstBuilder extends Builder<SelectionStrategy> {

	public SelectFirstBuilder() {
        super("first", "Selects the first animal in the list");
    }

    @Override
    protected SelectionStrategy createInstance(JSONObject data) {     	
        if (data.length() != 0) {
            throw new IllegalArgumentException(
                "SelectFirstBuilder: 'data' must be empty {}, but was: " + data.toString()
            );
        }
        return new SelectFirst();
    }
}
