# Contributing to ChatSphere

## What should I know before I get started?

### ChatSphere Client-Server Architecture

ChatSphere uses a Progressive Web Application (PWA) created with VueJS as frontend. This communicates via a GraphQL API with a Java server that uses a MySQL database.

## How Can I Contribute?

### Reporting Bugs

This section guides you through submitting a bug report. Following these guidelines helps maintainers and the community understand your report :pencil:, reproduce the behavior :computer: :computer:, and find related reports :mag_right:.

When you are creating a bug report, please include as many details as possible.

> **Note:** If you find a **Closed** issue that seems like it is the same thing that you're experiencing, open a new issue and include a link to the original issue in the body of your new one.

#### How Do I Submit A (Good) Bug Report?

Bugs are tracked as *GitLab issues*.

Explain the problem and include additional details to help maintainers reproduce the problem:

* **Use a clear and descriptive title** for the issue to identify the problem.
* **Describe the exact steps which reproduce the problem** in as many details as possible. For example, start by explaining how you started the application, e.g. which command exactly you used in the terminal, or how you started the application otherwise. When listing steps, **don't just say what you did, but explain how you did it**. For example, if you moved the cursor to the end of a line, explain if you used the mouse, or a keyboard shortcut or an application command, and if so which one?
* **Provide specific examples to demonstrate the steps**. Include links to files or GitHub projects, or copy/pasteable snippets, which you use in those examples. If you're providing snippets in the issue, use [Markdown code blocks](https://help.github.com/articles/markdown-basics/#multiple-lines).
* **Describe the behavior you observed after following the steps** and point out what exactly is the problem with that behavior.
* **Explain which behavior you expected to see instead and why.**
* **Include screenshots and animated GIFs** which show you following the described steps and clearly demonstrate the problem. You can use [this tool](https://www.cockos.com/licecap/) to record GIFs on macOS and Windows, and [this tool](https://github.com/colinkeenan/silentcast) or [this tool](https://github.com/GNOME/byzanz) on Linux.
* **If you're reporting a crash**, include a crash report with a stack trace from the operating system. Include the crash report in the issue in a [code block](https://help.github.com/articles/markdown-basics/#multiple-lines), a [file attachment](https://help.github.com/articles/file-attachments-on-issues-and-pull-requests/), or put it in a [gist](https://gist.github.com/) and provide link to that gist.
* **If the problem wasn't triggered by a specific action**, describe what you were doing before the problem happened and share more information using the guidelines below.

Provide more context by answering these questions:

* **Did the problem start happening recently** (e.g. after updating to a new version) or was this always a problem?
* If the problem started happening recently, **can you reproduce the problem in an older version?** What's the most recent version in which the problem doesn't happen?
* **Can you reliably reproduce the issue?** If not, provide details about how often the problem happens and under which conditions it normally happens.
* If the problem is related to working with files (e.g. opening and editing files), **does the problem happen for all files and projects or only some?** Does the problem happen only when working with local or remote files (e.g. on network drives), with files of a specific type (e.g. only JavaScript or Python files), with large files or files with very long lines, or with files in a specific encoding? Is there anything else special about the files you are using?

Include details about your configuration and environment:

* **Which version are you using?**
* **What's the name and version of the OS you're using**?
* **Are you running the application in a virtual machine?** If so, which VM software are you using and which operating systems and versions are used for the host and the guest?
* **Which keyboard layout are you using?** Are you using a US layout or some other layout?

### Suggesting Enhancements

This section guides you through submitting an enhancement suggestion, including completely new features and minor improvements to existing functionality. Following these guidelines helps maintainers and the community understand your suggestion :pencil: and find related suggestions :mag_right:.

#### How Do I Submit A (Good) Enhancement Suggestion?

Enhancement suggestions are tracked as *GitLab issues*. Create an issue on that repository and provide the following information:

* **Use a clear and descriptive title** for the issue to identify the suggestion.
* **Provide a step-by-step description of the suggested enhancement** in as many details as possible.
* **Provide specific examples to demonstrate the steps**. Include copy/pasteable snippets which you use in those examples, as [Markdown code blocks](https://help.github.com/articles/markdown-basics/#multiple-lines).
* **Describe the current behavior** and **explain which behavior you expected to see instead** and why.
* **Include screenshots and animated GIFs** which help you demonstrate the steps or point out the part of the application which the suggestion is related to. You can use [this tool](https://www.cockos.com/licecap/) to record GIFs on macOS and Windows, and [this tool](https://github.com/colinkeenan/silentcast) or [this tool](https://github.com/GNOME/byzanz) on Linux.
* **Explain why this enhancement would be useful**
* **List some other applications where this enhancement exists.**
* **Specify which version you're using.**
* **Specify the name and version of the OS you're using.**

### Your First Code Contribution

Unsure where to begin contributing? You can start by looking through these `beginner` and `help-wanted` issues:

* [Beginner issues][beginner] - issues which should only require a few lines of code, and a test or two.
* [Help wanted issues][help-wanted] - issues which should be a bit more involved than `beginner` issues.

Both issue lists are sorted by total number of comments. While not perfect, number of comments is a reasonable proxy for impact a given change will have.

### Pull Requests

* Fill in a *Pull Request Template*
* Do not include issue numbers in the PR title
* Include screenshots and animated GIFs in your pull request whenever possible.
* End all files with a newline
* Avoid platform-dependent code

## Styleguides

### Git Commit Messages

* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
* Limit the first line to 72 characters or less
* Reference issues and pull requests liberally after the first line
* When only changing documentation, include `[ci skip]` in the commit title
* Consider starting the commit message with an applicable emoji:
    * :art: `:art:` when improving the format/structure of the code
    * :racehorse: `:racehorse:` when improving performance
    * :non-potable_water: `:non-potable_water:` when plugging memory leaks
    * :memo: `:memo:` when writing docs
    * :penguin: `:penguin:` when fixing something on Linux
    * :apple: `:apple:` when fixing something on macOS
    * :checkered_flag: `:checkered_flag:` when fixing something on Windows
    * :bug: `:bug:` when fixing a bug
    * :fire: `:fire:` when removing code or files
    * :green_heart: `:green_heart:` when fixing the CI build
    * :white_check_mark: `:white_check_mark:` when adding tests
    * :lock: `:lock:` when dealing with security
    * :arrow_up: `:arrow_up:` when upgrading dependencies
    * :arrow_down: `:arrow_down:` when downgrading dependencies
    * :shirt: `:shirt:` when removing linter warnings

### JavaScript Styleguide

All JavaScript must adhere to [JavaScript Standard Style](https://standardjs.com/).

* Prefer the object spread operator (`{...anotherObj}`) to `Object.assign()`
* Inline `export`s with expressions whenever possible
  ```js
  // Use this:
  export default class ClassName {

  }

  // Instead of:
  class ClassName {

  }
  export default ClassName
  ```


### Documentation Styleguide

* Use [Markdown](https://daringfireball.net/projects/markdown).
* Reference methods and classes in markdown with the custom `{}` notation:
    * Reference classes with `{ClassName}`
    * Reference instance methods with `{ClassName::methodName}`
    * Reference class methods with `{ClassName.methodName}`

#### Example

```coffee
# Public: Disable the package with the given name.
#
# * `name`    The {String} name of the package to disable.
# * `options` (optional) The {Object} with disable options (default: {}):
#   * `trackTime`     A {Boolean}, `true` to track the amount of time taken.
#   * `ignoreErrors`  A {Boolean}, `true` to catch and ignore errors thrown.
# * `callback` The {Function} to call after the package has been disabled.
#
# Returns `undefined`.
disablePackage: (name, options, callback) ->
```

## Additional Notes

### Issue and Pull Request Labels

This section lists the labels we use to help us track and manage issues and pull requests. Most labels are used across all repositories.

The labels are loosely grouped by their purpose, but it's not required that every issue have a label from every group or that an issue can't have more than one label from the same group.

#### Type of Issue and Issue State

| Label name | Description |
| --- | --- | 
| `enhancement` | Feature requests. |
| `bug` | Confirmed bugs or reports that are very likely to be bugs. |
| `question` | Questions more than bug reports or feature requests (e.g. how do I do X). |
| `feedback` | General feedback more than bug reports or feature requests. |
| `help-wanted` | The core team would appreciate help from the community in resolving these issues. |
| `beginner` | Less complex issues which would be good first issues to work on for users who want to contribute. |
| `more-information-needed` | More information needs to be collected about these problems or feature requests (e.g. steps to reproduce). |
| `needs-reproduction` | Likely bugs, but haven't been reliably reproduced. |
| `blocked` | Issues blocked on other issues. |
| `duplicate` | Issues which are duplicates of other issues, i.e. they have been reported before. |
| `wontfix` | The core team has decided not to fix these issues for now, either because they're working as intended or for some other reason. |
| `invalid` | Issues which aren't valid (e.g. user errors). |
| `package-idea` |  Feature request which might be good candidates for new package. |
| `wrong-repo` | Issues reported on the wrong repository. |

#### Topic Categories

| Label name | Description |
| --- | --- | 
| `windows` |  Related to ChatSphere running on Windows. |
| `linux` | Related to ChatSphere running on Linux. |
| `mac` | Related to ChatSphere running on macOS. |
| `documentation` | Related to any type of documentation (e.g. API documentation)). |
| `performance` |  Related to performance. |
| `security` | Related to security. |
| `ui` | Related to visual design. |
| `api` | Related to ChatSphere's public APIs. |
| `uncaught-exception` |  Issues about uncaught exceptions. |
| `crash` | Reports of ChatSphere completely crashing. |
| `auto-indent` | Related to auto-indenting text. |
| `encoding` |Related to character encoding. |
| `network` | Related to network problems or working with remote files (e.g. on network drives). |
| `git` | Related to Git functionality (e.g. problems with gitignore files or with showing the correct file status). |

#### Topic Categories

| Label name | Description |
| --- | --- |
| `API` | Issues related to the GraphQL API Specification or Implementation |
| `Backend` | Issues related to the Java API Server |
| `Database` |  | Issues related to the Backend Database |
| `Frontend` | Issues related to the VueJS Frontend Application Logic |
| `UI` | Issues related to the Vuetify User Interface |

#### Pull Request Labels

| Label name | Description
| --- | --- |
| `work-in-progress` | Pull requests which are still being worked on, more changes will follow. |
| `needs-review` |  Pull requests which need code review, and approval from maintainers or core team. |
| `under-review` | Pull requests being reviewed by maintainers or core team. |
| `requires-changes` |  Pull requests which need to be updated based on review comments and then reviewed again. |
| `needs-testing` |  Pull requests which need manual testing. |
