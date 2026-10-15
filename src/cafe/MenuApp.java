package cafe;

import java.util.Scanner;

public class MenuApp {
    private ServicioProduccion servicio = new ServicioProduccion();

    public static void main(String[] args) {
        new MenuApp().mostrar();
    }

    public void mostrar() {
        Scanner sc = new Scanner(System.in);
        int op;
        do {
            System.out.println("\n=== Cafe: Produccion ===");
            System.out.println("1. Registrar colaborador");
            System.out.println("2. Registrar insumo");
            System.out.println("3. Registrar lote");
            System.out.println("4. Avanzar lote");
            System.out.println("5. Costo total de un lote");
            System.out.println("6. Lotes bloqueados");
            System.out.println("7. Quien puede hacer cada etapa");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
            try {
                op = Integer.parseInt(sc.nextLine().trim());
                if (op != 0) System.out.println("Pendiente de implementar: opcion " + op);
            } catch (NumberFormatException ex) {
                op = -1;
                System.out.println("Opcion invalida.");
            }
        } while (op != 0);
    }
}
