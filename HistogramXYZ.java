import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class HistogramXYZ {
    static { 
        System.load("C:\\Users\\carlo\\OneDrive\\Desktop\\faculdade\\zeluis\\grafica\\OpenCV2\\native\\opencv_java4120.dll");
    }

    private static double[] rgbToXyz(double r, double g, double b) {
        // Gamma correction
        r = (r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92;
        g = (g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92;
        b = (b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92;

        // Scale to 0-1
        r /= 255.0;
        g /= 255.0;
        b /= 255.0;

        // Convert to XYZ using D65 illuminant
        double x = r * 0.4124564 + g * 0.3575761 + b * 0.1804375;
        double y = r * 0.2126729 + g * 0.7151522 + b * 0.0721750;
        double z = r * 0.0193339 + g * 0.1191920 + b * 0.9503041;

        return new double[]{x, y, z};
    }

    private static double[] xyzToRgb(double x, double y, double z) {
        // Convert XYZ to RGB
        double r = x * 3.2404542 - y * 1.5371385 - z * 0.4985314;
        double g = -x * 0.9692660 + y * 1.8760108 + z * 0.0415560;
        double b = x * 0.0556434 - y * 0.2040259 + z * 1.0572252;

        // Gamma correction
        r = (r > 0.0031308) ? 1.055 * Math.pow(r, 1/2.4) - 0.055 : 12.92 * r;
        g = (g > 0.0031308) ? 1.055 * Math.pow(g, 1/2.4) - 0.055 : 12.92 * g;
        b = (b > 0.0031308) ? 1.055 * Math.pow(b, 1/2.4) - 0.055 : 12.92 * b;

        // Scale and clip
        r = Math.min(255, Math.max(0, r * 255));
        g = Math.min(255, Math.max(0, g * 255));
        b = Math.min(255, Math.max(0, b * 255));

        return new double[]{r, g, b};
    }

    public static void main(String[] args) {
        String filename = "images\\histogram\\original2";
        String fileformat = ".png";

        Mat img = Imgcodecs.imread(filename + fileformat);
        Mat equalizedImg = new Mat(img.rows(), img.cols(), img.type());

        if (img.empty()) {
            System.out.println("Imagem não carregada");
            return;
        }

        // Etapa 1: Calcular histograma do Y
        double[] histograma = new double[256];
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        // Primeiro passo: encontrar min/max Y e construir histograma
        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double[] bgr = img.get(i, j);
                double[] xyz = rgbToXyz(bgr[2], bgr[1], bgr[0]);
                
                // Normalizar Y para 0-255
                int yIndex = (int)(xyz[1] * 255);
                yIndex = Math.min(255, Math.max(0, yIndex));
                histograma[yIndex]++;
                
                minY = Math.min(minY, xyz[1]);
                maxY = Math.max(maxY, xyz[1]);
            }
        }

        // Etapa 2: Calcular CDF
        double[] cdf = new double[256];
        cdf[0] = histograma[0];
        for(int i = 1; i < 256; i++) {
            cdf[i] = cdf[i-1] + histograma[i];
        }

        // Normalizar CDF
        double cdfMin = cdf[0];
        double cdfMax = cdf[255];
        for(int i = 0; i < 256; i++) {
            cdf[i] = (cdf[i] - cdfMin) / (cdfMax - cdfMin);
        }

        // Etapa 3: Aplicar equalização
        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double[] bgr = img.get(i, j);
                double[] xyz = rgbToXyz(bgr[2], bgr[1], bgr[0]);
                
                // Aplicar equalização apenas no Y
                int yIndex = (int)(xyz[1] * 255);
                yIndex = Math.min(255, Math.max(0, yIndex));
                
                // Preservar a proporção relativa do Y original
                double yNorm = (xyz[1] - minY) / (maxY - minY);
                xyz[1] = minY + (maxY - minY) * cdf[yIndex] * yNorm;
                
                // Converter de volta para RGB
                double[] rgb = xyzToRgb(xyz[0], xyz[1], xyz[2]);
                
                equalizedImg.put(i, j, new double[]{rgb[2], rgb[1], rgb[0]});
            }
        }

        Imgcodecs.imwrite(filename + "_equalized_xyz_v2" + fileformat, equalizedImg);
        System.out.println("Equalização XYZ concluída!");

        img.release();
        equalizedImg.release();
    }
}