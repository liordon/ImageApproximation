package imageApproximation.dmonstrationUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class DemonstrationFrame extends JFrame {
    private ImageIcon image1;
    private final JLabel approxImageLabel = new JLabel(new ImageIcon());
    private Label topLabel;

    public DemonstrationFrame(String targetImagePath) {
        try {
            image1 = new ImageIcon(ImageIO.read(new File(targetImagePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        orderFrameLayout();
    }

    private void orderFrameLayout() {
        setTitle("Image Approximation Demo");
        final Panel verticalPanel = new Panel();
        add(verticalPanel);
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        topLabel = new Label("top level stats", Label.CENTER);
        verticalPanel.add(topLabel);

        final Panel imagePanel = new Panel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
        verticalPanel.add(imagePanel);

        JLabel targetImageLabel = new JLabel(image1);
        approxImageLabel.setIcon(new ImageIcon(new BufferedImage(image1.getIconWidth(), image1.getIconHeight(), BufferedImage.TYPE_INT_RGB)));
        imagePanel.add(approxImageLabel);
        imagePanel.add(new Label("<->"));
        imagePanel.add(targetImageLabel);
        verticalPanel.add(new Label("<-- best approximation VS target image -->", Label.CENTER));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    public DemonstrationFrame(Image targetImage) {
        image1 = new ImageIcon(targetImage);
        orderFrameLayout();
    }

    public static void main(String[] args) {
        DemonstrationFrame frame = new DemonstrationFrame(
                "src/main/resources/golden_bell-200x200.jpg");
        frame.setVisible(true);
    }

    public void updateApproximatedImage(BufferedImage imageIcon) {
        approxImageLabel.setIcon(new ImageIcon(imageIcon));
    }

    public void updateProgress(int numberOfIterations, double highestScore){
        topLabel.setText("iteration #" + numberOfIterations + " highest score: " + highestScore);
    }
}
