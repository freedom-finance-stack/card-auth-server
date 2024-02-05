# Contributing to Card Auth Server

Card Auth Server(ACS & Admin) is a free and open project, and we love to receive contributions from our community — you!
There are many ways to contribute, from writing tutorials, improving the documentation, submitting bug reports 
and feature requests or writing code which can be incorporated into Card Auth Server itself.

If you are excited and want to make contributions, sign up for the 
[Freedom Finance Stack Contributor Program](https://razorpay.com/).

### Fork and clone the repository
You will need to fork the main Card Auth Server code repository and clone it to your local machine.
See [github help page](https://docs.github.com/en/get-started/quickstart/fork-a-repo) for help.

### Creating an Issue

Before **creating** an Issue for `features`/`bugs`/`improvements` please follow these steps:

1. search existing Issues before creating a new issue (has someone raised this already)
1. all Issues are automatically given the label status: waiting for triage and are automatically locked so no comments can be made
1. if you wish to work on the Issue once it has been triaged and label changed to status: ready for dev, please include this in your Issue description

### Working on an Issue (get it assigned to you)
Before working on an existing Issue please follow these steps:
1. only ask to be assigned 1 **open** issue at a time
2. comment asking for the issue to be assigned to you (do not tag maintainers on GitHub as all maintainers receive your comment notifications)
3. after the Issue is assigned to you, you can start working on it
4. **only** start working on this Issue (and open a Pull Request) when it has been assigned to you - this will prevent confusion, multiple people working on the same issue and work not being used
5. reference the Issue in your Pull Request (for example `closes #123`)
6. please do **not** force push to your PR branch, this makes it very difficult to re-review - commits will be squashed when merged

## Submitting Bug/Feature Reports
When initiating a new issue in the ACS issue tracker, you'll encounter a template designed to streamline the reporting process. If you suspect you've identified a bug, please complete the form following the template to the best of your ability. Don't worry if you can't address every detail; provide information where possible.

For a comprehensive evaluation of the report, we primarily need a description of the observed behavior and a straightforward test case that allows us to replicate the problem independently. The ability to recreate the issue is crucial for us to effectively diagnose and address it. Your cooperation in providing these details greatly assists our debugging efforts.

### Triaging a Bug/Feature Report
* After initiating an issue, it's common to engage in discussions, especially when contributors hold differing opinions on whether the observed behavior is a bug or a feature. This dialogue is an integral part of the process and should maintain a focused, constructive, and professional tone.

* It's important to avoid short, abrupt responses that lack additional context or supporting details, as they can be perceived as unhelpful and unfriendly. Contributors are encouraged to contribute to a positive and collaborative environment by providing constructive input and assisting each other in making progress.

* If you find an issue that you believe doesn't need fixing or if you encounter information you think is incorrect, share your perspective with additional context and be open to being convinced otherwise. This approach helps us collectively reach accurate resolutions more efficiently.

### Resolving a Bug/Feature Report
In most instances, resolving issues involves the creation of a Pull Request (PR). The steps for initiating and reviewing a PR mirror those of opening and triaging issues. However, the PR process includes a crucial review and approval workflow to guarantee that the proposed changes adhere to the minimal quality and functional standards set by the hyperswitch project.

### Raising a PR
When creating a new Pull Request on GitHub, you'll encounter a template that should be completed. While it's encouraged to provide as much detail as possible, feel free to omit sections if you're uncertain about the information to be included. Aim to complete the template to the best of your ability.

## Contributing code and documentation changes
If you would like to contribute a new feature or a bug fix to Card Auth Server, please discuss your idea first on the GitHub issue.
The process for contributing to Card Auth Server can be found below.

### Tips for code changes
Following these tips prior to raising a pull request will speed up the review cycle.

* Add appropriate unit tests (details on writing tests can be found in the TESTING file)
* Add integration tests, if applicable
* Make sure the code you add follows the formatting guidelines
* Lines that are not part of your change should not be edited (e.g. don't format unchanged lines, don't reorder existing imports)
* Add the appropriate license headers to any new files

### Contributing to the Card Auth Server codebase
- Select the project to contribute.
- Fork the repo into your GitHub account.
- Clone th repo which you forked.
- Make the changes.
- Push and create a PR for the issue/feature.
### Submitting your changes

Once your changes and tests are ready to submit for review:
1. Test your changes
<<<<<<< Updated upstream
2. Sign the Contributor License Agreement
3. Rebase your changes
4. Submit a pull request
***

#### Formatting
Sevure Auth Server code is automatically formatted with [spotless](https://github.com/diffplug/spotless).
=======
2. Take the latest pull from master
3. Create new branch and checkout to the new branch
4. Commit your changes on the new branch
5. Submit a pull request
6. > IMP: Before pushing your changes please check [formatting plugin](#formatting) otherwise build can fail.

### Formatting
Card Auth Server code is automatically formatted with [spotless](https://github.com/diffplug/spotless).
>>>>>>> Stashed changes

Alternative manual steps for IntelliJ.

1. Install Google-Java-Format Plugin

####  Java Language Formatting Guidelines

Java files in the Card Auth Server codebase are automatically formatted using the [Spotless Maven plugin](https://github.com/diffplug/spotless/tree/main/plugin-maven). The formatting check is run automatically via the precommit task, but it can be run explicitly with:

```
mvn spotless:apply
```

> Note: Persistent non-compliance with this Contributing Guide can lead to a warning and/or ban under the [Code of Conduct](https://github.com/EddieHubCommunity/BioDrop/blob/main/CODE_OF_CONDUCT.md)