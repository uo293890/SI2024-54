package giis.demo.jdbc.Invoices;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import giis.demo.util.Database;

public class SponsorRepository {

    public List<Sponsor> getAllSponsors() {
        List<Sponsor> sponsors = new ArrayList<>();
        String sql = "SELECT * FROM Sponsor";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sponsor sponsor = new Sponsor();
                sponsor.setId(rs.getInt("id"));
                sponsor.setName(rs.getString("name"));
                sponsors.add(sponsor);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los patrocinadores", e);
        }

        return sponsors;
    }
}
