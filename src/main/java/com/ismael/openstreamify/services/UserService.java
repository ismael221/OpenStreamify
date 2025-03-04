package com.ismael.openstreamify.services;

import com.ismael.openstreamify.enums.Provider;
import com.ismael.openstreamify.model.Exceptions.BadRequestException;
import com.ismael.openstreamify.model.Users.RegisterDTO;
import com.ismael.openstreamify.model.Users.User;
import com.ismael.openstreamify.model.Users.UserRole;
import com.ismael.openstreamify.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    final
    UserRepository userRepository;

    final
    NotificationService notificationService;

    public UserService(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public void createNewUser(RegisterDTO user){
        if (this.userRepository.findByLogin(user.login()) != null){
            throw new BadRequestException("User already registered, please login");
        };

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        User newUser = new User(user.login(),encryptedPassword,user.role(), user.name(), user.provider());
        this.userRepository.save(newUser);

        String mensagemPersonalizada = "🚨 *New user Alert* 🚨\n\n" +
                "*Título*: " + "Novo usuário cadastrado" + "\n" +
                "*Estado*: " + user.role() + "\n" +
                "*Detalhes*: " + newUser.getLogin() + "\n" +
                "[Ver mais no Grafana](" + ' ' + ")";

        notificationService.enviarMensagemTelegram(mensagemPersonalizada);

    }


    public User findUserByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public User updateUserLogin(User user){
        User user1 = findUserByLogin(user.getLogin());
        if (user1 != null){
            user1.setLogin(user.getLogin());
            user1.setRole(user.getRole());
            user1.setPassword(user.getPassword());
            userRepository.saveAndFlush(user1);

            String mensagem = "🔒 *Alteração de conta*\n\n" +
                    "Usuário: " + user.getLogin() + " (ID: " + user.getId() + ") "+"(Ativo?: " + user.isActive() + ")"+"(Role: "+user.getRole()+ ") "+"\n" +
                    "A conta foi alterada com sucesso em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n\n" +
                    "Se você não fez essa alteração, entre em contato imediatamente com o suporte.";

            notificationService.enviarMensagemTelegram(mensagem);
            return  userRepository.saveAndFlush(user1);
        }
        return null;
    }

    public List<UUID> findAllUsersId(){
        return  userRepository.findAllUsersId();
    }

    public UUID findUserIdByLogin(String login){
        return userRepository.findUserIdByLogin(login);
    }

    public boolean activateUserAccountByEmail(String email){
        User userFound = findUserByLogin(email);

        if (userFound != null){
            userFound.setActive(true);
            return true;
        }

        return false;
    }

    public void processOAuthPostLogin(String email,String name, String provider) {
        User existUser = userRepository.findByLogin(email);
        if (existUser == null) {
            User newUser = new User();
            newUser.setLogin(email);
            newUser.setProvider(Provider.valueOf(provider.toUpperCase()));
            newUser.setActive(true);
            newUser.setRole(UserRole.USER);
            newUser.setName(name);
            newUser.setPassword(" ");

            userRepository.save(newUser);
        }
    }
}
