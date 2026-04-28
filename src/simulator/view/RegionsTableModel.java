package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;


import simulator.control.Controller;
import simulator.model.Animal.Diet;
import simulator.model.Animal.State;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {

	private Controller ctrl;
	//private List<RegionData> regions;
	private List<Info> regiones;
	
	private class Info {
		private int row;
		private int col;
		private String desc;
		private Map<Diet, Integer> diets;
		
		public Info(int row, int col, String desc , Map<Diet, Integer> diets) {
			this.row = row;
			this.col = col;
			this.desc = desc;
			this.diets = diets;
		}
	}
	
	RegionsTableModel(Controller ctrl) {
		this.ctrl = ctrl;
		this.regiones = new ArrayList<>();
		this.ctrl.addObserver(this);
		
	}

	@Override
	public int getRowCount() {
		return this.regiones.size();
	}

	@Override
	public int getColumnCount() {
		return Diet.values().length + 3;
	}
	
	@Override
    public String getColumnName(int column) {
		switch(column) {
		case 0:
			return "Row";
		case 1:
			return "Col";
		case 2:
			return "Desc.";
		default:
			return Diet.values()[column - 3].name();
		}
    }
	
	private void updateTable(MapInfo map) {
		regiones.clear();
		
		Iterator<RegionData> an = map.iterator();
		while(an.hasNext()) {
			RegionData rd = an.next();
			
			Map<Diet, Integer> m = new HashMap<>();
			for(Diet diet : Diet.values()) {
				m.put(diet, 0);
			}
				
			for(AnimalInfo a : rd.r().getAnimalsInfo()) {
				Diet d = a.getDiet();
				m.put(d, m.get(d)+1);
			}
			
			Info i = new Info(rd.row(), rd.col(), rd.r().toString(),m);
			regiones.add(i);
		}

		fireTableDataChanged();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Info i = regiones.get(rowIndex);
		if(columnIndex == 0) {
			return i.row;
		}
		else if(columnIndex == 1) {
			return i.col;
		}
		else if(columnIndex ==2) {
			return i.desc;
		}else {
			Diet d = Diet.values()[columnIndex - 3];
			return i.diets.get(d);
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		SwingUtilities.invokeLater(() -> updateTable(map));
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		SwingUtilities.invokeLater(() -> updateTable(map));
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		SwingUtilities.invokeLater(() -> updateTable(map));
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		SwingUtilities.invokeLater(() -> updateTable(map));
	}

	@Override
	public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		SwingUtilities.invokeLater(() -> updateTable(map));
	}
}
