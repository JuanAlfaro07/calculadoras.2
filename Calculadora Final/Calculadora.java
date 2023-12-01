import javax.swing.*; // En esta linea de codigo se importa las clases necesarias de javax.swing para la interfaz gráfica
import java.awt.*; // En esta linea de codigo se mporta las clases necesarias de java.awt para la interfaz gráfica
import java.awt.event.ActionEvent; // En esta linea de codigo se importa la clase ActionEvent para manejar eventos
import java.awt.event.ActionListener; // En esta linea de codigo se importa la interfaz ActionListener para manejar acciones
import java.util.Stack; // En esta linea de codigo se importa la clase Stack para utilizar pilas en la lógica de la calculadora

public class Calculadora extends JFrame implements ActionListener { // En esta linea de codigo se declara la clase Calculadora que extiende JFrame e implementa ActionListener
    private JTextField pantalla; // En esta linea de codigo se declara un campo para la pantalla de la calculadora

    // En esta porcion de codigo se crea el constructor de la clase Calculadora
    public Calculadora() {
        setTitle("Calculadora"); // En esta linea de codigo se establece el título de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // En esta linea de codigo se define la operación al cerrar la ventana
        setLayout(new BorderLayout()); // En esta linea de codigo se rstablece un layout de tipo BorderLayout en la ventana

        pantalla = new JTextField(); // Esta linea de codigo crea un nuevo campo de texto para la pantalla
        pantalla.setEditable(false); // Esta linea de codigo hace que la pantalla no sea editable directamente por el usuario
        pantalla.setHorizontalAlignment(JTextField.RIGHT); // Esta linea de codigo alinea el texto a la derecha en la pantalla
        pantalla.setFont(new Font("Arial", Font.PLAIN, 24)); // Esta linea de codigo establece la fuente y tamaño del texto en la pantalla
        add(pantalla, BorderLayout.NORTH); // Esta linea de codigo agrega la pantalla en la parte superior de la ventana

        JPanel panelBotones = new JPanel(new GridLayout(6, 4, 5, 5)); // Esta linea de codigo crea un panel con un layout de cuadrícula para los botones

        // En esta porcion de codigo se crea un Array de etiquetas para los botones de la calculadora
        String[] etiquetasBotones = {
            "AC", "(", ")", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "C", "=",
        };

        // En esta porcion de codigo se itera sobre las etiquetas de los botones para crear y agregar los botones al panel
        for (String etiqueta : etiquetasBotones) {
            JButton boton = new JButton(etiqueta); // Esta linea de codigo crea un nuevo botón con la etiqueta actual
            boton.setFont(new Font("Arial", Font.PLAIN, 18)); // Esta linea de codigo establece la fuente y tamaño del texto en el botón
            boton.addActionListener(this); // Esta linea de codigo asigna esta clase como el ActionListener para el botón
            panelBotones.add(boton); // Esta linea de codigo agrega el botón al panel
        }

        add(panelBotones, BorderLayout.CENTER); // Esta linea de codigo agrega el panel de botones al centro de la ventana

        pack(); // Esta linea de codigo ajusta el tamaño de la ventana automáticamente para que se ajuste a los componentes
        setLocationRelativeTo(null); // Esta linea de codigo hace que la ventana aparezca en el centro de la pantalla
    }

    // En esta porcion de codigo se utiliza un método que maneja las acciones realizadas en la interfaz
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand(); //Esta linea de codigo btiene el comando asociado a la acción realizada en la interfaz

