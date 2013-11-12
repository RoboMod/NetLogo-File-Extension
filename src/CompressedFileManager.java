/*************************************************************************
 * Copyright (C) 2013 Andreas Ihrig
 *
 * File: CompressedFileManager.java
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

import org.nlogo.workspace.*;
import org.nlogo.api.*;
import org.nlogo.api.FileModeJ;

import java.util.*;
import java.util.zip.*;
import java.io.*;

class CompressedFileManager {
  
  private static ZipInputStream in = null;
  private static ZipOutputStream out = null;
  private static ArrayList<String> entries = new ArrayList<String>();;

  public static void open(String filename, FileMode mode) throws IOException {
    //open zip as input stream and reada entries
    in = new ZipInputStream(new FileInputStream(filename));
    
    //entries = new ArrayList<String>();
    ZipEntry entry = in.getNextEntry();
    while(entry != null){
      if(!entry.isDirectory()) entries.add(entry.getName());
      
      entry = in.getNextEntry();
    }
    
    //if mode is not read, handle this
    if(mode == FileModeJ.NONE()) {
      in.close();
      System.err.println("file not open");
    }
    if(mode == FileModeJ.APPEND() || mode == FileModeJ.WRITE()) {
      in.close();
      out = new ZipOutputStream(new FileOutputStream(filename));
    }
  }
  
  public ArrayList<String> getEntries() {
    return entries;
  }
  
  public static class ZipFileOpen extends DefaultCommand {
    public Syntax getSyntax() {
      return Syntax.commandSyntax(new int[] {Syntax.StringType()});
    }
    
    public void perform(Argument args[], Context context) throws ExtensionException {
      String filename;
      try {
	filename = args[0].getString();
      }
      catch(LogoException e) {
	throw new ExtensionException( e.getMessage() ) ;
      }
      
      try {
	open(filename, FileModeJ.READ());
      }
      catch(IOException e) {
	throw new ExtensionException(e.getMessage());
      }
    }
  }
  
  public static class ZipEntryExists extends DefaultReporter {
    // take one number as input, report a list
    public Syntax getSyntax() {
      return Syntax.reporterSyntax(new int[] {Syntax.StringType()}, Syntax.BooleanType());
    }
    
    public Object report(Argument args[], Context context) throws ExtensionException {
      String filename;
      try {
	filename = args[0].getString();
      }
      catch(LogoException e) {
	throw new ExtensionException( e.getMessage() ) ;
      }
      
      for(String entry : entries) {
	if(filename.equals(entry)) return Boolean.TRUE;
      }
      
      return Boolean.FALSE;
    }
  }

  public static class ReadZipEntries extends DefaultReporter {
    // take one number as input, report a list
    public Syntax getSyntax() {
      return Syntax.reporterSyntax(new int[] {Syntax.StringType()}, Syntax.ListType());
    }
    
    public Object report(Argument args[], Context context) throws ExtensionException {
      String filename;
      try {
	filename = args[0].getString();
      }
      catch(LogoException e) {
	throw new ExtensionException( e.getMessage() ) ;
      }
      
      LogoListBuilder listbuilder = new LogoListBuilder();
      for(String entry : entries) {
	listbuilder.add(entry);
      }
      
      return listbuilder.toLogoList();
    }
  }
}