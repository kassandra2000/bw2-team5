package kassandrafalsitta.bw2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "utenti")

public class Utente {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    private String username;
    private String email;
    private String password;
    private String nome;
    private String cognome;
    private String avatar;

}
