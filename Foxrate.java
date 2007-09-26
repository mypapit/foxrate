/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as published by
 *  the Free Software Foundation
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Foxrate 1.0 <info@mypapit.net>
 * Copyright 2007 Mohammad Hafiz bin Ismail. All rights reserved.
 *
 * Info url : http://kirostudio.com
 * 
 * Foxrate.java
 * Mobile Device Currency Exchange Application 
 *
 * 
 * Currency exchange data taken from http://www.ecb.int/stats/exchange/eurofxref/html/index.en.html
 */

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.io.*;
import java.io.*;
import java.util.*;
import net.mypapit.java.StringTokenizer;
import javax.microedition.rms.*;

public class Foxrate extends MIDlet implements CommandListener
{
	public Display display;
	public ChoiceGroup cgFrom,cgTo;
	//private TextField tfFrom, tfTo;
	public Form form;
	private AboutForm aboutform;
	private RecordStore rs;
	private StringItem siDate;
	private Command cmdExit,cmdConvert,cmdAbout,cmdUpdate, cmdSwap;
	private String[] currency = {"Euro (EUR)","US Dollar (USD)","Japanese Yes (JPY)",
	"Bulgarian Lev (BGN)","Cyprus Pound (CYP)","Czech Korona (CZK)","Danish Korone (DKK)",
	"Estonian Krune (EEK)",	"British Pound (GBP)","Hungarian Forien (HUF)","Lithuanan Litas (LTL)",
	"Latvian Lats (LVL)","Maltese Lira (MTL)","Polish Zloty (PLN)","Romanian Neu (RON)",
	"Swedish Koruna (SEK)","Slovak Koruna (SKK)", "Swiss Franc (CHF)","Icelandic krona (ISK)",
	"Norwegian krone (NOK)","Croatian Kuna (HRK)","Russian Rouble (RUB)",
	"Turkish lira (TRY)","Aus Dollar (AUD)","Canadian Dollar (CAD)","Chinese yuan (CNY)",
	"Hong Kong Dollar (HKD)","Indo Rupiah (IDR)","S. Korean Won (KRW)","Malaysian Ringgit (MYR)",	
	"New Zealand Dollar (NZD)","Philippine Peso (PHP)",
	"S'pore Dollar (SGD)","Thai Bath (THB)","S.African Rand (ZAR)"};
	
	double preload[] = {1.0,1.3897, 159.52, 1.9558, 0.5842, 27.483, 7.4476, 15.6466, 0.68525, 253.94, 3.4528, 0.7020, 0.4293, 3.7815, 3.3265, 9.2766, 33.662, 1.6415, 88.74, 7.8345, 7.3285, 35.2250, 1.7576, 1.6565, 1.4360, 10.4373, 10.8256, 13066.65, 1294.64, 4.8327, 1.9437, 64.357, 2.0987, 44.535, 9.9189};
	
	
	public Vector vector;
	public RecordStore currencyRecord;
	private CurrencyConvertForm ccf;
	long lastMod = 45655768L;

	public Foxrate()
	{
		form = new Form("Foxrate - Currency Converter");
		cgFrom = new ChoiceGroup("From",Choice.POPUP,currency,null);
		cgTo = new ChoiceGroup("To",Choice.POPUP,currency,null);
	
		
		
		aboutform = new AboutForm("About","foxrate client 1.0","/foxrate.png");
		aboutform.setHyperlink("http://foxrate.org",this);
		aboutform.setCommandListener(this);
		aboutform.setCopyright("Mohammad Hafiz","2007");
		aboutform.append("Free and Open Source Currency Converter");
		aboutform.append("\n\nThis program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License version 2.0");
		
		cmdExit = new Command("Exit",Command.EXIT,99);
		cmdAbout = new Command("About",Command.HELP,95);
		cmdConvert = new Command("Convert",Command.SCREEN,1);
		cmdUpdate = new Command("Update",Command.SCREEN,90);
		cmdSwap = new Command("Swap Currency",Command.SCREEN,80);
			
		siDate = new StringItem("Last Update","");
		siDate.setLayout(Item.LAYOUT_NEWLINE_BEFORE | Item.LAYOUT_EXPAND);
		
		form.append(cgFrom);
		form.append(cgTo);
		form.append(siDate);
		form.addCommand(cmdExit);
		form.addCommand(cmdConvert);
		form.addCommand(cmdSwap);
		form.addCommand(cmdAbout);
		form.addCommand(cmdUpdate);
		
		
		form.setCommandListener(this);
		
		display = Display.getDisplay(this);
		
		
		
		vector = new Vector(40);
	
	}
	
