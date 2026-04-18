package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;

public abstract class  AnimalBuilder extends Builder<Animal> {
	
	private Factory<SelectionStrategy> bbf;

	public AnimalBuilder(String typeTag, String desc, Factory<SelectionStrategy> bbf) {
		super(typeTag, desc);
		this.bbf = bbf;
	}
	
	protected SelectionStrategy getStrategy(JSONObject data, String key) {
		return data.has(key) ? bbf.createInstance(data.getJSONObject(key)): new SelectFirst();
	}
	
	protected Vector2D getPosition(JSONObject data) {
		Vector2D position = null;
		if(!data.isNull("pos")){
			JSONObject pos = data.getJSONObject("pos");
			JSONArray xRange = pos.getJSONArray("x_range");
			JSONArray yRange = pos.getJSONArray("y_range");
			double X = xRange.getDouble(0) + (Utils.RAND.nextDouble() * (xRange.getDouble(1)-xRange.getDouble(0)));
			double Y = yRange.getDouble(0) + (Utils.RAND.nextDouble() * (yRange.getDouble(1)-yRange.getDouble(0)));
			position =  new Vector2D(X,Y);
		}
		return position;
	}	
}
