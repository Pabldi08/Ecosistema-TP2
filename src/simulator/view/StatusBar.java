package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class StatusBar extends JPanel implements EcoSysObserver {

	private Controller ctrl;
	private JLabel time;
	private JLabel animals;
	private JLabel dimension;

	StatusBar(Controller ctrl) {
		this.ctrl = ctrl;
		initGUI();
		this.ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		// Time
		this.time = new JLabel("Time: ");
		this.time.setPreferredSize(new Dimension(80, 20));
		this.add(time);
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);
		
		// Animals
		this.animals = new JLabel("Total Animals: ");
		this.animals.setPreferredSize(new Dimension(130, 20));
		this.add(animals);
		s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);
		
		//Dimension
		this.dimension = new JLabel("Dimension: ");
		this.add(dimension);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		updateLabels(time, map, animals);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		updateLabels(time, map, animals);
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		updateLabels(time, map, animals);
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		this.dimension.setText("Dimension: " + map.getHeight() + "x" + map.getWidth()
		+ " " + map.getRows()+ "x" + map.getCols());
	}

	@Override
	public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		updateLabels(time, map, animals);
	}
	
	private void updateLabels(double time, MapInfo map, List<AnimalInfo> animals) {
		this.time.setText(String.format("Time: %.3f", time));
		this.animals.setText("Total Animlas: " + animals.size());
		this.dimension.setText("Dimension: " + map.getWidth() + "x" + map.getHeight()
		+ " " + map.getCols()+ "x" + map.getRows());
	}
}
