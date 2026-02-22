/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import com.mycompany.clinicaveterinaria.model.Mascota;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pool.MyDataSource;

/**
 *
 * @author marmo
 */
public class MascotaDAOImpl implements MascotaDAO{
     @Override
    public List<Mascota> getAll() {
        List<Mascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascota";
        try (Connection con = MyDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Mascota(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("especie"),
                    rs.getInt("edad"),
                    rs.getInt("id_propietario")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Mascota getById(int id) {
        String sql = "SELECT * FROM mascota WHERE id = ?";
        try (Connection con = MyDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Mascota(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("especie"),
                    rs.getInt("edad"),
                    rs.getInt("id_propietario")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insertar(Mascota m) {
        String sql = "INSERT INTO mascota (nombre, especie, edad, id_propietario) VALUES (?, ?, ?, ?)";
        try (Connection con = MyDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getEspecie());
            ps.setInt(3, m.getEdad());
            ps.setInt(4, m.getIdPropietario());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Mascota m) {
        String sql = "UPDATE mascota SET nombre=?, especie=?, edad=?, id_propietario=? WHERE id=?";
        try (Connection con = MyDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getEspecie());
            ps.setInt(3, m.getEdad());
            ps.setInt(4, m.getIdPropietario());
            ps.setInt(5, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM mascota WHERE id = ?";
        try (Connection con = MyDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
