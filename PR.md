## PR submission

For this assignment I felt that the task was much larger than a typical single PR should contain, so I will break my
comments down into separate commits.

### refactored Loadfile to be more generic and extensible (bdfbecf)
Purpose: The App needs to be able to support multiple load file types.

Implementation:
1. Moved all Loadfile instantiation logic to a new abstract class `Loadfile` which will handle extension selection,
file path determination, and eventually validation

2. Minorly updated `OptLoadfile` class to implement the parent methods and use the parent `BufferedReader`


### added LFP file support(54816b5)
Purpose: Add support for the `.lfp` file type and extension

Implementation:
1. Created `LfpLoadfile` class and implemented logic necessary to render these files into the common `LoadfileEntry` type
2. Added `.lfp` and `LfpLoadFile` to the switch statement in `Loadfile.from` which handles instantiation of different
loadfile types
3. Added tests and sample data for this new file type.

### added DocumentsExistValidator and test(c993dcc)
Purpose: The app should support validating load files based on various conditions.  The first condition is that all files
listed by the loadfile should exist at the listed path.

Implementation:
1. Added abstract `Validator` class in `Loadfile`.  This class serves as the base for all validation methods.
2. Added `validate` method in `Loadfile`.  This is the public facing method which allows consumers to validate the given file
3. Added `DocumentsExistValidator` which confirms that each file listed in the loadfile also exists at the intended path
relative to the loadfile itself
4. Updated `App` to print data from the validators
5. Added tests and sample data for the new validator

## Added FileExtensionValidator and updated validate result(7557c5a)
Purpose:  The app should validate that file extensions listed for files in the loadfile match a list of supported types.

Implementation:
1. Added `FileExtensionValidator` class which checks each `LoadfileEntry` and ensures that the extension for that entry
is supported.  Currently only supports `.tif`, but more can be added by simply adding to the list in the validator.
2. Updated `Loadfile.validate` logic to return a `Result` object which lists the failed entries, and which validation they
failed for.
3. Updated `App` to support the new `validate` return object
4. Added tests for `FileExtensionValidator`

## Added XLF support, Jackson for XML processing(5a7fc88)
Purpose: The app should support the XLF loadfile format.  XLF has a XML style formatting

Implementation:
1. Added an XML processing library: Jackson.  This felt like a necessary decision because XML processing is complex and 
is something we should not have to support.  This library is very well maintained and documented.
2. Added and implemented `XlfLoadfile` which extends the common `Loadfile` class.  Inside this class are several dataclasses
which are used to model the XML data.  
3. Added `.xlf` and `XlfLoadfile` to the switch statement in `Loadfile.from` method
4. Added tests and sample data for `XlfLoadfile`

## added Coroutines for background processing of heavier tasks(33c2340)
Purpose: The creation and validation of loadfile objects could be very heavyweight tasks depending on the size of the loadfile.
These tasks should not run on the main application thread.

Implementation:
1. Added the kotlinx:coroutines library to build.gradle.  Coroutines provide excellent syntax and optimization for threaded
tasks.  This library is supported along side Kotlin by it's creators.
2. Updated `Loadfile.validate` and `Loadfile.from` methods to return a `Deferred` object wrapping their respective values.
These methods also now launch a single coroutine each to do their work.  A slightly more ideal situation would be for each
sub-task inside these methods to do their work in a separate coroutine, thus allowing the system to better optimize smaller
tasks.  I decided not to go this route for the sake of not over complicating this assignment.
3. Updated the `main` method to properly utilize the new `Deferred` return type.  Also added a `while..sleep`loop with a
timeout to demonstrate the asynchronous nature of the `Loadfile` methods.  Depending on application specifications, the
main method could be converted to a `suspend fun` to simply wait for the coroutine to finish.

## Assumptions
During this assignment I made some assumptions and tradeoffs I would like to point out

1.  I noticed one of the loadfiles used `"\"` as a file separator which caused some issues with file paths on my OSX machine.
For the purpose of file existance checking, I decided to normalize all file separators to whatever `File.separator` returned
on the given machine.

2.  I have implemented little or no error handling in most subclasses.  Because no recovery from these errors was possible,
it seemed reasonable to allow them to bubble up to the main `try/catch` statement in `App`.

3. As stated in the last commit description, the large tasks have been offloaded to single coroutines, when ideally they
would be split into much smaller pieces of work (searching for a single file, running a single validator, etc).

4. Modified the `App` logic to demonstrate the asynchronous nature of the heavy methods.  I in no way condone the `while..`
loop as a reasonable way wait to for tasks to finish, etc.

5. I have created no new directories in the `src` or `test` folders.  Ideally as this codebase grows it would be organized
into `/validators`, `/load_file_formats`, etc.  I thought leaving the structure flat would make reviewing easier.

6. I have implemented no string validation or case sensitivity checking.  The requirements do not lay out any guidance around
these subjects, and I have knowingly decided to omit them for the sake of time in this assignment.  This app saves no data,
so the risk of providing no validation should be fairly limited

7. I have implemented no path boundary checking in the places where files are loaded.  It may be possible to manipulate
the paths to gain some sort of insight into the larger filesystem on the machine, but because this app does not have side-affects
on the filesystem and only prints limited amounts of data, this risk feels reasonable for the sake of this assignment.