package Remittance_Currency_Simulator.PrimaryData;

import Remittance_Currency_Simulator.Primary.LedgerEntry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LedgerData extends FileData {

    private List<LedgerEntry> entries = new ArrayList<>();

    public LedgerData(String filePath) {
        super(filePath);
    }

    public void addEntry(LedgerEntry entry) {
        entries.add(entry);
    }

    public List<LedgerEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    @Override
    public void saveData() {
        try (BufferedWriter Writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {

            for (LedgerEntry entry : entries) {
                Writer.write(entry.toString());
                Writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        File file = new File(filePath);

        if (!file.exists()) {
            return;
        }

        entries.clear();

        try (BufferedReader Reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String data = Reader.readLine();
            while (data != null) {

                String[] p = data.split(",");

                if (p.length >= 6) {
                    entries.add(new LedgerEntry(p[0], p[1], p[2], p[3], Double.parseDouble(p[4]), LocalDateTime.parse(p[5])));
                }

                data = Reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
