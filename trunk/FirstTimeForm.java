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
 * FirstTimeForm.java
 * Currency update dialog
 *
 * 
 * Currency exchange data taken from http://www.ecb.int/stats/exchange/eurofxref/html/index.en.html
 */

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class FirstTimeForm extends Form implements CommandListener
{
	Foxrate midlet;
	Command cmdYes,cmdNo;
	boolean init;
		

	public FirstTimeForm(Foxrate midlet, boolean init){
		super("Update Currency");
		this.init = init;
		this.midlet =midlet;
		cmdYes = new Command("Yes",Command.OK,1);
		cmdNo = new Command("No",Command.CANCEL,2);
		this.addCommand(cmdYes);
		this.addCommand(cmdNo);
		this.setCommandListener(this);
		if (init) {
			this.append("Foxrate was started for the first time, do you want to load latest currency data from remote server ?");
		} else {
			this.append("Currency data has not been updated for more than 7 days. Update now ?");
		}
		
	
	}
	
	public void commandAction(Command c,Displayable d)
	{
		if (c == cmdYes) {
			midlet.retrieveData();
		} else if (c == cmdNo) {
			if (init) {
				midlet.preloadData();
				midlet.showAlert("Default currency data loaded.\nWarning: Data might not be accurate");
			} else {
				midlet.display.setCurrent(midlet.form);
			}
		}
		
	
	}

}
