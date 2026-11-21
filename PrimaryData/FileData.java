package Remittance_Currency_Simulator.PrimaryData;

import java.io.IOException;

public abstract class FileData {
    protected String filePath;

    public FileData(String filePath) {
        this.filePath = filePath;
    }

    public abstract void saveData();

    public abstract void loadData();
}
