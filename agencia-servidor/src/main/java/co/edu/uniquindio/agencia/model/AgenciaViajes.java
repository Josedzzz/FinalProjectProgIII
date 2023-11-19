package co.edu.uniquindio.agencia.model;

import co.edu.uniquindio.agencia.exceptions.*;
import co.edu.uniquindio.agencia.utilities.EmailUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Getter
@Setter
public class AgenciaViajes {
    private String nombre;
    private ArrayList<Destino> listaDestinos;
    private ArrayList<PaqueteTuristico> listaPaquetesTuristicos;
    private ArrayList<Reserva> listaReservas;
    private ArrayList<Guia> listaGuiasTuristicos;
    private ArrayList<Cliente> listaClientes;
    private ArrayList<Administrador> listaAdministradores;

    //Variables que tendran la instancia de esta clase
    private static final Logger LOGGER = Logger.getLogger(AgenciaViajes.class.getName());
    private static AgenciaViajes agenciaViajes;

    /**
     * Constructor que debe de ser privado para que ninguna otra clase pueda crear instancias de esta clase
     */
    private AgenciaViajes() {
        try {
            FileHandler fh = new FileHandler("logs.log", true);
            fh.setFormatter( new SimpleFormatter());
            LOGGER.addHandler(fh);
        } catch (IOException e) {
            LOGGER.log( Level.SEVERE, e.getMessage() );
        }
        LOGGER.log(Level.INFO, "Se crea una nueva instancia de la empresa");

        this.listaDestinos = new ArrayList<Destino>();
        this.listaPaquetesTuristicos = new ArrayList<PaqueteTuristico>();
        this.listaReservas = new ArrayList<Reserva>();
        this.listaGuiasTuristicos = new ArrayList<Guia>();
        this.listaClientes = new ArrayList<Cliente>();
        this.listaAdministradores = new ArrayList<Administrador>();
        //Quemo los datos de los administradores
        Administrador administrador = Administrador.administradorBuilder()
                .id("123")
                .nombre("Jose")
                .correo("josedavidamayar@gmail.com")
                .telefono("123")
                .residencia("Quimbaya")
                .contrasenia("123")
                .build();
        listaAdministradores.add(administrador);

        //Quemo datos de los guias
        ArrayList<Lenguaje> lenguajesGuia = new ArrayList<>();
        lenguajesGuia.add(Lenguaje.ESPANIOL);
        lenguajesGuia.add(Lenguaje.INGLES);
        ArrayList<CalificacionGuia> calificacionesGuia = new ArrayList<>();
        Guia guia = Guia.guiaBuilder()
                .id("333")
                .nombre("Camilo")
                .correo("Camilo@")
                .telefono("123")
                .residencia("Quimbaya")
                .contrasenia("333")
                .aniosExperiencia(2)
                .listaLenguajes(lenguajesGuia)
                .calificaciones(calificacionesGuia)
                .build();
        listaGuiasTuristicos.add(guia);

        //Quemo datos de los clientes
        Cliente cliente = Cliente.clienteBuilder()
                .id("111")
                .nombre("Nico")
                .correo("balinius11@gmail.com")
                .telefono("111")
                .residencia("Armenia")
                .contrasenia("111")
                .build();
        listaClientes.add(cliente);

        //Quemo datos de los destinos
        ArrayList<String> rutaImagenesDestino = new ArrayList<>();
        rutaImagenesDestino.add("src/main/resources/Images/ParqueDelCafe.jpg");
        rutaImagenesDestino.add("src/main/resources/Images/ParqueDelCafe2.jpg");
        ArrayList<CalificacionDestino> calificacionesDestino = new ArrayList<>();
        Destino destino = Destino.builder()
                .nombre("Parque del café")
                .ciudad("Montenegro")
                .descripcion("El Parque del Café es un parque temático colombiano situado en el corregimiento de Pueblo Tapao, del municipio de Montenegro en Quindío, Colombia.")
                .listaImagenes(rutaImagenesDestino)
                .tipoClima(TipoClima.CALIDO)
                .tipoDestino(TipoDestino.BOSQUE)
                .calificaciones(calificacionesDestino)
                .build();
        listaDestinos.add(destino);
        Destino destino1 = Destino.builder()
                .nombre("Panaca")
                .ciudad("Quimbaya")
                .descripcion("Algogooooo")
                .listaImagenes(rutaImagenesDestino)
                .tipoClima(TipoClima.CALIDO)
                .tipoDestino(TipoDestino.AVENTURA)
                .calificaciones(calificacionesDestino)
                .build();
        listaDestinos.add(destino1);

        //Quemo datos de los paquetes
        ArrayList<ServicioAdicional> serviciosAdicionalesPaquete = new ArrayList<>();
        serviciosAdicionalesPaquete.add(ServicioAdicional.ALIMENTOS);
        serviciosAdicionalesPaquete.add(ServicioAdicional.SEGUROS);
        ArrayList<Destino> destinosPaquete = new ArrayList<>();
        destinosPaquete.add(destino);
        destinosPaquete.add(destino1);
        LocalDate fechaInicial = LocalDate.of(2023, 12, 1);
        LocalDate fechaFinal = LocalDate.of(2023, 10, 2);
        PaqueteTuristico paqueteTuristico = PaqueteTuristico.builder()
                .nombre("Quindio Tour")
                .fechaInicial(fechaInicial)
                .fechaFinal(fechaFinal)
                .precio(100000.0)
                .cupoMaximo(20)
                .cupoDisponible(20)
                .listaServiciosAdicionales(serviciosAdicionalesPaquete)
                .listaDestinos(destinosPaquete)
                .build();
        listaPaquetesTuristicos.add(paqueteTuristico);

        //Quemo datos de reservas
        LocalDate fechaSolicitud = LocalDate.of(2023, 3, 4);
        Reserva reserva = Reserva.builder()
                .fechaSolicitud(fechaSolicitud)
                .fechaReserva(paqueteTuristico.getFechaInicial())
                .cantidadPersonas(2)
                .estadoReserva(EstadoReserva.PENDIENTE)
                .clienteInvolucrado(cliente)
                .paqueteTuristicoSeleccionado(paqueteTuristico)
                .guia(guia)
                .build();
        listaReservas.add(reserva);
    }

    /**
     * Metodo que se usara en otras clases que requieran la instancia de empresa
     * @return Instancia de la empresa
     */
    public static AgenciaViajes getInstance() {
        if (agenciaViajes == null) {
            agenciaViajes = new AgenciaViajes();
        }
        return agenciaViajes;
    }


    //FUNCIONES --------------------------------------------------------------------------------------

    //CRUD DESTINO -------------------------------------------------------------------------------

    /**
     * Obtiene el destino dado el nombre y la ciudad
     * @param nombre nombre del destino
     * @param ciudad ciudad del destino
     * @param i index que inicia en 0
     * @return
     */
    public Destino obtenerDestino(String nombre, String ciudad, int i) {
        if (i >= listaDestinos.size()) {
            return null;
        } else {
            Destino destino = listaDestinos.get(i);
            if (destino.getNombre().equals(nombre) && destino.getCiudad().equals(ciudad)) {
                return destino;
            } else {
                return obtenerDestino(nombre, ciudad, i + 1);
            }
        }
    }

    /**
     * Actualiza un los datos de un destino
     * @param nombre nombre del destino
     * @param ciudad ciudad del destino
     * @param descripcion descripcion del destino
     * @param tipoClima clima del destino
     * @throws DestinoNoRegistradoException
     */
    public void actualizarDestino(String nombre, String ciudad, String descripcion, TipoClima tipoClima, TipoDestino tipoDestino) throws DestinoNoRegistradoException, CampoObligatorioDestinoException {
        Destino destinoEncontrado = obtenerDestino(nombre, ciudad, 0);
        if (destinoEncontrado == null) {
            throw new DestinoNoRegistradoException("El destino que buscas actualizar no está registrado");
        } else {
            if (nombre == null || nombre.isEmpty()) {
                throw new CampoObligatorioDestinoException("El nombre del destino es obligatorio");
            }
            if (ciudad == null || ciudad.isEmpty()) {
                throw new CampoObligatorioDestinoException("La ciudad del destino es obligatoria");
            }
            if (descripcion == null || descripcion.isEmpty()) {
                throw new CampoObligatorioDestinoException("La descripción del destino es obligatoria");
            }
            /*if (listaImagenes == null || listaImagenes.isEmpty()) {
                throw new CampoObligatorioDestinoException("Ingrese por lo menos una imagén del destino");
            }*/
            if (tipoClima == null) {
                throw new CampoObligatorioDestinoException("El clima del destino es obligatorio");
            }
            if (tipoDestino == null) {
                throw new CampoObligatorioDestinoException("El tipo de destino es obligatorio");
            }
            destinoEncontrado.setDescripcion(descripcion);
            destinoEncontrado.setTipoClima(tipoClima);
            destinoEncontrado.setTipoDestino(tipoDestino);
        }
    }

