package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class ChangeRegionsDialog extends JDialog implements EcoSysObserver {

	private DefaultComboBoxModel<String> regionsModel;
	private DefaultComboBoxModel<String> fromRowModel;
	private DefaultComboBoxModel<String> toRowModel;
	private DefaultComboBoxModel<String> fromColModel;
	private DefaultComboBoxModel<String> toColModel;

	private DefaultTableModel dataTableModel;
	private Controller ctrl;
	private List<JSONObject> regionsInfo;

	private String[] headers = { "Key", "Value", "Description" };

	// TODO en caso de ser necesario, añadir los atributos aquí…
	ChangeRegionsDialog(Controller ctrl) {
		super((Frame) null, true);
		this.ctrl = ctrl;
		initGUI();
		this.ctrl.addObserver(this);
	}

	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		// Panel superior
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		JLabel help = new JLabel("<html>Select a region type, the rows/cols interval, and provide values for the "
				+ "parameters in the Value column (default values are used for parameters with no value).</html>");
		help.setAlignmentX(LEFT_ALIGNMENT);
		topPanel.add(help);
		mainPanel.add(topPanel);
		
		// Panel central
		JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Table setup
        this.regionsInfo = Main.regionFactory.getInfo();
        this.dataTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        this.dataTableModel.setColumnIdentifiers(this.headers);
        JTable table = new JTable(dataTableModel);
        centerPanel.add(new JScrollPane(table));
        mainPanel.add(centerPanel);
		// this.regionsModel es un modelo de combobox que incluye los tipos de regiones
		this.regionsModel = new DefaultComboBoxModel<>();
		
		// TODO añadir la descripción de todas las regiones a regionsModel. Para eso
		// usa la clave “desc” o “type” de los JSONObject en regionsInfo,
		// ya que estos nos dan información sobre lo que puede crear la factoría.

		// TODO crear un combobox que use regionsModel y añadirlo al diálogo.

		// TODO crear 4 modelos de combobox para this.fromRowModel, this.toRowModel,
		// this.fromColModel y this.toColModel.

		// TODO crear 4 combobox que usen estos modelos y añadirlos al diálogo.

		// TODO crear los botones OK y Cancel y añadirlos al diálogo.

		setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaño
		pack();
		setResizable(false);
		setVisible(false);
	}

	public void open(Frame parent) {
		setLocation(parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2,
				parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		pack();
		setVisible(true);
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

	// TODO el resto de métodos van aquí…
}