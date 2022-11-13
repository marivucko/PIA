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

/**
 *
 * @author user
 */

@FacesValidator("emailValidator")
public class emailValidator implements Validator {
    
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//        if (userDAO.usersWithEmail((String) value) == 2) {
//            FacesMessage showMessage = new FacesMessage("The maximum number of the accounts with same email address is 2.");
//            showMessage.setSeverity(FacesMessage.SEVERITY_WARN);
//            throw new ValidatorException(showMessage);
//        }

            FacesMessage showMessage = new FacesMessage(userDAO.usersWithEmail((String) value)+"");
            showMessage.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(showMessage);
    }
    
}
