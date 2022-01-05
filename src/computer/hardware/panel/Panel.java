package computer.hardware.panel;

import javax.swing.*;
import computer.hardware.cpu.Registers;
import computer.hardware.memory.Memory;

import javax.swing.border.BevelBorder;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/*
 * The construction of GUI and functions of buttons
 * Refer to manual to see how to use buttons.
 * Memory Text File should be named Text
 * Instruction Text File should be name Instructions
 * 
 * @author lee.joyce21@gmail.com
 * @version v0.0.2
 * 
 * */

public class Panel implements ActionListener{
	Registers reg = computer.TimeToRock.reg;
	Memory mem = computer.TimeToRock.mem;
    private JFrame frame;
    private JPanel btnPanel, ctrlPanel, r0Panel, r1Panel, r2Panel, r3Panel, currPanel;
    private JPanel ix1Panel, ix2Panel, ix3Panel, pcPanel, irPanel, marPanel, mbrPanel;
    private JPanel mfrPanel, ccPanel, upperPanel, lowerPanel, lftPanel, lbPanel, southPanel;
    private JTextField r0Text, r1Text, r2Text, r3Text, ix1Text, ix2Text, ix3Text;
    JTextField pcText, irText, marText, mbrText, mfrText, ccText, currText;
    private JToggleButton[] instButtons = new JToggleButton [16];
    private JButton storeButton, iplButton, ssButton, runButton, loadButton, fileButton;
    private JButton r0_loadBtn, r1_loadBtn, r2_loadBtn, r3_loadBtn, ix1_loadBtn;
    private JButton  ix2_loadBtn, ix3_loadBtn, pc_loadBtn, mar_loadBtn, mbr_loadBtn;
    private JLabel opLabel, gprLabel, ixLabel, iLabel, aLabel, currLabel;
    private JLabel r0Label, r1Label, r2Label, r3Label, ix1Label, ix2Label, ix3Label;
    private JLabel pcLabel, irLabel, marLabel, mbrLabel, mfrLabel, ccLabel;
    int instArray[] = new int[16];
    String text = null;
    Instruction in = new Instruction();
    int count = 1;
    int st = 0;
    
