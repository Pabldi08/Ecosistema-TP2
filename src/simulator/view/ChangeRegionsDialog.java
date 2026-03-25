package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
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

	private JComboBox regionSelector;
	private JComboBox rowFrom;
	private JComboBox rowTo;
	private JComboBox colFrom;
	private JComboBox colTo;
	
	private int status;

	private DefaultTableModel dataTableModel;
	private Controller ctrl;
	private List<JSONObject> regionsInfo;

	private String[] headers = { "Key", "Value", "Description" };

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
		mainPanel.add(topPanel());

		// Panel central
		mainPanel.add(centerPanel());
		
		// Panel inferior
		mainPanel.add(lowerPanel());
		
		updateTable();

		// Panel botones
		mainPanel.add(bottomPanel());

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
		fillComboBox(map);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		fillComboBox(map);
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {}

	@Override
	public void onAdvance(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
	}

	private void fillComboBox(MapInfo map) {
		this.fromRowModel.removeAllElements();
		this.toRowModel.removeAllElements();
		this.fromColModel.removeAllElements();
		this.toColModel.removeAllElements();
		for (int i = 0; i < map.getRows(); i++) {
			this.fromRowModel.addElement("" + i);
			this.toRowModel.addElement("" + i);
		}
		for (int i = 0; i < map.getCols(); i++) {
			this.fromColModel.addElement("" + i);
			this.toColModel.addElement("" + i);
		}
	}
	
	private void updateTable() {
		int seleccionado = this.regionSelector.getSelectedIndex();
		if(seleccionado != -1) {
			JSONObject data = this.regionsInfo.get(seleccionado).getJSONObject("data");
			int dataSize = data.keySet().size(), n = 0;
			this.dataTableModel.setRowCount(dataSize);
			for(String s: data.keySet()) {
				this.dataTableModel.setValueAt(s, n, 0);
				this.dataTableModel.setValueAt(data.getString(s), n, 2);
				n++;
			}
		}
	}

	private JPanel topPanel() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		JTextArea help = new JTextArea(
				"Select a region type, the rows/cols interval, and provide values for the parameters in the Value column (default values "
						+ "are used for parameters with no value).");
		help.setLineWrap(true);
		help.setWrapStyleWord(true); // Para que no corte las palabras por la mitad
		help.setOpaque(false); // Para que no se vea el cuadro de texto
		help.setFocusable(false);
		help.setFont(UIManager.getFont("Label.font"));
		topPanel.add(help);
		return topPanel;
	}
	
	private JPanel centerPanel() {
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
		JTable table = new JTable(dataTableModel);
		centerPanel.add(new JScrollPane(table));
		this.dataTableModel.setColumnIdentifiers(this.headers);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(125);
		table.getColumnModel().getColumn(2).setPreferredWidth(408);
		return centerPanel;
	}

	private JPanel lowerPanel() {
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new FlowLayout());
		JLabel regionType = new JLabel("Region type: ");
		lowerPanel.add(regionType);
		this.regionsModel = new DefaultComboBoxModel<>();
		for (JSONObject o : this.regionsInfo) {
			this.regionsModel.addElement(o.getString("type"));
		}
		this.regionSelector = new JComboBox(regionsModel);
		lowerPanel.add(regionSelector);
		this.regionSelector.addActionListener(e -> updateTable());

		JLabel rows = new JLabel("Row from/to: ");
		lowerPanel.add(rows);
		this.fromRowModel = new DefaultComboBoxModel<>();
		this.rowFrom = new JComboBox(fromRowModel);
		this.toRowModel = new DefaultComboBoxModel<>();
		this.rowTo = new JComboBox(toRowModel);
		lowerPanel.add(rowFrom);
		lowerPanel.add(rowTo);

		JLabel cols = new JLabel("Column from/to: ");
		lowerPanel.add(cols);
		this.fromColModel = new DefaultComboBoxModel<>();
		this.colFrom = new JComboBox(fromColModel);
		this.toColModel = new DefaultComboBoxModel<>();
		this.colTo = new JComboBox(toColModel);
		lowerPanel.add(colFrom);
		lowerPanel.add(colTo);

		return lowerPanel;
	}
	
	private JPanel bottomPanel() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				setVisible(false);
			}
		});
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(e -> okAction());
		bottomPanel.add(cancelButton);
		bottomPanel.add(okButton);
		return bottomPanel;
	}
	
	private void okAction() {
		status = 1;
		JSONObject regions = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject info = new JSONObject();
		JSONArray row = new JSONArray();
		row.put(this.rowFrom.getSelectedItem());
		row.put(this.rowTo.getSelectedItem());
		JSONArray col = new JSONArray();
		col.put(this.colFrom.getSelectedItem());
		col.put(this.colTo.getSelectedItem());
		info.put("col", col);
		info.put("row", row);
		list.put(info);
		regions.put("regions", list);
		
		JSONObject spec = new JSONObject();
		spec.put("type", this.regionSelector.getSelectedItem());
		JSONObject data = new JSONObject();
		
		for(int i = 0; i < this.dataTableModel.getRowCount();i++) {
			if(this.dataTableModel.getValueAt(i, 1) != null) {
				try {
					String key = (String)this.dataTableModel.getValueAt(i, 0);
					int value = Integer.parseInt((String) this.dataTableModel.getValueAt(i, 1));
					data.put(key, value);
				} catch (Exception ex) {
					ViewUtils.showErrorMsg(ChangeRegionsDialog.this, "Invalid values.");
					this.status = 0;
				}
			}
		}
		
		if(status == 1) {
			spec.put("data", data);
			info.put("spec", spec);
			this.ctrl.setRegions(regions);
			this.setVisible(false);
		}
	}
}