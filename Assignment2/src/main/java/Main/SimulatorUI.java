package Main;

import Main.ReadFromCsv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimulatorUI {
    private final JButton startSimulation=new JButton("Start simulation");
    private final JLabel deviceIdLabel = new JLabel("Device id:");

    List<String> allDeviceIds=readFromConfigurationFile();
    JComboBox deviceIdList;
    JLabel l1;
    public SimulatorUI() {JFrame jFrame= new JFrame();
        jFrame.setSize(250,250);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel=new JPanel();

        deviceIdList= new JComboBox(allDeviceIds.toArray());
        l1 = new JLabel("You selected device: " + allDeviceIds.get(0));
        l1.setForeground(Color.black);
        panel.setBackground(Color.decode("#ccd9ff"));
        panel.setLayout(null);
        jFrame.add(panel);
        deviceIdLabel.setBounds(7,7,120,35);
        startSimulation.setBounds(27,87,170,35);
        deviceIdList.setBounds(70,15,120,25);
        l1.setBounds(57,47,150,25);

        panel.add(deviceIdLabel);
        panel.add(startSimulation);
        panel.add(deviceIdList);
        panel.add(l1);
        startSimulationActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReadFromCsv csvReader = new ReadFromCsv();
                csvReader.readCSV();
                Send send= new Send();

                send.readWithDelay(csvReader,(String) deviceIdList.getSelectedItem());
            }
        });

        deviceIdListActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String data =((String) deviceIdList.getSelectedItem());
                l1.setText("You selected device: "+data);
            }
        });

        jFrame.setVisible(true);

    }
    public void startSimulationActionListener(final ActionListener actionListener)
    {
        startSimulation.addActionListener(actionListener);
    }

    public void deviceIdListActionListener(final ActionListener actionListener)
    {
        deviceIdList.addActionListener(actionListener);
    }

    public List<String> readFromConfigurationFile()
    {
        List<String> allDeviceIds= new ArrayList<>();
        try {
            File myObj = new File("configurationFile.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String deviceId = myReader.nextLine();
               allDeviceIds.add(deviceId);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return allDeviceIds;
    }
}
