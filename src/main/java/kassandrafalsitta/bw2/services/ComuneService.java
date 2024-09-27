package kassandrafalsitta.bw2.services;

import kassandrafalsitta.bw2.entities.Comune;
import kassandrafalsitta.bw2.entities.Provincia;
import kassandrafalsitta.bw2.payloads.ComuneDTO;
import kassandrafalsitta.bw2.repositories.ComuneRepository;
import kassandrafalsitta.bw2.repositories.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ComuneService {

    @Autowired
    private ComuneRepository comuneRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    // Metodo esistente per importare Comuni da CSV
    public void importComuniCSV(String filePath) {
        List<Comune> comuni = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String riga;
            br.readLine(); // Salta la prima riga che contiene i nomi dei campi

            while ((riga = br.readLine()) != null) {
                String[] casella = riga.split(";");

                Comune comune = new Comune();
                comune.setNome(casella[2]);

                // Se per caso ci sono province doppione, associa alla prima
                List<Provincia> province = provinciaRepository.findAllByNome(casella[3]);
                if (province.size() == 1) {
                    comune.setProvincia(province.get(0));
                } else if (province.size() > 1) {
                    comune.setProvincia(province.get(0));
                }
                comuni.add(comune);
            }

            comuneRepository.saveAll(comuni);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Metodo esistente per controllare se la tabella dei comuni è vuota
    public boolean isComuniTableEmpty() {
        return comuneRepository.count() == 0;
    }

    // Recupera tutti i comuni
    public List<Comune> findAll() {
        return comuneRepository.findAll();
    }

    // Recupera un comune tramite ID
    public Comune findById(UUID id) {
        return comuneRepository.findById(id).orElseThrow(() -> new RuntimeException("Comune non trovato"));
    }

    // Crea un nuovo comune utilizzando ComuneDTO
    public Comune saveComune(ComuneDTO comuneDTO) {
        Provincia provincia = provinciaRepository.findById(UUID.fromString(comuneDTO.provinciaId()))
                .orElseThrow(() -> new RuntimeException("Provincia non trovata"));

        Comune comune = new Comune();
        comune.setNome(comuneDTO.nome());
        comune.setProvincia(provincia);
        return comuneRepository.save(comune);
    }

    // Aggiorna un comune tramite ID utilizzando ComuneDTO
    public Comune findByIdAndUpdate(UUID id, ComuneDTO comuneDTO) {
        Comune comune = findById(id);
        Provincia provincia = provinciaRepository.findById(UUID.fromString(comuneDTO.provinciaId()))
                .orElseThrow(() -> new RuntimeException("Provincia non trovata"));

        comune.setNome(comuneDTO.nome());
        comune.setProvincia(provincia);
        return comuneRepository.save(comune);
    }

    // Elimina un comune tramite ID
    public void findByIdAndDelete(UUID id) {
        Comune comune = findById(id);
        comuneRepository.delete(comune);
    }
}