    /**
     * Elimina un destino
     * @param nombre nombre del destino
     * @param ciudad ciudad del destino
     * @throws DestinoNoRegistradoException
     */
    public void eliminarDestino(String nombre, String ciudad) throws DestinoNoRegistradoException {
        Destino destinoEncontrado = obtenerDestino(nombre, ciudad, 0);
        if (destinoEncontrado == null) {
            throw  new DestinoNoRegistradoException("El destino que buscas eliminar no está registrado");
        } else {
            listaDestinos.remove(destinoEncontrado);
        }
    }

    /**
     * Crea un destino
     * @param nombre nombre del destino
     * @param ciudad ciudad del destino
     * @param descripcion descipcion del destino
     * @param listaImagenes lista de imagenes del destino
     * @param tipoClima clima del destino
     * @throws CampoObligatorioDestinoException
     * @throws DestinoYaExistenteException
     */
    public void crearDestino(String nombre, String ciudad, String descripcion, ArrayList<String> listaImagenes, TipoClima tipoClima, TipoDestino tipoDestino, ArrayList<CalificacionDestino> listaCalificaciones) throws CampoObligatorioDestinoException, DestinoYaExistenteException {
        Destino destinoEncontrado = obtenerDestino(nombre, ciudad, 0);
        if (destinoEncontrado != null) {
            throw new DestinoYaExistenteException("El destino que deseas registrar ya existe");
        } else {
            if (nombre == null || nombre.isEmpty()) {
                throw new CampoObligatorioDestinoException("El nombre del destino es obligatorio");
            }
            if (ciudad == null || ciudad.isEmpty()) {
                throw new CampoObligatorioDestinoException("La ciudad del destino es obligatoria");
            }
            if (descripcion == null || descripcion.isEmpty()) {
                throw new CampoObligatorioDestinoException("La descripción del destino es obligatoria");
            }
            /*if (listaImagenes == null || listaImagenes.isEmpty()) {
                throw new CampoObligatorioDestinoException("Ingrese por lo menos una imagén del destino");
            }*/
            if (tipoClima == null) {
                throw new CampoObligatorioDestinoException("El clima del destino es obligatorio");
            }
            if (tipoDestino == null) {
                throw new CampoObligatorioDestinoException("El tipo de destino es obligatorio");
            }
            Destino destino = Destino.builder()
                    .nombre(nombre)
                    .ciudad(ciudad)
                    .descripcion(descripcion)
                    .listaImagenes(listaImagenes)
                    .tipoClima(tipoClima)
                    .tipoDestino(tipoDestino)
                    .calificaciones(listaCalificaciones)
                    .build();
            listaDestinos.add(destino);
        }
    }

    //CRUD PAQUETE TURISTICO -----------------------------------------------------------------------

    /**
     * Obtine el paquete turistico dado el nombre, su fecha inicial y su fecha final
     * @param nombre nombre del paquete turistico
     * @param fechaInicial fecha incial del paquete turistico
     * @param fechaFinal fecha final del paquete turistico
     * @param i index que inicia en 0
     * @return
     */
    public PaqueteTuristico obtenerPaqueteTuristico(String nombre, LocalDate fechaInicial, LocalDate fechaFinal, int i) {
        if (i >= listaPaquetesTuristicos.size()) {
            return null;
        } else {
            PaqueteTuristico paqueteTuristico = listaPaquetesTuristicos.get(i);
            if (paqueteTuristico.getNombre().equals(nombre) && paqueteTuristico.getFechaInicial().equals(fechaInicial) && paqueteTuristico.getFechaFinal().equals(fechaFinal)) {
                return paqueteTuristico;
            } else {
                return obtenerPaqueteTuristico(nombre, fechaInicial, fechaFinal, i + 1);
            }
        }
    }

    /**
     * Actualiza un paquete turistico
     * @param nombre nombre del paquete turistico
     * @param fechaInicial fecha inicial del paquete turistico
     * @param fechaFinal fecha final del paquete turistico
     * @param precio precio del paquete turistico
     * @param cupoMaximo cupo maximo del paquete turistico
     * @throws PaqueteTutisticoNoRegistradoException
     */
    public void actualizarPaqueteTuristico(String nombre, LocalDate fechaInicial, LocalDate fechaFinal, double precio, int cupoMaximo, ArrayList<ServicioAdicional> listaServiciosAdicionales) throws PaqueteTutisticoNoRegistradoException, CampoObligatorioPaqueteTuristicoException, FechaNoPermitidaException {
        PaqueteTuristico paqueteTuristicoEncontrado = obtenerPaqueteTuristico(nombre, fechaInicial, fechaFinal, 0);
        if (paqueteTuristicoEncontrado == null) {
            throw new PaqueteTutisticoNoRegistradoException("El paquete turístico no está registrado");
        } else {
            if (nombre == null || nombre.isEmpty()) {
                throw new CampoObligatorioPaqueteTuristicoException("El nombre del paquete turístico es obligatorio");
            }
            if (fechaInicial == null) {
                throw new CampoObligatorioPaqueteTuristicoException("La fecha inicial del paquete turístico es obligatoria");
            }
            if (fechaFinal == null) {
                throw new CampoObligatorioPaqueteTuristicoException("La fecha final del paquete turístico es obligatoria");
            }
            if (fechaInicial.isAfter(fechaFinal)) {
                throw new FechaNoPermitidaException("La fecha de inicio no puede estar después que la fecha final");
            }
            if (Double.isNaN(precio) || precio == 0.0) {
                throw new CampoObligatorioPaqueteTuristicoException("El precio del paquete turístico es obligatorio");
            }
            if (cupoMaximo == 0) {
                throw new CampoObligatorioPaqueteTuristicoException("El cupo máximo del paquete turístico es obligatorio");
            }
            paqueteTuristicoEncontrado.setPrecio(precio);
            //paqueteTuristicoEncontrado.setCupoMaximo(cupoMaximo);
            paqueteTuristicoEncontrado.setListaServiciosAdicionales(listaServiciosAdicionales);
        }
    }

    /**
     * Elimina un paquete turistico
     * @param nombre nombre del paquete turistico
     * @param fechaInicial fecha inicial del paquete turistico
     * @param fechaFinal fecha final del paquete turistico
     * @throws PaqueteTutisticoNoRegistradoException
     */
    public void eliminarPaqueteTuristico(String nombre, LocalDate fechaInicial, LocalDate fechaFinal) throws PaqueteTutisticoNoRegistradoException {
        PaqueteTuristico paqueteTuristicoEncontrado = obtenerPaqueteTuristico(nombre, fechaInicial, fechaFinal, 0);
        if (paqueteTuristicoEncontrado == null) {
            throw new PaqueteTutisticoNoRegistradoException("El paquete turístico no está registrado");
        } else {
            listaPaquetesTuristicos.remove(paqueteTuristicoEncontrado);
        }
    }

    /**
     * Crea un paquete turistico
     * @param nombre nombre del paquete turistico
     * @param fechaInicial fecha inicial del paquete turistico
     * @param fechaFinal fechia final del paquete turistico
     * @param precio precio del paquete turistico
     * @param cupoMaximo cupo maximo del paquete turistico
     * @param listaServiciosAdicionales servicios adicionales del paquete turistico
     * @param listaDestinos destinos del paquete turistico
     * @throws PaqueteTuristicoYaExistenteException
     * @throws CampoObligatorioPaqueteTuristicoException
     */
    public void crearPaqueteTuristico(String nombre, LocalDate fechaInicial, LocalDate fechaFinal, double precio, int cupoMaximo, ArrayList<ServicioAdicional> listaServiciosAdicionales, ArrayList<Destino> listaDestinos) throws PaqueteTuristicoYaExistenteException, CampoObligatorioPaqueteTuristicoException, FechaNoPermitidaException {
        PaqueteTuristico paqueteTuristicoEncontrado = obtenerPaqueteTuristico(nombre, fechaInicial, fechaFinal, 0);
        if (paqueteTuristicoEncontrado != null) {
            throw new PaqueteTuristicoYaExistenteException("El paquete turístico ya existe");
        } else {
            if (nombre == null || nombre.isEmpty()) {
                throw new CampoObligatorioPaqueteTuristicoException("El nombre del paquete turístico es obligatorio");
            }
            if (fechaInicial == null) {
                throw new CampoObligatorioPaqueteTuristicoException("La fecha inicial del paquete turístico es obligatoria");
            }
            if (fechaFinal == null) {
                throw new CampoObligatorioPaqueteTuristicoException("La fecha final del paquete turístico es obligatoria");
            }
            if (fechaInicial.isAfter(fechaFinal)) {
                throw new FechaNoPermitidaException("La fecha de inicio no puede estar después que la fecha final");
            }
            if (Double.isNaN(precio) || precio == 0.0) {
                throw new CampoObligatorioPaqueteTuristicoException("El precio del paquete turístico es obligatorio");
            }
            if (cupoMaximo == 0) {
                throw new CampoObligatorioPaqueteTuristicoException("El cupo máximo del paquete turístico es obligatorio");
            }
            /*if (listaDestinos == null || listaDestinos.isEmpty()) {
                throw new CampoObligatorioPaqueteTuristicoException("Los destinos del paquete turístico son obligatorios");
            }*/
            PaqueteTuristico paqueteTuristico = PaqueteTuristico.builder()
                    .nombre(nombre)
                    .fechaInicial(fechaInicial)
                    .fechaFinal(fechaFinal)
                    .precio(precio)
                    .cupoMaximo(cupoMaximo)
                    .cupoDisponible(cupoMaximo)
                    .listaServiciosAdicionales(listaServiciosAdicionales)
                    .listaDestinos(listaDestinos)
                    .build();
            listaPaquetesTuristicos.add(paqueteTuristico);
        }
    }



