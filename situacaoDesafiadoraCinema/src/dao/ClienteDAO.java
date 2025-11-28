package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;
import util.ConnectionFactory;

public class ClienteDAO {

    // ------------------------------------
    // READ - Buscar todos
    // ------------------------------------
    public List<Cliente> buscarTodos() {
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM clientes";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getInt("idade")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar clientes: " + e.getMessage());
            e.printStackTrace();
        }

        return clientes;
    }

    // ------------------------------------
    // READ BY ID
    // ------------------------------------
    public Cliente buscarPorId(Long id) {

        Cliente cliente = null;

        String sql = "SELECT id, nome, idade FROM clientes WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    cliente = new Cliente(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getInt("idade")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por ID: " + id + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }

        return cliente;
    }

    // ------------------------------------
    // CREATE
    // ------------------------------------
    public void inserir(Cliente cliente) {

        String sql = "INSERT INTO clientes (nome, idade) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setInt(2, cliente.getIdade());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente: " + cliente.getNome() + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------------
    // UPDATE
    // ------------------------------------
    public void atualizar(Cliente cliente) {

        String sql = "UPDATE clientes SET nome = ?, idade = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setInt(3, cliente.getIdade());
            stmt.setLong(4, cliente.getId());

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("Cliente ID " + cliente.getId() + " atualizado. Linhas afetadas: " + linhasAfetadas);

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar cliente ID: " + cliente.getId() + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------------
    // DELETE
    // ------------------------------------
    public void deletar(Long id) {

        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            System.out.println("Cliente ID " + id + " deletado. Linhas afetadas: " + linhasAfetadas);

        } catch (SQLException e) {
            System.err.println("Erro ao deletar cliente ID: " + id + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
