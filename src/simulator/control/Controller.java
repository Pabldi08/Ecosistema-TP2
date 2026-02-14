package simulator.control;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.AnimalInfo;
import simulator.model.MapInfo;
import simulator.model.Simulator;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

public class Controller {
	private Simulator sim;
	
	public Controller(Simulator sim) {
		this.sim = sim;
	}
	
	public void loadData(JSONObject data) {
		
		if(data.has("regions")) { 								//TODO: que usamos el has o el isNull negado?
																//cuando en el enunciado pone que una key es opcional a que se refiere????
			JSONArray regions = data.getJSONArray("regions");
			for (int i = 0; i < regions.length(); i++) {
			    JSONObject region = regions.getJSONObject(i);
			    sim.setRegion(i, i, region);
			    JSONArray rows = region.getJSONArray("row");
			    JSONArray cols = region.getJSONArray("col");
			    int rf = rows.getInt(0);
			    int rt = rows.getInt(1);
			    int cf = cols.getInt(0);
			    int ct = cols.getInt(1);
			    JSONObject O = region.getJSONObject("spec");
			    
			    for(int R = rf; R < rt; R++) {
			    	for(int C = cf; C <ct; C++) {
			    		sim.setRegion(R,C,O);
			    	}
			    }
			}
		}
		
		JSONArray animals = data.getJSONArray("animals");
		for (int i = 0; i < animals.length(); i++) {
			JSONObject animal = animals.getJSONObject(i);
			int N = animal.getInt("amount");
			for(int j = 0; j < N; j++) {
				JSONObject O = animal.getJSONObject("spec");
				sim.addAnimal(O);
			}
		}		
	}
	
	private List<ObjInfo> toAnimalsInfo(List<? extends AnimalInfo> animals) {
		List<ObjInfo> ol = new ArrayList<>(animals.size());
		for (AnimalInfo a : animals)
			ol.add(new ObjInfo(a.getGeneticCode(), (int) a.getPosition().getX(), (int) a.getPosition().getY(),(int)Math.round(a.getAge())+2));
		return ol;
	}
	
	public void run(double t, double dt, boolean sv, OutputStream out) {
		
		JSONObject initState = sim.asJSON();
		
		SimpleObjectViewer view = null;  
		if (sv) {  
		   MapInfo m = sim.getMapInfo();  
		   view = new SimpleObjectViewer("[ECOSYSTEM]", m.getWidth(), m.getHeight(), m.getCols(), m.getRows());  
		   view.update(toAnimalsInfo(sim.getAnimals()), sim.getTime(), dt);  
		}
		
		while(sim.getTime() <= t) {
			sim.advance(dt);
			if (sv) view.update(toAnimalsInfo(sim.getAnimals()), sim.getTime(), dt);
		}
		JSONObject finalState  = sim.asJSON();
		
		JSONObject obj = new JSONObject();
		obj.put("in", initState);
		obj.put("out", finalState);
		
		//TODO: Como escribimos en out, asi se hacia en TP1 por lo menos??
		/*try (OutputStreamWriter writer = new OutputStreamWriter(out)) {
		    obj.write(writer); 
		    writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			out.write(obj.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		PrintStream p = new PrintStream(out);
		p.println(obj.toString(2));
		
		if (sv) view.close();
	}
}
