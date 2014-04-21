# NetLogo file extension

This extension introduces extra file primitives to NetLogo:

* <a href="#fileuser-file-filtered-filter-name-extension">`user-file-filtered`</a> adds a filter to the user file open dialog
* <a href="#filezip-file-open-filename">`zip-file-open`</a> opens zip files
* <a href="#filezip-file-entries">`zip-file-entries`</a> shows entries of opened zip file
* <a href="#filezip-entry-exists-entryname">`zip-entry-exists?`</a> checks if zip file entry exists
* <a href="#filezip-entry-open-entryname">`zip-entry-open`</a> opens a zip file entry
* <a href="#filezip-entry-lines">`zip-entry-lines`</a> shows number of lines of an opened entry
* <a href="#filezip-entry-read-line">`zip-entry-read-line`</a> reads next line of opened entry
* <a href="#filezip-entry-at-end">`zip-entry-at-end?`</a> checks if entry cursor reached the end
* <a href="#filezip-file-extract-filename-directory">`zip-file-extract`</a> extracts a zip file
* <a href="#filezip-file-pack-dir-directory-filename">`zip-file-pack-dir`</a> packs a directory to zip file

## Building

Run `make`.

If compilation succeeds, `file.jar` and `file.jar.pack.gz` will be created.

## Installation

Copy `file.jar` and `file.jar.pack.gz` into `extensions/file/` in your NetLogo directory or your model directory.

## Usage

### Add to included extensions

To use this extension you have to tell NetLogo about it. Add `extensions [file]` in front of your code. If you use multiple extensions just seperate them with spaces in the squared brackets.

### Functions:

#### `file:user-file-filtered` \<filter-name\> \<extension\>

Works like the original reporter `user-file` but gives you the power to filter the files in the displayed dialog.
At the moment you can only define one extension name, e.g. `file:user-file-filtered "Text file" "txt"`.

#### `file:zip-file-open` \<filename\>

Opens the given filename as zip file like the standard file-open primitive.

#### `file:zip-file-entries`

Returns a list of entries of an opened zip file.

#### `file:zip-entry-exists?` \<entryname\>

Checks if given entryname is part of the opened zip file.

#### `file:zip-entry-open` \<entryname\>

Reads in lines of given entryname. This maybe result in big ram usage, so you should not use this way for really big file.

#### `file:zip-entry-lines`

Returns the number of lines of an opened entry.

#### `file:zip-entry-read-line`

Returns the next line of an opened entry. To check if there is a next line use `zip-entry-at-end?`.

#### `file:zip-entry-at-end?`

Checks if the cursor for the opened entry content reached the end. Also returns false if there is no content at all.

#### `file:zip-file-extract` \<filename\> \<directory\>

Extracts all entries of the given file (has to be a zip file) to an exisiting directory.
    
#### `file:zip-file-pack-dir` \<directory\> \<filename\>

Packs all files in the given directory to the given file. There is no filename extension check yet.

## Credits

Created by Andreas Ihrig (alias RoboMod).

## License

NetLogo-File-Extension is distributed under the [GNU General Public License version 2 (GPLv2)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.html).
