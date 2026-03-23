package simulator.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	// TODO definir atributos necesarios
	private Controller ctrl;
	private List<RegionData> regions;
	
	
	RegionsTableModel(Controller ctrl) {
		this.ctrl = ctrl;
		this.regions = new ArrayList<>();
		this.ctrl.addObserver(this);
		
	}

	@Override
	public int getRowCount() {
		return this.regions.size();
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
		regions.clear();
		
		Iterator<RegionData> an = map.iterator();
		while(an.hasNext()) {
			RegionData rd = an.next();
			regions.add(rd);
		}

		fireTableDataChanged();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		RegionData rd = regions.get(rowIndex);
		if(columnIndex == 0) {
			return rd.row();
		}
		else if(columnIndex == 1) {
			return rd.col();
		}
		else if(columnIndex ==2) {
			return rd.r();
		}else {
			Diet d = Diet.values()[columnIndex - 3];
			return rd.r().getAnimalsInfo().stream().filter(a -> a.getDiet() == d).count();
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		updateTable(map);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		updateTable(map);
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		updateTable(map);
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		updateTable(map);
	}

	@Override
	public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		updateTable(map);
	}
}
