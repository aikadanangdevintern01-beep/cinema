// // src/main/java/hunglcb/example/module5/config/SecurityConfig.java
// package hunglcb.example.module5.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityConfig {

// @Bean
// public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
// http.csrf(csrf -> csrf.disable())
// .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // Tắt login
// tạm
// return http.build();
// }

// @Bean
// public PasswordEncoder passwordEncoder() {
// return new BCryptPasswordEncoder(12); // Strength 12 là chuẩn cho SB 4.0
// }
// }