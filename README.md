# data-file-validation-tool

A standalone application for validating data files. Scope of the tool: flexibility to configure format validation as well as value assertion, ability to integrate with CI/CD pipeline as a command-line executable. For example, it can be used to validate the format of receive file from AZ DMV; it can be also used to validate expected values of an MVR report.

# Build the tool

To build project into an executable jar

```bash
./mvnw clean install
```

For Windows system
```bash
mvnw.cmd clean install
```

The build should be generated in `target` folder with name `data-file-validation-tool.jar`.

# Usage

With the built runnable jar file
```bash
java -jar data-file-validation-tool.jar <input file> [configuration file]
```
* Input file should be the data file for validating
* Configuration file is optional. If no configuration file is input then the [default configuration file](src/main/resources/config-default.json) would be used
* If the validation result is valid, exit code will be `0`, otherwise, `1`.

# The latest build note
The latest build can be conveniently downloaded in [`build`](build) folder. There is an example for trying in [`build/example`](build/example) folder. The example bases on validating receive file from AZ DMV, it includes: 
- [`config.json`](build/example/config.json) for receive file from AZ DMV
- a [spreadsheet file](build/example/AZ%20ADOA%20Portal%20Batch%20SBAF%20MVR%20Mapping-GTM%2013170%20final.xlsx) defines the format of receive file in _DMV RESULT_ tab.
- a [valid file](build/example/VALID_INPUT_DATA_SAMPLE.txt)
- an [invalid file](build/example/MISS_MATCHED_INPUT_DATA_SAMPLE.txt) and [its CSV output](build/example/MISS_MATCHED_INPUT_DATA_SAMPLE%20fields%20mismatched.csv)

Running the tool should generate output as below:
```bash
@localmachine:[data-file-validation-tool]$ cd build/
@localmachine:[build]$ ls -l
total 6320
-rw-r--r--  1 localuser  localuser  3229678 Jun 25 11:33 data-file-validation-tool.jar
drwxr-xr-x  6 localuser  localuser      192 Jun 25 11:46 example
@localmachine:[build]$ 
@localmachine:[build]$ java -jar data-file-validation-tool.jar example/MISS_MATCHED_INPUT_DATA_SAMPLE.txt example/config.json
Verification done with mismatched result. See more detail: 2020625115629 MISS_MATCHED_INPUT_DATA_SAMPLE fields mismatched.csv
@localmachine:[build]$ echo $?
1
@localmachine:[build]$ ls -l
total 6320
-rw-r--r--  1 localuser  localuser     1816 Jun 25 11:56 2020625115629 MISS_MATCHED_INPUT_DATA_SAMPLE fields mismatched.csv
-rw-r--r--  1 localuser  localuser  3229678 Jun 25 11:33 data-file-validation-tool.jar
drwxr-xr-x  6 localuser  localuser      192 Jun 25 11:46 example
@localmachine:[build]$ 
@localmachine:[build]$ java -jar data-file-validation-tool.jar example/VALID_INPUT_DATA_SAMPLE.txt example/config.json
Verification done with no error.
@localmachine:[build]$ echo $?
0
```


# Configuration definitions
The configuration file for the validation tool is a JSON-structure file that looks like below:
```json
{
  "configName": "configuration name",
  "description": "description about the configuration",
  "recordTypes": [{          
          "recordName": "record name",
          "recordDesc": "record description",
          "totalBytes": 50,
          "ignoreTotalBytes": true,
          "recognitionPattern": {
                "value": "^00+$",
                "length": 2,          
                "offset": 0
          },
          "fields": [{
                "name": "name of field",
                "offset": 0,
                "length": 2,
                "contents": "expected value in regular expressions format",
                "description": "description about the field",
                "ignore": true            
              }
          ]
      }
  ]
}
```
## General configuration

##### "configName":
The name of the configuration file
```textmate
ex: "configName": "AZ ADOA configuration for records types validation"
```

##### "description":
The description about the configuration file
```textmate
ex: "description": "DMV returns a single results file which will report a record 00 or record 01-12 per driver. This report need to validate"
```

