import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {
    static { 
        // System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
        // System.out.println();
        // System.out.println(Core.NATIVE_LIBRARY_NAME);
        System.load("C:\\Users\\carlo\\OneDrive\\Desktop\\faculdade\\zeluis\\grafica\\opencv\\native\\opencv_java4120.dll");
    }

    public static void main(String[] args) {
        String filename = "suisilvio";
        String fileFormat = ".png";
        Mat img = Imgcodecs.imread(filename + fileFormat);
        // System.out.println(img.type());
        Mat imgResultantequadrado = new Mat(img.rows() * 2, img.cols() * 2, 16);
        // Mat imgResultantequadrado = new Mat(img.rows(), img.cols(), 16);
        // Imgcodecs.imwrite("LMSZOOM.png", );
        if (img.empty()) {
            System.out.println("Não conseguiu carregar a imagem!");
        } else {
            System.out.println("Imagem carregada: " + img.size());
        }

        System.out.println(img.cols());
        System.out.println(img.rows());
        //Devolve no formato BGR em vez de RGB
        System.out.println(img.get(0, 0)[0] + " " + img.get(0, 0)[1] + " " + img.get(0, 0)[2]);
        for(int i = 0; i < img.rows(); i++){
            for(int j = 0; j < img.cols(); j++){
                imgResultantequadrado.put(2*i, 2*j, img.get(i, j));
                imgResultantequadrado.put(2*i, 2*j + 1, img.get(i, j));
                imgResultantequadrado.put(2*i + 1, 2*j, img.get(i, j));
                imgResultantequadrado.put(2*i + 1, 2*j + 1, img.get(i, j));
            }
        }
        Imgcodecs.imwrite(filename + "zoomquadrado" + fileFormat, imgResultantequadrado);

        Mat imgResultanteInterpolado = new Mat(img.rows() * 2 - 1, img.cols() * 2 - 1, 16);
        for(int i = 0; i < img.rows(); i++){
            for(int j = 0; j < img.cols(); j++){
                imgResultanteInterpolado.put(2*i, 2*j, img.get(i, j));
            }
            // for(int j = 0; j < img.cols(); j++){
            //     imgResultanteInterpolado.put(2*i, 2*j, img.get(i, j));
            // }
        }

        for(int i = 0; i < imgResultanteInterpolado.rows(); i++){
            for(int j = 0; j < imgResultanteInterpolado.cols(); j++){
                if(i % 2 == 0 && j % 2 == 0)continue;
                if(i % 2 != 0 && j % 2 != 0)continue;
                if(i % 2 == 0){
                    double[] colunaAnterior = imgResultanteInterpolado.get(i, j - 1);
                    double[] colunaPosterior = imgResultanteInterpolado.get(i, j + 1);
                    double[] result = new double[]{
                        (colunaAnterior[0] + colunaPosterior[0])/2,
                        (colunaAnterior[1] + colunaPosterior[1])/2,
                        (colunaAnterior[2] + colunaPosterior[2])/2
                    };
                    imgResultanteInterpolado.put(i, j, result);
                }else{
                    double[] linhaAnterior = imgResultanteInterpolado.get(i - 1, j);
                    double[] linhaPosterior = imgResultanteInterpolado.get(i + 1, j);
                    double[] result = new double[]{
                        (linhaAnterior[0] + linhaPosterior[0])/2,
                        (linhaAnterior[1] + linhaPosterior[1])/2,
                        (linhaAnterior[2] + linhaPosterior[2])/2
                    };
                    imgResultanteInterpolado.put(i, j, result);
                    if(j != 0){
                        double[] colunaAnteAnterior = imgResultanteInterpolado.get(i, j - 2);
                        double[] resultColunaAnterior = new double[]{
                            (colunaAnteAnterior[0] + result[0])/2,
                            (colunaAnteAnterior[1] + result[1])/2,
                            (colunaAnteAnterior[2] + result[2])/2,
                        };
                        imgResultanteInterpolado.put(i, j - 1, resultColunaAnterior);
                    }
                }
            }
        }

        Imgcodecs.imwrite(filename + "Interpolado" + fileFormat, imgResultanteInterpolado);
    }
}
