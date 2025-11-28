package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.EstadoSessao;
import model.Sessao;
import util.ConnectionFactory;

public class SessaoDAO {

    // ------------------------------------
    // READ - Buscar todos
    // ------------------------------------
    public List<Sessao> buscarTodas() {
        List<Sessao> sessoes = new ArrayList<>();

        String sql = "SELECT * FROM sessao";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sessao sessao = new Sessao(
                        rs.getLong("id_sessao"),
                        rs.getString("sala"),
                        rs.getTimestamp("horarioSessao").toLocalDateTime(),
                        rs.getLong("id_ingresso") // id do ingresso
                );              

                // estado salvo no banco
                sessao.setEstado(EstadoSessao.valueOf(rs.getString("estado")));                
                sessoes.add(sessao);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar sessões: " + e.getMessage());
            e.printStackTrace();
        }

        return sessoes;
    }

    // ------------------------------------
    // READ BY ID
    // ------------------------------------
    public Sessao buscarPorId(Long id) {

        Sessao sessao = null;

        String sql = "SELECT * FROM sessao WHERE id_sessao = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    sessao = new Sessao(
                            rs.getLong("id_sessao"),
                            rs.getString("sala"),
                            rs.getTimestamp("horarioSessao").toLocalDateTime(),
                            rs.getLong("id_ingresso") // id do ingresso
                    );
                    sessao.setEstado(EstadoSessao.valueOf(rs.getString("estado")));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar sessão por ID: " + id + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }

        return sessao;
    }

    // ------------------------------------
    // CREATE
    // ------------------------------------
    public void inserir(Sessao sessao) {

        String sql = "INSERT INTO sessao (sala, horarioSessao, estado, id_ingresso) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, sessao.getSala());
            stmt.setTimestamp(2, Timestamp.valueOf(sessao.getHorario()));
            stmt.setString(3, sessao.getEstado().name());
            stmt.setLong(4, sessao.getIdIngresso());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    sessao.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir sessão. Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------------
    // UPDATE
    // ------------------------------------
    public void atualizar(Sessao sessao) {

        String sql = "UPDATE sessao SET sala = ?, horarioSessao = ?, estado = ?, id_ingresso = ? WHERE id_sessao = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessao.getSala());
            stmt.setTimestamp(2, Timestamp.valueOf(sessao.getHorario()));
            stmt.setString(3, sessao.getEstado().name());
            stmt.setLong(4, sessao.getIdIngresso());
            stmt.setLong(5, sessao.getId());

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("Sessão ID " + sessao.getId() + " atualizada. Linhas afetadas: " + linhasAfetadas);

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar sessão ID: " + sessao.getId() + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------------
    // DELETE
    // ------------------------------------
    public void deletar(Long id) {

        String sql = "DELETE FROM sessao WHERE id_sessao = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("Sessão ID " + id + " deletada. Linhas afetadas: " + linhasAfetadas);

        } catch (SQLException e) {
            System.err.println("Erro ao deletar sessão ID: " + id + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
