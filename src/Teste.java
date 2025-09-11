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

        double[][] matInter = new double[][]{
            {10, 15, 20, 15, 10},
            {20, 17, 14, 12, 10},
            {30, 19,  8,  9, 10},
            {40, 27, 14, 10,  6},
            {50, 35, 20, 11,  2}
        };

        double[][] matInterRe = new double[][]{
            {0,0,0},
            {0,0,0},
            {0,0,0}
        };

        for(int i = 0; i < matInterRe.length; i++){
            for(int j = 0; j < matInterRe[i].length; j++){
                if(i == (matInterRe.length - 1) && j == (matInterRe[i].length - 1)){
                    matInterRe[i][j] = matInter[i*2][j*2];
                }else if(i == (matInterRe.length - 1)){
                    // System.out.println();
                    double direita = matInter[i*2][2*j+1];
                    // System.out.println("direita : " + direita);
                    double atual = matInter[i*2][j*2];
                    // System.out.println("atual : " + atual);
                    matInterRe[i][j] = Math.floor((direita + atual) / 2);
                    // System.out.println("Contar: " + (direita + atual) / 2);
                }else if(j == (matInterRe[i].length - 1)){
                    // System.out.println();
                    double abaixo = matInter[2*i+1][2*j];
                    // System.out.println("abaixo: " + abaixo);
                    double atual = matInter[2*i][2*j];
                    // System.out.println("atual: " + atual);
                    matInterRe[i][j] = Math.floor((abaixo + atual) / 2);
                    // System.out.println("Contar: " + (abaixo + atual) / 2);
                }else{
                    // System.out.println();
                    double direita =  matInter[2*i][2*j+1];
                    // System.out.println("direita : " + direita);
                    double abaixo = matInter[2*i+1][2*j];
                    // System.out.println("abaixo: " + abaixo);
                    double diagonal = matInter[2*i+1][2*j+1];
                    // System.out.println("diagnonal: " + diagonal);
                    double atual = matInter[2*i][2*j];
                    // System.out.println("atual: " + atual);
                    matInterRe[i][j] = Math.floor((direita + abaixo + diagonal + atual) / 4);
                    // System.out.println("Contar: " + (direita + abaixo + diagonal + atual) / 4);
                }
                System.out.println();
                System.out.println(matInterRe[i][j]);
            }
        }

        System.out.println();
        System.out.println();
        for(int i = 0; i < matInterRe.length; i++){
            for(int j = 0; j < matInterRe[i].length; j++){
                System.out.print(matInterRe[i][j] + " ");
            }
            System.out.println();
        }
    }
}
