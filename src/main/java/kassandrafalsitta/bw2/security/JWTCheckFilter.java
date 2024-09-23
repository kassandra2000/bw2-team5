package kassandrafalsitta.bw2.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import kassandrafalsitta.bw2.exceptions.UnauthorizedException;


import java.io.IOException;
import java.util.UUID;

@Component
public class JWTCheckFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private ClienteService utenteService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Per favore inserisci correttamente il token nell'Authorization Header");

        String accessToken = authHeader.substring(7);
        System.out.println("ACCESS TOKEN " + accessToken);
        jwtTools.verifyToken(accessToken);
        String id = jwtTools.extractIdFromToken(accessToken);
        Cliente currentCliente = this.utenteService.findById(UUID.fromString(id));

       Authentication authentication = new UsernamePasswordAuthenticationToken(currentCliente, null, currentClienti.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); // <-- Associo l'utente autenticato (Autentication) al Context

        filterChain.doFilter(request, response);
        // 5. Se il token non è ok --> 401
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
