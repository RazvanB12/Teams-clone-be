package com.example.moodleclone.utils.validators;

import com.example.moodleclone.dtos.IUser;
import com.example.moodleclone.entities.RoleEnum;
import com.example.moodleclone.entities.User;
import com.example.moodleclone.exceptions.UserAuthException;
import com.example.moodleclone.exceptions.UserValidationException;
import com.example.moodleclone.repositories.UserRepository;
import com.example.moodleclone.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwt;

    public User authUser(String token) throws UserAuthException {
        UUID userId = jwt.getIdFromToken(token);
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty() || jwt.isTokenExpired(token)) {
            String message = "Session expired. You need to login or register if you want to access this content!";
            throw new UserAuthException(message);
        }
        return userOptional.get();
    }

    public void verifyIfProfessor(User user) throws UserValidationException{
        if(!user.getRoleName().equals(RoleEnum.PROFESSOR)){
            throw new UserValidationException("You are not allowed to do this operation.");
        }
    }

    public void verifyIfAdmin(User user) throws UserValidationException{
        if(!user.getRoleName().equals(RoleEnum.ADMIN)){
            throw new UserValidationException("You are not allowed to do this operation.");
        }
    }

    public void validateUser (IUser user, Boolean ignorePassword) throws UserValidationException {
        try{
            Class<? extends IUser> userClass = user.getClass();
            for (Field f : userClass.getDeclaredFields()) {
                if("name".equals(f.getName())){
                    String name = (String) getValueOfField(f.getName(),user);
                    validateName(name);
                }
                else if("email".equals(f.getName())){
                    String email = (String) getValueOfField(f.getName(),user);
                    validateEmail(email);
                }
                else if("username".equals(f.getName())){
                    String username = (String) getValueOfField(f.getName(),user);
                    valdiateUsername(username);
                }
                else if("role".equals(f.getName())){
                    RoleEnum role = (RoleEnum) getValueOfField(f.getName(),user);
                    valdiateRole(role);
                }
                else if(!ignorePassword && "password".equals(f.getName())){
                    String password = (String) getValueOfField(f.getName(),user);
                    validatePassword(password);
                }
            }
        }catch(IntrospectionException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
            throw new UserValidationException("Could not do validation! :(");
        }
    }

    public void validateName(String name) throws UserValidationException {
        if(!Pattern.matches("([\\w ]+)",name)){
            throw new UserValidationException("Invalid user name!");
        }
    }

    public void validateEmail(String email) throws UserValidationException {
        if(!Pattern.matches("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})",email)){
            throw new UserValidationException("Invalid email!");
        }
    }

    public void valdiateUsername(String username) throws UserValidationException {
        if(!Pattern.matches("^(?=[a-zA-Z0-9._]{3,20}$)(?!.*[_.]{2})[^_.].*[^_.]$",username)){
            throw new UserValidationException("Invalid username!");
        }
    }
    public void valdiateRole(RoleEnum role) throws UserValidationException {
        if(role.equals(RoleEnum.ADMIN)){
            throw new UserValidationException("You cannot register as Admin!");
        }
    }
    public void validatePassword(String password) throws UserValidationException {
        if(!Pattern.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",password)){
            throw new UserValidationException("Password must contain an uppercase letter, lowercase letter, a digit and a special character!");
        }
    }

    private Object getValueOfField(String nameOfField, Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(nameOfField,obj.getClass());
        Method getter = propertyDescriptor.getReadMethod();
        return getter.invoke(obj);
    }


}

