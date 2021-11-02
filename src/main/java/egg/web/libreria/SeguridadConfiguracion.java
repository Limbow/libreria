package egg.web.libreria;

import egg.web.libreria.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadConfiguracion extends WebSecurityConfigurerAdapter {

    @Autowired
    public UsuarioServicio usuarioServicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http)throws Exception{
        http.headers().frameOptions().sameOrigin().and()
                .authorizeRequests()
                        .antMatchers("/css/*", "/img/*")
                        .permitAll()
                .and().formLogin()
                        .loginPage("/log-in")
                                .loginProcessingUrl("/succes")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/")
                                .permitAll()
                        .and().logout()
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .permitAll()
                .and().csrf().disable()
                        .authorizeRequests()

                        .antMatchers(HttpMethod.OPTIONS).permitAll()
                        .antMatchers(HttpMethod.GET).permitAll()
                        .antMatchers(HttpMethod.POST).permitAll()
                        .antMatchers(HttpMethod.PUT).permitAll()
                        .antMatchers(HttpMethod.DELETE).permitAll()
                        .anyRequest().authenticated()
                        .and().formLogin().permitAll()
                        .and().logout().permitAll();
                                
    }

}
