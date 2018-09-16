package app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {

    private String Company_ID;
    private String Name;
    private String Slogan;
    private String Description;
    private String City;
    private String State;
    private String Country;
    private String Created_On;
    private String Is_Active;
    private String Action;

    public Company(String Company_ID, String Name, String Slogan, String Description, String City, String State,
                   String Country, String Created_On, String Is_Active, String Action) {
        this.Company_ID = Company_ID;
        this.Name = Name;
        this.Slogan = Slogan;
        this.Description = Description;
        this.City = City;
        this.State = State;
        this.Country = Country;
        this.Created_On = Created_On;
        this.Is_Active = Is_Active;
        this.Action = Action;

    }

    public String getCompany_ID() {
        return Company_ID;
    }

    public void setCompany_ID(String company_ID) {
        Company_ID = company_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSlogan() {
        return Slogan;
    }

    public void setSlogan(String slogan) {
        Slogan = slogan;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCreated_On() {
        return Created_On;
    }

    public void setCreated_On(String created_On) {
        Created_On = created_On;
    }

    public String getIs_Active() {
        return Is_Active;
    }

    public void setIs_Active(String is_Active) {
        Is_Active = is_Active;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

}
