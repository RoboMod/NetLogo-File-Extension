/*************************************************************************
 * Copyright (C) 2013 Andreas Ihrig
 *
 * File: FileExtension.java
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
import java.util.*;
import java.io.IOException;

public class FileExtension extends DefaultClassManager {
  public void load(PrimitiveManager primitiveManager) {
    primitiveManager.addPrimitive("user-file-filtered", new UserFileFiltered());
    
    primitiveManager.addPrimitive("zip-file-entries", new CompressedFileManager.ZipFileEntries());
    primitiveManager.addPrimitive("zip-file-open", new CompressedFileManager.ZipFileOpen());
    
    primitiveManager.addPrimitive("zip-entry-exists?", new CompressedFileManager.ZipEntryExists());
    primitiveManager.addPrimitive("zip-entry-open", new CompressedFileManager.ZipEntryOpen());
    primitiveManager.addPrimitive("zip-entry-lines", new CompressedFileManager.ZipEntryLines());
    primitiveManager.addPrimitive("zip-entry-read-line", new CompressedFileManager.ZipEntryReadLine());
    primitiveManager.addPrimitive("zip-entry-at-end?", new CompressedFileManager.ZipEntryAtEnd());
    
    primitiveManager.addPrimitive("zip-file-extract", new CompressedFileManager.ZipFileExtract());
    primitiveManager.addPrimitive("zip-file-pack-dir", new CompressedFileManager.ZipFilePackDir());
  }
} 
