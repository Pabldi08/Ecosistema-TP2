package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


import simulator.control.Controller;
import simulator.model.Animal.Diet;

import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;

import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {

	// TODO definir atributos necesarios
	private Controller ctrl;
	
	private List<RegionInfo> regions;
	
	RegionsTableModel(Controller ctrl) {
		this.ctrl = ctrl;
		this.regions = new ArrayList<>();
		this.ctrl.addObserver(this);
		
		// TODO inicializar estructuras de datos correspondientes
		// TODO registrar this como observador
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

		for (int i = 0; i < map.getRows(); i++) {
			for (int j = 0; j < map.getCols(); j++) {
				this.regions.add(map.getRegionInfo(i, j));
			}
		}

		fireTableDataChanged();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		RegionInfo r = regions.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return r.getRow();
		case 1:
			return r.getCol();
		case 2:
			return r.getDescription();
		default:
			return r.getAnimalCount(Diet.values()[columnIndex - 3]);
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