    //CRUD DE GUIAS --------------------------------------------------------------------------------------

    /**
     * Obtiene el guia dado el id de este
     * @param id id del guia
     * @param i index que inicia en 0
     * @return
     */
    public Guia obtenerGuia(String id, int i) {
        if (i >= listaGuiasTuristicos.size()) {
            return null;
        } else {
            Guia guia = listaGuiasTuristicos.get(i);
            if (guia.getId().equals(id)) {
                return guia;
            } else {
                return obtenerGuia(id, i + 1);
            }
        }
    }

    /**
     * Actualiza los datos del guia
     * @param id id del guia
     * @param nombre nombre del guia
     * @param correo correo del guia
     * @param telefono telefono del guia
     * @param residencia residencia del guia
     * @param contrasenia contrasenia del guia
     * @param aniosExperiencia experiencia del guia
     * @param listaLenguajes lenguajes del guia
     * @throws GuiaNoRegistradoException
     */
    public void actulizarGuia(String id, String nombre, String correo, String telefono, String residencia, String contrasenia, int aniosExperiencia, ArrayList<Lenguaje> listaLenguajes) throws GuiaNoRegistradoException, CampoObligatorioGuiaException {
        Guia guiaEcontrado = obtenerGuia(id, 0);
        if (guiaEcontrado == null) {
            throw new GuiaNoRegistradoException("El guía que buscas actualizar no está registrado");
        } else {
            if (id == null || id.isEmpty()) {
                throw new CampoObligatorioGuiaException("El id del guía es obligatorio");
            }
            if (nombre == null || nombre.isEmpty()) {
                throw new CampoObligatorioGuiaException("El nombre del guía es obligatorio");
            }
            if (correo == null || correo.isEmpty()) {
                throw new CampoObligatorioGuiaException("El correo del guía es obligatorio");
            }
            if (telefono == null || telefono.isEmpty()) {
                throw new CampoObligatorioGuiaException("El teléfono del guía es obligatorio");
            }
            if (residencia == null || residencia.isEmpty()) {
                throw new CampoObligatorioGuiaException("La residencia del guía es obligatoria");
            }
            if (contrasenia == null || contrasenia.isEmpty()) {
                throw new CampoObligatorioGuiaException("La contraseña del guía es obligatoria");
            }
            if (listaLenguajes == null || listaLenguajes.isEmpty()) {
                throw new CampoObligatorioGuiaException("El / los lenguajes del guía son obligatorios");
            }
            //Actualizo los datos del guia
            guiaEcontrado.setNombre(nombre);
            guiaEcontrado.setCorreo(correo);
            guiaEcontrado.setTelefono(telefono);
            guiaEcontrado.setResidencia(residencia);
            guiaEcontrado.setContrasenia(contrasenia);
            guiaEcontrado.setAniosExperiencia(aniosExperiencia);
            guiaEcontrado.setListaLenguajes(listaLenguajes);
        }
    }

    /**
     * Elimina un guia turistico
     * @param id id del guia
     * @throws GuiaNoRegistradoException
     */
    public void eliminarGuia(String id) throws GuiaNoRegistradoException {
        Guia guiaEncontrado = obtenerGuia(id, 0);
        if (guiaEncontrado == null) {
            throw new GuiaNoRegistradoException("El guía turistico no está registrado");
        } else {
            listaGuiasTuristicos.remove(guiaEncontrado);
        }
    }

    /**
     * Crea un guia turistico
     * @param id id del guia
     * @param nombre nombre del guia
     * @param correo correo del guia
     * @param telefono telefono del guia
     * @param residencia residencia del guia
     * @param contrasenia contrasenia del guia
     * @param aniosExperiencia experiencia del guia
     * @param listaLenguajes lenguajes del guia
     * @throws GuiaYaExistenteException
     * @throws CampoObligatorioGuiaException
     */
    public void crearGuia(String id, String nombre, String correo, String telefono, String residencia, String contrasenia, int aniosExperiencia, ArrayList<Lenguaje> listaLenguajes, ArrayList<CalificacionGuia> listaCalificaciones) throws GuiaYaExistenteException, CampoObligatorioGuiaException {
        Guia guiaEncontrado = obtenerGuia(id, 0);
        if (guiaEncontrado != null) {
            throw new GuiaYaExistenteException("El guía que deseas registrar ya existe");
        } else {
            if (id == null || id.isEmpty()) {
                throw new CampoObligatorioGuiaException("El id del guía es obligatorio");
            }
            if (nombre == null || nombre.isEmpty()) {
                throw new CampoObligatorioGuiaException("El nombre del guía es obligatorio");
            }
            if (correo == null || correo.isEmpty()) {
                throw new CampoObligatorioGuiaException("El correo del guía es obligatorio");
            }
            if (telefono == null || telefono.isEmpty()) {
                throw new CampoObligatorioGuiaException("El teléfono del guía es obligatorio");
            }
            if (residencia == null || residencia.isEmpty()) {
                throw new CampoObligatorioGuiaException("La residencia del guía es obligatoria");
            }
            if (contrasenia == null || contrasenia.isEmpty()) {
                throw new CampoObligatorioGuiaException("La contraseña del guía es obligatoria");
            }
            if (listaLenguajes == null || listaLenguajes.isEmpty()) {
                throw new CampoObligatorioGuiaException("El / los lenguajes del guía son obligatorios");
            }
            Guia guia = Guia.guiaBuilder()
                    .id(id)
                    .nombre(nombre)
                    .correo(correo)
                    .telefono(telefono)
                    .residencia(residencia)
                    .contrasenia(contrasenia)
                    .aniosExperiencia(aniosExperiencia)
                    .listaLenguajes(listaLenguajes)
                    .calificaciones(listaCalificaciones)
                    .build();
            listaGuiasTuristicos.add(guia);
        }
    }

    //CRUD DE RESERVAS --------------------------------------------------------------------------------------

    /**
     * Obtiene la reserva dado el id de el cliente involucrado
     * @param listaReservas lista de reservas de la agencia de viajes
     * @param id id del cliente involucrado
     * @param i index que inicia en 0
     * @return
     */
    public Reserva obtenerReserva(Cliente clienteInvolucrado, int i) {
        if (i >= listaReservas.size()) {
            return null;
        } else {
            Reserva reserva = listaReservas.get(i);
            if (reserva.getClienteInvolucrado().equals(clienteInvolucrado)) {
                return reserva;
            } else {
                return obtenerReserva(clienteInvolucrado, i + 1);
            }
        }
    }

    /**
     * Se crea una reserva de un paquete turistico
     * @param fechaSolicitud
     * @param fechaReserva
     * @param cantidadPersonas
     * @param clienteSesion
     * @param paqueteTuristicoSeleccionado
     * @param guiaTuristico
     * @throws AtributoIncorrectoException
     * @throws AtributosVaciosException
     * @throws FechaNoPermitidaException
     */
    public void crearReserva(LocalDate fechaSolicitud, LocalDate fechaReserva, int cantidadPersonas, Cliente clienteSesion, PaqueteTuristico paqueteTuristicoSeleccionado, Guia guiaTuristico) throws AtributoIncorrectoException, AtributosVaciosException, FechaNoPermitidaException {
        Reserva reservaEncontrada = obtenerReserva(clienteSesion,0);
        if (fechaSolicitud.equals(null)) {
            throw new AtributosVaciosException("Ocurrió un error con la fecha de solicitud");
        }
        if (fechaReserva.equals(null)) {
            throw new AtributosVaciosException("Ocurrió un error con la fecha de reserva");
        }
        if (fechaSolicitud.isAfter(fechaReserva)) {
            throw new FechaNoPermitidaException("La fecha de este paquete ya expiró, por favor busca otro paquete");
        }
        if (cantidadPersonas == 0 || cantidadPersonas < 0 || cantidadPersonas > paqueteTuristicoSeleccionado.getCupoDisponible()){
            throw new AtributoIncorrectoException("La cantidad de personas es invalida, verifique que el valor ingresado no sea 0 o menor, o también que no sea superior a los cupos disponibles");
        }
        if (clienteSesion == null || clienteSesion.equals("")) {
            throw new AtributosVaciosException("Por favor inicia sesión para que puedas reservar este paquete");
        }
        if (paqueteTuristicoSeleccionado == null || paqueteTuristicoSeleccionado.equals("")){
            throw new AtributosVaciosException("Ocurrió un error con el paquete turístico");
        }
        //Al crear una reserva por defecto esta entra como estado pendiente
        EstadoReserva estadoReserva = EstadoReserva.PENDIENTE;
        Reserva reservaNueva = Reserva.builder()
                .fechaSolicitud(fechaSolicitud)
                .fechaReserva(fechaReserva)
                .cantidadPersonas(cantidadPersonas)
                .estadoReserva(estadoReserva)
                .clienteInvolucrado(clienteSesion)
                .paqueteTuristicoSeleccionado(paqueteTuristicoSeleccionado)
                .guia(guiaTuristico)
                .build();
        listaReservas.add(reservaNueva);
        //Le bajo el cupo disponible al paquete turistico
        paqueteTuristicoSeleccionado.setCupoDisponible(paqueteTuristicoSeleccionado.getCupoDisponible() - cantidadPersonas);
        //Envio el correo al cliente desde un hilo para no parar la ejecucion de la app
        new Thread(new Runnable() {
            @Override
            public void run() {
                EmailUtils.enviarEmail(clienteSesion.getCorreo(), "Hermes Travel Agency. Realizar reserva", "Estimad@ " + clienteSesion.getNombre() + " este correo es para informarle que su reserva fue realizada con éxito para el día " + fechaReserva + " del paquete turístico " + paqueteTuristicoSeleccionado.getNombre() + ".");
            }
        }).start();
    }

