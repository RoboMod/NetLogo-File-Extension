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
import java.nio.charset.*;
import java.nio.file.*;

class CompressedFileManager {
  
  private static String currentFile = "";
  //private static ZipEntry currentEntry = "";
  //private static FileMode currentFileMode = FileModeJ.NONE();
  private static ZipInputStream in = null;
  private static ZipOutputStream out = null;
  private static ArrayList<String> entries = new ArrayList<String>();
  private static ArrayList<String> entryContent = new ArrayList<String>();
  private static int entryContentIndex = -1;

  // file manager functions
  public static void open(String filename) throws IOException {
    //set current file
    currentFile = filename;
  
    try {
      //open zip as input stream and reada entries
      in = new ZipInputStream(new FileInputStream(filename));
    
      entries = new ArrayList<String>();
      ZipEntry entry = in.getNextEntry();
      while(entry != null){
	if(!entry.isDirectory()) entries.add(entry.getName());
	
	entry = in.getNextEntry();
      }
    }
    catch (IOException e) {
      // if zip file couldn't be opened, there are no entries
      entries = new ArrayList<String>();
    }
    finally {
      in.close();
    }
      
//     //if mode is not read, handle this
//     if(mode == FileModeJ.NONE()) {
//       in.close();
//       System.err.println("file not open");
//     }
//     if(mode == FileModeJ.APPEND() || mode == FileModeJ.WRITE()) {
//       in.close();
//       out = new ZipOutputStream(new FileOutputStream(filename));
//     }
  }
  
  public static ArrayList<String> getEntries() {
    return entries;
  }
  
  public static void extract(String directory) throws Exception {
    if(currentFile == "") {
      throw new Exception("no file opened");
    }
  
    if (!AbstractWorkspace.isApplet()) {
      java.io.File destination = new java.io.File(directory);
      
      if(destination.exists() && destination.isDirectory()) {
	byte[] content = new byte[2048];
	int byteRead;
       
	BufferedOutputStream bout = null;
	in = new ZipInputStream(new BufferedInputStream(new FileInputStream(currentFile)));
	ZipEntry entry;
	while((entry = in.getNextEntry()) != null)
	{
	  byteRead = 0;
	  content = new byte[2048];
	  bout = new BufferedOutputStream(new FileOutputStream(new java.io.File(entry.getName(), directory)),2048);
	  while ((byteRead = in.read(content, 0, 2048)) != -1)
	  {
	    bout.write(content, 0, byteRead);
	  }
	  bout.flush();
	  bout.close();
	}
	in.close();
      }
    }
  }
  
