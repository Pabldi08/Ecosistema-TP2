package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal.State;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {

    private Controller ctrl;

    private Map<String, Map<State, Integer>> data;

    SpeciesTableModel(Controller ctrl) {
        this.ctrl = ctrl;
        this.data = new HashMap<>();
        this.ctrl.addObserver(this);
    }
    
    @Override
    public int getRowCount() {
        return data.keySet().size();
    }

    @Override
    public int getColumnCount() {
        return State.values().length + 1;
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
        String gcode = data.keySet().stream().toList().get(rowIndex);
        if (columnIndex == 0) {
            return gcode;
        } else {
            State s = State.values()[columnIndex - 1];
            return data.get(gcode).get(s);
        }
    }

    private void updateTable(List<AnimalInfo> animals) {
        data.clear();
        
        for (AnimalInfo a : animals) {
            String gcode = a.getGeneticCode();
            State state = a.getState();
            if (!data.containsKey(gcode)) {
                Map<State, Integer> counters = new HashMap<>();
                for (State s : State.values()) {
                    counters.put(s, 0);
                }
                data.put(gcode, counters);
            }
            data.get(gcode).put(state, data.get(gcode).get(state) + 1);
        }
        fireTableStructureChanged();
    }

    @Override
    public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
        updateTable(animals);
    }

    @Override
    public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
        updateTable(animals);
    }

    @Override
    public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
        updateTable(animals);
    }

    @Override
    public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {}

    @Override
    public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
        updateTable(animals);
    }
}