        // En esta porcion de codigo se verifica qué acción se realizó y actúa en consecuencia
        if (comando.equals("=")) {
            try {
                String expresion = pantalla.getText(); // Esta linea de codigo obtiene la expresión actual de la pantalla
                String resultado = evaluarExpresion(expresion); // Esta linea de codigo evalúa la expresión y obtiene el resultado
                pantalla.setText(resultado); // Esta linea de codigo muestra el resultado en la pantalla
            } catch (Exception ex) {
                pantalla.setText("Error"); // Esta linea de codigo muestra "Error" si ocurre una excepción al evaluar la expresión
            }
        } else if (comando.equals("C")) {
            String textoActual = pantalla.getText(); // Esta linea de codigo obtiene el texto actual de la pantalla
            if (!textoActual.isEmpty()) {
                pantalla.setText(textoActual.substring(0, textoActual.length() - 1)); // Esta linea de codigo borra el último carácter del texto en la pantalla
            }
        } else if (comando.equals("AC")) {
            pantalla.setText(""); // Esta linea de codigo borra todo el contenido de la pantalla
        } else {
            pantalla.setText(pantalla.getText() + comando); // Esta linea de codigo concatena el comando con el contenido actual de la pantalla
        }
    }

    // En esta porcion de codigo se utiliza un método para evaluar la expresión matemática ingresada
    private String evaluarExpresion(String expresion) {
        Stack<Double> numeros = new Stack<>(); // Esta linea de codigo crea una pila para almacenar números
        Stack<Character> operadores = new Stack<>(); // Esta linea de codigo crea una pila para almacenar operadores

        // En esta porcion de codigo se itera sobre la expresión ingresada
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i); // Esta linea de codigo obtiene el carácter actual de la expresión

            // En esta porcion de codigo se verifica el tipo de carácter y realiza acciones correspondientes
            if (Character.isDigit(c) || c == '.') {
                StringBuilder numero = new StringBuilder(); // Esta linea de codigo crea un StringBuilder para construir números
                // En esta porcion de codigo se lee y construye el número completo
                while (i < expresion.length() && (Character.isDigit(expresion.charAt(i)) || expresion.charAt(i) == '.')) {
                    numero.append(expresion.charAt(i));
                    i++;
                }
                i--;

                numeros.push(Double.parseDouble(numero.toString())); // Esta linea de codigo convierte y guarda el número en la pila
            } else if (c == '(') {
                operadores.push(c); // Esta linea de codigo guarda el paréntesis '(' en la pila de operadores
            } else if (c == ')') {
                // En esta porcion de codigo se realiza operaciones hasta encontrar el paréntesis '(' correspondiente
                while (!operadores.isEmpty() && operadores.peek() != '(') {
                    realizarOperacion(numeros, operadores.pop()); // Esta linea de codigo realiza la operación con los números y operadores
                }
                operadores.pop(); // Esta linea de codigo quita el '(' de la pila de operadores
            } else if (esOperador(c)) {
                // En esta porcion de codigo realiza operaciones según la precedencia de los operadores
                while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(c)) {
                    realizarOperacion(numeros, operadores.pop()); // Esta linea de codigo realiza la operación con los números y operadores
                }
                operadores.push(c); // Esta linea de codigo agrega el operador a la pila de operadores
            }
        }

        // En esta porcion de codigo se realiza operaciones con los operadores y números restantes
        while (!operadores.isEmpty()) {
            realizarOperacion(numeros, operadores.pop()); // Esta linea de codigo realiza la operación con los números y operadores
        }

        return String.valueOf(numeros.pop()); // Esta linea de codigo retorna el resultado final de la expresión
    }

    // En esta porcion de codigo se utiliza un método para verificar si un carácter es un operador
    private boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%'; //Esta linea de codigo verifica si el carácter es un operador
    }

    // En esta porcion de codigo se utiliza un método para obtener la precedencia de un operador
    private int precedencia(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1; // Esta linea de codigo retorna precedencia 1 para suma y resta
            case '*':
            case '/':
            case '%':
                return 2; // Esta linea de codigo retorna precedencia 2 para multiplicación, división y módulo
        }
        return -1; // Esta linea de codigo retorna -1 si el operador no es válido
    }

    // En esta porcion de codigo se utiliza un método para realizar operaciones matemáticas
    private void realizarOperacion(Stack<Double> numeros, char operador) {
        double num2 = numeros.pop(); // Esta linea de codigo obtiene el segundo número de la pila
        double num1 = numeros.pop(); // Esta linea de codigo obtiene el primer número de la pila
        double resultado = 0; // Esta linea de codigo inicializa el resultado

        // En esta porcion de codigo se realiza la operación correspondiente según el operador
        switch (operador) {
            case '+':
                resultado = num1 + num2; // Esta linea de codigo realiza la suma
                break;
            case '-':
                resultado = num1 - num2; // Esta linea de codigo realiza la resta
                break;
            case '*':
                resultado = num1 * num2; // Esta linea de codigo realiza la multiplicación
                break;
            case '/':
                if (num2 != 0) {
                    resultado = num1 / num2; // Esta linea de codigo realiza la división si el divisor no es cero
                } else {
                    throw new ArithmeticException("Error: División por cero"); // Esta linea de codigo lanza una excepción si el divisor es cero
                }
                break;
            case '%':
                resultado = num1 % num2; // Esta linea de codigo realiza el módulo
                break;
        }

        numeros.push(resultado); // Esta linea de codigo agrega el resultado a la pila de números
    }

    // En esta porcion de codigo se utiliza un método principal (entry point) del programa
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // Esta linea de codigo ejecuta la creación de la interfaz en el hilo de eventos de Swing
            Calculadora calculadora = new Calculadora(); // Esta linea de codigo crea una instancia de la calculadora
            calculadora.setVisible(true); // Esta linea de codigo hace que la ventana de la calculadora sea visible
        });
    }
}
