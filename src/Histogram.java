import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Histogram {
    static { 
        System.load("D:\\Coisas\\estudos\\faculdade\\ZeLuis\\CompGrafica\\opencv\\OpenCV\\native\\opencv_java4110.dll");
    }

    public static void main(String[] args) {
        String filename = "images\\histogram\\blur";
        String fileformat = ".png";

        Mat img = Imgcodecs.imread(filename + fileformat);

        if (img.empty()){
            System.out.println("Imagem não carregada");
        } else {
            System.out.println("Imagem carregada -> " + img.size());
        }

        Map<Double, Integer> medias = new HashMap<>(); 

        for(int i = 0; i < img.rows(); i++){
            for(int j = 0; j < img.cols(); j++){
                double bgr[] = img.get(i, j);
                double mediaCinza = (bgr[0] + bgr[1] + bgr[2])/3;
                Integer cont;
                if(medias.get(mediaCinza) == null){
                    medias.put(mediaCinza, 0);
                }else{
                    cont = medias.get(mediaCinza);
                    cont ++;
                    medias.put(mediaCinza, cont);
                }
            }
        }

        for(double m : medias.keySet()){
            System.out.println(medias.get(m) + " -> " + m);
        }

        double prk = 
    }
}