#### "recordTypes":
A list of [configuration record types](#record-type-configuration)

## Record type configuration


##### "recordName":
The name of the record type
```textmate
ex: "recordName": "No Record Found"
```

##### "recordDesc":
The description about the record type
```textmate
ex: "recordDesc": "MVR Response Record"
```

##### "totalBytes":
The expected total characters of the record - numbers only
```totalBytes
ex: "totalBytes": 35 => total record length must be 35 characters
``` 

##### "ignoreTotalBytes" (optional):
Ignore validation of the total characters of the record - default to false
```textmate
ex: "ignoreTotalBytes": true => not validate the total characters of the record, but still validate the fields
```

##### "recognitionPattern":
The [recognition pattern](#recognition-pattern) of the record type 

##### "fields":
A list of [configuration field entries](#field-configuration)

## Recognition Pattern
##### "value":
The recognition pattern value. This value can be a text, or a regular expression (more about regular expressions see the sheet at https://www.jrebel.com/system/files/regular-expressions-cheat-sheet.pdf)
```
ex: The value for Record found record type can be:
- "value": "01"
- "value": "^00+$"
```
##### "offset":
The beginning index of Recognition Pattern - with positive number only (start from 0)
```textmate
ex: "offset": 0
``` 

##### "length":
The length of Recognition Pattern from the offset - positive number only (start from 1)
```textmate
ex: "length": 2
```

## Field configuration
##### "name":
The name of the configuration field
```textmate
ex: "name": "Company Account Number"
```

##### "offset":
The beginning index of character - with positive number only (start from 0)
```textmate
ex: "offset": 12 => the validation process start at offset 0 of the file
``` 

##### "length":
The length of field needing validating counting from the offset - positive number only (start from 1)
```textmate
ex: "length": 3 => starting from the input offset of the file, validating 3 characters for the field
```

##### "contents":
Expected content value for a configuration field. This value can be an array value, or a regular expression (more about regular expressions see the sheet at https://www.jrebel.com/system/files/regular-expressions-cheat-sheet.pdf) 
```textmate
ex: 
- For the Birthday field with "CCYYMMDD" format, the contents should be:
  "contents": "^[0-9]+$" => whole values of the configuration field should be number characters                           
- The Gender field: 
  "contents": ["MALE", "FEMALE", "UNKNOWN"] => the value of the configuration field should be either "MALE", "FEMALE" or "UNKNOWN"
- The Time Processed field with format "00:00:00.000":
  "contents": "^[0-9]{2}:[0-9]{2}:[0-9]{2}[.][0-9]{3}$" => the value should like "[2 number characters]:[2 number characters]:[2 number characters].[3 number characters]" 
```

##### "description":
Description about the configuration field, or the expected display value to show in the report
```textmate
ex: the description for Birthday field should be:
"description": "CCYYMMDD"
```

##### "ignore" (optional):
Ignore the configuration field - default to false
```textmate
ex: "ignore": true => not validate the configuration field
```

# The output of data-file-validation-tool 
After run validation tool, the output can be:

* "Verification done with no error" => The data file is valid without any error
* "Verification done with mismatched result. See more detail:..." => The data file is invalid with some mismatched data, and the detailed result of the verifying process is output to a CSV file

### Example output CSV file:

|Row number|Row value|Record type|Field name|Offset|Length|Description|Expected value|Actual value|
|---|---|---|---|---|---|---|---|---|
|2|01584030INS 056~AWVQND|01 Record Found, MVR Processed|Total Bytes|   |   |   |138|144|
|2|01584030INS 056~AWVQND|01 Record Found, MVR Processed|Time Processed|51|12|00:00:00.000|^[0-9]{2}:[0-9]{2}:[0-9]{2}[.][0-9]{3}$|18:00:2d.687|
|8|04584030INS Legal Name|04 AKA  Information|Date of Birth|167|8|CCYYMMDD|^[0-9]+$|0628Clas|

##### First Row
Miss matched data at row #2 from input file: 
* The content at row #2: "01584030INS 056~AWVQND..."
* The record type for checking with row #2: "01 Record Found, MVR Processed"
* Field miss matched name: "Total Bytes"
    * Expected total bytes of row #2: 138 bytes
    * Actual total bytes of row #2: 144 bytes
##### Second Row
Miss matched data at row #2 from input file:
* The content at row #2: "01584030INS 056~AWVQND..."
* The record type for checking with row #2: "01 Record Found, MVR Processed"
* Field miss matched name: "Time Processed"
    * The beginning index of character for Time Processed: 51
    * The length of Time Processed needing validating (beginning index from 51): 12
    * Expected display value for Time Processed: "00:00:00.000"
    * Expected value for Time Processed of row #2 is a regular expression: "^[0-9]{2}:[0-9]{2}:[0-9]{2}[.][0-9]{3}$"    
    * Actual value of Time Processed: "18:00:2d.687" (got 'd' as an invalid character)
##### Third Row
Miss matched data at row #8 from input file:
* The content at row #8: "04584030INS Legal Name..."
* The record type for checking with row #8: "04 AKA  Information"
* Field miss matched name: "Date of Birth"
    * The beginning index of character for Date of Birth: 167
    * The length of Date of Birth needing validating (beginning index from 167): 8
    * Expected display value for Date of Birth: "CCYYMMDD"
    * Expected value for Date of Birth of row #8 is a regular expression: "^[0-9]+$"
    * Actual value of Date of Birth: "0628Clas" (got "Clas" as invalid characters)