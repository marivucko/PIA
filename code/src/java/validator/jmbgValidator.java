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
@FacesValidator("jmbgValidator")
public class jmbgValidator  implements Validator {
    
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) return;
        User user = userDAO.checkExistsWithJMBG((String) value);
        if (user != null) {
            FacesMessage showMessage = new FacesMessage("User with this JMBG already exists.");
            showMessage.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(showMessage);
        }
        
        
        String jmbg = value.toString();
        String warning = "";
        
        boolean onlyDigits = true;
        
        if (jmbg.length() != 13) {
            warning = "The length of JMBG must be 13 digits";
        }
        else {
            int i;
            for (i = 0; i < 13; i++)
                if (!((jmbg.charAt(i) - '0' >= 0) && (jmbg.charAt(i) - '0' <= 9)))
                    onlyDigits = false;
            
            if (!onlyDigits)
                warning = "JMBG must contain only digits.";
            else {
                int dd, d1, d0, mm, m1, m0, ggg, g2, g1, g0, rr, r1, r0, bbb, b2, b1, b0, k;
                d1 = jmbg.charAt(0) - '0';      d0 = jmbg.charAt(1) - '0';
                m1 = jmbg.charAt(2) - '0';      m0 = jmbg.charAt(3) - '0';
                g2 = jmbg.charAt(4) - '0';      g1 = jmbg.charAt(5) - '0';      g0 = jmbg.charAt(6) - '0';

                r1 = jmbg.charAt(7) - '0';      r0 = jmbg.charAt(8) - '0';
                b2 = jmbg.charAt(9) - '0';      b1 = jmbg.charAt(10) - '0';      b0 = jmbg.charAt(11) - '0';
                k = jmbg.charAt(12) - '0';  
                
                dd = 10*d1 + d0; 
                mm = 10*m1 + m0;
                ggg = 100*g2 + 10*g1 + g0;
                
                rr = 10*r1 + r0;
                bbb = 100*b2 + 10*b1+ b0;
                
                 if (mm <= 0 || mm > 12) 
                {
                    warning = "The month of birth must be in the range of [1..12]";
                }
                else if (mm == 4 || mm == 6 || mm == 9 || mm == 11)
                {  if (dd > 30)
                       warning = "The day of birth must be less or equal to 30 for chosen month.";
                }
                else if (mm == 2)
                {
                    if ((ggg % 100 != 0) && (ggg % 4 == 0))  // prestupna godina
                    {
                        if (dd > 29)
                            warning = "The day of birth must be less or equal to 29 for chosen month and year.";

                    }
                    else                                   // nije prestupna godina
                    {
                        if (dd > 28)
                            warning = "The day of birth must be less or equal to 28 for chosen month and year.";
                    }
                }
                else 
                {
                    if (dd > 31)
                        warning = "The day of birth must be less or equal to 31 for chosen month.";
                } 
                
                if ((rr == 20) || (rr == 40) || ((rr >= 51) && (rr <= 59)) || (rr == 97) || (rr == 98) || (rr == 99))
                        warning += "Two-digit number that represents the region of birth is not used in JMBG.";
                
                
                if (warning.equals("")) {
                    int s = 7*d1+6*d0+5*m1+4*m0+3*g2+2*g1+7*g0+6*r1+5*r0+4*b2+3*b1+2*b0;
                    int m = s % 11; int n = 11-m;
                    if (m == 0 && k != 0)
                            warning += "Control digit should be 0, but it is " + k + "."; 
                    if (m > 1 && k != 11 - m)
                            warning += "Control digit should be " + n + ", but it is " + k + ".";

                    while (m == 1) {
                        bbb++; b2 = (bbb / 100); b1 = (bbb / 10) % 10; b0 = bbb % 10;
                        s = 7*d1+6*d0+5*m1+4*m0+3*g2+2*g1+7*g0+6*r1+5*r0+4*b2+3*b1+2*b0;
                        m = s % 11; n = 11-m;
                        if (m == 0 && k != 0)
                            warning += "Control digit should be 0, but it is " + k + ".";
                        if (m > 1 && k != 11 - m)
                            warning += "Control digit should be " + n + ", but it is " + k + ".";
                    }
                }
            }                 
        }
        
        if (!warning.equals("")) {
            FacesMessage showMessage = new FacesMessage(warning);
            showMessage.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(showMessage);
        }
    }
    
    
    
}
