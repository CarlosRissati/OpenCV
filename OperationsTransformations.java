import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.nio.channels.Pipe;

public class OperationsTransformations extends JFrame{

    private JLabel imagLabel;
    private Mat originalImg;
    private Mat currentImg;

    static { 
        System.load("C:\\Users\\carlo\\OneDrive\\Desktop\\faculdade\\zeluis\\grafica\\OpenCV2\\native\\opencv_java4120.dll");
    }

    public OperationsTransformations(){
        initializeUi();
    }

    private void initializeUi(){
        setTitle("Exercicio de Transformação e Operações");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        imagLabel = new JLabel();
        imagLabel.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane scrollPane = new JScrollPane(imagLabel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = createControPanel();
        add(controlPanel, BorderLayout.SOUTH);

        setSize(800,600);
        setLocationRelativeTo(null);
    }

    private JPanel createControPanel(){
        JPanel panel = new JPanel(new FlowLayout());

        JButton loadButton = new JButton("Carregar Imagem");
        loadButton.addActionListener(e -> loadImage());
        panel.add(loadButton);

        JButton redButton = new JButton("Cinza(Vermelho)");
        redButton.addActionListener(e -> applyGreyScale('R'));
        panel.add(redButton);

        JButton greenButton = new JButton("Cinza(Verde)");
        greenButton.addActionListener(e -> applyGreyScale('G'));
        panel.add(greenButton);

        JButton blueButton = new JButton("Cinza(Azul)");
        blueButton.addActionListener(e -> applyGreyScale('B'));
        panel.add(blueButton);

        // Botão para rotacionar
        JButton rotateButton = new JButton("Rotacionar 45°");
        rotateButton.addActionListener(e -> rotateImage(45));
        panel.add(rotateButton);
        
        // Botão para transladar
        JButton translateButton = new JButton("Transladar");
        translateButton.addActionListener(e -> translateImage(50, 30));
        panel.add(translateButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetImage());
        panel.add(resetButton);

        return panel;
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imagens", "jpg", "jpeg", "png", "bmp", "tiff"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            originalImg = Imgcodecs.imread(imagePath);
            
            if (originalImg.empty()) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar a imagem!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            currentImg = originalImg.clone();
            displayImage(currentImg);
        }
    }

    private void displayImage(Mat image) {
        if (image == null || image.empty()) {
            return;
        }
        
        BufferedImage bufferedImage = matToBufferedImage(image);
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        imagLabel.setIcon(imageIcon);
        imagLabel.revalidate();
        imagLabel.repaint();
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        
        try {
            return javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(byteArray));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void applyGreyScale(char channel){
        if(currentImg == null || currentImg.empty()){
            showNoImageMessage();
            return;
        }

        Mat resultImg = new Mat(currentImg.size(), currentImg.type());
        resultImg = tonsDeCinza(currentImg, channel);
        displayImage(resultImg);
    }

    private void resetImage() {
        if (originalImg == null || originalImg.empty()) {
            showNoImageMessage();
            return;
        }
        
        currentImg = originalImg.clone();
        displayImage(currentImg);
    }

    private void showNoImageMessage() {
        JOptionPane.showMessageDialog(this, "Por favor, carregue uma imagem primeiro!", 
            "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new OperationsTransformations().setVisible(true);
        });
    }

    private void rotateImage(double angleDegrees) {
        if (currentImg == null || currentImg.empty()) {
            showNoImageMessage();
            return;
        }
        
        // Centro da imagem
        org.opencv.core.Point center = new org.opencv.core.Point(
            currentImg.cols() / 2.0, currentImg.rows() / 2.0);
        
        // Cria matriz de rotação
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(center, angleDegrees, 1.0);
        
        // Calcula o novo tamanho da imagem para acomodar a rotação
        double cos = Math.abs(rotationMatrix.get(0, 0)[0]);
        double sin = Math.abs(rotationMatrix.get(0, 1)[0]);
        
        int newWidth = (int) (currentImg.cols() * cos + currentImg.rows() * sin);
        int newHeight = (int) (currentImg.rows() * cos + currentImg.cols() * sin);
        
        // Ajusta a matriz de rotação para centralizar a imagem no novo canvas
        rotationMatrix.put(0, 2, rotationMatrix.get(0, 2)[0] + (newWidth - currentImg.cols()) / 2.0);
        rotationMatrix.put(1, 2, rotationMatrix.get(1, 2)[0] + (newHeight - currentImg.rows()) / 2.0);
        
        // Aplica a transformação
        Mat rotatedImage = new Mat();
        Imgproc.warpAffine(currentImg, rotatedImage, rotationMatrix, 
            new org.opencv.core.Size(newWidth, newHeight), 
            Imgproc.INTER_LINEAR, 
            org.opencv.core.Core.BORDER_CONSTANT, 
            new org.opencv.core.Scalar(0, 0, 0)); // Fundo preto
        
        currentImg = rotatedImage;
        displayImage(currentImg);
    }
    
