# NetLogo file extension

This extension introduces some extra file primitives to NetLogo.

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

## Credits

Created by Andreas Ihrig (alias RoboMod).

## License

NetLogo-File-Extension is distributed under the [GNU General Public License version 2 (GPLv2)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.html).
