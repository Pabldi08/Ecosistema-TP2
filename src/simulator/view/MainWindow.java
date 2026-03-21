package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {

	  private Controller ctrl;

	  public MainWindow(Controller ctrl) {  
	    super("[ECOSYSTEM SIMULATOR]");  
	    this.ctrl = ctrl;  
	    initGUI();  
	  }

	  private void initGUI() {  
	    JPanel mainPanel = new JPanel(new BorderLayout());  
	    setContentPane(mainPanel);
	    
	    ControlPanel controlPanel = new ControlPanel(ctrl);
	    mainPanel.add(controlPanel, BorderLayout.PAGE_START);
	    
	    StatusBar statusBar = new StatusBar(ctrl);
	    mainPanel.add(statusBar, BorderLayout.PAGE_END);

	    // Definición del panel de tablas (usa un BoxLayout vertical)  
	    JPanel contentPanel = new JPanel();  
	    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));  
	    mainPanel.add(contentPanel, BorderLayout.CENTER);

	    InfoTable speciesTable = new InfoTable("Species", new SpeciesTableModel(this.ctrl));
	    speciesTable.setPreferredSize(new Dimension(500, 250));
	    contentPanel.add(speciesTable);
	    InfoTable regionsTable = new InfoTable("Regions", new RegionsTableModel(this.ctrl));
	    regionsTable.setPreferredSize(new Dimension(500, 250));
	    contentPanel.add(regionsTable);

	    // TODO llama a ViewUtils.quit(MainWindow.this) en el método windowClosing
	    //addWindowListener(...);
	    
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); TODO: CAMBIAR PARA ENTREGA 
	    pack();  
	    setVisible(true);  
	   }  
	}