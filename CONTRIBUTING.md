# Contributing to Secure Auth Server

Secure Auth Server(ACS Server & Admin Server) is a free and open project and we love to receive contributions from our community — you! There are many ways to contribute, from writing tutorials, improving the documentation, submitting bug reports and feature requests or writing code which can be incorporated into Secure Auth Server itself.

If you are excited and want to make contributions, sign up for the [Freedom Finance Stack Contributor Program](https://razorpay.com/).

## Bug Reports

## Feature Requests

## Contributing code and documentation changes
If you would like to contribute a new feature or a bug fix to Secure Auth Server, please discuss your idea first on the GitHub issue.

The process for contributing to Secure Auth Server can be found below.

### Fork and clone the repository
You will need to fork the main Secure Auth Server code repository and clone it to your local machine. See [github help page](https://docs.github.com/en/get-started/quickstart/fork-a-repo) for help.

### Tips for code changes
Following these tips prior to raising a pull request will speed up the review cycle.

* Add appropriate unit tests (details on writing tests can be found in the TESTING file)
* Add integration tests, if applicable
* Make sure the code you add follows the formatting guidelines
* Lines that are not part of your change should not be edited (e.g. don't format unchanged lines, don't reorder existing imports)
* Add the appropriate license headers to any new files

### Submitting your changes

Once your changes and tests are ready to submit for review:

1. Test your changes
2. Sign the Contributor License Agreement
3. Rebase your changes
4. Submit a pull request

### Contributing to the Secure Auth Server codebase

#### Prerequisites

#### Importing the project into IntelliJ IDEA

#### Formatting
Sevure Auth Server code is automatically formatted with [spotless](https://github.com/diffplug/spotless).

Alternative manual steps for IntelliJ.

1. Install Google-Java-Format Plugin

####  Java Language Formatting Guidelines

Java files in the Secure Auth Server codebase are automatically formatted using the [Spotless Maven plugin](https://github.com/diffplug/spotless/tree/main/plugin-maven). The formatting check is run automatically via the precommit task, but it can be run explicitly with:

```
mvn spotless:apply
```