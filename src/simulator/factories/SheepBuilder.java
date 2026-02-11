package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Sheep;

public class SheepBuilder extends Builder<Sheep> {
	public SheepBuilder() {
        super("sheep", "Creates a sheep");
    }

    @Override
    protected Sheep createInstance(JSONObject data) {
    	
        if (data == null) {
            throw new IllegalArgumentException("Sheep: 'data' cannot be null");
        }
        
        if (data.length() != 3) {
            throw new IllegalArgumentException(
                "Sheep: 'data' must have 3 keys, but was: " + data.toString()
            );
        } else {
    		SelectionStrategy mateStrategy = new SelectFirst();
    		SelectionStrategy dangerStrategy = new SelectFirst();
    		Vector2D position = null;
    		
    		if(!data.isNull("mate_strategy")) {
    			BuilderBasedFactory<SelectionStrategy> bbf = new BuilderBasedFactory<SelectionStrategy>();
    			mateStrategy = bbf.createInstance(data.getJSONObject("mate_strategy"));
    		}
    		if(!data.isNull("danger_strategy")) {
    			BuilderBasedFactory<SelectionStrategy> bbf = new BuilderBasedFactory<SelectionStrategy>();
    			dangerStrategy = bbf.createInstance(data.getJSONObject("danger_strategy"));
    		}
    		if(!data.isNull("pos")) {
        		JSONObject pos = data.getJSONObject("pos");
        		JSONArray xRange = pos.getJSONArray("x_range");
        		JSONArray yRange = pos.getJSONArray("y_range");
        		double X = xRange.getDouble(0) + (Utils.RAND.nextDouble() * (xRange.getDouble(1)-xRange.getDouble(0)));
        		double Y = yRange.getDouble(0) + (Utils.RAND.nextDouble() * (yRange.getDouble(1)-yRange.getDouble(0)));
        		position = new Vector2D(X,Y);
    		}	
            return new Sheep(mateStrategy, dangerStrategy, position);
        }
    }
}