	public void startApp() {
		this.getSettings();
		
		display.setCurrent(form);
		this.getCurrency();
	}
	
	public void pauseApp() {
	
	}
	
	public void destroyApp(boolean flag) {
		this.saveSettings(false);
		this.saveCurrency(false,vector);	
	
	try {
		rs.closeRecordStore();
		rs = null;
		currencyRecord.closeRecordStore();
		currencyRecord=null;
		
	} catch (Exception e){
	
	}
	notifyDestroyed();

}
	
	
	public void commandAction(Command c, Displayable d)
	{
		if (c == cmdExit) {
			destroyApp(false);
		} else if ( c == cmdAbout) {
			display.setCurrent(aboutform);
			
		} else if ( c == aboutform.DISMISS_COMMAND) {
			display.setCurrent(form);
		} else if ( c == cmdUpdate) {
			retrieveData();
			
		} else if (c == cmdConvert) {
			ccf = new CurrencyConvertForm(this);
			display.setCurrent(ccf);
		} else if (c == cmdSwap) {
			swapCurrency();
		} 
	
	
	}
	
	
public void swapCurrency()
{
	
	int temp = cgFrom.getSelectedIndex();
	cgFrom.setSelectedIndex(cgTo.getSelectedIndex(),true);
	cgTo.setSelectedIndex(temp,true);

}



public void retrieveData() 
{
	GetData getdata = new GetData(this);
	getdata.start();

}
	
public void saveSettings(boolean firsttime)
{
	
	try { 
		//rs = Recordstore.openRecordStore("settings",true);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);
		
		try {
		
			dout.writeInt(cgFrom.getSelectedIndex());
			dout.writeInt(cgTo.getSelectedIndex());
			dout.writeLong(lastMod);
			//dout.close();
			
			byte[] data = bout.toByteArray();
			if (firsttime) {
				rs.addRecord(data,0,data.length);
			} else {
				rs.setRecord(1,data,0,data.length);
			}

			dout.close();
		} catch (RecordStoreException rse) {
			this.showAlert("Error saving settings");
		} catch (Exception ex) {
			showAlert(ex.toString());
		}
		
	} catch (Exception ex2) {
			showAlert(ex2.toString());
	}
		

}

public void getSettings()
{
	ByteArrayInputStream bin;
	DataInputStream din;
	try {
			rs = RecordStore.openRecordStore("settings",true);
			
			if (rs.getNumRecords() == 0) {
				this.saveSettings(true);
				
			}
			
			byte [] data = rs.getRecord(1);
			bin =  new ByteArrayInputStream(data);
			din = new DataInputStream(bin);
			
			cgFrom.setSelectedIndex(din.readInt(),true);
			cgTo.setSelectedIndex(din.readInt(),true);
			lastMod = din.readLong();
			din.close();
			bin.close();
			
			
	
	} catch (RecordStoreException rse) 
	{
		showAlert("Error retrieving currency data");
	} catch (Exception ex) {
		showAlert(ex.toString());
	}
 

}

public void showAlert(String text)
{
	Alert a = new Alert("Error",text,null, AlertType.ERROR);
	a.setTimeout(Alert.FOREVER);
	display.setCurrent(a,form);
	
}

public void getCurrency()
{
	
	try {
		ByteArrayInputStream bin;
		DataInputStream din;
		
		currencyRecord = RecordStore.openRecordStore("currency",true);
		//long longdate = currencyRecord.getLastModified();
		siDate.setText((new Date(this.lastMod)).toString());
		
		if ((System.currentTimeMillis() - lastMod)>691200000) {
			display.setCurrent(new FirstTimeForm(this,false));
		}

		if (currencyRecord.getNumRecords() == 0)	{
				//this.saveCurrency(true);
				display.setCurrent(new FirstTimeForm(this,true));
				return;
				
		}
		
		byte[] data = currencyRecord.getRecord(1);
		bin = new ByteArrayInputStream(data);
		din = new DataInputStream(bin);
		//lastMod = din.readLong();
		int currencyCount = din.readInt();
		vector =new Vector(40);
		
		for (int i = 0;i<currencyCount;i++) {
			vector.addElement(new Double (din.readDouble()));
		}
		
		bin.close();
		din.close();
		

	} catch (RecordStoreException rse){
		this.showAlert("Error retrieving currency data");
		
	} catch (Exception ex) {
		this.showAlert("General Exception - " + ex.toString());
	}

}

public void saveCurrency(boolean init,Vector kvector)
{
	//RecordStore currencyRecord= null;
	try {
	
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream (bos);
		currencyRecord = RecordStore.openRecordStore("currency",true);
		
		Enumeration elements  = kvector.elements();
		
		//dos.writeLong(lastMod);
		dos.writeInt(kvector.size());
		
		while(elements.hasMoreElements()) {
			dos.writeDouble(  ( ((Double) elements.nextElement()).doubleValue() )  );
		
		}
		byte[] data = bos.toByteArray();
		
		if (currencyRecord.getNumRecords() == 0) {
			currencyRecord.addRecord(data,0,data.length);
		} else {
			currencyRecord.setRecord(1,data,0,data.length);
		}
		dos.close();
		bos.close();
		//rs.closeRecordStore();
		
		
	} catch (RecordStoreFullException rsfe) {
		this.showAlert("Database full, unable to save currency data");
		
	} catch (RecordStoreException rse) {
		this.showAlert("Unable to save currency data");
		
	} catch (IOException ioex) {
		this.showAlert("IO Error. Unable to save currency data");
	
	} catch (Exception ex) {
		this.showAlert("Application Error - General Exception.");
		//ex.printStackTrace();		
	}
}

public void preloadData()
{
	for (int i=0;i<preload.length;i++) {
		lastMod = System.currentTimeMillis() - 660000000;
		vector.addElement(new Double(preload[i]));	
	}
	if (vector.size() > 10) {
		this.saveCurrency(false,vector);
	}

	

}



}

