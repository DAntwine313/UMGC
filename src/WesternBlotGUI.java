import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextField;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class WesternBlotGUI extends JFrame implements ActionListener
{
    private JPanel panelBottom;
    private JButton buttonGray, buttonEdgeDetector, buttonInvert, buttonBrightnessContrast, buttonReset;
    private JTextField textFieldImagePath;
    private MarvinImagePanel imagePanel;
    private MarvinImage image, backupImage;
    private MarvinImagePlugin imagePlugin;


    public WesternBlotGUI()
    {
        super("UMGC Western Blot Editor");

        // Create Graphical Interface
        buttonGray = new JButton("Gray");
        buttonGray.addActionListener(this);
        buttonEdgeDetector = new JButton("EdgeDetector");
        buttonEdgeDetector.addActionListener(this);
        buttonInvert = new JButton("Invert");
        buttonInvert.addActionListener(this);
        buttonBrightnessContrast = new JButton("Bright/Contrast");
        buttonInvert.addActionListener(this);
        buttonReset = new JButton("Reset");
        buttonReset.addActionListener(this);

        panelBottom = new JPanel();
        panelBottom.add(buttonGray);
        panelBottom.add(buttonEdgeDetector);
        panelBottom.add(buttonInvert);
        panelBottom.add(buttonBrightnessContrast);
        panelBottom.add(buttonReset);

        // ImagePanel
        imagePanel = new MarvinImagePanel();

        Container l_c = getContentPane();
        l_c.setLayout(new BorderLayout());
        l_c.add(panelBottom, BorderLayout.SOUTH);
        l_c.add(imagePanel, BorderLayout.NORTH);

        // Load image
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & GIF Images", "jpg", "gif");
        chooser.setFileFilter(filter);
        textFieldImagePath = new JTextField(100);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            textFieldImagePath.setText(chooser.getSelectedFile().getAbsolutePath());
        }
        image = MarvinImageIO.loadImage(textFieldImagePath.getText());
        backupImage = image.clone();
        imagePanel.setImage(image);
        setSize(340,430);
        setVisible(true);
    }

    public static void main(String args[]){
        WesternBlotGUI t = new WesternBlotGUI();
        t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e){
        image = backupImage.clone();
        if(e.getSource() == buttonGray){
            imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale.jar");
            imagePlugin.process(image, image);
        }
        else if(e.getSource() == buttonEdgeDetector){
            imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.edge.edgeDetector.jar");
            imagePlugin.process(image, image);
        }
        else if(e.getSource() == buttonInvert){
            imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.invert.jar");
            imagePlugin.process(image, image);
        }
        else if(e.getSource() == buttonBrightnessContrast){
            imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.brightnessAndContrast.jar");
            imagePlugin.setAttribute("contrast", 50);
            imagePlugin.process(image, image);
        }
        image.update();
        imagePanel.setImage(image);
    }
}