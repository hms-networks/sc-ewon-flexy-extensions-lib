# Ewon Flexy Extensions Library Changelog

## Version 1.13.8
### Features
- Updated SCAppManagement.enableAppAutoRestart() to return a boolean indicating if the auto-restart functionality was successfully enabled.
- Added SCCountdownLatch.getCount() method to return the current count of the latch without blocking.
### Bug Fixes
- N/A

## Version 1.13.7
### Features
- Added support for managing the MQTT keep alive interval in ConstrainedMqttManager.java.
### Bug Fixes
- N/A

## Version 1.13.6
### Features
- N/A
### Bug Fixes
- Fixed an issue in ApplicationControlApiListener.java where constants were not properly scoped for access by subclasses.

## Version 1.13.5
### Features
- Added support for registering custom API forms in ApplicationControlApiListener.java.
### Bug Fixes
- N/A
### Other
- Updated code formatting to comply with Google Java Format v1.16.

## Version 1.13.4
### Features
- Added connection check before publishing MQTT messages.
- Improved MQTT manager error handling.
### Bug Fixes
- Removed unsupported Java 7 references to the 'java.nio' package.
- Removed try number increment in AutomaticRetryCode.getCurrentTryNumber().
### Other
- Reduced default MQTT keep alive internal to from 60 to 10 seconds to detect connection issues faster.

## Version 1.13.3
### Features
- N/A
### Bug Fixes
- Fixed minimum firmware version.
### Other
- N/A

## Version 1.13.2
### Features
- Added Ewon firmware version checking.
  - Allows for applications to specify and enforce a minimum firmware version.
### Bug Fixes
- N/A
### Other
- N/A

## Version 1.13.1
### Features
- Removed loading of jar to Ewon via FTP during install phase.
### Bug Fixes
- N/A
### Other
- Removed unused IDE run configurations.

## Version 1.13.0
### Features
- Added HashUtilities.java class to allow for easy hashing (MD5 and SHA-1) of strings, files, and byte arrays.
- Added SecurityProviderUtilites.java class to provide a simple API for adding and removing security providers in the JVM.
- Added SCWonkaSecurityProvider.java class to allow access to the Wonka JVM MD5 and SHA-1 MessageDigest algorithms.
  - SecurityProviderUtilities.java can be used to add SCWonkaSecurityProvider.java to the JVM.
### Bug Fixes
- N/A
### Other
- N/A

## Version 1.12.1
### Features
- N/A
### Bug Fixes
- N/A
### Other
- Deprecated the PreAllocatedStringBuilder.java class in favor of the Java native StringBuffer class

## Version 1.12.0
### Features
- Added additional status codes in MqttStatusCode class
- Added ConstrainedMqttManager.java (a wrapper of MqttManager.java) to handle common MQTT use cases
- Added SCHttpUtility methods to output HTTP request response data to a file
### Bug Fixes
- Fixed improper Javadoc in AutomaticRetryCodeLinear.java
### Other
- Simplified MqttManager.java by internally getting status and passing to implementation methods

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
- Add check and exception for circularized file errors
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