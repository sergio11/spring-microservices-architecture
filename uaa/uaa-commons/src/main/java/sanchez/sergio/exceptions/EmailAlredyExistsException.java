package sanchez.sergio.exceptions;


/**
 *
 * @author sergio
 */
public class EmailAlredyExistsException extends RuntimeException {
    
    private String firstName;
    private String lastName;
    private String mail;

    public EmailAlredyExistsException(String firstName, String lastName, String mail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    
}
