import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class HistogramEqualizationXYZ {
    static { 
        System.load("C:\\Users\\carlo\\OneDrive\\Desktop\\faculdade\\zeluis\\grafica\\OpenCV2\\native\\opencv_java4120.dll");
    }

    public static void main(String[] args) {
        String filename = "images\\histogram\\original1";
        String fileformat = ".png";

        Mat img = Imgcodecs.imread(filename + fileformat);

        if (img.empty()){
            System.out.println("Imagem não carregada");
            return;
        } else {
            System.out.println("Imagem carregada -> " + img.size());
        }

        // Equalização usando YIQ
        System.out.println("\n=== EQUALIZAÇÃO YIQ ===");
        Mat imgEqualizedYIQ = equalizeHistogramYIQ(img);
        Imgcodecs.imwrite(filename + "_equalized_yiq" + fileformat, imgEqualizedYIQ);
        
        // Equalização usando HSV
        System.out.println("\n=== EQUALIZAÇÃO HSV ===");
        Mat imgEqualizedHSV = equalizeHistogramHSV(img);
        Imgcodecs.imwrite(filename + "_equalized_hsv" + fileformat, imgEqualizedHSV);
        
        System.out.println("\nImagens equalizadas salvas!");
    }

    /**
     * Equalização de histograma no espaço YIQ
     * Aplica equalização apenas no canal Y (luminância)
     */
    public static Mat equalizeHistogramYIQ(Mat img) {
        Mat result = img.clone();
        int N = img.rows() * img.cols(); // Número total de pixels
        int L = 256; // Níveis de cinza (0-255)
        
        // Mapa para contar frequência dos níveis Y
        Map<Integer, Integer> histogram = new HashMap<>();
        
        // Primeira passagem: converter BGR para YIQ e construir histograma do canal Y
        double[][][] yiqPixels = new double[img.rows()][img.cols()][3];
        
        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double[] bgr = img.get(i, j);
                double b = bgr[0] / 255.0;
                double g = bgr[1] / 255.0;
                double r = bgr[2] / 255.0;
                
                // Conversão BGR para YIQ
                double y = 0.299 * r + 0.587 * g + 0.114 * b;
                double i_yiq = 0.596 * r - 0.274 * g - 0.322 * b;
                double q = 0.211 * r - 0.523 * g + 0.312 * b;
                
                yiqPixels[i][j][0] = y;
                yiqPixels[i][j][1] = i_yiq;
                yiqPixels[i][j][2] = q;
                
                // Quantizar Y para construir histograma (0-255)
                int yLevel = (int) Math.round(y * 255);
                yLevel = Math.max(0, Math.min(255, yLevel));
                
                histogram.put(yLevel, histogram.getOrDefault(yLevel, 0) + 1);
            }
        }
        
        // Calcular função de transformação usando as fórmulas das imagens
        Map<Integer, Integer> transformFunction = new TreeMap<>();
        
        for(int k = 0; k <= 255; k++) {
            int nk = histogram.getOrDefault(k, 0); // Número de pixels com nível k
            double prk = (double) nk / N; // P(rk) = nk/N
            
            // Sk = T(rk) = Σ(i=0 to k) ni/N
            double sk = 0;
            for(int i = 0; i <= k; i++) {
                int ni = histogram.getOrDefault(i, 0);
                sk += (double) ni / N;
            }
            
            // Novo nível equalizado
            int newLevel = (int) Math.round(sk * (L - 1));
            newLevel = Math.max(0, Math.min(255, newLevel));
            transformFunction.put(k, newLevel);
            
            if(nk > 0) {
                System.out.printf("k=%d, nk=%d, P(rk)=%.4f, Sk=%.4f, novo_nivel=%d%n", 
                                k, nk, prk, sk, newLevel);
            }
        }
        
        // Segunda passagem: aplicar equalização e converter de volta para BGR
        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double y = yiqPixels[i][j][0];
                double i_yiq = yiqPixels[i][j][1];
                double q = yiqPixels[i][j][2];
                
                // Equalizar apenas o canal Y
                int yLevel = (int) Math.round(y * 255);
                yLevel = Math.max(0, Math.min(255, yLevel));
                int newYLevel = transformFunction.get(yLevel);
                double newY = newYLevel / 255.0;
                
                // Converter YIQ equalizado de volta para RGB
                double r = newY + 0.956 * i_yiq + 0.621 * q;
                double g = newY - 0.272 * i_yiq - 0.647 * q;
                double b = newY - 1.106 * i_yiq + 1.703 * q;
                
                // Garantir que os valores estão no range [0,1] e converter para [0,255]
                r = Math.max(0, Math.min(1, r)) * 255;
                g = Math.max(0, Math.min(1, g)) * 255;
                b = Math.max(0, Math.min(1, b)) * 255;
                
                result.put(i, j, new double[]{b, g, r}); // BGR format
            }
        }
        
        return result;
    }

    /**
     * Equalização de histograma no espaço XYZ
     * Aplica equalização apenas no canal Y (luminância)
     */
    public static Mat equalizeHistogramXYZ(Mat img) {
        Mat result = img.clone();
        int N = img.rows() * img.cols(); // Número total de pixels
        int L = 256; // Níveis de cinza (0-255)
        
        // Mapa para contar frequência dos níveis Y
        Map<Integer, Integer> histogram = new HashMap<>();
        
        // Primeira passagem: converter BGR para XYZ e construir histograma do canal Y
        double[][][] xyzPixels = new double[img.rows()][img.cols()][3];
        
        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double[] bgr = img.get(i, j);
                double b = bgr[0] / 255.0;
                double g = bgr[1] / 255.0;
                double r = bgr[2] / 255.0;
                
                // Aplicar correção gamma (linearização)
                r = gammaCorrection(r);
                g = gammaCorrection(g);
                b = gammaCorrection(b);
                
                // Conversão RGB linear para XYZ (matriz sRGB para XYZ D65)
                double x = 0.4124564 * r + 0.3575761 * g + 0.1804375 * b;
                double y = 0.2126729 * r + 0.7151522 * g + 0.0721750 * b;
                double z = 0.0193339 * r + 0.1191920 * g + 0.9503041 * b;
                
                xyzPixels[i][j][0] = x;
                xyzPixels[i][j][1] = y;
                xyzPixels[i][j][2] = z;
                
                // Quantizar Y para construir histograma (0-255)
                // Normalizar Y pelo valor máximo teórico (aproximadamente 1.0)
                int yLevel = (int) Math.round(y * 255);
                yLevel = Math.max(0, Math.min(255, yLevel));
                
                histogram.put(yLevel, histogram.getOrDefault(yLevel, 0) + 1);
            }
        }
        
        // Calcular função de transformação usando as fórmulas das imagens
        Map<Integer, Integer> transformFunction = new TreeMap<>();
        
        for(int k = 0; k <= 255; k++) {
            int nk = histogram.getOrDefault(k, 0); // Número de pixels com nível k
            double prk = (double) nk / N; // P(rk) = nk/N
            
            // Sk = T(rk) = Σ(i=0 to k) ni/N
            double sk = 0;
            for(int i = 0; i <= k; i++) {
                int ni = histogram.getOrDefault(i, 0);
                sk += (double) ni / N;
            }
            
            // Novo nível equalizado
            int newLevel = (int) Math.round(sk * (L - 1));
            newLevel = Math.max(0, Math.min(255, newLevel));
            transformFunction.put(k, newLevel);
            
            if(nk > 0) {
                System.out.printf("k=%d, nk=%d, P(rk)=%.4f, Sk=%.4f, novo_nivel=%d%n", 
                                k, nk, prk, sk, newLevel);
            }
        }
        
        // Segunda passagem: aplicar equalização e converter de volta para BGR
        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double x = xyzPixels[i][j][0];
                double y = xyzPixels[i][j][1];
                double z = xyzPixels[i][j][2];
                
                // Equalizar apenas o canal Y
                int yLevel = (int) Math.round(y * 255);
                yLevel = Math.max(0, Math.min(255, yLevel));
                int newYLevel = transformFunction.get(yLevel);
                double newY = newYLevel / 255.0;
                
                // Manter proporção de X e Z em relação ao Y original
                // Isso preserva a cromaticidade
                double ratio = (y > 0) ? newY / y : 1.0;
                double newX = x * ratio;
                double newZ = z * ratio;
                
                // Converter XYZ equalizado de volta para RGB linear
                double r = 3.2404542 * newX - 1.5371385 * newY - 0.4985314 * newZ;
                double g = -0.9692660 * newX + 1.8760108 * newY + 0.0415560 * newZ;
                double b = 0.0556434 * newX - 0.2040259 * newY + 1.0572252 * newZ;
                
                // Aplicar correção gamma inversa (para voltar ao espaço sRGB)
                r = inverseGammaCorrection(r);
                g = inverseGammaCorrection(g);
                b = inverseGammaCorrection(b);
                
                // Converter para [0,255] e garantir que os valores estão no range
                r = Math.max(0, Math.min(1, r)) * 255;
                g = Math.max(0, Math.min(1, g)) * 255;
                b = Math.max(0, Math.min(1, b)) * 255;
                
                result.put(i, j, new double[]{b, g, r}); // BGR format
            }
        }
        
        return result;
    }
    
    /**
     * Aplicar correção gamma para linearizar os valores RGB
     */
    private static double gammaCorrection(double value) {
        if (value <= 0.04045) {
            return value / 12.92;
        } else {
            return Math.pow((value + 0.055) / 1.055, 2.4);
        }
    }
    
    /**
     * Aplicar correção gamma inversa para voltar ao espaço sRGB
     */
    private static double inverseGammaCorrection(double value) {
        if (value <= 0.0031308) {
            return value * 12.92;
        } else {
            return 1.055 * Math.pow(value, 1.0 / 2.4) - 0.055;
        }
    }

    /**
     * Equalização de histograma no espaço HSV
     * Aplica equalização apenas no canal V (valor/intensidade)
     */
    public static Mat equalizeHistogramHSV(Mat img) {
        Mat result = img.clone();
        int N = img.rows() * img.cols(); // Número total de pixels
        int L = 256; // Níveis de cinza (0-255)
        
        // Mapa para contar frequência dos níveis V
        Map<Integer, Integer> histogram = new HashMap<>();
        
        // Primeira passagem: converter BGR para HSV e construir histograma do canal V
        double[][][] hsvPixels = new double[img.rows()][img.cols()][3];
        
        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double[] bgr = img.get(i, j);
                double b = bgr[0] / 255.0;
                double g = bgr[1] / 255.0;
                double r = bgr[2] / 255.0;
                
                // Conversão BGR para HSV
                double max = Math.max(r, Math.max(g, b));
                double min = Math.min(r, Math.min(g, b));
                double delta = max - min;
                
                // Hue
                double h = 0;
                if (delta != 0) {
                    if (max == r) {
                        h = 60 * (((g - b) / delta) % 6);
                    } else if (max == g) {
                        h = 60 * (((b - r) / delta) + 2);
                    } else {
                        h = 60 * (((r - g) / delta) + 4);
                    }
                }
                if (h < 0) h += 360;
                
                // Saturation
                double s = (max == 0) ? 0 : (delta / max);
                
                // Value
                double v = max;
                
                hsvPixels[i][j][0] = h;
                hsvPixels[i][j][1] = s;
                hsvPixels[i][j][2] = v;
                
                // Quantizar V para construir histograma (0-255)
                int vLevel = (int) Math.round(v * 255);
                vLevel = Math.max(0, Math.min(255, vLevel));
                
                histogram.put(vLevel, histogram.getOrDefault(vLevel, 0) + 1);
            }
        }
        
        // Calcular função de transformação usando as fórmulas das imagens
        Map<Integer, Integer> transformFunction = new TreeMap<>();
        
        for(int k = 0; k <= 255; k++) {
            int nk = histogram.getOrDefault(k, 0); // Número de pixels com nível k
            double prk = (double) nk / N; // P(rk) = nk/N
            
            // Sk = T(rk) = Σ(i=0 to k) ni/N
            double sk = 0;
            for(int i = 0; i <= k; i++) {
                int ni = histogram.getOrDefault(i, 0);
                sk += (double) ni / N;
            }
            
            // Novo nível equalizado
            int newLevel = (int) Math.round(sk * (L - 1));
            newLevel = Math.max(0, Math.min(255, newLevel));
            transformFunction.put(k, newLevel);
            
            if(nk > 0) {
                System.out.printf("k=%d, nk=%d, P(rk)=%.4f, Sk=%.4f, novo_nivel=%d%n", 
                                k, nk, prk, sk, newLevel);
            }
        }
        
        // Segunda passagem: aplicar equalização e converter de volta para BGR
        for(int i = 0; i < img.rows(); i++) {
            for(int j = 0; j < img.cols(); j++) {
                double h = hsvPixels[i][j][0];
                double s = hsvPixels[i][j][1];
                double v = hsvPixels[i][j][2];
                
                // Equalizar apenas o canal V
                int vLevel = (int) Math.round(v * 255);
                vLevel = Math.max(0, Math.min(255, vLevel));
                int newVLevel = transformFunction.get(vLevel);
                double newV = newVLevel / 255.0;
                
                // Converter HSV equalizado de volta para RGB
                double c = newV * s;
                double x = c * (1 - Math.abs(((h / 60) % 2) - 1));
                double m = newV - c;
                
                double r = 0, g = 0, b = 0;
                
                if (h >= 0 && h < 60) {
                    r = c; g = x; b = 0;
                } else if (h >= 60 && h < 120) {
                    r = x; g = c; b = 0;
                } else if (h >= 120 && h < 180) {
                    r = 0; g = c; b = x;
                } else if (h >= 180 && h < 240) {
                    r = 0; g = x; b = c;
                } else if (h >= 240 && h < 300) {
                    r = x; g = 0; b = c;
                } else if (h >= 300 && h < 360) {
                    r = c; g = 0; b = x;
                }
                
                r = (r + m) * 255;
                g = (g + m) * 255;
                b = (b + m) * 255;
                
                // Garantir que os valores estão no range [0,255]
                r = Math.max(0, Math.min(255, r));
                g = Math.max(0, Math.min(255, g));
                b = Math.max(0, Math.min(255, b));
                
                result.put(i, j, new double[]{b, g, r}); // BGR format
            }
        }
        
        return result;
    }
}