package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Ingresso;
import util.ConnectionFactory;

public class IngressoDAO {

    // ------------------------------------
    // READ - Buscar todos
    // ------------------------------------
    public List<Ingresso> buscarTodos() {
        List<Ingresso> ingressos = new ArrayList<>();

        String sql = "SELECT * FROM ingressos";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ingresso ingresso = new Ingresso(
                        rs.getLong("id"),
                        rs.getString("assento"),
                        rs.getString("filme")
                );
                ingressos.add(ingresso);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar ingressos: " + e.getMessage());
            e.printStackTrace();
        }

        return ingressos;
    }

    // ------------------------------------
    // READ BY ID
    // ------------------------------------
    public Ingresso buscarPorId(Long id) {

        Ingresso ingresso = null;

        String sql = "SELECT id_ingresso, assento, filme FROM ingressos WHERE id_ingresso = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    ingresso = new Ingresso(
                            rs.getLong("id"),
                            rs.getString("assento"),
                            rs.getString("filme")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar ingresso por ID: " + id + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }

        return ingresso;
    }

    // ------------------------------------
    // CREATE
    // ------------------------------------
    public void inserir(Ingresso ingresso) {

        String sql = "INSERT INTO ingressos (assento, filme) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ingresso.getAssento());
            stmt.setString(2, ingresso.getFilme());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ingresso.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir ingresso. Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------------
    // UPDATE
    // ------------------------------------
    public void atualizar(Ingresso ingresso) {

        String sql = "UPDATE ingressos SET assento = ?, filme = ? WHERE id_ingresso = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ingresso.getAssento());
            stmt.setString(2, ingresso.getFilme());
            stmt.setLong(3, ingresso.getId());

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("Ingresso ID " + ingresso.getId() + " atualizado. Linhas afetadas: " + linhasAfetadas);

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar ingresso ID: " + ingresso.getId() + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------------
    // DELETE
    // ------------------------------------
    public void deletar(Long id) {

        String sql = "DELETE FROM ingressos WHERE id_ingresso = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("Ingresso ID " + id + " deletado. Linhas afetadas: " + linhasAfetadas);

        } catch (SQLException e) {
            System.err.println("Erro ao deletar ingresso ID: " + id + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
