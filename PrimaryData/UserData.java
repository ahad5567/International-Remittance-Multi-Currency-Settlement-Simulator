package Remittance_Currency_Simulator.PrimaryData;

import Remittance_Currency_Simulator.Primary.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserData extends FileData {

    private List<User> users = new ArrayList<>();

    public UserData(String filePath) {
        super(filePath);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User findUserByEmail(String email) {

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public User findUserById(String id) {

        for (User user : users) {
            if (user.getUserId().equals(id)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public void saveData(){
        try (BufferedWriter Writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            for (User user : users) {

                String role = user.getClass().getSimpleName();

                Writer.write(role + "," +
                        user.getUserId() + "," +
                        user.getName() + "," +
                        user.getEmail() + "," +
                        user.getPhone() + "," +
                        user.getPassword());

                Writer.newLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void loadData(){
        File file  = new File(filePath);

        if(!file.exists()){
            return;
        }

        users.clear();

        try (BufferedReader Reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String data = Reader.readLine();
            while (data != null){

                String[] p = data.split(",");

                if (p.length >= 6) {
                    String role = p[0], id = p[1], name = p[2], email = p[3], phone = p[4], password = p[5];

                    if (role.equals("Sender")) {
                        users.add(new Sender(id, name, email, phone, password));
                    } else if (role.equals("Receiver")) {
                        users.add(new Receiver(id, name, email, phone, password));
                    } else if (role.equals("Admin")) {
                        users.add(new Admin(id, name, email, phone, password));
                    }
                }

                data = Reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers(){
        return (new ArrayList<>(users));
    }
}
