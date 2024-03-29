# Ewon Flexy Extensions Library Contribution Guidelines

Thank you for your interest in contributing to the project.

All types of contributions are valued and encouraged, though there are a few guidelines which must be followed to make the contribution process easy and consistent. See the [Table of Contents](#table-of-contents) for different ways to help and details about how this project handles them.

If you like the project, but don't have time to contribute, there are other easy ways to show your support and appreciation, such as:
- Star the project
 - Refer to this project in another project
 - Mention or share the project at local meetups and tell your colleagues

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Asking Questions](#asking-questions)
- [How to Contribute](#how-to-contribute)
  - [Reporting Bugs and Errors](#reporting-bugs-and-errors)
  - [Suggesting Features or Enhancements](#suggesting-features-or-enhancements)
  - [Making Modifications or Updates](#making-modifications-or-updates)
    - [Documentation](#documentation)
    - [Source Code](#source-code)
  - [Committing and Merging Modifications and Updates](#committing-and-merging-modifications-and-updates)
    - [Commit Scope](#commit-scope)
    - [Commit Messages](#commit-messages)
    - [Pull Requests](#pull-requests)

## Code of Conduct

This project and everyone who participates in it is governed by the project's [Code of Conduct](https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/blob/main/CODE_OF_CONDUCT.md).

By participating, you are expected to uphold this code. Please report unacceptable behavior to the project maintainer(s).

## Asking Questions

Before asking a question, it is best to search for existing [Issues](https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/issues) or [Discussions](https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/discussions) that may help. In case you've found a suitable issue or discussion and still need clarification, you may comment directly on the issue or discussion. It is also advisable to search the internet for answers.

If you then still have a question or need clarification, you may create a [new discussion](https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/discussions/new) or [new issue](https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/issues/new) and provide as much context and detail as possible.

## How To Contribute

> ### Legal Notice
> When contributing to this project, you must agree that you have authored 100% of the content, that you have the necessary rights to the content, and that the content you contribute may be provided under the project license.

### Reporting Bugs and Errors

We use GitHub issues to track bugs and errors. If you run into an issue with the project, please create a [new issue](https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/issues/new) with the following in consideration:

- Explain the behavior you would expect and the actual behavior.
- Please provide as much context as possible and describe the *reproduction steps* that someone else can follow to recreate the issue on their own. This usually includes your code. If possible, please isolate the problem and create a test case.

Once you have submitted an issue on GitHub, we will label it and follow-up accordingly.

### Suggesting Features or Enhancements

We use GitHub issues to track feature and enhancement requests. If you'd like to suggest a feature or enhancement, please create a [new issue](https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/issues/new) with the following in consideration:

- Make sure that you are using the latest version.
- Make sure that you are using the latest Ewon firmware.
  - Check for updates at [https://www.ewon.biz/technical-support/pages/firmware](https://www.ewon.biz/technical-support/pages/firmware).
- Read the [README.md](https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/blob/main/README.md) carefully to find out if the functionality is already covered.
- Perform a search for existing [Issues](https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/issues) or [Discussions](https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/discussions) to see if the feature or enhancement has already been suggested.
  - If the feature or enhancement has already been suggested, add a comment to the existing issue or discussion instead of opening a new one.
- Make sure the feature or enhancement is with the scope and aims of the project. New features shall be useful to the majority of users, not a small subset.
  - If a feature or enhancement targets a minority of users, it is advised to consider writing an add-on or library.

### Making Modifications or Updates

The development environment for this project uses the standard Ewon Java development environment, which is [Eclipse](https://www.eclipse.org/). Alternative IDEs can be used but may not be fully compatible with the Ewon Java Toolkit and thus are not guaranteed nor officially supported by HMS.

Documentation and additional information about the Ewon Java development environment is available in the Ewon Java Toolkit User Guide \(J2SE\) at [https://developer.ewon.biz/content/java-0#dev-documents](https://developer.ewon.biz/content/java-0#dev-documents).

#### Documentation

Changes made to the documentation in this project must be made in an individual commit, or accompany the commit with the relevant source code changes.

Documentation for this project shall be written in present passive voice and is governed by the project's [Code of Conduct](https://github.com/hms-networks/sc-ewon-flexy-extensions-lib/blob/main/CODE_OF_CONDUCT.md).

#### Source Code

Changes made to the source code in this project must be organized into individual commits, each with an individual objective.

Source code in this project adheres to the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html), and any changes must follow the same format. Detailed information about the Google Java Style Guide can be found at [https://google.github.io/styleguide/javaguide.html](https://google.github.io/styleguide/javaguide.html).

Source code changes which do not adhere to the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) will be rejected automatically during the pull request phase.

### Committing and Merging Modifications and Updates

#### Commit Scope

Each commit to this project should have very limited scope. All changes in each commit should have a common theme, and you should be able to describe the scope of the change as a single statement. _Hint: If you can not describe the commit without using the word "and" or creating a list, the commit should be broken up._

To ensure a clean and productive Git history, commits which do not follow those guidelines should be reorganized, otherwise they may be rejected during the pull request phase.

#### Commit Messages

Each commit must contain a message describing the change(s) made. The commit message must meet the following guidelines:

1. Subject (First Line)
   1. Must not be more than 50 characters in length
   2. Must describe commit change-set concisely
   3. Must capitalize first character
   4. Must not end with a period
   5. Must be followed by a blank line (unless body omitted)
2. Body
   1. Must not be more than 72 characters in length, per line
   2. Must describe commit change-set thoroughly
   3. Must use proper capitalization, grammar, and punctuation
   4. Must use blank lines to separate paragraphs

```
Subject line

More detailed explanatory text, if necessary. Wrap to 72 characters
or less. The blank line between the subject line and commit body is
critical (unless commit body is omitted).

Additional paragraphs can be added after another blank line. Bullet
points and other rich text can be included as well.

1. Example Change
2. Example Addition
3. Example Bug Fix
```

#### Pull Requests

All code committed to this project must follow the pull request procedure before it can be merged to the main or master branch. The pull request procedure ensures that all changes have been reviewed by a project maintainer, and tested to function properly.

In addition to any automated testing which may be performed for pull requests, all modified or newly-introduced code must be thoroughly tested prior to creating a pull request.

Once a pull request has been created, the following must be performed:

1. Designate pull request assignee
   - Under most circumstances, the person creating the pull request shall be designated as the assignee. Should another person become responsible for the pull request, that person shall be designated as the assignee.
2. Add content labels
   - Pull requests and other GitHub content can be labeled to document the type of content addressed. These labels can be found and assigned in the pull request sidebar.
3. Add linked issues
   - If the pull request resolves or otherwise addresses an issue documented using GitHub Issues, the issue should be linked to the pull request. Linked issues can be found and assigned in the pull request sidebar.
4. Add pull request reviewers
   - Pull requests must be reviewed by at least one, but preferably two, reviewers or code-owners. These reviewers should be chosen based on project familiarity, code language, and dependencies. In some scenarios, a code-owner may automatically be assigned to a pull request.