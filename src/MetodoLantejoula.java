import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class MetodoLantejoula {
    
    static { 
        System.load("C:\\Users\\carlo\\OneDrive\\Desktop\\faculdade\\zeluis\\grafica\\OpenCV2\\native\\opencv_java4120.dll");
    }

    public static void main(String[] args) {
        // Mat elementoEstruturante = Imgcodecs.imread("Lantejoula\\estruturante.jpg");
        // double[][] elementoEstruturante = new double[][]{
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        //     {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        // };
        
        // Mat img = Imgcodecs.imread("Lantejoula\\exemplo.png");
        
        // Binarizar(img);
        
        // double[][] elementoEstruturante = new double[][]{
        //     {255, 0, 255},
        //     {255, 0, 255},
        //     {255, 0, 255}
        // };
        
        // Mat img = new Mat(5, 5, 16);
        // double[] branco = new double[]{255, 255, 255};
        // double[] preto = new double[]{0, 0, 0};
        // for(int i = 0; i < img.rows(); i++){
        //     for(int j = 0; j < img.cols(); j++){
        //         if(i == 0 || j == 0 || i == 4 || j == 4){
        //             img.put(i, j, branco);
        //         }else{
        //             img.put(i, j, preto);
        //         }
        //     }
        // }

        
        //Equações para lantejoula
        int[][] matAux = new int[][]{
            {1, 1, 1, 1, 0, 0, 0},
            {1, 1, 1, 1, 0, 0, 0},
            {1, 1, 1, 1, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1}
        };

        double[][] elementoEstruturante = new double[][]{
            {255, 0, 255},
            {0  , 0,   0},
            {255, 0, 255}
        };

        Mat img = new Mat(matAux.length, matAux.length, 16);
        double[] branco = new double[]{255, 255, 255};
        double[] preto = new double[]{0, 0, 0};
        for(int i = 0; i < img.rows(); i++){
            for(int j = 0; j < img.cols(); j++){
                if(matAux[i][j] == 1){
                    img.put(i, j, preto);
                }else{
                    img.put(i, j, branco);
                }
            }
        }

        Imgcodecs.imwrite("Lantejoula\\resultBinario.png", img);

        Mat result = LanteJoul(img, elementoEstruturante);
        System.out.println("Result gerado");
        Imgcodecs.imwrite("Lantejoula\\esqueleto.png", result);
        
    }

    public static void Binarizar(Mat img){

        for(int i = 0; i < img.rows(); i++){
            for(int j = 0; j < img.cols(); j++){
                if(img.get(i, j)[0] > 125 || img.get(i, j)[1] > 125 || img.get(i, j)[2] > 125){
                    img.put(i, j, new double[]{255,255,255});
                }else{
                    img.put(i, j, new double[]{0,0,0});
                }
            }
        }

    }

    public static boolean VerifyIncludedRangeErosao(int index[], Mat img, double[][] elemntEstrut){
        int centIndex = elemntEstrut.length/2;
        boolean verification = true;

        for(int i = 0; i < elemntEstrut.length; i++){
            for(int j = 0; j < elemntEstrut.length; j++){
                if((index[0] - (i - centIndex)) < 0) {
                    verification = false;
                    break;
                }
                if((index[1] - (j - centIndex)) < 0) {
                    verification = false;
                    break;
                }
                if((index[0] + (i - centIndex)) > img.rows() - 1) {
                    verification = false;
                    break;
                }
                if((index[1] + (j - centIndex)) > img.cols() - 1) {
                    verification = false;
                    break;
                }
                if(elemntEstrut[i][j] == 255) continue;
                if(!(elemntEstrut[i][j] == img.get(index[0] + (i - centIndex), index[1] + (j - centIndex))[0])) verification = false;
            }
        }

        return verification;
    }

    // index {0}{10}
    // centIndex = 2
    // i = 2 j = 4
    // img.rows() = 11
    // img.cols() = 11

    // 0 - (2 - 2) -> (0 - 0) < 0 ? false
    // 10 - (4 - 2) -> (10 - 2) < 0 ? false
    // 0 + (2 - 2) -> (0 + 0) > 11 ? false
    // 10 + (4 - 2) -> (10 + 2) > 11 ? true

    public static Mat Erosao(Mat img, double[][] elemntEstrut){

        int centralIndex = elemntEstrut.length/2;
        Mat result = new Mat(img.size(), img.type());

        for(int i = 0; i < result.rows(); i++){
            for(int j = 0; j < result.cols(); j++){
                result.put(i, j, new double[]{255,255,255});
            }
        }

        for(int i = 0; i < img.rows(); i++){
            for(int j = 0; j < img.cols(); j++){
                double valorInverso = (elemntEstrut[centralIndex][centralIndex] == 255 ? 0 : 255);
                boolean verificar = (elemntEstrut[centralIndex][centralIndex] == img.get(i, j)[0]);

                if(verificar){
                    if(!VerifyIncludedRangeErosao(new int[]{i, j}, img, elemntEstrut)){
                        result.put(i, j, new double[]{valorInverso, valorInverso, valorInverso});
                    }else{
                        result.put(i, j, img.get(i,j));
                    }
                }else{
                    result.put(i, j, new double[]{valorInverso, valorInverso, valorInverso});
                }
            }
        }

        return result;
    }

    private static void PaintIncludedRangeDilatacao(int[] index, Mat result, Mat img, double[][] elemntEstrut){
        int centIndex = elemntEstrut.length/2;

        for(int i = 0; i < elemntEstrut.length; i++){
            for(int j = 0; j < elemntEstrut.length; j++){
                if((index[0] - (i - centIndex)) < 0) continue;
                if((index[1] - (j - centIndex)) < 0) continue;
                if((index[0] + (i - centIndex)) > result.rows() - 1) continue;
                if((index[1] + (j - centIndex)) > result.cols() - 1) continue;
                if(elemntEstrut[i][j] == 255) continue;
                double valor = elemntEstrut[i][j];
                result.put(index[0] + (i - centIndex), index[1] + (j - centIndex), new double[]{valor, valor, valor});
            }
        }

    }

    public static Mat Dilatacao(Mat img, double[][] elemntEstrut){

        int centralIndex = elemntEstrut.length/2;
        Mat result = new Mat(img.size(), img.type());

        for(int i = 0; i < result.rows(); i++){
            for(int j = 0; j < result.cols(); j++){
                result.put(i, j, new double[]{255,255,255});
            }
        }

        for(int i = 0; i < result.rows(); i++){
            for(int j = 0; j < result.cols(); j++){
                boolean verificar = (elemntEstrut[centralIndex][centralIndex] == img.get(i, j)[0]);
                if(verificar){
                    PaintIncludedRangeDilatacao(new int[]{i, j}, result, img, elemntEstrut);
                }
            }
        }

        return result;
    }

    public static Mat LanteJoul(Mat img, double[][] elemntEstrut){

        Mat esqueleto = new Mat(img.size(), img.type());
        for(int i = 0; i < esqueleto.rows(); i++){
            for(int j = 0; j < esqueleto.cols(); j++){
                esqueleto.put(i, j, new double[]{255,255,255});
            }
        }

        Mat matrizAnterior = img.clone();
        do {
            Mat resultante = Erosao(matrizAnterior, elemntEstrut);
            Mat dilatar = Dilatacao(resultante, elemntEstrut);
            esqueleto = Adicao(esqueleto, Diferenca(matrizAnterior, dilatar));
            matrizAnterior = resultante.clone();
        } while (Nulo(matrizAnterior) != true);

        return esqueleto;
    }

    public static Mat Adicao(Mat esqueleto, Mat diferenca){

        Mat result = esqueleto.clone();

        for(int i = 0; i < result.rows(); i++){
            for(int j = 0; j < result.cols(); j++){
                if(diferenca.get(i, j)[0] == 0){
                    result.put(i,j, diferenca.get(i, j));
                }
            }
        }

        return result;
    }

    public static boolean Nulo(Mat teste){
        boolean aux = false;

        for(int i = 0; i < teste.rows(); i++){
            for(int j = 0; j < teste.cols(); j++){
                if(teste.get(i, j)[0] == 255){
                    aux = true;
                }else{
                    return aux = false;
                }
            }
        }

        return aux;
    }

    public static Mat Diferenca(Mat A, Mat B){

        Mat result = new Mat(A.size(), A.type());
        for(int i = 0; i < result.rows(); i++){
            for(int j = 0; j < result.cols(); j++){
                result.put(i, j, new double[]{255,255,255});
            }
        }

        double[] branco = new double[]{255, 255, 255};
        double[] preto = new double[]{0, 0, 0};

        for(int i = 0; i < result.rows(); i++){
            for(int j = 0; j < result.cols(); j++){
                if(A.get(i, j)[0] == B.get(i, j)[0]){
                    result.put(i, j, branco);
                }else {
                    result.put(i, j, preto);
                }
            }
        }

        return result;
    }

}
