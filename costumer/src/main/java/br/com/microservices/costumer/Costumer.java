package br.com.microservices.costumer;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.Formula;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "costumer")
public class Costumer {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "costumer_id_sequence"
    )
    @SequenceGenerator(
            name = "costumer_id_sequence",
            sequenceName = "costumer_id_sequence"
    )
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Formula("concat(firstname,' ', lastname)")
    private String fullname;

    @Email
    @Column(name = "email", unique = true)
    private String email;
}
