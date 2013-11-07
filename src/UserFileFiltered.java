/*************************************************************************
 * Copyright (C) 2013 Andreas Ihrig
 *
 * File: UserFileFiltered.java
 *
 * This file is part of NetLogo-File-Extension.
 *
 * NetLogo-File-Extension is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * NetLogo-File-Extension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * or see <http://www.gnu.org/licenses/>.
 *
 */

package org.nlogo.extensions.file;

import org.nlogo.api.*;
import java.io.File;
import javax.swing.filechooser.*;
import javax.swing.JFileChooser; 
//import java.util.List;
import java.util.ArrayList;

public class UserFileFiltered extends DefaultReporter {
  // take one number as input, report a list
  public Syntax getSyntax() {
    return Syntax.reporterSyntax(
      new int[] {Syntax.StringType(), Syntax.RepeatableType() | Syntax.StringType()},
      Syntax.StringType() | Syntax.BooleanType(),
      2, 2); // default # of inputs, minimum # of inputs
  }
  
  public Object report(Argument args[], Context context)
      throws ExtensionException {
    ArrayList<String> extensions = new ArrayList<String>();
    String filtername;
    try {
      filtername = args[0].getString();
      
      for(int i = 1; i < args.length; i++) {
	extensions.add(args[i].getString());
      }
    }
    catch(LogoException e) {
      throw new ExtensionException( e.getMessage() ) ;
    }
    
    // create the file chooser
    final JFileChooser fc = new JFileChooser();
    fc.setDialogType(JFileChooser.OPEN_DIALOG);
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    String[] type = {};
    FileFilter filefilter = new FileNameExtensionFilter(filtername, extensions.toArray(type));
    fc.setFileFilter(filefilter);
    int returnVal = fc.showOpenDialog(null);
    
    String inputFileStr = "";
    
    // let user choose file
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File inputFile = fc.getSelectedFile();
      inputFileStr = inputFile.getAbsolutePath();
    }
    else {
      return Boolean.FALSE;
    }
    
    return inputFileStr;
  }
} 
