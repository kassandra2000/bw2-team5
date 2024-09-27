package kassandrafalsitta.bw2.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ProvinciaDTO(

        @NotEmpty(message = "Il nome della provincia è obbligatorio")
        @Size(min = 2, max = 40, message = "Il nome della provincia deve essere compreso tra 2 e 40 caratteri")
        String nome,

        @NotEmpty(message = "La sigla della provincia è obbligatoria")
        @Size(min = 2, max = 2, message = "La sigla della provincia deve essere di 2 caratteri")
        String sigla
) {

}
