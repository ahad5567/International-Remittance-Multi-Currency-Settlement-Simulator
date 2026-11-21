package Remittance_Currency_Simulator.Primary;

public abstract class User {

    private String userID, name, email, phone, password;

    public User(String userID, String name, String email, String phone, String password){
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public abstract void showDashboard();
    public boolean validateLogin(String password){
        return this.password.equals(password);
    }
    public void updateProfile(String name, String phone){
        this.name = name;
        this.phone = phone;
    }
    public String getUserId(){
        return userID;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPhone(){
        return phone;
    }
    public String getPassword(){
        return password;
    }
}
