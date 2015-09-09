package UserInterface;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import DataBaseInterface.*;
import Util.*;

public class wheel_bill_print extends JFrame {
	private JTable tbl_particulars;
	private JLabel lbl_name_value, lbl_mobile_value, lbl_vehicle_no_value, lbl_make_value, lbl_date_value, lbl_bill_no_value, lbl_total_value, lbl_free_checkup_date;
	private JTextPane txt_rupees, txt_comments;
	private DefaultTableModel mdl_particulars;
	private Handler dbh;

	public wheel_bill_print(int bill_id) {
		getContentPane().setBackground(new Color(255, 255, 255));
		setTitle("Abhi Wheels Estimate");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 0, 559, 757);
		setResizable(false);
		setBackground(Color.WHITE);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(10, 11, 163, 89);
		ImageIcon wheels_logo = getScaledImage("/resources/abhi wheels logo.jpg", lblNewLabel.getWidth(), lblNewLabel.getHeight());

		lblNewLabel.setIcon(wheels_logo);

		getContentPane().add(lblNewLabel);

		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(0, 0, 0));
		separator.setBounds(10, 155, 535, 7);
		getContentPane().add(separator);

		dbh = Handler.getInstance();
		mdl_particulars = new DefaultTableModel();

		String[] columns =  {
				"S.No.",
				"Particulars",
				"Quanity x Amount",
				"Sub Total"
		};

		mdl_particulars.setColumnIdentifiers(columns);

		List<Object[]> bill_details = dbh.get_single_bill_detail(bill_id);
		String c_name = null, c_mobile = null, v_num = null, v_make = null, date = null;
		String free_checkup_date = null, comments = null;
		double total_amt = 0.0;

		if(bill_details.isEmpty()){
			JOptionPane.showMessageDialog(null, "Bill not available");
			this.setVisible(false);
		}

		// Load all service particulars to the bill
		int count = 1;
		Map<Integer, Integer> service_row_map = new TreeMap<Integer, Integer>();
		List<ServiceParticulars> sp_list = dbh.list_service_particulars();
		for (ServiceParticulars sp : sp_list){
			String[] o = new String[4];
			o[0] = count + "";
			o[1] = sp.getService_name();
			o[2] = "";
			o[3] = "";

			service_row_map.put(sp.getId(), count -1);
			mdl_particulars.addRow(o);
			count++;
		}

		for(Object[] obj : bill_details){

			ServiceDetail      obj_sd 	= (ServiceDetail)obj[1];
			ServiceBill        obj_sb 	= (ServiceBill)obj[2];
			CustomerVehicle    obj_cveh = (CustomerVehicle)obj[3];
			Customer           obj_cus 	= (Customer)obj[4];

			if(c_name == null){
				c_name = obj_cus.getName();
				c_mobile = obj_cus.getMobile();
				v_num = obj_cveh.getVehicle_number();
				v_make = obj_cveh.getVehicle_make();
				date = Time.get_date(obj_sb.getCreated_epoch());
				free_checkup_date = Time.get_date(obj_sb.getFree_checkup_date());
				comments = obj_sb.getComments();
				total_amt = obj_sb.getTotal_amount();
			}

			String quantity = "" + obj_sd.getQuantity() + " x " + obj_sd.getAmount();
			String total = (obj_sd.getQuantity() * obj_sd.getAmount()) + "";
			mdl_particulars.setValueAt(quantity, service_row_map.get(obj_sd.getService_particular_id()), 2);
			mdl_particulars.setValueAt(total, service_row_map.get(obj_sd.getService_particular_id()), 3);
		}

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblName.setBounds(12, 178, 59, 15);
		getContentPane().add(lblName);

		JLabel lblMobile = new JLabel("Mobile:");
		lblMobile.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblMobile.setBounds(12, 197, 59, 15);
		getContentPane().add(lblMobile);

		lbl_name_value = new JLabel("name_value");
		lbl_name_value.setBounds(83, 178, 235, 15);
		lbl_name_value.setText(c_name);
		getContentPane().add(lbl_name_value);

		lbl_mobile_value = new JLabel("mobile_value");
		lbl_mobile_value.setBounds(83, 197, 115, 15);
		lbl_mobile_value.setText(c_mobile);
		getContentPane().add(lbl_mobile_value);

		JLabel lblNewLabel_1 = new JLabel("Vehicle No.:");
		lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(318, 178, 90, 15);
		getContentPane().add(lblNewLabel_1);

		lbl_vehicle_no_value = new JLabel("vehicle_no_value");
		lbl_vehicle_no_value.setBounds(413, 178, 132, 15);
		lbl_vehicle_no_value.setText(v_num);
		getContentPane().add(lbl_vehicle_no_value);

		JLabel lblMake = new JLabel("Make:");
		lblMake.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblMake.setBounds(318, 196, 53, 15);
		getContentPane().add(lblMake);

		lbl_make_value = new JLabel("make_value");
		lbl_make_value.setBounds(413, 196, 115, 15);
		lbl_make_value.setText(v_make);
		getContentPane().add(lbl_make_value);

		JLabel lblDate = new JLabel("Date:");
		lblDate.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDate.setBounds(12, 159, 44, 15);
		getContentPane().add(lblDate);

		lbl_date_value = new JLabel("date_value");
		lbl_date_value.setBounds(83, 159, 93, 15);
		lbl_date_value.setText(date);
		getContentPane().add(lbl_date_value);

		JLabel lblBillNo = new JLabel("Bill No.:");
		lblBillNo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblBillNo.setBounds(318, 159, 70, 15);
		getContentPane().add(lblBillNo);

		lbl_bill_no_value = new JLabel("bill_no_value");
		lbl_bill_no_value.setBounds(413, 159, 115, 15);
		lbl_bill_no_value.setText(bill_id + "");
		getContentPane().add(lbl_bill_no_value);

		tbl_particulars = new JTable();
		tbl_particulars.setModel(mdl_particulars);
		tbl_particulars.setRowHeight(30);
		tbl_particulars.setColumnSelectionAllowed(false);
		tbl_particulars.setBorder(new LineBorder(new Color(0, 0, 0)));

		tbl_particulars.getColumnModel().getColumn(0).setResizable(false);
		tbl_particulars.getColumnModel().getColumn(0).setPreferredWidth(60);
		tbl_particulars.getColumnModel().getColumn(0).setMaxWidth(60);
		tbl_particulars.getColumnModel().getColumn(1).setResizable(false);
		tbl_particulars.getColumnModel().getColumn(1).setPreferredWidth(250);
		tbl_particulars.getColumnModel().getColumn(1).setMaxWidth(325);
		tbl_particulars.getColumnModel().getColumn(2).setResizable(false);
		tbl_particulars.getColumnModel().getColumn(2).setPreferredWidth(180);
		tbl_particulars.getColumnModel().getColumn(2).setMaxWidth(180);
		tbl_particulars.getColumnModel().getColumn(3).setResizable(false);
		tbl_particulars.getColumnModel().getColumn(3).setPreferredWidth(130);
		tbl_particulars.getColumnModel().getColumn(3).setMaxWidth(130);
		tbl_particulars.getColumnModel().getColumn(3).setCellRenderer(NumberRenderer.getCurrencyRenderer());
		tbl_particulars.setBounds(10, 224, 535, 279);

		JScrollPane tbl_particulars_main = new JScrollPane(tbl_particulars);
		tbl_particulars_main.setBackground(Color.WHITE);
		tbl_particulars_main.setBounds(10, 224, 535, 265);
		getContentPane().add(tbl_particulars_main);

		getContentPane().add(tbl_particulars_main);

		JLabel lblRupees = new JLabel("Rupees:");
		lblRupees.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblRupees.setBounds(13, 504, 70, 15);
		getContentPane().add(lblRupees);

		final JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		btnClose.setBounds(410, 660, 76, 25);
		getContentPane().add(btnClose);

		final JButton btnPrint = new JButton("Print");
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPrint.setVisible(false);
				btnClose.setVisible(false);
				print();
				btnPrint.setVisible(true);
				btnClose.setVisible(true);
			}
		});
		btnPrint.setBounds(409, 620, 76, 25);
		getContentPane().add(btnPrint);

		JLabel lblAbhiEnterprices = new JLabel("ABHI ENTERPRISES");
		lblAbhiEnterprices.setFont(new Font("Dialog", Font.BOLD, 30));
		lblAbhiEnterprices.setBounds(197, 11, 331, 36);
		getContentPane().add(lblAbhiEnterprices);

		JTextPane txt_address = new JTextPane();
		txt_address.setBackground(SystemColor.window);
		txt_address.setContentType("text/html");
		String address = "# 53, 14th \"C\" Cross, Geleyarabalaga, Mahalakshmipuram, Behind Nandini Theatre, Opp. Dr. Rajkumar Indoor Stadium, Bangalore - 86";
		txt_address.setText("<html><center>" + address + "</center></html>");
		txt_address.setBounds(20, 113, 525, 36);
		getContentPane().add(txt_address);

		JTextPane txtpnComputerizedEmissionTest = new JTextPane();
		txtpnComputerizedEmissionTest.setFont(new Font("Dialog", Font.BOLD, 11));
		txtpnComputerizedEmissionTest.setText("Computerised Emission test center for Petrol and Diesel");
		txtpnComputerizedEmissionTest.setBounds(176, 40, 367, 15);
		getContentPane().add(txtpnComputerizedEmissionTest);

		JLabel lblEmail = new JLabel("email:");
		lblEmail.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEmail.setBounds(189, 76, 44, 15);
		getContentPane().add(lblEmail);

		JLabel lblAbhiweelsgmailcom = new JLabel("abhiweels@gmail.com");
		lblAbhiweelsgmailcom.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblAbhiweelsgmailcom.setBounds(239, 76, 176, 15);
		getContentPane().add(lblAbhiweelsgmailcom);

		JLabel lblMobile_1 = new JLabel("Mobile:");
		lblMobile_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblMobile_1.setBounds(180, 93, 59, 15);
		getContentPane().add(lblMobile_1);

		JLabel label = new JLabel("9880067133, 9379667133, 8023597133");
		label.setFont(new Font("Dialog", Font.PLAIN, 12));
		label.setBounds(240, 93, 305, 15);
		getContentPane().add(label);

		txt_rupees = new JTextPane();
		txt_rupees.setFont(new Font("Dialog", Font.BOLD, 12));
		txt_rupees.setBorder(new LineBorder(new Color(0, 0, 0)));
		txt_rupees.setText(NumberWordConverter.convert((int)Math.round(total_amt)) + " rupees only");
		txt_rupees.setBounds(67, 502, 285, 51);
		getContentPane().add(txt_rupees);

		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setFont(new Font("Dialog", Font.BOLD, 15));
		lblTotal.setBounds(356, 504, 53, 15);
		getContentPane().add(lblTotal);

		lbl_total_value = new JLabel("");
		lbl_total_value.setFont(new Font("Dialog", Font.BOLD, 15));
		lbl_total_value.setBounds(407, 499, 140, 25);
		lbl_total_value.setText(total_amt + " /-");
		getContentPane().add(lbl_total_value);

		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(10, 561, 535, 8);
		getContentPane().add(separator_1);

		JTextPane txtpnFreeCheckupWithin = new JTextPane();
		txtpnFreeCheckupWithin.setBackground(SystemColor.window);
		txtpnFreeCheckupWithin.setFont(new Font("Dialog", Font.BOLD, 15));
		txtpnFreeCheckupWithin.setText("FREE CHECK-UP WITHIN 30 DAYS");
		txtpnFreeCheckupWithin.setBounds(20, 573, 298, 25);
		getContentPane().add(txtpnFreeCheckupWithin);

		JLabel lblAbhiWheels = new JLabel("ABHI WHEELS");
		lblAbhiWheels.setFont(new Font("Dialog", Font.BOLD, 20));
		lblAbhiWheels.setBounds(375, 586, 168, 22);
		getContentPane().add(lblAbhiWheels);

		JLabel lblFor = new JLabel("for");
		lblFor.setBounds(342, 593, 29, 15);
		getContentPane().add(lblFor);

		JLabel lblRemarksByTechnician = new JLabel("Remarks by technician:");
		lblRemarksByTechnician.setBounds(26, 620, 168, 15);
		getContentPane().add(lblRemarksByTechnician);

		txt_comments = new JTextPane();
		txt_comments.setBorder(new LineBorder(new Color(0, 0, 0)));
		txt_comments.setText(comments);
		txt_comments.setBounds(28, 637, 305, 69);
		getContentPane().add(txt_comments);

		lbl_free_checkup_date = new JLabel("free_checkup_date");
		lbl_free_checkup_date.setFont(new Font("Dialog", Font.PLAIN, 12));
		lbl_free_checkup_date.setBounds(29, 599, 277, 15);
		lbl_free_checkup_date.setText("Your free checkup due date is " + free_checkup_date);
		getContentPane().add(lbl_free_checkup_date);

		JCheckBox chckbxFreeCheckupDone = new JCheckBox("Free Checkup Done");
		chckbxFreeCheckupDone.setFont(new Font("Dialog", Font.PLAIN, 12));
		chckbxFreeCheckupDone.setBackground(Color.WHITE);
		chckbxFreeCheckupDone.setBounds(360, 533, 171, 23);
		getContentPane().add(chckbxFreeCheckupDone);

		JLabel lblAlignmentTyres = new JLabel("Alignment & Tyres, Tours & Travels");
		lblAlignmentTyres.setFont(new Font("Dialog", Font.BOLD, 11));
		lblAlignmentTyres.setBounds(239, 55, 246, 15);
		getContentPane().add(lblAlignmentTyres);

	}

	// Source: http://stackoverflow.com/questions/30703329/how-to-print-jpanel-in-java
	public void print(){
		PrinterJob pjob = PrinterJob.getPrinterJob();
		PageFormat preformat = pjob.defaultPage();
		preformat.setOrientation(PageFormat.PORTRAIT);
		PageFormat postformat = pjob.pageDialog(preformat);
		//If user does not hit cancel then print.
		if (preformat != postformat) {
			//Set print component
			pjob.setPrintable(new Printer(this), postformat);
			if (pjob.printDialog()) {
				try {
					pjob.print();
				} catch (PrinterException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Print not succeed!!! Someting went wrong!");
				}
			}
		}
		JOptionPane.showMessageDialog(this,"Print completed");
	}

	private ImageIcon getScaledImage(String path, int w, int h){
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(w, h,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		return new ImageIcon(newimg);
	}
}
