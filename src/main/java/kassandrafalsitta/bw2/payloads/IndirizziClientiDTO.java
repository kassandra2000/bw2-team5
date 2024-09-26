package kassandrafalsitta.bw2.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record IndirizziClientiDTO(@NotEmpty(message = "L'UUID della sede legale è obbligatoria")
                                  @Size(min = 36, max = 36, message = "L'UUID della sede legale  deve avere 36 caratteri")
                                  String sedeLegaleId,
                                  @NotEmpty(message = "L'UUID della sede operativa è obbligatoria")
                                  @Size(min = 36, max = 36, message = "L'UUID della sede operativa  deve avere 36 caratteri")
                                  String sedeOperativaId) {
}
