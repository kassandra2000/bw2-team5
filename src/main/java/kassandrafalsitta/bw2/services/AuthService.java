package kassandrafalsitta.bw2.services;
import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.entities.Utente;
import kassandrafalsitta.bw2.exceptions.UnauthorizedException;
import kassandrafalsitta.bw2.payloads.ClientiLoginDTO;
import kassandrafalsitta.bw2.payloads.UtenteLoginDTO;
import kassandrafalsitta.bw2.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private ClientiService clientiService;
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(ClientiLoginDTO body) {
        Cliente found = this.clientiService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.createToken(found);
        } else {
            throw new UnauthorizedException("Credenziali errate!");
        }
    }

    public String checkCredentialsAndGenerateToken(UtenteLoginDTO body) {
        Utente found = this.utenteService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.createToken(found);
        } else {
            throw new UnauthorizedException("Credenziali errate!");
        }
    }
}
