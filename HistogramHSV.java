import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class HistogramHSV {
    static { 
        System.load("C:\\Users\\carlo\\OneDrive\\Desktop\\faculdade\\zeluis\\grafica\\OpenCV2\\native\\opencv_java4120.dll");
    }

    // Converte RGB para HSV
    private static double[] rgbToHsv(double r, double g, double b) {
        r /= 255.0;
        g /= 255.0;
        b /= 255.0;
        
        double max = Math.max(r, Math.max(g, b));
        double min = Math.min(r, Math.min(g, b));
        double delta = max - min;
        
        double h = 0, s = 0, v = max;
        
        if (delta != 0) {
            s = delta / max;
            if (r == max) {
                h = (g - b) / delta;
            } else if (g == max) {
                h = 2 + (b - r) / delta;
            } else {
                h = 4 + (r - g) / delta;
            }
            h *= 60;
            if (h < 0) h += 360;
        }
        
        return new double[]{h, s, v};
    }

    // Converte HSV para RGB
    private static double[] hsvToRgb(double h, double s, double v) {
        double r = 0, g = 0, b = 0;
        
        if (s == 0) {
            r = g = b = v;
        } else {
            h /= 60;
            int i = (int)Math.floor(h);
            double f = h - i;
            double p = v * (1 - s);
            double q = v * (1 - s * f);
            double t = v * (1 - s * (1 - f));
            
            switch (i) {
                case 0: r = v; g = t; b = p; break;
                case 1: r = q; g = v; b = p; break;
                case 2: r = p; g = v; b = t; break;
                case 3: r = p; g = q; b = v; break;
                case 4: r = t; g = p; b = v; break;
                default: r = v; g = p; b = q; break;
            }
        }
        
        return new double[]{r * 255, g * 255, b * 255};
    }

    public static void main(String[] args) {
        String filename = "images\\histogram\\original1";
        String fileformat = ".png";

        Mat img = Imgcodecs.imread(filename + fileformat);
        Mat equalizedImg = new Mat(img.rows(), img.cols(), img.type());

        if (img.empty()){
            System.out.println("Imagem não carregada");
            return;
        }

        int[] nk = new int[256];
        int N = img.rows() * img.cols();

        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double[] bgr = img.get(i, j);
                double[] hsv = rgbToHsv(bgr[2], bgr[1], bgr[0]);
                int v = (int)(hsv[2] * 255); // Convertendo V para 0-255
            }
        }

        double[] pr = new double[256];
        for(int k = 0; k < 256; k++) {
            pr[k] = (double)nk[k] / N;
        }

        double[] sk = new double[256];
        sk[0] = pr[0];
        for(int k = 1; k < 256; k++) {
            sk[k] = sk[k-1] + pr[k];
        }

        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double[] bgr = img.get(i, j);
                double[] hsv = rgbToHsv(bgr[2], bgr[1], bgr[0]);
                
                int v = (int)(hsv[2] * 255);
                hsv[2] = sk[v]; 
                
                double[] rgb = hsvToRgb(hsv[0], hsv[1], hsv[2]);
                
                rgb[0] = Math.min(255, Math.max(0, rgb[0]));
                rgb[1] = Math.min(255, Math.max(0, rgb[1]));
                rgb[2] = Math.min(255, Math.max(0, rgb[2]));
                
                equalizedImg.put(i, j, new double[]{rgb[2], rgb[1], rgb[0]});
            }
        }

        Imgcodecs.imwrite(filename + "_equalized_hsv" + fileformat, equalizedImg);
        System.out.println("Equalização HSV concluída!");

        img.release();
        equalizedImg.release();
    }
}