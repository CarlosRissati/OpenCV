public class Teste {
    public static void main(String[] args){
        double[][] mat = new double[][]{
            {10, 20, 10},
            {30,  8, 10},
            {50, 20,  2}
        };
        double[][] matR = new double[][]{
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0}
        };


        for(int i = 0; i < mat.length; i++){
            for(int j = 0; j < mat[i].length; j++){
                // matR.put(2*i, 2*j, mati, j));
                matR[2*i][2*j] = mat[i][j];
            }
            // for(int j = 0; j < mat.cols(); j++){
            //     matR.put(2*i, 2*j, mati, j));
            // }
        }

        for(int i = 0; i < matR.length; i++){
            for(int j = 0; j < matR[i].length; j++){
                if(i % 2 == 0 && j % 2 == 0)continue;
                if(i % 2 != 0 && j % 2 != 0)continue;
                if(i % 2 == 0){
                    double colunaAnterior = matR[i][j - 1];
                    double colunaPosterior = matR[i][j + 1];
                    double result = (colunaAnterior + colunaPosterior)/2;
                    matR[i][j] = result;
                }else{
                    double linhaAnterior = matR[i - 1][j];
                    double linhaPosterior = matR[i + 1][j];
                    double result = (linhaAnterior + linhaPosterior)/2;
                    matR[i][j] = result;
                    if(j != 0){
                        double colunaAnteAnterior = matR[i][j - 2];
                        double resultColunaAnterior = (colunaAnteAnterior + result)/2;
                        matR[i][j - 1] = resultColunaAnterior;
                    }
                }
            }
        }

        for(int i = 0; i < matR.length; i++){
            for(int j = 0; j < matR[i].length; j++){
                System.out.print(matR[i][j] + " ");
            }
            System.out.println();
        }
    }
}
