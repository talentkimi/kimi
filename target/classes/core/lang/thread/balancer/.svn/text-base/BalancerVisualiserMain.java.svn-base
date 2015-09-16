package core.lang.thread.balancer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import core.lang.thread.ThreadManager;

/**
 * Used for debugging only. This visualiser can be started by specifying system property via startup parameters "-DshowBalancerVisualiser=true".
 * @author Dimitrijs
 *
 */
public class BalancerVisualiserMain extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Random random = new Random();
	private Balancer currentBalancer = ThreadManager.SEARCH_BALANCER;

	private static class Row {
		
		InfoHolder data;
		
		Column[] columns = new Column[] {
				new Column("Color") { Object getValue() { return data.color; } }, 
				new Column("Name") { Object getValue() { return data.info.name; } }, 
				new Column("Active") { Object getValue() { return data.info.activeCount; } }, 
				new Column("Limit") { Object getValue() { return data.info.limit; } }, 
				new Column("Weight") { Object getValue() { return data.info.weight; } }, 
				new Column("Pop Factor") { Object getValue() { return String.format("%.2f", data.info.popularityFactor); } }, 
				new Column("Speed Factor") { Object getValue() { return String.format("%.2f", data.info.speedFactor); } }, 
				new Column("Finished count") { Object getValue() { return data.info.finishedCount; } }, 
				new Column("Rejected count") { Object getValue() { return data.info.rejectedCount; } } 
		};
		
		private abstract class Column {
			String name;
			Column(String name) {
				this.name = name;
			}
			abstract Object getValue();
			String getName() {
				return name;
			}
		}
	}
	
	private final Map<String, TableData> balancers = new HashMap<String, TableData>();
	
	private final Row row = new Row();
	
	private static class TableData {
		private final Map<String, InfoHolder> categoryMap = new HashMap<String, InfoHolder>();
		private final List<InfoHolder> categories = new ArrayList<InfoHolder>();
	}
	
	private final Timer timer;
	
	private Balancer.Info lastInfo;
	private TableData lastTableData;
	
	public BalancerVisualiserMain() {
		super("Balancer Visualiser");
		createMenu();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		setLayout(new BorderLayout());
		final CategoryTableModel tableModel = new CategoryTableModel();
		final JTable table = new JTable(tableModel);
		table.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
			private JComponent label = new JComponent() {
				private static final long serialVersionUID = 1L;
				private Dimension d = new Dimension();
				@Override
				protected void paintComponent(Graphics g) {
					g.setColor(getBackground());
					getSize(d);
					g.fillRect(0, 0, d.width, d.height);
				}
 				
			};
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Color c = (Color) value;
				label.setBackground(c);
				return label;
			}
		});
		final JScrollPane tablePane = new JScrollPane(table);
		add(tablePane, BorderLayout.CENTER);
		
		final JPanel status = new JPanel(new BorderLayout());
		final AllCategories catView = new AllCategories();
		final JLabel totalSize = new JLabel(" ");
		totalSize.setHorizontalAlignment(SwingConstants.CENTER);
		status.add(totalSize, BorderLayout.NORTH);
		status.add(catView, BorderLayout.CENTER);
		add(status, BorderLayout.SOUTH);

		timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
				tableModel.fireTableDataChanged();
				totalSize.setText(String.format("threads: %d / %d; categories %d / %d; balancing enabled: %b", lastInfo.size, lastInfo.limit, lastInfo.activeCategories, lastInfo.categories.size(), lastInfo.enabled));
				catView.repaint();
			}
		});
		timer.start();
		
		pack();
	}
	
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem changeBalancer = new JMenuItem("Show another balancer...");
		file.add(changeBalancer);
		menuBar.add(file);
		setJMenuBar(menuBar);
		
		changeBalancer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Balancer> all = Balancer.getAllBalancers();
				Balancer[] options = all.toArray(new Balancer[all.size()]);
				if (!all.contains(currentBalancer)) {
					currentBalancer = options[0];
				}
				Balancer result = (Balancer) JOptionPane.showInputDialog(
						BalancerVisualiserMain.this, 
						"Select balancer", 
						"Change balancer",
						JOptionPane.QUESTION_MESSAGE, 
						null,
						options,
						currentBalancer);
				if (result != null) {
					currentBalancer = result;
				}
			}
		});
	}
	
	private void update() {
		lastInfo = currentBalancer.getSnapshot();
		lastTableData = balancers.get(lastInfo.name);
		if (lastTableData == null) {
			lastTableData = new TableData();
			balancers.put(lastInfo.name, lastTableData);
		}
		for (BalancedCategory.Info ci : lastInfo.categories) {
			InfoHolder h = lastTableData.categoryMap.get(ci.name);
			if (h == null) {
				h = new InfoHolder();
				lastTableData.categoryMap.put(ci.name, h);
				lastTableData.categories.add(h);
			}
			h.info = ci; 
		}
	}
	
	public static void start() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new BalancerVisualiserMain().setVisible(true);
			}
		});
	}
	
	private class CategoryTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount() {
			return row.columns.length;
		}

		@Override
		public int getRowCount() {
			if (lastTableData == null) {
				update();
			}
			return lastTableData.categories.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			row.data = lastTableData.categories.get(rowIndex);
			return row.columns[columnIndex].getValue();
		}

		@Override
		public String getColumnName(int column) {
			return row.columns[column].getName();
		}
	}

	public class AllCategories extends JComponent {
		private static final long serialVersionUID = 1L;
		private Dimension dim = new Dimension();
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(Color.WHITE);
			getSize(dim);
			g.fillRect(0, 0, dim.width, dim.height);
			if (lastInfo == null) {
				return;
			}
			
			int len = Math.max(lastInfo.size, lastInfo.limit);
			int x = 0;
			for (int i = 0; i < lastTableData.categories.size(); i++) {
				Color c = lastTableData.categories.get(i).color;
				g.setColor(c);
				int w = dim.width * lastTableData.categories.get(i).info.activeCount / len;
				g.fillRect(x, 0, w, dim.height);
				x += w;
			}
			
		}
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(16, 16);
		}
	}
	
	public static class InfoHolder {
		BalancedCategory.Info info;
		Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));;
	}

}
