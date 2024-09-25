package kassandrafalsitta.bw2.services;

import kassandrafalsitta.bw2.entities.Comune;
import kassandrafalsitta.bw2.entities.Provincia;
import kassandrafalsitta.bw2.repositories.ComuneRepository;
import kassandrafalsitta.bw2.repositories.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComuneService {

    @Autowired
    private ComuneRepository comuneRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    public void importComuniCSV(String filePath) {
        List<Comune> comuni = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String riga;
            br.readLine(); // Salta la prima riga che contiene e i nomi dei campi

            while ((riga = br.readLine()) != null) {
                String[] casella = riga.split(";");

                Comune comune = new Comune();
                comune.setNome(casella[2]); // Imposta il nome del comune

                // utilizzando il nome della provincia nel file dei comuni, andiamo a cercare se esiste
                //una provincia con lo stesso nome nella repository delle province
                Optional<Provincia> provinciaAssociazione = provinciaRepository.findByNome(casella[3]);
                // se troviamo un risultato assegnamo la provincia trovata da provinciaRepository all'attributo
                //provincia dell'istanza Comune
                provinciaAssociazione.ifPresent(comune::setProvincia);

                comuni.add(comune);
            }

            comuneRepository.saveAll(comuni); // Salva tutti i comuni
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isComuniTableEmpty() {
        return comuneRepository.count() == 0;
    }

}

/*public void importComuniCSV(String filePath) {
    try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
        String[] riga;
        List<Comune> comuni = new ArrayList<>();
        reader.readNext(); // Salta la prima riga del file
        while ((riga = reader.readNext()) != null) {
            Comune comune = new Comune();
            comune.setNome(riga[2]);

            // utilizzando il nome della provincia nel file dei comuni, andiamo a cercare se esiste
            //una provincia con lo stesso nome nella repository delle province
            Optional<Provincia> provinciaAssociazione = provinciaRepository.findByNome(riga[3]);

            // se troviamo un risultato assegnamo la provincia trovata da provinciaRepository all'attributo
            //provincia dell'istanza Comune
            provinciaAssociazione.ifPresent(comune::setProvincia);

            comuni.add(comune);
        }
        comuneRepository.saveAll(comuni);
    } catch (Exception e) {
        e.printStackTrace();
 */
