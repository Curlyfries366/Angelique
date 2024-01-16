import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;

public class Editor extends JFrame {
	private final JTabbedPane _tabs = new JTabbedPane();
	private final HashMap<Container, EditTab> _tabMap = new HashMap<>();
	public final boolean _debugMode = true;

public Editor() {
super("Bitmap/Wav Editor");
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setExtendedState(JFrame.MAXIMIZED_BOTH);
setUndecorated(false);
Toolkit tk = Toolkit.getDefaultToolkit();
setBounds(0, 0, tk.getScreenSize().width, tk.getScreenSize().height);
_tabs.addChangeListener(event -> {
_tabs.validate();
_tabs.repaint();
if (_tabMap.containsKey(_tabs.getSelectedComponent())) {
_tabMap.get(_tabs.getSelectedComponent()).draw();
}
});
JMenuBar menuBar = new JMenuBar();
JMenu fileMenu = new JMenu("File");
fileMenu.setMnemonic(KeyEvent.VK_F);
JMenuItem bmpMenuItem = new JMenuItem("Open BMP File");
bmpMenuItem.setMnemonic(KeyEvent.VK_B);
bmpMenuItem.addActionListener(event -> {
JFileChooser chooser = new JFileChooser();
chooser.setCurrentDirectory(new
File(System.getProperty("user.dir")));
chooser.setAcceptAllFileFilterUsed(true);
FileFilter filter = new FileFilter()
{
@Override
public boolean accept(File file)
{
return
file.getName().trim().toLowerCase().endsWith(".bmp");
}
@Override
public String getDescription()
{
return "Bitmap Image File";
}
};
chooser.addChoosableFileFilter(filter);
chooser.setFileFilter(filter);
int option = chooser.showOpenDialog(this);
if (option != JFileChooser.APPROVE_OPTION) {
return;
}
final File filePath = chooser.getSelectedFile();
Bitmap bmpTab = new Bitmap(this, filePath);
bmpTab.parse();
addTab(filePath, bmpTab);
});
fileMenu.add(bmpMenuItem);
JMenuItem wavMenuItem = new JMenuItem("Open WAV File");
wavMenuItem.setMnemonic(KeyEvent.VK_W);
wavMenuItem.addActionListener(event -> {
JFileChooser chooser = new JFileChooser();
chooser.setCurrentDirectory(new
File(System.getProperty("user.dir")));
chooser.setAcceptAllFileFilterUsed(true);
FileFilter filter = new FileFilter()
{

	public boolean accept(File file) {
		return file.getName().trim().toLowerCase().endsWith(".wav");
	}

	public String getDescription() {
		return "Wave Audio File";
	}};
	chooser.addChoosableFileFilter(filter);chooser.setFileFilter(filter);

	int option = chooser.showOpenDialog(this);if(option!=JFileChooser.APPROVE_OPTION)
	{
		return;
	}
	final File filePath = chooser.getSelectedFile();
	WaveRIFF wavTab = new WaveRIFF(this, filePath);wavTab.parse();

addTab(filePath, wavTab);
});fileMenu.add(wavMenuItem);

	JMenuItem exitMenuItem = new JMenuItem(
			"Close Program");exitMenuItem.setMnemonic(KeyEvent.VK_C);exitMenuItem.addActionListener(event->
	{
		System.exit(0);
	});fileMenu.add(exitMenuItem);menuBar.add(fileMenu);

setJMenuBar(menuBar);
add(_tabs);
setVisible(true);
requestFocus();
}

private void addTab(File file, EditTab tab) {
try
{
_tabs.addTab(file.getName(), tab.getRootPane());
_tabMap.put(tab.getRootPane(), tab);
_tabs.setSelectedIndex(_tabs.getTabCount() - 1);
validate();
repaint();
}
catch (Exception ex)
{
ex.printStackTrace();
}
}

public void closeTab(EditTab tab) {
if (_tabs.getTabCount() == 0)
{
return;
}
_tabs.remove(tab.getRootPane());
_tabMap.remove(tab.getRootPane());
}

public static void main(String[] args) {
new Editor();
}
}