  public static void packDir(String directory) throws Exception {
    if(currentFile == "") {
      throw new Exception("no file opened");
    }
  
    if (!AbstractWorkspace.isApplet()) {
      java.io.File source = new java.io.File(directory);
      
      if(source.exists() && source.isDirectory()) {
	byte[] content = new byte[2048];
	int byteRead;
       
	BufferedInputStream bin = null;
	out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(currentFile), 2048));
	ZipEntry entry;
	//DirectoryStream<Path> stream = Files.newDirectoryStream(FileSystem.getPath(directory));
	java.io.File[] list = source.listFiles();
	for (java.io.File file: list) {
	    entry = new ZipEntry(file.getName());
	    out.putNextEntry(entry);
	    byteRead = 0;
	    content = new byte[2048];
	    bin = new BufferedInputStream(new FileInputStream(file));
	    while ((byteRead = bin.read(content, 0, 2048)) != -1) {
	      out.write(content, 0, byteRead);
	    }
	    bin.close();
	    out.closeEntry();
	}
	//stream.close();
	out.close();
      }
    }
  }
  
  public static void openEntry(String entry) throws Exception {
    if(entries.indexOf(entry) == -1) {
      throw new Exception("entry doesn't exist");
    }
    
    // open zip stream
    in = new ZipInputStream(new FileInputStream(currentFile));
    // search for entry
    ZipEntry zipEntry = in.getNextEntry();
    while(zipEntry != null){
      if(zipEntry.getName() == entry) break;
      
      zipEntry = in.getNextEntry();
    }
    
    if(zipEntry != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(entry), Charset.forName("UTF-8")));
      String line;
      entryContent.clear();
      while ((line = reader.readLine()) != null) {
	entryContent.add(line);
      }
      entryContentIndex = 0;
    }
    in.close();
  }
  
  public static String entryReadLine() throws Exception {
    if(entryContentIndex >= entryContent.size()) {
      throw new Exception("out of bounds");
    }
    return entryContent.get(entryContentIndex++);
  }
  
  public static Boolean entryAtEnd() {
    return entryContent.size() > 0 & (entryContentIndex >= 0) & (entryContentIndex < entryContent.size());
  }
  
  // zip file primitives
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
	open(filename);
      }
      catch(IOException e) {
	throw new ExtensionException(e.getMessage());
      }
    }
  }
  
  public static class ZipFileEntries extends DefaultReporter {
    public Syntax getSyntax() {
      return Syntax.reporterSyntax(new int[] {}, Syntax.ListType());
    }
    
    public Object report(Argument args[], Context context) throws ExtensionException {
//       String filename;
//       try {
// 	filename = args[0].getString();
//       }
//       catch(LogoException e) {
// 	throw new ExtensionException( e.getMessage() );
//       }
      
//       if(currentFile == "") {
// 	throw new ExtensionException( "no file opened" );
//       }
//	there is no check need cause the entries are empty if no file is opened
      
      LogoListBuilder listbuilder = new LogoListBuilder();
      for(String entry : entries) {
	listbuilder.add(entry);
      }
      
      return listbuilder.toLogoList();
    }
  }
  
  public static class ZipFileExtract extends DefaultCommand {
    public Syntax getSyntax() {
      return Syntax.commandSyntax(new int[] {Syntax.StringType()});
    }
    
    public void perform(Argument args[], Context context) throws ExtensionException {
      String dirname;
      try {
	dirname = args[0].getString();
      }
      catch(LogoException e) {
	throw new ExtensionException( e.getMessage() ) ;
      }
      
      try {
	extract(dirname);
      }
      catch (Exception e) {
	throw new ExtensionException( e.getMessage() );
      }
    }
  }
  
  public static class ZipFilePackDir extends DefaultCommand {
    public Syntax getSyntax() {
      return Syntax.commandSyntax(new int[] {Syntax.StringType()});
    }
    
    public void perform(Argument args[], Context context) throws ExtensionException {
      String dirname;
      try {
	dirname = args[0].getString();
      }
      catch(LogoException e) {
	throw new ExtensionException( e.getMessage() ) ;
      }
      
      try {
	packDir(dirname);
      }
      catch (Exception e) {
	throw new ExtensionException( e.getMessage() );
      }
    }
  }
  
  // zip entry primitives
  public static class ZipEntryExists extends DefaultReporter {
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
  
  public static class ZipEntryOpen extends DefaultCommand {
    public Syntax getSyntax() {
      return Syntax.commandSyntax(new int[] {Syntax.StringType()});
    }
    
    public void perform(Argument args[], Context context) throws ExtensionException {
      String entryname;
      try {
	entryname = args[0].getString();
      }
      catch(LogoException e) {
	throw new ExtensionException( e.getMessage() ) ;
      }
      
      try {
	openEntry(entryname);
      }
      catch (Exception e) {
	throw new ExtensionException( e.getMessage() );
      }
    }
  }
  
  public static class ZipEntryReadLine extends DefaultReporter {
    public Syntax getSyntax() {
      return Syntax.reporterSyntax(new int[] {}, Syntax.StringType());
    }
    
    public Object report(Argument args[], Context context) throws ExtensionException {
      try {
	return entryReadLine();
      }
      catch (Exception e) {
	throw new ExtensionException( e.getMessage() );
      }
    }
  }

  public static class ZipEntryAtEnd extends DefaultReporter {
    public Syntax getSyntax() {
      return Syntax.reporterSyntax(new int[] {}, Syntax.BooleanType());
    }
    
    public Object report(Argument args[], Context context) throws ExtensionException {
      try {
	return entryAtEnd();
      }
      catch (Exception e) {
	throw new ExtensionException( e.getMessage() );
      }
    }
  }
}