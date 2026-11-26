package com.clubnautico.clubnauticovaldivia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity 
@Table(name = "usuarios")
public class Usuario {
    @Id 
    @NotBlank(message = "El rut no puede estar vacío")
    @Size(min = 10, max = 11, message = "El rut debe contener entre 10 y 11 caracteres")
    @Column(unique = true, length = 12, nullable = false)
    private String rut;

    @NotNull(message = "El nombre no puede ser nulo")
    @Column(length = 50, nullable = false)
    private String nombre;

    @NotNull(message = "El teléfono no puede ser nulo")
    private Integer telefono;

    @NotBlank(message = "El correo no puede estar vacío")
    @Size(min = 10, max = 100, message = "El correo debe contener entre 10 y 100 caracteres")
    @Column(unique = true, length = 100, nullable = false)
    private String correo;

    @JsonIgnore
    @ToString.Exclude
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Column(length = 255, nullable = false)
    private String contraseña;

    //Relaciones

    //Muchos usuarios pueden tener un rol
    @NotNull(message = "El rol no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    //Un usuario puede tener muchos espacios
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "usuario")
    private List<Espacio> espacios;

    //Un usuario puede tener muchas boletas
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "usuario")
    private List<Boleta> boletas;

    //Un usuario puede tener muchos documentos
    @OneToMany(mappedBy = "usuario")
    @ToString.Exclude
    private List<Documento> documentos;
}
