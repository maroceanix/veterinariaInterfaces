/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import com.mycompany.clinicaveterinaria.model.Usuario;
import java.util.List;

/**
 *
 * @author marmo
 */
public interface UsuarioDAO {
     List<Usuario> getAll();
    Usuario getByNombreYPassword(String nombre, String password);
    void insertar(Usuario u);
    void eliminar(int id);

}
