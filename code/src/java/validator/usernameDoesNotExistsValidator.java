/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

import dao.UserDAO;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import pojo.User;

/**
 *
 * @author user
 */
@FacesValidator("usernameNotExistsValidator")
public class usernameDoesNotExistsValidator implements Validator {
    
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) return;
        User user = userDAO.checkExists((String) value);
        if (user == null) {
            FacesMessage showMessage = new FacesMessage("User with this username does not exist.");
            showMessage.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(showMessage);
        } 
        else {
            if (user.isUserRegistered()) {
                FacesMessage showMessage = new FacesMessage("User with this username is already registered.");
                showMessage.setSeverity(FacesMessage.SEVERITY_INFO);
                throw new ValidatorException(showMessage);
            }
        }
    }
    
}

