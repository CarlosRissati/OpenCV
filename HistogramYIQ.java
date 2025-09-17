
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class HistogramYIQ {
    static { 
        System.load("C:\\Users\\carlo\\OneDrive\\Desktop\\faculdade\\zeluis\\grafica\\OpenCV2\\native\\opencv_java4120.dll");
    }

    private static double[] rgbToYiq(double r, double g, double b) {
        double[] yiq = new double[3];
        yiq[0] = 0.299 * r + 0.587 * g + 0.114 * b;  // Y
        yiq[1] = 0.596 * r - 0.274 * g - 0.322 * b;  // I
        yiq[2] = 0.211 * r - 0.523 * g + 0.312 * b;  // Q
        return yiq;
    }

    private static double[] yiqToRgb(double y, double i, double q) {
        double[] rgb = new double[3];
        rgb[0] = y + 0.956 * i + 0.621 * q;  // R
        rgb[1] = y - 0.272 * i - 0.647 * q;  // G
        rgb[2] = y - 1.105 * i + 1.702 * q;  // B
        return rgb;
    }

    private static int[] calcularHistograma(Mat img) {
        int[] histogram = new int[256];
        
        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double[] bgr = img.get(i, j);
                double[] yiq = rgbToYiq(bgr[2], bgr[1], bgr[0]); 
                int y = (int) Math.round(yiq[0]);
                histogram[y]++;
            }
        }
        return histogram;
    }

    private static int[] histogramaAcumulativo(int[] histogram) {
        int[] cumulativeHist = new int[256];
        cumulativeHist[0] = histogram[0];
        
        for(int i = 1; i < 256; i++) {
            cumulativeHist[i] = cumulativeHist[i-1] + histogram[i];
        }
        return cumulativeHist;
    }

    public static void main(String[] args) {
        String filename = "images\\histogram\\original2";
        String fileformat = ".png";

        Mat img = Imgcodecs.imread(filename + fileformat);
        Mat equalizedImg = new Mat(img.rows(), img.cols(), img.type());

        if (img.empty()){
            System.out.println("Imagem não carregada");
            return;
        }

        int[] histogram = calcularHistograma(img);
        int[] cumulativeHist = histogramaAcumulativo(histogram);
        double alpha = 255.0 / (img.rows() * img.cols());
        
        int[] lookupTable = new int[256];
        for(int i = 0; i < 256; i++) {
            lookupTable[i] = (int) Math.round(alpha * cumulativeHist[i]);
        }
        
        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double[] bgr = img.get(i, j);
                
                double[] yiq = rgbToYiq(bgr[2], bgr[1], bgr[0]);
                
                yiq[0] = lookupTable[(int)Math.round(yiq[0])];
                
                double[] rgb = yiqToRgb(yiq[0], yiq[1], yiq[2]);
                
                rgb[0] = Math.min(255, Math.max(0, rgb[0]));
                rgb[1] = Math.min(255, Math.max(0, rgb[1]));
                rgb[2] = Math.min(255, Math.max(0, rgb[2]));
                
                equalizedImg.put(i, j, new double[]{rgb[2], rgb[1], rgb[0]});
            }
        }

        Imgcodecs.imwrite(filename + "_equalized_yiq" + fileformat, equalizedImg);
    }
}