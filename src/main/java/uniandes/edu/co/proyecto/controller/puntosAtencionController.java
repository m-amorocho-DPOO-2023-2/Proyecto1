package uniandes.edu.co.proyecto.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uniandes.edu.co.proyecto.modelo.Oficina;
import uniandes.edu.co.proyecto.modelo.PuntoAtencion;
import uniandes.edu.co.proyecto.repositorio.OficinaRepository;
import uniandes.edu.co.proyecto.repositorio.PuntoAtencionRepository;

@Controller
public class puntosAtencionController {

    @Autowired
    PuntoAtencionRepository puntoAtencionRepository;

    @Autowired
    OficinaRepository oficinaRepository;


    @GetMapping("/puntosAtencion")
    public String puntosAtencion(Model model) {
        model.addAttribute("puntosAtencion", puntoAtencionRepository.darPuntosAtencion());
        return "puntosAtencion";
        //return model.toString();
    }

    @GetMapping("/puntosAtencion/new")
    public String puntosAtencionForm(Model model) {
        model.addAttribute("puntoAtencion", new PuntoAtencion());
        return "puntoAtencionNuevo";
    }

    @PostMapping("/puntosAtencion/new/save")
    public String puntoAtencionGuardar(@Param("tipo_punto") String tipo_punto,@Param("oficina_id") Integer oficina_id ) {
            Oficina oficina = oficinaRepository.darOficina(oficina_id);
            PuntoAtencion puntoAtencion = new PuntoAtencion();
            puntoAtencion.setOficina(oficina);
            puntoAtencion.setTipo_punto(tipo_punto);
            puntoAtencionRepository.insertarPuntoAtencion(puntoAtencion.getTipo_punto(), puntoAtencion.getIdOficinaAsociada());
        return "redirect:/puntosAtencion";
    }

    @GetMapping("/puntosAtencion/{id}/edit")
    public String puntoAtencionEditarForm(@PathVariable("id") int id, Model model) {
        PuntoAtencion puntoAtencion = puntoAtencionRepository.darPuntoAtencion(id);
        if (puntoAtencion != null) {
            model.addAttribute("puntoAtencion", puntoAtencion);
            return "puntoAtencionEditar";
        } else {
            return "redirect:/puntosAtencion";
        }
    }

    @GetMapping("/puntosAtencion/{id}/edit/save")
    public String puntoAtencionEditarGuardar(@PathVariable("id") int id, @ModelAttribute PuntoAtencion puntoAtencion) {
        puntoAtencionRepository.modificarPuntoAtencion(id, puntoAtencion.getTipo_punto(),
                puntoAtencion.getOficina().getId());

        return "redirect:/puntosAtencion";

    }

     
    @GetMapping("/puntosAtencion/{id}/delete")
    public String puntoAtencionEliminar(@PathVariable("id") int id){

        int transacciones = puntoAtencionRepository.darTransaccionesPunto(id);
        int opCuentas = puntoAtencionRepository.darOperacionesCuentaPunto(id);
        int opPrestamos = puntoAtencionRepository.darOperacionesPrestamosPunto(id);
        if(opPrestamos==0 && opCuentas==0 && transacciones==0){
            puntoAtencionRepository.eliminarPuntoAtencion(id);
        }
        return "redirect:/puntosAtencion";
    }
}
