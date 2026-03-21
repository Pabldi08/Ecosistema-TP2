package simulator.view;

import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import simulator.control.Controller;
import simulator.model.Animal.State;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {
	
	private List<String> geneticCodes;
	private Map<String, Map<State, Integer>> data;

	private Controller ctrl;

	SpeciesTableModel(Controller ctrl) {
		this.ctrl = ctrl;
		this.ctrl.addObserver(this);
		// TODO inicializar estructuras de datos correspondientes
		// TODO registrar this como observador
	}
	// TODO el resto de métodos van aquí …

	@Override
	public int getRowCount() {
		return ;
	}

	@Override
	public int getColumnCount() {
		return State.values().length+1;
	}
	
	@Override
	public String getColumnName(int column) {
	    if (column == 0) {
	        return "Species";
	    } else {
	        return State.values()[column - 1].name();
	    }
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO Auto-generated method stub

	}
}
