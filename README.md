# Maledict
__An Interactive Tool for Learning Linear and Differential Cryptanalysis of SPNs__

Maledict is a Java application intended as a pedagogical tool for the learning of linear and differential cryptanalysis
(LDC) of substitution-permutation network (SPN) ciphers. The tool supports the definition of SPNs with bijective S-boxes
of uniform size, and block sizes up to 64 bits. The intended use of this tool is as a supplementary instructional tool
in a classroom environment teaching foundational cryptography.

Using this code and continuing to build upon it is encouraged.

Copyright &copy; 2016  Mike Carpenter
 
> This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
> Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
> later version.
> 
> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
> warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
> details.
> 
> You should have received a copy of the GNU Lesser General Public License along with this program. If not, see
> <<http://www.gnu.org/licenses/>>.


## Building and Running

Maledict is built using Gradle. It can be run using the `ui:run` command. It is recommended to exclude the `test` task
when building without having made any changes, as it is a long-running task and false-negatives in some fo the tests may
incorrectly halt the build task. Thus the typical command that should be used to run this application is
`./gradlew ui:run -x test` (on Mac/Linux) or `gradlew ui:run -x test` (on Windows).


## Using Maledict

### New or Load

Upon starting Maledict, you will be prompted to either start a new SPN or load a previously-saved one. SPNs are saved as
XML files serialized using XStream ant may be loaded through this dialog. A sample file, `test-spn.xml`, is included in
the root of this repository.

### SPN Definition Dialog

When a SPN has been loaded (or you have chosen to create a new one) you will be presented with the SPN Definition
Dialog. On this screen you may define individual components by right-clicking them in the tree structure and choosing
"Edit," which will take you to that component's Definition Dialog. Each Definition Dialog contains a Help button to
explain the component.

You may also save a reference to a component and copy it over another component in the SPN structure using the same
right-click menu.

### Linear and Differential Cryptanalysis

To perform Linear and Differential Cryptanalysis, use the Analyze menu on the SPN Definition Dialog. Upon completion of
analysis and retrieval of a subkey, you will be prompted to save a report. This will be saved in the directory of your
choosing as an HTML file and accompanying PNG showing the cipher approximation laid over the SPN structure.
