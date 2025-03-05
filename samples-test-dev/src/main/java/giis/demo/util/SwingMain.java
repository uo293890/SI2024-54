package giis.demo.util;

import java.awt.EventQueue;
import giis.demo.tkrun.*;

public class SwingMain {
	public static void main(String[] args) {
        Database db = new Database();
        System.out.println("Creando la base de datos...");
        db.createDatabase(true);  // Recrear tablas
        System.out.println("Base de datos creada.");

        System.out.println("Cargando datos iniciales...");
        db.loadDatabase();        // Cargar datos iniciales
        System.out.println("Datos iniciales cargados.");

        EventQueue.invokeLater(() -> {
            MainWindowView mainView = new MainWindowView();
            MainWindowModel mainModel = new MainWindowModel();
            new MainWindowController(mainModel, mainView);
        });
    }
}