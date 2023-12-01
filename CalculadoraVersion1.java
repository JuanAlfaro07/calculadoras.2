import java.util.Scanner;

public class CalculadoraVersion1 {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numUno, numDos, resultado, parametro;
        String opcion = "y";

        while (opcion.equalsIgnoreCase("y")) {
            System.out.println("Ingrese el primer numero");
            numUno = in.nextInt();

            System.out.println("Ingrese el segundo numero");
            numDos = in.nextInt();

            System.out.println("Ingrese un numero de caso: ");
            parametro = in.nextInt();

            switch (parametro) {
                case 1:
                    resultado = numUno + numDos;
                    System.out.println("El resultado de la suma es: " + resultado);
                    break;

                case 2:
                    resultado = numUno - numDos;
                    System.out.println("El resultado de la resta es: " + resultado);
                    break;

                case 3:
                    resultado = numUno * numDos;
                    System.out.println("El resultado de la multiplicacion es: " + resultado);
                    break;

                case 4:
                    if (numDos != 0) {
                        resultado = numUno / numDos;
                        System.out.println("El resultado de la division es: " + resultado);
                    } else {
                        System.out.println("No es posible dividir por cero");
                    }
                    break;

                default:
                    System.out.println("Error, la opcion no existe");
                    break;
            }

            System.out.println("¿Desea continuar operando? Ingrese Y para Si y N para No");
            opcion = in.next();
        }
    }
}
