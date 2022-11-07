# Ewon Flexy Extensions Library Changelog

## Version 1.12.0-pre7
### Features
- Added additional known status codes in MqttStatusCode class
- Added SmartMqttManager wrapper of MqttManager to handle common use cases
- Added SCHttpUtility methods to output response data to a file
### Bug Fixes
- Fixed improper Javadoc in AutomaticRetryCodeLinear
### Other
- Simplified MqttManager by internally getting status and passing to implementation methods 

## Version 1.11.3
### Features
- N/A
### Bug Fixes
- Fixed exception when disabling historical data queue max behind time enforcement
### Other
- Add historical data queue max behind time accessor

## Version 1.11.2
### Features
- Added option to disable historical data queue max behind time enforcement
### Bug Fixes
- Added missing and updated outdated javadocs
- Added missing circularization check to historical data string EBD calls
### Other
- General code cleanup

## Version 1.11.1
### Features
- N/A
### Bug Fixes
- Fix inverted logic with WAN IP detection

## Version 1.11.0
### Features
- Added support for application pause when no WAN IP address is available
### Bug Fixes
- N/A
 
## Version 1.10.0
### Features
- Added application arguments parser class
### Bug Fixes
- N/A

## Version 1.9.0
### Features
- Added application control API class
### Bug Fixes
- N/A

## Version 1.8.0
### Features
- Added support for topic name in tag creation
  - Added helper methods for persistent MEM tag creation
### Bug Fixes
- N/A

## Version 1.7.0
### Features
- Added Base64 utility class from public domain
### Bug Fixes
- N/A

## Version 1.6.0
### Features
- Added new method setQueueFifoTimeSpanMins
- Add maximum duration that historical fifo can get behind (48 hours)
- Make maximum duration configurable by library user 
- Changed historical log read to stream from file based 
- Add check and exception fo circularized file errors
- Add function for library user to advance historical tracking time
- Add eventfile module for reading the Flexy event file
- Add alarm configuration to tag information objects
### Bug Fixes
- Fix bug where historical data log overwritten by is circularized file
- Add missing Javadocs parameters

## Version 1.5.0
### Features
- Add milliseconds to days, hours, minutes, seconds utility method
- Add local time zone utilities
- Add data point method to get time stamp as date object
- Update ISO 8601 date formats for proper time zone handling
- Add method to get value of the Ewon 'Record data in UTC' option
- Add method to get historical data queue running behind time in milliseconds
- Add method to get ISO 8601 formatted timestamp for a given data point
### Bug Fixes
- Fix bug preventing local time offset calculation only on first call
### Other
- Combined duplicate time libraries

## Version 1.4.0
### Features
- Added support for the tag unit in TagInfo objects
- Added system.threading package
  - Added CountdownLatch implementation
- Updated local time offset calculator
  - Removed dependency on system credentials
- Added utility for getting string contents of an input stream
### Bug Fixes
- N/A
### Other
- N/A

## Version 1.3.1
### Features
- N/A
### Bug Fixes
- Fix bug preventing access of HTTP timeout setting function.
### Other
- N/A

## Version 1.3.0
### Features
- Added system.http package
  - Perform HTTP GET and POST requests
  - Set Ewon system HTTP timeouts
### Bug Fixes
- N/A
### Other
- N/A

## Version 1.2.0
### Features
- Added system.application.SCAppManagement.java
### Bug Fixes
- N/A
### Other
- N/A

## Version 1.1.2
### Features
- Added multi-character delimiter support to StringUtils split method
### Bug Fixes
- N/A
### Other
- N/A

## Version 1.1.1
### Features
- Added SCTimeFormat ISO 8601 date format constant
### Bug Fixes
- N/A
### Other
- Update date/time format of alarm messages to ISO 8601
- Update date/time format of local data files to ISO 8601.

## Version 1.1.0
### Features
- Added system.info.SCSystemInfo.java
### Bug Fixes
- N/A
### Other
- Trimmed trailing whitespace in markdown files
- Fixed spelling in SCTagUtils.java
- Fixed spelling in CONTRIBUTING.md

## Version 1.0.0
### Features
- Initial Connector Release
### Bug Fixes
- N/A
### Other
- N/A