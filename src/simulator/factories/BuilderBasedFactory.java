package simulator.factories;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {  
	private Map<String, Builder<T>> builders;  
	private List<JSONObject> buildersInfo;

	public BuilderBasedFactory() {  
		this.builders = new HashMap<>();
		this.buildersInfo = new LinkedList<>();
	}

	public BuilderBasedFactory(List<Builder<T>> builders) {  
		this();
		for(Builder<T> b: builders) {
			this.addBuilder(b);
		}
	}

	public void addBuilder(Builder<T> b) {  
		this.builders.put(b.getTypeTag(),b);
		this.buildersInfo.add(b.getInfo());
	}

	@Override  
	public T createInstance(JSONObject info) {  
		if (info == null) {  
			throw new IllegalArgumentException("’info’ cannot be null");  
		}
		
		for(Map.Entry<String, Builder<T>> b: this.builders.entrySet()) {
			if(b.getKey().equals(info.getString("type"))){
				T a = b.getValue().createInstance(info.has("data") ? info.getJSONObject("data") : new JSONObject());
				if(a != null) return a;
			}
		}
		throw new IllegalArgumentException("Unrecognized ‘info’:" + info.toString());
	}

	@Override  
	public List<JSONObject> getInfo() {  
		return Collections.unmodifiableList(buildersInfo);  
	}
}