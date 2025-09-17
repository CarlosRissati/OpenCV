import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class HistogramYIQequalized {
 
    static { 
        System.load("C:\\Users\\carlo\\OneDrive\\Desktop\\faculdade\\zeluis\\grafica\\OpenCV2\\native\\opencv_java4120.dll");
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

        int[] nk = new int[256];
        int N = img.rows() * img.cols();

        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double[] bgr = img.get(i, j);
                double y = 0.299 * bgr[2] + 0.587 * bgr[1] + 0.114 * bgr[0];
                int k = (int)Math.round(y);
                nk[k]++;
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
                
                double y = 0.299 * bgr[2] + 0.587 * bgr[1] + 0.114 * bgr[0];
                double i_comp = 0.596 * bgr[2] - 0.274 * bgr[1] - 0.322 * bgr[0];
                double q = 0.211 * bgr[2] - 0.523 * bgr[1] + 0.312 * bgr[0];
                
                int k = (int)Math.round(y);
                y = sk[k] * 255; 
                
                double r = y + 0.956 * i_comp + 0.621 * q;
                double g = y - 0.272 * i_comp - 0.647 * q;
                double b = y - 1.105 * i_comp + 1.702 * q;
                
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));
                
                equalizedImg.put(i, j, new double[]{b, g, r});
            }
        }

        Imgcodecs.imwrite(filename + "_equalized_yiq" + fileformat, equalizedImg);
        System.out.println("Equalização concluída!");

        img.release();
        equalizedImg.release();
    }
}