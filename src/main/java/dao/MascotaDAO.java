/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import com.mycompany.clinicaveterinaria.model.Mascota;
import java.util.List;

/**
 *
 * @author marmo
 */
public interface MascotaDAO {
     List<Mascota> getAll();
    Mascota getById(int id);
    void insertar(Mascota m);
    void actualizar(Mascota m);
    void eliminar(int id);

}