    /**
     * Cancela una reserva y le devuelve los cupos al paquete
     * @param reserva
     * @throws EstadoReservaException
     */
    public void cancelarReserva(Reserva reserva) throws EstadoReservaException, AtributosVaciosException {
        if (reserva != null) {
            if (!reserva.getEstadoReserva().equals(EstadoReserva.CANCELADA)) {
                PaqueteTuristico paquete = reserva.getPaqueteTuristicoSeleccionado();
                paquete.setCupoDisponible(paquete.getCupoDisponible() + reserva.getCantidadPersonas());
                reserva.setEstadoReserva(EstadoReserva.CANCELADA);
                //Envio el correo al cliente desde un hilo para no parar la ejecucion de la app
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EmailUtils.enviarEmail(reserva.getClienteInvolucrado().getCorreo(), "Hermes Travel Agency. Cancelación de reserva", "Estimad@ " + reserva.getClienteInvolucrado().getNombre() + " este correo es para informarle que su reserva fue cancelada con éxito para el día " + reserva.getFechaReserva() + " del paquete turístico " + reserva.getPaqueteTuristicoSeleccionado().getNombre() + ".");
                    }
                }).start();
            } else {
                throw new EstadoReservaException("La reserva que tratas de cancelar ya fue cancelada en el pasado");
            }
        } else {
            throw new AtributosVaciosException("Por favor selecciona una reserva");
        }
    }

    /**
     * Confirma una reserva
     * @param reserva
     * @throws EstadoReservaException
     */
    public void confirmarReserva(Reserva reserva) throws EstadoReservaException, AtributosVaciosException {
        if (reserva != null) {
            if (reserva.getEstadoReserva().equals(EstadoReserva.PENDIENTE)) {
                reserva.setEstadoReserva(EstadoReserva.CONFIRMADA);
                //Envio el correo al cliente desde un hilo para no parar la ejecucion de la app
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EmailUtils.enviarEmail(reserva.getClienteInvolucrado().getCorreo(), "Hermes Travel Agency. Confirmación de reserva", "Estimad@ " + reserva.getClienteInvolucrado().getNombre() + " este correo es para informarle que su reserva fue confirmada con éxito para el día " + reserva.getFechaReserva() + " del paquete turístico " + reserva.getPaqueteTuristicoSeleccionado().getNombre() + ".");
                    }
                }).start();
            } else {
                throw new EstadoReservaException("La reserva que tratas de confirmar ya fue confirmada o cancelada en el pasado");
            }
        } else {
            throw new AtributosVaciosException("Por favor selecciona una reserva");
        }
    }

    /**
     * Metodo que lista las reservas de un cliente
     * @param cliente
     * @param listaReservas
     * @param i
     */
    public static void listarTodasLasReservas(Cliente cliente, ArrayList<Reserva> listaReservas, int i) {
        if (i < listaReservas.size()) {
            Reserva reserva = listaReservas.get(i);
            if (reserva.getClienteInvolucrado().equals(cliente)) {
                LOGGER.log(Level.INFO,"Se muestra la lista de reservas del cliente");
            }
            listarTodasLasReservas(cliente, listaReservas, i + 1);
        }
    }

    //CRUD DE CLIENTE --------------------------------------------------------------------------------------

    /**
     * Obtiene el cliente dado el id de este
     * @param id id del cliente
     * @param i index que inicia en 0
     * @return
     */
    public Cliente obtenerCliente(String id, int i) {
        if (i >= listaClientes.size()) {
            return null;
        } else {
            Cliente cliente = listaClientes.get(i);
            if (cliente.getId().equals(id)) {
                return cliente;
            } else {
                return obtenerCliente(id, i + 1);
            }
        }
    }

    /**
     * Retorna un cliente nuevo
     * @param id
     * @param nombre
     * @param correo
     * @param telefono
     * @param residencia
     * @param contrasenia
     * @return
     * @throws ClienteYaExistenteException
     * @throws AtributosVaciosException
     */
    public Cliente crearCliente(String id, String nombre, String correo, String telefono, String residencia, String contrasenia) throws AtributosVaciosException, ClienteYaExistenteException {
        Cliente clienteEncontrado = obtenerCliente(id,0);
        if (clienteEncontrado != null) {
            throw new ClienteYaExistenteException("El cliente ya existe");
        } else {
            if(id == null || id.isBlank()){
                throw new AtributosVaciosException("El id es obligatoria");
            }
            if(nombre == null || nombre.isBlank()){
                throw new AtributosVaciosException("El nombre es obligatorio");
            }
            if(correo == null || correo.isBlank()){
                throw new AtributosVaciosException("El correo es obligatorio");
            }
            if(telefono == null || telefono.isBlank()){
                throw new AtributosVaciosException("El telefono es obligatorio");
            }
            if(residencia == null || residencia.isBlank()){
                throw new AtributosVaciosException("La direccion es obligatoria");
            }
            if(contrasenia == null || contrasenia.isBlank()){
                throw new AtributosVaciosException("La contraseña es obligatoria");
            }
            if(clienteEncontrado != null){
                throw new ClienteYaExistenteException("El cliente no se encuentra registrado");
            }
            Cliente clienteNuevo = Cliente.clienteBuilder()
                    .id(id)
                    .nombre(nombre)
                    .correo(correo)
                    .telefono(telefono)
                    .residencia(residencia)
                    .contrasenia(contrasenia)
                    .build();
            listaClientes.add(clienteNuevo);
            return clienteNuevo;
        }
    }

    /**
     * Actualiza los datos del cliente
     * @param id
     * @param nombre
     * @param correo
     * @param telefono
     * @param residencia
     * @param contrasenia
     * @throws ClienteNoRegistradoException
     * @throws AtributosVaciosException
     */
    public void actualizarCliente(String id, String nombre, String correo, String telefono, String residencia, String contrasenia)throws ClienteNoRegistradoException , AtributosVaciosException{
        Cliente clienteEncontrado = obtenerCliente(id,0);
        if(clienteEncontrado == null){
            throw new ClienteNoRegistradoException("EL cliente no está registrado");
        } else {
            if (id == null || id.isBlank()) {
                throw new AtributosVaciosException("El id es obligatorio");
            }
            if (nombre == null || nombre.isBlank()) {
                throw new AtributosVaciosException("El nombre es obligatorio");
            }
            if (correo == null || correo.isBlank()) {
                throw new AtributosVaciosException("El correo es obligatorio");
            }
            if (telefono == null || telefono.isBlank()) {
                throw new AtributosVaciosException("El telefono es obligatorio");
            }
            if (residencia == null || residencia.isBlank()) {
                throw new AtributosVaciosException("La residencia es obligatoria");
            }
            if (contrasenia == null || contrasenia.isBlank()) {
                throw new AtributosVaciosException("La contraseña es obligatoria");
            }
            clienteEncontrado.setNombre(nombre);
            clienteEncontrado.setCorreo(correo);
            clienteEncontrado.setTelefono(telefono);
            clienteEncontrado.setResidencia(residencia);
            clienteEncontrado.setContrasenia(contrasenia);
        }
    }

    /**
     * Elimina un cliente dado su cedula
     * @param id
     * @throws ClienteNoRegistradoException
     */
    public void eliminarCliente(String id)throws ClienteNoRegistradoException {
        Cliente clientePorEliminar = obtenerCliente(id,0);
        if(clientePorEliminar != null){
            listaClientes.remove(clientePorEliminar);
        }else{
            throw new ClienteNoRegistradoException("El cliente no se encuentra registrado");
        }
    }

    //FUNCIONES APARTE AL CRUD PARA EL MANEJO DE LA AGENCIA ----------------------------------

    //FUNCIONES PARA EL INICIO DE SESION ----------------------------------------------------

    /**
     * Valida que los campos no esten vacios
     * @param cedula del cliente o del administrador
     * @param contrasenia del cliente o del administrador
     * @param admin radioButton de admin
     * @param cliente radioButton de cliente
     * @throws AtributosVaciosException
     */
    public void validarDatosInicioSesion(String cedula, String contrasenia, boolean admin, boolean cliente) throws AtributosVaciosException {
        if (cedula == null || cedula.isBlank()) {
            throw new AtributosVaciosException("Por favor ingrese la cédula");
        }
        if (contrasenia == null || contrasenia.isBlank()) {
            throw new AtributosVaciosException("Por favor ingrese la contraseña");
        }
        if (admin == false && cliente == false) {
            throw new AtributosVaciosException("Por favor seleccione con que tipo de usuario desea ingresar");
        }
    }

    /**
     * Busca el administrador al que le corresponde la cedula y la contrasenia
     * @param cedula del administrador
     * @param contrasenia del administrador
     * @param i index en el cual se empieza a buscar el admin
     * @return Administrador al que le corresponden los datos
     * @throws AdminNoEncontradoException
     */
    public Administrador iniciarSesionAdmin(String cedula, String contrasenia, int i) throws AdminNoEncontradoException {
        if (i >= listaAdministradores.size()) {
            throw new AdminNoEncontradoException("Los datos ingresados no pertenecen a ningún administrador");
        }
        Administrador administrador = listaAdministradores.get(i);
        if (administrador.getId().equals(cedula) && administrador.getContrasenia().equals(contrasenia)) {
            return administrador;
        } else {
            return iniciarSesionAdmin(cedula, contrasenia, i + 1);
        }
    }

    /**
     * Busca el cliente al que le corresponde la cedula y la contrasenia
     * @param cedula del cliente
     * @param contrasenia del cliente
     * @param i index en el cual se empieza a buscar el cliente
     * @return Cliente al que le corresponden los datos
     * @throws ClienteNoRegistradoException
     */
    public Cliente iniciarSesionCliente(String cedula, String contrasenia, int i) throws ClienteNoRegistradoException {
        if (i >= listaClientes.size()) {
            throw new ClienteNoRegistradoException("Los datos ingresados no pertenecen a ningún cliente");
        }
        Cliente cliente = listaClientes.get(i);
        if (cliente.getId().equals(cedula) && cliente.getContrasenia().equals(contrasenia)) {
            return cliente;
        } else {
            return iniciarSesionCliente(cedula, contrasenia, i + 1);
        }
    }

    /**
     * Valida que los campos no esten vacios
     * @param cedula
     * @param admin
     * @param cliente
     * @throws AtributosVaciosException
     */
    public void validarDatosRecuperarContrasenia(String cedula, boolean admin, boolean cliente) throws AtributosVaciosException {
        if (cedula == null || cedula.isBlank()) {
            throw new AtributosVaciosException("Por favor ingrese una cédula");
        }
        if (admin == false && cliente == false) {
            throw new AtributosVaciosException("Por favor seleccione el tipo de usuario para recuperar su contraseña");
        }
    }

    /**
     * Busca el administrador al que le corresponde la cedula para recuperrar su contrasenia
     * @param cedula
     * @param i
     * @return
     * @throws AdminNoEncontradoException
     */
    public Administrador recuperarContraseniaAdmin(String cedula, int i) throws AdminNoEncontradoException {
        if (i >= listaAdministradores.size()) {
            throw new AdminNoEncontradoException("La cédula ingresada no corresponde a ningún administrador");
        }
        Administrador administrador = listaAdministradores.get(i);
        if (administrador.getId().equals(cedula)) {
            return administrador;
        } else {
            return recuperarContraseniaAdmin(cedula, i + 1);
        }
    }

    /**
     * Busca el cliente al que le corresponde la cedula para recuperar su contrasenia
     * @param cedula
     * @param i
     * @return
     * @throws ClienteNoRegistradoException
     */
    public Cliente recuperarContraseniaCliente(String cedula, int i) throws ClienteNoRegistradoException {
        if (i >= listaClientes.size()) {
            throw new ClienteNoRegistradoException("La cédula ingresada no corresponde a ningún cliente");
        }
        Cliente cliente = listaClientes.get(i);
        if (cliente.getId().equals(cedula)) {
            return cliente;
        } else {
            return recuperarContraseniaCliente(cedula, i + 1);
        }
    }

    /**
     * Genera el codigo que se le envia al correo al usuario para que este recupere su contrasenia
     * @param longitud
     * @return
     */
    public String generarCodigo(int longitud) {
        if (longitud == 0) {
            return "";
        } else {
            Random random = new Random();
            int num = random.nextInt(10);
            return num + generarCodigo(longitud - 1);
        }
    }

    /**
     * Envia el correo al administrador para que revise el codigo para el cambio de contrasenia
     * @param administrador
     * @param codigo
     */
    public void enviarCorreoContraseniaAdmin(Administrador administrador, String codigo) {
        //Se pone en un hilo para que no bloque la ejecucion del programa mientras se manda el correo
        new Thread(new Runnable() {
            @Override
            public void run() {
                EmailUtils.enviarEmail(administrador.getCorreo(), "Cambio de contraseña", "El código que debe ingresar es: " + codigo);
            }
        }).start();
    }

    /**
     * Emvia el correo al cliente para que revise el codigo para el cambio de contrasenia
     * @param cliente
     * @param codigo
     */
    public void enviarCorreoContraseniaCliente(Cliente cliente, String codigo) {
        //Se pone en un hilo para que no bloque la ejecucion del programa mientras se manda el correo
        new Thread(new Runnable() {
            @Override
            public void run() {
                EmailUtils.enviarEmail(cliente.getCorreo(), "Cambio de contraseña", "El código que debe ingresar es: " + codigo);
            }
        }).start();
    }

    //FUNCIONES PARA LA VIEW DE RECUPER CONTRASENIAS -------------------------------------------------

    /**
     * Cambia la contrasenia del admin siempre y cuando el codigo sea correcto
     * @param administradorSesion
     * @param codigoCorrecto
     * @param codigoIngresado
     * @param nuevaContrasenia
     * @throws AtributoIncorrectoException
     */
    public void hacerCambioContraseniaAdmin(Administrador administradorSesion, String codigoCorrecto, String codigoIngresado, String nuevaContrasenia) throws AtributoIncorrectoException, AtributosVaciosException {
        if (codigoCorrecto.equals(codigoIngresado)) {
            if (nuevaContrasenia.isBlank() || nuevaContrasenia.equals("")) {
                throw new AtributosVaciosException("Tienes que llenar el campo indicando la nueva contraseña");
            }
            administradorSesion.setContrasenia(nuevaContrasenia);
        } else {
            throw new AtributoIncorrectoException("El código que ingresaste no es el que te hemos enviado");
        }
    }

    /**
     * Cambia la contrasenia del cliente siempre y cuando el codigo sea correcto
     * @param clienteSesion
     * @param codigoCorrecto
     * @param codigoIngresado
     * @param nuevaContrasenia
     * @throws AtributoIncorrectoException
     * @throws AtributosVaciosException
     */
    public void hacerCambioContraseniaCliente(Cliente clienteSesion, String codigoCorrecto, String codigoIngresado, String nuevaContrasenia) throws AtributoIncorrectoException, AtributosVaciosException {
        if (codigoCorrecto.equals(codigoIngresado)) {
            if (nuevaContrasenia.isBlank() || nuevaContrasenia.equals("")) {
                throw new AtributosVaciosException("Tienes que llenar el campo indicando la nueva contraseña");
            }
            clienteSesion.setContrasenia(nuevaContrasenia);
        } else {
            throw new AtributoIncorrectoException("El código que ingresaste no es el que te hemos enviado");
        }
    }

    //FUNCIONES PARA LA VIEW DE GESTIONAR GUIAS ------------------------------------------------------

    /**
     * Verifica si un guia habla el idioma buscado
     * @param guia
     * @param lenguajeBuscado
     * @param i indice que va a iterar sobre la lista de idiomas que habla el guia
     * @return
     */
    public boolean hablaIdiomaGuia(Guia guia, Lenguaje lenguajeBuscado, int i) {
        if (i >= guia.getListaLenguajes().size()) {
            return false;
        }
        Lenguaje lenguaje = guia.getListaLenguajes().get(i);
        if (lenguaje.equals(lenguajeBuscado)) {
            return true;
        } else {
            return hablaIdiomaGuia(guia, lenguajeBuscado, i + 1);
        }
    }

    /**
     * Crea el arraylist de los lenguajes que habla un guia
     * @param espaniol valor booleano de si habla espaniol o no
     * @param ingles valor booleano de si habla ingles o no
     * @param frances valor booleano de si habla frances o no
     * @return
     */
    public ArrayList<Lenguaje> obtenerArrayIdiomasGuia(boolean espaniol, boolean ingles, boolean frances) {
        ArrayList<Lenguaje> lenguajes = new ArrayList<>();
        if (espaniol) {
            lenguajes.add(Lenguaje.ESPANIOL);
        }
        if (ingles) {
            lenguajes.add(Lenguaje.INGLES);
        }
        if (frances) {
            lenguajes.add(Lenguaje.FRANCES);
        }
        return lenguajes;
    }

    //FUNCIONES PARA LA VIEW DE GESTIONAR DESTINOS ------------------------------------------------

    /**
     * Obtiene la primera imagen de la lista de imagenes de un destino
     * @param listaImagenes
     * @return
     * @throws AtributosVaciosException cuando el arrayList esta vacio
     */
    public String obtenerImagenDestino(ArrayList<String> listaImagenes) throws AtributosVaciosException {
        if (listaImagenes == null || listaImagenes.isEmpty()) {
            throw new AtributosVaciosException("Por el momento no hay imágenes disponibles para este destino. Por favor ingresale una imagen");
        } else {
            return listaImagenes.get(0);
        }
    }

    /**
     * Guarda una imagen en la lista de imagenes de un destino especifico
     * @param destino
     * @param rutaImagen
     */
    public void subirImagenDestino(Destino destino, String rutaImagen) throws DestinoNoRegistradoException {
        if (destino == null) {
            throw new DestinoNoRegistradoException("Por favor selecciona un destino en la tabla");
        }
        destino.getListaImagenes().add(rutaImagen);
    }

    /**
     * Elimina una imagen de la lista de imagenes de un destino en especifico
     * @param destino
     * @param rutaImagen
     * @throws DestinoNoRegistradoException
     * @throws AtributosVaciosException
     */
    public void eliminarImagenDestino(Destino destino, String rutaImagen) throws DestinoNoRegistradoException, AtributosVaciosException {
        if (destino == null) {
            throw new DestinoNoRegistradoException("Por favor seleccione un destino en la tabla");
        }
        if (rutaImagen == null || rutaImagen.isBlank()) {
            throw new AtributosVaciosException("El destino no tiene ninguna imagen para eliminarla");
        }
        destino.getListaImagenes().remove(rutaImagen);
    }

    /**
     * Indice de la siguiente imagen en el array de las imagenes del destino
     * Si el indice es el ultimo, se vuelve a 0
     * @param destino
     * @param indiceActual
     * @return
     * @throws DestinoNoRegistradoException
     * @throws AtributosVaciosException
     */
    public int siguienteImagenDestino(Destino destino, int indiceActual) throws DestinoNoRegistradoException, AtributosVaciosException {
        if (destino == null) {
            throw new DestinoNoRegistradoException("Por favor seleccione un destino en la tabla");
        }
        if (destino.getListaImagenes() == null || destino.getListaImagenes().isEmpty()) {
            throw new AtributosVaciosException("Por el momento no hay imágenes disponibles para este destino. Por favor ingresale una imagen");
        }
        int siguienteIndice = (indiceActual + 1) % destino.getListaImagenes().size();
        return siguienteIndice;
    }

    /**
     * Indice de la imagen anterior en el array de las imagenes del destino
     * Si el indice es el 0, se va al ultimo
     * @param destino
     * @param indiceActual
     * @return
     * @throws DestinoNoRegistradoException
     * @throws AtributosVaciosException
     */
    public int anteriorImagenDestino(Destino destino, int indiceActual) throws DestinoNoRegistradoException, AtributosVaciosException {
        if (destino == null) {
            throw new DestinoNoRegistradoException("Por favor seleccione un destino en la tabla");
        }
        if (destino.getListaImagenes() == null || destino.getListaImagenes().isEmpty()) {
            throw new AtributosVaciosException("Por el momento no hay imágenes disponibles para este destino. Por favor ingresale una imagen");
        }
        int indiceAnterior = (indiceActual - 1 + destino.getListaImagenes().size()) % destino.getListaImagenes().size();
        return indiceAnterior;
    }

    //FUNCIONES PARA LA VIEW DE GESTIONAR PAQUETES ----------------------------------------------------

    /**
     * Obtiene la lista de destinos de un paquete en especifico para validar si esta vacio o no
     * @param paqueteTuristico
     * @return
     * @throws AtributosVaciosException
     */
    public ArrayList<Destino> obtenerDestinosPaquete(PaqueteTuristico paqueteTuristico) throws AtributosVaciosException, PaqueteTutisticoNoRegistradoException {
        if (paqueteTuristico == null) {
            throw new PaqueteTutisticoNoRegistradoException("Seleccione un paquete turistico en la tabla para poder ver los destinos de este paquete");
        }
        if (paqueteTuristico.getListaDestinos() == null || paqueteTuristico.getListaDestinos().isEmpty()) {
            throw new AtributosVaciosException("Por el momento no hay destinos disponibles para este paquete. Por favor seleccione uno de los destinos desponibles y agregelo al paquete.");
        } else {
            return paqueteTuristico.getListaDestinos();
        }
    }

    /**
     * Verifica si un paqueteTuristico tiene el servicio adicional buscado
     * @param paqueteTuristico
     * @param servicioAdicionalBuscado
     * @param i indice que va a iterar sobre la lista de serviciosAdicionales del paquete
     * @return
     */
    public boolean tieneServicioAdicionalPaquete(PaqueteTuristico paqueteTuristico, ServicioAdicional servicioAdicionalBuscado, int i) {
        if (i >= paqueteTuristico.getListaServiciosAdicionales().size()) {
            return false;
        }
        ServicioAdicional servicioAdicional = paqueteTuristico.getListaServiciosAdicionales().get(i);
        if (servicioAdicional.equals(servicioAdicionalBuscado)) {
            return true;
        } else {
            return tieneServicioAdicionalPaquete(paqueteTuristico, servicioAdicionalBuscado, i + 1);
        }
    }

    /**
     * Crea un arrayList con los servicios adicionales del paquete
     * @param transporte
     * @param alimentos
     * @param seguros
     * @param recreacion
     * @param bar
     * @return
     */
    public ArrayList<ServicioAdicional> obtenerServiciosAdicionalesPaquete(boolean transporte, boolean alimentos, boolean seguros, boolean recreacion, boolean bar) {
        ArrayList<ServicioAdicional> serviciosAdicionales = new ArrayList<>();
        if (transporte) {
            serviciosAdicionales.add(ServicioAdicional.TRANSPORTE);
        }
        if (alimentos) {
            serviciosAdicionales.add(ServicioAdicional.ALIMENTOS);
        }
        if (seguros) {
            serviciosAdicionales.add(ServicioAdicional.SEGUROS);
        }
        if (recreacion) {
            serviciosAdicionales.add(ServicioAdicional.RECREACION);
        }
        if (bar) {
            serviciosAdicionales.add(ServicioAdicional.BAR);
        }
        return serviciosAdicionales;
    }

    /**
     * Guarda el destino en la lista de destinos del paquete turistico especifico
     * @param paqueteTuristico
     * @param destino
     * @throws PaqueteTutisticoNoRegistradoException
     */
    public void agregarDestinoPaquete(PaqueteTuristico paqueteTuristico, Destino destino) throws PaqueteTutisticoNoRegistradoException, DestinoNoRegistradoException, DestinoYaExistenteException {
        if (paqueteTuristico == null) {
            throw new PaqueteTutisticoNoRegistradoException("Por favor seleccione un paquete en la tabla");
        }
        if (destino == null) {
            throw new DestinoNoRegistradoException("Por favor seleccione un destino en la tabla");
        }
        if (!verificarDestinoEnPaquete(paqueteTuristico, destino, 0)) {
            paqueteTuristico.getListaDestinos().add(destino);
        } else {
            throw new DestinoYaExistenteException("El destino ya se encuentra en el paquete");
        }
    }

    /**
     * Funcion auxiliar que verifica si un destino esta en la lista de destinos de un paquete
     * @param paqueteTuristico
     * @param destino
     * @param indice
     * @return
     */
    private boolean verificarDestinoEnPaquete(PaqueteTuristico paqueteTuristico, Destino destino, int indice) {
        if (indice >= paqueteTuristico.getListaDestinos().size()) {
            return false;
        }
        if (paqueteTuristico.getListaDestinos().get(indice).getNombre().equals(destino.getNombre()) && paqueteTuristico.getListaDestinos().get(indice).getCiudad().equals(destino.getCiudad())) {
            return true;
        } else {
            return verificarDestinoEnPaquete(paqueteTuristico, destino, indice + 1);
        }
    }

    //FUNCIONES PARA LA VIEW DE INFORMACION DEL DESTINO ----------------------------------------------

    /**
     * Da la las calificaciones obtenidas de un destino dado
     * @param destinoSeleccion
     * @param i index para la llamada recursiva
     * @return
     * @throws AtributosVaciosException
     */
    public ArrayList<CalificacionDestino> obtenerCalificacionesDestino(Destino destinoSeleccion, int i) throws AtributosVaciosException {
        if (i >= listaDestinos.size()) {
            throw new AtributosVaciosException("El destino por el momento no cuenta con calificaciones");
        } else {
            Destino destino = listaDestinos.get(i);
            if (destino.getNombre().equals(destinoSeleccion.getNombre()) && destino.getCiudad().equals(destinoSeleccion.getCiudad())) {
                return destino.getCalificaciones();
            }
            return obtenerCalificacionesDestino(destinoSeleccion, i + 1);
        }
    }

    //FUNCIONES PARA LA VIEW DE HACER RESERVAS ----------------------------------------------------

    /**
     * Calcula el precio de la reserva teniendo en cuenta la cantidad de personas y el precio del paquete
     * @param paqueteSeleccion
     * @param cantidadPersonas
     * @return
     * @throws AtributoIncorrectoException
     */
    public double calcularPrecioReserva(PaqueteTuristico paqueteSeleccion, int cantidadPersonas) throws AtributoIncorrectoException {
        if (cantidadPersonas <= 0) {
            throw new AtributoIncorrectoException("Por favor ingresa la cantidad de personas para calcular el precio de la reserva");
        } else {
            return paqueteSeleccion.getPrecio() * cantidadPersonas;
        }
    }

    //FUNCIONES PARA LA VIEW DE RESERVAS ---------------------------------------------------------

    /**
     * Obtiene las reservas pendientes de un cliente, es decir, las reservas que estan por llegar, no las pasadas
     * @param clienteSesion
     * @param fechaActual
     * @param listaReservasCliente
     * @param i
     * @return
     */
    public ArrayList<Reserva> obtenerReservasPendientesCliente(Cliente clienteSesion, LocalDate fechaActual, ArrayList<Reserva> listaReservasCliente, int i) {
        if (i >= listaReservas.size()) {
            return listaReservasCliente;
        } else {
            Reserva reserva = listaReservas.get(i);
            if (reserva.getClienteInvolucrado().getId().equals(clienteSesion.getId()) && fechaActual.isBefore(reserva.getPaqueteTuristicoSeleccionado().getFechaInicial())) {
                listaReservasCliente.add(reserva);
            }
            return obtenerReservasPendientesCliente(clienteSesion, fechaActual, listaReservasCliente, i + 1);
        }
    }

    /**
     * Revisa si una reserva tiene o no un guia
     * @param reserva
     * @return
     * @throws AtributosVaciosException
     */
    public boolean reservaContieneGuia(Reserva reserva) throws AtributosVaciosException {
        if (reserva != null) {
            if (reserva.getGuia() != null) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new AtributosVaciosException("Por favor seleccione una reserva para ver el guía de esta misma");
        }
    }

    //FUNCIONES PARA LA VIEW DE CALIFICAR DESTINOS ----------------------------------------------------

    /**
     * Obtienen la lista de destinos ya visitados por un cliente
     * @param clienteSesion
     * @param fechaActual
     * @param listaDestinosVisitadosCliente
     * @param i
     * @return
     */
    public ArrayList<Destino> obtenerDestinosVisitadosCliente(Cliente clienteSesion, LocalDate fechaActual, ArrayList<Destino> listaDestinosVisitadosCliente, int i) {
        if (i >= listaReservas.size()) {
            return listaDestinosVisitadosCliente;
        } else {
            Reserva reserva = listaReservas.get(i);
            //Es la fecha de cuando se termina el paquete turistico
            LocalDate fechaFinalReserva = reserva.getPaqueteTuristicoSeleccionado().getFechaFinal();
            if (reserva.getClienteInvolucrado().getId().equals(clienteSesion.getId()) && fechaActual.isAfter(fechaFinalReserva)) {
                listaDestinosVisitadosCliente.addAll(reserva.getPaqueteTuristicoSeleccionado().getListaDestinos());
            }
            return obtenerDestinosVisitadosCliente(clienteSesion, fechaActual, listaDestinosVisitadosCliente, i + 1);
        }
    }

    /**
     * Califica un destino siempre y cuando no este calificado
     * @param clienteSesion
     * @param destinoSeleccion
     * @param calificacion
     * @param comentario
     * @throws AtributosVaciosException
     * @throws CalificacionException
     * @throws AtributoIncorrectoException
     */
    public void calificarDestino(Cliente clienteSesion, Destino destinoSeleccion, double calificacion, String comentario) throws AtributosVaciosException, CalificacionException, AtributoIncorrectoException {
        if (destinoSeleccion != null) {
            if (!destinoYaCalificado(clienteSesion, destinoSeleccion, 0)) {
                if (calificacion < 0.0 || calificacion > 5.0) {
                    throw new AtributoIncorrectoException("Por favor ingrese una calificación entre 0.0 y 5.0");
                }
                if (comentario.equals("") || comentario.isBlank()) {
                    throw new AtributoIncorrectoException("Por favor ingrese un comentario para enviar la calificación");
                }
                CalificacionDestino calificacionDestino = CalificacionDestino.builder()
                        .calificacion(calificacion)
                        .comentario(comentario)
                        .clienteAsociado(clienteSesion)
                        .destinoAsociado(destinoSeleccion)
                        .build();
                destinoSeleccion.getCalificaciones().add(calificacionDestino);
            } else {
                throw new CalificacionException("El destino que tratas de calificar, ya lo calificaste");
            }
        } else {
            throw new AtributosVaciosException("Por favor seleccione un destino en la tabla");
        }
    }

    /**
     * Verifica si un destino ya fue calificado por un cliente
     * @param clienteSesion
     * @param destinoSeleccion
     * @param i
     * @return
     */
    private boolean destinoYaCalificado(Cliente clienteSesion, Destino destinoSeleccion, int i) {
        if (i >= destinoSeleccion.getCalificaciones().size()) {
            return false;
        } else {
            Cliente clienteAux = destinoSeleccion.getCalificaciones().get(i).getClienteAsociado();
            if (clienteAux.getId().equals(clienteSesion.getId())) {
                return true;
            }
            return destinoYaCalificado(clienteSesion, destinoSeleccion, i + 1);
        }
    }

    //FUNCIONES PARA VIEW DE CALIFICAR GUIAS -------------------------------------------------------

    /**
     * Obtiene la lista de guias ya contratados por el cliente
     * @param clienteSesion
     * @param fechaActual
     * @param listaGuiasCliente
     * @param i
     * @return
     */
    public ArrayList<Guia> obtenerGuiasCliente(Cliente clienteSesion, LocalDate fechaActual, ArrayList<Guia> listaGuiasCliente, int i) {
        if (i >= listaReservas.size()) {
            return listaGuiasCliente;
        } else {
            Reserva reserva = listaReservas.get(i);
            //Fecha de cuando se termina el paquete turistico
            LocalDate fechaFinalReserva = reserva.getPaqueteTuristicoSeleccionado().getFechaFinal();
            if (reserva.getClienteInvolucrado().getId().equals(clienteSesion.getId()) && fechaActual.isAfter(fechaFinalReserva)) {
                if (reserva.getGuia() != null) {
                    listaGuiasCliente.add(reserva.getGuia());
                }
            }
        }
        return obtenerGuiasCliente(clienteSesion, fechaActual, listaGuiasCliente, i + 1);
    }

    /**
     * Califica un guia siempre y cuando no este calificado por el cliente
     * @param clienteSesion
     * @param guiaSeleccion
     * @param calificacion
     * @param comentario
     * @throws AtributosVaciosException
     * @throws CalificacionException
     * @throws AtributoIncorrectoException
     */
    public void calificarGuia(Cliente clienteSesion, Guia guiaSeleccion, double calificacion, String comentario) throws AtributosVaciosException, CalificacionException, AtributoIncorrectoException {
        if (guiaSeleccion != null) {
            if (!guiaYaCalificado(clienteSesion, guiaSeleccion, 0)) {
                if (calificacion < 0.0 || calificacion > 5.0) {
                    throw new AtributoIncorrectoException("Por favor ingrese una calificación entre 0.0 y 5.0");
                }
                if (comentario.equals("") || comentario.isBlank()) {
                    throw new AtributoIncorrectoException("Por favor ingrese un comentario para enviar la calificación");
                }
                CalificacionGuia calificacionGuia = CalificacionGuia.builder()
                        .calificacion(calificacion)
                        .comentario(comentario)
                        .clienteAsociado(clienteSesion)
                        .guiaAsociado(guiaSeleccion)
                        .build();
                guiaSeleccion.getCalificaciones().add(calificacionGuia);
            } else {
                throw new CalificacionException("EL guía que tratas de calificar ya lo calificaste en el pasado");
            }
        } else {
            throw new AtributosVaciosException("Por favor seleccione un guía en la tabla");
        }
    }

    /**
     * Verifica si un guia ya fue calificado por un cliente
     * @param clienteSesion
     * @param guiaSeleccion
     * @param i
     * @return
     */
    private boolean guiaYaCalificado(Cliente clienteSesion, Guia guiaSeleccion, int i) {
        if (i >= guiaSeleccion.getCalificaciones().size()) {
            return false;
        } else {
            Cliente clienteAux = guiaSeleccion.getCalificaciones().get(i).getClienteAsociado();
            if (clienteAux.getId().equals(clienteSesion.getId())) {
                return true;
            }
            return guiaYaCalificado(clienteSesion, guiaSeleccion, i + 1);
        }
    }

    //FUNCIONES PARA LA VIEW DE BUSCADOR DE DESTINOS ------------------------------------------------

    /**
     * Obtiene la lista de destinos de un tipo en especifico
     * @param tipoDestino
     * @param listaDestinosTipo
     * @param i
     * @return
     */
    public ArrayList<Destino> obtenerDestinosTipo(TipoDestino tipoDestino, ArrayList<Destino> listaDestinosTipo, int i) {
        if (i >= listaDestinos.size()) {
            return listaDestinosTipo;
        } else {
            Destino destino = listaDestinos.get(i);
            if (destino.getTipoDestino().equals(tipoDestino)) {
                listaDestinosTipo.add(destino);
            }
            return obtenerDestinosTipo(tipoDestino, listaDestinosTipo, i + 1);
        }
    }

    /**
     * Da una lista de destinos recomendados a un cliente especifico dependiendo el tipo de destinos que mas visita
     * Si no ha visitado ningun destino le muestra la lista de destinos en general
     * @param cliente
     * @param contPlaya
     * @param contBosque
     * @param contAventura
     * @param contCiudad
     * @param i
     * @return
     */
    public ArrayList<Destino> recomendarDestinosCLiente(Cliente cliente, int contPlaya, int contBosque, int contAventura, int contCiudad, int i) throws ClienteNoRegistradoException {
        if (cliente != null) {
            if (i >= listaReservas.size()) {
                if (contPlaya == 0 && contBosque == 0 && contAventura == 0 && contCiudad == 0) {
                    return listaDestinos;
                } else {
                    TipoDestino tipoDestino = obtenerTipoDestinoMayor(contPlaya, contBosque, contAventura, contCiudad);
                    ArrayList<Destino> listaDestinosRecomendado = new ArrayList<>();
                    return obtenerDestinosTipo(tipoDestino, listaDestinosRecomendado, 0);
                }
            } else {
                Reserva reserva = listaReservas.get(i);
                Cliente clienteAsociado = listaReservas.get(i).getClienteInvolucrado();
                if (clienteAsociado.getId().equals(cliente.getId())) {
                    contPlaya = contPlaya + contarTipoDestino(reserva.getPaqueteTuristicoSeleccionado().getListaDestinos(), TipoDestino.PLAYA, 0, 0);
                    contBosque = contBosque + contarTipoDestino(reserva.getPaqueteTuristicoSeleccionado().getListaDestinos(), TipoDestino.BOSQUE, 0, 0);
                    contAventura = contAventura + contarTipoDestino(reserva.getPaqueteTuristicoSeleccionado().getListaDestinos(), TipoDestino.AVENTURA, 0, 0);
                    contCiudad = contCiudad + contarTipoDestino(reserva.getPaqueteTuristicoSeleccionado().getListaDestinos(), TipoDestino.CIUDAD, 0, 0);
                }
                return recomendarDestinosCLiente(cliente, contPlaya, contBosque, contAventura, contCiudad, i + 1);
            }
        } else {
            throw new ClienteNoRegistradoException("Por favor inicia sesión para que puedas ver nuestrar recomendaciones");
        }
    }

    /**
     * Me retorna el tipo de destino mayor
     * @param contPlaya
     * @param contBosque
     * @param contAventura
     * @param contCiudad
     * @return
     */
    private TipoDestino obtenerTipoDestinoMayor(int contPlaya, int contBosque, int contAventura, int contCiudad) {
        TipoDestino tipoDestinoMayor = null;
        int contMayor = 0;
        if (contPlaya > contMayor) {
            tipoDestinoMayor = TipoDestino.PLAYA;
            contMayor = contPlaya;
        }
        if (contBosque > contMayor) {
            tipoDestinoMayor = TipoDestino.BOSQUE;
            contMayor = contBosque;
        }
        if (contAventura > contMayor) {
            tipoDestinoMayor = TipoDestino.AVENTURA;
            contMayor = contAventura;
        }
        if (contCiudad > contMayor) {
            tipoDestinoMayor = TipoDestino.CIUDAD;
            contMayor = contCiudad;
        }
        return tipoDestinoMayor;
    }

    /**
     * Cuenta cuantos destinos de un tipo hay en una lista de destinos pasada como parametro
     * @param listaDestinosCliente
     * @param tipoDestino
     * @param cont
     * @param i
     * @return
     */
    private int contarTipoDestino(ArrayList<Destino> listaDestinosCliente, TipoDestino tipoDestino, int cont, int i) {
        if (i >= listaDestinosCliente.size()) {
            return cont;
        } else {
            Destino destino = listaDestinosCliente.get(i);
            if (destino.getTipoDestino().equals(tipoDestino)) {
                cont = cont + 1;
            }
            return contarTipoDestino(listaDestinosCliente, tipoDestino, cont, i + 1);
        }
    }

    //FUNCIONES PARA LA VIEW DE BUSCADOR DE PAQUETES -------------------------------------------------

    /**
     * Obtiene la lista de paquetes que contienen destinos de un tipo en especifico
     * @param tipoDestino
     * @param listaPaquetesTipo
     * @param i
     * @return
     */
    public ArrayList<PaqueteTuristico> obtenerPaquetesTipo(TipoDestino tipoDestino, ArrayList<PaqueteTuristico> listaPaquetesTipo, int i) {
        if (i >= listaPaquetesTuristicos.size()) {
            return listaPaquetesTipo;
        } else {
            PaqueteTuristico paquete = listaPaquetesTuristicos.get(i);
            if (paqueteContieneTipo(paquete, tipoDestino, 0)) {
                listaPaquetesTipo.add(paquete);
            }
            return obtenerPaquetesTipo(tipoDestino, listaPaquetesTipo, i + 1);
        }
    }

    /**
     * Verifica si un paquete tiene un destino de un tipo en especifico
     * @param paquete
     * @param tipoDestino
     * @param i
     * @return
     */
    private boolean paqueteContieneTipo(PaqueteTuristico paquete, TipoDestino tipoDestino, int i) {
        if (i >= paquete.getListaDestinos().size()) {
            return false;
        } else {
            Destino destino = paquete.getListaDestinos().get(i);
            if (destino.getTipoDestino().equals(tipoDestino)) {
                return true;
            }
        }
        return paqueteContieneTipo(paquete, tipoDestino, i + 1);
    }

    /**
     * Retorna una lista de paquetes recomendada dependiendo de el tipo de destinos que mas visita
     * Es decir, se recomiendan paquetes que tengan el tipo de destino que mas le gusta al usuario
     * @param cliente
     * @param contPlaya
     * @param contBosque
     * @param contAventura
     * @param contCiudad
     * @param i
     * @return
     * @throws ClienteNoRegistradoException
     */
    public ArrayList<PaqueteTuristico> recomendarPaquetesCliente(Cliente cliente, int contPlaya, int contBosque, int contAventura, int contCiudad, int i) throws ClienteNoRegistradoException {
        if (cliente != null) {
            if (i >= listaReservas.size()) {
                if (contPlaya == 0 && contBosque == 0 && contAventura == 0 && contCiudad == 0) {
                    return listaPaquetesTuristicos;
                } else {
                    TipoDestino tipoDestino = obtenerTipoDestinoMayor(contPlaya, contBosque, contAventura, contCiudad);
                    ArrayList<PaqueteTuristico> listaPaquetesRecomendado = new ArrayList<>();
                    return obtenerPaquetesTipo(tipoDestino, listaPaquetesRecomendado, 0);
                }
            } else {
                Reserva reserva = listaReservas.get(i);
                Cliente clienteAsociado = listaReservas.get(i).getClienteInvolucrado();
                if (clienteAsociado.getId().equals(cliente.getId())) {
                    contPlaya = contPlaya + contarTipoDestino(reserva.getPaqueteTuristicoSeleccionado().getListaDestinos(), TipoDestino.PLAYA, 0, 0);
                    contBosque = contBosque + contarTipoDestino(reserva.getPaqueteTuristicoSeleccionado().getListaDestinos(), TipoDestino.BOSQUE, 0, 0);
                    contAventura = contAventura + contarTipoDestino(reserva.getPaqueteTuristicoSeleccionado().getListaDestinos(), TipoDestino.AVENTURA, 0, 0);
                    contCiudad = contCiudad + contarTipoDestino(reserva.getPaqueteTuristicoSeleccionado().getListaDestinos(), TipoDestino.CIUDAD, 0, 0);
                }
                return recomendarPaquetesCliente(cliente, contPlaya, contBosque, contAventura, contCiudad, i + 1);
            }
        } else {
            throw new ClienteNoRegistradoException("Por favor inicia sesión para que puedas ver nuestrar recomendaciones");
        }
    }

}