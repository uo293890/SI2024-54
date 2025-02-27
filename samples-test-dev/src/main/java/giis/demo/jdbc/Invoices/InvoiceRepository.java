package giis.demo.jdbc.Invoices;

import giis.demo.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceRepository {

    // Método para guardar la factura en la base de datos
    public void save(Invoice invoice) {
        String sql = "INSERT INTO Invoice (agreement_id, amount, vat_percentage, profit_or_nonprofit) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Establecer los parámetros de la sentencia
            stmt.setInt(1, invoice.getAgreementId());
            stmt.setDouble(2, invoice.getAmount());
            stmt.setDouble(3, invoice.getVatPercentage());
            stmt.setString(4, invoice.getProfitOrNonprofit());

            // Ejecutar la sentencia de inserción
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la factura", e);
        }
    }

    // Método para obtener todas las facturas desde la base de datos
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM Invoice";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Iterar a través de los resultados y crear objetos Invoice
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("id"));
                invoice.setAgreementId(rs.getInt("agreement_id"));
                invoice.setAmount(rs.getDouble("amount"));
                invoice.setVatPercentage(rs.getDouble("vat_percentage"));
                invoice.setProfitOrNonprofit(rs.getString("profit_or_nonprofit"));
                invoices.add(invoice);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las facturas", e);
        }

        return invoices;
    }

    // Método para obtener una factura por su ID
    public Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM Invoice WHERE id = ?";
        Invoice invoice = null;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    invoice = new Invoice();
                    invoice.setId(rs.getInt("id"));
                    invoice.setAgreementId(rs.getInt("agreement_id"));
                    invoice.setAmount(rs.getDouble("amount"));
                    invoice.setVatPercentage(rs.getDouble("vat_percentage"));
                    invoice.setProfitOrNonprofit(rs.getString("profit_or_nonprofit"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la factura por ID", e);
        }

        return invoice;
    }
}