    private void translateImage(double dx, double dy) {
        if (currentImg == null || currentImg.empty()) {
            showNoImageMessage();
            return;
        }
        
        // Cria matriz de translação
        Mat translationMatrix = new Mat(2, 3, org.opencv.core.CvType.CV_32FC1);
        translationMatrix.put(0, 0, 1, 0, dx);  // [1, 0, dx]
        translationMatrix.put(1, 0, 0, 1, dy);  // [0, 1, dy]
        
        // Calcula novo tamanho se necessário (para translações positivas)
        int newWidth = currentImg.cols() + (int) Math.max(0, dx);
        int newHeight = currentImg.rows() + (int) Math.max(0, dy);
        
        // Aplica a transformação
        Mat translatedImage = new Mat();
        Imgproc.warpAffine(currentImg, translatedImage, translationMatrix, 
            new org.opencv.core.Size(newWidth, newHeight),
            Imgproc.INTER_LINEAR,
            org.opencv.core.Core.BORDER_CONSTANT,
            new org.opencv.core.Scalar(128, 128, 128)); // Fundo cinza
        
        currentImg = translatedImage;
        displayImage(currentImg);
    }
    // public static void main(String[] args) {
    //     // try{
    //     //     Mat img = Imgcodecs.imread("images\\histogram\\original1.png");
    //     //     Mat img2 = Imgcodecs.imread("images\\histogram\\original2.png");
        
    //     //     if(img.empty()){
    //     //         System.out.println("Imagem não encontrada");
    //     //         return;
    //     //     }

    //         // JFrame GUI = HighGui.createJFrame("Jframe2", 0);
    //         // GUI = HighGui.imshow("Teste2", img2);
            
            
    //     //     while (true) {
    //     //         HighGui.imshow("Teste", img);
    //     //         int key = HighGui.waitKey(30);

    //     //         if(key == 27){ //ESC
    //     //             System.out.println("Esc pressionado");
    //     //             HighGui.destroyWindow("Teste");
    //     //             break;
    //     //         }else if(key == 'r' || key == 'R'){
    //     //             // System.out.println("Apertou a R");
    //     //             HighGui.imshow("Teste", img2);
    //     //         }else if(key == 'g' || key == 'G'){
    //     //             // System.out.println("Apertou a G");
    //     //             HighGui.imshow("Teste", tonsDeCinza(img, 'G'));
    //     //         }else if(key == 'b' || key == 'B'){
    //     //             // System.out.println("Apertou a R");
    //     //             HighGui.imshow("Teste", tonsDeCinza(img, 'B'));
    //     //         }else if(key == 'o' || key == 'O'){
    //     //             // System.out.println("Original");
    //     //             HighGui.imshow("Teste", img);
    //     //         }
    //     //     }
    //     //     System.out.println("sai do loop");
    //     //     HighGui.destroyAllWindows();
    //     //     // HighGui.waitKey(1);
            
            

    //     // } catch(Exception e){
    //     //     e.printStackTrace();
    //     // }
    // }

    public static Mat tonsDeCinza(Mat img, char channel){
        Mat resultImg = new Mat(img.size(), img.type());
        // System.out.println(img.get(10, 10)[0]);
        for(int i = 0; i < img.rows(); i++){
            for(int j = 0; j < img.cols(); j++){
                double cinza = 0;
                switch (Character.toUpperCase(channel)) {
                    case 'R':
                        cinza = (img.get(i, j)[2]);
                        break;
                    case 'G':
                        cinza = (img.get(i, j)[1]);
                        break;
                    case 'B':
                        cinza = (img.get(i, j)[0]);
                        break;
                }
                resultImg.put(i, j, new double[]{cinza, cinza, cinza});
            }
        }

        return resultImg;
    }

}