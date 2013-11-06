# NetLogo file extension

This extension introduces some extra file primitives to NetLogo.

## Building

Run `make`.

If compilation succeeds, `file.jar` and `file.jar.pack.gz` will be created.

## Installation

Copy `file.jar` and `file.jar.pack.gz` into `extensions/file/` in your NetLogo directory or your model directory.

## Usage

#### `file:user-file-filtered` filter filter-name

Works like the original reporter `user-file` but gives you the power to filter the files in the displayed dialog.
At the moment you can only define the extension name, e.g. `file:user-file "txt" "Text file"`.

## Credits

Created by Andreas Ihrig (alias RoboMod).

## License

NetLogo-File-Extension is distributed under the [https://www.gnu.org/licenses/old-licenses/gpl-2.0.html]GNU General Public License version 2 (GPLv2)