class GetData implements Runnable,CommandListener {

Foxrate midlet;
Gauge g;
Form frmRunning;
String sb;
public Command cmdCancel;

public GetData (Foxrate midlet)
{
	this.midlet = midlet;
	//this.vector = midlet.vector;
	g = new Gauge("Retrieving...",false,Gauge.INDEFINITE,Gauge.CONTINUOUS_RUNNING);
	frmRunning = new Form("Updating Currency Data");
	cmdCancel = new Command("Cancel",Command.CANCEL,5);
	frmRunning.addCommand(cmdCancel);
	frmRunning.setCommandListener(this);
	frmRunning.append(g);
	
	cmdCancel = new Command("Cancel",Command.CANCEL,5);
	frmRunning.addCommand(cmdCancel);

	
	midlet.display.setCurrent(frmRunning);
	sb = new String("wtf");
}

public void commandAction (Command cmd,Displayable disp)
{
	if (cmd == cmdCancel) {
		midlet.display.setCurrent(midlet.form);
		return;
	}

}

public void start() {
       Thread t = new Thread(this);
       t.start();
}

public void run() {
	HttpConnection conn=null;
	InputStream is=null;
	
	try {
			conn = (HttpConnection) Connector.open("http://stub.mypapit.net/currency/eurofxref.csv",Connector.READ);
			if (conn.getResponseCode() == HttpConnection.HTTP_OK) {
					is = conn.openInputStream();
					byte buf[] = new byte[512];
					int total =0;
					while (total < 512) {
						int count = is.read(buf,total,512-total);
						if (count<0) {
							break;
						}
						total += count;
					}
					
					sb = new String(buf,0,total);
					//midlet.form.append(sb);
					vectorized();
					//midlet.saveCurrency(false,midlet.vector);
					midlet.display.setCurrent(midlet.form);
			} else if (conn.getResponseCode() == HttpConnection.HTTP_NOT_FOUND) {
					midlet.showAlert("This application has expired. Please get a new version from http://foxrate.org/");
			
			} else {
				midlet.showAlert("Server busy or unavailable. Please try again later");	
			}

	} catch (SecurityException sex) {
		midlet.showAlert("Connection failed. You need to authorize this application to access network");
	} catch (IOException ioex) {
			midlet.showAlert("Connection failed. Please try again later.");
	} catch (Exception e){
		//midlet.showAlert(e.toString());
		midlet.display.setCurrent(midlet.form);
	} finally {
		try {
			
			if (is != null) {
				is.close();
			}
			
			if (conn != null) {
				conn.close();
			}
		} catch (IOException ioexception) {}
			is =null; 
			conn =null;
	
	
	}
	
	
	


}

/*
*
* Store retrieved data into Vector
*
*/

public void vectorized()
{
	midlet.lastMod = System.currentTimeMillis();
	midlet.vector = new Vector(40);
	Vector newVector = midlet.vector;
	StringTokenizer tok = new StringTokenizer(sb,"\n");
	tok.nextToken();
	sb = new String(tok.nextToken());
	tok = new StringTokenizer(sb,",");
	tok.nextToken();
	newVector.addElement(new Double(1.0));
	//System.out.println("Vector size : " + newVector.size());
	//int count=0;
	while (tok.hasMoreTokens()) {
		newVector.addElement(Double.valueOf(tok.nextToken()));
		//midlet.form.append("count " + (++count));
	}

	

}


}
