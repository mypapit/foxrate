/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as 
 *  published by the Free Software Foundation
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
 * CurrencyConvertForm.java
 * Currency Conversion Interface
 *
 * 
 * Currency exchange data taken from http://www.ecb.int/stats/exchange/eurofxref/html/index.en.html
 */

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import java.util.*;


public class CurrencyConvertForm extends Form implements CommandListener,ItemStateListener
{

public TextField tfFrom,tfTo;
public Foxrate midlet;
private Command cmdBack,cmdConvert,cmdSwap;

public CurrencyConvertForm(Foxrate midlet) {
	super("Foxrate - Currency Converter");
	this.midlet = midlet;
	
	
	String from = midlet.cgFrom.getString(midlet.cgFrom.getSelectedIndex());
	String to = midlet.cgTo.getString(midlet.cgTo.getSelectedIndex());
	tfFrom = new TextField(from,"",12,TextField.DECIMAL);
	tfTo = new TextField(to,"",12,TextField.DECIMAL|TextField.UNEDITABLE);
	
	cmdConvert = new Command("Convert",Command.SCREEN,1);
	cmdBack = new Command("Back",Command.BACK,2);
	cmdSwap = new Command("Swap Currency",Command.SCREEN,3);
	
	addCommand(cmdConvert);
	addCommand(cmdBack);
	addCommand(cmdSwap);
	
	append(tfFrom);
	append(tfTo);
	
	this.setCommandListener(this);
	this.setItemStateListener(this);
	


}

public void commandAction(Command c, Displayable d)
{
	if (c == cmdConvert) {
		convertCurrency();
	} else if (c == cmdBack) {
		midlet.display.setCurrent(midlet.form);
	} else if ( c == cmdSwap) {
		swapValue();
	}

}

public void swapValue()
{
	int temp = midlet.cgFrom.getSelectedIndex();
	midlet.cgFrom.setSelectedIndex(midlet.cgTo.getSelectedIndex(),true);
	midlet.cgTo.setSelectedIndex(temp,true);
	
	tfFrom.setLabel(midlet.cgFrom.getString(midlet.cgFrom.getSelectedIndex()));
	tfTo.setLabel(midlet.cgTo.getString(midlet.cgTo.getSelectedIndex()));
	tfFrom.setString(tfTo.getString());
	
	convertCurrency();


}


public void convertCurrency()
{
			Vector vector = midlet.vector;
			if (tfFrom.getString().length() < 1) {
				return;				
			}

			Double from = (Double) vector.elementAt(midlet.cgFrom.getSelectedIndex());
			Double to = (Double) vector.elementAt(midlet.cgTo.getSelectedIndex());
			
			double rate = to.doubleValue()/from.doubleValue();
			double original = Double.parseDouble(tfFrom.getString());
			double result = to.doubleValue()/from.doubleValue() * Double.parseDouble(tfFrom.getString());
			
			
			tfTo.setString(""+(double)(int)((result+0.0005)*1000.0)/1000.0);
			//Double result = new Double (rate*original);
			//System.out.println("From " + from.toString() + "To " + to.toString() + " Value " + result.toString());
			

}

public void itemStateChanged(Item i)
{
		if ( i == tfFrom) {
			convertCurrency();
		}
	
}


}