    public Panel(){
    	/*
         *construction of the interface
         *upper panel = lftPanel (btnPanel - button, lbPanel - label) + crtlPanel
         *south panel = lower panel - registers, etc.
         */
        frame = new JFrame("Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLayout(new BorderLayout());
        
        //upper panel construction
        upperPanel = new JPanel(new FlowLayout());
        upperPanel.setBorder(BorderFactory.createEmptyBorder(100, 0,0, 0));
        
        //upper panel - left side construction
        lftPanel = new JPanel(new GridLayout(2,1,5,5));
        lftPanel.setSize(1000,200);
        //panel for instruction button labels
        lbPanel = new JPanel(new FlowLayout());
        lbPanel.setSize(1000,100);
        lbPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //panel for instruction buttons
        btnPanel = new JPanel(new GridLayout(1,16,5,5));
        btnPanel.setSize(1000,100);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //creating instruction buttons
        for(int i=0;i<16;i++) {
            instButtons[i] = new JToggleButton(String.valueOf(i));
            instButtons[i].setFocusable(false);
            instButtons[i].addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    e.getStateChange();
                }
            });
        }    
        //adding instruction buttons to button panel
        for(int i=0;i<16;i++) {
            btnPanel.add(instButtons[15-i], 1, i);
        }
        //creating instruction button labels
        opLabel = new JLabel("Operation Code");
        opLabel.setBorder(BorderFactory.createEmptyBorder(0,30,0,95));
        gprLabel = new JLabel("GPR");
        gprLabel.setBorder(BorderFactory.createEmptyBorder(0,40,0,30));
        ixLabel = new JLabel("IX");
        ixLabel.setBorder(BorderFactory.createEmptyBorder(0,55,0,30));
        iLabel = new JLabel("I");
        iLabel.setBorder(BorderFactory.createEmptyBorder(0,40,0,30));
        aLabel = new JLabel("Address");
        aLabel.setBorder(BorderFactory.createEmptyBorder(0,80,0,20));
        //adding instruction button labels to label panel
        lbPanel.add(opLabel);
        lbPanel.add(gprLabel);
        lbPanel.add(ixLabel);
        lbPanel.add(iLabel);
        lbPanel.add(aLabel);
        //adding button panel and label panel to left panel
        lftPanel.add(btnPanel);
        lftPanel.add(lbPanel);
        //creating panel for "control" buttons (load, SS, IPL, etc.)
        ctrlPanel = new JPanel();
        ctrlPanel.setLayout(new GridLayout(2,3,5,5));
        ctrlPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        //creating the control buttons
        //store button - store binary to be able to load in register
        storeButton = new JButton("Store");
        storeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == storeButton) {
                    for(int i=0;i<16;i++) {
                        if(instButtons[15-i].isSelected()) {
                            instArray[i]=1;
                        }
                        else {
                            instArray[i]=0;
                        }
                    }
                    text = String.join("", Arrays.stream(instArray).mapToObj(String::valueOf).toArray(n->new String[n]));
                }
                for(int j=0;j<16;j++) {
                    instButtons[j].setSelected(false);
                }
            }});
        storeButton.setFocusable(false);
        storeButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        //load button - choose an instruction txt file from computer
        loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==loadButton) {
                    if(text == null) { //switch buttons for memory, store, switch buttons for data, load
                    	JOptionPane.showMessageDialog(frame, "Please enter a memory location.");
                    }
                    else if(Integer.parseInt(text,2) <= 6){//reserved memory location
                    	JOptionPane.showMessageDialog(frame, "Invalid Memory Location.");
                    }
                    else {
                    	int addr = Integer.parseInt(text,2);
                    	String memo;
                    	for(int i=0;i<16;i++) {
                            if(instButtons[15-i].isSelected()) {
                                instArray[i]=1;
                            }
                            else {
                                instArray[i]=0;
                            }
                    	}
                       memo = String.join("", Arrays.stream(instArray).mapToObj(String::valueOf).toArray(n->new String[n]));
                    mem.writeMemory(addr, memo);
                    for(int j=0;j<16;j++) {
                        instButtons[j].setSelected(false);
                    }
                    }
                    
                    
                }
                
            }
            
        });
        loadButton.setFocusable(false);
        loadButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        loadButton.setPreferredSize(new Dimension(60,25));
        //IPL button - reset and loads
        iplButton = new JButton("IPL");
        iplButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==iplButton) {
                    reset();
            
					
                }
                
            }
            
        });
        iplButton.setFocusable(false);
        iplButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        iplButton.setPreferredSize(new Dimension(60,25));
        //ssButton, counts 9 steps which should go through the process of one instruction
        ssButton = new JButton("SS");
        ssButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	String text = null;
                if(e.getSource() == ssButton) {
                	text = in.ssButton(count);
                	sStep();
                	count++;
                }
                if(count ==2) {
                	sStep();
                }
                else if(count ==3) {
                	sStep();
                }
                else if(count ==4) {
                	sStep();
                }
                else if(count ==5) {
                	sStep();
                }
                else if(count==6) {
                	sStep();
                }
                else if(count==7) {
                	sStep();
                }
                else if(count==8) {
                	sStep();
                }
                else if(count==9) {
                	count=1;
                }
            }
            
        });
        ssButton.setFocusable(false);
        ssButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        ssButton.setPreferredSize(new Dimension(60,25));
        //runButton
        runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	runButton(true);
            }
            
        });
        runButton.setFocusable(false);
        runButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        runButton.setPreferredSize(new Dimension(60,25));
        //file Button to load a file
        fileButton = new JButton("File");
        fileButton.addActionListener(new ActionListener() {//only for files in jar

            public void actionPerformed(ActionEvent e) {
            	Load ld = new Load();
            	          	
            	try {
					ld.readFile("./instr.txt");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, "File not found.");
					e1.printStackTrace();
				}
            	
            	pcText.setText(reg.PC.getValueString());
            	
            	
            }
            
        });
        fileButton.setFocusable(false);
        fileButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        fileButton.setPreferredSize(new Dimension(60,25));
        //adding the control buttons to panel
        ctrlPanel.add(storeButton);
        ctrlPanel.add(loadButton);
        ctrlPanel.add(iplButton);
        ctrlPanel.add(ssButton);
        ctrlPanel.add(runButton);
        ctrlPanel.add(fileButton);
        
        //adding main components of the upper panel to said panel
        upperPanel.add(lftPanel);
        upperPanel.add(ctrlPanel);
        
        //lower panel construction
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new GridLayout(8,2,5,5));
        //gpr panels
        //r0
        r0Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        r0Panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        r0Label = new JLabel("GPR0");
        r0Text = new JTextField();
        r0Text.setEditable(false);
        r0Text.setColumns(40);
        r0Text.setBackground(Color.WHITE);
        r0_loadBtn = new JButton("Load");
        r0_loadBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(text == null) {
            		JOptionPane.showMessageDialog(frame, "Please input data.");
            	}
            	else {
                r0Text.setText(text);
                reg.GPR0.setValue(text);
            	}
                for(int j=0;j<16;j++) {
                    instButtons[j].setSelected(false);
                }
            }
            
        });
        r0_loadBtn.setFocusable(false);
        r0_loadBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
        r0_loadBtn.setPreferredSize(new Dimension(60,25));
        
        r0Panel.add(r0Label);
        r0Panel.add(r0Text);
        r0Panel.add(r0_loadBtn);
        //r1
        r1Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        r1Panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        r1Label = new JLabel("GPR1");
        r1Text = new JTextField();
        r1Text.setEditable(false);
        r1Text.setColumns(40);
        r1Text.setBackground(Color.WHITE);
        r1_loadBtn = new JButton("Load");
        r1_loadBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(text == null) {
            		JOptionPane.showMessageDialog(frame, "Please input data.");
            	}
            	else {
            	r1Text.setText(text);
                reg.GPR1.setValue(text);
            	}
                for(int j=0;j<16;j++) {
                    instButtons[j].setSelected(false);
                }
            }
            
        });
        r1_loadBtn.setFocusable(false);
        r1_loadBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
        r1_loadBtn.setPreferredSize(new Dimension(60,25));
        
        r1Panel.add(r1Label);
        r1Panel.add(r1Text);
        r1Panel.add(r1_loadBtn);
        //r2
        r2Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        r2Panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        r2Label = new JLabel("GPR2");
        r2Text = new JTextField();
        r2Text.setEditable(false);
        r2Text.setColumns(40);
        r2Text.setBackground(Color.WHITE);
        r2_loadBtn = new JButton("Load");
        r2_loadBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(text == null) {
            		JOptionPane.showMessageDialog(frame, "Please input data.");
            	}
            	else {
            	r2Text.setText(text);
                reg.GPR2.setValue(text);
            	}
                for(int j=0;j<16;j++) {
                    instButtons[j].setSelected(false);
                }
            }
            
        });
        r2_loadBtn.setFocusable(false);
        r2_loadBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
        r2_loadBtn.setPreferredSize(new Dimension(60,25));
        
        r2Panel.add(r2Label);
        r2Panel.add(r2Text);
        r2Panel.add(r2_loadBtn);
        //r3
        r3Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        r3Panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        r3Label = new JLabel("GPR3");
        r3Text = new JTextField();
        r3Text.setEditable(false);
        r3Text.setColumns(40);
        r3Text.setBackground(Color.WHITE);
        r3_loadBtn = new JButton("Load");
        r3_loadBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(text == null) {
            		JOptionPane.showMessageDialog(frame, "Please input data.");
            	}
            	else {
            	r3Text.setText(text);
                reg.GPR3.setValue(text);
            	}
                for(int j=0;j<16;j++) {
                    instButtons[j].setSelected(false);
                }
            }
            
        });
        r3_loadBtn.setFocusable(false);
        r3_loadBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
        r3_loadBtn.setPreferredSize(new Dimension(60,25));
        
        r3Panel.add(r3Label);
        r3Panel.add(r3Text);
        r3Panel.add(r3_loadBtn);
        
        //ix panels
        //ix1
        ix1Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ix1Panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ix1Label = new JLabel("IX1");
        ix1Text = new JTextField();
        ix1Text.setEditable(false);
        ix1Text.setColumns(40);
        ix1Text.setBackground(Color.WHITE);
        ix1_loadBtn = new JButton("Load");
        ix1_loadBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(text == null) {
            		JOptionPane.showMessageDialog(frame, "Please input data.");
            	}
            	else {
            	reg.X1.setValue(text);
            	ix1Text.setText(text);
            	}
                for(int j=0;j<16;j++) {
                    instButtons[j].setSelected(false);
                }
            }
            
        });
        ix1_loadBtn.setFocusable(false);
        ix1_loadBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
        ix1_loadBtn.setPreferredSize(new Dimension(60,25));
        
        ix1Panel.add(ix1Label);
        ix1Panel.add(ix1Text);
        ix1Panel.add(ix1_loadBtn);
        //ix2
        ix2Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ix2Panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ix2Label = new JLabel("IX2");
        ix2Text = new JTextField();
        ix2Text.setEditable(false);
        ix2Text.setColumns(40);
        ix2Text.setBackground(Color.WHITE);
        ix2_loadBtn = new JButton("Load");
        ix2_loadBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(text == null) {
            		JOptionPane.showMessageDialog(frame, "Please input data.");
            	}
            	else {
            	reg.X2.setValue(text);
            	ix2Text.setText(text);
            	}
                for(int j=0;j<16;j++) {
                    instButtons[j].setSelected(false);
                }
            }
            
        });
        ix2_loadBtn.setFocusable(false);
        ix2_loadBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
        ix2_loadBtn.setPreferredSize(new Dimension(60,25));
        
        ix2Panel.add(ix2Label);
        ix2Panel.add(ix2Text);
        ix2Panel.add(ix2_loadBtn);
        //ix3
        ix3Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ix3Panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ix3Label = new JLabel("IX3");
        ix3Text = new JTextField();
        ix3Text.setEditable(false);
        ix3Text.setColumns(40);
        ix3Text.setBackground(Color.WHITE);
        ix3_loadBtn = new JButton("Load");
        ix3_loadBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(text == null) {
            		JOptionPane.showMessageDialog(frame, "Please input data.");
            	}
            	else {
            	reg.X3.setValue(text);
            	ix3Text.setText(text);
            	}
                for(int j=0;j<16;j++) {
                    instButtons[j].setSelected(false);
                }
            }
            
        });
        ix3_loadBtn.setFocusable(false);
        ix3_loadBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
        ix3_loadBtn.setPreferredSize(new Dimension(60,25));
        
        ix3Panel.add(ix3Label);
        ix3Panel.add(ix3Text);
        ix3Panel.add(ix3_loadBtn);
        
        //pc panel
        pcPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pcPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));
        pcLabel = new JLabel("PC");
        pcText = new JTextField();
        pcText.setEditable(false);
        pcText.setColumns(40);
        pcText.setBackground(Color.WHITE);
        pc_loadBtn = new JButton("Load");
        pc_loadBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(text == null) {
            		JOptionPane.showMessageDialog(frame, "Please input data.");
            	}
            	else {
            	char[] temp = new char[12];
            	char[] str = new char[text.length()];
            	for(int i = 0; i<text.length();i++) {
            		str[i] = text.charAt(i);
            	}
            	for(int j=0; j<12;j++) {
            		temp[11-j] = str[15-j];
            	}
            	String bin = new String(temp);
            	reg.PC.setValue(bin);
            	pcText.setText(bin);
            	}
                for(int j=0;j<16;j++) {
                    instButtons[j].setSelected(false);
                }
            }
            
        });
        pc_loadBtn.setFocusable(false);
        pc_loadBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
        pc_loadBtn.setPreferredSize(new Dimension(60,25));
        
        pcPanel.add(pcLabel);
        pcPanel.add(pcText);
        pcPanel.add(pc_loadBtn);
        
        //ir panel
        irPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        irPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));
        irLabel = new JLabel("IR");
        irText = new JTextField();
        irText.setEditable(false);
        irText.setColumns(40);
        irText.setBackground(Color.WHITE);
        
        irPanel.add(irLabel);
        irPanel.add(irText);
        
        //mar panel
        marPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        marPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));
        marLabel = new JLabel("MAR");
        marText = (new JTextField());
        marText.setEditable(false);
        marText.setColumns(40);
        marText.setBackground(Color.WHITE);
        mar_loadBtn = new JButton("Load");
        mar_loadBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(text == null) {
            		JOptionPane.showMessageDialog(frame, "Please input data.");
            	}
            	else {
            	char[] temp = new char[12];
            	char[] str = new char[text.length()];
            	for(int i = 0; i<text.length();i++) {
            		str[i] = text.charAt(i);
            	}
            	for(int j=0; j<12;j++) {
            		temp[11-j] = str[15-j];
            	}
            	String bin = new String(temp);
            	reg.MAR.setValue(bin);
            	marText.setText(bin);
            	}
                for(int j=0;j<16;j++) {
                    instButtons[j].setSelected(false);
                }
            }
            
        });
        mar_loadBtn.setFocusable(false);
        mar_loadBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
        mar_loadBtn.setPreferredSize(new Dimension(60,25));
        
        marPanel.add(marLabel);
        marPanel.add(marText);
        marPanel.add(mar_loadBtn);
        
        //mbr panel
        mbrPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        mbrPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));
        mbrLabel = new JLabel("MBR");
        mbrText = new JTextField();
        mbrText.setEditable(false);
        mbrText.setColumns(40);
        mbrText.setBackground(Color.WHITE);
        mbr_loadBtn = new JButton("Load");
        mbr_loadBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	if(text == null) {
            		JOptionPane.showMessageDialog(frame, "Please input data.");
            	}
            	else {
            	reg.MBR.setValue(text);
                mbrText.setText(text);
            	}
                for(int j=0;j<16;j++) {
                    instButtons[j].setSelected(false);
                }
            }
            
        });
        mbr_loadBtn.setFocusable(false);
        mbr_loadBtn.setBorder(new BevelBorder(BevelBorder.RAISED));
        mbr_loadBtn.setPreferredSize(new Dimension(60,25));
        
        mbrPanel.add(mbrLabel);
        mbrPanel.add(mbrText);
        mbrPanel.add(mbr_loadBtn);
        
        //mfr panel
        mfrPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        mfrPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));
        mfrLabel = new JLabel("MFR");
        mfrText = new JTextField();
        mfrText.setEditable(false);
        mfrText.setColumns(40);
        mfrText.setBackground(Color.WHITE);
        
        mfrPanel.add(mfrLabel);
        mfrPanel.add(mfrText);
        
        //cc panel
        ccPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ccPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));
        ccLabel = new JLabel("CC");
        ccText = new JTextField();
        ccText.setEditable(false);
        ccText.setColumns(40);
        ccText.setBackground(Color.WHITE);
        
        ccPanel.add(ccLabel);
        ccPanel.add(ccText);

        //current mem panel
        currPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        currPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));
        currLabel = new JLabel("MEM");
        currText = new JTextField();
        currText.setEditable(false);
        currText.setColumns(40);
        currText.setBackground(Color.WHITE);

        currPanel.add(currLabel);
        currPanel.add(currText);
        
        lowerPanel.add(r0Panel);
        lowerPanel.add(currPanel);
        lowerPanel.add(r1Panel);
        lowerPanel.add(pcPanel);
        lowerPanel.add(r2Panel);
        lowerPanel.add(marPanel);
        lowerPanel.add(r3Panel);
        lowerPanel.add(mbrPanel);
        lowerPanel.add(ix1Panel);
        lowerPanel.add(irPanel);
        lowerPanel.add(ix2Panel);
        lowerPanel.add(mfrPanel);
        lowerPanel.add(ix3Panel);
        lowerPanel.add(ccPanel);
        
        //southPanel contains lowerPanel
        southPanel = new JPanel();
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 200, 0));
        southPanel.add(lowerPanel);
        
        frame.add(upperPanel, BorderLayout.NORTH);
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
        
    }
    
    protected void runButton(boolean b) {
    	String text = null;
        while(b) {
        	
        	for(int i=1;i<9;i++) {
        		text = in.ssButton(count);
        		sStep();
        		count++;
        	}
      
        	b=false;
        }
		
	}

	public static void main(String[] args) {

    }
    
    public void actionPerformed(ActionEvent e) {
        
    }
    
    //reset conditions
    public void reset() {
    	FileActions action = new FileActions();
	MemFile file = new MemFile();
    	String reset16 = "0000000000000000";
    	String reset12 = "000000000000";
        //reset the instruction switches
        for(int i=0; i<16; i++) {
            instButtons[i].setSelected(false);
        }
        //reset the control buttons
        runButton.setSelected(false);
        loadButton.setSelected(false);
        reg.GPR0.setValue(reset16);
        reg.GPR1.setValue(reset16);
        reg.GPR2.setValue(reset16);
        reg.GPR3.setValue(reset16);
        reg.PC.setValue(reset12);
        reg.IR.setValue(reset16);
       	reg.MAR.setValue(reset12);
        reg.MBR.setValue(reset16);
        reg.MFR.setValue("");
        reg.CC.setValue("");
        r0Text.setText(reset16);
        r1Text.setText(reset16);
        r2Text.setText(reset16);
        r3Text.setText(reset16);
        ix1Text.setText(reset16);
        ix2Text.setText(reset16);
        ix3Text.setText(reset16);
        pcText.setText(reset12);
        irText.setText(reset16);
        marText.setText(reset12);
        mbrText.setText(reset16);
        mfrText.setText("");
        ccText.setText("");
        text = null;
        count = 1;
        for(int j=0;j<16;j++) {
            instArray[j] = 0;
        }
        //load
    	try {
		file.readFile("resources/IPL.txt");
		action.readFile("resources/Instructions.txt");
	} catch (Exception e) {
		JOptionPane.showMessageDialog(frame, "File not found.");
		e.printStackTrace();
		}
    
        pcText.setText(reg.PC.getValueString());
				
    }
    
    public void sStep() {
    	r0Text.setText(reg.GPR0.getValueString());
    	r1Text.setText(reg.GPR1.getValueString());
    	r2Text.setText(reg.GPR2.getValueString());
    	r3Text.setText(reg.GPR3.getValueString());
        pcText.setText(reg.PC.getValueString());
        irText.setText(reg.IR.getValueString());
        marText.setText(reg.MAR.getValueString());
        mbrText.setText(reg.MBR.getValueString());
        mfrText.setText(reg.MFR.getValueString());
        ccText.setText("");
        ix1Text.setText(reg.X1.getValueString());
        ix2Text.setText(reg.X2.getValueString());
        ix3Text.setText(reg.X3.getValueString());
       
    }
}
	
