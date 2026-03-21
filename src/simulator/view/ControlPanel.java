package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;

class ControlPanel extends JPanel {

	final static int minSteps = 1;
	final static int maxSteps = 10000;
	final static int stepAdvance = 100;

	private Controller ctrl;
	private ChangeRegionsDialog changeRegionsDialog;

	private JToolBar toolaBar;
	private JFileChooser fc;
	private boolean stopped = true; // utilizado en los botones de run/stop
	private JButton quitButton;
	private JButton folderButton;
	private JButton regionButton;
	private JButton changeRegionButton;
	private JButton playButton;
	private JButton stopButton;
	private JSpinner stepsCounter;
	private JTextField deltaTimeInput;

	ControlPanel(Controller ctrl) {
		this.ctrl = ctrl;
		this.changeRegionsDialog = new ChangeRegionsDialog(ctrl);
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		toolaBar = new JToolBar();
		add(toolaBar, BorderLayout.PAGE_START);

		this.fc = new JFileChooser();
		File dir = new File(System.getProperty("user.dir") + "/resources/examples");
		this.fc.setCurrentDirectory(dir);

		// Open button
		this.folderButton = new JButton();
		this.folderButton.setToolTipText("Load an input file into the simulator");
		this.folderButton.setIcon(new ImageIcon("resources/icons/open.png"));
		this.folderButton.addActionListener((e) -> this.fc.showOpenDialog(ViewUtils.getWindow(this)));
		this.toolaBar.add(folderButton);

		this.toolaBar.addSeparator();

		// Map button
		this.regionButton = new JButton();
		this.regionButton.setToolTipText("Map Viewer");
		this.regionButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		this.regionButton.addActionListener((e) -> new MapWindow(ViewUtils.getWindow(this), ctrl));
		this.toolaBar.add(regionButton);

		// Regions button
		this.changeRegionButton = new JButton();
		this.changeRegionButton.setToolTipText("Change Regions");
		this.changeRegionButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		this.changeRegionButton.addActionListener((e) -> this.changeRegionsDialog.open(ViewUtils.getWindow(this)));
		this.toolaBar.add(changeRegionButton);

		this.toolaBar.addSeparator();

		// Run button
		this.playButton = new JButton();
		this.playButton.setToolTipText("Run the simulator");
		this.playButton.setIcon(new ImageIcon("resources/icons/run.png"));
		this.playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				double dt;
				int n;
				try {
					buttonsState(false);
					dt = Double.parseDouble(deltaTimeInput.getText());
				    n = ((Number) stepsCounter.getValue()).intValue();
				    runSim(n, dt);
				} catch (Exception ex) {
					ViewUtils.showErrorMsg(ControlPanel.this, "El valor de delta time no es un número válido");
				}
				
			}
		});
		this.toolaBar.add(playButton);

		// Stop button
		this.stopButton = new JButton();
		this.stopButton.setToolTipText("Stop the simulator");
		this.stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		this.stopButton.addActionListener(e -> buttonsState(true));
		this.toolaBar.add(stopButton);

		// Steps label and spinner
		this.toolaBar.add(new JLabel("Steps: "));
		this.stepsCounter = new JSpinner();
		this.stepsCounter.setModel(new SpinnerNumberModel(maxSteps, minSteps, maxSteps, stepAdvance));
		this.stepsCounter.setToolTipText("Simulation steps to run: " + minSteps + "-" + maxSteps);
		// TODO: tiene que ser editable por el usuario o solo con las flechas?
		/*
		 * JSpinner.NumberEditor editor = new JSpinner.NumberEditor(this.stepsCounter);
		 * this.stepsCounter.setEditor(editor);
		 * editor.getTextField().setEditable(false);
		 */
		this.toolaBar.add(stepsCounter);

		// Delta-Time label and textField
		this.toolaBar.add(new JLabel("Delta-Time: "));
		this.deltaTimeInput = new JTextField(6);
		this.deltaTimeInput.setToolTipText("Real time (seconds) corresponding to a step");
		this.deltaTimeInput.setText("0.03");
		this.toolaBar.add(deltaTimeInput);

		// TODO crear los diferentes botones/atributos y añadirlos a la toolBar.
		// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
		// this.toolaBar.addSeparator() para añadir la línea de separación vertical
		// entre las componentes que lo necesiten.

		// Quit Button
		this.toolaBar.add(Box.createGlue()); // this aligns the button to the right
		this.toolaBar.addSeparator();
		this.quitButton = new JButton();
		this.quitButton.setToolTipText("Quit");
		this.quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		this.quitButton.addActionListener((e) -> ViewUtils.quit(this));
		this.toolaBar.add(quitButton);

		// TODO Inicializar this.fc con una instancia de JFileChooser. Para que siempre
		// abre en la carpeta de ejemplos puedes usar:
		//
		// this.fc.setCurrentDirectory(new File(System.getProperty("user.dir") +
		// "/resources/examples"));

		// TODO Inicializar this.changeRegionsDialog con instancias del diálogo de
		// cambio
		// de regiones

	}

	private void runSim(int n, double dt) {
		if (n > 0 && !this.stopped) {
			try {
				this.ctrl.advance(dt);
				SwingUtilities.invokeLater(() -> runSim(n - 1, dt));
			} catch (Exception e) {
				ViewUtils.showErrorMsg(ControlPanel.this, e.getMessage());
				buttonsState(true);
				this.stopped = true;
			}
		} else {
			buttonsState(true);
			this.stopped = true;
		}
	}

	private void buttonsState(boolean state) {
		stopped = state;
		changeRegionButton.setEnabled(state);
		regionButton.setEnabled(state);
		folderButton.setEnabled(state);
		quitButton.setEnabled(state);
		stepsCounter.setEnabled(state);
		deltaTimeInput.setEnabled(state);
		playButton.setEnabled(state);
	}
	// TODO el resto de métodos van aquí…
}